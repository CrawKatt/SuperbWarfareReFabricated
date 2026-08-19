package com.atsuishio.superbwarfare.init

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry
import net.minecraft.world.level.GameRules

object ModGameRules {
    val MOD_RULE_DO_GENERATE_LOOTS: GameRules.Key<GameRules.BooleanValue> =
        GameRuleRegistry.register(
            "sbwDoGenerateLoots",
            GameRules.Category.SPAWNING,
            GameRuleFactory.createBooleanRule(true)
        )

    fun bootstrap() {
    }
}
