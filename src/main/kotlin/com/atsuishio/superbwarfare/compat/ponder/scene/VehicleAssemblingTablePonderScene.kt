package com.atsuishio.superbwarfare.compat.ponder.scene

import com.atsuishio.superbwarfare.Mod.Companion.loc
import com.atsuishio.superbwarfare.compat.ponder.GeneratedPonderSupport
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper
import net.createmod.ponder.api.scene.SceneBuilder
import net.createmod.ponder.api.scene.SceneBuildingUtil
import net.minecraft.core.BlockPos
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.phys.Vec3

object VehicleAssemblingTablePonderScene {

    fun register(helper: PonderSceneRegistrationHelper<ResourceLocation>) {
        helper.forComponents(loc("vehicle_assembling_table"))
            .addStoryBoard("basic_7x7", VehicleAssemblingTablePonderScene::introScene)
            .addStoryBoard("basic_7x7", VehicleAssemblingTablePonderScene::interactScene)
    }

    private fun introScene(scene: SceneBuilder, util: SceneBuildingUtil) {
        scene.title("vehicle_assembling_table_scene_1", "Scene 1")
        val context = GeneratedPonderSupport.Context()
        GeneratedPonderSupport.preScanBounds(scene, BlockPos(3, 1, 3), BlockPos(3, 1, 3))
        scene.addKeyframe()
        GeneratedPonderSupport.showStructure(scene, context, null, null, null, null)
        GeneratedPonderSupport.setBlock(scene, context, "superbwarfare:vehicle_assembling_table", null, BlockPos(3, 1, 3), null, null, false, false, "none", null, null, null, null, null)
        scene.idle(20)
        scene.addKeyframe()
        GeneratedPonderSupport.showText(scene, "Edit this ponder scene!", Vec3(2.5, 2.0, 2.5), 40, null, true)
        scene.idle(60)
        scene.addKeyframe()
        GeneratedPonderSupport.showText(scene, "右击可以打开装配界面", Vec3(3.5, 2.0, 3.5), 40, null, true)
        scene.idle(20)
        GeneratedPonderSupport.showControls(scene, Vec3(3.5, 2.0, 3.5), "right", 20, "right", null, null, false, false)
        scene.idle(60)
        scene.addKeyframe()
        GeneratedPonderSupport.showText(scene, "只要背包中有足够的材料，就可以直接合成物品", Vec3(3.5, 2.0, 3.5), 60, null, true)
        scene.idle(60)
    }


    private fun interactScene(scene: SceneBuilder, util: SceneBuildingUtil) {
        scene.title("vehicle_assembling_table_s_df2b0", "交互?")
        val context = GeneratedPonderSupport.Context()
        GeneratedPonderSupport.preScanBounds(scene, BlockPos(2, 1, 3), BlockPos(3, 2, 4))
        GeneratedPonderSupport.showStructure(scene, context, null, null, null, null)
        GeneratedPonderSupport.setBlock(scene, context, "superbwarfare:vehicle_assembling_table", null, BlockPos(3, 1, 3), null, null, false, false, "none", null, null, null, null, null)
        scene.idle(20)
        GeneratedPonderSupport.showText(scene, "使用撬棍右击的话……", Vec3(3.5, 2.0, 3.5), 40, null, true)
        scene.idle(20)
        GeneratedPonderSupport.showControls(scene, Vec3(3.5, 2.0, 3.5), "right", 40, "right", "superbwarfare:crowbar", null, false, false)
        scene.idle(20)
        GeneratedPonderSupport.destroyBlock(scene, context, BlockPos(3, 1, 3), BlockPos(2, 2, 4), null)
        GeneratedPonderSupport.createEntity(scene, context, "superbwarfare:vehicle_assembling_table", Vec3(3.0, 1.1, 4.0), null, null, null, null, "1", null, null, null)
        scene.idle(20)
    }


}
