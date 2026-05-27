package com.atsuishio.superbwarfare.init;

import com.atsuishio.superbwarfare.Mod;
import com.atsuishio.superbwarfare.item.common.container.SmallContainerBlockItem;
import com.google.common.collect.ImmutableSet;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;
import java.util.function.Supplier;

public class ModVillagers {

    public static final Supplier<PoiType> ARMORY_POI = Registration.custom(net.minecraft.core.registries.BuiltInRegistries.POINT_OF_INTEREST_TYPE, "armory",
            () -> new PoiType(ImmutableSet.copyOf(ModBlocks.REFORGING_TABLE.get().getStateDefinition().getPossibleStates()), 1, 1));

    public static final Supplier<VillagerProfession> ARMORY = Registration.custom(net.minecraft.core.registries.BuiltInRegistries.VILLAGER_PROFESSION, "armory",
            () -> new VillagerProfession("armory", holder -> holder.get() == ARMORY_POI.get(), holder -> holder.get() == ARMORY_POI.get(),
                    ImmutableSet.of(), ImmutableSet.of(), null));

    // TODO: Fabric does not have VillagerTradesEvent or WandererTradesEvent.
    // Use net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper or
    // VillagerTrades registers directly via VillagerTrades.ITEM_LISTINGS.
    // Below is the converted trade registration kept for reference:

    public static void addCustomTrades() {
        // Example: register(VillagerProfession, 1, trades)
        // See TradeOfferHelper from fabric-api
    }

    public static void addWandererTrade() {
        // Wanderer trades need different handling in Fabric
    }
}
