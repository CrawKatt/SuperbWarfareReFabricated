package com.atsuishio.superbwarfare.recipe.vehicle

import com.atsuishio.superbwarfare.Mod
import com.atsuishio.superbwarfare.data.DeserializeFromString
import com.atsuishio.superbwarfare.data.StringInstanceBuilder
import com.atsuishio.superbwarfare.data.StringOrObjectFactory
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.crafting.Ingredient
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.math.max

@StringOrObjectFactory(VehicleAssemblingIngredient.InstanceBuilder::class)
@Serializable
class VehicleAssemblingIngredient : DeserializeFromString {
    @SerialName("ingredient")
    var ingredientString: String = ""

    @JvmField
    @SerialName("count")
    var count: Int = 1

    @kotlinx.serialization.Transient
    @Transient
    var ingredientObject: Ingredient? = null

    constructor()

    constructor(ingredientString: String, count: Int) {
        this.ingredientString = ingredientString
        this.count = count
    }

    val ingredient: Ingredient
        get() {
            if (ingredientObject == null) deserializeFromString(ingredientString)
            return ingredientObject!!
        }

    override fun deserializeFromString(str: String) {
        ingredientString = str
        val matcher: Matcher = INGREDIENT_PATTERN.matcher(str)
        if (!matcher.matches()) {
            Mod.LOGGER.warn("invalid vehicle assembling ingredient: {}", str)
            ingredientObject = Ingredient.EMPTY
            return
        }

        val countString = matcher.group("count")
        if (countString.isNotEmpty()) count = max(1, countString.toInt())

        val id = matcher.group("id")
        ingredientObject = if (matcher.group("prefix") == "#") {
            Ingredient.of(TagKey.create(Registries.ITEM, ResourceLocation.parse(id)))
        } else {
            Ingredient.of(BuiltInRegistries.ITEM.get(ResourceLocation.parse(id)))
        }
    }

    object InstanceBuilder : StringInstanceBuilder<VehicleAssemblingIngredient> {
        override fun fromString(value: String) = VehicleAssemblingIngredient().apply {
            deserializeFromString(value)
        }
    }

    companion object {
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, VehicleAssemblingIngredient> =
            StreamCodec.composite(
                ByteBufCodecs.STRING_UTF8, { it.ingredientString },
                ByteBufCodecs.VAR_INT, { it.count },
                ::VehicleAssemblingIngredient
            )

        private val INGREDIENT_PATTERN: Pattern =
            Pattern.compile("^(?<count>(\\d+)?)\\s*(x\\s*)?(?<prefix>#?)(?<id>\\w+:\\S+)$")
    }
}
