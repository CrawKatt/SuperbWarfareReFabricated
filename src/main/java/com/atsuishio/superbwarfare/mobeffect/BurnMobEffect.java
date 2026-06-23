package com.atsuishio.superbwarfare.mobeffect;

import com.atsuishio.superbwarfare.init.ModDamageTypes;
import com.atsuishio.superbwarfare.init.ModMobEffects;
import com.atsuishio.superbwarfare.capability.PersistentDataAccessor;
import com.atsuishio.superbwarfare.network.message.receive.ClientIndicatorMessage;
import com.atsuishio.superbwarfare.tools.DamageHandler;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class BurnMobEffect extends MobEffect {

    public BurnMobEffect() {
        super(MobEffectCategory.HARMFUL, -12708330);
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity.isInWater()) {
            entity.removeEffect(ModMobEffects.BURN);
            return true;
        }

        entity.setRemainingFireTicks(40);

        var data = ((PersistentDataAccessor) entity).superbwarfare$getPersistentData();
        Entity attacker = data.contains("BurnAttacker")
                ? entity.level().getEntity(data.getInt("BurnAttacker"))
                : null;

        DamageHandler.doDamage(entity, ModDamageTypes.causeBurnDamage(entity.level().registryAccess(), attacker), 0.6f + (0.3f * amplifier));
        entity.invulnerableTime = 0;

        if (attacker instanceof ServerPlayer player) {
            player.level().playSound(null, player.blockPosition(), ModSounds.INDICATION, SoundSource.VOICE, 1, 1);
            ServerPlayNetworking.send(player, new ClientIndicatorMessage(0, 5));
        }
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration % 20 == 0;
    }
}
