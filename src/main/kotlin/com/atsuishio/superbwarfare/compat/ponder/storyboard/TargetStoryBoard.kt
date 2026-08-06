package com.atsuishio.superbwarfare.compat.ponder.storyboard

import net.createmod.ponder.api.scene.SceneBuilder
import net.createmod.ponder.api.scene.SceneBuildingUtil
import net.minecraft.core.Direction

object TargetStoryBoard {
    fun infoScene(scene: SceneBuilder, util: SceneBuildingUtil) {
        scene.configureBasePlate(0, 0, 7)
        scene.world().showSection(util.select().everywhere(), Direction.UP)
        scene.markAsFinished()
    }
}