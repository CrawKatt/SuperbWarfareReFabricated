package com.atsuishio.superbwarfare.init;

import com.atsuishio.superbwarfare.Mod;
import com.atsuishio.superbwarfare.data.gun.Ammo;
import com.atsuishio.superbwarfare.entity.projectile.MediumRocketEntity;
import com.atsuishio.superbwarfare.item.*;
import com.atsuishio.superbwarfare.item.armor.*;
import com.atsuishio.superbwarfare.item.common.BlueprintItem;
import com.atsuishio.superbwarfare.item.common.MaterialPack;
import com.atsuishio.superbwarfare.item.common.MedicalKitItem;
import com.atsuishio.superbwarfare.item.common.ammo.*;
import com.atsuishio.superbwarfare.item.common.container.ContainerBlockItem;
import com.atsuishio.superbwarfare.item.common.container.LuckyContainerBlockItem;
import com.atsuishio.superbwarfare.item.common.container.SmallContainerBlockItem;
import com.atsuishio.superbwarfare.item.trinket.DogTagItem;
import com.atsuishio.superbwarfare.item.trinket.IffItem;
import com.atsuishio.superbwarfare.item.trinket.ParachuteItem;
import com.atsuishio.superbwarfare.item.gun.handgun.*;
import com.atsuishio.superbwarfare.item.gun.launcher.*;
import com.atsuishio.superbwarfare.item.gun.machinegun.*;
import com.atsuishio.superbwarfare.item.gun.rifle.*;
import com.atsuishio.superbwarfare.item.gun.shotgun.Aa12Item;
import com.atsuishio.superbwarfare.item.gun.shotgun.HomemadeShotgunItem;
import com.atsuishio.superbwarfare.item.gun.shotgun.M870Item;
import com.atsuishio.superbwarfare.item.gun.smg.Mp5Item;
import com.atsuishio.superbwarfare.item.gun.smg.VectorItem;
import com.atsuishio.superbwarfare.item.gun.sniper.*;
import com.atsuishio.superbwarfare.item.gun.special.BocekItem;
import com.atsuishio.superbwarfare.item.gun.special.RepairToolItem;
import com.atsuishio.superbwarfare.item.gun.special.TaserItem;
import com.atsuishio.superbwarfare.item.gun.vehicle.VehicleGun;
import com.atsuishio.superbwarfare.perk.Perk;
import com.atsuishio.superbwarfare.tiers.ModItemTier;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DispenserBlock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class ModItems {

    public static final Item REPAIR_TOOL = register("repair_tool", new RepairToolItem());
    public static final Item TASER = register("taser", new TaserItem());
    public static final Item GLOCK_17 = register("glock_17", new Glock17Item());
    public static final Item GLOCK_18 = register("glock_18", new Glock18Item());
    public static final Item MP_443 = register("mp_443", new Mp443Item());
    public static final Item M_1911 = register("m_1911", new M1911Item());
    public static final Item HOMEMADE_SHOTGUN = register("homemade_shotgun", new HomemadeShotgunItem());
    public static final Item TRACHELIUM = register("trachelium", new TracheliumItem());
    public static final Item MP_5 = register("mp_5", new Mp5Item());
    public static final Item VECTOR = register("vector", new VectorItem());
    public static final Item AK_47 = register("ak_47", new AK47Item());
    public static final Item AK_12 = register("ak_12", new AK12Item());
    public static final Item SKS = register("sks", new SksItem());
    public static final Item M_4 = register("m_4", new M4Item());
    public static final Item HK_416 = register("hk_416", new Hk416Item());
    public static final Item QBZ_95 = register("qbz_95", new Qbz95Item());
    public static final Item QBZ_191 = register("qbz_191", new Qbz191Item());
    public static final Item INSIDIOUS = register("insidious", new InsidiousItem());
    public static final Item MK_14 = register("mk_14", new Mk14Item());
    public static final Item QL_1031 = register("ql_1031", new Ql1031Item());
    public static final Item MARLIN = register("marlin", new MarlinItem());
    public static final Item K_98 = register("k_98", new K98Item());
    public static final Item MOSIN_NAGANT = register("mosin_nagant", new MosinNagantItem());
    public static final Item SVD = register("svd", new SvdItem());

    public static final Item AWM = register("awm", new AwmItem());
    public static final Item M_98B = register("m_98b", new M98bItem());
    public static final Item SENTINEL = register("sentinel", new SentinelItem());
    public static final Item HUNTING_RIFLE = register("hunting_rifle", new HuntingRifleItem());
    public static final Item NTW_20 = register("ntw_20", new Ntw20Item());
    public static final Item M_870 = register("m_870", new M870Item());
    public static final Item AA_12 = register("aa_12", new Aa12Item());
    public static final Item DEVOTION = register("devotion", new DevotionItem());
    public static final Item RPK = register("rpk", new RpkItem());
    public static final Item M_60 = register("m_60", new M60Item());
    public static final Item M_2_HB = register("m_2_hb", new M2HBItem());
    public static final Item MINIGUN = register("minigun", new MinigunItem());
    public static final Item M_79 = register("m_79", new M79Item());
    public static final Item SECONDARY_CATACLYSM = register("secondary_cataclysm", new SecondaryCataclysmItem());
    public static final Item RPG = register("rpg", new RpgItem());
    public static final Item JAVELIN = register("javelin", new JavelinItem());
    public static final Item IGLA_9K38 = register("igla_9k38", new IglaItem());
    public static final Item AURELIA_SCEPTRE = register("aurelia_sceptre", new AureliaSceptreItem());
    public static final Item BOCEK = register("bocek", new BocekItem());

    public static final Item VEHICLE_GUN = register("vehicle_gun", new VehicleGun());

    /**
     * Ammo
     */

    public static final Item HANDGUN_AMMO = register("handgun_ammo", new AmmoSupplierItem(Ammo.HANDGUN, 1, new Item.Properties()));
    public static final Item RIFLE_AMMO = register("rifle_ammo", new AmmoSupplierItem(Ammo.RIFLE, 1, new Item.Properties()));
    public static final Item SNIPER_AMMO = register("sniper_ammo", new AmmoSupplierItem(Ammo.SNIPER, 1, new Item.Properties()));
    public static final Item SHOTGUN_AMMO = register("shotgun_ammo", new AmmoSupplierItem(Ammo.SHOTGUN, 1, new Item.Properties()));
    public static final Item HEAVY_AMMO = register("heavy_ammo", new AmmoSupplierItem(Ammo.HEAVY, 1, new Item.Properties()));
    public static final Item HANDGUN_AMMO_BOX = register("handgun_ammo_box", new HandgunAmmoBox());
    public static final Item RIFLE_AMMO_BOX = register("rifle_ammo_box", new RifleAmmoBox());
    public static final Item SNIPER_AMMO_BOX = register("sniper_ammo_box", new SniperAmmoBox());
    public static final Item SHOTGUN_AMMO_BOX = register("shotgun_ammo_box", new ShotgunAmmoBox());
    public static final Item CREATIVE_AMMO_BOX = register("creative_ammo_box", new CreativeAmmoBox());
    public static final Item AMMO_BOX = register("ammo_box", new AmmoBoxItem());
    public static final Item TASER_ELECTRODE = register("taser_electrode", new Item(new Item.Properties()));
    public static final Item GRENADE_40MM = register("grenade_40mm", new Item(new Item.Properties()));

    public static final Item MORTAR_SHELL = register("mortar_shell", new MortarShell());
    public static final Item POTION_MORTAR_SHELL = register("potion_mortar_shell", new PotionMortarShell());
    public static final Item RPG_ROCKET_STANDARD = register("rpg_rocket_standard", new RpgRocketStandard());
    public static final Item RPG_ROCKET_TBG = register("rpg_rocket_tbg", new RpgRocketTBG());
    public static final Item LUNGE_MINE = register("lunge_mine", new LungeMine());
    public static final Item HE_5_INCHES = register("he_5_inches", new CannonShellItem(new Item.Properties().rarity(Rarity.RARE)));
    public static final Item AP_5_INCHES = register("ap_5_inches", new CannonShellItem(new Item.Properties().rarity(Rarity.RARE)));
    public static final Item CM_5_INCHES = register("cm_5_inches", new CannonShellItem(new Item.Properties().rarity(Rarity.RARE)));
    public static final Item GS_5_INCHES = register("gs_5_inches", new CannonShellItem(new Item.Properties().rarity(Rarity.RARE)));
    public static final Item HAND_GRENADE = register("hand_grenade", new HandGrenade());
    public static final Item RGO_GRENADE = register("rgo_grenade", new RgoGrenade());
    public static final Item M18_SMOKE_GRENADE = register("m18_smoke_grenade", new M18SmokeGrenade());
    public static final Item CLAYMORE_MINE = register("claymore_mine", new ClaymoreMine());
    public static final Item TM_62 = register("tm_62", new Tm62Item());
    public static final Item PTKM_1R = register("ptkm_1r", new Ptkm1rItem());
    public static final Item C4_BOMB = register("c4_bomb", new C4BombItem());
    public static final Item BLU_43_MINE = register("blu_43_mine", new Blu43MineItem());
    public static final Item SMALL_SHELL = register("small_shell", new Item(new Item.Properties()));
    public static final Item SMALL_ROCKET = register("small_rocket", new Item(new Item.Properties().stacksTo(16)));
    public static final Item MEDIUM_ROCKET_AP = register("medium_rocket_ap", new MediumRocketItem(500, 6, 100, 0, 0, MediumRocketEntity.Type.AP, 0));
    public static final Item MEDIUM_ROCKET_HE = register("medium_rocket_he", new MediumRocketItem(200, 12, 200, 0.2f, 40, MediumRocketEntity.Type.HE, 0));
    public static final Item MEDIUM_ROCKET_CM = register("medium_rocket_cm", new MediumRocketItem(300, 12, 300, 0, 0, MediumRocketEntity.Type.CM, 20));
    public static final Item JAVELIN_MISSILE = register("javelin_missile", new Item(new Item.Properties().stacksTo(4)));
    public static final Item MEDIUM_ANTI_AIR_MISSILE = register("medium_anti_air_missile", new Item(new Item.Properties().stacksTo(4)));
    public static final Item MEDIUM_ANTI_GROUND_MISSILE = register("medium_anti_ground_missile", new Item(new Item.Properties().stacksTo(4)));
    public static final Item LARGE_ANTI_GROUND_MISSILE = register("large_anti_ground_missile", new Item(new Item.Properties().stacksTo(2)));
    public static final Item SWARM_DRONE = register("swarm_drone", new Item(new Item.Properties().stacksTo(14)));
    public static final Item MEDIUM_AERIAL_BOMB = register("medium_aerial_bomb", new Item(new Item.Properties().stacksTo(2)));

    /**
     * items
     */

    public static Item SENPAI_SPAWN_EGG;

    public static final Item ANCIENT_CPU = register("ancient_cpu", new Item(new Item.Properties().rarity(Rarity.RARE)));
    public static final Item PROPELLER = register("propeller", new Item(new Item.Properties()));
    public static final Item LARGE_PROPELLER = register("large_propeller", new Item(new Item.Properties()));
    public static final Item MOTOR = register("motor", new Item(new Item.Properties()));
    public static final Item LARGE_MOTOR = register("large_motor", new Item(new Item.Properties()));
    public static final Item WHEEL = register("wheel", new Item(new Item.Properties()));
    public static final Item TRACK = register("track", new Item(new Item.Properties()));
    public static final Item DRONE = register("drone", new Drone());

    public static final Item MONITOR = register("monitor", new Monitor());
    public static final Item ARTILLERY_INDICATOR = register("artillery_indicator", new ArtilleryIndicator());

    public static final Item DETONATOR = register("detonator", new Detonator());
    public static final Item TARGET_DEPLOYER = register("target_deployer", new TargetDeployer());
    public static final Item DPS_GENERATOR_DEPLOYER = register("dps_generator_deployer", new DPSGeneratorDeployer());
    public static final Item KNIFE = register("knife", new SwordItem(ModItemTier.STEEL, new CustomDamageProperty(1200).attributes(SwordItem.createAttributes(ModItemTier.STEEL, 0, -1.8f))));
    public static final Item HAMMER = register("hammer", new Hammer(Tiers.IRON, 11, -3.2f, 400));
    public static final Item GOLDEN_HAMMER = register("golden_hammer", new Hammer(Tiers.GOLD, 11, -3.2f, 150));
    public static final Item STEEL_HAMMER = register("steel_hammer", new Hammer(ModItemTier.STEEL, 9, -3.2f, 600));
    public static final Item DIAMOND_HAMMER = register("diamond_hammer", new Hammer(Tiers.DIAMOND, 12, -3.2f, 1500));
    public static final Item CEMENTED_CARBIDE_HAMMER = register("cemented_carbide_hammer", new Hammer(ModItemTier.CEMENTED_CARBIDE, 8, -3.2f, 2000));
    public static final Item NETHERITE_HAMMER = register("netherite_hammer", new NetheriteHammer());
    public static final Item T_BATON = register("t_baton", new TBaton());
    public static final Item ELECTRIC_BATON = register("electric_baton", new ElectricBaton());
    public static final Item STEEL_PIPE = register("steel_pipe", new SteelPipe());
    public static final Item CROWBAR = register("crowbar", new Crowbar());
    public static final Item DEFUSER = register("defuser", new Defuser());
    public static final Item ARMOR_PLATE = register("armor_plate", new ArmorPlate());

    public static final Item RU_HELMET_6B47 = register("ru_helmet_6b47", new RuHelmet6b47());
    public static final Item RU_CHEST_6B43 = register("ru_chest_6b43", new RuChest6b43());
    public static final Item US_HELMET_PASGT = register("us_helmet_pasgt", new UsHelmetPasgt());
    public static final Item US_CHEST_IOTV = register("us_chest_iotv", new UsChestIotv());
    public static final Item GE_HELMET_M_35 = register("ge_helmet_m_35", new GeHelmetM35());
    public static final Item PARACHUTE = register("parachute", new ParachuteItem());

    public static final Item MORTAR_DEPLOYER = register("mortar_deployer", new MortarDeployer());
    public static final Item MORTAR_BARREL = register("mortar_barrel", new Item(new Item.Properties()));
    public static final Item MORTAR_BASE_PLATE = register("mortar_base_plate", new Item(new Item.Properties()));
    public static final Item MORTAR_BIPOD = register("mortar_bipod", new Item(new Item.Properties()));
    public static final Item TOW_DEPLOYER = register("tow_deployer", new TowDeployer());
    public static final Item SEEKER = register("seeker", new Item(new Item.Properties()));
    public static final Item MISSILE_ENGINE = register("missile_engine", new Item(new Item.Properties()));
    public static final Item FUSEE = register("fusee", new Item(new Item.Properties()));
    public static final Item PRIMER = register("primer", new Item(new Item.Properties()));
    public static final Item AP_HEAD = register("ap_head", new Item(new Item.Properties()));
    public static final Item HE_HEAD = register("he_head", new Item(new Item.Properties()));
    public static final Item CM_HEAD = register("cm_head", new Item(new Item.Properties()));
    public static final Item GS_HEAD = register("gs_head", new Item(new Item.Properties()));
    public static final Item CANNON_CORE = register("cannon_core", new Item(new Item.Properties()));
    public static final Item COPPER_PLATE = register("copper_plate", new Item(new Item.Properties()));
    public static final Item STEEL_INGOT = register("steel_ingot", new Item(new Item.Properties()));
    public static final Item LEAD_INGOT = register("lead_ingot", new Item(new Item.Properties()));
    public static final Item SILVER_INGOT = register("silver_ingot", new Item(new Item.Properties()));
    public static final Item TUNGSTEN_INGOT = register("tungsten_ingot", new Item(new Item.Properties()));
    public static final Item CEMENTED_CARBIDE_INGOT = register("cemented_carbide_ingot", new Item(new Item.Properties()));
    public static final Item HIGH_ENERGY_EXPLOSIVES = register("high_energy_explosives", new Item(new Item.Properties()));
    public static final Item GRAIN = register("grain", new Item(new Item.Properties()));
    public static final Item IRON_POWDER = register("iron_powder", new Item(new Item.Properties()));
    public static final Item TUNGSTEN_POWDER = register("tungsten_powder", new Item(new Item.Properties()));
    public static final Item COAL_POWDER = register("coal_powder", new Item(new Item.Properties()));
    public static final Item COAL_IRON_POWDER = register("coal_iron_powder", new Item(new Item.Properties()));
    public static final Item RAW_CEMENTED_CARBIDE_POWDER = register("raw_cemented_carbide_powder", new Item(new Item.Properties()));
    public static final Item GALENA = register("galena", new Item(new Item.Properties()));
    public static final Item SCHEELITE = register("scheelite", new Item(new Item.Properties()));
    public static final Item RAW_SILVER = register("raw_silver", new Item(new Item.Properties()));
    public static final Item DOG_TAG = register("dog_tag", new DogTagItem());
    public static final Item IFF = register("iff", new IffItem());
    public static final Item CELL = register("cell", new BatteryItem(24000, new Item.Properties()));
    public static final Item BATTERY = register("battery", new BatteryItem(100000, new Item.Properties()));
    public static final Item SMALL_BATTERY_PACK = register("small_battery_pack", new BatteryItem(500000, new Item.Properties()));
    public static final Item MEDIUM_BATTERY_PACK = register("medium_battery_pack", new BatteryItem(5000000, new Item.Properties()));
    public static final Item LARGE_BATTERY_PACK = register("large_battery_pack", new BatteryItem(20000000, new Item.Properties()));
    public static final Item LASER_UNIT = register("laser_unit", new Item(new Item.Properties()));
    public static final Item BEAST = register("beast", new Beast());
    public static final Item TRANSCRIPT = register("transcript", new Transcript());
    public static final Item FIRING_PARAMETERS = register("firing_parameters", new FiringParameters());
    public static final Item MEDICAL_KIT = register("medical_kit", new MedicalKitItem());
    public static final Item VEHICLE_DAMAGE_ANALYZER = register("vehicle_damage_analyzer", new VehicleDamageAnalyzer());
    public static final Item VEHICLE_RESET_KIT = register("vehicle_reset_kit", new VehicleResetKit());

    public static final Item TUNGSTEN_ROD = register("tungsten_rod", new Item(new Item.Properties()));

    public static final Materials IRON_MATERIALS = registerMaterials("iron");
    public static final Materials STEEL_MATERIALS = registerMaterials("steel");
    public static final Materials CEMENTED_CARBIDE_MATERIALS = registerMaterials("cemented_carbide");
    public static final Materials NETHERITE_MATERIALS = registerMaterials("netherite");

    public static final Item COMMON_MATERIAL_PACK = register("common_material_pack", new MaterialPack(Rarity.COMMON));
    public static final Item RARE_MATERIAL_PACK = register("rare_material_pack", new MaterialPack(Rarity.RARE));
    public static final Item EPIC_MATERIAL_PACK = register("epic_material_pack", new MaterialPack(Rarity.EPIC));
    public static final Item LEGENDARY_MATERIAL_PACK = register("legendary_material_pack", new MaterialPack(Rarity.EPIC));

    public static final Item LIGHT_ARMAMENT_MODULE = register("light_armament_module", new Item(new Item.Properties().rarity(Rarity.RARE)));
    public static final Item MEDIUM_ARMAMENT_MODULE = register("medium_armament_module", new Item(new Item.Properties().rarity(Rarity.EPIC)));
    public static final Item HEAVY_ARMAMENT_MODULE = register("heavy_armament_module", new Item(new Item.Properties().rarity(Rarity.EPIC)));

    public static final Item TRACHELIUM_BLUEPRINT = register("trachelium_blueprint", new BlueprintItem(Rarity.EPIC));
    public static final Item GLOCK_17_BLUEPRINT = register("glock_17_blueprint", new BlueprintItem(Rarity.COMMON));
    public static final Item MP_443_BLUEPRINT = register("mp_443_blueprint", new BlueprintItem(Rarity.COMMON));
    public static final Item GLOCK_18_BLUEPRINT = register("glock_18_blueprint", new BlueprintItem(Rarity.RARE));
    public static final Item HUNTING_RIFLE_BLUEPRINT = register("hunting_rifle_blueprint", new BlueprintItem(Rarity.RARE));
    public static final Item M_79_BLUEPRINT = register("m_79_blueprint", new BlueprintItem(Rarity.RARE));
    public static final Item RPG_BLUEPRINT = register("rpg_blueprint", new BlueprintItem(Rarity.RARE));
    public static final Item BOCEK_BLUEPRINT = register("bocek_blueprint", new BlueprintItem(Rarity.EPIC));
    public static final Item M_4_BLUEPRINT = register("m_4_blueprint", new BlueprintItem(Rarity.RARE));
    public static final Item AA_12_BLUEPRINT = register("aa_12_blueprint", new BlueprintItem(Rarity.EPIC));
    public static final Item HK_416_BLUEPRINT = register("hk_416_blueprint", new BlueprintItem(Rarity.RARE));
    public static final Item RPK_BLUEPRINT = register("rp_k_blueprint", new BlueprintItem(Rarity.EPIC));
    public static final Item SKS_BLUEPRINT = register("sks_blueprint", new BlueprintItem(Rarity.RARE));
    public static final Item NTW_20_BLUEPRINT = register("ntw_20_blueprint", new BlueprintItem(Rarity.EPIC));
    public static final Item MP_5_BLUEPRINT = register("mp_5_blueprint", new BlueprintItem(Rarity.RARE));
    public static final Item VECTOR_BLUEPRINT = register("vector_blueprint", new BlueprintItem(Rarity.EPIC));
    public static final Item MINIGUN_BLUEPRINT = register("minigun_blueprint", new BlueprintItem(Rarity.EPIC));
    public static final Item MK_14_BLUEPRINT = register("mk_14_blueprint", new BlueprintItem(Rarity.EPIC));
    public static final Item SENTINEL_BLUEPRINT = register("sentinel_blueprint", new BlueprintItem(Rarity.EPIC));
    public static final Item M_60_BLUEPRINT = register("m_60_blueprint", new BlueprintItem(Rarity.EPIC));
    public static final Item SVD_BLUEPRINT = register("svd_blueprint", new BlueprintItem(Rarity.EPIC));
    public static final Item MARLIN_BLUEPRINT = register("marlin_blueprint", new BlueprintItem(Rarity.COMMON));
    public static final Item M_870_BLUEPRINT = register("m_870_blueprint", new BlueprintItem(Rarity.RARE));
    public static final Item AWM_BLUEPRINT = register("awm_blueprint", new BlueprintItem(Rarity.EPIC));
    public static final Item M_98B_BLUEPRINT = register("m_98b_blueprint", new BlueprintItem(Rarity.EPIC));
    public static final Item AK_47_BLUEPRINT = register("ak_47_blueprint", new BlueprintItem(Rarity.RARE));
    public static final Item AK_12_BLUEPRINT = register("ak_12_blueprint", new BlueprintItem(Rarity.RARE));
    public static final Item DEVOTION_BLUEPRINT = register("devotion_blueprint", new BlueprintItem(Rarity.EPIC));
    public static final Item TASER_BLUEPRINT = register("taser_blueprint", new BlueprintItem(Rarity.COMMON));
    public static final Item M_1911_BLUEPRINT = register("m_1911_blueprint", new BlueprintItem(Rarity.COMMON));
    public static final Item QBZ_95_BLUEPRINT = register("qbz_95_blueprint", new BlueprintItem(Rarity.RARE));
    public static final Item QBZ_191_BLUEPRINT = register("qbz_191_blueprint", new BlueprintItem(Rarity.EPIC));
    public static final Item K_98_BLUEPRINT = register("k_98_blueprint", new BlueprintItem(Rarity.RARE));
    public static final Item MOSIN_NAGANT_BLUEPRINT = register("mosin_nagant_blueprint", new BlueprintItem(Rarity.RARE));
    public static final Item IGLA_BLUEPRINT = register("igla_9k38_blueprint", new BlueprintItem(Rarity.EPIC));
    public static final Item JAVELIN_BLUEPRINT = register("javelin_blueprint", new BlueprintItem(Rarity.EPIC));
    public static final Item M_2_HB_BLUEPRINT = register("m_2_hb_blueprint", new BlueprintItem(Rarity.RARE));
    public static final Item SECONDARY_CATACLYSM_BLUEPRINT = register("secondary_cataclysm_blueprint", new BlueprintItem(Rarity.EPIC));
    public static final Item INSIDIOUS_BLUEPRINT = register("insidious_blueprint", new BlueprintItem(Rarity.EPIC));
    public static final Item AURELIA_SCEPTRE_BLUEPRINT = register("aurelia_sceptre_blueprint", new BlueprintItem(Rarity.EPIC));
    public static final Item QL_1031_BLUEPRINT = register("ql_1031_blueprint", new BlueprintItem(Rarity.EPIC));
    public static final Item MK_42_BLUEPRINT = register("mk_42_blueprint", new BlueprintItem(Rarity.EPIC));
    public static final Item MLE_1934_BLUEPRINT = register("mle_1934_blueprint", new BlueprintItem(Rarity.EPIC));
    public static final Item BL_132_BLUEPRINT = register("bl_132_blueprint", new BlueprintItem(Rarity.EPIC));
    public static final Item HPJ_11_BLUEPRINT = register("hpj_11_blueprint", new BlueprintItem(Rarity.EPIC));
    public static final Item ANNIHILATOR_BLUEPRINT = register("annihilator_blueprint", new BlueprintItem(Rarity.EPIC));

    /**
     * Block Items
     */
    public static Item GALENA_ORE;
    public static Item DEEPSLATE_GALENA_ORE;
    public static Item SCHEELITE_ORE;
    public static Item DEEPSLATE_SCHEELITE_ORE;
    public static Item SILVER_ORE;
    public static Item DEEPSLATE_SILVER_ORE;
    public static Item JUMP_PAD;
    public static Item SANDBAG;
    public static Item BARBED_WIRE;
    public static Item DRAGON_TEETH;
    public static Item REFORGING_TABLE;
    public static Item CHARGING_STATION;
    public static Item CREATIVE_CHARGING_STATION;
    public static Item LEAD_BLOCK;
    public static Item STEEL_BLOCK;
    public static Item TUNGSTEN_BLOCK;
    public static Item SILVER_BLOCK;
    public static Item CEMENTED_CARBIDE_BLOCK;
    public static Item FUMO_25;
    public static Item VEHICLE_DEPLOYER;
    public static Item AIRCRAFT_CATAPULT;
    public static Item SUPERB_ITEM_INTERFACE;
    public static Item CREATIVE_SUPERB_ITEM_INTERFACE;
    public static Item VEHICLE_ASSEMBLING_TABLE;

    /**
     * Vehicle Items
     */
    public static final Item CONTAINER = register("container", new ContainerBlockItem());
    public static final Item SMALL_CONTAINER = register("small_container", new SmallContainerBlockItem());
    public static final Item LUCKY_CONTAINER = register("lucky_container", new LuckyContainerBlockItem());

    public record Materials(
            String name,
            Item barrel,
            Item action,
            Item spring,
            Item trigger
    ) {
    }

    public static Materials registerMaterials(String name) {
        return new Materials(
                name,
                register(name + "_barrel", new Item(new Item.Properties())),
                register(name + "_action", new Item(new Item.Properties())),
                register(name + "_spring", new Item(new Item.Properties())),
                register(name + "_trigger", new Item(new Item.Properties()))
        );
    }

    /**
     * Perk Items
     */
    public static final Map<Perk, Item> PERK_ITEMS = new HashMap<>();

    /**
     * 单独注册，用于Tab图标，不要删
     */
    public static Item AP_BULLET;
    public static Item INTELLIGENT_CHIP;

    public static void registerPerkItems() {
        ModPerks.AMMO_PERKS.forEach(ModItems::registerSinglePerkItem);
        ModPerks.FUNC_PERKS.forEach(ModItems::registerSinglePerkItem);
        ModPerks.DAMAGE_PERKS.forEach(ModItems::registerSinglePerkItem);

        AP_BULLET = PERK_ITEMS.get(ModPerks.AP_BULLET);
        INTELLIGENT_CHIP = PERK_ITEMS.get(ModPerks.INTELLIGENT_CHIP);
    }

    private static void registerSinglePerkItem(Perk perk) {
        PERK_ITEMS.put(perk, register(perk.getId().getPath(), new PerkItem<>(perk)));
    }

    public static final Item SHORTCUT_PACK = register("shortcut_pack", new ShortcutPack());
    public static final Item EMPTY_PERK = register("empty_perk", new Item(new Item.Properties()));

    public static void registerBlockItems() {
        var epic = Rarity.EPIC;
        var common = Rarity.COMMON;

        GALENA_ORE = blockItem(ModBlocks.GALENA_ORE);
        DEEPSLATE_GALENA_ORE = blockItem(ModBlocks.DEEPSLATE_GALENA_ORE);
        SCHEELITE_ORE = blockItem(ModBlocks.SCHEELITE_ORE);
        DEEPSLATE_SCHEELITE_ORE = blockItem(ModBlocks.DEEPSLATE_SCHEELITE_ORE);
        SILVER_ORE = blockItem(ModBlocks.SILVER_ORE);
        DEEPSLATE_SILVER_ORE = blockItem(ModBlocks.DEEPSLATE_SILVER_ORE);
        JUMP_PAD = blockItem(ModBlocks.JUMP_PAD);
        SANDBAG = blockItem(ModBlocks.SANDBAG);
        BARBED_WIRE = blockItem(ModBlocks.BARBED_WIRE);
        DRAGON_TEETH = blockItem(ModBlocks.DRAGON_TEETH);
        REFORGING_TABLE = blockItem(ModBlocks.REFORGING_TABLE);
        CHARGING_STATION = register("charging_station", new ChargingStationBlockItem());
        CREATIVE_CHARGING_STATION = register("creative_charging_station", new CreativeChargingStationBlockItem());
        LEAD_BLOCK = blockItem(ModBlocks.LEAD_BLOCK);
        STEEL_BLOCK = blockItem(ModBlocks.STEEL_BLOCK);
        TUNGSTEN_BLOCK = blockItem(ModBlocks.TUNGSTEN_BLOCK);
        SILVER_BLOCK = blockItem(ModBlocks.SILVER_BLOCK);
        CEMENTED_CARBIDE_BLOCK = blockItem(ModBlocks.CEMENTED_CARBIDE_BLOCK);
        FUMO_25 = blockItem(ModBlocks.FUMO_25);
        VEHICLE_DEPLOYER = register("vehicle_deployer", new VehicleDeployerBlockItem());
        AIRCRAFT_CATAPULT = blockItem(ModBlocks.AIRCRAFT_CATAPULT);
        SUPERB_ITEM_INTERFACE = blockItem(ModBlocks.SUPERB_ITEM_INTERFACE);
        CREATIVE_SUPERB_ITEM_INTERFACE = blockItem(ModBlocks.CREATIVE_SUPERB_ITEM_INTERFACE, epic);
        VEHICLE_ASSEMBLING_TABLE = register("vehicle_assembling_table", new VehicleAssemblingTableBlockItem());
    }

    public static void registerSpawnEggs() {
        SENPAI_SPAWN_EGG = register("senpai_spawn_egg", new SpawnEggItem(ModEntities.SENPAI, -11584987, -14014413, new Item.Properties()));
    }

    private static BlockItem blockItem(Block block) {
        return Registry.register(BuiltInRegistries.ITEM, Mod.loc(BuiltInRegistries.BLOCK.getKey(block).getPath()), new BlockItem(block, new Item.Properties()));
    }

    private static BlockItem blockItem(Block block, Rarity rarity) {
        return Registry.register(BuiltInRegistries.ITEM, Mod.loc(BuiltInRegistries.BLOCK.getKey(block).getPath()), new BlockItem(block, new Item.Properties().rarity(rarity)));
    }

    public static void registerDispenserBehavior() {
        List<Item> list = new ArrayList<>();
        list.addAll(List.of(
                HANDGUN_AMMO, RIFLE_AMMO, SNIPER_AMMO, SHOTGUN_AMMO, HEAVY_AMMO,
                HANDGUN_AMMO_BOX, RIFLE_AMMO_BOX, SNIPER_AMMO_BOX, SHOTGUN_AMMO_BOX,
                CREATIVE_AMMO_BOX, AMMO_BOX,
                ANCIENT_CPU, PROPELLER, LARGE_PROPELLER, MOTOR, LARGE_MOTOR, WHEEL, TRACK,
                STEEL_INGOT, LEAD_INGOT, SILVER_INGOT, TUNGSTEN_INGOT, CEMENTED_CARBIDE_INGOT,
                HIGH_ENERGY_EXPLOSIVES, GRAIN, IRON_POWDER, TUNGSTEN_POWDER, COAL_POWDER,
                COAL_IRON_POWDER, RAW_CEMENTED_CARBIDE_POWDER, GALENA, SCHEELITE, RAW_SILVER
        ));

        for (var item : list) {
            if (item instanceof ProjectileItem launchable) {
                DispenserBlock.registerProjectileBehavior(item);
            }
        }

        DispenserBlock.registerBehavior(C4_BOMB, new C4BombItem.C4DispenseItemBehavior());
        DispenserBlock.registerBehavior(CLAYMORE_MINE, new ClaymoreMine.ClaymoreDispenseBehavior());
        DispenserBlock.registerBehavior(BLU_43_MINE, new Blu43MineItem.Blu43MineDispenseBehavior());
        DispenserBlock.registerBehavior(RPG_ROCKET_STANDARD, new RpgRocketStandard.RocketDispenseBehavior());
        DispenserBlock.registerBehavior(RPG_ROCKET_TBG, new RpgRocketTBG.RocketDispenseBehavior());
        DispenserBlock.registerBehavior(RGO_GRENADE, new RgoGrenade.RgoGrenadeDispenserBehavior());
        DispenserBlock.registerBehavior(M18_SMOKE_GRENADE, new M18SmokeGrenade.SmokeGrenadeDispenserBehavior());
        DispenserBlock.registerBehavior(TM_62, new Tm62Item.Tm62DispenseBehavior());
        DispenserBlock.registerBehavior(MEDIUM_ROCKET_AP, new MediumRocketItem.MediumRocketDispenseBehavior(MEDIUM_ROCKET_AP));
        DispenserBlock.registerBehavior(MEDIUM_ROCKET_CM, new MediumRocketItem.MediumRocketDispenseBehavior(MEDIUM_ROCKET_CM));
        DispenserBlock.registerBehavior(MEDIUM_ROCKET_HE, new MediumRocketItem.MediumRocketDispenseBehavior(MEDIUM_ROCKET_HE));
        DispenserBlock.registerBehavior(MORTAR_SHELL, new MortarShell.MortarShellDispenseBehavior(MORTAR_SHELL));
        DispenserBlock.registerBehavior(POTION_MORTAR_SHELL, new MortarShell.MortarShellDispenseBehavior(POTION_MORTAR_SHELL));
    }

    private static Item register(String name, Item item) {
        return Registry.register(BuiltInRegistries.ITEM, Mod.loc(name), item);
    }
}
