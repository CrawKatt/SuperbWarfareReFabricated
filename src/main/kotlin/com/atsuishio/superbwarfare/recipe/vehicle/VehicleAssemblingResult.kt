package com.atsuishio.superbwarfare.recipe.vehicle

import com.atsuishio.superbwarfare.Mod
import com.atsuishio.superbwarfare.item.container.ContainerBlockItem.Companion.createInstance
import com.atsuishio.superbwarfare.tools.TagDataParser
import com.mojang.serialization.MapCodec
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import net.minecraft.core.RegistryAccess
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.ItemStack
import kotlin.jvm.optionals.getOrNull

/** 载具装配配方的产物，由 kotlinx.serialization 从 `result` 字段反序列化。 */
@Serializable
data class VehicleAssemblingResult(
    @SerialName("item")
    var itemString: String = "",

    @SerialName("entity")
    var entityTypeString: String = "",

    @SerialName("count")
    var count: Int = 1,

    @SerialName("nbt")
    var nbt: JsonObject? = null,
) {
    @kotlinx.serialization.Transient
    @Transient
    @get:JvmName("result")
    var result: ItemStack? = null

    fun getResult(): ItemStack {
        if (result != null) return result!!

        if (entityTypeString.isNotEmpty()) {
            val type = EntityType.byString(entityTypeString).getOrNull()
            if (type == null) {
                Mod.LOGGER.warn("invalid entity type: {}", entityTypeString)
                result = ItemStack.EMPTY
            } else {
                result = createInstance(type).copyWithCount(count)
            }
        } else if (itemString.isNotEmpty()) {
            val itemKey = ResourceLocation.parse(itemString)
            val item = BuiltInRegistries.ITEM.getOptional(itemKey).getOrNull()
            if (item == null) {
                Mod.LOGGER.warn("invalid item: {}", itemString)
                result = ItemStack.EMPTY
            } else if (nbt != null) {
                val tag = TagDataParser.parseObject(nbt)
                val stackTag = CompoundTag()
                stackTag.put("components", tag)
                stackTag.putString("id", itemKey.toString())
                stackTag.putInt("count", count)
                result = ItemStack.EMPTY
                ItemStack.parse(RegistryAccess.EMPTY, stackTag).ifPresent { result = it }
            } else {
                result = ItemStack(item, count)
            }
        } else {
            result = ItemStack.EMPTY
        }

        return result!!
    }

    companion object {
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, VehicleAssemblingResult> =
            StreamCodec.composite(
                ByteBufCodecs.STRING_UTF8, { it.itemString },
                ByteBufCodecs.STRING_UTF8, { it.entityTypeString },
                ByteBufCodecs.VAR_INT, { it.count },
                { item, entity, count -> VehicleAssemblingResult(item, entity, count) }
            )
    }
}
