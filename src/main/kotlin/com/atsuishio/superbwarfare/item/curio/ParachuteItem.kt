package com.atsuishio.superbwarfare.item.curio

import com.atsuishio.superbwarfare.init.ModItems
import com.atsuishio.superbwarfare.init.ModSounds
import dev.emi.trinkets.api.SlotReference
import dev.emi.trinkets.api.Trinket
import dev.emi.trinkets.api.TrinketsApi
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.phys.Vec3

class ParachuteItem : Item(Properties().stacksTo(1).durability(600)), Trinket {
    override fun isValidRepairItem(pStack: ItemStack, pRepairCandidate: ItemStack): Boolean {
        return pRepairCandidate.`is`(Items.PHANTOM_MEMBRANE)
    }

    override fun canEquip(stack: ItemStack, slot: SlotReference, entity: LivingEntity): Boolean {
        return TrinketsApi.getTrinketComponent(entity)
            .map { !it.isEquipped(this) }
            .orElse(false)
    }

    override fun tick(stack: ItemStack, slot: SlotReference, entity: LivingEntity) {
        if (entity !is Player) {
            val tag = stack.getOrCreateTag()
            if (!tag.getBoolean(TAG_OPEN) && entity.deltaMovement.y < -0.6 && entity.fallDistance > 4) {
                tag.putBoolean(TAG_OPEN, true)
                entity.level().playSound(
                    null,
                    entity.x,
                    entity.y,
                    entity.z,
                    ModSounds.PARACHUTE_OPEN,
                    SoundSource.PLAYERS,
                    1f,
                    1f
                )
            }
        }

        if (stack.getOrCreateTag().getBoolean(TAG_OPEN)) {
            if ((entity.onGround() || entity.isInWater) || entity.isFallFlying || entity.vehicle != null || (entity is Player && entity.abilities.flying)) {
                stack.orCreateTag.putBoolean(TAG_OPEN, false)
                entity.level().playSound(
                    null,
                    entity.x,
                    entity.y,
                    entity.z,
                    ModSounds.PARACHUTE_CLOSE,
                    SoundSource.PLAYERS,
                    1f,
                    1f
                )
            }
            if (entity is Player) {
                if (entity.level().isClientSide) {
                    entity.addDeltaMovement(
                        Vec3(entity.lookAngle.x, 0.0, entity.lookAngle.z).normalize().scale(0.05)
                    )
                    entity.deltaMovement = entity.deltaMovement.multiply(1.03, 0.75, 1.03)
                }
            } else {
                if (!entity.level().isClientSide) {
                    entity.addDeltaMovement(
                        Vec3(entity.lookAngle.x, 0.0, entity.lookAngle.z).normalize().scale(0.05)
                    )
                    entity.deltaMovement = entity.deltaMovement.multiply(1.03, 0.75, 1.03)
                }
            }

            if (entity.tickCount % 40 == 0) {
                stack.hurtAndBreak(1, entity) { }
            }
            entity.resetFallDistance()
        }
    }

    companion object {
        const val TAG_OPEN: String = "Open"

        @JvmStatic
        fun isParachuteOpen(entity: LivingEntity?): Boolean {
            if (entity == null) return false
            return TrinketsApi.getTrinketComponent(entity)
                .map { component ->
                    component.getEquipped(ModItems.PARACHUTE)
                        .firstOrNull()
                        ?.b
                        ?.orCreateTag
                        ?.getBoolean(TAG_OPEN)
                        ?: false
                }
                .orElse(false)
        }

        @JvmStatic
        fun isParachuteVisible(entity: LivingEntity?): Boolean {
            if (entity == null) return false
            return TrinketsApi.getTrinketComponent(entity)
                .map { it.isEquipped(ModItems.PARACHUTE) }
                .orElse(false)
        }
    }
}
