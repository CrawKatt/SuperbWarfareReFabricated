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
        ModCapabilities.PLAYER_VARIABLE.set(player, original.copy())
        ModCapabilities.PLAYER_VARIABLE.markDirty(player)

        ModCapabilities.INFINITE_AMMO.set(player, ModCapabilities.INFINITE_AMMO.get(oldPlayer))
        ModCapabilities.INFINITE_AMMO.markDirty(player)
    }
}
