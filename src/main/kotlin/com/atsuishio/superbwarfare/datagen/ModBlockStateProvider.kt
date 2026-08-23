package com.atsuishio.superbwarfare.datagen

import com.atsuishio.superbwarfare.Mod
import com.atsuishio.superbwarfare.Mod.Companion.loc
import com.atsuishio.superbwarfare.init.ModBlocks
import com.google.gson.JsonObject
import net.minecraft.core.Direction
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.data.CachedOutput
import net.minecraft.data.DataProvider
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.StringRepresentable
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import java.util.concurrent.CompletableFuture

class ModBlockStateProvider(private val output: PackOutput) : DataProvider {
    private val blockStates = linkedMapOf<ResourceLocation, JsonObject>()
    private val models = linkedMapOf<ResourceLocation, JsonObject>()

    override fun run(pOutput: CachedOutput): CompletableFuture<*> {
        blockStates.clear()
        models.clear()
        registerStatesAndModels()

        val blockStatePath = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "blockstates")
        val modelPath = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "models")
        return CompletableFuture.allOf(
            *(blockStates.map { (id, json) -> DataProvider.saveStable(pOutput, json, blockStatePath.json(id)) } +
                models.map { (id, json) -> DataProvider.saveStable(pOutput, json, modelPath.json(id)) })
                .toTypedArray()
        )
    }

    override fun getName(): String {
        return "${Mod.MODID} block states"
    }

    private fun registerStatesAndModels() {
        horizontalBlock(ModBlocks.BARBED_WIRE, blockModel("barbed_wire"))
        horizontalBlock(ModBlocks.JUMP_PAD, blockModel("jump_pad"))
        horizontalBlock(ModBlocks.REFORGING_TABLE, blockModel("reforging_table"))
        horizontalBlock(ModBlocks.CONTAINER, blockModel("container"))
        horizontalBlock(ModBlocks.SMALL_CONTAINER, blockModel("small_container"))
        horizontalBlock(ModBlocks.LUCKY_CONTAINER, blockModel("container"))
        horizontalBlock(ModBlocks.CHARGING_STATION, blockModel("charging_station"))
        horizontalBlock(ModBlocks.CREATIVE_CHARGING_STATION, blockModel("creative_charging_station"))
        horizontalBlock(
            ModBlocks.VEHICLE_DEPLOYER,
            cubeBottomTop(
                "vehicle_deployer", texture("vehicle_deployer_side"),
                texture("vehicle_deployer_bottom"), texture("vehicle_deployer_top"),
                texture("vehicle_deployer_bottom")
            )
        )
        horizontalBlock(ModBlocks.VEHICLE_ASSEMBLING_TABLE, blockModel("vehicle_assembling_table"))
        horizontalBlock(ModBlocks.BLUEPRINT_RESEARCH_TABLE, blockModel("blueprint_research_table"))
        simpleBlock(
            ModBlocks.BIOGAS_GENERATOR,
            cubeBottomTop(
                "biogas_generator",
                texture("biogas_generator_side"),
                texture("biogas_generator_bottom"),
                texture("biogas_generator_top"),
                texture("biogas_generator_side")
            )
        )

        horizontalBlock(
            ModBlocks.AIRCRAFT_CATAPULT,
            cube(
                "aircraft_catapult",
                texture("vehicle_deployer_bottom"),
                texture("aircraft_catapult_top"),
                texture("aircraft_catapult_side"),
                texture("aircraft_catapult_side"),
                texture("aircraft_catapult_side2"),
                texture("aircraft_catapult_side2"),
                texture("aircraft_catapult_top")
            )
        )

        horizontalBlock(
            ModBlocks.CATAPULT_CONTROLLER,
            cube(
                "catapult_controller",
                texture("vehicle_deployer_bottom"),
                texture("aircraft_catapult_controller_top"),
                texture("aircraft_catapult_controller_side"),
                texture("aircraft_catapult_controller_side"),
                texture("aircraft_catapult_side2"),
                texture("aircraft_catapult_side2"),
                texture("aircraft_catapult_top")
            )
        )

        directionalBlock(
            ModBlocks.SUPERB_ITEM_INTERFACE,
            cubeBottomTop(
                "superb_item_interface",
                texture("superb_item_interface_side"),
                texture("superb_item_interface_bottom"),
                texture("superb_item_interface_top"),
                texture("superb_item_interface_bottom")
            )
        )

        directionalBlock(
            ModBlocks.CREATIVE_SUPERB_ITEM_INTERFACE,
            cubeBottomTop(
                "creative_superb_item_interface",
                texture("creative_superb_item_interface_side"),
                texture("creative_superb_item_interface_bottom"),
                texture("creative_superb_item_interface_top"),
                texture("creative_superb_item_interface_bottom")
            )
        )

        blockWithItem(ModBlocks.GALENA_ORE)
        blockWithItem(ModBlocks.DEEPSLATE_GALENA_ORE)
        blockWithItem(ModBlocks.SCHEELITE_ORE)
        blockWithItem(ModBlocks.DEEPSLATE_SCHEELITE_ORE)
        blockWithItem(ModBlocks.LEAD_BLOCK)
        blockWithItem(ModBlocks.STEEL_BLOCK)
        blockWithItem(ModBlocks.TUNGSTEN_BLOCK)
        blockWithItem(ModBlocks.CEMENTED_CARBIDE_BLOCK)
        blockWithItem(ModBlocks.SILVER_ORE)
        blockWithItem(ModBlocks.DEEPSLATE_SILVER_ORE)
        blockWithItem(ModBlocks.SILVER_BLOCK)
        blockWithItem(ModBlocks.RAW_GALENA_BLOCK)
        blockWithItem(ModBlocks.RAW_SCHEELITE_BLOCK)
        blockWithItem(ModBlocks.RAW_SILVER_BLOCK)

        simpleBlock(ModBlocks.FUMO_25, blockModel("fumo_25"))
    }

    private fun simpleBlock(block: Block, model: ResourceLocation) {
        variantsBlock(block, model, RotationMode.NONE)
    }

    private fun horizontalBlock(block: Block, model: ResourceLocation) {
        variantsBlock(block, model, RotationMode.HORIZONTAL)
    }

    private fun directionalBlock(block: Block, model: ResourceLocation) {
        variantsBlock(block, model, RotationMode.DIRECTIONAL)
    }

    private fun variantsBlock(block: Block, model: ResourceLocation, rotationMode: RotationMode) {
        val variants = JsonObject()
        for (state in block.stateDefinition.possibleStates) {
            val variant = JsonObject()
            val rotation = rotation(state, rotationMode)
            variant.addProperty("model", model.toString())
            if (rotation.first != 0) {
                variant.addProperty("x", rotation.first)
            }
            if (rotation.second != 0) {
                variant.addProperty("y", rotation.second)
            }
            variants.add(variantKey(state), variant)
        }

        val json = JsonObject()
        json.add("variants", variants)
        addBlockState(block, json)
    }

    private fun blockWithItem(block: Block) {
        val path = blockPath(block)
        val model = cubeAll(path)
        simpleBlock(block, model)
        addModel(loc("item/$path"), model(model))
    }

    private fun cubeAll(name: String): ResourceLocation {
        return addModel(
            loc("block/$name"),
            model(ResourceLocation.withDefaultNamespace("block/cube_all"), mapOf("all" to texture(name)))
        )
    }

    private fun cubeBottomTop(
        name: String,
        side: ResourceLocation,
        bottom: ResourceLocation,
        top: ResourceLocation,
        particle: ResourceLocation
    ): ResourceLocation {
        return addModel(
            loc("block/$name"),
            model(
                ResourceLocation.withDefaultNamespace("block/cube_bottom_top"),
                linkedMapOf("bottom" to bottom, "particle" to particle, "side" to side, "top" to top)
            )
        )
    }

    private fun cube(
        name: String,
        down: ResourceLocation,
        up: ResourceLocation,
        north: ResourceLocation,
        south: ResourceLocation,
        east: ResourceLocation,
        west: ResourceLocation,
        particle: ResourceLocation
    ): ResourceLocation {
        return addModel(
            loc("block/$name"),
            model(
                ResourceLocation.withDefaultNamespace("block/cube"),
                linkedMapOf(
                    "down" to down,
                    "east" to east,
                    "north" to north,
                    "particle" to particle,
                    "south" to south,
                    "up" to up,
                    "west" to west
                )
            )
        )
    }

    private fun model(parent: ResourceLocation, textures: Map<String, ResourceLocation> = emptyMap()): JsonObject {
        val json = JsonObject()
        json.addProperty("parent", parent.toString())
        if (textures.isNotEmpty()) {
            val textureJson = JsonObject()
            textures.forEach { (key, value) -> textureJson.addProperty(key, value.toString()) }
            json.add("textures", textureJson)
        }
        return json
    }

    private fun addBlockState(block: Block, json: JsonObject) {
        val id = blockKey(block)
        check(blockStates.put(id, json) == null) { "Duplicate blockstate: $id" }
    }

    private fun addModel(id: ResourceLocation, json: JsonObject): ResourceLocation {
        check(models.put(id, json) == null) { "Duplicate model: $id" }
        return id
    }

    private fun rotation(state: BlockState, mode: RotationMode): Pair<Int, Int> {
        return when (mode) {
            RotationMode.NONE -> 0 to 0
            RotationMode.HORIZONTAL -> 0 to horizontalY(state.getValue(BlockStateProperties.HORIZONTAL_FACING))
            RotationMode.DIRECTIONAL -> directionalRotation(state.getValue(BlockStateProperties.FACING))
        }
    }

    private fun horizontalY(direction: Direction): Int {
        return when (direction) {
            Direction.EAST -> 90
            Direction.SOUTH -> 180
            Direction.WEST -> 270
            else -> 0
        }
    }

    private fun directionalRotation(direction: Direction): Pair<Int, Int> {
        return when (direction) {
            Direction.DOWN -> 180 to 0
            Direction.UP -> 0 to 0
            Direction.NORTH -> 90 to 0
            Direction.EAST -> 90 to 90
            Direction.SOUTH -> 90 to 180
            Direction.WEST -> 90 to 270
        }
    }

    private fun variantKey(state: BlockState): String {
        return state.values.entries.joinToString(",") { (property, value) ->
            "${property.name}=${valueName(value)}"
        }
    }

    private fun valueName(value: Comparable<*>): String {
        return if (value is StringRepresentable) value.serializedName else value.toString()
    }

    private fun blockModel(name: String): ResourceLocation {
        return loc("block/$name")
    }

    private fun texture(name: String): ResourceLocation {
        return loc("block/$name")
    }

    private fun blockKey(block: Block): ResourceLocation {
        return BuiltInRegistries.BLOCK.getKey(block)
    }

    private fun blockPath(block: Block): String {
        return blockKey(block).path
    }

    private enum class RotationMode {
        NONE,
        HORIZONTAL,
        DIRECTIONAL
    }
}
