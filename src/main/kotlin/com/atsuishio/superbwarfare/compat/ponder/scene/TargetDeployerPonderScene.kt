package com.atsuishio.superbwarfare.compat.ponder.scene

import com.atsuishio.superbwarfare.Mod.Companion.loc
import com.atsuishio.superbwarfare.compat.ponder.GeneratedPonderSupport
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper
import net.createmod.ponder.api.scene.SceneBuilder
import net.createmod.ponder.api.scene.SceneBuildingUtil
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.phys.Vec3

object TargetDeployerPonderScene {

    fun register(helper: PonderSceneRegistrationHelper<ResourceLocation>) {
        helper.forComponents(loc("target_deployer"))
            .addStoryBoard("basic_5x5", TargetDeployerPonderScene::introScene)
            .addStoryBoard("basic_5x5", TargetDeployerPonderScene::interactScene)
    }

    private fun introScene(scene: SceneBuilder, util: SceneBuildingUtil) {
        scene.title("target_deployer_scene_1", "Introduction")
        val context = GeneratedPonderSupport.Context()
        scene.addKeyframe()
        GeneratedPonderSupport.showStructure(scene, context, null, null, null, null)
        GeneratedPonderSupport.createEntity(scene, context, "superbwarfare:target", Vec3(2.5, 1.0, 2.5), null, null, null, null, "1", "simultaneous", null, "down")
        GeneratedPonderSupport.showText(scene, "标靶是卓越前线的第一个物品", Vec3(3.5, 1.0, 3.5), 40, null, true)
        scene.idle(60)
        scene.addKeyframe()
        GeneratedPonderSupport.showText(scene, "使用武器攻击标靶可以显示该次攻击的伤害", Vec3(3.5, 1.0, 4.5), 60, null, true)
        scene.idle(20)
        GeneratedPonderSupport.showControls(scene, Vec3(2.5, 2.0, 2.5), "right", 40, "left", "minecraft:diamond_sword", null, false, false)
        scene.idle(60)
    }


    private fun interactScene(scene: SceneBuilder, util: SceneBuildingUtil) {
        scene.title("target_deployer_s_c3454", "交互")
        val context = GeneratedPonderSupport.Context()
        GeneratedPonderSupport.showStructure(scene, context, null, null, null, null)
        GeneratedPonderSupport.createEntity(scene, context, "superbwarfare:target", Vec3(2.5, 1.0, 2.5), Vec3(1.5, 1.0, 2.5), null, null, null, "1", "simultaneous", null, "down")
        scene.idle(20)
        scene.addKeyframe()
        GeneratedPonderSupport.showText(scene, "空手右击可以让标靶面朝自己", Vec3(2.5, 2.0, 2.5), 40, null, true)
        scene.idle(20)
        GeneratedPonderSupport.showControls(scene, Vec3(2.5, 2.0, 2.5), "right", 20, "right", null, null, false, false)
        scene.idle(20)
        GeneratedPonderSupport.modifyEntitiesNbt(scene, context, false, "superbwarfare:target", "1", "{Rotation:[180f,0f]}", null, null, null, null, null)
        scene.idle(20)
        scene.addKeyframe()
        scene.idle(20)
        scene.addKeyframe()
        GeneratedPonderSupport.showText(scene, "潜行时空手右击能够拆除标靶", Vec3(2.5, 2.0, 2.5), 40, null, true)
        scene.idle(20)
        GeneratedPonderSupport.showControls(scene, Vec3(2.5, 2.0, 2.5), "right", 20, "right", null, null, true, false)
        scene.idle(20)
        GeneratedPonderSupport.clearEntities(scene, context, false, "superbwarfare:target", "1", null, null, null, null, null)
        scene.idle(20)
    }


}
