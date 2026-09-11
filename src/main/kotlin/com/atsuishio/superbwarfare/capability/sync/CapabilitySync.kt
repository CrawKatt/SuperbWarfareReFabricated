package com.atsuishio.superbwarfare.capability.sync

import com.atsuishio.superbwarfare.Mod
import com.atsuishio.superbwarfare.event.custom.EntityPairingCallback
import com.atsuishio.superbwarfare.network.NetworkRegistry
import com.atsuishio.superbwarfare.network.message.receive.CapabilitySyncMessage
import com.atsuishio.superbwarfare.serialization.ByteBufDecoder
import com.atsuishio.superbwarfare.serialization.ByteBufEncoder
import io.netty.buffer.Unpooled
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.fabricmc.fabric.api.networking.v1.PlayerLookup
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.Entity

object CapabilitySync {
    private data class Entry(
        val id: ResourceLocation,
        val getter: (Entity) -> SyncedCapability?,
    )

    private val registry = LinkedHashMap<ResourceLocation, Entry>()
    private var dirty = HashMap<ResourceLocation, HashMap<Int, MutableSet<ResourceLocation>>>()
    private var eventsRegistered = false

    private val trackingTargets = setOf(SyncTarget.TRACKING)
    private val selfTargets = setOf(SyncTarget.TRACKING, SyncTarget.SELF)

    fun register(id: ResourceLocation, getter: (Entity) -> SyncedCapability?): ResourceLocation {
        registry[id] = Entry(id, getter)
        return id
    }

    fun registerEvents() {
        if (eventsRegistered) return
        eventsRegistered = true

        ServerTickEvents.END_SERVER_TICK.register { flush(it) }
        ServerPlayConnectionEvents.JOIN.register { handler, _, _ -> pushSelf(handler.player) }
        ServerPlayerEvents.AFTER_RESPAWN.register { _, player, _ -> pushSelf(player) }
        ServerEntityWorldChangeEvents.AFTER_PLAYER_CHANGE_WORLD.register { player, _, _ -> pushSelf(player) }
        EntityPairingCallback.EVENT.register { entity, player ->
            pushAll(entity, player, trackingTargets)
        }
    }

    fun markDirty(entity: Entity?, id: ResourceLocation) {
        if (entity == null || entity.level().isClientSide || id !in registry) return

        dirty.getOrPut(entity.level().dimension().location()) { HashMap() }
            .getOrPut(entity.id) { HashSet() }
            .add(id)
    }

    fun apply(entity: Entity, id: ResourceLocation, data: ByteArray, full: Boolean) {
        val capability = registry[id]?.getter?.invoke(entity) ?: return
        val buffer = FriendlyByteBuf(Unpooled.wrappedBuffer(data))
        try {
            capability.readSync(ByteBufDecoder(buffer), full)
        } finally {
            buffer.release()
        }
    }

    private fun encode(capability: SyncedCapability, full: Boolean): ByteArray {
        val buffer = FriendlyByteBuf(Unpooled.buffer())
        return try {
            capability.writeSync(ByteBufEncoder(buffer), full)
            ByteArray(buffer.readableBytes()).also(buffer::readBytes)
        } finally {
            buffer.release()
        }
    }

    private fun flush(server: MinecraftServer) {
        if (dirty.isEmpty()) return

        val snapshot = dirty
        dirty = HashMap()

        for ((dimension, entities) in snapshot) {
            val level = server.allLevels.firstOrNull { it.dimension().location() == dimension } ?: continue
            for ((entityId, capabilityIds) in entities) {
                level.getEntity(entityId)?.let { send(it, capabilityIds, false) }
            }
        }
    }

    private fun send(entity: Entity, capabilityIds: Collection<ResourceLocation>, full: Boolean) {
        val grouped = LinkedHashMap<SyncTarget, MutableList<CapabilitySyncMessage.Entry>>()

        for (id in capabilityIds) {
            val entry = registry[id] ?: continue
            val capability = entry.getter(entity) ?: continue
            grouped.getOrPut(capability.syncTarget) { ArrayList() }
                .add(CapabilitySyncMessage.Entry(id, encode(capability, full)))
        }

        for ((target, entries) in grouped) {
            val message = CapabilitySyncMessage(entity.id, full, entries)
            when (target) {
                SyncTarget.TRACKING -> {
                    for (player in PlayerLookup.tracking(entity)) {
                        NetworkRegistry.sendToPlayer(player, message)
                    }
                    if (entity is ServerPlayer) NetworkRegistry.sendToPlayer(entity, message)
                }
                SyncTarget.SELF -> if (entity is ServerPlayer) {
                    NetworkRegistry.sendToPlayer(entity, message)
                }
            }
        }
    }

    private fun pushAll(entity: Entity, player: ServerPlayer, targets: Set<SyncTarget>) {
        val entries = ArrayList<CapabilitySyncMessage.Entry>(registry.size)

        for (entry in registry.values) {
            val capability = entry.getter(entity) ?: continue
            if (capability.syncTarget in targets) {
                entries.add(CapabilitySyncMessage.Entry(entry.id, encode(capability, true)))
            }
        }

        if (entries.isNotEmpty()) {
            NetworkRegistry.sendToPlayer(player, CapabilitySyncMessage(entity.id, true, entries))
        }
    }

    private fun pushSelf(entity: Entity) {
        val player = entity as? ServerPlayer ?: return
        pushAll(player, player, selfTargets)
    }
}
