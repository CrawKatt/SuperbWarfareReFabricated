package com.atsuishio.superbwarfare.item.gun.special

import com.atsuishio.superbwarfare.client.TooltipTool
import com.atsuishio.superbwarfare.client.renderer.gun.GeoGunRenderer
import com.atsuishio.superbwarfare.client.renderer.gun.HammerRenderer
import com.atsuishio.superbwarfare.init.ModTags
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.util.RandomSource
import net.minecraft.world.Container
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment

open class HammerItem(properties: Properties) : KnifeItem(properties) {

    override fun canBeDepleted(): Boolean = true

    override fun isDamageable(stack: ItemStack?): Boolean = true

    override fun getRecipeRemainder(itemstack: ItemStack): ItemStack {
        val stack = itemstack.copy()
        stack.hurt(1, RandomSource.create(), null)
        stack.getOrCreateTag().putInt("CraftCount", stack.getOrCreateTag().getInt("CraftCount") + 1)
        if (stack.isEmpty || stack.damageValue >= stack.maxDamage) {
            return ItemStack.EMPTY
        }
        return stack
    }

    override fun appendHoverText(
        stack: ItemStack,
        level: Level?,
        tooltip: MutableList<Component>,
        flag: TooltipFlag
    ) {
        super.appendHoverText(stack, level, tooltip, flag)
        TooltipTool.addHideText(
            tooltip,
            Component.translatable("des.superbwarfare.hammer", stack.getOrCreateTag().getInt("CraftCount"))
                .withStyle(ChatFormatting.GRAY)
        )
    }

    @Environment(EnvType.CLIENT)
    override fun createRenderer(): GeoGunRenderer = HammerRenderer()

    companion object {
        @JvmStatic
        fun onItemCraftedByHammer(item: ItemStack, container: Container, player: Player) {
            if (player.level().isClientSide) return

            if (item.`is`(ModTags.Items.HAMMER)) {
                var count = 0
                for (i in 0..<container.containerSize) {
                    if (container.getItem(i).`is`(ModTags.Items.HAMMER)) count++
                }
                if (count == 2) {
                    container.clearContent()
                }
            }
        }
    }
}
