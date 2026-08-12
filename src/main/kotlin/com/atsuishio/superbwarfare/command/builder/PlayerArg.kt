package com.atsuishio.superbwarfare.command.builder

import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.commands.arguments.EntityArgument
import net.minecraft.server.level.ServerPlayer

class CommandNodeWithPlayerArg(builder: ArgumentBuilder<CommandSourceStack, *>, argName: String) :
    CommandNodeWithArg<ServerPlayer>(builder, argName) {

    val CommandContext<CommandSourceStack>.playerArg get() = getArg(this@CommandNodeWithPlayerArg)

    override fun CommandContext<CommandSourceStack>.getArg(ctx: CommandNodeWithArg<ServerPlayer>): ServerPlayer =
        EntityArgument.getPlayer(this, ctx.name)
}

inline fun CommandNode.playerArg(argName: String = "$name.player", builder: CommandNodeWithPlayerArg.() -> Unit) {
    cmd += CommandNodeWithPlayerArg(Commands.argument(argName, EntityArgument.player()), argName).apply(builder)
}
