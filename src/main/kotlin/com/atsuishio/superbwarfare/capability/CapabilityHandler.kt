package com.atsuishio.superbwarfare.capability

import com.atsuishio.superbwarfare.capability.player.PlayerVariable
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents
import net.minecraft.server.level.ServerPlayer

class CapabilityHandler : EntityComponentInitializer {

    override fun registerEntityComponentFactories(registry: EntityComponentFactoryRegistry) {
        registerCapabilities(registry)
    }

    /**
     * 按 [ModCapabilities.ALL] 中声明的挂载目标批量挂载。
     *
     * 新增 capability 不需要动这里，只要在 [ModCapabilities] 里登记。
     */
    fun registerCapabilities(registry: EntityComponentFactoryRegistry) {
        for (capability in ModCapabilities.ALL) {
            capability.registerComponents(registry)
        }

        ServerPlayerEvents.COPY_FROM.register { original, player, _ -> clonePlayer(original, player) }
    }

    fun clonePlayer(oldPlayer: ServerPlayer, player: ServerPlayer) {
        if (player.level().isClientSide) return

        val original = PlayerVariable.getOrDefault(oldPlayer)

        // 复制发生在 PlayerList#respawn 的初始同步之前，客户端会收到复制后的数据；
        // 而重生后客户端不会重建本地玩家实体，所以还要显式标记一次，让框架重推。
        ModCapabilities.PLAYER_VARIABLE.set(player, original.copy())
        ModCapabilities.PLAYER_VARIABLE.markDirty(player)
    }
}
