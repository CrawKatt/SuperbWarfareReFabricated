package com.atsuishio.superbwarfare.compat.ponder.scene

import com.atsuishio.superbwarfare.Mod.Companion.loc
import com.atsuishio.superbwarfare.compat.ponder.GeneratedPonderSupport
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper
import net.createmod.ponder.api.scene.SceneBuilder
import net.createmod.ponder.api.scene.SceneBuildingUtil
import net.minecraft.core.BlockPos
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.phys.Vec3

object CreativeChargingStationPonderScene {
    fun register(helper: PonderSceneRegistrationHelper<ResourceLocation>) {
        helper.forComponents(loc("creative_charging_station"))
            .addStoryBoard("basic_15x15", CreativeChargingStationPonderScene::introScene)
            .addStoryBoard("basic_5x5", CreativeChargingStationPonderScene::interactScene)
            .addStoryBoard("basic_15x15", CreativeChargingStationPonderScene::infiniteEnergyScene)
    }

    private fun introScene(scene: SceneBuilder, util: SceneBuildingUtil) {
        scene.title("creative_charging_station_intro", "Creative Charging Station Introduction")
        val context = GeneratedPonderSupport.Context()
        GeneratedPonderSupport.preScanBounds(scene, BlockPos(-1, -8, -1), BlockPos(15, 8, 15))
        scene.addKeyframe()
        GeneratedPonderSupport.showStructure(scene, context, null, null, 0.5f, null)
        GeneratedPonderSupport.setBlock(scene, context, "superbwarfare:creative_charging_station", mapOf("show_range" to "false"), BlockPos(7, 1, 7), null, "{Energy:0,FuelTick:0,Items:[],MaxFuelTick:1600,ShowRange:0b}", false, false, "none", null, null, null, null, null)
        scene.idle(20)
        scene.addKeyframe()
        GeneratedPonderSupport.showText(scene, "The Creative Charging Station is a block that wirelessly supplies infinite power to vehicles and turrets from this mod", Vec3(7.5, 1.5, 7.0), 60, null, true)
        scene.idle(60)
        GeneratedPonderSupport.createEntity(scene, context, "superbwarfare:waveforce_tower", Vec3(10.5, 1.0, 7.5), null, null, null, null, null, "simultaneous", null, "north")
        GeneratedPonderSupport.createEntity(scene, context, "superbwarfare:wheel_chair", Vec3(4.5, 1.0, 7.5), null, null, null, null, null, "simultaneous", null, "north")
        scene.idle(40)
        scene.addKeyframe()
        GeneratedPonderSupport.showText(scene, "The default wireless charging range is an 8-block radius centered on the station", Vec3(7.5, 1.5, 7.0), 60, null, true)
        GeneratedPonderSupport.highlightSection(scene, "green", BlockPos(15, 8, -1), BlockPos(-1, -8, 15), 60)
        scene.idle(80)
    }

    private fun interactScene(scene: SceneBuilder, util: SceneBuildingUtil) {
        scene.title("creative_charging_station_interact", "Interaction")
        val context = GeneratedPonderSupport.Context()
        GeneratedPonderSupport.preScanBounds(scene, BlockPos(2, 1, 2), BlockPos(2, 1, 2))
        GeneratedPonderSupport.showStructure(scene, context, null, null, null, null)
        GeneratedPonderSupport.setBlock(scene, context, "superbwarfare:creative_charging_station", mapOf("show_range" to "false"), BlockPos(2, 1, 2), null, "{Energy:0,FuelTick:0,Items:[],MaxFuelTick:1600,ShowRange:0b}", false, false, "none", null, null, null, null, null)
        scene.idle(20)
        GeneratedPonderSupport.showText(scene, "Right-click the Creative Charging Station while holding an item with FE energy to fill its energy", Vec3(2.0, 1.5, 2.5), 60, null, true)
        scene.idle(20)
        GeneratedPonderSupport.showControls(scene, Vec3(2.5, 1.5, 2.5), "right", 40, "right", "superbwarfare:small_battery_pack", null, false, false)
        scene.idle(60)
        GeneratedPonderSupport.showText(scene, "Right-click it again to clear its energy", Vec3(2.0, 1.5, 2.5), 60, null, true)
        scene.idle(20)
        GeneratedPonderSupport.showControls(scene, Vec3(2.5, 1.5, 2.5), "right", 40, "right", "superbwarfare:small_battery_pack", null, false, false)
        scene.idle(60)
    }

    private fun infiniteEnergyScene(scene: SceneBuilder, util: SceneBuildingUtil) {
        scene.title("creative_charging_station_infinite_energy", "Infinite Energy")
        val context = GeneratedPonderSupport.Context()
        GeneratedPonderSupport.showStructure(scene, context, null, null, 0.5f, null)
        GeneratedPonderSupport.showText(scene, "The Creative Charging Station can be placed in a vehicle inventory", null, 60, null, true)
        scene.idle(20)
        GeneratedPonderSupport.createEntity(scene, context, "superbwarfare:a_10a", Vec3(7.5, 1.0, 7.5), null, null, null, null, "1", "simultaneous", null, "north")
        scene.idle(20)
        GeneratedPonderSupport.showText(scene, "Sneak + right-click to open the vehicle inventory, then place the Creative Charging Station inside to provide infinite energy", Vec3(7.5, 1.0, 7.5), 60, null, true)
        GeneratedPonderSupport.showControls(scene, Vec3(7.5, 1.0, 7.5), "right", 60, "right", null, null, true, false)
        scene.idle(60)
    }

}

