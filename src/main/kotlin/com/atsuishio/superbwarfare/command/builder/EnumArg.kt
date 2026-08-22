package com.atsuishio.superbwarfare.command.builder

import com.atsuishio.superbwarfare.command.LowerCamelCaseEnumArgument
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import kotlin.reflect.KClass

class CommandNodeWithEnumArg<T : Enum<T>>(
    builder: ArgumentBuilder<CommandSourceStack, *>,
    argName: String,
    val type: KClass<T>
) : CommandNodeWithArg<T>(builder, argName) {

    val CommandContext<CommandSourceStack>.enumArg: T get() = getArgument(name, type.java)

    override fun CommandContext<CommandSourceStack>.getArg(ctx: CommandNodeWithArg<T>): T =
        getArgument(ctx.name, type.java)
}

inline fun <reified T : Enum<T>> CommandNode.enumArg(
    argName: String = "$name.${T::class.simpleName}",
    noinline builder: CommandNodeWithEnumArg<T>.() -> Unit
) {
    cmd += CommandNodeWithEnumArg(
        Commands.argument(argName, LowerCamelCaseEnumArgument.enumArgument(T::class.java)),
        argName,
        T::class
    ).apply(builder)
}
