package com.atsuishio.superbwarfare.init

import com.atsuishio.superbwarfare.Mod
import com.atsuishio.superbwarfare.perk.js.PerkDescriptor
import net.fabricmc.fabric.api.event.registry.DynamicRegistries
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey

object ModDatapackRegistries {
    val PERKS_KEY: ResourceKey<Registry<PerkDescriptor>> =
        ResourceKey.createRegistryKey(Mod.loc("sbw/perks"))

    fun init() {
        DynamicRegistries.registerSynced(PERKS_KEY, PerkDescriptor.CODEC)
    }
}
