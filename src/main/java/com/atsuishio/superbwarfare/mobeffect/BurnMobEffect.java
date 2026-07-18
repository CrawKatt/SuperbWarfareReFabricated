package com.atsuishio.superbwarfare.mobeffect;

import com.atsuishio.superbwarfare.entity.mixin.EntityPersistentDataAccess;
import com.atsuishio.superbwarfare.event.custom.LivingTickCallback;
import com.atsuishio.superbwarfare.event.custom.MobEffectAddedCallback;
import com.atsuishio.superbwarfare.event.custom.MobEffectRemovedCallback;
import com.atsuishio.superbwarfare.init.ModDamageTypes;
import com.atsuishio.superbwarfare.init.ModMobEffects;
import com.atsuishio.superbwarfare.init.ModSounds;
import com.atsuishio.superbwarfare.network.NetworkRegistry;
import com.atsuishio.superbwarfare.network.message.receive.ClientIndicatorMessage;
import com.atsuishio.superbwarfare.tools.DamageHandler;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

public class BurnMobEffect extends MobEffect {

    private static final String BURN_ATTACKER = "BurnAttacker";

    public BurnMobEffect() {
        super(MobEffectCategory.HARMFUL, -12708330);
    }

    public static void registerEvents() {
        MobEffectAddedCallback.EVENT.register(BurnMobEffect::onEffectAdded);
        MobEffectRemovedCallback.EVENT.register(BurnMobEffect::onEffectRemoved);
        LivingTickCallback.EVENT.register(BurnMobEffect::onLivingTick);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        var persistentData = EntityPersistentDataAccess.of(entity).superbwarfare$getPersistentData();
        Entity attacker;
        if (!persistentData.contains(BURN_ATTACKER)) {
            attacker = null;
        } else {
            attacker = entity.level().getEntity(persistentData.getInt(BURN_ATTACKER));
        }

        DamageHandler.doDamage(entity, ModDamageTypes.causeBurnDamage(entity.level().registryAccess(), attacker), 0.6f + (0.3f * amplifier));
        entity.invulnerableTime = 0;

        if (attacker instanceof ServerPlayer player) {
            player.level().playSound(null, player.blockPosition(), ModSounds.INDICATION.get(), SoundSource.VOICE, 1, 1);
            NetworkRegistry.sendToPlayer(player, new ClientIndicatorMessage(0, 5));
        }

    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 20 == 0;
    }

    public static void onEffectAdded(LivingEntity living, MobEffectInstance instance, @Nullable Entity source) {
        if (!instance.getEffect().equals(ModMobEffects.BURN.get())) {
            return;
        }

        DamageHandler.doDamage(living, new DamageSource(living.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.IN_FIRE), source), 0.6f + (0.3f * instance.getAmplifier()));
        living.invulnerableTime = 0;

        if (source instanceof LivingEntity entitySource) {
            EntityPersistentDataAccess.of(living).superbwarfare$getPersistentData()
                    .putInt(BURN_ATTACKER, entitySource.getId());
        }
    }

    public static void onEffectRemoved(LivingEntity living, @Nullable MobEffectInstance instance) {
        if (instance == null) {
            return;
        }

        if (instance.getEffect().equals(ModMobEffects.BURN.get())) {
            EntityPersistentDataAccess.of(living).superbwarfare$getPersistentData()
                    .remove(BURN_ATTACKER);
        }
    }

    public static void onLivingTick(LivingEntity living) {
        if (living.hasEffect(ModMobEffects.BURN.get())) {
            living.setRemainingFireTicks(2);
        }

        if (living.isInWater()) {
            living.removeEffect(ModMobEffects.BURN.get());
        }
    }
}
