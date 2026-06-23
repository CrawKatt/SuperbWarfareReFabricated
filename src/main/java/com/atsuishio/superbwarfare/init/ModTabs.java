package com.atsuishio.superbwarfare.init;

import com.atsuishio.superbwarfare.Mod;
import com.atsuishio.superbwarfare.api.event.RegisterContainersEvent;
import com.atsuishio.superbwarfare.item.ArmorPlate;
import com.atsuishio.superbwarfare.item.BatteryItem;
import com.atsuishio.superbwarfare.item.C4BombItem;
import com.atsuishio.superbwarfare.item.ElectricBaton;
import com.atsuishio.superbwarfare.item.common.container.LuckyContainerBlockItem;
import com.atsuishio.superbwarfare.item.common.container.SmallContainerBlockItem;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.flag.FeatureFlagSet;
import team.reborn.energy.api.EnergyStorage;

import java.util.function.Supplier;

public class ModTabs {

    public static final CreativeModeTab GUN_TAB = register("guns",
            FabricItemGroup.builder()
                    .title(Component.translatable("item_group.superbwarfare.guns"))
                    .icon(() -> new ItemStack(ModItems.TASER))
                    .displayItems((param, output) -> {
                        output.accept(ModItems.REPAIR_TOOL);
                        output.accept(ModItems.TASER);
                        output.accept(ModItems.GLOCK_17);
                        output.accept(ModItems.GLOCK_18);
                        output.accept(ModItems.MP_443);
                        output.accept(ModItems.M_1911);
                        output.accept(ModItems.HOMEMADE_SHOTGUN);
                        output.accept(ModItems.TRACHELIUM);
                        output.accept(ModItems.MP_5);
                        output.accept(ModItems.VECTOR);
                        output.accept(ModItems.AK_47);
                        output.accept(ModItems.AK_12);
                        output.accept(ModItems.SKS);
                        output.accept(ModItems.M_4);
                        output.accept(ModItems.HK_416);
                        output.accept(ModItems.QBZ_95);
                        output.accept(ModItems.QBZ_191);
                        output.accept(ModItems.INSIDIOUS);
                        output.accept(ModItems.MK_14);
                        output.accept(ModItems.QL_1031);
                        output.accept(ModItems.MARLIN);
                        output.accept(ModItems.K_98);
                        output.accept(ModItems.MOSIN_NAGANT);
                        output.accept(ModItems.SVD);
                        output.accept(ModItems.AWM);
                        output.accept(ModItems.M_98B);
                        output.accept(ModItems.SENTINEL);
                        output.accept(ModItems.HUNTING_RIFLE);
                        output.accept(ModItems.NTW_20);
                        output.accept(ModItems.M_870);
                        output.accept(ModItems.AA_12);
                        output.accept(ModItems.DEVOTION);
                        output.accept(ModItems.RPK);
                        output.accept(ModItems.M_60);
                        output.accept(ModItems.M_2_HB);
                        output.accept(ModItems.MINIGUN);
                        output.accept(ModItems.M_79);
                        output.accept(ModItems.SECONDARY_CATACLYSM);
                        output.accept(ModItems.RPG);
                        output.accept(ModItems.JAVELIN);
                        output.accept(ModItems.IGLA_9K38);
                        output.accept(ModItems.AURELIA_SCEPTRE);
                        output.accept(ModItems.BOCEK);

                        addChargedVariants(output, ModItems.TASER, ModItems.GLOCK_18, ModItems.DEVOTION, ModItems.MINIGUN,
                                ModItems.M_79, ModItems.SECONDARY_CATACLYSM, ModItems.AURELIA_SCEPTRE, ModItems.BOCEK,
                                ModItems.REPAIR_TOOL, ModItems.INSIDIOUS, ModItems.TRACHELIUM, ModItems.AWM,
                                ModItems.SENTINEL, ModItems.NTW_20, ModItems.AA_12, ModItems.RPK, ModItems.M_60,
                                ModItems.M_2_HB, ModItems.QL_1031);
                    })
                    .build());

