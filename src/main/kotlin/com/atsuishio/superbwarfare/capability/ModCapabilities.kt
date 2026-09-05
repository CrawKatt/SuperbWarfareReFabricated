package com.atsuishio.superbwarfare.capability

import com.atsuishio.superbwarfare.capability.entity.InfinityAmmoCapability
import com.atsuishio.superbwarfare.capability.living.PhosphorusFireCapability
import com.atsuishio.superbwarfare.capability.player.PlayerVariable
import dev.onyxstudios.cca.api.v3.component.ComponentKey
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity

class ModCapabilities : EntityComponentInitializer {

    override fun registerEntityComponentFactories(registry: EntityComponentFactoryRegistry) {
        registry.registerForPlayers(
            PLAYER_VARIABLE,
            { PlayerVariable() },
            RespawnCopyStrategy.ALWAYS_COPY
        )
        registry.registerFor(LivingEntity::class.java, PHOSPHORUS_FIRE) { PhosphorusFireCapability() }
        registry.registerFor(Entity::class.java, INFINITY_AMMO) { InfinityAmmoCapability() }
    }

    companion object {
        @JvmField
        val PLAYER_VARIABLE: ComponentKey<PlayerVariable> = ComponentRegistry.getOrCreate(
            PlayerVariable.ID,
            PlayerVariable::class.java
        )

        @JvmField
        val PHOSPHORUS_FIRE: ComponentKey<PhosphorusFireCapability> = ComponentRegistry.getOrCreate(
            PhosphorusFireCapability.ID,
            PhosphorusFireCapability::class.java
        )

        @JvmField
        val INFINITY_AMMO: ComponentKey<InfinityAmmoCapability> = ComponentRegistry.getOrCreate(
            InfinityAmmoCapability.ID,
            InfinityAmmoCapability::class.java
        )
    }
}
