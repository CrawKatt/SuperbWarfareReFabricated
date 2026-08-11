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
        scene.title("jump_pad", "Scene 1")
        val context = GeneratedPonderSupport.Context()
        GeneratedPonderSupport.preScanBounds(scene, BlockPos(2, 1, 2), BlockPos(2, 1, 2))
        scene.addKeyframe()
        GeneratedPonderSupport.showStructure(scene, context, null, null, null, null)
        scene.idle(20)
        GeneratedPonderSupport.setBlock(scene, context, "superbwarfare:jump_pad", mapOf("waterlogged" to "false", "facing" to "south", "activated" to "false"), BlockPos(2, 1, 2), null, null, false, false, "down", 20, 1, false, null, "down")
        scene.addKeyframe()
        GeneratedPonderSupport.showText(scene, "弹射台是一种功能性方块，可以自动将在上面的玩家，载具或者生物弹射到一定高度", Vec3(2.5, 1.0, 2.5), 60, null, true)
        scene.idle(80)
        scene.addKeyframe()
        GeneratedPonderSupport.showText(scene, "潜行时踏上弹射台可以朝着玩家面朝方向弹射", Vec3(2.5, 1.0, 2.5), 60, null, true)
        scene.idle(80)
        GeneratedPonderSupport.createItemEntity(scene, context, "minecraft:apple", 5, Vec3(2.5, 5.0, 2.5), Vec3(0.0, 0.0, 0.0), null, null)
        scene.addKeyframe()
        GeneratedPonderSupport.showText(scene, "如果弹射的是玩家，在空中的时候可以按空格往光标方向前进（Non terrae plus ultra）", Vec3(2.5, 3.0, 2.5), 60, null, true)
        scene.idle(80)
    }


}
