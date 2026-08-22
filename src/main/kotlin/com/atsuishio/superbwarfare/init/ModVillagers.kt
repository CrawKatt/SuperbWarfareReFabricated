package com.atsuishio.superbwarfare.init

import com.atsuishio.superbwarfare.Mod
import com.atsuishio.superbwarfare.Mod.Companion.loc
import com.atsuishio.superbwarfare.item.container.SmallContainerBlockItem
import com.google.common.collect.ImmutableSet
import net.fabricmc.fabric.api.`object`.builder.v1.trade.TradeOfferHelper
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.entity.ai.village.poi.PoiType
import net.minecraft.world.entity.npc.VillagerProfession
import net.minecraft.world.entity.npc.VillagerTrades
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.trading.ItemCost
import net.minecraft.world.item.trading.MerchantOffer

object ModVillagers {
    @JvmField
    val ARMORY_POI: PoiType = Registry.register(
        BuiltInRegistries.POINT_OF_INTEREST_TYPE,
        Mod.loc("armory"),
        PoiType(ImmutableSet.copyOf(ModBlocks.REFORGING_TABLE.stateDefinition.possibleStates), 1, 1)
    )

    @JvmField
    val ARMORY: VillagerProfession = Registry.register(
        BuiltInRegistries.VILLAGER_PROFESSION,
        Mod.loc("armory"),
        VillagerProfession(
            "armory",
            { it.value() === ARMORY_POI },
            { it.value() === ARMORY_POI },
            ImmutableSet.of(),
            ImmutableSet.of(),
            null
        )
    )

