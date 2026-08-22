package com.atsuishio.superbwarfare.command

import com.atsuishio.superbwarfare.command.builder.buildCommand
import com.atsuishio.superbwarfare.command.builder.entityArg
import com.atsuishio.superbwarfare.command.builder.stringArg
import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.Entity

val SKIN_COMMAND = buildCommand("skin") {
    requirePermission(2)

    entityArg("vehicle") {
        "set" {
            stringArg("skinId") {
                execute {
                    val (res, msg) = setSkin(entity, stringArg)
                    if (res) success { msg } else fail { msg }
                }
            }
        }

        "clear" {
            execute {
                val vehicle = entity as? VehicleEntity
                    ?: fail { Component.translatable("commands.superbwarfare.skin.fail.vehicle") }
                vehicle.skinId = ""
                success { Component.translatable("commands.superbwarfare.skin.success.clear", entity.displayName) }
            }
        }
    }
}

private fun setSkin(entity: Entity, skinId: String): Pair<Boolean, Component> {
    val vehicle = entity as? VehicleEntity
        ?: return false to Component.translatable("commands.superbwarfare.skin.fail.vehicle")
    if (skinId.isBlank()) {
        return false to Component.translatable("commands.superbwarfare.skin.fail.empty")
    }
    vehicle.skinId = skinId
    return true to Component.translatable("commands.superbwarfare.skin.success.set", entity.displayName, skinId)
}
