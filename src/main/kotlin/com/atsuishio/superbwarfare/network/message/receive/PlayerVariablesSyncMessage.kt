package com.atsuishio.superbwarfare.network.message.receive

import com.atsuishio.superbwarfare.data.gun.Ammo
import com.atsuishio.superbwarfare.init.ModComponents
import com.atsuishio.superbwarfare.ksp.annotation.RegisterPacket
import com.atsuishio.superbwarfare.network.ClientPacketPayload
import com.atsuishio.superbwarfare.network.PayloadContext
import com.atsuishio.superbwarfare.tools.clientLevel
import kotlinx.serialization.Serializable

@Serializable
@RegisterPacket
data class PlayerVariablesSyncMessage(
    val target: Int,
    val data: Map<Byte, Int>,
) : ClientPacketPayload() {

    override fun PayloadContext.handler() {
        val entity = clientLevel?.getEntity(target) ?: return

        val variables = ModComponents.PLAYER_VARIABLE.get(entity)

        for ((type, value) in data) {
            when (type) {
                (-1).toByte() -> {
                    variables.activeThermalImaging = value == 1
                }

                else -> {
                    val types = Ammo.entries.toTypedArray()
                    if (type >= 0 && type < types.size) {
                        types[type.toInt()].set(variables, value)
                    }
                }
            }
        }

        variables.sync(entity)
    }
}
