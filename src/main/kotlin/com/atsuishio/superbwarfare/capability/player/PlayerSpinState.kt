package com.atsuishio.superbwarfare.capability.player

import com.atsuishio.superbwarfare.Mod.Companion.loc
import com.atsuishio.superbwarfare.init.ModDataAttachments
import kotlinx.serialization.Serializable
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.Entity

@Serializable
data class PlayerSpinState(
    val spinning: Boolean = false,
) {
    companion object {
        val ID: ResourceLocation = loc("player_spin_state")

        @JvmStatic
        fun isSpinning(entity: Entity): Boolean = entity.getData(ModDataAttachments.PLAYER_SPIN_STATE).spinning

        @JvmStatic
        fun set(entity: Entity, spinning: Boolean) =
            entity.setData(ModDataAttachments.PLAYER_SPIN_STATE, PlayerSpinState(spinning))
    }
}