    @JvmStatic
    fun init() {

            // 等级 1 交易
            val list1 = listOf(
                BasicItemListing(
                    ItemStack(Items.EMERALD, 1),
                    ItemStack(ModItems.CRUST, 4), 16, 4, 0.05f
                ),
                BasicItemListing(
                    ItemStack(ModItems.TASER_BLUEPRINT),
                    ItemStack(Items.EMERALD, 2), 16, 5, 0.05f
                ),
                BasicItemListing(
                    ItemStack(Items.EMERALD, 1),
                    ItemStack(ModItems.HANDGUN_AMMO, 20), 16, 1, 0.05f
                ),
                BasicItemListing(
                    ItemStack(Items.EMERALD, 1),
                    ItemStack(ModItems.RIFLE_AMMO, 15), 16, 1, 0.05f
                ),
                BasicItemListing(
                    ItemStack(Items.EMERALD, 1),
                    ItemStack(ModItems.SNIPER_AMMO, 8), 16, 1, 0.05f
                ),
                BasicItemListing(
                    ItemStack(Items.EMERALD, 1),
                    ItemStack(ModItems.SHOTGUN_AMMO, 8), 16, 1, 0.05f
                ),
                BasicItemListing(
                    ItemStack(Items.EMERALD, 1),
                    ItemStack(ModItems.HEAVY_AMMO, 6), 32, 1, 0.05f
                ),
                BasicItemListing(
                    ItemStack(Items.EMERALD, 1),
                    ItemStack(ModItems.SMALL_SHELL_AP, 4), 32, 1, 0.05f
                ),
                BasicItemListing(
                    ItemStack(Items.EMERALD, 1),
                    ItemStack(ModItems.SMALL_SHELL_HE, 4), 32, 1, 0.05f
                ),
                BasicItemListing(
                    ItemStack(Items.EMERALD, 1),
                    ItemStack(ModItems.SMALL_SHELL_GS, 6), 48, 1, 0.05f
                ),
                BasicItemListing(
                    ItemStack(Items.EMERALD, 1),
                    ItemStack(ModItems.SMALL_SHELL_AA, 8), 64, 1, 0.05f
                ),
                BasicItemListing(
                    ItemStack(Items.EMERALD, 1),
                    ItemStack(ModItems.BLU_43_MINE, 8), 32, 1, 0.05f
                ),
                BasicItemListing(
                    ItemStack(ModItems.HANDGUN_AMMO, 40),
                    ItemStack(Items.EMERALD, 1), 32, 2, 0.05f
                ),
                BasicItemListing(
                    ItemStack(ModItems.RIFLE_AMMO, 30),
                    ItemStack(Items.EMERALD, 1), 32, 2, 0.05f
                ),
                BasicItemListing(
                    ItemStack(ModItems.SNIPER_AMMO, 16),
                    ItemStack(Items.EMERALD, 1), 32, 2, 0.05f
                ),
                BasicItemListing(
                    ItemStack(ModItems.SHOTGUN_AMMO, 16),
                    ItemStack(Items.EMERALD, 1), 32, 2, 0.05f
                ),
                BasicItemListing(
                    ItemStack(ModItems.HEAVY_AMMO, 12),
                    ItemStack(Items.EMERALD, 1), 64, 2, 0.05f
                ),
                BasicItemListing(
                    ItemStack(ModItems.SMALL_SHELL_AP, 8),
                    ItemStack(Items.EMERALD, 1), 64, 2, 0.05f
                ),
                BasicItemListing(
                    ItemStack(ModItems.SMALL_SHELL_HE, 8),
                    ItemStack(Items.EMERALD, 1), 64, 2, 0.05f
                ),
                BasicItemListing(
                    ItemStack(ModItems.SMALL_SHELL_GS, 12),
                    ItemStack(Items.EMERALD, 1), 64, 2, 0.05f
                ),
                BasicItemListing(
                    ItemStack(ModItems.SMALL_SHELL_AA, 16),
                    ItemStack(Items.EMERALD, 1), 64, 2, 0.05f
                ),
                BasicItemListing(
                    ItemStack(ModItems.BLU_43_MINE, 16),
                    ItemStack(Items.EMERALD, 1), 64, 2, 0.05f
                ),
            )
        registerTrade(1, list1)

            // 等级 2 交易
            val list2 = listOf(
                BasicItemListing(
                    ItemStack(Items.EMERALD, 10),
                    ItemStack(ModItems.STEEL_MATERIALS.action), 12, 5, 0.05f
                ),
                BasicItemListing(
                    ItemStack(Items.EMERALD, 8),
                    ItemStack(ModItems.STEEL_MATERIALS.barrel), 12, 5, 0.05f
                ),
                BasicItemListing(
                    ItemStack(Items.EMERALD, 6),
                    ItemStack(ModItems.STEEL_MATERIALS.trigger), 12, 5, 0.05f
                ),
                BasicItemListing(
                    ItemStack(Items.EMERALD, 8),
                    ItemStack(ModItems.STEEL_MATERIALS.spring), 12, 5, 0.05f
                ),
                BasicItemListing(
                    ItemStack(Items.EMERALD, 16),
                    ItemStack(ModItems.MARLIN_BLUEPRINT), 8, 25, 0.05f
                ),
                BasicItemListing(
                    ItemStack(Items.EMERALD, 16),
                    ItemStack(ModItems.GLOCK_17_BLUEPRINT), 8, 15, 0.05f
                ),
                BasicItemListing(
                    ItemStack(Items.EMERALD, 16),
                    ItemStack(ModItems.M_1911_BLUEPRINT), 8, 15, 0.05f
                ),
                BasicItemListing(
                    ItemStack(Items.EMERALD, 16),
                    ItemStack(ModItems.MP_443_BLUEPRINT), 8, 15, 0.05f
                ),
                BasicItemListing(
                    ItemStack(Items.EMERALD, 16),
                    ItemStack(ModItems.TASER_BLUEPRINT), 8, 15, 0.05f
                )
            )
        registerTrade(2, list2)

            // 等级 3 交易
            val list3 = listOf(
                BasicItemListing(
                    ItemStack(Items.EMERALD, 3),
                    ItemStack(ModItems.HANDGUN_AMMO_BOX, 2), 8, 5, 0.05f
                ),
                BasicItemListing(
                    ItemStack(Items.EMERALD, 2),
                    ItemStack(ModItems.RIFLE_AMMO_BOX, 1), 8, 5, 0.05f
                ),
                BasicItemListing(
                    ItemStack(Items.EMERALD, 3),
                    ItemStack(ModItems.SNIPER_AMMO_BOX, 1), 8, 5, 0.05f
                ),
                BasicItemListing(
                    ItemStack(Items.EMERALD, 3),
                    ItemStack(ModItems.SHOTGUN_AMMO_BOX, 1), 8, 5, 0.05f
                ),
                BasicItemListing(
                    ItemStack(ModItems.HANDGUN_AMMO_BOX, 4),
                    ItemStack(Items.EMERALD, 3), 16, 5, 0.05f
                ),
                BasicItemListing(
                    ItemStack(ModItems.RIFLE_AMMO_BOX, 1),
                    ItemStack(Items.EMERALD, 1), 16, 5, 0.05f
                ),
                BasicItemListing(
                    ItemStack(ModItems.SNIPER_AMMO_BOX, 2),
                    ItemStack(Items.EMERALD, 3), 16, 5, 0.05f
                ),
                BasicItemListing(
                    ItemStack(ModItems.SHOTGUN_AMMO_BOX, 2),
                    ItemStack(Items.EMERALD, 3), 16, 5, 0.05f
                ),
                BasicItemListing(
                    ItemStack(Items.EMERALD, 16),
                    ItemStack(ModItems.CEMENTED_CARBIDE_MATERIALS.barrel), 12, 10, 0.05f
                ),
                BasicItemListing(
                    ItemStack(Items.EMERALD, 20),
                    ItemStack(ModItems.CEMENTED_CARBIDE_MATERIALS.action), 10, 10, 0.05f
                ),
                BasicItemListing(
                    ItemStack(Items.EMERALD, 16),
                    ItemStack(ModItems.CEMENTED_CARBIDE_MATERIALS.spring), 10, 10, 0.05f
                ),
                BasicItemListing(
                    ItemStack(Items.EMERALD, 12),
                    ItemStack(ModItems.CEMENTED_CARBIDE_MATERIALS.trigger), 10, 10, 0.05f
                ),
                BasicItemListing(
                    ItemStack(Items.EMERALD, 32),
                    ItemStack(ModItems.M_4_BLUEPRINT), 10, 25, 0.05f
                ),
                BasicItemListing(
                    ItemStack(Items.EMERALD, 32),
                    ItemStack(ModItems.M_79_BLUEPRINT), 10, 25, 0.05f
                ),
                BasicItemListing(
                    ItemStack(Items.EMERALD, 32),
                    ItemStack(ModItems.AK_47_BLUEPRINT), 10, 25, 0.05f
                ),
                BasicItemListing(
                    ItemStack(Items.EMERALD, 32),
                    ItemStack(ModItems.GLOCK_18_BLUEPRINT), 10, 25, 0.05f
                ),
                BasicItemListing(
                    ItemStack(Items.EMERALD, 32),
                    ItemStack(ModItems.SKS_BLUEPRINT), 10, 25, 0.05f
                ),
                BasicItemListing(
                    ItemStack(Items.EMERALD, 32),
                    ItemStack(ModItems.M_870_BLUEPRINT), 10, 25, 0.05f
                ),
                BasicItemListing(
                    ItemStack(Items.EMERALD, 32),
                    ItemStack(ModItems.K_98_BLUEPRINT), 10, 25, 0.05f
                ),
                BasicItemListing(
                    ItemStack(Items.EMERALD, 32),
                    ItemStack(ModItems.MOSIN_NAGANT_BLUEPRINT), 10, 25, 0.05f
                ),
                BasicItemListing(
                    ItemStack(Items.EMERALD, 32),
                    ItemStack(ModItems.RPG_BLUEPRINT), 10, 25, 0.05f
                ),
                BasicItemListing(
                    ItemStack(Items.EMERALD, 32),
                    ItemStack(ModItems.HK_416_BLUEPRINT), 10, 25, 0.05f
                ),
                BasicItemListing(
                    ItemStack(Items.EMERALD, 32),
                    ItemStack(ModItems.QBZ_95_BLUEPRINT), 10, 25, 0.05f
                ),
                BasicItemListing(
                    ItemStack(Items.EMERALD, 32),
                    ItemStack(ModItems.AK_12_BLUEPRINT), 10, 25, 0.05f
                ),
                BasicItemListing(
                    ItemStack(Items.EMERALD, 32),
                    ItemStack(ModItems.HUNTING_RIFLE_BLUEPRINT), 10, 25, 0.05f
                )
            )
        registerTrade(3, list3)

            // 等级 4 交易
            val list4 = listOf(
                BasicItemListing(
                    ItemStack(Items.EMERALD, 2),
                    ItemStack(ModItems.GRENADE_40MM, 1), 16, 5, 0.05f
                ),
                BasicItemListing(
                    ItemStack(Items.EMERALD, 2),
                    ItemStack(ModItems.HAND_GRENADE, 1), 16, 5, 0.05f
                ),
                BasicItemListing(
                    ItemStack(Items.EMERALD, 2),
                    ItemStack(ModItems.RGO_GRENADE, 1), 16, 5, 0.05f
                ),
                BasicItemListing(
                    ItemStack(Items.EMERALD, 3),
                    ItemStack(ModItems.MORTAR_SHELL, 1), 16, 5, 0.05f
                ),
                BasicItemListing(
                    ItemStack(Items.EMERALD, 4),
                    ItemStack(ModItems.CLAYMORE_MINE, 1), 16, 5, 0.05f
                ),
                BasicItemListing(
                    ItemStack(Items.EMERALD, 4),
                    ItemStack(ModItems.C4_BOMB, 1), 16, 5, 0.05f
                ),
                BasicItemListing(
                    ItemStack(Items.EMERALD, 4),
                    ItemStack(ModItems.RPG_ROCKET_TBG, 1), 16, 5, 0.05f
                ),
                BasicItemListing(
                    ItemStack(Items.EMERALD, 4),
                    ItemStack(ModItems.TM_62, 1), 16, 5, 0.05f
                ),
                BasicItemListing(
                    ItemStack(Items.EMERALD, 3),
                    ItemStack(ModItems.SMALL_ROCKET, 1), 16, 5, 0.05f
                ),
                BasicItemListing(
                    ItemStack(ModItems.GRENADE_40MM, 1),
                    ItemStack(Items.EMERALD, 1), 32, 5, 0.05f
                ),
                BasicItemListing(
                    ItemStack(ModItems.HAND_GRENADE, 1),
                    ItemStack(Items.EMERALD, 1), 32, 5, 0.05f
                ),
                BasicItemListing(
                    ItemStack(ModItems.RGO_GRENADE, 1),
                    ItemStack(Items.EMERALD, 1), 32, 5, 0.05f
                ),
                BasicItemListing(
                    ItemStack(ModItems.MORTAR_SHELL, 3),
                    ItemStack(Items.EMERALD, 2), 32, 5, 0.05f
                ),
                BasicItemListing(
                    ItemStack(ModItems.CLAYMORE_MINE, 1),
                    ItemStack(Items.EMERALD, 2), 32, 5, 0.05f
                ),
                BasicItemListing(
                    ItemStack(ModItems.C4_BOMB, 1),
                    ItemStack(Items.EMERALD, 2), 32, 5, 0.05f
                ),
                BasicItemListing(
                    ItemStack(ModItems.RPG_ROCKET_TBG, 1),
                    ItemStack(Items.EMERALD, 2), 32, 5, 0.05f
                ),
                BasicItemListing(
                    ItemStack(ModItems.TM_62, 1),
                    ItemStack(Items.EMERALD, 2), 32, 5, 0.05f
                ),
                BasicItemListing(
                    ItemStack(ModItems.SMALL_ROCKET, 3),
                    ItemStack(Items.EMERALD, 2), 32, 5, 0.05f
                ),
                BasicItemListing(
                    ItemStack(Items.EMERALD, 64),
                    ItemStack(ModItems.RPK_BLUEPRINT), 10, 30, 0.05f
                ),
                BasicItemListing(
                    ItemStack(Items.EMERALD, 64),
                    ItemStack(ModItems.VECTOR_BLUEPRINT), 10, 30, 0.05f
                ),
                BasicItemListing(
                    ItemStack(Items.EMERALD, 64),
                    ItemStack(ModItems.MK_14_BLUEPRINT), 10, 30, 0.05f
                ),
                BasicItemListing(
                    ItemStack(Items.EMERALD, 64),
                    ItemStack(ModItems.M_60_BLUEPRINT), 10, 30, 0.05f
                ),
                BasicItemListing(
                    ItemStack(Items.EMERALD, 64),
                    ItemStack(ModItems.SVD_BLUEPRINT), 10, 30, 0.05f
                ),
                BasicItemListing(
                    ItemStack(Items.EMERALD, 64),
                    ItemStack(ModItems.M_98B_BLUEPRINT), 10, 30, 0.05f
                ),
                BasicItemListing(
                    ItemStack(Items.EMERALD, 64),
                    ItemStack(ModItems.AWM_BLUEPRINT), 10, 30, 0.05f
                ),
                BasicItemListing(
                    ItemStack(Items.EMERALD, 64),
                    ItemStack(ModItems.DEVOTION_BLUEPRINT), 10, 30, 0.05f
                ),
                BasicItemListing(
                    ItemStack(Items.EMERALD, 8),
                    ItemStack(ModItems.LARGE_SHELL_HE, 1), 8, 10, 0.05f
                ),
                BasicItemListing(
                    ItemStack(Items.EMERALD, 8),
                    ItemStack(ModItems.LARGE_SHELL_AP, 1), 8, 10, 0.05f
                ),
                BasicItemListing(
                    ItemStack(Items.EMERALD, 8),
                    ItemStack(ModItems.LARGE_SHELL_CM, 1), 8, 10, 0.05f
                ),
                BasicItemListing(
                    ItemStack(Items.EMERALD, 8),
                    ItemStack(ModItems.MEDIUM_ROCKET_HE, 1), 8, 10, 0.05f
                ),
                BasicItemListing(
                    ItemStack(Items.EMERALD, 8),
                    ItemStack(ModItems.MEDIUM_ROCKET_AP, 1), 8, 10, 0.05f
                ),
                BasicItemListing(
                    ItemStack(Items.EMERALD, 8),
                    ItemStack(ModItems.MEDIUM_ROCKET_CM, 1), 8, 10, 0.05f
                ),
                BasicItemListing(
                    ItemStack(Items.EMERALD, 12),
                    ItemStack(ModItems.JAVELIN_MISSILE, 1), 8, 10, 0.05f
                ),
                BasicItemListing(
                    ItemStack(Items.EMERALD, 12),
                    ItemStack(ModItems.MEDIUM_ANTI_GROUND_MISSILE, 1), 8, 10, 0.05f
                ),
                BasicItemListing(
                    ItemStack(Items.EMERALD, 16),
                    ItemStack(ModItems.LARGE_ANTI_GROUND_MISSILE, 1), 8, 10, 0.05f
                ),
                BasicItemListing(
                    ItemStack(Items.EMERALD, 32),
                    ItemStack(ModItems.EXTRA_LARGE_ANTI_GROUND_MISSILE, 1), 8, 20, 0.05f
                ),
                BasicItemListing(
                    ItemStack(Items.EMERALD, 16),
                    ItemStack(ModItems.MEDIUM_AERIAL_BOMB, 1), 8, 10, 0.05f
                ),
                BasicItemListing(
                    ItemStack(Items.EMERALD, 32),
                    ItemStack(ModItems.LARGE_AERIAL_BOMB, 1), 8, 10, 0.05f
                ),
                BasicItemListing(
                    ItemStack(ModItems.LARGE_SHELL_HE, 1),
                    ItemStack(Items.EMERALD, 4), 32, 4, 0.05f
                ),
                BasicItemListing(
                    ItemStack(ModItems.LARGE_SHELL_AP, 1),
                    ItemStack(Items.EMERALD, 4), 32, 4, 0.05f
                ),
                BasicItemListing(
                    ItemStack(ModItems.LARGE_SHELL_CM, 1),
                    ItemStack(Items.EMERALD, 4), 32, 4, 0.05f
                ),
                BasicItemListing(
                    ItemStack(ModItems.MEDIUM_ROCKET_HE, 1),
                    ItemStack(Items.EMERALD, 4), 32, 4, 0.05f
                ),
                BasicItemListing(
                    ItemStack(ModItems.MEDIUM_ROCKET_AP, 1),
                    ItemStack(Items.EMERALD, 4), 32, 4, 0.05f
                ),
                BasicItemListing(
                    ItemStack(ModItems.MEDIUM_ROCKET_CM, 1),
                    ItemStack(Items.EMERALD, 4), 32, 4, 0.05f
                ),
                BasicItemListing(
                    ItemStack(ModItems.JAVELIN_MISSILE, 1),
                    ItemStack(Items.EMERALD, 6), 32, 4, 0.05f
                ),
                BasicItemListing(
                    ItemStack(ModItems.MEDIUM_ANTI_GROUND_MISSILE, 1),
                    ItemStack(Items.EMERALD, 6), 32, 4, 0.05f
                ),
                BasicItemListing(
                    ItemStack(ModItems.LARGE_ANTI_GROUND_MISSILE, 1),
                    ItemStack(Items.EMERALD, 8), 32, 4, 0.05f
                ),
                BasicItemListing(
                    ItemStack(ModItems.EXTRA_LARGE_ANTI_GROUND_MISSILE, 1),
                    ItemStack(Items.EMERALD, 16), 32, 8, 0.05f
                ),
                BasicItemListing(
                    ItemStack(ModItems.MEDIUM_AERIAL_BOMB, 1),
                    ItemStack(Items.EMERALD, 8), 32, 4, 0.05f
                ),
                BasicItemListing(
                    ItemStack(ModItems.LARGE_AERIAL_BOMB, 1),
                    ItemStack(Items.EMERALD, 16), 32, 4, 0.05f
                )
            )
        registerTrade(4, list4)

            // 等级 5 交易
            val list5 = listOf(
                BasicItemListing(
                    ItemStack(Items.EMERALD, 22),
                    ItemStack(ModItems.PERK_ITEMS[ModPerks.POISONOUS_BULLET]!!, 1), 4, 10, 0.05f
                ),
                BasicItemListing(
                    ItemStack(Items.EMERALD, 24),
                    ItemStack(ModItems.PERK_ITEMS[ModPerks.SUBSISTENCE]!!, 1), 4, 10, 0.05f
                ),
                BasicItemListing(
                    ItemStack(Items.EMERALD, 25),
                    ItemStack(ModItems.PERK_ITEMS[ModPerks.KILL_CLIP]!!, 1), 4, 10, 0.05f
                ),
                BasicItemListing(
                    ItemStack(Items.EMERALD, 26),
                    ItemStack(ModItems.PERK_ITEMS[ModPerks.GUTSHOT_STRAIGHT]!!, 1), 4, 10, 0.05f
                ),
                BasicItemListing(
                    ItemStack(Items.EMERALD, 22),
                    ItemStack(ModItems.PERK_ITEMS[ModPerks.HEAD_SEEKER]!!, 1), 4, 10, 0.05f
                ),
                BasicItemListing(
                    ItemStack(Items.EMERALD, 34),
                    ItemStack(ModItems.PERK_ITEMS[ModPerks.SILVER_BULLET]!!, 1), 4, 15, 0.05f
                ),
                BasicItemListing(
                    ItemStack(Items.EMERALD, 30),
                    ItemStack(ModItems.PERK_ITEMS[ModPerks.FIELD_DOCTOR]!!, 1), 4, 15, 0.05f
                ),
                BasicItemListing(
                    ItemStack(Items.EMERALD, 34),
                    ItemStack(ModItems.PERK_ITEMS[ModPerks.HEAL_CLIP]!!, 1), 4, 15, 0.05f
                ),
                BasicItemListing(
                    ItemStack(Items.EMERALD, 30),
                    ItemStack(ModItems.PERK_ITEMS[ModPerks.KILLING_TALLY]!!, 1), 4, 15, 0.05f
                ),
                BasicItemListing(
                    ItemStack(Items.EMERALD, 34),
                    ItemStack(ModItems.PERK_ITEMS[ModPerks.FOURTH_TIMES_CHARM]!!, 1), 4, 15, 0.05f
                ),
                BasicItemListing(
                    ItemStack(Items.EMERALD, 48),
                    ItemStack(ModItems.PERK_ITEMS[ModPerks.MONSTER_HUNTER]!!, 1), 4, 25, 0.05f
                ),
                BasicItemListing(
                    ItemStack(Items.EMERALD, 40),
                    ItemStack(ModItems.PERK_ITEMS[ModPerks.VORPAL_WEAPON]!!, 1), 4, 25, 0.05f
                ),
                BasicItemListing(
                    ItemStack(Items.EMERALD, 42),
                    ItemStack(ModItems.PERK_ITEMS[ModPerks.MAGNIFICENT_HOWL]!!, 1), 4, 25, 0.05f
                ),
                BasicItemListing(
                    ItemStack(Items.EMERALD, 64),
                    ItemStack(ModItems.PERK_ITEMS[ModPerks.FAIR_MEANS]!!, 1), 4, 25, 0.05f
                ),
                BasicItemListing(
                    ItemStack(Items.EMERALD, 40),
                    ItemStack(ModItems.PERK_ITEMS[ModPerks.HIGH_IMPACT_RESERVES]!!, 1), 4, 25, 0.05f
                ),
                BasicItemListing(
                    ItemStack(Items.EMERALD, 48),
                    ItemStack(ModItems.PERK_ITEMS[ModPerks.ONE_TWO_PUNCH]!!, 1), 4, 25, 0.05f
                )
            )
        registerTrade(5, list5)
        TradeOfferHelper.registerWanderingTraderOffers(2) { rareTrades ->
            rareTrades.add(
                BasicItemListing(
                    ItemStack(Items.EMERALD, 16),
                    SmallContainerBlockItem.createInstance(loc("containers/blueprints")), 10, 0, 0.05f
                )
            )
            rareTrades.add(
                BasicItemListing(
                    ItemStack(Items.EMERALD, 10),
                    SmallContainerBlockItem.createInstance(loc("containers/common")), 10, 0, 0.05f
                )
            )
        }
    }

    private fun registerTrade(level: Int, listings: List<VillagerTrades.ItemListing>) {
        TradeOfferHelper.registerVillagerOffers(ARMORY, level) { factories -> factories.addAll(listings) }
    }

    private fun BasicItemListing(
        buy: ItemStack,
        sell: ItemStack,
        maxTrades: Int,
        xp: Int,
        priceMultiplier: Float
    ) = VillagerTrades.ItemListing { _, _ ->
        MerchantOffer(ItemCost(buy.item, buy.count), sell, maxTrades, xp, priceMultiplier)
    }
}
