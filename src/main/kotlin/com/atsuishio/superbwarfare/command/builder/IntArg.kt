package com.atsuishio.superbwarfare.command.builder

import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands

class CommandNodeWithIntArg(builder: ArgumentBuilder<CommandSourceStack, *>, argName: String) :
    CommandNodeWithArg<Int>(builder, argName) {

    val CommandContext<CommandSourceStack>.intArg get() = getArg(this@CommandNodeWithIntArg)

    override fun CommandContext<CommandSourceStack>.getArg(ctx: CommandNodeWithArg<Int>) =
        IntegerArgumentType.getInteger(this, ctx.name)
}

inline fun CommandNode.intArg(
    argName: String = "$name.int",
    min: Int = Int.MIN_VALUE,
    max: Int = Int.MAX_VALUE,
    builder: CommandNodeWithIntArg.() -> Unit
) {
    cmd += CommandNodeWithIntArg(Commands.argument(argName, IntegerArgumentType.integer(min, max)), argName)
        .apply(builder)
}
