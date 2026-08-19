package com.atsuishio.superbwarfare.datagen

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator

class DataGenerators : DataGeneratorEntrypoint {
    override fun onInitializeDataGenerator(fabricDataGenerator: FabricDataGenerator) {
        val pack = fabricDataGenerator.createPack()

        pack.addProvider { output -> ModLootTableProvider.create(output) }
        pack.addProvider { output -> ModRecipeProvider(output) }
        pack.addProvider { output -> ModBlockStateProvider(output) }
        pack.addProvider { output -> ModItemModelProvider(output) }

        val tagProvider = pack.addProvider { output, registries -> ModBlockTagProvider(output, registries) }
        pack.addProvider { output, registries -> ModItemTagProvider(output, registries, tagProvider.contentsGetter()) }
        pack.addProvider { output, registries -> ModEntityTypeTagProvider(output, registries) }
        pack.addProvider { output, registries -> ModDamageTypeTagProvider(output, registries) }
        pack.addProvider { output -> ModAdvancementProvider(output) }
        pack.addProvider { output, registries -> ModPerkTagProvider(output, registries) }
        pack.addProvider { output -> ModWreckageLootProvider(output) }
    }
}
