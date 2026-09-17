package com.atsuishio.superbwarfare.recipe

import com.atsuishio.superbwarfare.Mod
import com.atsuishio.superbwarfare.data.DataLoader
import com.atsuishio.superbwarfare.init.ModRecipes
import com.atsuishio.superbwarfare.serialization.kserializer.SerializedIngredient
import com.atsuishio.superbwarfare.tools.GsonObject
import com.atsuishio.superbwarfare.tools.TagDataParser
import com.atsuishio.superbwarfare.tools.toKxJson
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import net.minecraft.core.RegistryAccess
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.ItemTags
import net.minecraft.world.SimpleContainer
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import net.minecraftforge.registries.ForgeRegistries

class ResearchingRecipe(
    val recipeId: ResourceLocation,
    val input: Ingredient,
    val base: Ingredient,
    val addition: Ingredient,
    val special: Ingredient,
    val selectable: Boolean,
    val color: Int,
    val time: Int,
    val result: Result
) : Recipe<SimpleContainer> {
    override fun matches(
        container: SimpleContainer,
        level: Level
    ): Boolean {
        if (container.containerSize < 4) {
            return false
        }
        return input.test(container.getItem(0))
                && base.test(container.getItem(1))
                && addition.test(container.getItem(2))
                && special.test(container.getItem(3))
    }

    override fun assemble(
        pContainer: SimpleContainer,
        pRegistryAccess: RegistryAccess
    ): ItemStack = this.result.getResult().copy()

    override fun isSpecial() = true

    override fun canCraftInDimensions(pWidth: Int, pHeight: Int) = true

    override fun getResultItem(pRegistryAccess: RegistryAccess): ItemStack = this.result.getResult().copy()

    override fun getId() = this.recipeId

    override fun getSerializer(): RecipeSerializer<*> = ModRecipes.RESEARCHING_SERIALIZER.get()

    override fun getType(): RecipeType<*> = ModRecipes.RESEARCHING_TYPE.get()

    /** 配方产物，由 kotlinx.serialization 从 `result` 字段反序列化 */
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
            if (this.resultStack != null) return this.resultStack!!
            if (!item.isEmpty()) {
                val item = ForgeRegistries.ITEMS.getValue(ResourceLocation(item))
                if (item == null) {
                    Mod.LOGGER.warn("invalid item: $item")
                    this.resultStack = ItemStack.EMPTY
                } else {
                    if (nbt != null) {
                        val tag = TagDataParser.parseObject(nbt)
                        val tmp = CompoundTag()
                        if (tag.contains("ForgeCaps")) {
                            tmp.put("ForgeCaps", tag.get("ForgeCaps"))
                            tag.remove("ForgeCaps")
                        }

                        tmp.put("tag", tag)
                        tmp.putString("id", this.item)
                        tmp.putInt("Count", count)
                        this.resultStack = ItemStack.of(tmp)
                    } else {
                        this.resultStack = ItemStack(item, count)
                    }
                }
            } else if (!this.getResultList().isEmpty()) {
                this.resultStack = ItemStack(this.getResultList().random(), count)
            } else {
                this.resultStack = ItemStack.EMPTY
            }

            return this.resultStack!!
        }

        fun getResultList(): MutableList<Item> {
            if (this.list != null && !this.list!!.isEmpty()) return this.list!!
            if (this.tag.isEmpty()) return mutableListOf()

            val tags = ForgeRegistries.ITEMS.tags() ?: return mutableListOf()
            val itemTag = tags.getTag(ItemTags.create(ResourceLocation(this.tag)))

            val list = mutableListOf<Item>()
            itemTag.forEach { list.add(it) }
            list.sortBy { it.descriptionId }
            this.list = list
            return this.list!!
        }

        fun getItemByIndex(index: Int): ItemStack {
            if (this.isRandom() && this.getResultList().size > index) {
                return ItemStack(this.getResultList()[index], count)
            }
            return this.getResult()
        }

        fun isRandom() = this.tag.isNotEmpty()

        fun rollItem(): ItemStack {
            if (this.isRandom() && !this.getResultList().isEmpty()) {
                return ItemStack(this.getResultList().random(), count)
            }
            return this.getResult()
        }
    }

    class Serializer : RecipeSerializer<ResearchingRecipe> {
        /** 配方 JSON 的 kotlinx 映射 */
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

        /**
         * [GsonObject] 是 `tools.JsonUtil` 里对 Gson `JsonObject` 的 typealias：
         * 原版 `RecipeSerializer` 接口只收 Gson 的 JsonObject，这个入参类型无法改，
         * 所以这里立刻把它转成 kotlinx 的 JsonObject，配方解析全部由 kotlinx.serialization 完成。
         */
        override fun fromJson(
            id: ResourceLocation,
            json: GsonObject
        ): ResearchingRecipe {
            val data = DataLoader.JSON.decodeFromJsonElement(
                RecipeData.serializer(),
                json.toKxJson().jsonObject
            )
            return ResearchingRecipe(
                id,
                data.input,
                data.base,
                data.addition,
                data.special,
                data.selectable,
                data.color.coerceIn(0, 4),
                data.time,
                data.result
            )
        }

        override fun fromNetwork(
            id: ResourceLocation,
            buffer: FriendlyByteBuf
        ): ResearchingRecipe {
            val input = Ingredient.fromNetwork(buffer)
            val base = Ingredient.fromNetwork(buffer)
            val addition = Ingredient.fromNetwork(buffer)
            val special = Ingredient.fromNetwork(buffer)
            val selectable = buffer.readBoolean()
            val color = buffer.readInt()
            val time = buffer.readInt()

            val res = Result()
            val flag = buffer.readBoolean()
            if (flag) {
                res.tag = buffer.readUtf()
            } else {
                res.resultStack = buffer.readItem()
            }
            return ResearchingRecipe(id, input, base, addition, special, selectable, color, time, res)
        }

        override fun toNetwork(
            buffer: FriendlyByteBuf,
            recipe: ResearchingRecipe
        ) {
            recipe.input.toNetwork(buffer)
            recipe.base.toNetwork(buffer)
            recipe.addition.toNetwork(buffer)
            recipe.special.toNetwork(buffer)
            buffer.writeBoolean(recipe.selectable)
            buffer.writeInt(recipe.color)
            buffer.writeInt(recipe.time)

            val res = recipe.result
            val flag = res.tag.isNotEmpty()
            buffer.writeBoolean(flag)
            if (flag) {
                buffer.writeUtf(res.tag)
            } else {
                buffer.writeItem(res.getResult())
            }
        }
    }
}
