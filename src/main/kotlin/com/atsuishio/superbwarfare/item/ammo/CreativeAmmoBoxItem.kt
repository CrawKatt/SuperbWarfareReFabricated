package com.atsuishio.superbwarfare.item.ammo

import com.atsuishio.superbwarfare.capability.living.InfiniteAmmoCapability
import com.atsuishio.superbwarfare.network.message.receive.ClientInfiniteAmmoMessage
import com.atsuishio.superbwarfare.tools.sendPacketTo
import com.atsuishio.superbwarfare.tools.sendPacketToTrackingThis
import net.fabricmc.fabric.api.event.player.UseEntityCallback
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Rarity
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level

object CreativeAmmoBoxItem : Item(Properties().rarity(Rarity.EPIC).stacksTo(1)) {

    init {
        UseEntityCallback.EVENT.register { player, _, hand, entity, _ ->
            if (player.getItemInHand(hand).item != this) {
                return@register InteractionResult.PASS
            }

            if (invertInfiniteAmmo(player, entity)) {
                InteractionResult.SUCCESS
            } else {
                InteractionResult.PASS
            }
        }
    }

    override fun appendHoverText(
        stack: ItemStack,
        context: TooltipContext,
        tooltipComponents: MutableList<Component>,
        tooltipFlag: TooltipFlag
    ) {
        tooltipComponents.add(
            Component.translatable("des.superbwarfare.creative_ammo_box").withStyle(ChatFormatting.GRAY)
        )
    }

    override fun use(level: Level, player: Player, usedHand: InteractionHand): InteractionResultHolder<ItemStack> {
        invertInfiniteAmmo(player, player)
        return super.use(level, player, usedHand)
    }

    private fun invertInfiniteAmmo(player: Player? = null, entity: Entity): Boolean {
        if (entity.level().isClientSide) return false

        var hasInfiniteAmmo = false

        InfiniteAmmoCapability.modify(entity) {
            hasInfiniteAmmo = !it.hasInfiniteAmmo
            it.hasInfiniteAmmo = hasInfiniteAmmo
        }

        // TODO message
        player?.displayClientMessage(Component.literal(if (hasInfiniteAmmo) "+ infinity" else "- infinity"), true)

        if (entity is Player) {
            sendPacketTo(entity, ClientInfiniteAmmoMessage(entity.id, hasInfiniteAmmo))
        }
        entity.sendPacketToTrackingThis(ClientInfiniteAmmoMessage(entity.id, hasInfiniteAmmo))
        return true
    }
}