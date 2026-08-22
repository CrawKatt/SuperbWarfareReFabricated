package com.atsuishio.superbwarfare.item.trinket

import com.atsuishio.superbwarfare.init.ModItems
import com.atsuishio.superbwarfare.init.ModSounds
import com.atsuishio.superbwarfare.tools.NBTTool
import dev.emi.trinkets.api.SlotReference
import dev.emi.trinkets.api.TrinketItem
import dev.emi.trinkets.api.TrinketsApi
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.phys.Vec3

class ParachuteItem : TrinketItem(Properties().stacksTo(1).durability(600)) {
    override fun isValidRepairItem(stack: ItemStack, repairCandidate: ItemStack): Boolean {
        return repairCandidate.`is`(Items.PHANTOM_MEMBRANE)
    }

    override fun canEquip(stack: ItemStack, slot: SlotReference, entity: LivingEntity): Boolean {
        return TrinketsApi.getTrinketComponent(entity)
            .map { component -> !component.isEquipped(this) }
            .orElse(false)!!
    }

    override fun tick(stack: ItemStack, slot: SlotReference, entity: LivingEntity) {
        val tag = NBTTool.getTag(stack)

        if (entity !is Player) {
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

        if (tag.getBoolean(TAG_OPEN)) {
            val level = entity.level()

            if (
                entity.onGround() ||
                entity.isInWater ||
                entity.isFallFlying ||
                entity.vehicle != null ||
                entity is Player && entity.abilities.flying
            ) {
                tag.putBoolean(TAG_OPEN, false)
                NBTTool.saveTag(stack, tag)

                level.playSound(
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
                if (level.isClientSide) {
                    entity.addDeltaMovement(
                        Vec3(entity.lookAngle.x, 0.0, entity.lookAngle.z).normalize().scale(0.05)
                    )
                    entity.deltaMovement = entity.deltaMovement.multiply(1.03, 0.75, 1.03)
                }
            } else {
                if (!level.isClientSide) {
                    entity.addDeltaMovement(
                        Vec3(entity.lookAngle.x, 0.0, entity.lookAngle.z).normalize().scale(0.05)
                    )
                    entity.deltaMovement = entity.deltaMovement.multiply(1.03, 0.75, 1.03)
                }
            }

            if (entity.tickCount % 40 == 0 && level is ServerLevel) {
                stack.hurtAndBreak(1, level, entity as? ServerPlayer) { }
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
                        .any { pair ->
                            NBTTool.getTag(pair.b).getBoolean(TAG_OPEN)
                        }
                }
                .orElse(false)!!
        }

        @JvmStatic
        fun isParachuteVisible(entity: LivingEntity?): Boolean {
            if (entity == null) return false

            return TrinketsApi.getTrinketComponent(entity)
                .map { component ->
                    component.isEquipped(ModItems.PARACHUTE)
                }
                .orElse(false)!!
        }
    }
}
