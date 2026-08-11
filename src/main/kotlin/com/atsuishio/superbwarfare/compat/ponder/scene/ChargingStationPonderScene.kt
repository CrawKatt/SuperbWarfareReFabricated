package com.atsuishio.superbwarfare.compat.ponder.scene

import com.atsuishio.superbwarfare.Mod.Companion.loc
import com.atsuishio.superbwarfare.compat.ponder.GeneratedPonderSupport
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
        scene.title("charging_station_scene_1", "Scene 1")
        val context = GeneratedPonderSupport.Context()
        GeneratedPonderSupport.preScanBounds(scene, BlockPos(-1, -8, -1), BlockPos(15, 8, 15))
        scene.addKeyframe()
        GeneratedPonderSupport.showStructure(scene, context, null, null, 0.5f, null)
        GeneratedPonderSupport.setBlock(scene, context, "superbwarfare:charging_station", mapOf("show_range" to "false"), BlockPos(7, 1, 7), null, "{Energy:0,FuelTick:0,Items:[],MaxFuelTick:1600,ShowRange:0b}", false, false, "none", null, null, null, null, null)
        scene.idle(20)
        scene.addKeyframe()
        GeneratedPonderSupport.showText(scene, "充电站是一个为本模组载具和炮塔无线提供电力的方块", Vec3(7.5, 1.5, 7.0), 60, null, true)
        scene.idle(60)
        GeneratedPonderSupport.createEntity(scene, context, "superbwarfare:waveforce_tower", Vec3(10.5, 1.0, 7.5), null, null, null, null, null, "simultaneous", null, "north")
        GeneratedPonderSupport.createEntity(scene, context, "superbwarfare:wheel_chair", Vec3(4.5, 1.0, 7.5), null, null, null, null, null, "simultaneous", null, "north")
        scene.idle(40)
        scene.addKeyframe()
        GeneratedPonderSupport.showText(scene, "无线充电范围默认是以充电站为中心半径8格", Vec3(7.5, 1.5, 7.0), 60, null, true)
        GeneratedPonderSupport.highlightSection(scene, "green", BlockPos(15, 8, -1), BlockPos(-1, -8, 15), 60)
        scene.idle(80)
        scene.addKeyframe()
        GeneratedPonderSupport.showText(scene, "充电站内放入燃料或食物来给充电站发电", Vec3(7.5, 1.5, 7.0), 60, null, true)
        GeneratedPonderSupport.showControls(scene, Vec3(7.5, 1.5, 7.0), "right", 60, "right", "minecraft:coal", null, false, false)
        scene.idle(80)
        scene.addKeyframe()
        GeneratedPonderSupport.showText(scene, "同时也支持其他模组的能量导入充电站", Vec3(7.5, 1.5, 7.0), 60, null, true)
        scene.idle(80)
    }


}
