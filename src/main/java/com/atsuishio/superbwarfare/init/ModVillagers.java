package com.atsuishio.superbwarfare.init;

import com.atsuishio.superbwarfare.Mod;
import com.atsuishio.superbwarfare.item.common.container.SmallContainerBlockItem;
import com.google.common.collect.ImmutableSet;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;

import java.util.List;

public class ModVillagers {

    public static final PoiType ARMORY_POI = Registry.register(
            BuiltInRegistries.POINT_OF_INTEREST_TYPE,
            Mod.loc("armory"),
            new PoiType(ImmutableSet.copyOf(ModBlocks.REFORGING_TABLE.getStateDefinition().getPossibleStates()), 1, 1)
    );

    public static final VillagerProfession ARMORY = Registry.register(
            BuiltInRegistries.VILLAGER_PROFESSION,
            Mod.loc("armory"),
            new VillagerProfession("armory", holder -> holder.value() == ARMORY_POI, holder -> holder.value() == ARMORY_POI,
                    ImmutableSet.of(), ImmutableSet.of(), null)
    );

    public static void init() {
        registerTrade(1,
                trade(new ItemStack(Items.EMERALD, 2), new ItemStack(ModItems.TASER_BLUEPRINT), 16, 5, 0.05f),
                trade(new ItemStack(Items.EMERALD, 1), new ItemStack(ModItems.HANDGUN_AMMO, 20), 16, 1, 0.05f),
                trade(new ItemStack(Items.EMERALD, 1), new ItemStack(ModItems.RIFLE_AMMO, 15), 16, 1, 0.05f),
                trade(new ItemStack(Items.EMERALD, 1), new ItemStack(ModItems.SNIPER_AMMO, 8), 16, 1, 0.05f),
                trade(new ItemStack(Items.EMERALD, 1), new ItemStack(ModItems.SHOTGUN_AMMO, 8), 16, 1, 0.05f),
                trade(new ItemStack(Items.EMERALD, 1), new ItemStack(ModItems.HEAVY_AMMO, 6), 32, 1, 0.05f),
                trade(new ItemStack(Items.EMERALD, 1), new ItemStack(ModItems.SMALL_SHELL, 4), 32, 1, 0.05f),
                trade(new ItemStack(Items.EMERALD, 1), new ItemStack(ModItems.BLU_43_MINE, 4), 32, 1, 0.05f),

                trade(new ItemStack(ModItems.HANDGUN_AMMO, 40), new ItemStack(Items.EMERALD, 1), 32, 2, 0.05f),
                trade(new ItemStack(ModItems.RIFLE_AMMO, 30), new ItemStack(Items.EMERALD, 1), 32, 2, 0.05f),
                trade(new ItemStack(ModItems.SNIPER_AMMO, 16), new ItemStack(Items.EMERALD, 1), 32, 2, 0.05f),
                trade(new ItemStack(ModItems.SHOTGUN_AMMO, 16), new ItemStack(Items.EMERALD, 1), 32, 2, 0.05f),
                trade(new ItemStack(ModItems.HEAVY_AMMO, 12), new ItemStack(Items.EMERALD, 1), 64, 2, 0.05f),
                trade(new ItemStack(ModItems.SMALL_SHELL, 8), new ItemStack(Items.EMERALD, 1), 64, 2, 0.05f),
                trade(new ItemStack(ModItems.BLU_43_MINE, 8), new ItemStack(Items.EMERALD, 1), 64, 2, 0.05f)
        );
        registerTrade(2,
                trade(new ItemStack(Items.EMERALD, 10), new ItemStack(ModItems.STEEL_MATERIALS.action()), 12, 5, 0.05f),
                trade(new ItemStack(Items.EMERALD, 8), new ItemStack(ModItems.STEEL_MATERIALS.barrel()), 12, 5, 0.05f),
                trade(new ItemStack(Items.EMERALD, 6), new ItemStack(ModItems.STEEL_MATERIALS.trigger()), 12, 5, 0.05f),
                trade(new ItemStack(Items.EMERALD, 8), new ItemStack(ModItems.STEEL_MATERIALS.spring()), 12, 5, 0.05f),

                trade(new ItemStack(Items.EMERALD, 16), new ItemStack(ModItems.MARLIN_BLUEPRINT), 8, 25, 0.05f),
                trade(new ItemStack(Items.EMERALD, 16), new ItemStack(ModItems.GLOCK_17_BLUEPRINT), 8, 15, 0.05f),
                trade(new ItemStack(Items.EMERALD, 16), new ItemStack(ModItems.M_1911_BLUEPRINT), 8, 15, 0.05f),
                trade(new ItemStack(Items.EMERALD, 16), new ItemStack(ModItems.MP_443_BLUEPRINT), 8, 15, 0.05f),
                trade(new ItemStack(Items.EMERALD, 16), new ItemStack(ModItems.TASER_BLUEPRINT), 8, 15, 0.05f)
        );
        registerTrade(3,
                trade(new ItemStack(Items.EMERALD, 3), new ItemStack(ModItems.HANDGUN_AMMO_BOX, 2), 8, 5, 0.05f),
                trade(new ItemStack(Items.EMERALD, 2), new ItemStack(ModItems.RIFLE_AMMO_BOX, 1), 8, 5, 0.05f),
                trade(new ItemStack(Items.EMERALD, 3), new ItemStack(ModItems.SNIPER_AMMO_BOX, 1), 8, 5, 0.05f),
                trade(new ItemStack(Items.EMERALD, 3), new ItemStack(ModItems.SHOTGUN_AMMO_BOX, 1), 8, 5, 0.05f),

                trade(new ItemStack(ModItems.HANDGUN_AMMO_BOX, 4), new ItemStack(Items.EMERALD, 3), 16, 5, 0.05f),
                trade(new ItemStack(ModItems.RIFLE_AMMO_BOX, 1), new ItemStack(Items.EMERALD, 1), 16, 5, 0.05f),
                trade(new ItemStack(ModItems.SNIPER_AMMO_BOX, 2), new ItemStack(Items.EMERALD, 3), 16, 5, 0.05f),
                trade(new ItemStack(ModItems.SHOTGUN_AMMO_BOX, 2), new ItemStack(Items.EMERALD, 3), 16, 5, 0.05f),

                trade(new ItemStack(Items.EMERALD, 16), new ItemStack(ModItems.CEMENTED_CARBIDE_MATERIALS.barrel()), 12, 10, 0.05f),
                trade(new ItemStack(Items.EMERALD, 20), new ItemStack(ModItems.CEMENTED_CARBIDE_MATERIALS.action()), 10, 10, 0.05f),
                trade(new ItemStack(Items.EMERALD, 16), new ItemStack(ModItems.CEMENTED_CARBIDE_MATERIALS.spring()), 10, 10, 0.05f),
                trade(new ItemStack(Items.EMERALD, 12), new ItemStack(ModItems.CEMENTED_CARBIDE_MATERIALS.trigger()), 10, 10, 0.05f),

                trade(new ItemStack(Items.EMERALD, 32), new ItemStack(ModItems.M_4_BLUEPRINT), 10, 25, 0.05f),
                trade(new ItemStack(Items.EMERALD, 32), new ItemStack(ModItems.M_79_BLUEPRINT), 10, 25, 0.05f),
                trade(new ItemStack(Items.EMERALD, 32), new ItemStack(ModItems.AK_47_BLUEPRINT), 10, 25, 0.05f),
                trade(new ItemStack(Items.EMERALD, 32), new ItemStack(ModItems.GLOCK_18_BLUEPRINT), 10, 25, 0.05f),
                trade(new ItemStack(Items.EMERALD, 32), new ItemStack(ModItems.SKS_BLUEPRINT), 10, 25, 0.05f),
                trade(new ItemStack(Items.EMERALD, 32), new ItemStack(ModItems.M_870_BLUEPRINT), 10, 25, 0.05f),
                trade(new ItemStack(Items.EMERALD, 32), new ItemStack(ModItems.K_98_BLUEPRINT), 10, 25, 0.05f),
                trade(new ItemStack(Items.EMERALD, 32), new ItemStack(ModItems.MOSIN_NAGANT_BLUEPRINT), 10, 25, 0.05f),
                trade(new ItemStack(Items.EMERALD, 32), new ItemStack(ModItems.RPG_BLUEPRINT), 10, 25, 0.05f),
                trade(new ItemStack(Items.EMERALD, 32), new ItemStack(ModItems.HK_416_BLUEPRINT), 10, 25, 0.05f),
                trade(new ItemStack(Items.EMERALD, 32), new ItemStack(ModItems.QBZ_95_BLUEPRINT), 10, 25, 0.05f),
                trade(new ItemStack(Items.EMERALD, 32), new ItemStack(ModItems.AK_12_BLUEPRINT), 10, 25, 0.05f),
                trade(new ItemStack(Items.EMERALD, 32), new ItemStack(ModItems.HUNTING_RIFLE_BLUEPRINT), 10, 25, 0.05f)
        );
        registerTrade(4,
                trade(new ItemStack(Items.EMERALD, 2), new ItemStack(ModItems.GRENADE_40MM, 1), 16, 5, 0.05f),
                trade(new ItemStack(Items.EMERALD, 2), new ItemStack(ModItems.HAND_GRENADE, 1), 16, 5, 0.05f),
                trade(new ItemStack(Items.EMERALD, 2), new ItemStack(ModItems.RGO_GRENADE, 1), 16, 5, 0.05f),
                trade(new ItemStack(Items.EMERALD, 3), new ItemStack(ModItems.MORTAR_SHELL, 1), 16, 5, 0.05f),
                trade(new ItemStack(Items.EMERALD, 4), new ItemStack(ModItems.CLAYMORE_MINE, 1), 16, 5, 0.05f),
                trade(new ItemStack(Items.EMERALD, 4), new ItemStack(ModItems.C4_BOMB, 1), 16, 5, 0.05f),
                trade(new ItemStack(Items.EMERALD, 4), new ItemStack(ModItems.RPG_ROCKET_TBG, 1), 16, 5, 0.05f),
                trade(new ItemStack(Items.EMERALD, 4), new ItemStack(ModItems.TM_62, 1), 16, 5, 0.05f),
                trade(new ItemStack(Items.EMERALD, 3), new ItemStack(ModItems.SMALL_ROCKET, 1), 16, 5, 0.05f),

                trade(new ItemStack(ModItems.GRENADE_40MM, 1), new ItemStack(Items.EMERALD, 1), 32, 5, 0.05f),
                trade(new ItemStack(ModItems.HAND_GRENADE, 1), new ItemStack(Items.EMERALD, 1), 32, 5, 0.05f),
                trade(new ItemStack(ModItems.RGO_GRENADE, 1), new ItemStack(Items.EMERALD, 1), 32, 5, 0.05f),
                trade(new ItemStack(ModItems.MORTAR_SHELL, 3), new ItemStack(Items.EMERALD, 2), 32, 5, 0.05f),
                trade(new ItemStack(ModItems.CLAYMORE_MINE, 1), new ItemStack(Items.EMERALD, 2), 32, 5, 0.05f),
                trade(new ItemStack(ModItems.C4_BOMB, 1), new ItemStack(Items.EMERALD, 2), 32, 5, 0.05f),
                trade(new ItemStack(ModItems.RPG_ROCKET_TBG, 1), new ItemStack(Items.EMERALD, 2), 32, 5, 0.05f),
                trade(new ItemStack(ModItems.TM_62, 1), new ItemStack(Items.EMERALD, 2), 32, 5, 0.05f),
                trade(new ItemStack(ModItems.SMALL_ROCKET, 3), new ItemStack(Items.EMERALD, 2), 32, 5, 0.05f),

                trade(new ItemStack(Items.EMERALD, 64), new ItemStack(ModItems.RPK_BLUEPRINT), 10, 30, 0.05f),
                trade(new ItemStack(Items.EMERALD, 64), new ItemStack(ModItems.VECTOR_BLUEPRINT), 10, 30, 0.05f),
                trade(new ItemStack(Items.EMERALD, 64), new ItemStack(ModItems.MK_14_BLUEPRINT), 10, 30, 0.05f),
                trade(new ItemStack(Items.EMERALD, 64), new ItemStack(ModItems.M_60_BLUEPRINT), 10, 30, 0.05f),
                trade(new ItemStack(Items.EMERALD, 64), new ItemStack(ModItems.SVD_BLUEPRINT), 10, 30, 0.05f),
                trade(new ItemStack(Items.EMERALD, 64), new ItemStack(ModItems.M_98B_BLUEPRINT), 10, 30, 0.05f),
                trade(new ItemStack(Items.EMERALD, 64), new ItemStack(ModItems.AWM_BLUEPRINT), 10, 30, 0.05f),
                trade(new ItemStack(Items.EMERALD, 64), new ItemStack(ModItems.DEVOTION_BLUEPRINT), 10, 30, 0.05f),

                trade(new ItemStack(Items.EMERALD, 8), new ItemStack(ModItems.HE_5_INCHES, 1), 8, 10, 0.05f),
                trade(new ItemStack(Items.EMERALD, 8), new ItemStack(ModItems.AP_5_INCHES, 1), 8, 10, 0.05f),
                trade(new ItemStack(Items.EMERALD, 8), new ItemStack(ModItems.CM_5_INCHES, 1), 8, 10, 0.05f),
                trade(new ItemStack(Items.EMERALD, 8), new ItemStack(ModItems.MEDIUM_ROCKET_HE, 1), 8, 10, 0.05f),
                trade(new ItemStack(Items.EMERALD, 8), new ItemStack(ModItems.MEDIUM_ROCKET_AP, 1), 8, 10, 0.05f),
                trade(new ItemStack(Items.EMERALD, 8), new ItemStack(ModItems.MEDIUM_ROCKET_CM, 1), 8, 10, 0.05f),
                trade(new ItemStack(Items.EMERALD, 12), new ItemStack(ModItems.JAVELIN_MISSILE, 1), 8, 10, 0.05f),
                trade(new ItemStack(Items.EMERALD, 12), new ItemStack(ModItems.MEDIUM_ANTI_GROUND_MISSILE, 1), 8, 10, 0.05f),
                trade(new ItemStack(Items.EMERALD, 16), new ItemStack(ModItems.LARGE_ANTI_GROUND_MISSILE, 1), 8, 10, 0.05f),
                trade(new ItemStack(Items.EMERALD, 16), new ItemStack(ModItems.MEDIUM_AERIAL_BOMB, 1), 8, 10, 0.05f),

                trade(new ItemStack(ModItems.HE_5_INCHES, 1), new ItemStack(Items.EMERALD, 4), 32, 4, 0.05f),
                trade(new ItemStack(ModItems.AP_5_INCHES, 1), new ItemStack(Items.EMERALD, 4), 32, 4, 0.05f),
                trade(new ItemStack(ModItems.CM_5_INCHES, 1), new ItemStack(Items.EMERALD, 4), 32, 4, 0.05f),
                trade(new ItemStack(ModItems.MEDIUM_ROCKET_HE, 1), new ItemStack(Items.EMERALD, 4), 32, 4, 0.05f),
                trade(new ItemStack(ModItems.MEDIUM_ROCKET_AP, 1), new ItemStack(Items.EMERALD, 4), 32, 4, 0.05f),
                trade(new ItemStack(ModItems.MEDIUM_ROCKET_CM, 1), new ItemStack(Items.EMERALD, 4), 32, 4, 0.05f),
                trade(new ItemStack(ModItems.JAVELIN_MISSILE, 1), new ItemStack(Items.EMERALD, 6), 32, 4, 0.05f),
                trade(new ItemStack(ModItems.MEDIUM_ANTI_GROUND_MISSILE, 1), new ItemStack(Items.EMERALD, 6), 32, 4, 0.05f),
                trade(new ItemStack(ModItems.LARGE_ANTI_GROUND_MISSILE, 1), new ItemStack(Items.EMERALD, 8), 32, 4, 0.05f),
                trade(new ItemStack(ModItems.MEDIUM_AERIAL_BOMB, 1), new ItemStack(Items.EMERALD, 8), 32, 4, 0.05f)
        );
        registerTrade(5,
                trade(new ItemStack(Items.EMERALD, 22), new ItemStack(ModItems.PERK_ITEMS.get(ModPerks.POISONOUS_BULLET), 1), 4, 10, 0.05f),
                trade(new ItemStack(Items.EMERALD, 24), new ItemStack(ModItems.PERK_ITEMS.get(ModPerks.SUBSISTENCE), 1), 4, 10, 0.05f),
                trade(new ItemStack(Items.EMERALD, 25), new ItemStack(ModItems.PERK_ITEMS.get(ModPerks.KILL_CLIP), 1), 4, 10, 0.05f),
                trade(new ItemStack(Items.EMERALD, 26), new ItemStack(ModItems.PERK_ITEMS.get(ModPerks.GUTSHOT_STRAIGHT), 1), 4, 10, 0.05f),
                trade(new ItemStack(Items.EMERALD, 22), new ItemStack(ModItems.PERK_ITEMS.get(ModPerks.HEAD_SEEKER), 1), 4, 10, 0.05f),
                trade(new ItemStack(Items.EMERALD, 34), new ItemStack(ModItems.PERK_ITEMS.get(ModPerks.SILVER_BULLET), 1), 4, 15, 0.05f),
                trade(new ItemStack(Items.EMERALD, 30), new ItemStack(ModItems.PERK_ITEMS.get(ModPerks.FIELD_DOCTOR), 1), 4, 15, 0.05f),
                trade(new ItemStack(Items.EMERALD, 34), new ItemStack(ModItems.PERK_ITEMS.get(ModPerks.HEAL_CLIP), 1), 4, 15, 0.05f),
                trade(new ItemStack(Items.EMERALD, 30), new ItemStack(ModItems.PERK_ITEMS.get(ModPerks.KILLING_TALLY), 1), 4, 15, 0.05f),
                trade(new ItemStack(Items.EMERALD, 34), new ItemStack(ModItems.PERK_ITEMS.get(ModPerks.FOURTH_TIMES_CHARM), 1), 4, 15, 0.05f),
                trade(new ItemStack(Items.EMERALD, 48), new ItemStack(ModItems.PERK_ITEMS.get(ModPerks.MONSTER_HUNTER), 1), 4, 25, 0.05f),
                trade(new ItemStack(Items.EMERALD, 40), new ItemStack(ModItems.PERK_ITEMS.get(ModPerks.VORPAL_WEAPON), 1), 4, 25, 0.05f),
                trade(new ItemStack(Items.EMERALD, 42), new ItemStack(ModItems.PERK_ITEMS.get(ModPerks.MAGNIFICENT_HOWL), 1), 4, 25, 0.05f),
                trade(new ItemStack(Items.EMERALD, 64), new ItemStack(ModItems.PERK_ITEMS.get(ModPerks.FAIR_MEANS), 1), 4, 25, 0.05f),
                trade(new ItemStack(Items.EMERALD, 40), new ItemStack(ModItems.PERK_ITEMS.get(ModPerks.HIGH_IMPACT_RESERVES), 1), 4, 25, 0.05f),
                trade(new ItemStack(Items.EMERALD, 48), new ItemStack(ModItems.PERK_ITEMS.get(ModPerks.ONE_TWO_PUNCH), 1), 4, 25, 0.05f)
        );

        TradeOfferHelper.registerWanderingTraderOffers(2, factories -> {
            factories.add((entity, random) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 16),
                    SmallContainerBlockItem.createInstance(Mod.loc("containers/blueprints")),
                    10, 0, 0.05f
            ));
            factories.add((entity, random) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 10),
                    SmallContainerBlockItem.createInstance(Mod.loc("containers/common")),
                    10, 0, 0.05f
            ));
        });
    }

    private static void registerTrade(int level, VillagerTrades.ItemListing... listings) {
        TradeOfferHelper.registerVillagerOffers(ARMORY, level, factories -> {
            for (var listing : listings) {
                factories.add(listing);
            }
        });
    }

    private static VillagerTrades.ItemListing trade(ItemStack buy, ItemStack sell, int maxTrades, int xp, float priceMult) {
        return (entity, random) -> new MerchantOffer(new ItemCost(buy.getItem(), buy.getCount()), sell, maxTrades, xp, priceMult);
    }
}
