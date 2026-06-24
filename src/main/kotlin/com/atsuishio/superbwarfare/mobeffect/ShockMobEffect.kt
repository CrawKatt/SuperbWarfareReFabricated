package com.atsuishio.superbwarfare.mobeffect

import com.atsuishio.superbwarfare.capability.PersistentDataAccessor
import com.atsuishio.superbwarfare.init.ModDamageTypes
import com.atsuishio.superbwarfare.init.ModMobEffects
import com.atsuishio.superbwarfare.init.ModSounds
import com.atsuishio.superbwarfare.network.message.receive.ClientIndicatorMessage
import com.atsuishio.superbwarfare.tools.DamageHandler
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.core.BlockPos
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerPlayer
import net.minecraft.sounds.SoundSource
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.player.Player

open class ShockMobEffect : MobEffect(MobEffectCategory.HARMFUL, -256) {

    init {
        addAttributeModifier(
            Attributes.MOVEMENT_SPEED,
            ResourceLocation.withDefaultNamespace("effect.speed"),
            -10.0,
            AttributeModifier.Operation.ADD_VALUE
        )
    }

    override fun applyEffectTick(entity: LivingEntity, amplifier: Int): Boolean {
        val data = (entity as PersistentDataAccessor).superbwarfare$getPersistentData()
        val attacker = if (data.contains("TargetShockAttacker")) {
            entity.level().getEntity(data.getInt("TargetShockAttacker"))
        } else null

        DamageHandler.doDamage(
            entity,
            ModDamageTypes.causeShockDamage(entity.level().registryAccess(), attacker),
            2f + 1.25f * amplifier
        )
        entity.level().playSound(null, entity.onPos, ModSounds.ELECTRIC, SoundSource.PLAYERS, 1f, 1f)

        if (!entity.level().isClientSide && entity is Player) {
            entity.level().playSound(
                null,
                BlockPos.containing(entity.x, entity.y, entity.z),
                ModSounds.SHOCK,
                SoundSource.HOSTILE,
                1f,
                1f
            )
        }

        if (attacker is ServerPlayer) {
            attacker.level().playSound(null, attacker.blockPosition(), ModSounds.INDICATION, SoundSource.VOICE, 1f, 1f)
            ServerPlayNetworking.send(attacker, ClientIndicatorMessage(0, 5))
        }
        return true
    }

    override fun shouldApplyEffectTickThisTick(duration: Int, amplifier: Int): Boolean {
        return duration % 20 == 0
    }

    companion object {
        @JvmStatic
        fun shouldCancelDamage(attacker: LivingEntity): Boolean {
            return attacker.hasEffect(ModMobEffects.SHOCK)
        }
    }
}
