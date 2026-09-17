package com.atsuishio.superbwarfare.recipe.vehicle

import com.atsuishio.superbwarfare.data.SingleOrList
import com.atsuishio.superbwarfare.data.StringOrObject
import com.atsuishio.superbwarfare.tools.serializerToJsonMapCodec
import com.mojang.serialization.MapCodec
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.crafting.RecipeSerializer

object VehicleAssemblingRecipeSerializer : RecipeSerializer<VehicleAssemblingRecipe> {
    private val DATA_CODEC = serializerToJsonMapCodec(VehicleAssemblingRecipeData.serializer())

    val CODEC: MapCodec<VehicleAssemblingRecipe> = DATA_CODEC.xmap(
        { data ->
            VehicleAssemblingRecipe(
                data.getInputs() ?: mutableListOf(),
                VehicleAssemblingRecipe.Category.getCategory(data.category),
                data.result ?: VehicleAssemblingResult()
            )
        },
        { recipe ->
            VehicleAssemblingRecipeData(
                SingleOrList(recipe.inputs.map { StringOrObject(it) }.toMutableList()),
                recipe.result,
                recipe.category.typeName
            )
        }
    )

    val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, VehicleAssemblingRecipe> =
        StreamCodec.composite(
            VehicleAssemblingIngredient.STREAM_CODEC.apply(ByteBufCodecs.list()),
            { it.inputs },
            ByteBufCodecs.STRING_UTF8,
            { it.category.typeName },
            VehicleAssemblingResult.STREAM_CODEC,
            { it.result },
            { inputs, category, result ->
                VehicleAssemblingRecipe(
                    inputs,
                    VehicleAssemblingRecipe.Category.getCategory(category),
                    result
                )
            }
        )

    override fun codec(): MapCodec<VehicleAssemblingRecipe> = CODEC

    override fun streamCodec(): StreamCodec<RegistryFriendlyByteBuf, VehicleAssemblingRecipe> = STREAM_CODEC
}
