package com.atsuishio.superbwarfare.init;

import com.atsuishio.superbwarfare.Mod;
import com.atsuishio.superbwarfare.item.common.container.SmallContainerBlockItem;
import com.google.common.collect.ImmutableSet;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.ItemLike;

import java.util.List;
import java.util.function.Supplier;

public class ModVillagers {

    private static final PoiType ARMORY_POI_VALUE = PointOfInterestHelper.register(
            Mod.loc("armory"), 1, 1, ModBlocks.REFORGING_TABLE.get());
    public static final Supplier<PoiType> ARMORY_POI = () -> ARMORY_POI_VALUE;

    public static final Supplier<VillagerProfession> ARMORY = Registration.custom(net.minecraft.core.registries.BuiltInRegistries.VILLAGER_PROFESSION, "armory",
            () -> new VillagerProfession("armory", holder -> holder.value() == ARMORY_POI.get(), holder -> holder.value() == ARMORY_POI.get(),
                    ImmutableSet.of(), ImmutableSet.of(), null));

    public static void addCustomTrades() {
        TradeOfferHelper.registerVillagerOffers(ARMORY.get(), 1, ModVillagers::addLevelOneTrades);
        TradeOfferHelper.registerVillagerOffers(ARMORY.get(), 2, ModVillagers::addLevelTwoTrades);
        TradeOfferHelper.registerVillagerOffers(ARMORY.get(), 3, ModVillagers::addLevelThreeTrades);
        TradeOfferHelper.registerVillagerOffers(ARMORY.get(), 4, ModVillagers::addLevelFourTrades);
        TradeOfferHelper.registerVillagerOffers(ARMORY.get(), 5, ModVillagers::addLevelFiveTrades);
    }

    public static void addWandererTrade() {
        TradeOfferHelper.registerWanderingTraderOffers(2, factories -> {
            add(factories, new ItemStack(Items.EMERALD, 16),
                    SmallContainerBlockItem.createInstance(Mod.loc("containers/blueprints")), 10, 0);
            add(factories, new ItemStack(Items.EMERALD, 10),
                    SmallContainerBlockItem.createInstance(Mod.loc("containers/common")), 10, 0);
        });
    }

    private static void addLevelOneTrades(List<VillagerTrades.ItemListing> trades) {
        add(trades, ModItems.TASER_BLUEPRINT.get(), 1, Items.EMERALD, 2, 16, 5);

        add(trades, Items.EMERALD, 1, ModItems.HANDGUN_AMMO.get(), 20, 16, 1);
        add(trades, Items.EMERALD, 1, ModItems.RIFLE_AMMO.get(), 15, 16, 1);
        add(trades, Items.EMERALD, 1, ModItems.SNIPER_AMMO.get(), 8, 16, 1);
        add(trades, Items.EMERALD, 1, ModItems.SHOTGUN_AMMO.get(), 8, 16, 1);
        add(trades, Items.EMERALD, 1, ModItems.HEAVY_AMMO.get(), 6, 32, 1);
        add(trades, Items.EMERALD, 1, ModItems.SMALL_SHELL.get(), 4, 32, 1);
        add(trades, Items.EMERALD, 1, ModItems.BLU_43_MINE.get(), 4, 32, 1);

        add(trades, ModItems.HANDGUN_AMMO.get(), 40, Items.EMERALD, 1, 32, 2);
        add(trades, ModItems.RIFLE_AMMO.get(), 30, Items.EMERALD, 1, 32, 2);
        add(trades, ModItems.SNIPER_AMMO.get(), 16, Items.EMERALD, 1, 32, 2);
        add(trades, ModItems.SHOTGUN_AMMO.get(), 16, Items.EMERALD, 1, 32, 2);
        add(trades, ModItems.HEAVY_AMMO.get(), 12, Items.EMERALD, 1, 64, 2);
        add(trades, ModItems.SMALL_SHELL.get(), 8, Items.EMERALD, 1, 64, 2);
        add(trades, ModItems.BLU_43_MINE.get(), 8, Items.EMERALD, 1, 64, 2);
    }

