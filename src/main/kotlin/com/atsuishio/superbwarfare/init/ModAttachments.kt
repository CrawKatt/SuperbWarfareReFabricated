package com.atsuishio.superbwarfare.init

import com.atsuishio.superbwarfare.capability.living.PhosphorusFireCapability
import com.atsuishio.superbwarfare.capability.player.PlayerVariable
import org.ladysnake.cca.api.v3.component.ComponentKey

object ModAttachments {
    @JvmField
    val PLAYER_VARIABLE: ComponentKey<PlayerVariable> = ModComponents.PLAYER_VARIABLE

    @JvmField
    val PHOSPHORUS_FIRE: ComponentKey<PhosphorusFireCapability> = ModComponents.PHOSPHORUS_FIRE
}