    public static final CreativeModeTab PERK_TAB = register("perk",
            FabricItemGroup.builder()
                    .title(Component.translatable("item_group.superbwarfare.perk"))
                    .icon(() -> new ItemStack(ModItems.AP_BULLET))
                    .displayItems((param, output) -> {
                        output.accept(ModItems.REFORGING_TABLE);
                        ModItems.PERK_ITEMS.values().forEach(output::accept);
                    })
                    .build());

    public static final CreativeModeTab AMMO_TAB = register("ammo",
            FabricItemGroup.builder()
                    .title(Component.translatable("item_group.superbwarfare.ammo"))
                    .icon(() -> new ItemStack(ModItems.SHOTGUN_AMMO_BOX))
                    .displayItems((param, output) -> {
                        output.accept(ModItems.HANDGUN_AMMO);
                        output.accept(ModItems.RIFLE_AMMO);
                        output.accept(ModItems.SNIPER_AMMO);
                        output.accept(ModItems.SHOTGUN_AMMO);
                        output.accept(ModItems.HEAVY_AMMO);
                        output.accept(ModItems.HANDGUN_AMMO_BOX);
                        output.accept(ModItems.RIFLE_AMMO_BOX);
                        output.accept(ModItems.SNIPER_AMMO_BOX);
                        output.accept(ModItems.SHOTGUN_AMMO_BOX);
                        output.accept(ModItems.CREATIVE_AMMO_BOX);
                        output.accept(ModItems.AMMO_BOX);
                        output.accept(ModItems.TASER_ELECTRODE);
                        output.accept(ModItems.GRENADE_40MM);
                        output.accept(ModItems.MORTAR_SHELL);
                        output.accept(ModItems.RPG_ROCKET_STANDARD);
                        output.accept(ModItems.RPG_ROCKET_TBG);
                        output.accept(ModItems.LUNGE_MINE);
                        output.accept(ModItems.HE_5_INCHES);
                        output.accept(ModItems.AP_5_INCHES);
                        output.accept(ModItems.CM_5_INCHES);
                        output.accept(ModItems.GS_5_INCHES);
                        output.accept(ModItems.HAND_GRENADE);
                        output.accept(ModItems.RGO_GRENADE);
                        output.accept(ModItems.M18_SMOKE_GRENADE);
                        output.accept(ModItems.CLAYMORE_MINE);
                        output.accept(ModItems.TM_62);
                        output.accept(ModItems.PTKM_1R);
                        output.accept(ModItems.BLU_43_MINE);
                        output.accept(ModItems.SMALL_SHELL);
                        output.accept(ModItems.SMALL_ROCKET);
                        output.accept(ModItems.MEDIUM_ROCKET_AP);
                        output.accept(ModItems.MEDIUM_ROCKET_HE);
                        output.accept(ModItems.MEDIUM_ROCKET_CM);
                        output.accept(ModItems.JAVELIN_MISSILE);
                        output.accept(ModItems.MEDIUM_ANTI_AIR_MISSILE);
                        output.accept(ModItems.MEDIUM_ANTI_GROUND_MISSILE);
                        output.accept(ModItems.LARGE_ANTI_GROUND_MISSILE);
                        output.accept(ModItems.SWARM_DRONE);
                        output.accept(ModItems.MEDIUM_AERIAL_BOMB);
                        output.accept(ModItems.C4_BOMB);
                        output.accept(C4BombItem.makeInstance());
                        output.accept(ModItems.POTION_MORTAR_SHELL);

                        param.holders().lookup(Registries.POTION)
                                .ifPresent(potion -> generatePotionEffectTypes(output, potion, ModItems.POTION_MORTAR_SHELL,
                                        CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS,
                                        param.enabledFeatures()));
                    })
                    .build());

