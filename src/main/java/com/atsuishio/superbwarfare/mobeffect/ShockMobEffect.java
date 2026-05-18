package com.atsuishio.superbwarfare.mobeffect;

import com.atsuishio.superbwarfare.init.ModDamageTypes;
import com.atsuishio.superbwarfare.init.ModMobEffects;
import com.atsuishio.superbwarfare.init.ModSounds;
import com.atsuishio.superbwarfare.capability.PersistentDataAccessor;
import com.atsuishio.superbwarfare.network.message.receive.ClientIndicatorMessage;
import com.atsuishio.superbwarfare.tools.DamageHandler;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

public class ShockMobEffect extends MobEffect {

    public ShockMobEffect() {
        super(MobEffectCategory.HARMFUL, -256);
        addAttributeModifier(Attributes.MOVEMENT_SPEED, ResourceLocation.withDefaultNamespace("effect.speed"), -10F, AttributeModifier.Operation.ADD_VALUE);
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        var data = ((PersistentDataAccessor) entity).superbwarfare$getPersistentData();
        Entity attacker = data.contains("TargetShockAttacker")
                ? entity.level().getEntity(data.getInt("TargetShockAttacker"))
                : null;

        DamageHandler.doDamage(entity, ModDamageTypes.causeShockDamage(entity.level().registryAccess(), attacker), 2 + (1.25f * amplifier));
        entity.level().playSound(null, entity.getOnPos(), ModSounds.ELECTRIC, SoundSource.PLAYERS, 1, 1);

        if (!entity.level().isClientSide() && entity instanceof Player) {
            entity.level().playSound(null, BlockPos.containing(entity.getX(), entity.getY(), entity.getZ()), ModSounds.SHOCK, SoundSource.HOSTILE, 1, 1);
        }

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

    public static boolean shouldCancelDamage(LivingEntity attacker) {
        return attacker.hasEffect(ModMobEffects.SHOCK);
    }
}
