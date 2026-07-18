package com.atsuishio.superbwarfare.init;

import com.atsuishio.superbwarfare.Mod;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.List;

public class ModWorldgen {

    private static final List<ResourceKey<PlacedFeature>> OVERWORLD_ORES = List.of(
            ResourceKey.create(Registries.PLACED_FEATURE, Mod.loc("silver_ore")),
            ResourceKey.create(Registries.PLACED_FEATURE, Mod.loc("galena_ore")),
            ResourceKey.create(Registries.PLACED_FEATURE, Mod.loc("scheelite_ore")),
            ResourceKey.create(Registries.PLACED_FEATURE, Mod.loc("deepslate_silver_ore")),
            ResourceKey.create(Registries.PLACED_FEATURE, Mod.loc("deepslate_galena_ore")),
            ResourceKey.create(Registries.PLACED_FEATURE, Mod.loc("deepslate_scheelite_ore"))
    );

    private static final List<ResourceKey<Biome>> SENPAI_BIOMES = List.of(
            Biomes.BADLANDS, Biomes.BAMBOO_JUNGLE, Biomes.BIRCH_FOREST,
            Biomes.CHERRY_GROVE, Biomes.DARK_FOREST, Biomes.DESERT,
            Biomes.DRIPSTONE_CAVES, Biomes.ERODED_BADLANDS, Biomes.FLOWER_FOREST,
            Biomes.FOREST, Biomes.FROZEN_PEAKS, Biomes.GROVE,
            Biomes.ICE_SPIKES, Biomes.JAGGED_PEAKS, Biomes.JUNGLE,
            Biomes.LUSH_CAVES, Biomes.MANGROVE_SWAMP, Biomes.MEADOW,
            Biomes.OLD_GROWTH_BIRCH_FOREST, Biomes.OLD_GROWTH_PINE_TAIGA,
            Biomes.OLD_GROWTH_SPRUCE_TAIGA, Biomes.PLAINS, Biomes.SAVANNA,
            Biomes.SAVANNA_PLATEAU, Biomes.SNOWY_SLOPES, Biomes.SNOWY_BEACH,
            Biomes.SNOWY_PLAINS, Biomes.SNOWY_TAIGA, Biomes.STONY_PEAKS,
            Biomes.STONY_SHORE, Biomes.SUNFLOWER_PLAINS, Biomes.SWAMP,
            Biomes.TAIGA, Biomes.WINDSWEPT_FOREST, Biomes.WINDSWEPT_GRAVELLY_HILLS,
            Biomes.WINDSWEPT_HILLS, Biomes.WINDSWEPT_SAVANNA, Biomes.WOODED_BADLANDS
    );

    public static void register() {
        for (var feature : OVERWORLD_ORES) {
            BiomeModifications.addFeature(
                    BiomeSelectors.foundInOverworld(),
                    GenerationStep.Decoration.UNDERGROUND_ORES,
                    feature
            );
        }

        BiomeModifications.addSpawn(
                BiomeSelectors.includeByKey(SENPAI_BIOMES),
                MobCategory.MONSTER,
                ModEntities.SENPAI.get(),
                20, 4, 4
        );
    }
}
