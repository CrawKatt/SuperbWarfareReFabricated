package com.atsuishio.superbwarfare.advancement.criteria

import com.atsuishio.superbwarfare.init.ModCriteriaTriggers
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.advancements.Criterion
import net.minecraft.advancements.critereon.ContextAwarePredicate
import net.minecraft.advancements.critereon.DamagePredicate
import net.minecraft.advancements.critereon.EntityPredicate
import net.minecraft.advancements.critereon.SimpleCriterionTrigger
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.damagesource.DamageSource
import java.util.*

class VehicleHurtTrigger : SimpleCriterionTrigger<VehicleHurtTrigger.TriggerInstance>() {
    override fun codec(): Codec<TriggerInstance> {
        return TriggerInstance.CODEC
    }

    fun trigger(pPlayer: ServerPlayer, source: DamageSource, amount: Float) {
        this.trigger(pPlayer) { instance -> instance.matches(pPlayer, source, amount) }
    }

    @JvmRecord
    data class TriggerInstance(
        val playerVar: Optional<ContextAwarePredicate>,
        val damage: Optional<DamagePredicate>
    ) : SimpleInstance {
        override fun player() = this.playerVar

        companion object {
            val CODEC: Codec<TriggerInstance> = RecordCodecBuilder.create { instance ->
                instance.group(
                    EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::playerVar),
                    DamagePredicate.CODEC.optionalFieldOf("damage").forGetter(TriggerInstance::damage)
                ).apply(instance) { player, damage -> TriggerInstance(player, damage) }
            }

            @JvmStatic
            fun vehicleHurt(): Criterion<TriggerInstance> {
                return ModCriteriaTriggers.VEHICLE_HURT
                    .createCriterion(
                        TriggerInstance(
                            Optional.empty<ContextAwarePredicate>(),
                            Optional.empty<DamagePredicate>()
                        )
                    )
            }

            @JvmStatic
            fun vehicleHurt(damage: DamagePredicate): Criterion<TriggerInstance> {
                return ModCriteriaTriggers.VEHICLE_HURT
                    .createCriterion(
                        TriggerInstance(
                            Optional.empty<ContextAwarePredicate>(),
                            Optional.of(damage)
                        )
                    )
            }

            @JvmStatic
            fun vehicleHurt(damageBuilder: DamagePredicate.Builder): Criterion<TriggerInstance> {
                return ModCriteriaTriggers.VEHICLE_HURT
                    .createCriterion(
                        TriggerInstance(
                            Optional.empty<ContextAwarePredicate>(),
                            Optional.of(damageBuilder.build())
                        )
                    )
            }
        }

        fun matches(pPlayer: ServerPlayer, source: DamageSource, amount: Float): Boolean {
            return this.damage.isPresent && this.damage.get().matches(pPlayer, source, amount, amount, false)
        }
    }
}
