package com.atsuishio.superbwarfare.item.curio

import com.atsuishio.superbwarfare.config.server.MiscConfig
import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity
import com.atsuishio.superbwarfare.init.ModItems
import com.atsuishio.superbwarfare.network.message.receive.EntitySyncMessage
import com.atsuishio.superbwarfare.network.message.receive.PlayerInfoSyncMessage
import com.atsuishio.superbwarfare.tools.SeekTool
import com.atsuishio.superbwarfare.tools.VectorTool
import com.atsuishio.superbwarfare.tools.sendPacketTo
import dev.emi.trinkets.api.SlotReference
import dev.emi.trinkets.api.Trinket
import dev.emi.trinkets.api.TrinketsApi
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.minecraft.ChatFormatting
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.nbt.CompoundTag
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
            if (!MiscConfig.SYNC_ENTITY_OVER_RANGE.get()) return
            if (server.tickCount % MiscConfig.SYNC_ENTITY_INTERVAL.get() != 0) return

            for (level in server.allLevels) {
                val entities = level.allEntities
                    .asSequence()
                    .filter { it is VehicleEntity && SeekTool.NOT_IN_SMOKE.test(it) }
                    .toList()

                val players = server.playerList.players
                for (player in players) {
                    if (!player.isAlive) continue

                    val hasIff = TrinketsApi.getTrinketComponent(player)
                        .map { it.isEquipped(ModItems.IFF) }
                        .orElse(false)
                    if (!hasIff) continue

                    val list = entities.mapNotNull {
                        if (!SeekTool.IS_FRIENDLY.test(player, it)) return@mapNotNull null
                        EntitySyncMessage.SyncedEntity(
                            it.id,
                            BuiltInRegistries.ENTITY_TYPE.getKey(it.type),
                            it.position(),
                            it.deltaMovement,
                            CompoundTag().also { tag -> it.saveWithoutId(tag) }
                        )
                    }.toList()
                    sendPacketTo(player, EntitySyncMessage(level.dimension().location(), list, true))

                    val playerList = players
                        .asSequence()
                        .mapNotNull {
                            if (level != it.level()) return@mapNotNull null
                            if (!SeekTool.IS_FRIENDLY.test(player, it)) return@mapNotNull null
                            val vehicle = it.vehicle
                            if (vehicle != null) {
                                PlayerInfoSyncMessage.SyncedPlayerInfo(
                                    it.uuid,
                                    if (vehicle is VehicleEntity)
                                        VectorTool.lerpGetEntityBoundingBoxCenter(vehicle, 1f)
                                    else it.position(),
                                    it.displayName.string,
                                    onVehicle = true,
                                    it == vehicle.firstPassenger
                                )
                            } else {
                                PlayerInfoSyncMessage.SyncedPlayerInfo(
                                    it.uuid,
                                    it.position(),
                                    it.displayName.string,
                                    onVehicle = false,
                                    isDriver = false
                                )
                            }
                        }.toList()
                    sendPacketTo(player, PlayerInfoSyncMessage(level.dimension().location(), playerList))
                }
            }
        }
    }
}
