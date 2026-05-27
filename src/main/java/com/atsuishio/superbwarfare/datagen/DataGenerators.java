package com.atsuishio.superbwarfare.datagen;

import com.atsuishio.superbwarfare.Mod;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;

import java.util.concurrent.CompletableFuture;

public class DataGenerators {

    public static void gatherData(DataGenerator generator, PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        generator.addProvider(true, ModLootTableProvider.create(packOutput));
        generator.addProvider(true, new ModRecipeProvider(packOutput));
        generator.addProvider(true, new ModBlockStateProvider(packOutput));
        generator.addProvider(true, new ModItemModelProvider(packOutput));
        ModBlockTagProvider tagProvider = generator.addProvider(true, new ModBlockTagProvider(packOutput, lookupProvider));
        generator.addProvider(true, new ModItemTagProvider(packOutput, lookupProvider, tagProvider.contentsGetter()));
        generator.addProvider(true, new ModEntityTypeTagProvider(packOutput, lookupProvider));
        generator.addProvider(true, new ModDamageTypeTagProvider(packOutput, lookupProvider));
        generator.addProvider(true, new ModAdvancementProvider(packOutput));
    }
}
