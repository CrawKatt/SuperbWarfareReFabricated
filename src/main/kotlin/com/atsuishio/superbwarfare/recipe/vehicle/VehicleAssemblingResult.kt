package com.atsuishio.superbwarfare.recipe.vehicle

import com.atsuishio.superbwarfare.Mod
import com.atsuishio.superbwarfare.item.container.ContainerBlockItem.Companion.createInstance
import com.atsuishio.superbwarfare.tools.TagDataParser
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.nbt.CompoundTag
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items

/**
 * 载具装配配方的产物（`result` 字段），由 kotlinx.serialization 反序列化。
 *
 * [result] 是按需构建的运行时缓存，不参与序列化。
 */
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
        if (this.result != null) return this.result!!

        if (!entityTypeString.isEmpty()) {
            val type = EntityType.byString(entityTypeString).orElse(null)
            if (type == null) {
                Mod.LOGGER.warn("invalid entity type: {}", entityTypeString)
                this.result = ItemStack.EMPTY
            } else {
                this.result = createInstance(type).copyWithCount(count)
            }
        } else if (!itemString.isEmpty()) {
            val item = BuiltInRegistries.ITEM.get(ResourceLocation(itemString))
            if (item == Items.AIR) {
                Mod.LOGGER.warn("invalid item: {}", itemString)
                this.result = ItemStack.EMPTY
            } else {
                if (nbt != null) {
                    val tag = TagDataParser.parseObject(nbt)
                    val tmp = CompoundTag()
                    tmp.put("tag", tag)
                    tmp.putString("id", itemString)
                    tmp.putInt("Count", count)
                    this.result = ItemStack.of(tmp)
                } else {
                    this.result = ItemStack(item, count)
                }
            }
        } else {
            this.result = ItemStack.EMPTY
        }

        return this.result!!
    }
}
