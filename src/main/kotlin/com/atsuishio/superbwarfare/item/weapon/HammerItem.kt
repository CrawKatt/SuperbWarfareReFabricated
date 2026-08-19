package com.atsuishio.superbwarfare.item.weapon

import com.atsuishio.superbwarfare.client.TooltipTool
import com.atsuishio.superbwarfare.init.ModSounds
import com.atsuishio.superbwarfare.init.ModTags
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.sounds.SoundSource
import net.minecraft.util.RandomSource
import net.minecraft.world.Container
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.SwordItem
import net.minecraft.world.item.Tier
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level
import org.joml.Math

open class HammerItem(tier: Tier, attackDamage: Int, attackSpeed: Float, properties: Properties) :
    SwordItem(tier, attackDamage, attackSpeed, properties) {

    constructor(tier: Tier, attackDamage: Int, attackSpeed: Float, maxDamage: Int) : this(
        tier,
        attackDamage,
        attackSpeed,
        Properties().durability(maxDamage)
    )

    override fun appendHoverText(
        pStack: ItemStack,
        pLevel: Level?,
        pTooltipComponents: MutableList<Component>,
        pIsAdvanced: TooltipFlag
    ) {
        TooltipTool.addHideText(
            pTooltipComponents,
            Component.translatable("des.superbwarfare.hammer", pStack.getOrCreateTag().getInt("CraftCount"))
                .withStyle(ChatFormatting.GRAY)
        )
    }

    override fun getRecipeRemainder(itemstack: ItemStack): ItemStack = getCraftingRemainingStack(itemstack)

    override fun hurtEnemy(pStack: ItemStack, pTarget: LivingEntity, pAttacker: LivingEntity): Boolean {
        pAttacker.level().playSound(
            null,
            pTarget.onPos,
            ModSounds.MELEE_HIT,
            SoundSource.PLAYERS,
            1f,
            ((2 * Math.random() - 1) * 0.1f + 1.0f).toFloat()
        )
        return super.hurtEnemy(pStack, pTarget, pAttacker)
    }

    companion object {
        @JvmStatic
        fun getCraftingRemainingStack(itemstack: ItemStack): ItemStack {
            val stack = itemstack.copy()
            stack.hurt(1, RandomSource.create(), null)
            stack.getOrCreateTag().putInt("CraftCount", stack.getOrCreateTag().getInt("CraftCount") + 1)
            if (stack.isEmpty || stack.damageValue >= stack.maxDamage) {
                return ItemStack.EMPTY
            }
            return stack
        }

        @JvmStatic
        fun onItemCrafted(crafted: ItemStack, container: Container, player: Player) {
            if (player.level().isClientSide) return

            if (crafted.`is`(ModTags.Items.HAMMER)) {
                var count = 0
                for (i in 0..<container.containerSize) {
                    if (container.getItem(i).`is`(ModTags.Items.HAMMER)) count++
                }
                if (count == 2) {
                    for (i in 0..<container.containerSize) {
                        container.setItem(i, ItemStack.EMPTY)
                    }
                }
            }
        }
    }
}