    private static void addLevelTwoTrades(List<VillagerTrades.ItemListing> trades) {
        add(trades, Items.EMERALD, 10, ModItems.STEEL_MATERIALS.action().get(), 1, 12, 5);
        add(trades, Items.EMERALD, 8, ModItems.STEEL_MATERIALS.barrel().get(), 1, 12, 5);
        add(trades, Items.EMERALD, 6, ModItems.STEEL_MATERIALS.trigger().get(), 1, 12, 5);
        add(trades, Items.EMERALD, 8, ModItems.STEEL_MATERIALS.spring().get(), 1, 12, 5);

        add(trades, Items.EMERALD, 16, ModItems.MARLIN_BLUEPRINT.get(), 1, 8, 25);
        add(trades, Items.EMERALD, 16, ModItems.GLOCK_17_BLUEPRINT.get(), 1, 8, 15);
        add(trades, Items.EMERALD, 16, ModItems.M_1911_BLUEPRINT.get(), 1, 8, 15);
        add(trades, Items.EMERALD, 16, ModItems.MP_443_BLUEPRINT.get(), 1, 8, 15);
        add(trades, Items.EMERALD, 16, ModItems.TASER_BLUEPRINT.get(), 1, 8, 15);
    }

    private static void addLevelThreeTrades(List<VillagerTrades.ItemListing> trades) {
        add(trades, Items.EMERALD, 3, ModItems.HANDGUN_AMMO_BOX.get(), 2, 8, 5);
        add(trades, Items.EMERALD, 2, ModItems.RIFLE_AMMO_BOX.get(), 1, 8, 5);
        add(trades, Items.EMERALD, 3, ModItems.SNIPER_AMMO_BOX.get(), 1, 8, 5);
        add(trades, Items.EMERALD, 3, ModItems.SHOTGUN_AMMO_BOX.get(), 1, 8, 5);

        add(trades, ModItems.HANDGUN_AMMO_BOX.get(), 4, Items.EMERALD, 3, 16, 5);
        add(trades, ModItems.RIFLE_AMMO_BOX.get(), 1, Items.EMERALD, 1, 16, 5);
        add(trades, ModItems.SNIPER_AMMO_BOX.get(), 2, Items.EMERALD, 3, 16, 5);
        add(trades, ModItems.SHOTGUN_AMMO_BOX.get(), 2, Items.EMERALD, 3, 16, 5);

        add(trades, Items.EMERALD, 16, ModItems.CEMENTED_CARBIDE_MATERIALS.barrel().get(), 1, 12, 10);
        add(trades, Items.EMERALD, 20, ModItems.CEMENTED_CARBIDE_MATERIALS.action().get(), 1, 10, 10);
        add(trades, Items.EMERALD, 16, ModItems.CEMENTED_CARBIDE_MATERIALS.spring().get(), 1, 10, 10);
        add(trades, Items.EMERALD, 12, ModItems.CEMENTED_CARBIDE_MATERIALS.trigger().get(), 1, 10, 10);

        add(trades, Items.EMERALD, 32, ModItems.M_4_BLUEPRINT.get(), 1, 10, 25);
        add(trades, Items.EMERALD, 32, ModItems.M_79_BLUEPRINT.get(), 1, 10, 25);
        add(trades, Items.EMERALD, 32, ModItems.AK_47_BLUEPRINT.get(), 1, 10, 25);
        add(trades, Items.EMERALD, 32, ModItems.GLOCK_18_BLUEPRINT.get(), 1, 10, 25);
        add(trades, Items.EMERALD, 32, ModItems.SKS_BLUEPRINT.get(), 1, 10, 25);
        add(trades, Items.EMERALD, 32, ModItems.M_870_BLUEPRINT.get(), 1, 10, 25);
        add(trades, Items.EMERALD, 32, ModItems.K_98_BLUEPRINT.get(), 1, 10, 25);
        add(trades, Items.EMERALD, 32, ModItems.MOSIN_NAGANT_BLUEPRINT.get(), 1, 10, 25);
        add(trades, Items.EMERALD, 32, ModItems.RPG_BLUEPRINT.get(), 1, 10, 25);
        add(trades, Items.EMERALD, 32, ModItems.HK_416_BLUEPRINT.get(), 1, 10, 25);
        add(trades, Items.EMERALD, 32, ModItems.QBZ_95_BLUEPRINT.get(), 1, 10, 25);
        add(trades, Items.EMERALD, 32, ModItems.AK_12_BLUEPRINT.get(), 1, 10, 25);
        add(trades, Items.EMERALD, 32, ModItems.HUNTING_RIFLE_BLUEPRINT.get(), 1, 10, 25);
    }

