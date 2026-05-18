package com.atsuishio.superbwarfare.datagen;

import com.atsuishio.superbwarfare.Mod;
import com.atsuishio.superbwarfare.init.ModBlocks;
import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ModBlockStateProvider implements DataProvider {

    private final PackOutput output;

    private final Map<ResourceLocation, JsonObject> blockStates = new HashMap<>();
    private final Map<ResourceLocation, JsonObject> models = new HashMap<>();
    private final List<ResourceLocation> simpleItemModels = new ArrayList<>();

    public ModBlockStateProvider(PackOutput output) {
        this.output = output;
    }

    @Override
    public @NotNull CompletableFuture<?> run(@NotNull CachedOutput cache) {
        generate();

        PackOutput.PathProvider blockstatePath = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "blockstates");
        PackOutput.PathProvider modelPath = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "models/block");
        PackOutput.PathProvider itemModelPath = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "models/item");

        List<CompletableFuture<?>> futures = new ArrayList<>();

        for (var entry : blockStates.entrySet()) {
            futures.add(DataProvider.saveStable(cache, entry.getValue(), blockstatePath.json(entry.getKey())));
        }
        for (var entry : models.entrySet()) {
            futures.add(DataProvider.saveStable(cache, entry.getValue(), modelPath.json(entry.getKey())));
        }
        for (var modelId : simpleItemModels) {
            JsonObject json = new JsonObject();
            json.addProperty("parent", modelId.toString());
            futures.add(DataProvider.saveStable(cache, json, itemModelPath.json(modelId)));
        }

        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }

    private void generate() {
        horizontalBlock(ModBlocks.BARBED_WIRE, modLoc("block/barbed_wire"));
        horizontalBlock(ModBlocks.JUMP_PAD, modLoc("block/jump_pad"));
        horizontalBlock(ModBlocks.REFORGING_TABLE, modLoc("block/reforging_table"));
        horizontalBlock(ModBlocks.CONTAINER, modLoc("block/container"));
        horizontalBlock(ModBlocks.SMALL_CONTAINER, modLoc("block/small_container"));
        horizontalBlock(ModBlocks.LUCKY_CONTAINER, modLoc("block/container"));
        horizontalBlock(ModBlocks.CHARGING_STATION, modLoc("block/charging_station"));
        horizontalBlock(ModBlocks.CREATIVE_CHARGING_STATION, modLoc("block/creative_charging_station"));
        horizontalBlock(ModBlocks.VEHICLE_DEPLOYER, cubeBottomTop("vehicle_deployer",
                modLoc("block/vehicle_deployer_side"),
                modLoc("block/vehicle_deployer_bottom"),
                modLoc("block/vehicle_deployer_top")));
        horizontalBlock(ModBlocks.VEHICLE_ASSEMBLING_TABLE, modLoc("block/vehicle_assembling_table"));

        horizontalBlock(ModBlocks.AIRCRAFT_CATAPULT, cube("aircraft_catapult",
                modLoc("block/vehicle_deployer_bottom"),
                modLoc("block/aircraft_catapult_top"),
                modLoc("block/aircraft_catapult_side"),
                modLoc("block/aircraft_catapult_side"),
                modLoc("block/aircraft_catapult_side2"),
                modLoc("block/aircraft_catapult_side2")));

        directionalBlock(ModBlocks.SUPERB_ITEM_INTERFACE, cubeBottomTop("superb_item_interface",
                modLoc("block/superb_item_interface_side"),
                modLoc("block/superb_item_interface_bottom"),
                modLoc("block/superb_item_interface_top")));

        directionalBlock(ModBlocks.CREATIVE_SUPERB_ITEM_INTERFACE, cubeBottomTop("creative_superb_item_interface",
                modLoc("block/creative_superb_item_interface_side"),
                modLoc("block/creative_superb_item_interface_bottom"),
                modLoc("block/creative_superb_item_interface_top")));

        blockWithItem(ModBlocks.GALENA_ORE);
        blockWithItem(ModBlocks.DEEPSLATE_GALENA_ORE);
        blockWithItem(ModBlocks.SCHEELITE_ORE);
        blockWithItem(ModBlocks.DEEPSLATE_SCHEELITE_ORE);
        blockWithItem(ModBlocks.LEAD_BLOCK);
        blockWithItem(ModBlocks.STEEL_BLOCK);
        blockWithItem(ModBlocks.TUNGSTEN_BLOCK);
        blockWithItem(ModBlocks.CEMENTED_CARBIDE_BLOCK);
        blockWithItem(ModBlocks.SILVER_ORE);
        blockWithItem(ModBlocks.DEEPSLATE_SILVER_ORE);
        blockWithItem(ModBlocks.SILVER_BLOCK);

        simpleBlock(ModBlocks.FUMO_25, modLoc("block/fumo_25"));
    }

    private void simpleBlock(Block block, ResourceLocation model) {
        String key = BuiltInRegistries.BLOCK.getKey(block).getPath();
        JsonObject bs = new JsonObject();
        JsonObject variants = new JsonObject();
        JsonObject variant = new JsonObject();
        variant.addProperty("model", model.toString());
        variants.add("", variant);
        bs.add("variants", variants);
        blockStates.put(Mod.loc(key), bs);
    }

    private void horizontalBlock(Block block, ResourceLocation model) {
        String key = BuiltInRegistries.BLOCK.getKey(block).getPath();
        JsonObject bs = new JsonObject();
        JsonObject variants = new JsonObject();

        addVariant(variants, "facing=north", model, 0);
        addVariant(variants, "facing=south", model, 180);
        addVariant(variants, "facing=west", model, 270);
        addVariant(variants, "facing=east", model, 90);

        bs.add("variants", variants);
        blockStates.put(Mod.loc(key), bs);
        simpleItemModel(block);
    }

    private void directionalBlock(Block block, ResourceLocation model) {
        String key = BuiltInRegistries.BLOCK.getKey(block).getPath();
        JsonObject bs = new JsonObject();
        JsonObject variants = new JsonObject();

        addVariant(variants, "facing=north", model, 0);
        addVariant(variants, "facing=east", model, 90);
        addVariant(variants, "facing=south", model, 180);
        addVariant(variants, "facing=west", model, 270);

        bs.add("variants", variants);
        blockStates.put(Mod.loc(key), bs);
        simpleItemModel(block);
    }

    private void addVariant(JsonObject variants, String key, ResourceLocation model, int y) {
        JsonObject v = new JsonObject();
        v.addProperty("model", model.toString());
        if (y != 0) v.addProperty("y", y);
        variants.add(key, v);
    }

    private void blockWithItem(Block block) {
        ResourceLocation model = cubeAll(block);
        simpleBlock(block, model);
        simpleItemModel(block);
    }

    private ResourceLocation cubeAll(Block block) {
        String name = BuiltInRegistries.BLOCK.getKey(block).getPath();
        ResourceLocation id = Mod.loc(name);
        JsonObject json = new JsonObject();
        json.addProperty("parent", "minecraft:block/cube_all");
        JsonObject textures = new JsonObject();
        textures.addProperty("all", modLoc("block/" + name).toString());
        json.add("textures", textures);
        models.put(id, json);
        return ResourceLocation.fromNamespaceAndPath(Mod.MODID, "block/" + name);
    }

    private ResourceLocation cubeBottomTop(String name, ResourceLocation side, ResourceLocation bottom, ResourceLocation top) {
        ResourceLocation id = Mod.loc(name);
        JsonObject json = new JsonObject();
        json.addProperty("parent", "minecraft:block/cube_bottom_top");
        JsonObject textures = new JsonObject();
        textures.addProperty("side", side.toString());
        textures.addProperty("bottom", bottom.toString());
        textures.addProperty("top", top.toString());
        textures.addProperty("particle", bottom.toString());
        json.add("textures", textures);
        models.put(id, json);
        return ResourceLocation.fromNamespaceAndPath(Mod.MODID, "block/" + name);
    }

    private ResourceLocation cube(String name, ResourceLocation down, ResourceLocation up, ResourceLocation north, ResourceLocation south, ResourceLocation east, ResourceLocation west) {
        ResourceLocation id = Mod.loc(name);
        JsonObject json = new JsonObject();
        json.addProperty("parent", "minecraft:block/cube");
        JsonObject textures = new JsonObject();
        textures.addProperty("down", down.toString());
        textures.addProperty("up", up.toString());
        textures.addProperty("north", north.toString());
        textures.addProperty("south", south.toString());
        textures.addProperty("east", east.toString());
        textures.addProperty("west", west.toString());
        json.add("textures", textures);
        models.put(id, json);
        return ResourceLocation.fromNamespaceAndPath(Mod.MODID, "block/" + name);
    }

    private void simpleItemModel(Block block) {
        String name = BuiltInRegistries.BLOCK.getKey(block).getPath();
        simpleItemModels.add(ResourceLocation.fromNamespaceAndPath(Mod.MODID, name));
    }

    private ResourceLocation modLoc(String path) {
        return Mod.loc(path);
    }

    @Override
    public @NotNull String getName() {
        return "Superb Warfare Block States";
    }
}
