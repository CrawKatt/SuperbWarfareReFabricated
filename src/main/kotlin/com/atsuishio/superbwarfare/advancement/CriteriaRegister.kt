package com.atsuishio.superbwarfare.advancement

import com.atsuishio.superbwarfare.advancement.criteria.OttoSprintTrigger
import com.atsuishio.superbwarfare.advancement.criteria.RPGMeleeExplosionTrigger
import net.minecraft.advancements.CriteriaTriggers
import net.minecraft.advancements.critereon.SimpleCriterionTrigger

object CriteriaRegister {
    lateinit var RPG_MELEE_EXPLOSION: RPGMeleeExplosionTrigger

    lateinit var OTTO_SPRINT: OttoSprintTrigger

    @JvmStatic
    fun setup() {
        RPG_MELEE_EXPLOSION = register(RPGMeleeExplosionTrigger())
        OTTO_SPRINT = register(OttoSprintTrigger())
    }

    fun <T : SimpleCriterionTrigger<*>> register(criterion: T): T {
        CriteriaTriggers.register(criterion)
        return criterion
    }
}
