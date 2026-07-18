package com.atsuishio.superbwarfare.mobeffect;

import com.atsuishio.superbwarfare.entity.mixin.EntityPersistentDataAccess;
import com.atsuishio.superbwarfare.event.custom.LivingAttackCallback;
import com.atsuishio.superbwarfare.event.custom.LivingTickCallback;
import com.atsuishio.superbwarfare.event.custom.MobEffectAddedCallback;
import com.atsuishio.superbwarfare.event.custom.MobEffectRemovedCallback;
import com.atsuishio.superbwarfare.init.ModDamageTypes;
import com.atsuishio.superbwarfare.init.ModMobEffects;
import com.atsuishio.superbwarfare.init.ModSounds;
import com.atsuishio.superbwarfare.network.NetworkRegistry;
import com.atsuishio.superbwarfare.network.message.receive.ClientIndicatorMessage;
import com.atsuishio.superbwarfare.tools.DamageHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public class ShockMobEffect extends MobEffect {

    private static final String SHOCK_ATTACKER = "TargetShockAttacker";

    public ShockMobEffect() {
        super(MobEffectCategory.HARMFUL, -256);
        addAttributeModifier(Attributes.MOVEMENT_SPEED, "7107DE5E-7CE8-4030-940E-514C1F160890", -10F, AttributeModifier.Operation.ADDITION);
    }

    public static void registerEvents() {
        MobEffectAddedCallback.EVENT.register(ShockMobEffect::onEffectAdded);
        MobEffectRemovedCallback.EVENT.register(ShockMobEffect::onEffectRemoved);
        LivingTickCallback.EVENT.register(ShockMobEffect::onLivingTick);
        LivingAttackCallback.EVENT.register(ShockMobEffect::onEntityAttacked);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        var persistentData = EntityPersistentDataAccess.of(entity).superbwarfare$getPersistentData();
        Entity attacker;
        if (!persistentData.contains(SHOCK_ATTACKER)) {
            attacker = null;
        } else {
            attacker = entity.level().getEntity(persistentData.getInt(SHOCK_ATTACKER));
        }

        DamageHandler.doDamage(entity, ModDamageTypes.causeShockDamage(entity.level().registryAccess(), attacker), 2 + (1.25f * amplifier));
        entity.level().playSound(null, entity.getOnPos(), ModSounds.ELECTRIC.get(), SoundSource.PLAYERS, 1, 1);

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
        if (!instance.getEffect().equals(ModMobEffects.SHOCK.get())) {
            return;
        }

        if (living instanceof Player) {
            if (!living.level().isClientSide()) {
                living.level().playSound(null, BlockPos.containing(living.getX(), living.getY(), living.getZ()), ModSounds.SHOCK.get(), SoundSource.HOSTILE, 1, 1);
            } else {
                living.level().playLocalSound(living.getX(), living.getY(), living.getZ(), ModSounds.SHOCK.get(), SoundSource.HOSTILE, 1, 1, false);
            }
        }

        DamageHandler.doDamage(living, ModDamageTypes.causeShockDamage(living.level().registryAccess(), source),
                2 + (1.25f * instance.getAmplifier()));

        if (source instanceof LivingEntity entitySource) {
            EntityPersistentDataAccess.of(living).superbwarfare$getPersistentData()
                    .putInt(SHOCK_ATTACKER, entitySource.getId());
        }
    }

    public static void onEffectRemoved(LivingEntity living, @Nullable MobEffectInstance instance) {
        if (instance == null) {
            return;
        }

        if (instance.getEffect().equals(ModMobEffects.SHOCK.get())) {
            EntityPersistentDataAccess.of(living).superbwarfare$getPersistentData()
                    .remove(SHOCK_ATTACKER);
        }
    }

    public static void onLivingTick(LivingEntity living) {
        if (living.hasEffect(ModMobEffects.SHOCK.get())) {
            living.setXRot((float) Mth.nextDouble(RandomSource.create(), -23, -36));
            living.xRotO = living.getXRot();
        }
    }

    public static boolean onEntityAttacked(LivingEntity attacked, DamageSource source, float amount) {
        Entity entity = source.getDirectEntity();
        if (entity == null) {
            return true;
        }

        return !(entity instanceof LivingEntity living) || !living.hasEffect(ModMobEffects.SHOCK.get());
    }
}
