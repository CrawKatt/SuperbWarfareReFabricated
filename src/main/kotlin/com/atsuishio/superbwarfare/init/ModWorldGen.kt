package com.atsuishio.superbwarfare.init

import com.atsuishio.superbwarfare.Mod.Companion.loc
import net.fabricmc.fabric.api.biome.v1.BiomeModifications
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.entity.MobCategory
import net.minecraft.world.level.levelgen.GenerationStep

object ModWorldGen {
    private val OVERWORLD = TagKey.create(
        Registries.BIOME,
        ResourceLocation.fromNamespaceAndPath("c", "is_overworld")
    )

    private val ORES = listOf(
        "deepslate_galena_ore",
        "deepslate_scheelite_ore",
        "deepslate_silver_ore",
        "galena_ore",
        "scheelite_ore",
        "silver_ore"
    )

    @JvmStatic
    fun init() {
        val overworld = BiomeSelectors.tag(OVERWORLD)
        ORES.forEach { name ->
            BiomeModifications.addFeature(
                overworld,
                GenerationStep.Decoration.UNDERGROUND_ORES,
                ResourceKey.create(Registries.PLACED_FEATURE, loc(name))
            )
        }

        BiomeModifications.addSpawn(overworld, MobCategory.MONSTER, ModEntities.SENPAI, 20, 4, 4)
        BiomeModifications.addSpawn(overworld, MobCategory.MONSTER, ModEntities.STEEL_COIL, 5, 1, 3)
    }
}
