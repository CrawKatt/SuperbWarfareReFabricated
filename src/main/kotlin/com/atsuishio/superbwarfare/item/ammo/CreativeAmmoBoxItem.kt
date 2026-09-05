package com.atsuishio.superbwarfare.item.ammo

import com.atsuishio.superbwarfare.capability.entity.InfinityAmmoCapability
import com.atsuishio.superbwarfare.network.message.receive.ClientInfinityAmmoMessage
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

            invertInfinityAmmo(player, entity)
            InteractionResult.PASS
        }
    }

    override fun appendHoverText(
        pStack: ItemStack,
        pLevel: Level?,
        pTooltipComponents: MutableList<Component>,
        pIsAdvanced: TooltipFlag
    ) {
        pTooltipComponents.add(
            Component.translatable("des.superbwarfare.creative_ammo_box").withStyle(ChatFormatting.GRAY)
        )
    }

    override fun use(
        level: Level,
        player: Player,
        usedHand: InteractionHand
    ): InteractionResultHolder<ItemStack> {
        invertInfinityAmmo(player, player)
        return super.use(level, player, usedHand)
    }

    private fun invertInfinityAmmo(player: Player? = null, entity: Entity) {
        if (entity.level().isClientSide) return

        var hasInfiniteAmmo = false

        InfinityAmmoCapability.modify(entity) {
            hasInfiniteAmmo = !it.hasInfinityAmmo
            it.hasInfinityAmmo = hasInfiniteAmmo
        }

        // TODO message
        player?.displayClientMessage(
            Component.literal(if (hasInfiniteAmmo) "+ infinity" else "- infinity"),
            true
        )

        if (entity is Player) {
            sendPacketTo(entity, ClientInfinityAmmoMessage(entity.id, hasInfiniteAmmo))
        }

        entity.sendPacketToTrackingThis(
            ClientInfinityAmmoMessage(entity.id, hasInfiniteAmmo)
        )
    }
}
