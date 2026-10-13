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

/**
 * Capability 自动同步框架。
 *
 * Forge 的 Capability 系统只负责持久化（`ICapabilitySerializable` + `AttachCapabilitiesEvent`）
 * 与玩家重生时的数据搬运，**不会**把数据发给客户端，官方要求各 mod 自己用包同步，
 * 并且明确列出三个必须自己处理同步的时机：实体生成、数据变化、新客户端开始观察。
 *
 * 本对象把这三件事一次性地接过来：
 *
 * 1. **何时同步**：[markDirty] 只打脏标记，真正的发包统一在服务端 tick 结束时由 [flush] 完成，
 *    同一 tick 内的多次修改会合并成一个包，同一实体上的多个 capability 也会合并。
 * 2. **向谁同步**：由 [SyncedCapability.syncTarget] 决定，收件人交给 `PacketDistributor`
 *    与实体 tracking 集合解析，不再在每个调用点手写 `sendPacketToTrackingThis`。
 * 3. **全量补发**：`StartTracking`（新观察者）、玩家登录/重生/换维度时自动推送完整数据。
 *    玩家自己的实体不在自己的 tracking 集合里，且客户端在重生/换维度时不会重建本地玩家实体，
 *    因此这几处必须显式补发，否则客户端会一直留着旧值。
 *
 * 新增一个自动同步的 capability 只需要在 `ModCapabilities` 里登记一行：挂载、存档读写与
 * 这里的登记都会自动完成；数据由 `CapabilityWrapper` 用 kotlinx 序列化器编解码，
 * 框架只负责"什么时候发、发给谁、怎么打包"。
 *
 * 线程约束：注册表与脏集合只在服务端主线程读写（tick、命令、物品交互、效果事件都在主线程）。
 */
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
