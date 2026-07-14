package com.atsuishio.superbwarfare.tools

import com.atsuishio.superbwarfare.config.server.SyncConfig
import com.atsuishio.superbwarfare.config.server.VehicleConfig
import com.atsuishio.superbwarfare.entity.projectile.MissileProjectile
import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity
import com.atsuishio.superbwarfare.network.message.receive.BeyondVisualEntitySyncMessage
import com.atsuishio.superbwarfare.tools.ServerSyncedEntityHandler.cleanAll
import com.atsuishio.superbwarfare.tools.ServerSyncedEntityHandler.getEntries
import com.atsuishio.superbwarfare.tools.ServerSyncedEntityHandler.register
import net.minecraft.nbt.CompoundTag
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.MinecraftServer
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.levelgen.Heightmap
import net.minecraft.world.phys.Vec3
import net.minecraftforge.event.TickEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.registries.ForgeRegistries
import java.util.concurrent.ConcurrentHashMap

/**
 * 服务端同步实体处理器 —— ClientSyncedEntityHandler 的服务端镜像。
 *
 * 载具和导弹每 tick 主动调用 [register] 将自身加入此列表。
 * 雷达/IFF 等消费者调用 [getEntries] 从此列表查询，避免遍历 level.allEntities。
 * 定期调用 [cleanAll] 清理已消失实体的过期条目。
 */
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
object ServerSyncedEntityHandler {

    data class Entry(
        val entityId: Int,
        val pos: Vec3,
        val eyePos: Vec3,
        val yRot: Float,
        val xRot: Float,
        val entityType: ResourceLocation,
        val nbt: CompoundTag,
        /** 实体注册/更新时间戳（系统时间 ms），用于 NBT 序列化间隔判定和过期清理，不受服务器重启影响 */
        val timeStamp: Long,
        val targetPos: Vec3?,
        /** 隐身减益系数，非载具实体为 1.0 */
        val trackDistanceMultiply: Double,
        /** 实体离地高度 */
        val heightAboveGround: Double,
    )

    // dim string → entityId → Entry
    private val entities = ConcurrentHashMap<String, ConcurrentHashMap<Int, Entry>>()

    /** 等待在下一 tick 广播中发送 removed=true 通知给客户端的实体集合 */
    private val pendingRemovals = ConcurrentHashMap<String, MutableSet<Pair<Int, ResourceLocation>>>()

    /**
     * 注册或更新实体。每 tick 由 VehicleEntity / MissileProjectile / IffItem 调用。
     * NBT 每 tick 重新序列化，保证 [BeyondVisualEntitySyncMessage] 携带最新实体状态。
     */
    @JvmStatic
    @JvmOverloads
    fun register(entity: Entity, targetPos: Vec3? = null) {
        if (!SyncConfig.SYNC_ENTITY_OVER_RANGE.get()) return
        val level = entity.level()
        if (level.isClientSide) return
        level.server ?: return
        if (entity !is VehicleEntity && entity !is MissileProjectile && entity !is Player
            && entity !is LivingEntity && !VehicleConfig.inScanList(entity.type)) return

        val dim = level.dimension().location().toString()
        val now = System.currentTimeMillis()

        val nbt = entity.getBvrSyncNbt()

        val td = if (entity is VehicleEntity)
            entity.computed().trackDistanceMultiply else 1.0
        val hag = computeHeightAboveGround(entity)

        val entry = Entry(
            entityId = entity.id,
            pos = entity.position(),
            eyePos = entity.eyePosition,
            yRot = entity.yRot,
            xRot = entity.xRot,
            entityType = ForgeRegistries.ENTITY_TYPES.getKey(entity.type) ?: return,
            nbt = nbt,
            timeStamp = now,
            targetPos = targetPos,
            trackDistanceMultiply = td,
            heightAboveGround = hag,
        )

        entities.getOrPut(dim) { ConcurrentHashMap() }[entity.id] = entry
    }

    @JvmStatic
    fun unregister(entity: Entity) {
        if (entity.level().isClientSide) return
        val dim = entity.level().dimension().location().toString()
        entities[dim]?.remove(entity.id)
        // 记录待移除实体，下一 tick 广播时通知客户端立即清理
        val entityType = ForgeRegistries.ENTITY_TYPES.getKey(entity.type) ?: return
        pendingRemovals.getOrPut(dim) { ConcurrentHashMap.newKeySet() }.add(Pair(entity.id, entityType))
    }

    @JvmStatic
    fun getEntries(dim: ResourceLocation): Collection<Entry> {
        return entities[dim.toString()]?.values ?: emptyList()
    }

    /** 计算实体离地高度（使用高度图，高效） */
    private fun computeHeightAboveGround(entity: Entity): Double {
        val level = entity.level()
        val surfaceY = level.getHeight(
            Heightmap.Types.WORLD_SURFACE,
            entity.blockX,
            entity.blockZ
        )
        return (entity.y - surfaceY).coerceAtLeast(0.0)
    }

