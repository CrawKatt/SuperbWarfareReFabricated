package com.atsuishio.superbwarfare.network.message.send

import com.atsuishio.superbwarfare.init.ModBlockEntities
import com.atsuishio.superbwarfare.init.ModItems
import com.atsuishio.superbwarfare.item.container.SmallContainerBlockItem
import com.atsuishio.superbwarfare.ksp.annotation.RegisterPacket
import com.atsuishio.superbwarfare.network.PayloadContext
import com.atsuishio.superbwarfare.network.ServerPacketPayload
import com.atsuishio.superbwarfare.serialization.kserializer.SerializedResourceLocation
import com.atsuishio.superbwarfare.serialization.kserializer.SerializedTag
import kotlinx.serialization.Serializable
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.nbt.CompoundTag
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.storage.loot.LootTable

@Serializable
@RegisterPacket
data class CreativeContainerStackMessage(
    val slot: Int,
    val item: SerializedResourceLocation,
    val data: SerializedTag
) : ServerPacketPayload() {
    override fun PayloadContext.handler() {
        val player = sender()
        if (!player.isCreative) return
        if (slot !in 1..45) return

        val tag = data as? CompoundTag ?: return
        val item = BuiltInRegistries.ITEM.get(item)
        val stack = when (item) {
            ModItems.CONTAINER -> {
                if (!tag.contains("EntityType", 8)) return
                ItemStack(ModItems.CONTAINER).also {
                    BlockItem.setBlockEntityData(it, ModBlockEntities.CONTAINER, tag.copy())
                }
            }
            ModItems.LUCKY_CONTAINER -> {
                if (!tag.contains("Location", 8)) return
                ItemStack(ModItems.LUCKY_CONTAINER).also {
                    BlockItem.setBlockEntityData(it, ModBlockEntities.LUCKY_CONTAINER, tag.copy())
                }
            }
            ModItems.SMALL_CONTAINER -> {
                if (!tag.contains("LootTable", 8)) return
                val location = ResourceLocation.tryParse(tag.getString("LootTable")) ?: return
                val lootTable = ResourceKey.create(Registries.LOOT_TABLE, location)
                SmallContainerBlockItem.createInstance(lootTable, tag.getLong("LootTableSeed"))
            }
            else -> return
        }

        player.inventoryMenu.getSlot(slot).setByPlayer(stack.copyWithCount(1))
        player.inventoryMenu.broadcastChanges()
    }
}
