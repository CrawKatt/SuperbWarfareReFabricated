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
import com.atsuishio.superbwarfare.item.curio.DogTagItem;
import com.atsuishio.superbwarfare.item.curio.IffItem;
import com.atsuishio.superbwarfare.item.curio.ParachuteItem;
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
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DispenserBlock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public class ModItems {
    /**
     * guns
     */
    public static final List<Supplier<Item>> GUNS_LIST = new ArrayList<>();

    public static final Supplier<Item> REPAIR_TOOL = gun("repair_tool", RepairToolItem::new);
    public static final Supplier<Item> TASER = gun("taser", TaserItem::new);
    public static final Supplier<Item> GLOCK_17 = gun("glock_17", Glock17Item::new);
    public static final Supplier<Item> GLOCK_18 = gun("glock_18", Glock18Item::new);
    public static final Supplier<Item> MP_443 = gun("mp_443", Mp443Item::new);
    public static final Supplier<Item> M_1911 = gun("m_1911", M1911Item::new);
    public static final Supplier<Item> HOMEMADE_SHOTGUN = gun("homemade_shotgun", HomemadeShotgunItem::new);
    public static final Supplier<Item> TRACHELIUM = gun("trachelium", TracheliumItem::new);
    public static final Supplier<Item> MP_5 = gun("mp_5", Mp5Item::new);
    public static final Supplier<Item> VECTOR = gun("vector", VectorItem::new);
    public static final Supplier<Item> AK_47 = gun("ak_47", AK47Item::new);
    public static final Supplier<Item> AK_12 = gun("ak_12", AK12Item::new);
    public static final Supplier<Item> SKS = gun("sks", SksItem::new);
    public static final Supplier<Item> M_4 = gun("m_4", M4Item::new);
    public static final Supplier<Item> HK_416 = gun("hk_416", Hk416Item::new);
    public static final Supplier<Item> QBZ_95 = gun("qbz_95", Qbz95Item::new);
    public static final Supplier<Item> QBZ_191 = gun("qbz_191", Qbz191Item::new);
    public static final Supplier<Item> INSIDIOUS = gun("insidious", InsidiousItem::new);
    public static final Supplier<Item> MK_14 = gun("mk_14", Mk14Item::new);
    public static final Supplier<Item> QL_1031 = gun("ql_1031", Ql1031Item::new);
    public static final Supplier<Item> MARLIN = gun("marlin", MarlinItem::new);
    public static final Supplier<Item> K_98 = gun("k_98", K98Item::new);
    public static final Supplier<Item> MOSIN_NAGANT = gun("mosin_nagant", MosinNagantItem::new);
    public static final Supplier<Item> SVD = gun("svd", SvdItem::new);
    public static final Supplier<Item> AWM = gun("awm", AwmItem::new);
    public static final Supplier<Item> M_98B = gun("m_98b", M98bItem::new);
    public static final Supplier<Item> SENTINEL = gun("sentinel", SentinelItem::new);
    public static final Supplier<Item> HUNTING_RIFLE = gun("hunting_rifle", HuntingRifleItem::new);
    public static final Supplier<Item> NTW_20 = gun("ntw_20", Ntw20Item::new);
    public static final Supplier<Item> M_870 = gun("m_870", M870Item::new);
    public static final Supplier<Item> AA_12 = gun("aa_12", Aa12Item::new);
    public static final Supplier<Item> DEVOTION = gun("devotion", DevotionItem::new);
    public static final Supplier<Item> RPK = gun("rpk", RpkItem::new);
    public static final Supplier<Item> M_60 = gun("m_60", M60Item::new);
    public static final Supplier<Item> M_2_HB = gun("m_2_hb", M2HBItem::new);
    public static final Supplier<Item> MINIGUN = gun("minigun", MinigunItem::new);
    public static final Supplier<Item> M_79 = gun("m_79", M79Item::new);
    public static final Supplier<Item> SECONDARY_CATACLYSM = gun("secondary_cataclysm", SecondaryCataclysmItem::new);
    public static final Supplier<Item> RPG = gun("rpg", RpgItem::new);
    public static final Supplier<Item> JAVELIN = gun("javelin", JavelinItem::new);
    public static final Supplier<Item> IGLA_9K38 = gun("igla_9k38", IglaItem::new);
    public static final Supplier<Item> AURELIA_SCEPTRE = gun("aurelia_sceptre", AureliaSceptreItem::new);
    public static final Supplier<Item> BOCEK = gun("bocek", BocekItem::new);
    public static final Supplier<Item> VEHICLE_GUN = gun("vehicle_gun", VehicleGun::new);

    /**
     * Ammo
     */
    public static final List<Supplier<Item>> AMMO_LIST = new ArrayList<>();

    public static final Supplier<Item> HANDGUN_AMMO = ammo("handgun_ammo", () -> new AmmoSupplierItem(Ammo.HANDGUN, 1, new Item.Properties()));
    public static final Supplier<Item> RIFLE_AMMO = ammo("rifle_ammo", () -> new AmmoSupplierItem(Ammo.RIFLE, 1, new Item.Properties()));
    public static final Supplier<Item> SNIPER_AMMO = ammo("sniper_ammo", () -> new AmmoSupplierItem(Ammo.SNIPER, 1, new Item.Properties()));
    public static final Supplier<Item> SHOTGUN_AMMO = ammo("shotgun_ammo", () -> new AmmoSupplierItem(Ammo.SHOTGUN, 1, new Item.Properties()));
    public static final Supplier<Item> HEAVY_AMMO = ammo("heavy_ammo", () -> new AmmoSupplierItem(Ammo.HEAVY, 1, new Item.Properties()));
    public static final Supplier<Item> HANDGUN_AMMO_BOX = ammo("handgun_ammo_box", HandgunAmmoBox::new);
    public static final Supplier<Item> RIFLE_AMMO_BOX = ammo("rifle_ammo_box", RifleAmmoBox::new);
    public static final Supplier<Item> SNIPER_AMMO_BOX = ammo("sniper_ammo_box", SniperAmmoBox::new);
    public static final Supplier<Item> SHOTGUN_AMMO_BOX = ammo("shotgun_ammo_box", ShotgunAmmoBox::new);
    public static final Supplier<Item> CREATIVE_AMMO_BOX = ammo("creative_ammo_box", CreativeAmmoBox::new);
    public static final Supplier<Item> AMMO_BOX = ammo("ammo_box", AmmoBoxItem::new);
    public static final Supplier<Item> TASER_ELECTRODE = ammo("taser_electrode", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> GRENADE_40MM = ammo("grenade_40mm", () -> new Item(new Item.Properties()));

    public static final Supplier<Item> MORTAR_SHELL = ammo("mortar_shell", MortarShell::new);
    public static final Supplier<Item> POTION_MORTAR_SHELL = ammo("potion_mortar_shell", PotionMortarShell::new);
    public static final Supplier<Item> RPG_ROCKET_STANDARD = ammo("rpg_rocket_standard", RpgRocketStandard::new);
    public static final Supplier<Item> RPG_ROCKET_TBG = ammo("rpg_rocket_tbg", RpgRocketTBG::new);
    public static final Supplier<Item> LUNGE_MINE = ammo("lunge_mine", LungeMine::new);
    public static final Supplier<Item> HE_5_INCHES = ammo("he_5_inches", () -> new CannonShellItem(new Item.Properties().rarity(Rarity.RARE)));
    public static final Supplier<Item> AP_5_INCHES = ammo("ap_5_inches", () -> new CannonShellItem(new Item.Properties().rarity(Rarity.RARE)));
    public static final Supplier<Item> CM_5_INCHES = ammo("cm_5_inches", () -> new CannonShellItem(new Item.Properties().rarity(Rarity.RARE)));
    public static final Supplier<Item> GS_5_INCHES = ammo("gs_5_inches", () -> new CannonShellItem(new Item.Properties().rarity(Rarity.RARE)));
    public static final Supplier<Item> HAND_GRENADE = ammo("hand_grenade", HandGrenade::new);
    public static final Supplier<Item> RGO_GRENADE = ammo("rgo_grenade", RgoGrenade::new);
    public static final Supplier<Item> M18_SMOKE_GRENADE = ammo("m18_smoke_grenade", M18SmokeGrenade::new);
    public static final Supplier<Item> CLAYMORE_MINE = ammo("claymore_mine", ClaymoreMine::new);
    public static final Supplier<Item> TM_62 = ammo("tm_62", Tm62Item::new);
    public static final Supplier<Item> PTKM_1R = ammo("ptkm_1r", Ptkm1rItem::new);
    public static final Supplier<Item> C4_BOMB = ammo("c4_bomb", C4BombItem::new);
    public static final Supplier<Item> BLU_43_MINE = ammo("blu_43_mine", Blu43MineItem::new);
    public static final Supplier<Item> SMALL_SHELL = ammo("small_shell", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> SMALL_ROCKET = ammo("small_rocket", () -> new Item(new Item.Properties().stacksTo(16)));
    public static final Supplier<Item> MEDIUM_ROCKET_AP = ammo("medium_rocket_ap", () -> new MediumRocketItem(500, 6, 100, 0, 0, MediumRocketEntity.Type.AP, 0));
    public static final Supplier<Item> MEDIUM_ROCKET_HE = ammo("medium_rocket_he", () -> new MediumRocketItem(200, 12, 200, 0.2f, 40, MediumRocketEntity.Type.HE, 0));
    public static final Supplier<Item> MEDIUM_ROCKET_CM = ammo("medium_rocket_cm", () -> new MediumRocketItem(300, 12, 300, 0, 0, MediumRocketEntity.Type.CM, 20));
    public static final Supplier<Item> JAVELIN_MISSILE = ammo("javelin_missile", () -> new Item(new Item.Properties().stacksTo(4)));
    public static final Supplier<Item> MEDIUM_ANTI_AIR_MISSILE = ammo("medium_anti_air_missile", () -> new Item(new Item.Properties().stacksTo(4)));
    public static final Supplier<Item> MEDIUM_ANTI_GROUND_MISSILE = ammo("medium_anti_ground_missile", () -> new Item(new Item.Properties().stacksTo(4)));
    public static final Supplier<Item> LARGE_ANTI_GROUND_MISSILE = ammo("large_anti_ground_missile", () -> new Item(new Item.Properties().stacksTo(2)));
    public static final Supplier<Item> SWARM_DRONE = ammo("swarm_drone", () -> new Item(new Item.Properties().stacksTo(14)));
    public static final Supplier<Item> MEDIUM_AERIAL_BOMB = ammo("medium_aerial_bomb", () -> new Item(new Item.Properties().stacksTo(2)));

    /**
     * items
     */
    public static final List<Supplier<Item>> ITEMS_LIST = new ArrayList<>();

    public static final Supplier<Item> SENPAI_SPAWN_EGG = item("senpai_spawn_egg", () -> new SpawnEggItem(ModEntities.SENPAI.get(), -11584987, -14014413, new Item.Properties()));
    public static final Supplier<Item> ANCIENT_CPU = item("ancient_cpu", () -> new Item(new Item.Properties().rarity(Rarity.RARE)));
    public static final Supplier<Item> PROPELLER = item("propeller", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> LARGE_PROPELLER = item("large_propeller", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> MOTOR = item("motor", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> LARGE_MOTOR = item("large_motor", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> WHEEL = item("wheel", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> TRACK = item("track", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> DRONE = item("drone", Drone::new);

    public static final Supplier<Item> MONITOR = item("monitor", Monitor::new);
    public static final Supplier<Item> ARTILLERY_INDICATOR = item("artillery_indicator", ArtilleryIndicator::new);

    public static final Supplier<Item> DETONATOR = item("detonator", Detonator::new);
    public static final Supplier<Item> TARGET_DEPLOYER = item("target_deployer", TargetDeployer::new);
    public static final Supplier<Item> DPS_GENERATOR_DEPLOYER = item("dps_generator_deployer", DPSGeneratorDeployer::new);
    public static final Supplier<Item> KNIFE = item("knife", () -> new SwordItem(ModItemTier.STEEL, 0, -1.8f, new Item.Properties().durability(1200)));
    public static final Supplier<Item> HAMMER = item("hammer", () -> new Hammer(Tiers.IRON, 11, -3.2f, new Item.Properties().durability(400)));
    public static final Supplier<Item> GOLDEN_HAMMER = item("golden_hammer", () -> new Hammer(Tiers.GOLD, 11, -3.2f, new Item.Properties().durability(150)));
    public static final Supplier<Item> STEEL_HAMMER = item("steel_hammer", () -> new Hammer(ModItemTier.STEEL, 9, -3.2f, new Item.Properties().durability(600)));
    public static final Supplier<Item> DIAMOND_HAMMER = item("diamond_hammer", () -> new Hammer(Tiers.DIAMOND, 12, -3.2f, new Item.Properties().durability(1500)));
    public static final Supplier<Item> CEMENTED_CARBIDE_HAMMER = item("cemented_carbide_hammer", () -> new Hammer(ModItemTier.CEMENTED_CARBIDE, 8, -3.2f, new Item.Properties().durability(2000)));
    public static final Supplier<Item> NETHERITE_HAMMER = item("netherite_hammer", NetheriteHammer::new);
    public static final Supplier<Item> T_BATON = item("t_baton", TBaton::new);
    public static final Supplier<Item> ELECTRIC_BATON = item("electric_baton", ElectricBaton::new);
    public static final Supplier<Item> STEEL_PIPE = item("steel_pipe", SteelPipe::new);
    public static final Supplier<Item> CROWBAR = item("crowbar", Crowbar::new);
    public static final Supplier<Item> DEFUSER = item("defuser", Defuser::new);
    public static final Supplier<Item> ARMOR_PLATE = item("armor_plate", ArmorPlate::new);

    public static final Supplier<Item> RU_HELMET_6B47 = item("ru_helmet_6b47", RuHelmet6b47::new);
    public static final Supplier<Item> RU_CHEST_6B43 = item("ru_chest_6b43", RuChest6b43::new);
    public static final Supplier<Item> US_HELMET_PASGT = item("us_helmet_pasgt", UsHelmetPasgt::new);
    public static final Supplier<Item> US_CHEST_IOTV = item("us_chest_iotv", UsChestIotv::new);
    public static final Supplier<Item> GE_HELMET_M_35 = item("ge_helmet_m_35", GeHelmetM35::new);
    public static final Supplier<Item> PARACHUTE = item("parachute", ParachuteItem::new);

    public static final Supplier<Item> MORTAR_DEPLOYER = item("mortar_deployer", MortarDeployer::new);
    public static final Supplier<Item> MORTAR_BARREL = item("mortar_barrel", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> MORTAR_BASE_PLATE = item("mortar_base_plate", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> MORTAR_BIPOD = item("mortar_bipod", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> TOW_DEPLOYER = item("tow_deployer", TowDeployer::new);
    public static final Supplier<Item> SEEKER = item("seeker", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> MISSILE_ENGINE = item("missile_engine", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> FUSEE = item("fusee", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> PRIMER = item("primer", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> AP_HEAD = item("ap_head", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> HE_HEAD = item("he_head", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> CM_HEAD = item("cm_head", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> GS_HEAD = item("gs_head", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> CANNON_CORE = item("cannon_core", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> COPPER_PLATE = item("copper_plate", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> STEEL_INGOT = item("steel_ingot", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> LEAD_INGOT = item("lead_ingot", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> SILVER_INGOT = item("silver_ingot", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> TUNGSTEN_INGOT = item("tungsten_ingot", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> CEMENTED_CARBIDE_INGOT = item("cemented_carbide_ingot", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> HIGH_ENERGY_EXPLOSIVES = item("high_energy_explosives", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> GRAIN = item("grain", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> IRON_POWDER = item("iron_powder", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> TUNGSTEN_POWDER = item("tungsten_powder", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> COAL_POWDER = item("coal_powder", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> COAL_IRON_POWDER = item("coal_iron_powder", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> RAW_CEMENTED_CARBIDE_POWDER = item("raw_cemented_carbide_powder", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> GALENA = item("galena", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> SCHEELITE = item("scheelite", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> RAW_SILVER = item("raw_silver", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> DOG_TAG = item("dog_tag", DogTagItem::new);
    public static final Supplier<Item> IFF = item("iff", IffItem::new);
    public static final Supplier<Item> CELL = item("cell", () -> new BatteryItem(24000, new Item.Properties()));
    public static final Supplier<Item> BATTERY = item("battery", () -> new BatteryItem(100000, new Item.Properties()));
    public static final Supplier<Item> SMALL_BATTERY_PACK = item("small_battery_pack", () -> new BatteryItem(500000, new Item.Properties()));
    public static final Supplier<Item> MEDIUM_BATTERY_PACK = item("medium_battery_pack", () -> new BatteryItem(5000000, new Item.Properties()));
    public static final Supplier<Item> LARGE_BATTERY_PACK = item("large_battery_pack", () -> new BatteryItem(20000000, new Item.Properties()));
    public static final Supplier<Item> LASER_UNIT = item("laser_unit", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> BEAST = item("beast", Beast::new);
    public static final Supplier<Item> TRANSCRIPT = item("transcript", Transcript::new);
    public static final Supplier<Item> FIRING_PARAMETERS = item("firing_parameters", FiringParameters::new);
    public static final Supplier<Item> MEDICAL_KIT = item("medical_kit", MedicalKitItem::new);
    public static final Supplier<Item> VEHICLE_DAMAGE_ANALYZER = item("vehicle_damage_analyzer", VehicleDamageAnalyzer::new);
    public static final Supplier<Item> VEHICLE_RESET_KIT = item("vehicle_reset_kit", VehicleResetKit::new);

    public static final Supplier<Item> TUNGSTEN_ROD = item("tungsten_rod", () -> new Item(new Item.Properties()));

    public static final Materials IRON_MATERIALS = registerMaterials("iron");
    public static final Materials STEEL_MATERIALS = registerMaterials("steel");
    public static final Materials CEMENTED_CARBIDE_MATERIALS = registerMaterials("cemented_carbide");
    public static final Materials NETHERITE_MATERIALS = registerMaterials("netherite");

    public static final Supplier<Item> COMMON_MATERIAL_PACK = item("common_material_pack", () -> new MaterialPack(Rarity.COMMON));
    public static final Supplier<Item> RARE_MATERIAL_PACK = item("rare_material_pack", () -> new MaterialPack(Rarity.RARE));
    public static final Supplier<Item> EPIC_MATERIAL_PACK = item("epic_material_pack", () -> new MaterialPack(Rarity.EPIC));
    public static final Supplier<Item> LEGENDARY_MATERIAL_PACK = item("legendary_material_pack", () -> new MaterialPack(ModRarities.LEGENDARY));

    public static final Supplier<Item> LIGHT_ARMAMENT_MODULE = item("light_armament_module", () -> new Item(new Item.Properties().rarity(Rarity.RARE)));
    public static final Supplier<Item> MEDIUM_ARMAMENT_MODULE = item("medium_armament_module", () -> new Item(new Item.Properties().rarity(Rarity.EPIC)));
    public static final Supplier<Item> HEAVY_ARMAMENT_MODULE = item("heavy_armament_module", () -> new Item(new Item.Properties().rarity(ModRarities.LEGENDARY)));

    public static final Supplier<Item> TRACHELIUM_BLUEPRINT = item("trachelium_blueprint", () -> new BlueprintItem(Rarity.EPIC));
    public static final Supplier<Item> GLOCK_17_BLUEPRINT = item("glock_17_blueprint", () -> new BlueprintItem(Rarity.COMMON));
    public static final Supplier<Item> MP_443_BLUEPRINT = item("mp_443_blueprint", () -> new BlueprintItem(Rarity.COMMON));
    public static final Supplier<Item> GLOCK_18_BLUEPRINT = item("glock_18_blueprint", () -> new BlueprintItem(Rarity.RARE));
    public static final Supplier<Item> HUNTING_RIFLE_BLUEPRINT = item("hunting_rifle_blueprint", () -> new BlueprintItem(Rarity.RARE));
    public static final Supplier<Item> M_79_BLUEPRINT = item("m_79_blueprint", () -> new BlueprintItem(Rarity.RARE));
    public static final Supplier<Item> RPG_BLUEPRINT = item("rpg_blueprint", () -> new BlueprintItem(Rarity.RARE));
    public static final Supplier<Item> BOCEK_BLUEPRINT = item("bocek_blueprint", () -> new BlueprintItem(Rarity.EPIC));
    public static final Supplier<Item> M_4_BLUEPRINT = item("m_4_blueprint", () -> new BlueprintItem(Rarity.RARE));
    public static final Supplier<Item> AA_12_BLUEPRINT = item("aa_12_blueprint", () -> new BlueprintItem(ModRarities.LEGENDARY));
    public static final Supplier<Item> HK_416_BLUEPRINT = item("hk_416_blueprint", () -> new BlueprintItem(Rarity.RARE));
    public static final Supplier<Item> RPK_BLUEPRINT = item("rpk_blueprint", () -> new BlueprintItem(Rarity.EPIC));
    public static final Supplier<Item> SKS_BLUEPRINT = item("sks_blueprint", () -> new BlueprintItem(Rarity.RARE));
    public static final Supplier<Item> NTW_20_BLUEPRINT = item("ntw_20_blueprint", () -> new BlueprintItem(ModRarities.LEGENDARY));
    public static final Supplier<Item> MP_5_BLUEPRINT = item("mp_5_blueprint", () -> new BlueprintItem(Rarity.RARE));
    public static final Supplier<Item> VECTOR_BLUEPRINT = item("vector_blueprint", () -> new BlueprintItem(Rarity.EPIC));
    public static final Supplier<Item> MINIGUN_BLUEPRINT = item("minigun_blueprint", () -> new BlueprintItem(ModRarities.LEGENDARY));
    public static final Supplier<Item> MK_14_BLUEPRINT = item("mk_14_blueprint", () -> new BlueprintItem(Rarity.EPIC));
    public static final Supplier<Item> SENTINEL_BLUEPRINT = item("sentinel_blueprint", () -> new BlueprintItem(Rarity.EPIC));
    public static final Supplier<Item> M_60_BLUEPRINT = item("m_60_blueprint", () -> new BlueprintItem(Rarity.EPIC));
    public static final Supplier<Item> SVD_BLUEPRINT = item("svd_blueprint", () -> new BlueprintItem(Rarity.EPIC));
    public static final Supplier<Item> MARLIN_BLUEPRINT = item("marlin_blueprint", () -> new BlueprintItem(Rarity.COMMON));
    public static final Supplier<Item> M_870_BLUEPRINT = item("m_870_blueprint", () -> new BlueprintItem(Rarity.RARE));
    public static final Supplier<Item> AWM_BLUEPRINT = item("awm_blueprint", () -> new BlueprintItem(Rarity.EPIC));
    public static final Supplier<Item> M_98B_BLUEPRINT = item("m_98b_blueprint", () -> new BlueprintItem(Rarity.EPIC));
    public static final Supplier<Item> AK_47_BLUEPRINT = item("ak_47_blueprint", () -> new BlueprintItem(Rarity.RARE));
    public static final Supplier<Item> AK_12_BLUEPRINT = item("ak_12_blueprint", () -> new BlueprintItem(Rarity.RARE));
    public static final Supplier<Item> DEVOTION_BLUEPRINT = item("devotion_blueprint", () -> new BlueprintItem(Rarity.EPIC));
    public static final Supplier<Item> TASER_BLUEPRINT = item("taser_blueprint", () -> new BlueprintItem(Rarity.COMMON));
    public static final Supplier<Item> M_1911_BLUEPRINT = item("m_1911_blueprint", () -> new BlueprintItem(Rarity.COMMON));
    public static final Supplier<Item> QBZ_95_BLUEPRINT = item("qbz_95_blueprint", () -> new BlueprintItem(Rarity.RARE));
    public static final Supplier<Item> QBZ_191_BLUEPRINT = item("qbz_191_blueprint", () -> new BlueprintItem(Rarity.EPIC));
    public static final Supplier<Item> K_98_BLUEPRINT = item("k_98_blueprint", () -> new BlueprintItem(Rarity.RARE));
    public static final Supplier<Item> MOSIN_NAGANT_BLUEPRINT = item("mosin_nagant_blueprint", () -> new BlueprintItem(Rarity.RARE));
    public static final Supplier<Item> IGLA_BLUEPRINT = item("igla_9k38_blueprint", () -> new BlueprintItem(Rarity.EPIC));
    public static final Supplier<Item> JAVELIN_BLUEPRINT = item("javelin_blueprint", () -> new BlueprintItem(ModRarities.LEGENDARY));
    public static final Supplier<Item> M_2_HB_BLUEPRINT = item("m_2_hb_blueprint", () -> new BlueprintItem(Rarity.RARE));
    public static final Supplier<Item> SECONDARY_CATACLYSM_BLUEPRINT = item("secondary_cataclysm_blueprint", () -> new BlueprintItem(ModRarities.LEGENDARY));
    public static final Supplier<Item> INSIDIOUS_BLUEPRINT = item("insidious_blueprint", () -> new BlueprintItem(Rarity.EPIC));
    public static final Supplier<Item> AURELIA_SCEPTRE_BLUEPRINT = item("aurelia_sceptre_blueprint", () -> new BlueprintItem(ModRarities.LEGENDARY));
    public static final Supplier<Item> QL_1031_BLUEPRINT = item("ql_1031_blueprint", () -> new BlueprintItem(ModRarities.LEGENDARY));
    public static final Supplier<Item> MK_42_BLUEPRINT = item("mk_42_blueprint", () -> new BlueprintItem(ModRarities.LEGENDARY));
    public static final Supplier<Item> MLE_1934_BLUEPRINT = item("mle_1934_blueprint", () -> new BlueprintItem(ModRarities.LEGENDARY));
    public static final Supplier<Item> BL_132_BLUEPRINT = item("bl_132_blueprint", () -> new BlueprintItem(ModRarities.LEGENDARY));
    public static final Supplier<Item> HPJ_11_BLUEPRINT = item("hpj_11_blueprint", () -> new BlueprintItem(ModRarities.LEGENDARY));
    public static final Supplier<Item> ANNIHILATOR_BLUEPRINT = item("annihilator_blueprint", () -> new BlueprintItem(ModRarities.LEGENDARY));

    /**
     * Block
     */
    public static final List<Supplier<Item>> BLOCKS_LIST = new ArrayList<>();

    public static final Supplier<Item> GALENA_ORE = blockItem("galena_ore", ModBlocks.GALENA_ORE);
    public static final Supplier<Item> DEEPSLATE_GALENA_ORE = blockItem("deepslate_galena_ore", ModBlocks.DEEPSLATE_GALENA_ORE);
    public static final Supplier<Item> SCHEELITE_ORE = blockItem("scheelite_ore", ModBlocks.SCHEELITE_ORE);
    public static final Supplier<Item> DEEPSLATE_SCHEELITE_ORE = blockItem("deepslate_scheelite_ore", ModBlocks.DEEPSLATE_SCHEELITE_ORE);
    public static final Supplier<Item> SILVER_ORE = blockItem("silver_ore", ModBlocks.SILVER_ORE);
    public static final Supplier<Item> DEEPSLATE_SILVER_ORE = blockItem("deepslate_silver_ore", ModBlocks.DEEPSLATE_SILVER_ORE);
    public static final Supplier<Item> JUMP_PAD = blockItem("jump_pad", ModBlocks.JUMP_PAD);
    public static final Supplier<Item> SANDBAG = blockItem("sandbag", ModBlocks.SANDBAG);
    public static final Supplier<Item> BARBED_WIRE = blockItem("barbed_wire", ModBlocks.BARBED_WIRE);
    public static final Supplier<Item> DRAGON_TEETH = blockItem("dragon_teeth", ModBlocks.DRAGON_TEETH);
    public static final Supplier<Item> REFORGING_TABLE = blockItem("reforging_table", ModBlocks.REFORGING_TABLE);
    public static final Supplier<Item> CHARGING_STATION = registerBlock("charging_station", ChargingStationBlockItem::new);
    public static final Supplier<Item> CREATIVE_CHARGING_STATION = registerBlock("creative_charging_station", CreativeChargingStationBlockItem::new);
    public static final Supplier<Item> LEAD_BLOCK = blockItem("lead_block", ModBlocks.LEAD_BLOCK);
    public static final Supplier<Item> STEEL_BLOCK = blockItem("steel_block", ModBlocks.STEEL_BLOCK);
    public static final Supplier<Item> TUNGSTEN_BLOCK = blockItem("tungsten_block", ModBlocks.TUNGSTEN_BLOCK);
    public static final Supplier<Item> SILVER_BLOCK = blockItem("silver_block", ModBlocks.SILVER_BLOCK);
    public static final Supplier<Item> CEMENTED_CARBIDE_BLOCK = blockItem("cemented_carbide_block", ModBlocks.CEMENTED_CARBIDE_BLOCK);
    public static final Supplier<Item> FUMO_25 = blockItem("fumo_25", ModBlocks.FUMO_25);
    public static final Supplier<Item> VEHICLE_DEPLOYER = registerBlock("vehicle_deployer", VehicleDeployerBlockItem::new);
    public static final Supplier<Item> AIRCRAFT_CATAPULT = blockItem("aircraft_catapult", ModBlocks.AIRCRAFT_CATAPULT);
    public static final Supplier<Item> SUPERB_ITEM_INTERFACE = blockItem("superb_item_interface", ModBlocks.SUPERB_ITEM_INTERFACE);
    public static final Supplier<Item> CREATIVE_SUPERB_ITEM_INTERFACE = blockItemRarity("creative_superb_item_interface", ModBlocks.CREATIVE_SUPERB_ITEM_INTERFACE, Rarity.EPIC);
    public static final Supplier<Item> VEHICLE_ASSEMBLING_TABLE = registerBlock("vehicle_assembling_table", VehicleAssemblingTableBlockItem::new);

    /**
     * Vehicle
     */
    public static final List<Supplier<Item>> VEHICLES_LIST = new ArrayList<>();

    public static final Supplier<Item> CONTAINER = vehicle("container", ContainerBlockItem::new);
    public static final Supplier<Item> SMALL_CONTAINER = vehicle("small_container", SmallContainerBlockItem::new);
    public static final Supplier<Item> LUCKY_CONTAINER = vehicle("lucky_container", LuckyContainerBlockItem::new);

    public record Materials(
            String name,
            Supplier<Item> barrel,
            Supplier<Item> action,
            Supplier<Item> spring,
            Supplier<Item> trigger
    ) {
    }

    public static Materials registerMaterials(String name) {
        return new Materials(
                name,
                item(name + "_barrel", () -> new Item(new Item.Properties())),
                item(name + "_action", () -> new Item(new Item.Properties())),
                item(name + "_spring", () -> new Item(new Item.Properties())),
                item(name + "_trigger", () -> new Item(new Item.Properties()))
        );
    }

    /**
     * Perk Items
     */
    public static final List<Supplier<Item>> PERKS_LIST = new ArrayList<>();

    public static final Map<Supplier<Perk>, Supplier<Item>> PERK_ITEMS = new HashMap<>();

    /**
     * 单独注册，用于Tab图标，不要删
     */
    public static Supplier<Item> AP_BULLET;
    public static Supplier<Item> INTELLIGENT_CHIP;

    public static void registerPerkItems() {
        ModPerks.AMMO_PERKS.forEach(entry -> registerSinglePerkItem(entry.name(), entry));
        ModPerks.FUNC_PERKS.forEach(entry -> registerSinglePerkItem(entry.name(), entry));
        ModPerks.DAMAGE_PERKS.forEach(entry -> registerSinglePerkItem(entry.name(), entry));

        AP_BULLET = PERK_ITEMS.get(ModPerks.AP_BULLET);
        INTELLIGENT_CHIP = PERK_ITEMS.get(ModPerks.INTELLIGENT_CHIP);
    }

    private static void registerSinglePerkItem(String name, Supplier<Perk> perk) {
        Supplier<Item> item = registerPerk(name, () -> new PerkItem(perk));
        PERK_ITEMS.put(perk, item);
    }

    public static final Supplier<Item> SHORTCUT_PACK = registerPerk("shortcut_pack", ShortcutPack::new);
    public static final Supplier<Item> EMPTY_PERK = registerPerk("empty_perk", () -> new Item(new Item.Properties()));

    // Registration helpers

    private static Supplier<Item> gun(String name, Supplier<Item> item) {
        Supplier<Item> reg = Registration.item(name, item);
        GUNS_LIST.add(reg);
        return reg;
    }

    private static Supplier<Item> ammo(String name, Supplier<Item> item) {
        Supplier<Item> reg = Registration.item(name, item);
        AMMO_LIST.add(reg);
        return reg;
    }

    private static Supplier<Item> item(String name, Supplier<Item> item) {
        Supplier<Item> reg = Registration.item(name, item);
        ITEMS_LIST.add(reg);
        return reg;
    }

    private static Supplier<Item> blockItem(String name, Supplier<Block> block) {
        Supplier<Item> reg = Registration.item(name, () -> new BlockItem(block.get(), new Item.Properties()));
        BLOCKS_LIST.add(reg);
        return reg;
    }

    private static Supplier<Item> blockItemRarity(String name, Supplier<Block> block, Rarity rarity) {
        Supplier<Item> reg = Registration.item(name, () -> new BlockItem(block.get(), new Item.Properties().rarity(rarity)));
        BLOCKS_LIST.add(reg);
        return reg;
    }

    private static Supplier<Item> registerBlock(String name, Supplier<Item> item) {
        Supplier<Item> reg = Registration.item(name, item);
        BLOCKS_LIST.add(reg);
        return reg;
    }

    private static Supplier<Item> vehicle(String name, Supplier<Item> item) {
        Supplier<Item> reg = Registration.item(name, item);
        VEHICLES_LIST.add(reg);
        return reg;
    }

    private static Supplier<Item> registerPerk(String name, Supplier<Item> item) {
        Supplier<Item> reg = Registration.item(name, item);
        PERKS_LIST.add(reg);
        return reg;
    }

    public static void registerDispenserBehavior() {
        List<Supplier<Item>> list = new ArrayList<>();
        list.addAll(AMMO_LIST);
        list.addAll(ITEMS_LIST);

        for (var item : list) {
            if (item.get() instanceof DispenserLaunchable launchable) {
                DispenserBlock.registerBehavior(item.get(), launchable.getLaunchBehavior());
            }
        }
    }

    public static void register() {

    }
}
