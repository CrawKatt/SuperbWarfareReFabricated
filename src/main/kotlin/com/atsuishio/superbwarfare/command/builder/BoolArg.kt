package com.atsuishio.superbwarfare.command.builder

import com.mojang.brigadier.arguments.BoolArgumentType
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands

class CommandNodeWithBoolArg(builder: ArgumentBuilder<CommandSourceStack, *>, argName: String) :
    CommandNodeWithArg<Boolean>(builder, argName) {

    val CommandContext<CommandSourceStack>.boolArg get() = getArg(this@CommandNodeWithBoolArg)

    override fun CommandContext<CommandSourceStack>.getArg(ctx: CommandNodeWithArg<Boolean>) =
        BoolArgumentType.getBool(this, ctx.name)
}

inline fun CommandNode.boolArg(argName: String = "$name.bool", builder: CommandNodeWithBoolArg.() -> Unit) {
    cmd += CommandNodeWithBoolArg(Commands.argument(argName, BoolArgumentType.bool()), argName).apply(builder)
}
