package com.atsuishio.superbwarfare.event

import com.atsuishio.superbwarfare.Mod
import com.atsuishio.superbwarfare.capability.player.PlayerChargeState
import com.atsuishio.superbwarfare.data.gun.BayonetChargeInfo
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.player.Player

// 冲锋状态的服务端一半：客户端在本地开/关的那一刻发报文过来，这里只负责照写 + 加/撤移速
object ChargeWindowHandler {

    private val SPEED_MODIFIER = Mod.loc("bayonet_speed_modifier")

    // 移速加成和原版迅捷同源：等级 × 20%
    fun start(player: Player, charge: BayonetChargeInfo) {
        PlayerChargeState.modify(player) { it.copy(charging = true) }
        player.getAttribute(Attributes.MOVEMENT_SPEED)?.addTransientModifier(
            AttributeModifier(
                SPEED_MODIFIER,
                0.2 * (charge.speed + 1), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
            )
        )
    }

    fun clear(player: Player) {
        PlayerChargeState.modify(player) { it.copy(charging = false) }
        player.getAttribute(Attributes.MOVEMENT_SPEED)?.removeModifier(SPEED_MODIFIER)
    }
}