    private static void addLevelFourTrades(List<VillagerTrades.ItemListing> trades) {
        add(trades, Items.EMERALD, 2, ModItems.GRENADE_40MM.get(), 1, 16, 5);
        add(trades, Items.EMERALD, 2, ModItems.HAND_GRENADE.get(), 1, 16, 5);
        add(trades, Items.EMERALD, 2, ModItems.RGO_GRENADE.get(), 1, 16, 5);
        add(trades, Items.EMERALD, 3, ModItems.MORTAR_SHELL.get(), 1, 16, 5);
        add(trades, Items.EMERALD, 4, ModItems.CLAYMORE_MINE.get(), 1, 16, 5);
        add(trades, Items.EMERALD, 4, ModItems.C4_BOMB.get(), 1, 16, 5);
        add(trades, Items.EMERALD, 4, ModItems.RPG_ROCKET_TBG.get(), 1, 16, 5);
        add(trades, Items.EMERALD, 4, ModItems.TM_62.get(), 1, 16, 5);
        add(trades, Items.EMERALD, 3, ModItems.SMALL_ROCKET.get(), 1, 16, 5);

        add(trades, ModItems.GRENADE_40MM.get(), 1, Items.EMERALD, 1, 32, 5);
        add(trades, ModItems.HAND_GRENADE.get(), 1, Items.EMERALD, 1, 32, 5);
        add(trades, ModItems.RGO_GRENADE.get(), 1, Items.EMERALD, 1, 32, 5);
        add(trades, ModItems.MORTAR_SHELL.get(), 3, Items.EMERALD, 2, 32, 5);
        add(trades, ModItems.CLAYMORE_MINE.get(), 1, Items.EMERALD, 2, 32, 5);
        add(trades, ModItems.C4_BOMB.get(), 1, Items.EMERALD, 2, 32, 5);
        add(trades, ModItems.RPG_ROCKET_TBG.get(), 1, Items.EMERALD, 2, 32, 5);
        add(trades, ModItems.TM_62.get(), 1, Items.EMERALD, 2, 32, 5);
        add(trades, ModItems.SMALL_ROCKET.get(), 3, Items.EMERALD, 2, 32, 5);

        add(trades, Items.EMERALD, 64, ModItems.RPK_BLUEPRINT.get(), 1, 10, 30);
        add(trades, Items.EMERALD, 64, ModItems.VECTOR_BLUEPRINT.get(), 1, 10, 30);
        add(trades, Items.EMERALD, 64, ModItems.MK_14_BLUEPRINT.get(), 1, 10, 30);
        add(trades, Items.EMERALD, 64, ModItems.M_60_BLUEPRINT.get(), 1, 10, 30);
        add(trades, Items.EMERALD, 64, ModItems.SVD_BLUEPRINT.get(), 1, 10, 30);
        add(trades, Items.EMERALD, 64, ModItems.M_98B_BLUEPRINT.get(), 1, 10, 30);
        add(trades, Items.EMERALD, 64, ModItems.AWM_BLUEPRINT.get(), 1, 10, 30);
        add(trades, Items.EMERALD, 64, ModItems.DEVOTION_BLUEPRINT.get(), 1, 10, 30);

        add(trades, Items.EMERALD, 8, ModItems.HE_5_INCHES.get(), 1, 8, 10);
        add(trades, Items.EMERALD, 8, ModItems.AP_5_INCHES.get(), 1, 8, 10);
        add(trades, Items.EMERALD, 8, ModItems.CM_5_INCHES.get(), 1, 8, 10);
        add(trades, Items.EMERALD, 8, ModItems.MEDIUM_ROCKET_HE.get(), 1, 8, 10);
        add(trades, Items.EMERALD, 8, ModItems.MEDIUM_ROCKET_AP.get(), 1, 8, 10);
        add(trades, Items.EMERALD, 8, ModItems.MEDIUM_ROCKET_CM.get(), 1, 8, 10);
        add(trades, Items.EMERALD, 12, ModItems.JAVELIN_MISSILE.get(), 1, 8, 10);
        add(trades, Items.EMERALD, 12, ModItems.MEDIUM_ANTI_GROUND_MISSILE.get(), 1, 8, 10);
        add(trades, Items.EMERALD, 16, ModItems.LARGE_ANTI_GROUND_MISSILE.get(), 1, 8, 10);
        add(trades, Items.EMERALD, 16, ModItems.MEDIUM_AERIAL_BOMB.get(), 1, 8, 10);

        add(trades, ModItems.HE_5_INCHES.get(), 1, Items.EMERALD, 4, 32, 4);
        add(trades, ModItems.AP_5_INCHES.get(), 1, Items.EMERALD, 4, 32, 4);
        add(trades, ModItems.CM_5_INCHES.get(), 1, Items.EMERALD, 4, 32, 4);
        add(trades, ModItems.MEDIUM_ROCKET_HE.get(), 1, Items.EMERALD, 4, 32, 4);
        add(trades, ModItems.MEDIUM_ROCKET_AP.get(), 1, Items.EMERALD, 4, 32, 4);
        add(trades, ModItems.MEDIUM_ROCKET_CM.get(), 1, Items.EMERALD, 4, 32, 4);
        add(trades, ModItems.JAVELIN_MISSILE.get(), 1, Items.EMERALD, 6, 32, 4);
        add(trades, ModItems.MEDIUM_ANTI_GROUND_MISSILE.get(), 1, Items.EMERALD, 6, 32, 4);
        add(trades, ModItems.LARGE_ANTI_GROUND_MISSILE.get(), 1, Items.EMERALD, 8, 32, 4);
        add(trades, ModItems.MEDIUM_AERIAL_BOMB.get(), 1, Items.EMERALD, 8, 32, 4);
    }

