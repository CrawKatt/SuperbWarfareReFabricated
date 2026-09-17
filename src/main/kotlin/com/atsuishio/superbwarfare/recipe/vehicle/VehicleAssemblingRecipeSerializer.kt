package com.atsuishio.superbwarfare.recipe.vehicle

import com.atsuishio.superbwarfare.data.DataLoader
import com.atsuishio.superbwarfare.tools.GsonObject
import com.atsuishio.superbwarfare.tools.toKxJson
import kotlinx.serialization.json.jsonObject
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeSerializer

class VehicleAssemblingRecipeSerializer : RecipeSerializer<VehicleAssemblingRecipe> {
    /**
     * [GsonObject] 是 `tools.JsonUtil` 里对 Gson `JsonObject` 的 typealias：
     * 原版 `RecipeSerializer` 接口只收 Gson 的 JsonObject，这个入参类型无法改，
     * 所以这里立刻把它转成 kotlinx 的 JsonObject，配方解析全部由 kotlinx.serialization 完成。
     */
    override fun fromJson(pRecipeId: ResourceLocation, pSerializedRecipe: GsonObject): VehicleAssemblingRecipe {
        val data = DataLoader.JSON.decodeFromJsonElement(
            VehicleAssemblingRecipeData.serializer(),
            pSerializedRecipe.toKxJson().jsonObject
        )
        return VehicleAssemblingRecipe(pRecipeId, data)
    }

    override fun fromNetwork(pRecipeId: ResourceLocation, pBuffer: FriendlyByteBuf): VehicleAssemblingRecipe {
        val count = pBuffer.readVarInt()
        val ingredients = mutableListOf<VehicleAssemblingIngredient>()
        repeat(count) {
            val assemblingIngredient = VehicleAssemblingIngredient()
            assemblingIngredient.ingredientObject = Ingredient.fromNetwork(pBuffer)
            assemblingIngredient.count = pBuffer.readInt()
            ingredients.add(assemblingIngredient)
        }
        val category = pBuffer.readEnum(VehicleAssemblingRecipe.Category::class.java)
        val resultItem = pBuffer.readItem()
        val result = VehicleAssemblingResult()
        result.result = resultItem
        return VehicleAssemblingRecipe(pRecipeId, category, result, ingredients)
    }

    override fun toNetwork(pBuffer: FriendlyByteBuf, pRecipe: VehicleAssemblingRecipe) {
        pBuffer.writeVarInt(pRecipe.inputs.size)
        for (ingredient in pRecipe.inputs) {
            ingredient.ingredient.toNetwork(pBuffer)
            pBuffer.writeInt(ingredient.count)
        }
        pBuffer.writeEnum(pRecipe.category)
        pBuffer.writeItem(pRecipe.result.getResult())
    }
}
