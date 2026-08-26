package com.atsuishio.superbwarfare.item.curio

import com.atsuishio.superbwarfare.config.server.SyncConfig
import com.atsuishio.superbwarfare.network.message.receive.EntityRelationSyncMessage
import com.atsuishio.superbwarfare.network.message.receive.PlayerInfoSyncMessage
import com.atsuishio.superbwarfare.tools.SeekTool
import com.atsuishio.superbwarfare.tools.ServerSyncedEntityHandler
import com.atsuishio.superbwarfare.tools.sendPacketTo
import dev.emi.trinkets.api.SlotReference
import dev.emi.trinkets.api.Trinket
import dev.emi.trinkets.api.TrinketsApi
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.server.MinecraftServer
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level

open class IffItem : Item(Properties().stacksTo(1)), Trinket {
    override fun canEquip(stack: ItemStack, slot: SlotReference, entity: LivingEntity): Boolean {
        return TrinketsApi.getTrinketComponent(entity)
            .map { !it.isEquipped(this) }
            .orElse(false)
    }

    override fun appendHoverText(
        pStack: ItemStack,
        pLevel: Level?,
        pTooltipComponents: MutableList<Component?>,
        pIsAdvanced: TooltipFlag
    ) {
        pTooltipComponents.add(Component.translatable("des.superbwarfare.iff_1").withStyle(ChatFormatting.GRAY))
    }

    companion object {
        init {
            ServerTickEvents.END_SERVER_TICK.register(::onIFFItemServerTick)
        }

        private fun onIFFItemServerTick(server: MinecraftServer) {
            if (server.tickCount % SyncConfig.SYNC_ENTITY_INTERVAL.get() != 0) return

            for (player in server.playerList.players) {
                if (!player.isAlive) continue
                // 将自己注册到 ServerSyncedEntityHandler，供雷达等系统发现
                ServerSyncedEntityHandler.register(player)

                // 向所有队友同步自身 ID 和玩家信息
                val dim = player.level().dimension().location()
                val idMsg = EntityRelationSyncMessage(dim, friendlyIds = listOf(player.id))
                val infoMsg = PlayerInfoSyncMessage(
                    dim, listOf(
                        PlayerInfoSyncMessage.SyncedPlayerInfo(
                            uuid = player.uuid,
                            pos = player.position(),
                            name = player.name.string,
                            onVehicle = player.vehicle != null,
                            isDriver = player.vehicle != null && player.vehicle?.controllingPassenger == player,
                            relation = "friendly",
                            entityId = player.id,
                        )
                    )
                )
                for (teammate in server.playerList.players) {
                    if (teammate != player && teammate.isAlive
                        && teammate.level().dimension() == player.level().dimension()
                        && SeekTool.IS_FRIENDLY.test(teammate, player)
                    ) {
                        sendPacketTo(teammate, idMsg)
                        sendPacketTo(teammate, infoMsg)
                    }
                }
            }
        }
    }
}