    public static final CreativeModeTab ITEM_TAB = register("item",
            FabricItemGroup.builder()
                    .title(Component.translatable("item_group.superbwarfare.item"))
                    .icon(() -> new ItemStack(ModItems.TARGET_DEPLOYER))
                    .displayItems((param, output) -> {
                        output.accept(ModItems.ANCIENT_CPU);
                        output.accept(ModItems.PROPELLER);
                        output.accept(ModItems.LARGE_PROPELLER);
                        output.accept(ModItems.MOTOR);
                        output.accept(ModItems.LARGE_MOTOR);
                        output.accept(ModItems.WHEEL);
                        output.accept(ModItems.TRACK);
                        output.accept(ModItems.DRONE);
                        output.accept(ModItems.MONITOR);
                        output.accept(ModItems.ARTILLERY_INDICATOR);
                        output.accept(ModItems.DETONATOR);
                        output.accept(ModItems.TARGET_DEPLOYER);
                        output.accept(ModItems.DPS_GENERATOR_DEPLOYER);
                        output.accept(ModItems.KNIFE);
                        output.accept(ModItems.HAMMER);
                        output.accept(ModItems.GOLDEN_HAMMER);
                        output.accept(ModItems.STEEL_HAMMER);
                        output.accept(ModItems.DIAMOND_HAMMER);
                        output.accept(ModItems.CEMENTED_CARBIDE_HAMMER);
                        output.accept(ModItems.NETHERITE_HAMMER);
                        output.accept(ModItems.T_BATON);
                        output.accept(ModItems.ELECTRIC_BATON);
                        output.accept(ModItems.STEEL_PIPE);
                        output.accept(ModItems.CROWBAR);
                        output.accept(ModItems.DEFUSER);
                        output.accept(ModItems.ARMOR_PLATE);
                        output.accept(ArmorPlate.getInfiniteInstance());
                        output.accept(ModItems.MORTAR_DEPLOYER);
                        output.accept(ModItems.MORTAR_BARREL);
                        output.accept(ModItems.MORTAR_BASE_PLATE);
                        output.accept(ModItems.MORTAR_BIPOD);
                        output.accept(ModItems.TOW_DEPLOYER);
                        output.accept(ModItems.SEEKER);
                        output.accept(ModItems.MISSILE_ENGINE);
                        output.accept(ModItems.FUSEE);
                        output.accept(ModItems.PRIMER);
                        output.accept(ModItems.AP_HEAD);
                        output.accept(ModItems.HE_HEAD);
                        output.accept(ModItems.CM_HEAD);
                        output.accept(ModItems.GS_HEAD);
                        output.accept(ModItems.CANNON_CORE);
                        output.accept(ModItems.COPPER_PLATE);
                        output.accept(ModItems.STEEL_INGOT);
                        output.accept(ModItems.LEAD_INGOT);
                        output.accept(ModItems.SILVER_INGOT);
                        output.accept(ModItems.TUNGSTEN_INGOT);
                        output.accept(ModItems.CEMENTED_CARBIDE_INGOT);
                        output.accept(ModItems.HIGH_ENERGY_EXPLOSIVES);
                        output.accept(ModItems.GRAIN);
                        output.accept(ModItems.IRON_POWDER);
                        output.accept(ModItems.TUNGSTEN_POWDER);
                        output.accept(ModItems.COAL_POWDER);
                        output.accept(ModItems.COAL_IRON_POWDER);
                        output.accept(ModItems.RAW_CEMENTED_CARBIDE_POWDER);
                        output.accept(ModItems.GALENA);
                        output.accept(ModItems.SCHEELITE);
                        output.accept(ModItems.RAW_SILVER);
                        output.accept(ModItems.DOG_TAG);
                        output.accept(ModItems.IFF);
                        output.accept(ModItems.CELL);
                        output.accept(ModItems.BATTERY);
                        output.accept(ModItems.SMALL_BATTERY_PACK);
                        output.accept(ModItems.MEDIUM_BATTERY_PACK);
                        output.accept(ModItems.LARGE_BATTERY_PACK);
                        output.accept(ModItems.LASER_UNIT);
                        output.accept(ModItems.BEAST);
                        output.accept(ModItems.TRANSCRIPT);
                        output.accept(ModItems.FIRING_PARAMETERS);
                        output.accept(ModItems.MEDICAL_KIT);
                        output.accept(ModItems.VEHICLE_DAMAGE_ANALYZER);
                        output.accept(ModItems.VEHICLE_RESET_KIT);
                        output.accept(ModItems.TUNGSTEN_ROD);
                        output.accept(ModItems.LIGHT_ARMAMENT_MODULE);
                        output.accept(ModItems.MEDIUM_ARMAMENT_MODULE);
                        output.accept(ModItems.HEAVY_ARMAMENT_MODULE);
                        output.accept(ModItems.IRON_MATERIALS.barrel());
                        output.accept(ModItems.IRON_MATERIALS.action());
                        output.accept(ModItems.IRON_MATERIALS.spring());
                        output.accept(ModItems.IRON_MATERIALS.trigger());
                        output.accept(ModItems.STEEL_MATERIALS.barrel());
                        output.accept(ModItems.STEEL_MATERIALS.action());
                        output.accept(ModItems.STEEL_MATERIALS.spring());
                        output.accept(ModItems.STEEL_MATERIALS.trigger());
                        output.accept(ModItems.CEMENTED_CARBIDE_MATERIALS.barrel());
                        output.accept(ModItems.CEMENTED_CARBIDE_MATERIALS.action());
                        output.accept(ModItems.CEMENTED_CARBIDE_MATERIALS.spring());
                        output.accept(ModItems.CEMENTED_CARBIDE_MATERIALS.trigger());
                        output.accept(ModItems.NETHERITE_MATERIALS.barrel());
                        output.accept(ModItems.NETHERITE_MATERIALS.action());
                        output.accept(ModItems.NETHERITE_MATERIALS.spring());
                        output.accept(ModItems.NETHERITE_MATERIALS.trigger());
                        output.accept(ModItems.COMMON_MATERIAL_PACK);
                        output.accept(ModItems.RARE_MATERIAL_PACK);
                        output.accept(ModItems.EPIC_MATERIAL_PACK);
                        output.accept(ModItems.LEGENDARY_MATERIAL_PACK);
                        output.accept(ModItems.RU_HELMET_6B47);
                        output.accept(ModItems.RU_CHEST_6B43);
                        output.accept(ModItems.US_HELMET_PASGT);
                        output.accept(ModItems.US_CHEST_IOTV);
                        output.accept(ModItems.GE_HELMET_M_35);
                        output.accept(ModItems.PARACHUTE);
                        output.accept(ModItems.SHORTCUT_PACK);
                        output.accept(ModItems.EMPTY_PERK);

                        addBatteryFullVariants(output, ModItems.CELL, ModItems.BATTERY, ModItems.SMALL_BATTERY_PACK,
                                ModItems.MEDIUM_BATTERY_PACK, ModItems.LARGE_BATTERY_PACK);
                        if (ModItems.ELECTRIC_BATON instanceof ElectricBaton baton) {
                            output.accept(baton.makeFullEnergyStack());
                        }
                    })
                    .build());

