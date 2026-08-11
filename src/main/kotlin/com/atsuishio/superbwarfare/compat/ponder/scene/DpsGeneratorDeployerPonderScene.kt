package com.atsuishio.superbwarfare.compat.ponder.scene

import com.atsuishio.superbwarfare.Mod.Companion.loc
import com.atsuishio.superbwarfare.compat.ponder.GeneratedPonderSupport
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper
import net.createmod.ponder.api.scene.SceneBuilder
import net.createmod.ponder.api.scene.SceneBuildingUtil
import net.minecraft.core.BlockPos
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.phys.Vec3

object DpsGeneratorDeployerPonderScene {

    fun register(helper: PonderSceneRegistrationHelper<ResourceLocation>) {
        helper.forComponents(loc("dps_generator_deployer"))
            .addStoryBoard("basic_5x5", DpsGeneratorDeployerPonderScene::introScene)
            .addStoryBoard("basic_5x5", DpsGeneratorDeployerPonderScene::interactScene)
    }

    private fun introScene(scene: SceneBuilder, util: SceneBuildingUtil) {
        scene.title("dps_generator_deployer_scene_1", "Scene 1")
        val context = GeneratedPonderSupport.Context()
        scene.addKeyframe()
        GeneratedPonderSupport.showStructure(scene, context, null, null, null, null)
        GeneratedPonderSupport.createEntity(scene, context, "superbwarfare:dps_generator", Vec3(2.5, 1.0, 2.5), Vec3(2.5, 0.0, 0.5), null, null, "{AbsorptionAmount:0.0f,Attributes:[{Base:0.0d,Name:\"forge:step_height_addition\"},{Base:0.08d,Name:\"forge:entity_gravity\"},{Base:0.0d,Name:\"minecraft:generic.movement_speed\"}],Brain:{memories:{}},CanUpdate:1b,Energy:0,FallFlying:0b,ForgeCaps:{\"curios:inventory\":{Curios:[]},\"superbwarfare:phosphorus_fire_capability\":{SbwPhosphorusFire:0b}},Health:40.0f,HurtByTimestamp:0,Invulnerable:0b,Level:0,NoGravity:1b}", "1", "simultaneous", null, "down")
        scene.idle(20)
        scene.addKeyframe()
        GeneratedPonderSupport.showText(scene, "DPS发电机是一种受到伤害时能产生FE能量并储存的标靶", Vec3(2.5, 2.0, 2.5), 60, null, true)
        scene.addKeyframe()
        scene.idle(80)
        scene.addKeyframe()
        GeneratedPonderSupport.showText(scene, "攻击可以让标靶储存FE能量", Vec3(2.5, 2.0, 2.5), 60, null, true)
        scene.idle(80)
        GeneratedPonderSupport.showControls(scene, Vec3(2.5, 2.0, 2.5), "right", 60, "left", "superbwarfare:beast", null, false, false)
        scene.idle(80)
        scene.addKeyframe()
        GeneratedPonderSupport.showText(scene, "拥有快速回血功能，当玩家在其回血前成功将其击倒，发电机等级将提升一级，最大等级为7", Vec3(2.5, 2.0, 2.5), 60, null, true)
        scene.idle(20)
        GeneratedPonderSupport.modifyEntitiesNbt(scene, context, false, "superbwarfare:dps_generator", "1", "{Level:1}", null, null, null, null, null)
        scene.idle(20)
        GeneratedPonderSupport.modifyEntitiesNbt(scene, context, false, "superbwarfare:dps_generator", "1", "{Level:2}", null, null, null, null, null)
        scene.idle(60)
    }


    private fun interactScene(scene: SceneBuilder, util: SceneBuildingUtil) {
        scene.title("dps_generator_deployer_s_ba02a", "导出FE能量")
        val context = GeneratedPonderSupport.Context()
        GeneratedPonderSupport.preScanBounds(scene, BlockPos(2, 1, 2), BlockPos(2, 1, 2))
        GeneratedPonderSupport.showStructure(scene, context, null, null, null, null)
        scene.idle(20)
        GeneratedPonderSupport.createEntity(scene, context, "superbwarfare:dps_generator", Vec3(2.5, 2.0, 2.5), Vec3(2.5, 0.0, 0.5), null, null, "{AbsorptionAmount:0.0f,Attributes:[{Base:0.0d,Name:\"forge:step_height_addition\"},{Base:0.08d,Name:\"forge:entity_gravity\"},{Base:0.0d,Name:\"minecraft:generic.movement_speed\"}],Brain:{memories:{}},CanUpdate:1b,Energy:0,FallFlying:0b,ForgeCaps:{\"curios:inventory\":{Curios:[]},\"superbwarfare:phosphorus_fire_capability\":{SbwPhosphorusFire:0b}},Health:40.0f,HurtByTimestamp:0,Invulnerable:0b,Level:0,NoGravity:1b}", null, "simultaneous", null, "down")
        GeneratedPonderSupport.setBlock(scene, context, "superbwarfare:charging_station", mapOf("show_range" to "false"), BlockPos(2, 1, 2), null, "{Energy:0,FuelTick:0,Items:[],MaxFuelTick:1600,ShowRange:0b}", false, false, "down", 20, 1, false, null, "down")
        scene.idle(20)
        scene.addKeyframe()
        GeneratedPonderSupport.showText(scene, "在DPS发电机下面一格放置任意具有FE存储的方块均可导出能量", Vec3(2.5, 3.0, 2.5), 60, null, true)
        scene.idle(80)
        scene.addKeyframe()
        GeneratedPonderSupport.showText(scene, "这里拿本模组的充电站演示", Vec3(2.5, 1.5, 2.0), 60, null, true)
        scene.idle(70)
    }


}
