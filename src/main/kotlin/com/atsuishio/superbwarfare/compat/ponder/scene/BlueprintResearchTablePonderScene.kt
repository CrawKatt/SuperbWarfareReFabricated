package com.atsuishio.superbwarfare.compat.ponder.scene

import com.atsuishio.superbwarfare.Mod.Companion.loc
import com.atsuishio.superbwarfare.compat.ponder.GeneratedPonderSupport
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper
import net.createmod.ponder.api.scene.SceneBuilder
import net.createmod.ponder.api.scene.SceneBuildingUtil
import net.minecraft.core.BlockPos
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.phys.Vec3

object BlueprintResearchTablePonderScene {
    fun register(helper: PonderSceneRegistrationHelper<ResourceLocation>) {
        helper.forComponents(loc("blueprint_research_table"))
            .addStoryBoard("basic_7x7", BlueprintResearchTablePonderScene::introScene)
    }

    private fun introScene(scene: SceneBuilder, util: SceneBuildingUtil) {
        scene.title("blueprint_research_table_intro", "Blueprint Research Table Introduction")
        val context = GeneratedPonderSupport.Context()
        GeneratedPonderSupport.preScanBounds(scene, BlockPos(3, 1, 3), BlockPos(3, 1, 3))
        scene.addKeyframe()
        GeneratedPonderSupport.showStructure(scene, context, null, null, null, null)
        scene.idle(20)
        GeneratedPonderSupport.setBlock(scene, context, "superbwarfare:blueprint_research_table", mapOf("part" to "foot", "facing" to "west", "enabled" to "false"), BlockPos(3, 1, 3), null, "{Activated:0b,Crafting:0b,Fuel:0,Items:[],LastSelectedIndex:0,Tick:0}", false, false, "down", 20, 1, false, null, "down")
        scene.idle(20)
        scene.addKeyframe()
        GeneratedPonderSupport.showText(scene, "This is the Blueprint Research Table", Vec3(3.5, 2.0, 3.5), 60, null, true)
        scene.idle(80)
        scene.addKeyframe()
        GeneratedPonderSupport.showText(scene, "Perform blueprint research to obtain gun blueprints (it can also be used to craft weapon modules)", Vec3(3.5, 2.0, 3.5), 60, null, true)
        scene.idle(80)
    }
}

