package com.atsuishio.superbwarfare.init;

import com.atsuishio.superbwarfare.Mod;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

public class ModLootModifier {

    private static final ResourceLocation COMMON_BP = Mod.loc("chests/blue_print_common");
    private static final ResourceLocation RARE_BP = Mod.loc("chests/blue_print_rare");
    private static final ResourceLocation EPIC_BP = Mod.loc("chests/blue_print_epic");
    private static final ResourceLocation ANCIENT_BP = Mod.loc("chests/ancient_cpu");

    public static void register() {
        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
            addPoolIfTarget(id, tableBuilder, "simple_dungeon", COMMON_BP);
            addPoolIfTarget(id, tableBuilder, "abandoned_mineshaft", COMMON_BP);
            addPoolIfTarget(id, tableBuilder, "shipwreck_map", COMMON_BP);
            addPoolIfTarget(id, tableBuilder, "shipwreck_supply", COMMON_BP);
            addPoolIfTarget(id, tableBuilder, "shipwreck_treasure", COMMON_BP);
            addPoolIfTarget(id, tableBuilder, "ruined_portal", COMMON_BP);

            addPoolIfTarget(id, tableBuilder, "ancient_city", RARE_BP);
            addPoolIfTarget(id, tableBuilder, "ancient_city_ice_box", RARE_BP);
            addPoolIfTarget(id, tableBuilder, "bastion_bridge", RARE_BP);
            addPoolIfTarget(id, tableBuilder, "bastion_hoglin_stable", RARE_BP);
            addPoolIfTarget(id, tableBuilder, "bastion_other", RARE_BP);
            addPoolIfTarget(id, tableBuilder, "buried_treasure", RARE_BP);
            addPoolIfTarget(id, tableBuilder, "desert_pyramid", RARE_BP);
            addPoolIfTarget(id, tableBuilder, "igloo", RARE_BP);
            addPoolIfTarget(id, tableBuilder, "jungle_temple", RARE_BP);

            addPoolIfTarget(id, tableBuilder, "pillager_outpost", EPIC_BP);
            addPoolIfTarget(id, tableBuilder, "stronghold_library", EPIC_BP);
            addPoolIfTarget(id, tableBuilder, "woodland_mansion", EPIC_BP);
            addPoolIfTarget(id, tableBuilder, "end_city_treasure", EPIC_BP);

            addPoolIfTarget(id, tableBuilder, "ancient_city", ANCIENT_BP);
        });
    }

    private static void addPoolIfTarget(ResourceLocation tableId, net.minecraft.world.level.storage.loot.LootTable.Builder tableBuilder, String path, ResourceLocation lootTable) {
        var target = new ResourceLocation("minecraft", "chests/" + path);
        if (target.equals(tableId)) {
            tableBuilder.pool(LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1.0f))
                    .add(LootTableReference.lootTableReference(lootTable))
                    .build());
        }
    }
}
