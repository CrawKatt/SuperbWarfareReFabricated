package com.atsuishio.superbwarfare.item.projectile

import com.atsuishio.superbwarfare.entity.projectile.MortarShellEntity
import com.atsuishio.superbwarfare.init.ModEntities
import com.atsuishio.superbwarfare.init.ModItems
import com.atsuishio.superbwarfare.init.ModSounds
import com.atsuishio.superbwarfare.item.DispenserLaunchable
import net.minecraft.core.BlockSource
import net.minecraft.core.Position
import net.minecraft.core.dispenser.AbstractProjectileDispenseBehavior
import net.minecraft.core.dispenser.DispenseItemBehavior
import net.minecraft.network.chat.Component
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.alchemy.PotionUtils
import net.minecraft.world.item.alchemy.Potions
import net.minecraft.world.level.Level
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry

class PotionMortarShellItem : MortarShellItem(), DispenserLaunchable {
    override fun getDefaultInstance(): ItemStack {
        return PotionUtils.setPotion(super.getDefaultInstance(), Potions.POISON)
    }

    override fun appendHoverText(
        pStack: ItemStack,
        pLevel: Level?,
        pTooltip: MutableList<Component>,
        pFlag: TooltipFlag
    ) {
        PotionUtils.addPotionTooltip(pStack, pTooltip, 0.125f)
    }

    override fun getLaunchBehavior(): DispenseItemBehavior {
        return object : AbstractProjectileDispenseBehavior() {
            override fun getPower(): Float {
                return 0.5f
            }

            override fun getProjectile(pLevel: Level, pPosition: Position, pStack: ItemStack): Projectile {
                val shell = MortarShellEntity(
                    ModEntities.MORTAR_SHELL,
                    pPosition.x(),
                    pPosition.y(),
                    pPosition.z(),
                    pLevel,
                    0.13f
                )
                shell.setEffectsFromItem(pStack)
                return shell
            }

            override fun playSound(source: BlockSource) {
                source.level
                    .playSound(null, source.pos, ModSounds.MORTAR_FIRE, SoundSource.BLOCKS, 1f, 1f)
            }
        }
    }

    companion object {
        @JvmStatic
        fun registerColorHandler() {
            ColorProviderRegistry.ITEM.register(
                { stack, layer ->
                    if (layer == 1) PotionUtils.getColor(stack) else -1
                },
                ModItems.POTION_MORTAR_SHELL
            )
        }
    }
}
