package com.atsuishio.superbwarfare.compat.ponder.scene

import com.atsuishio.superbwarfare.Mod.Companion.loc
import com.atsuishio.superbwarfare.compat.ponder.GeneratedPonderSupport
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper
import net.createmod.ponder.api.scene.SceneBuilder
import net.createmod.ponder.api.scene.SceneBuildingUtil
import net.minecraft.core.BlockPos
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.phys.Vec3

object JumpPadPonderScene {
    fun register(helper: PonderSceneRegistrationHelper<ResourceLocation>) {
        helper.forComponents(loc("jump_pad"))
            .addStoryBoard("basic_5x5", JumpPadPonderScene::introScene)
    }

    private fun introScene(scene: SceneBuilder, util: SceneBuildingUtil) {
        scene.title("jump_pad_intro", "Jump Pad Introduction")
        val context = GeneratedPonderSupport.Context()
        GeneratedPonderSupport.preScanBounds(scene, BlockPos(2, 1, 2), BlockPos(2, 1, 2))
        scene.addKeyframe()
        GeneratedPonderSupport.showStructure(scene, context, null, null, null, null)
        scene.idle(20)
        GeneratedPonderSupport.setBlock(scene, context, "superbwarfare:jump_pad", mapOf("waterlogged" to "false", "facing" to "south", "activated" to "false"), BlockPos(2, 1, 2), null, null, false, false, "down", 20, 1, false, null, "down")
        scene.addKeyframe()
        GeneratedPonderSupport.showText(scene, "The Jump Pad is a functional block that automatically launches players, vehicles, or mobs on top of it to a certain height", Vec3(2.5, 1.0, 2.5), 60, null, true)
        scene.idle(80)
        scene.addKeyframe()
        GeneratedPonderSupport.showText(scene, "Step onto the Jump Pad while sneaking to launch in the direction the player is facing", Vec3(2.5, 1.0, 2.5), 60, null, true)
        scene.idle(80)
        GeneratedPonderSupport.createItemEntity(scene, context, "minecraft:apple", 5, Vec3(2.5, 5.0, 2.5), Vec3(0.0, 0.0, 0.0), null, null)
        scene.addKeyframe()
        GeneratedPonderSupport.showText(scene, "If a player is launched, pressing Space in mid-air dashes toward the crosshair (Non terrae plus ultra)", Vec3(2.5, 3.0, 2.5), 60, null, true)
        scene.idle(80)
    }
}

