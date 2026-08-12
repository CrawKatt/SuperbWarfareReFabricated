package com.atsuishio.superbwarfare.command.builder

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands

class CommandNodeWithStringArg(argBuilder: ArgumentBuilder<CommandSourceStack, *>, argName: String) :
    CommandNodeWithArg<String>(argBuilder, argName) {

    val CommandContext<CommandSourceStack>.stringArg get() = getArg(this@CommandNodeWithStringArg)

    override fun CommandContext<CommandSourceStack>.getArg(ctx: CommandNodeWithArg<String>): String =
        StringArgumentType.getString(this, ctx.name)
}

inline fun CommandNode.stringArg(argName: String = "$name.string", builder: CommandNodeWithStringArg.() -> Unit) {
    cmd += CommandNodeWithStringArg(Commands.argument(argName, StringArgumentType.string()), argName).apply(builder)
}
