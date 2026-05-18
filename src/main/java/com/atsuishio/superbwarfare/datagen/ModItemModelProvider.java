package com.atsuishio.superbwarfare.datagen;

import com.atsuishio.superbwarfare.Mod;
import com.atsuishio.superbwarfare.datagen.builder.CustomSeparateModelBuilder;
import com.atsuishio.superbwarfare.init.ModBlocks;
import com.atsuishio.superbwarfare.init.ModItems;
import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ModItemModelProvider implements DataProvider {

    private final PackOutput output;
    private final Map<ResourceLocation, JsonObject> models = new HashMap<>();

    public ModItemModelProvider(PackOutput output) {
        this.output = output;
    }

    @Override
    public @NotNull CompletableFuture<?> run(@NotNull CachedOutput cache) {
        generate();
        PackOutput.PathProvider modelPath = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "models/item");
        List<CompletableFuture<?>> futures = new ArrayList<>();
        for (var entry : models.entrySet()) {
            futures.add(DataProvider.saveStable(cache, entry.getValue(), modelPath.json(entry.getKey())));
        }
        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }

    private void generate() {
        gunItem(ModItems.AA_12, "aa_12");
        gunItem(ModItems.AK_12, "ak_12");
        gunItem(ModItems.AK_47, "ak_47");
        gunItem(ModItems.AURELIA_SCEPTRE, "aurelia_sceptre");
        gunItem(ModItems.BOCEK, "bocek");
        gunItem(ModItems.DEVOTION, "devotion");
        gunItem(ModItems.GLOCK_17, "glock_17");
        gunItem(ModItems.GLOCK_18, "glock_17");
        gunItem(ModItems.HK_416, "hk_416");
        gunItem(ModItems.HOMEMADE_SHOTGUN, "homemade_shotgun");
        gunItem(ModItems.HUNTING_RIFLE, "hunting_rifle");
        gunItem(ModItems.INSIDIOUS, "insidious");
        gunItem(ModItems.JAVELIN, "javelin");
        gunItem(ModItems.K_98, "k_98");
        gunItem(ModItems.M_4, "m_4");
        gunItem(ModItems.M_60, "m_60");
        gunItem(ModItems.M_79, "m_79");
        gunItem(ModItems.M_1911, "m_1911");
        gunItem(ModItems.M_870, "m_870");
        gunItem(ModItems.M_98B, "m_98b");
        gunItem(ModItems.MARLIN, "marlin");
        gunItem(ModItems.MINIGUN, "minigun");
        gunItem(ModItems.MK_14, "mk_14");
        gunItem(ModItems.MOSIN_NAGANT, "mosin_nagant");
        gunItem(ModItems.MP_443, "mp_443");
        gunItem(ModItems.NTW_20, "ntw_20");
        gunItem(ModItems.QBZ_95, "qbz_95");
        gunItem(ModItems.RPG, "rpg");
        gunItem(ModItems.RPK, "rpk");
        gunItem(ModItems.SECONDARY_CATACLYSM, "secondary_cataclysm");
        gunItem(ModItems.SENTINEL, "sentinel");
        gunItem(ModItems.SKS, "sks");
        gunItem(ModItems.SVD, "svd");
        gunItem(ModItems.TASER, "taser");
        gunItem(ModItems.TRACHELIUM, "trachelium");
        gunItem(ModItems.VECTOR, "vector");
        gunItem(ModItems.MP_5, "mp_5");
        gunItem(ModItems.M_2_HB, "m_2_hb");
        gunItem(ModItems.QBZ_191, "qbz_191");
        gunItem(ModItems.AWM, "awm");
        gunItem(ModItems.IGLA_9K38, "igla_9k38");
        gunItem(ModItems.REPAIR_TOOL, "repair_tool");
        gunItem(ModItems.QL_1031, "ql_1031");

        simpleItem(ModItems.VEHICLE_GUN);
        simpleItem(ModItems.EMPTY_PERK, "perk/");
        simpleItem(ModItems.MORTAR_SHELL);

        simpleItem(ModItems.ANCIENT_CPU);
        simpleItem(ModItems.PROPELLER);
        simpleItem(ModItems.LARGE_PROPELLER);
        simpleItem(ModItems.MOTOR);
        simpleItem(ModItems.LARGE_MOTOR);
        simpleItem(ModItems.WHEEL);
        simpleItem(ModItems.TRACK);
        simpleItem(ModItems.DRONE);
        simpleItem(ModItems.LIGHT_ARMAMENT_MODULE);
        simpleItem(ModItems.MEDIUM_ARMAMENT_MODULE);
        simpleItem(ModItems.HEAVY_ARMAMENT_MODULE);

        simpleItem(ModItems.TARGET_DEPLOYER);
        simpleItem(ModItems.DPS_GENERATOR_DEPLOYER);
        simpleItem(ModItems.MORTAR_DEPLOYER);
        simpleItem(ModItems.MORTAR_BARREL);
        simpleItem(ModItems.MORTAR_BASE_PLATE);
        simpleItem(ModItems.MORTAR_BIPOD);
        simpleItem(ModItems.SEEKER);
        simpleItem(ModItems.MISSILE_ENGINE);
        simpleItem(ModItems.FUSEE);
        simpleItem(ModItems.PRIMER);
        simpleItem(ModItems.BLU_43_MINE);
        simpleItem(ModItems.AP_HEAD);
        simpleItem(ModItems.HE_HEAD);
        simpleItem(ModItems.CM_HEAD);
        simpleItem(ModItems.GS_HEAD);
        simpleItem(ModItems.CANNON_CORE);
        simpleItem(ModItems.COPPER_PLATE);
        simpleItem(ModItems.STEEL_INGOT);
        simpleItem(ModItems.LEAD_INGOT);
        simpleItem(ModItems.TUNGSTEN_INGOT);
        simpleItem(ModItems.CEMENTED_CARBIDE_INGOT);
        simpleItem(ModItems.HIGH_ENERGY_EXPLOSIVES);
        simpleItem(ModItems.GRAIN);
        simpleItem(ModItems.IRON_POWDER);
        simpleItem(ModItems.TUNGSTEN_POWDER);
        simpleItem(ModItems.COAL_POWDER);
        simpleItem(ModItems.COAL_IRON_POWDER);
        simpleItem(ModItems.RAW_CEMENTED_CARBIDE_POWDER);
        simpleItem(ModItems.GALENA);
        simpleItem(ModItems.SCHEELITE);
        simpleItem(ModItems.DOG_TAG);
        simpleItem(ModItems.IFF);
        simpleItem(ModItems.TRANSCRIPT);
        simpleItem(ModItems.RAW_SILVER);
        simpleItem(ModItems.SILVER_INGOT);
        handheldItem(ModItems.BEAST);
        handheldItem(ModItems.CROWBAR);
        handheldItem(ModItems.DEFUSER);
        simpleItem(ModItems.FIRING_PARAMETERS);
        simpleItem(ModItems.HANDGUN_AMMO);
        simpleItem(ModItems.RIFLE_AMMO);
        simpleItem(ModItems.SNIPER_AMMO);
        simpleItem(ModItems.SHOTGUN_AMMO);
        simpleItem(ModItems.HEAVY_AMMO);
        simpleItem(ModItems.SMALL_ROCKET);
        simpleItem(ModItems.MEDIUM_ROCKET_AP);
        simpleItem(ModItems.MEDIUM_ROCKET_HE);
        simpleItem(ModItems.MEDIUM_ROCKET_CM);
        simpleItem(ModItems.MEDIUM_ANTI_GROUND_MISSILE);
        simpleItem(ModItems.LARGE_ANTI_GROUND_MISSILE);
        simpleItem(ModItems.SMALL_SHELL);
        simpleItem(ModItems.SWARM_DRONE);
        simpleItem(ModItems.MEDIUM_AERIAL_BOMB);
        simpleItem(ModItems.SMALL_BATTERY_PACK);
        simpleItem(ModItems.MEDIUM_BATTERY_PACK);
        simpleItem(ModItems.LARGE_BATTERY_PACK);
        simpleItem(ModItems.MEDICAL_KIT);
        simpleItem(ModItems.PARACHUTE);
        simpleItem(ModItems.VEHICLE_DAMAGE_ANALYZER);
        simpleItem(ModItems.MEDIUM_ANTI_AIR_MISSILE);
        simpleItem(ModItems.LASER_UNIT);
        simpleItem(ModItems.TOW_DEPLOYER);
        simpleItem(ModItems.VEHICLE_RESET_KIT);

        simpleItem(ModItems.TUNGSTEN_ROD);

        simpleMaterials(ModItems.IRON_MATERIALS);
        simpleMaterials(ModItems.STEEL_MATERIALS);
        simpleMaterials(ModItems.CEMENTED_CARBIDE_MATERIALS);
        simpleMaterials(ModItems.NETHERITE_MATERIALS);

        simpleItem(ModItems.COMMON_MATERIAL_PACK);
        simpleItem(ModItems.RARE_MATERIAL_PACK);
        simpleItem(ModItems.EPIC_MATERIAL_PACK);
        simpleItem(ModItems.LEGENDARY_MATERIAL_PACK);

        simpleItem(ModItems.RU_HELMET_6B47);
        simpleItem(ModItems.RU_CHEST_6B43);
        simpleItem(ModItems.US_HELMET_PASGT);
        simpleItem(ModItems.US_CHEST_IOTV);
        simpleItem(ModItems.GE_HELMET_M_35);

        gunBlueprintItem(ModItems.TRACHELIUM_BLUEPRINT);
        gunBlueprintItem(ModItems.GLOCK_17_BLUEPRINT);
        gunBlueprintItem(ModItems.GLOCK_18_BLUEPRINT);
        gunBlueprintItem(ModItems.MP_443_BLUEPRINT);
        gunBlueprintItem(ModItems.HUNTING_RIFLE_BLUEPRINT);
        gunBlueprintItem(ModItems.M_79_BLUEPRINT);
        gunBlueprintItem(ModItems.RPG_BLUEPRINT);
        gunBlueprintItem(ModItems.BOCEK_BLUEPRINT);
        gunBlueprintItem(ModItems.M_4_BLUEPRINT);
        gunBlueprintItem(ModItems.AA_12_BLUEPRINT);
        gunBlueprintItem(ModItems.HK_416_BLUEPRINT);
        gunBlueprintItem(ModItems.RPK_BLUEPRINT);
        gunBlueprintItem(ModItems.SKS_BLUEPRINT);
        gunBlueprintItem(ModItems.NTW_20_BLUEPRINT);
        gunBlueprintItem(ModItems.VECTOR_BLUEPRINT);
        gunBlueprintItem(ModItems.MINIGUN_BLUEPRINT);
        gunBlueprintItem(ModItems.MK_14_BLUEPRINT);
        gunBlueprintItem(ModItems.SENTINEL_BLUEPRINT);
        gunBlueprintItem(ModItems.M_60_BLUEPRINT);
        gunBlueprintItem(ModItems.SVD_BLUEPRINT);
        gunBlueprintItem(ModItems.MARLIN_BLUEPRINT);
        gunBlueprintItem(ModItems.M_870_BLUEPRINT);
        gunBlueprintItem(ModItems.AWM_BLUEPRINT);
        gunBlueprintItem(ModItems.M_98B_BLUEPRINT);
        gunBlueprintItem(ModItems.AK_12_BLUEPRINT);
        gunBlueprintItem(ModItems.AK_47_BLUEPRINT);
        gunBlueprintItem(ModItems.DEVOTION_BLUEPRINT);
        gunBlueprintItem(ModItems.TASER_BLUEPRINT);
        gunBlueprintItem(ModItems.M_1911_BLUEPRINT);
        gunBlueprintItem(ModItems.QBZ_95_BLUEPRINT);
        gunBlueprintItem(ModItems.K_98_BLUEPRINT);
        gunBlueprintItem(ModItems.MOSIN_NAGANT_BLUEPRINT);
        gunBlueprintItem(ModItems.JAVELIN_BLUEPRINT);
        gunBlueprintItem(ModItems.AURELIA_SCEPTRE_BLUEPRINT);
        cannonBlueprintItem(ModItems.MK_42_BLUEPRINT);
        cannonBlueprintItem(ModItems.MLE_1934_BLUEPRINT);
        cannonBlueprintItem(ModItems.ANNIHILATOR_BLUEPRINT);
        cannonBlueprintItem(ModItems.HPJ_11_BLUEPRINT);
        cannonBlueprintItem(ModItems.BL_132_BLUEPRINT);
        gunBlueprintItem(ModItems.M_2_HB_BLUEPRINT);
        gunBlueprintItem(ModItems.SECONDARY_CATACLYSM_BLUEPRINT);
        gunBlueprintItem(ModItems.INSIDIOUS_BLUEPRINT);
        gunBlueprintItem(ModItems.MP_5_BLUEPRINT);
        gunBlueprintItem(ModItems.QBZ_191_BLUEPRINT);
        gunBlueprintItem(ModItems.IGLA_BLUEPRINT);
        gunBlueprintItem(ModItems.QL_1031_BLUEPRINT);

        evenSimplerBlockItem(ModBlocks.BARBED_WIRE);
        evenSimplerBlockItem(ModBlocks.JUMP_PAD);
        evenSimplerBlockItem(ModBlocks.REFORGING_TABLE);
        evenSimplerBlockItem(ModBlocks.CHARGING_STATION);
        evenSimplerBlockItem(ModBlocks.CREATIVE_CHARGING_STATION);
        evenSimplerBlockItem(ModBlocks.VEHICLE_DEPLOYER);
        evenSimplerBlockItem(ModBlocks.AIRCRAFT_CATAPULT);
        evenSimplerBlockItem(ModBlocks.SUPERB_ITEM_INTERFACE);
        evenSimplerBlockItem(ModBlocks.CREATIVE_SUPERB_ITEM_INTERFACE);
    }

    private void simpleMaterials(ModItems.Materials materials) {
        simpleItem(materials.action());
        simpleItem(materials.barrel());
        simpleItem(materials.trigger());
        simpleItem(materials.spring());
    }

    private void simpleItem(Item item) {
        simpleItem(item, "");
    }

    private void simpleItem(Item item, String location) {
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(item);
        JsonObject json = new JsonObject();
        json.addProperty("parent", "minecraft:item/generated");
        JsonObject textures = new JsonObject();
        textures.addProperty("layer0", modLoc("item/" + location + id.getPath()).toString());
        json.add("textures", textures);
        models.put(id, json);
    }

    private void evenSimplerBlockItem(Block block) {
        String name = BuiltInRegistries.BLOCK.getKey(block).getPath();
        JsonObject json = new JsonObject();
        json.addProperty("parent", modLoc("block/" + name).toString());
        models.put(ResourceLocation.fromNamespaceAndPath(Mod.MODID, name), json);
    }

    private void gunBlueprintItem(Item item) {
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(item);
        JsonObject json = new JsonObject();
        json.addProperty("parent", "minecraft:item/generated");
        JsonObject textures = new JsonObject();
        textures.addProperty("layer0", modLoc("item/gun_blueprint").toString());
        json.add("textures", textures);
        models.put(id, json);
    }

    private void cannonBlueprintItem(Item item) {
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(item);
        JsonObject json = new JsonObject();
        json.addProperty("parent", "minecraft:item/generated");
        JsonObject textures = new JsonObject();
        textures.addProperty("layer0", modLoc("item/cannon_blueprint").toString());
        json.add("textures", textures);
        models.put(id, json);
    }

    private void handheldItem(Item item) {
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(item);
        JsonObject json = new JsonObject();
        json.addProperty("parent", "minecraft:item/handheld");
        JsonObject textures = new JsonObject();
        textures.addProperty("layer0", modLoc("item/" + id.getPath()).toString());
        json.add("textures", textures);
        models.put(id, json);
    }

    private void gunIcon(Item item, String name) {
        JsonObject json = new JsonObject();
        json.addProperty("parent", "minecraft:item/generated");
        JsonObject textures = new JsonObject();
        textures.addProperty("layer0", modLoc("item/" + name + "_icon").toString());
        json.add("textures", textures);
        models.put(Mod.loc(name + "_icon"), json);
    }

    private void gunBase(Item item, String name) {
        JsonObject json = new JsonObject();
        json.addProperty("parent", modLoc("displaysettings/" + name + ".item").toString());
        JsonObject textures = new JsonObject();
        textures.addProperty("layer0", modLoc("item/" + name).toString());
        json.add("textures", textures);
        models.put(Mod.loc(name + "_base"), json);
    }

    private void customSeparatedGunModel(Item item, String name) {
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(item);

        CustomSeparateModelBuilder builder = CustomSeparateModelBuilder.begin()
                .base(modLoc("item/" + name + "_base").toString())
                .perspective(ItemDisplayContext.GUI, modLoc("item/" + name + "_icon").toString());

        JsonObject json = builder.toJson();
        json.addProperty("gui_light", "front");

        JsonObject textures = new JsonObject();
        textures.addProperty("particle", modLoc("item/" + name + "_icon").toString());
        json.add("textures", textures);

        models.put(id, json);
    }

    public void gunItem(Item item, String name) {
        this.gunIcon(item, name);
        this.gunBase(item, name);
        this.customSeparatedGunModel(item, name);
    }

    private ResourceLocation modLoc(String path) {
        return Mod.loc(path);
    }

    @Override
    public @NotNull String getName() {
        return "Superb Warfare Item Models";
    }
}
