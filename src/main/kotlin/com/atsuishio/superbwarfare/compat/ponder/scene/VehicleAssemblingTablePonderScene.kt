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
        scene.title("vehicle_assembling_table_intro", "Vehicle Assembling")
        val context: GeneratedPonderSupport.Context = GeneratedPonderSupport.Context()
        GeneratedPonderSupport.preScanBounds(scene, BlockPos(3, 1, 3), BlockPos(3, 1, 3))
        scene.addKeyframe()
        GeneratedPonderSupport.showStructure(scene, context, null, null, null, null)
        GeneratedPonderSupport.setBlock(
            scene,
            context,
            "superbwarfare:vehicle_assembling_table",
            null,
            BlockPos(3, 1, 3),
            null,
            null,
            immediateDisplay = false,
            spawnParticles = false,
            entranceAnimation = "none",
            entranceDuration = null,
            entranceInterval = null,
            smartDisplay = null,
            linkId = null,
            direction = null
        )
        scene.idle(60)
        GeneratedPonderSupport.showText(
            scene,
            "Right-click to open assembling menu",
            Vec3(3.5, 2.0, 3.5),
            40,
            null,
            true
        )
        scene.idle(20)
        GeneratedPonderSupport.showControls(
            scene, Vec3(3.5, 2.0, 3.5), "right", 20, "right", null, null,
            whileSneaking = false,
            whileCtrl = false
        )
        scene.idle(60)
        GeneratedPonderSupport.showText(
            scene,
            "You can craft items when you have enough materials in inventory",
            Vec3(3.5, 2.0, 3.5),
            60,
            null,
            true
        )
        scene.idle(60)
    }

    private fun interactScene(scene: SceneBuilder, util: SceneBuildingUtil) {
        scene.title("vehicle_assembling_table_interaction", "Interact?")
        val context: GeneratedPonderSupport.Context = GeneratedPonderSupport.Context()
        GeneratedPonderSupport.preScanBounds(scene, BlockPos(2, 1, 3), BlockPos(3, 2, 4))
        GeneratedPonderSupport.showStructure(scene, context, null, null, null, null)
        GeneratedPonderSupport.setBlock(
            scene,
            context,
            "superbwarfare:vehicle_assembling_table",
            null,
            BlockPos(3, 1, 3),
            null,
            null,
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
        GeneratedPonderSupport.showText(scene, "Right-click with a crowbar...", Vec3(3.5, 2.0, 3.5), 40, null, true)
        scene.idle(20)
        GeneratedPonderSupport.showControls(
            scene,
            Vec3(3.5, 2.0, 3.5),
            "right",
            40,
            "right",
            "superbwarfare:crowbar",
            null,
            whileSneaking = false,
            whileCtrl = false
        )
        scene.idle(20)
        GeneratedPonderSupport.destroyBlock(scene, context, BlockPos(3, 1, 3), BlockPos(2, 2, 4), null)
        GeneratedPonderSupport.createEntity(
            scene,
            context,
            "superbwarfare:vehicle_assembling_table",
            Vec3(3.0, 1.1, 4.0),
            null,
            null,
            null,
            null,
            "1",
            null,
            null,
            null
        )
        scene.idle(20)
    }
}