    public static final CreativeModeTab BLOCK_TAB = register("block",
            FabricItemGroup.builder()
                    .title(Component.translatable("item_group.superbwarfare.block"))
                    .icon(() -> new ItemStack(ModItems.SANDBAG))
                    .displayItems((param, output) -> {
                        output.accept(ModItems.SANDBAG);
                        output.accept(ModItems.BARBED_WIRE);
                        output.accept(ModItems.DRAGON_TEETH);
                        output.accept(ModItems.GALENA_ORE);
                        output.accept(ModItems.DEEPSLATE_GALENA_ORE);
                        output.accept(ModItems.SCHEELITE_ORE);
                        output.accept(ModItems.DEEPSLATE_SCHEELITE_ORE);
                        output.accept(ModItems.SILVER_ORE);
                        output.accept(ModItems.DEEPSLATE_SILVER_ORE);
                        output.accept(ModItems.LEAD_BLOCK);
                        output.accept(ModItems.STEEL_BLOCK);
                        output.accept(ModItems.TUNGSTEN_BLOCK);
                        output.accept(ModItems.SILVER_BLOCK);
                        output.accept(ModItems.CEMENTED_CARBIDE_BLOCK);
                        output.accept(ModItems.JUMP_PAD);
                        output.accept(ModItems.REFORGING_TABLE);
                        output.accept(ModItems.CHARGING_STATION);
                        output.accept(ModItems.CREATIVE_CHARGING_STATION);
                        output.accept(ModItems.FUMO_25);
                        output.accept(ModItems.VEHICLE_DEPLOYER);
                        output.accept(ModItems.AIRCRAFT_CATAPULT);
                        output.accept(ModItems.SUPERB_ITEM_INTERFACE);
                        output.accept(ModItems.CREATIVE_SUPERB_ITEM_INTERFACE);
                        output.accept(ModItems.VEHICLE_ASSEMBLING_TABLE);
                    })
                    .build());

