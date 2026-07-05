package com.atsuishio.superbwarfare.world.saveddata

import com.atsuishio.superbwarfare.config.server.ProjectileConfig
import it.unimi.dsi.fastutil.longs.Long2IntOpenHashMap
import it.unimi.dsi.fastutil.longs.LongArrayFIFOQueue
import it.unimi.dsi.fastutil.longs.LongOpenHashSet
import it.unimi.dsi.fastutil.longs.LongSet
import net.minecraft.core.BlockPos
import net.minecraft.core.SectionPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.TicketType
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.level.chunk.ChunkStatus
import net.minecraft.world.level.chunk.LevelChunk
import net.minecraft.world.level.chunk.ProtoChunk
import net.minecraft.world.level.saveddata.SavedData
import net.minecraftforge.event.TickEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod

/**
 * Per-dimension chunk manager that force-loads chunks for fast-moving projectiles.
 *
 * Inspired by Create Big Cannons / Ritchie's Projectile Library's ChunkManager.
 * Uses a queue-based approach where projectiles enqueue their current chunk each tick,
 * and this manager processes the queue at [net.minecraftforge.event.TickEvent.LevelTickEvent], force-loading chunks
 * via [ServerLevel.getChunkSource().updateChunkForced] and aging them out with a
 * configurable TTL.
 *
 * @param chunks the persisted set of chunk positions to keep loaded (survives server restart)
 */
class ProjectileChunkSavedData private constructor(private val chunks: LongOpenHashSet) : SavedData() {

    /** Pending chunks to process (not persisted) */
    private val queue: LongArrayFIFOQueue = LongArrayFIFOQueue()

    /** Dedup set for the queue (not persisted) */
    private val inQueue: LongOpenHashSet = LongOpenHashSet(chunks)

    /** Actively force-loaded chunks -> remaining tick TTL. -1 = pending first aging pass. (not persisted) */
    private val loaded: Long2IntOpenHashMap = Long2IntOpenHashMap()

    init {
        // Enqueue all persisted chunks for loading
        val iter = chunks.iterator()
        while (iter.hasNext()) {
            queue.enqueue(iter.nextLong())
        }
    }

    // ──────────────────────────────────────────────
    //  NBT Persistence
    // ──────────────────────────────────────────────

    override fun save(tag: CompoundTag): CompoundTag {
        tag.putLongArray(KEY_LOADED_CHUNKS, chunks.toLongArray())
        return tag
    }

    // ──────────────────────────────────────────────
    //  Public API
    // ──────────────────────────────────────────────

    /**
     * Enqueue a chunk for force-loading. Idempotent: if already queued or loaded, does nothing.
     */
    fun queueForceLoad(chunkPos: ChunkPos) {
        val chunkLong = chunkPos.toLong()
        if (inQueue.add(chunkLong)) {
            queue.enqueue(chunkLong)
            chunks.add(chunkLong)
            isDirty = true
        }
    }

