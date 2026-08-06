package com.atsuishio.superbwarfare.compat.ponder.scene

import com.atsuishio.superbwarfare.Mod.Companion.loc
import com.atsuishio.superbwarfare.compat.ponder.storyboard.TargetStoryBoard
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper
import net.minecraft.resources.ResourceLocation

object TargetPonderScene {
    fun register(helper: PonderSceneRegistrationHelper<ResourceLocation>) {
        helper.forComponents(loc("target_deployer"))
            .addStoryBoard("basic_5x5", TargetStoryBoard::infoScene)
            .addStoryBoard("basic_5x5", TargetStoryBoard::interactScene)
    }
}