    private static void addLevelFiveTrades(List<VillagerTrades.ItemListing> trades) {
        add(trades, Items.EMERALD, 22, ModItems.PERK_ITEMS.get(ModPerks.POISONOUS_BULLET).get(), 1, 4, 10);
        add(trades, Items.EMERALD, 24, ModItems.PERK_ITEMS.get(ModPerks.SUBSISTENCE).get(), 1, 4, 10);
        add(trades, Items.EMERALD, 25, ModItems.PERK_ITEMS.get(ModPerks.KILL_CLIP).get(), 1, 4, 10);
        add(trades, Items.EMERALD, 26, ModItems.PERK_ITEMS.get(ModPerks.GUTSHOT_STRAIGHT).get(), 1, 4, 10);
        add(trades, Items.EMERALD, 22, ModItems.PERK_ITEMS.get(ModPerks.HEAD_SEEKER).get(), 1, 4, 10);
        add(trades, Items.EMERALD, 34, ModItems.PERK_ITEMS.get(ModPerks.SILVER_BULLET).get(), 1, 4, 15);
        add(trades, Items.EMERALD, 30, ModItems.PERK_ITEMS.get(ModPerks.FIELD_DOCTOR).get(), 1, 4, 15);
        add(trades, Items.EMERALD, 34, ModItems.PERK_ITEMS.get(ModPerks.HEAL_CLIP).get(), 1, 4, 15);
        add(trades, Items.EMERALD, 30, ModItems.PERK_ITEMS.get(ModPerks.KILLING_TALLY).get(), 1, 4, 15);
        add(trades, Items.EMERALD, 34, ModItems.PERK_ITEMS.get(ModPerks.FOURTH_TIMES_CHARM).get(), 1, 4, 15);
        add(trades, Items.EMERALD, 48, ModItems.PERK_ITEMS.get(ModPerks.MONSTER_HUNTER).get(), 1, 4, 25);
        add(trades, Items.EMERALD, 40, ModItems.PERK_ITEMS.get(ModPerks.VORPAL_WEAPON).get(), 1, 4, 25);
        add(trades, Items.EMERALD, 42, ModItems.PERK_ITEMS.get(ModPerks.MAGNIFICENT_HOWL).get(), 1, 4, 25);
        add(trades, Items.EMERALD, 64, ModItems.PERK_ITEMS.get(ModPerks.FAIR_MEANS).get(), 1, 4, 25);
        add(trades, Items.EMERALD, 40, ModItems.PERK_ITEMS.get(ModPerks.HIGH_IMPACT_RESERVES).get(), 1, 4, 25);
        add(trades, Items.EMERALD, 48, ModItems.PERK_ITEMS.get(ModPerks.ONE_TWO_PUNCH).get(), 1, 4, 25);
    }

    private static void add(List<VillagerTrades.ItemListing> trades, ItemLike cost, int costCount,
                            ItemLike result, int resultCount, int maxUses, int xp) {
        add(trades, new ItemStack(cost, costCount), new ItemStack(result, resultCount), maxUses, xp);
    }

    private static void add(List<VillagerTrades.ItemListing> trades, ItemStack cost, ItemStack result,
                            int maxUses, int xp) {
        trades.add((trader, random) -> new MerchantOffer(cost.copy(), result.copy(), maxUses, xp, 0.05f));
    }

    public static void register() {
        addCustomTrades();
        addWandererTrade();
    }
}
