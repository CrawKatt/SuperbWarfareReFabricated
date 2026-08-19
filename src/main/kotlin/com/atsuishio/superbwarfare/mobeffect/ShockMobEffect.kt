package com.atsuishio.superbwarfare.mobeffect

import com.atsuishio.superbwarfare.entity.mixin.EntityPersistentDataAccess
import com.atsuishio.superbwarfare.init.ModDamageTypes
import com.atsuishio.superbwarfare.init.ModMobEffects
import com.atsuishio.superbwarfare.init.ModSounds
import com.atsuishio.superbwarfare.network.NetworkRegistry
import com.atsuishio.superbwarfare.network.message.receive.ClientIndicatorMessage
import com.atsuishio.superbwarfare.tools.DamageHandler
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerPlayer
import net.minecraft.sounds.SoundSource
import net.minecraft.util.Mth
import net.minecraft.util.RandomSource
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.player.Player

open class ShockMobEffect : MobEffect(MobEffectCategory.HARMFUL, -256) {
    init {
        addAttributeModifier(
            Attributes.MOVEMENT_SPEED,
            "7107DE5E-7CE8-4030-940E-514C1F160890",
            -10.0,
            AttributeModifier.Operation.ADDITION
        )
    }

    override fun applyEffectTick(entity: LivingEntity, amplifier: Int) {
        val attacker = getShockAttacker(entity)

        DamageHandler.doDamage(
            entity,
            ModDamageTypes.causeShockDamage(entity.level().registryAccess(), attacker),
            2f + 1.25f * amplifier
        )
        entity.level().playSound(
            null,
            entity.onPos,
            ModSounds.ELECTRIC,
            SoundSource.PLAYERS,
            1f,
            1f
        )

        val player = attacker as? ServerPlayer ?: return
        player.level().playSound(
            null,
            player.blockPosition(),
            ModSounds.INDICATION,
            SoundSource.VOICE,
            1f,
            1f
        )
        NetworkRegistry.sendToPlayer(player, ClientIndicatorMessage(0, 5))
    }

    override fun isDurationEffectTick(duration: Int, amplifier: Int): Boolean {
        return duration % 20 == 0
    }

    companion object {
        const val TAG_ATTACKER = "TargetShockAttacker"

        @JvmStatic
        fun onShockAdded(living: LivingEntity, instance: MobEffectInstance, source: Entity?) {
            if (instance.effect != ModMobEffects.SHOCK) return

            if (living is Player) {
                if (!living.level().isClientSide) {
                    living.level().playSound(
                        null,
                        BlockPos.containing(living.x, living.y, living.z),
                        ModSounds.SHOCK,
                        SoundSource.HOSTILE,
                        1f,
                        1f
                    )
                } else {
                    living.level().playLocalSound(
                        living.x,
                        living.y,
                        living.z,
                        ModSounds.SHOCK,
                        SoundSource.HOSTILE,
                        1f,
                        1f,
                        false
                    )
                }
            }

            DamageHandler.doDamage(
                living,
                ModDamageTypes.causeShockDamage(living.level().registryAccess(), source),
                2f + 1.25f * instance.amplifier
            )

            if (source is LivingEntity) {
                persistentData(living).putInt(TAG_ATTACKER, source.id)
            }
        }

        @JvmStatic
        fun onShockRemoved(living: LivingEntity, instance: MobEffectInstance?) {
            if (instance == null || instance.effect != ModMobEffects.SHOCK) return
            persistentData(living).remove(TAG_ATTACKER)
        }

        @JvmStatic
        fun onLivingTick(living: LivingEntity) {
            if (living.hasEffect(ModMobEffects.SHOCK)) {
                living.xRot = Mth.nextDouble(RandomSource.create(), -23.0, -36.0).toFloat()
                living.xRotO = living.xRot
            }
        }

        @JvmStatic
        fun shouldCancelDamage(attacker: LivingEntity): Boolean {
            return attacker.hasEffect(ModMobEffects.SHOCK)
        }

        private fun getShockAttacker(entity: LivingEntity): Entity? {
            val data = persistentData(entity)
            return if (data.contains(TAG_ATTACKER)) {
                entity.level().getEntity(data.getInt(TAG_ATTACKER))
            } else {
                null
            }
        }

        private fun persistentData(entity: LivingEntity) =
            EntityPersistentDataAccess.of(entity).`superbwarfare$getPersistentData`()
    }
}
