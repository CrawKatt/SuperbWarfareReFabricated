package com.atsuishio.superbwarfare.command.builder

import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.commands.arguments.EntityArgument
import net.minecraft.server.level.ServerPlayer

class CommandNodeWithPlayersArg(builder: ArgumentBuilder<CommandSourceStack, *>, argName: String) :
    CommandNodeWithArg<Collection<ServerPlayer>>(builder, argName) {

    val CommandContext<CommandSourceStack>.playersArg get() = getArg(this@CommandNodeWithPlayersArg)

    override fun CommandContext<CommandSourceStack>.getArg(
        ctx: CommandNodeWithArg<Collection<ServerPlayer>>
    ): Collection<ServerPlayer> =
        EntityArgument.getPlayers(this, ctx.name)
}

inline fun CommandNode.playersArg(argName: String = "$name.players", builder: CommandNodeWithPlayersArg.() -> Unit) {
    cmd += CommandNodeWithPlayersArg(Commands.argument(argName, EntityArgument.players()), argName).apply(builder)
}
