package com.atsuishio.superbwarfare.event.custom;

import com.atsuishio.superbwarfare.Mod;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

@FunctionalInterface
public interface LivingDamagePostCallback {
    ResourceLocation LOWEST_PHASE = Mod.loc("living_damage_post_lowest");
    Event<LivingDamagePostCallback> EVENT = EventFactory.createWithPhases(LivingDamagePostCallback.class,
            callbacks -> (entity, source, newDamage, absorptionReduction) -> {
                for (LivingDamagePostCallback callback : callbacks) {
                    callback.onDamage(entity, source, newDamage, absorptionReduction);
                }
            }, Event.DEFAULT_PHASE, LOWEST_PHASE);

    void onDamage(LivingEntity entity, DamageSource source, float newDamage, float absorptionReduction);
}
