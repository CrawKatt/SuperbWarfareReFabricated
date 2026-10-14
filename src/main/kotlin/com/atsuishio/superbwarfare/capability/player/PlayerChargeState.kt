package com.atsuishio.superbwarfare.capability.player

import com.atsuishio.superbwarfare.Mod.Companion.loc
import com.atsuishio.superbwarfare.init.ModDataAttachments
import kotlinx.serialization.Serializable
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.Entity

// 刺刀冲锋状态：冲锋者的客户端在本地开/关的那一刻发报文，服务端照写；给别人算姿势用。不落盘
@Serializable
data class PlayerChargeState(
    val charging: Boolean = false,
) {
    companion object {
        val ID: ResourceLocation = loc("player_charge_state")

        @JvmStatic
        fun getOrDefault(entity: Entity): PlayerChargeState = entity.getData(ModDataAttachments.PLAYER_CHARGE_STATE)

        // 渲染路径每帧都会问，不走会新造默认值的那条路
        @JvmStatic
        fun isCharging(entity: Entity): Boolean =
            entity.getData(ModDataAttachments.PLAYER_CHARGE_STATE).charging

        @JvmStatic
        fun modify(entity: Entity, mapper: (PlayerChargeState) -> PlayerChargeState) {
            entity.setData(ModDataAttachments.PLAYER_CHARGE_STATE, mapper(getOrDefault(entity)))
        }
    }
}
