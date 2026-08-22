package com.atsuishio.superbwarfare.compat.ponder.scene

import com.atsuishio.superbwarfare.Mod.Companion.loc
import com.atsuishio.superbwarfare.compat.ponder.GeneratedPonderSupport
import com.atsuishio.superbwarfare.compat.ponder.GeneratedPonderSupport.createEntity
import com.atsuishio.superbwarfare.compat.ponder.GeneratedPonderSupport.highlightSection
import com.atsuishio.superbwarfare.compat.ponder.GeneratedPonderSupport.preScanBounds
import com.atsuishio.superbwarfare.compat.ponder.GeneratedPonderSupport.setBlock
import com.atsuishio.superbwarfare.compat.ponder.GeneratedPonderSupport.showControls
import com.atsuishio.superbwarfare.compat.ponder.GeneratedPonderSupport.showStructure
import com.atsuishio.superbwarfare.compat.ponder.GeneratedPonderSupport.showText
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper
import net.createmod.ponder.api.scene.SceneBuilder
import net.createmod.ponder.api.scene.SceneBuildingUtil
import net.minecraft.core.BlockPos
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.phys.Vec3

object ChargingStationPonderScene {
    fun register(helper: PonderSceneRegistrationHelper<ResourceLocation>) {
        helper.forComponents(loc("charging_station"))
            .addStoryBoard("basic_15x15", ChargingStationPonderScene::introScene)
    }

    private fun introScene(scene: SceneBuilder, util: SceneBuildingUtil) {
        scene.title("charging_station_intro", "Charging Station Introduction")
        val context = GeneratedPonderSupport.Context()
        preScanBounds(scene, BlockPos(-1, -8, -1), BlockPos(15, 8, 15))
        scene.addKeyframe()
        showStructure(scene, context, null, null, 0.5f, null)
        setBlock(
            scene,
            context,
            "superbwarfare:charging_station",
            mapOf("show_range" to "false"),
            BlockPos(7, 1, 7),
            null,
            "{Energy:0,FuelTick:0,Items:[],MaxFuelTick:1600,ShowRange:0b}",
            false,
            spawnParticles = false,
            entranceAnimation = "none",
            entranceDuration = null,
            entranceInterval = null,
            smartDisplay = null,
            linkId = null,
            direction = null
        )
        scene.idle(20)
        scene.addKeyframe()
        showText(
            scene,
            "The Charging Station wirelessly provides power to vehicles and turrets from this mod",
            Vec3(7.5, 1.5, 7.0),
            60,
            null,
            true
        )
        scene.idle(60)
        createEntity(
            scene,
            context,
            "superbwarfare:waveforce_tower",
            Vec3(10.5, 1.0, 7.5),
            null,
            null,
            null,
            null,
            null,
            "simultaneous",
            null,
            "north"
        )
        createEntity(
            scene,
            context,
            "superbwarfare:wheel_chair",
            Vec3(4.5, 1.0, 7.5),
            null,
            null,
            null,
            null,
            null,
            "simultaneous",
            null,
            "north"
        )
        scene.idle(40)
        scene.addKeyframe()
        showText(
            scene,
            "The default wireless charging range is an 8-block radius centered on the station",
            Vec3(7.5, 1.5, 7.0),
            60,
            null,
            true
        )
        highlightSection(scene, "green", BlockPos(15, 8, -1), BlockPos(-1, -8, 15), 60)
        scene.idle(80)
        scene.addKeyframe()
        showText(
            scene,
            "Insert fuel or food into the Charging Station to generate power",
            Vec3(7.5, 1.5, 7.0),
            60,
            null,
            true
        )
        showControls(
            scene, Vec3(7.5, 1.5, 7.0), "right", 60, "right", "minecraft:coal", null,
            whileSneaking = false,
            whileCtrl = false
        )
        scene.idle(80)
        scene.addKeyframe()
        showText(
            scene,
            "Energy from other mods can also be imported into the Charging Station",
            Vec3(7.5, 1.5, 7.0),
            60,
            null,
            true
        )
        scene.idle(80)
    }
}
