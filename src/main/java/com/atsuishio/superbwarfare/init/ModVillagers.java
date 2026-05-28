package com.atsuishio.superbwarfare.init;

import com.atsuishio.superbwarfare.Mod;
import com.google.common.collect.ImmutableSet;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;

import java.util.List;
import java.util.function.Supplier;

public class ModVillagers {

    public static final Supplier<PoiType> ARMORY_POI = Registration.custom(net.minecraft.core.registries.BuiltInRegistries.POINT_OF_INTEREST_TYPE, "armory",
            () -> new PoiType(ImmutableSet.copyOf(ModBlocks.REFORGING_TABLE.get().getStateDefinition().getPossibleStates()), 1, 1));

    public static final Supplier<VillagerProfession> ARMORY = Registration.custom(net.minecraft.core.registries.BuiltInRegistries.VILLAGER_PROFESSION, "armory",
            () -> new VillagerProfession("armory", holder -> holder.value() == ARMORY_POI.get(), holder -> holder.value() == ARMORY_POI.get(),
                    ImmutableSet.of(), ImmutableSet.of(), null));

    public static void addCustomTrades() {
        TradeOfferHelper.registerVillagerOffers(ARMORY.get(), 1, factories -> {
            factories.add((entity, random) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 5),
                    new ItemStack(ModItems.STEEL_INGOT.get(), 4),
                    16, 2, 0.05f
            ));
            factories.add((entity, random) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 8),
                    new ItemStack(ModItems.LEAD_INGOT.get(), 4),
                    16, 2, 0.05f
            ));
        });

        TradeOfferHelper.registerVillagerOffers(ARMORY.get(), 2, factories -> {
            factories.add((entity, random) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 16),
                    new ItemStack(Items.IRON_INGOT, 12),
                    new ItemStack(ModItems.TUNGSTEN_INGOT.get(), 4),
                    8, 5, 0.05f
            ));
            factories.add((entity, random) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 10),
                    new ItemStack(ModItems.HAMMER.get()),
                    4, 10, 0.05f
            ));
        });

        TradeOfferHelper.registerVillagerOffers(ARMORY.get(), 3, factories -> {
            factories.add((entity, random) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 24),
                    new ItemStack(ModItems.CROWBAR.get()),
                    2, 15, 0.05f
            ));
        });
    }

    public static void addWandererTrade() {
        TradeOfferHelper.registerWanderingTraderOffers(1, factories -> {
            factories.add((entity, random) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 12),
                    new ItemStack(ModItems.SHORTCUT_PACK.get()),
                    2, 8, 0.1f
            ));
        });
    }

    public static void register() {
        addCustomTrades();
        addWandererTrade();
    }
}