    public static final CreativeModeTab VEHICLE_TAB = register("vehicle",
            FabricItemGroup.builder()
                    .title(Component.translatable("item_group.superbwarfare.vehicle"))
                    .icon(() -> new ItemStack(ModItems.CONTAINER))
                    .displayItems((param, output) -> {
                        output.accept(ModItems.CROWBAR);
                        output.accept(ModItems.VEHICLE_ASSEMBLING_TABLE);
                        RegisterContainersEvent.CONTAINERS.forEach(output::accept);
                        output.accept(ModItems.LUCKY_CONTAINER);
                        LuckyContainerBlockItem.LUCKY_CONTAINERS.stream().map(Supplier::get).forEach(output::accept);
                        output.accept(ModItems.SMALL_CONTAINER);
                        SmallContainerBlockItem.SMALL_CONTAINERS.stream().map(Supplier::get).forEach(output::accept);
                    })
                    .build());

    public static void init() {
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.SPAWN_EGGS).register(entries -> {
            entries.accept(ModItems.SENPAI_SPAWN_EGG);
        });
    }

    private static CreativeModeTab register(String name, CreativeModeTab tab) {
        return Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, Mod.loc(name), tab);
    }

    private static void addChargedVariants(CreativeModeTab.Output output, ItemStack... items) {
        for (var stack : items) {
            var storage = EnergyStorage.ITEM.find(stack, null);
            if (storage != null && storage.getCapacity() > 0) {
                var charged = stack.copy();
                storage = EnergyStorage.ITEM.find(charged, null);
                if (storage != null) {
                    try (var t = Transaction.openOuter()) {
                        storage.insert(Long.MAX_VALUE, t);
                        t.commit();
                    }
                }
                output.accept(charged);
            }
        }
    }

    private static void addChargedVariants(CreativeModeTab.Output output, Item item, Item... items) {
        addChargedVariants(output, new ItemStack(item));
        for (var i : items) {
            addChargedVariants(output, new ItemStack(i));
        }
    }

    private static void addBatteryFullVariants(CreativeModeTab.Output output, Item... items) {
        for (var item : items) {
            if (item instanceof BatteryItem batteryItem) {
                output.accept(batteryItem.makeFullEnergyStack());
            }
        }
    }

    private static void generatePotionEffectTypes(
            CreativeModeTab.Output output, HolderLookup<Potion> potions, Item item,
            CreativeModeTab.TabVisibility visibility, FeatureFlagSet requiredFeatures
    ) {
        potions.listElements()
                .filter(potion -> potion.value().isEnabled(requiredFeatures))
                .map(potion -> PotionContents.createItemStack(item, potion))
                .forEach(itemStack -> output.accept(itemStack, visibility));
    }
}
