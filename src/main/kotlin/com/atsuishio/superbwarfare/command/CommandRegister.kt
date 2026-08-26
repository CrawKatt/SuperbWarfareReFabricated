package com.atsuishio.superbwarfare.command

import com.atsuishio.superbwarfare.command.builder.buildCommand
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands

object CommandRegister {
    @JvmStatic
    fun register() {
        CommandRegistrationCallback.EVENT.register { dispatcher, registryAccess, environment ->
            val command = buildCommand("sbw") {
                add(AMMO_COMMAND)
                add(CONFIG_COMMAND)
                add(TDM_COMMAND)
                add(RIDE_COMMAND)
                add(DISMOUNT_COMMAND)
                add(SKIN_COMMAND)
                add(LOITER_COMMAND)
            }

            val result = dispatcher.register(command as LiteralArgumentBuilder<CommandSourceStack>)
            dispatcher.register(Commands.literal("superbwarfare").redirect(result))
        }
    }
}