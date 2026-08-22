package com.atsuishio.superbwarfare.init

import com.atsuishio.superbwarfare.Mod
import com.atsuishio.superbwarfare.advancement.criteria.OttoSprintTrigger
import com.atsuishio.superbwarfare.advancement.criteria.RPGMeleeExplosionTrigger
import com.atsuishio.superbwarfare.advancement.criteria.VehicleHurtTrigger
import net.minecraft.advancements.CriterionTrigger
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries

object ModCriteriaTriggers {
    @JvmField
    val RPG_MELEE_EXPLOSION: RPGMeleeExplosionTrigger = register("rpg_melee_explosion", RPGMeleeExplosionTrigger())!!

    @JvmField
    val OTTO_SPRINT: OttoSprintTrigger = register("otto_sprint", OttoSprintTrigger())!!

    @JvmField
    val VEHICLE_HURT: VehicleHurtTrigger = register("vehicle_hurt", VehicleHurtTrigger())!!

    private fun <T : CriterionTrigger<*>?> register(name: String?, trigger: T?): T? {
        return Registry.register<CriterionTrigger<*>?, T?>(BuiltInRegistries.TRIGGER_TYPES, Mod.loc(name!!), trigger)
    }

    @JvmStatic
    fun init() {
    }
}
