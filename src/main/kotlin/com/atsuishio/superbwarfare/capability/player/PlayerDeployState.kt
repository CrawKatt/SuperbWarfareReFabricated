package com.atsuishio.superbwarfare.capability.player

import com.atsuishio.superbwarfare.Mod.Companion.loc
import com.atsuishio.superbwarfare.init.ModComponents
import com.atsuishio.superbwarfare.serialization.kserializer.SerializedBlockPos
import com.atsuishio.superbwarfare.serialization.kserializer.SerializedUUID
import kotlinx.serialization.Serializable
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.Entity
import java.util.function.Function

@Serializable
data class PlayerDeployState(
    val tripodPos: SerializedBlockPos? = null,
    val gunUuid: SerializedUUID? = null
) {

    companion object {
        val ID: ResourceLocation = loc("player_deploy_state")

        @JvmStatic
        fun getOrDefault(entity: Entity): PlayerDeployState {
            return ModComponents.PLAYER_DEPLOY_STATE.get(entity).value
        }

        @JvmStatic
        fun modify(entity: Entity, mapper: Function<PlayerDeployState, PlayerDeployState>) {
            val state = getOrDefault(entity)
            val new = mapper.apply(state)

            if (new != state) {
                ModComponents.PLAYER_DEPLOY_STATE.get(entity).value = new
                if (!entity.level().isClientSide) ModComponents.PLAYER_DEPLOY_STATE.sync(entity)
            }
        }
    }
}
