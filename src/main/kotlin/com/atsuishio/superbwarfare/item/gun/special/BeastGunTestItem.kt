package com.atsuishio.superbwarfare.item.gun.special

import com.atsuishio.superbwarfare.client.GunRendererBuilder
import com.atsuishio.superbwarfare.client.model.item.BeastGunTestModel
import com.atsuishio.superbwarfare.init.ModRarities
import com.atsuishio.superbwarfare.item.gun.GunGeoItem
import com.atsuishio.superbwarfare.item.weapon.BeastItem.Companion.beastKill
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import software.bernie.geckolib.renderer.GeoItemRenderer
import java.util.function.Supplier
import javax.annotation.ParametersAreNonnullByDefault

open class BeastGunTestItem : GunGeoItem(Properties().rarity(ModRarities.BEAST)) {
    override fun hurtEnemy(stack: ItemStack, target: LivingEntity, attacker: LivingEntity): Boolean {
        beastKill(attacker, target)
        return true
    }

    override fun isEnchantable(stack: ItemStack): Boolean {
        return false
    }

    @ParametersAreNonnullByDefault
    override fun appendHoverText(
        stack: ItemStack,
        context: TooltipContext,
        tooltipComponents: MutableList<Component>,
        tooltipFlag: TooltipFlag
    ) {
        tooltipComponents.add(Component.translatable("des.superbwarfare.beast").withColor(0xa56855))
    }

    override fun getRenderer(): Supplier<out GeoItemRenderer<*>> =
        GunRendererBuilder.simple { BeastGunTestModel }
}