    /** 判定实体是否在地下（实体顶部低于地表） */
    @JvmStatic
    fun isUnderground(entity: Entity): Boolean {
        val level = entity.level()
        val surfaceY = level.getHeight(
            Heightmap.Types.WORLD_SURFACE,
            entity.blockX,
            entity.blockZ
        )
        return entity.y + entity.bbHeight < surfaceY
    }

    /**
     * 清理已消失实体的过期条目
     */
    @JvmStatic
    fun cleanAll(server: MinecraftServer) {
        val now = System.currentTimeMillis()
        for (dimLevel in server.allLevels) {
            val dimKey = dimLevel.dimension().location().toString()
            val dimEntries = entities[dimKey] ?: continue
            val toRemove = dimEntries.values.filter { entry ->
                dimLevel.getEntity(entry.entityId) == null && now - entry.timeStamp > SyncConfig.SERVER_SYNC_EXPIRE_TIME.get()
            }
            if (toRemove.isNotEmpty()) {
                dimEntries.values.removeAll(toRemove.toSet())
                // 过期清理时也通知客户端移除
                val pending = pendingRemovals.getOrPut(dimKey) { ConcurrentHashMap.newKeySet() }
                for (entry in toRemove) {
                    pending.add(Pair(entry.entityId, entry.entityType))
                }
            }
        }
    }

    @SubscribeEvent
    fun tick(event: TickEvent.ServerTickEvent) {
        if (event.phase == TickEvent.Phase.START) return
        val server = event.server
        if (server.tickCount % SyncConfig.SERVER_SYNC_CLEAN_INTERVAL.get() == 0) {
            cleanAll(server)
        }
        broadcastWorldRender(server)
    }

    /**
     * 将 [ServerSyncedEntityHandler] 中所有实体无条件发送给同维度的每个玩家。
     * 超视距渲染不依赖雷达/IFF，所有载具和导弹都应能被看见。
     */
    private fun broadcastWorldRender(server: MinecraftServer) {
        for (dimLevel in server.allLevels) {
            val dim = dimLevel.dimension().location()
            val dimStr = dim.toString()
            val dimEntries = entities[dimStr] ?: continue

            // 收集待移除实体的通知（来自 unregister / cleanAll）
            val removedList = mutableListOf<BeyondVisualEntitySyncMessage.SyncedEntity>()
            val pending = pendingRemovals.remove(dimStr)
            if (pending != null) {
                for ((id, type) in pending) {
                    removedList.add(
                        BeyondVisualEntitySyncMessage.SyncedEntity(
                            id = id,
                            type = type,
                            pos = Vec3.ZERO,
                            targetPos = null,
                            tag = CompoundTag(),
                            removed = true,
                        )
                    )
                }
            }

            if (dimEntries.isEmpty() && removedList.isEmpty()) continue

            val syncedList = mutableListOf<BeyondVisualEntitySyncMessage.SyncedEntity>()
            val deadIds = mutableListOf<Int>()

            for (entry in dimEntries.values) {
                val entity = dimLevel.getEntity(entry.entityId)
                if (entity == null) {
                    // 实体已从世界中移除但条目仍在 map 中，通知客户端清理并移除条目
                    deadIds.add(entry.entityId)
                    removedList.add(
                        BeyondVisualEntitySyncMessage.SyncedEntity(
                            id = entry.entityId,
                            type = entry.entityType,
                            pos = Vec3.ZERO,
                            targetPos = null,
                            tag = CompoundTag(),
                            removed = true,
                        )
                    )
                    continue
                }
                if (entity !is VehicleEntity && entity !is MissileProjectile && entity !is LivingEntity) continue
                syncedList.add(
                    BeyondVisualEntitySyncMessage.SyncedEntity(
                        entry.entityId, entry.entityType, entry.pos, entry.targetPos, entry.nbt,
                        entry.yRot, entry.xRot,
                        heightAboveGround = entry.heightAboveGround,
                    )
                )
            }

            // 从 map 中移除已死实体条目
            for (id in deadIds) {
                dimEntries.remove(id)
            }

            if (syncedList.isNotEmpty() || removedList.isNotEmpty()) {
                for (player in dimLevel.players()) {
                    // 收集玩家乘坐链上所有载具的 ID，无需将载具同步给乘坐在其上的玩家
                    val ridingIds = mutableSetOf<Int>()
                    var riding: Entity? = player.vehicle
                    while (riding != null) {
                        ridingIds.add(riding.id)
                        riding = riding.vehicle
                    }

                    val filtered = if (ridingIds.isEmpty()) {
                        syncedList
                    } else {
                        syncedList.filter { it.id !in ridingIds }
                    }

                    if (filtered.isNotEmpty() || removedList.isNotEmpty()) {
                        sendPacketTo(player, BeyondVisualEntitySyncMessage(dim, filtered + removedList))
                    }
                }
            }
        }
    }
}
