package com.atsuishio.superbwarfare.network.message.send

import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity
import com.atsuishio.superbwarfare.network.PayloadContext
import com.atsuishio.superbwarfare.network.ServerPacketPayload

/**
 * 主驾驶双击卸载乘客键时发送，强制让除主驾驶以外的所有乘客离开载具。
 * 由客户端在检测到 0.5s 内双击卸载乘客键时发送。
 */
object VehicleUnloadPassengersMessage : ServerPacketPayload() {

    override fun PayloadContext.handler() {
        val player = sender()
        val vehicle = player.vehicle as? VehicleEntity ?: return

        // 仅主驾驶可以卸载乘客
        if (vehicle.firstPassenger != player) return

        // 收集除主驾驶以外的所有乘客并让其下车
        val passengers = vehicle.passengers.toList()
        for (passenger in passengers) {
            if (passenger != player) {
                passenger.stopRiding()
            }
        }
    }
}