    /**
     * Called at [net.minecraftforge.event.TickEvent.LevelTickEvent] END phase.
     * Ages existing forced chunks, processes the pending queue, and releases expired chunks.
     */
    fun tick(level: ServerLevel) {
        val forcedChunks: LongSet = level.forcedChunks

        val maxForceLoaded = ProjectileConfig.PROJECTILE_MAX_CHUNKS_FORCE_LOADED.get().let {
            if (it <= 0) Int.MAX_VALUE else it
        }
        val maxEachTick = ProjectileConfig.PROJECTILE_MAX_CHUNKS_LOADED_EACH_TICK.get()
        val chunkAge = ProjectileConfig.PROJECTILE_CHUNK_AGE.get()
        val entityLoadTimeout = -chunkAge - 1

        // ── Phase 1: Age existing loaded chunks ──
        val toRemove = LongOpenHashSet()
        val entryIter = loaded.long2IntEntrySet().fastIterator()
        while (entryIter.hasNext()) {
            val entry = entryIter.next()
            val chunkLong = entry.longKey
            var value = entry.intValue

            // Chunk was just loaded (TTL = -1): check if entities are active in it
            if (value <= -1) {
                val chunkPos = ChunkPos(chunkLong)
                val blockPos = BlockPos(
                    SectionPos.sectionToBlockCoord(chunkPos.x),
                    0,
                    SectionPos.sectionToBlockCoord(chunkPos.z)
                )
                if (level.isPositionEntityTicking(blockPos)) {
                    value = chunkAge // Reset TTL — entities are still here
                }
            }

            val newValue = value - 1
            entry.setValue(newValue)

            // Remove if TTL reached 0 (counted down from positive) or exceeded entityLoadTimeout
            if (newValue == 0 || newValue <= entityLoadTimeout) {
                toRemove.add(chunkLong)
            }
        }

        // ── Phase 2: Process pending queue ──
        val removalCount = toRemove.size
        val capacity = maxForceLoaded - loaded.size + removalCount
        val qSize = queue.size()
        val toLoad = minOf(maxEachTick, capacity.coerceAtLeast(0), qSize)

        repeat(toLoad) {
            if (queue.isEmpty) return@repeat

            val chunkLong = queue.dequeueLong()
            inQueue.remove(chunkLong)

            val chunkPos = ChunkPos(chunkLong)

            // Already loaded with positive TTL — just refresh
            if (loaded.containsKey(chunkLong) && loaded[chunkLong] > -1) {
                loaded.put(chunkLong, chunkAge)
                toRemove.remove(chunkLong)
                return@repeat
            }

            // Try loading without generating new terrain
            if (!forcedChunks.contains(chunkLong) && loadChunkNoGenerate(level, chunkPos)) {
                loaded.put(chunkLong, -1)
                level.chunkSource.updateChunkForced(chunkPos, true)
            } else {
                // Couldn't load — remove from persistent set
                chunks.remove(chunkLong)
            }
        }

        // ── Phase 3: Release expired chunks ──
        val removalIter = toRemove.iterator()
        while (removalIter.hasNext()) {
            val chunkLong = removalIter.nextLong()
            val chunkPos = ChunkPos(chunkLong)
            level.chunkSource.updateChunkForced(chunkPos, false)
            loaded.remove(chunkLong)

            // Only remove from persistent set if not re-queued
            if (!inQueue.contains(chunkLong)) {
                chunks.remove(chunkLong)
            }
        }

        // ── Phase 4: Trim and mark dirty ──
        loaded.trim()
        inQueue.trim()
        queue.trim()
        chunks.trim()
        isDirty = true
    }

    // ──────────────────────────────────────────────
    //  Internal
    // ──────────────────────────────────────────────

    /**
     * Try to load a chunk without generating new terrain.
     * Returns true only if the chunk already exists as a [LevelChunk].
     */
    private fun loadChunkNoGenerate(level: ServerLevel, chunkPos: ChunkPos): Boolean {
        val source = level.chunkSource

        // Fast path: already loaded
        val existing = source.getChunkNow(chunkPos.x, chunkPos.z)
        if (existing != null) return true

        // Slow path: try loading at EMPTY status
        var access = source.getChunk(chunkPos.x, chunkPos.z, ChunkStatus.EMPTY, true)
        if (access is ProtoChunk) {
            // Cancel any region-generation ticket this may have triggered
            source.removeRegionTicket(TicketType.UNKNOWN, chunkPos, -11, chunkPos)
            // Re-fetch at FULL status without generation
            access = source.getChunk(chunkPos.x, chunkPos.z, ChunkStatus.FULL, true)
        }

        return access is LevelChunk
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
    companion object {
        private const val FILE_ID = "superbwarfare_projectile_chunks"
        private const val KEY_LOADED_CHUNKS = "LoadedChunks"

        /**
         * Get or create the per-dimension [ProjectileChunkSavedData].
         */
        @JvmStatic
        fun get(level: ServerLevel): ProjectileChunkSavedData {
            return level.dataStorage.computeIfAbsent(
                { tag -> load(tag) },
                { ProjectileChunkSavedData(LongOpenHashSet()) },
                FILE_ID
            )
        }

        /**
         * Convenience: enqueue a chunk for force-loading on the given level.
         */
        @JvmStatic
        fun queueForceLoad(level: ServerLevel, chunkPos: ChunkPos) {
            get(level).queueForceLoad(chunkPos)
        }

        /**
         * Convenience: run the tick processing for the given level.
         */
        @JvmStatic
        fun tickLevel(level: ServerLevel) {
            get(level).tick(level)
        }

        private fun load(tag: CompoundTag): ProjectileChunkSavedData {
            val arr = tag.getLongArray(KEY_LOADED_CHUNKS)
            val set = LongOpenHashSet(arr)
            return ProjectileChunkSavedData(set)
        }

        @SubscribeEvent
        fun onChunkLoadLevelTick(event: TickEvent.LevelTickEvent) {
            if (!ProjectileConfig.PROJECTILE_CHUNK_LOADING.get()) return
            if (event.phase != TickEvent.Phase.END) return
            val level = event.level as? ServerLevel ?: return
            tickLevel(level)
        }
    }
}
