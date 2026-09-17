package com.atsuishio.superbwarfare.recipe

import com.atsuishio.superbwarfare.Mod
import com.atsuishio.superbwarfare.init.ModRecipes
import com.atsuishio.superbwarfare.serialization.kserializer.SerializedIngredient
import com.atsuishio.superbwarfare.tools.TagDataParser
import com.atsuishio.superbwarfare.tools.serializerToJsonMapCodec
import com.mojang.serialization.MapCodec
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import net.minecraft.core.HolderLookup
import net.minecraft.core.RegistryAccess
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import kotlin.jvm.optionals.getOrNull

class ResearchingRecipe(
    val input: Ingredient,
    val base: Ingredient,
    val addition: Ingredient,
    val special: Ingredient,
    val selectable: Boolean,
    val color: Int,
    val time: Int,
    val result: Result
) : Recipe<RecipeInput> {
    companion object {
        fun create(
            input: Ingredient,
            base: Ingredient,
            addition: Ingredient,
            special: Ingredient,
            selectable: Boolean,
            color: Int,
            time: Int,
            count: Int,
            result: Item
        ): ResearchingRecipe {
            return ResearchingRecipe(
                input,
                base,
                addition,
                special,
                selectable,
                color,
                time,
                Result(item = BuiltInRegistries.ITEM.getKey(result).toString(), count = count)
            )
        }

        fun create(
            input: Ingredient,
            base: Ingredient,
            addition: Ingredient,
            special: Ingredient,
            selectable: Boolean,
            color: Int,
            time: Int,
            count: Int,
            tag: TagKey<Item>
        ): ResearchingRecipe {
            return ResearchingRecipe(
                input,
                base,
                addition,
                special,
                selectable,
                color,
                time,
                Result(tag = tag.location.toString(), count = count)
            )
        }
    }

    override fun matches(container: RecipeInput, level: Level): Boolean {
        if (container.size() < 4) return false
        return input.test(container.getItem(0))
                && base.test(container.getItem(1))
                && addition.test(container.getItem(2))
                && special.test(container.getItem(3))
    }

    override fun assemble(input: RecipeInput, registries: HolderLookup.Provider): ItemStack =
        result.getResult().copy()

    override fun isSpecial() = true

    override fun canCraftInDimensions(pWidth: Int, pHeight: Int) = true

    override fun getResultItem(registries: HolderLookup.Provider): ItemStack = result.getResult().copy()

    override fun getSerializer(): RecipeSerializer<*> = ModRecipes.RESEARCHING_SERIALIZER

    override fun getType(): RecipeType<*> = ModRecipes.RESEARCHING_TYPE

    /** 配方产物，由 kotlinx.serialization 从 `result` 字段反序列化。 */
    @Serializable
    data class Result(
        @SerialName("item") var item: String = "",
        @SerialName("tag") var tag: String = "",
        @SerialName("count") var count: Int = 1,
        @SerialName("nbt") var nbt: JsonObject? = null,
    ) {
        @kotlinx.serialization.Transient
        @Transient
        var resultStack: ItemStack? = null

        @kotlinx.serialization.Transient
        @Transient
        var list: MutableList<Item>? = null

        fun getResult(): ItemStack {
            if (resultStack != null) return resultStack!!

            if (item.isNotEmpty()) {
                val itemKey = ResourceLocation.parse(item)
                val itemValue = BuiltInRegistries.ITEM.getOptional(itemKey).getOrNull()
                if (itemValue == null) {
                    Mod.LOGGER.warn("invalid item: {}", item)
                    resultStack = ItemStack.EMPTY
                } else if (nbt != null) {
                    val tag = TagDataParser.parseObject(nbt)
                    val stackTag = CompoundTag()
                    stackTag.put("components", tag)
                    stackTag.putString("id", itemKey.toString())
                    stackTag.putInt("count", count)
                    resultStack = ItemStack.parseOptional(RegistryAccess.EMPTY, stackTag)
                } else {
                    resultStack = ItemStack(itemValue, count)
                }
            } else if (getResultList().isNotEmpty()) {
                resultStack = ItemStack(getResultList().random(), count)
            } else {
                resultStack = ItemStack.EMPTY
            }

            return resultStack!!
        }

        fun getResultList(): MutableList<Item> {
            if (list != null && list!!.isNotEmpty()) return list!!
            if (tag.isEmpty()) return mutableListOf()

            val itemTag = BuiltInRegistries.ITEM.getTag(TagKey.create(Registries.ITEM, ResourceLocation.parse(tag)))
                .map { items -> items.map { it.value() } }
                .getOrNull()
                ?: return mutableListOf()

            list = itemTag.toMutableList().sortedBy { it.descriptionId }.toMutableList()
            return list!!
        }

        fun getItemByIndex(index: Int): ItemStack {
            if (isRandom() && getResultList().size > index) {
                return ItemStack(getResultList()[index], count)
            }
            return getResult()
        }

        fun isRandom() = tag.isNotEmpty()

        fun rollItem(): ItemStack {
            if (isRandom() && getResultList().isNotEmpty()) {
                return ItemStack(getResultList().random(), count)
            }
            return getResult()
        }
    }

    object Serializer : RecipeSerializer<ResearchingRecipe> {
        @Serializable
        private data class RecipeData(
            @SerialName("input") val input: SerializedIngredient = Ingredient.EMPTY,
            @SerialName("base") val base: SerializedIngredient = Ingredient.EMPTY,
            @SerialName("addition") val addition: SerializedIngredient = Ingredient.EMPTY,
            @SerialName("special") val special: SerializedIngredient = Ingredient.EMPTY,
            @SerialName("selectable") val selectable: Boolean = false,
            @SerialName("color") val color: Int = 0,
            @SerialName("time") val time: Int = 1200,
            @SerialName("result") val result: Result = Result(),
        )

        private val DATA_CODEC = serializerToJsonMapCodec(RecipeData.serializer())

        val CODEC: MapCodec<ResearchingRecipe> = DATA_CODEC.xmap(
            { data ->
                ResearchingRecipe(
                    data.input,
                    data.base,
                    data.addition,
                    data.special,
                    data.selectable,
                    data.color.coerceIn(0, 4),
                    data.time,
                    data.result
                )
            },
            { recipe ->
                RecipeData(
                    recipe.input,
                    recipe.base,
                    recipe.addition,
                    recipe.special,
                    recipe.selectable,
                    recipe.color,
                    recipe.time,
                    recipe.result
                )
            }
        )

        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, ResearchingRecipe> =
            StreamCodec.of(this::toNetwork, this::fromNetwork)

        fun fromNetwork(buffer: RegistryFriendlyByteBuf): ResearchingRecipe {
            val input = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer)
            val base = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer)
            val addition = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer)
            val special = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer)
            val selectable = buffer.readBoolean()
            val color = buffer.readInt()
            val time = buffer.readInt()

            val res = Result()
            if (buffer.readBoolean()) {
                res.tag = buffer.readUtf()
            } else {
                res.resultStack = ItemStack.STREAM_CODEC.decode(buffer)
            }

            return ResearchingRecipe(input, base, addition, special, selectable, color, time, res)
        }

        fun toNetwork(buffer: RegistryFriendlyByteBuf, recipe: ResearchingRecipe) {
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.input)
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.base)
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.addition)
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.special)
            buffer.writeBoolean(recipe.selectable)
            buffer.writeInt(recipe.color)
            buffer.writeInt(recipe.time)

            val result = recipe.result
            val random = result.isRandom()
            buffer.writeBoolean(random)
            if (random) {
                buffer.writeUtf(result.tag)
            } else {
                ItemStack.STREAM_CODEC.encode(buffer, result.getResult())
            }
        }

        override fun codec(): MapCodec<ResearchingRecipe> = CODEC

        override fun streamCodec(): StreamCodec<RegistryFriendlyByteBuf, ResearchingRecipe> = STREAM_CODEC
    }
}
