package com.atsuishio.superbwarfare.item.curio

import com.atsuishio.superbwarfare.network.message.receive.EntitySyncMessage
import com.atsuishio.superbwarfare.tools.SeekTool
import com.atsuishio.superbwarfare.tools.ServerSyncedEntityHandler
import com.atsuishio.superbwarfare.tools.sendPacketTo
import net.minecraft.ChatFormatting
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.tick.ServerTickEvent
import top.theillusivec4.curios.api.CuriosApi
import top.theillusivec4.curios.api.SlotContext
import top.theillusivec4.curios.api.type.capability.ICurioItem

open class IffItem : Item(Properties().stacksTo(1)), ICurioItem {
    override fun canEquip(slotContext: SlotContext, stack: ItemStack?): Boolean {
        return CuriosApi.getCuriosInventory(slotContext.entity())
            .flatMap { c -> c.findFirstCurio(this) }
            .isEmpty
    }

    override fun appendHoverText(
        stack: ItemStack,
        context: TooltipContext,
        tooltipComponents: MutableList<Component>,
        tooltipFlag: TooltipFlag
    ) {
        tooltipComponents.add(Component.translatable("des.superbwarfare.iff_1").withStyle(ChatFormatting.GRAY))
    }

    @EventBusSubscriber
    companion object {
        @SubscribeEvent
        fun onServerTick(event: ServerTickEvent.Post) {
            val server = event.server

            for (player in server.playerList.players) {
                if (!player.isAlive) continue
                // 将自己注册到 ServerSyncedEntityHandler，供雷达等系统发现
                ServerSyncedEntityHandler.register(player)

                // 向所有队友同步自身位置
                val dim = player.level().dimension().location()
                val surfaceY = player.level().getHeight(
                    net.minecraft.world.level.levelgen.Heightmap.Types.WORLD_SURFACE,
                    player.blockX, player.blockZ
                )
                val hag = (player.y - surfaceY).coerceAtLeast(0.0)
                val synced = EntitySyncMessage.SyncedEntity(
                    player.id,
                    BuiltInRegistries.ENTITY_TYPE.getKey(player.type),
                    player.position(),
                    null,
                    player.serializeNBT(player.level().registryAccess()),
                    player.yRot,
                    player.xRot,
                    heightAboveGround = hag,
                )
                val msg = EntitySyncMessage(dim, listOf(synced), true)
                for (teammate in server.playerList.players) {
                    if (teammate != player && teammate.isAlive
                        && teammate.level().dimension() == player.level().dimension()
                        && SeekTool.IS_FRIENDLY.test(teammate, player)
                    ) {
                        sendPacketTo(teammate, msg)
                    }
                }
            }
        }
    }
}
