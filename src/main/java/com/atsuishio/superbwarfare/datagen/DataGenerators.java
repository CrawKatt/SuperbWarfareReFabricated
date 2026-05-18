package com.atsuishio.superbwarfare.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class DataGenerators implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        pack.addProvider(ModLootTableProvider::create);
        pack.addProvider(ModRecipeProvider::new);
        pack.addProvider((output, registriesFuture) -> new ModBlockStateProvider(output));
        pack.addProvider((output, registriesFuture) -> new ModItemModelProvider(output));

        var blockTags = pack.addProvider(ModBlockTagProvider::new);
        pack.addProvider((output, registriesFuture) -> new ModItemTagProvider(output, registriesFuture, blockTags));

        pack.addProvider(ModEntityTypeTagProvider::new);
        pack.addProvider(ModDamageTypeTagProvider::new);
        pack.addProvider(ModAdvancementProvider::new);
    }
}
