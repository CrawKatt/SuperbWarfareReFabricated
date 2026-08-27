package com.atsuishio.superbwarfare.item.gun.special

import com.atsuishio.superbwarfare.client.GunRendererBuilder
import com.atsuishio.superbwarfare.client.model.item.BeastGunTestModel
import com.atsuishio.superbwarfare.init.ModRarities
import com.atsuishio.superbwarfare.item.EntitySwingHook
import com.atsuishio.superbwarfare.item.gun.GunGeoItem
import com.atsuishio.superbwarfare.item.weapon.BeastItem.Companion.beastKill
import com.atsuishio.superbwarfare.tools.TraceTool
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.Style
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level
import software.bernie.geckolib.renderer.GeoItemRenderer
import java.util.function.Supplier
import javax.annotation.ParametersAreNonnullByDefault

open class BeastGunTestItem : GunGeoItem(Properties().rarity(ModRarities.BEAST)), EntitySwingHook {
    override fun hurtEnemy(stack: ItemStack, target: LivingEntity, attacker: LivingEntity): Boolean {
        beastKill(attacker, target)
        return true
    }

    override fun canBeHurtBy(source: DamageSource): Boolean {
        return false
    }

    override fun isEnchantable(stack: ItemStack): Boolean {
        return false
    }

    @ParametersAreNonnullByDefault
    override fun onEntitySwing(stack: ItemStack, entity: LivingEntity): Boolean {
        val target = TraceTool.findMeleeEntity(entity, 51.4)
        if (target != null) {
            beastKill(entity, target)
        }
        return false
    }

    override fun onLeftClickEntity(stack: ItemStack, player: Player, entity: Entity): Boolean {
        beastKill(player, entity)
        return false
    }

    override fun appendHoverText(
        pStack: ItemStack,
        pLevel: Level?,
        pTooltipComponents: MutableList<Component>,
        pIsAdvanced: TooltipFlag
    ) {
        pTooltipComponents.add(
            Component.translatable("des.superbwarfare.beast").withStyle(Style.EMPTY.withColor(0xa56855))
        )
    }

    override fun getRenderer(): Supplier<out GeoItemRenderer<*>> =
        GunRendererBuilder.simple { BeastGunTestModel }
}
