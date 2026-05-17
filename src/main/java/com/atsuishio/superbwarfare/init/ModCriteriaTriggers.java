package com.atsuishio.superbwarfare.init;

import com.atsuishio.superbwarfare.Mod;
import com.atsuishio.superbwarfare.advancement.criteria.OttoSprintTrigger;
import com.atsuishio.superbwarfare.advancement.criteria.RPGMeleeExplosionTrigger;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;

public class ModCriteriaTriggers {

    public static final RPGMeleeExplosionTrigger RPG_MELEE_EXPLOSION = register("rpg_melee_explosion", new RPGMeleeExplosionTrigger());
    public static final OttoSprintTrigger OTTO_SPRINT = register("otto_sprint", new OttoSprintTrigger());

    private static <T extends CriterionTrigger<?>> T register(String name, T trigger) {
        return Registry.register(BuiltInRegistries.TRIGGER_TYPES, Mod.loc(name), trigger);
    }

    public static void init() {
    }
}
