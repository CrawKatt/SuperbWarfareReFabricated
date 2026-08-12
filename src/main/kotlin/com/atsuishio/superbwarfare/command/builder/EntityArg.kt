package com.atsuishio.superbwarfare.command.builder

import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.commands.arguments.EntityArgument
import net.minecraft.world.entity.Entity

class CommandNodeWithEntityArg(builder: ArgumentBuilder<CommandSourceStack, *>, argName: String) :
    CommandNodeWithArg<Entity>(builder, argName) {

    val CommandContext<CommandSourceStack>.entity get() = getArg(this@CommandNodeWithEntityArg)

    override fun CommandContext<CommandSourceStack>.getArg(ctx: CommandNodeWithArg<Entity>): Entity =
        EntityArgument.getEntity(this, ctx.name)
}

inline fun CommandNode.entityArg(argName: String = "$name.entity", builder: CommandNodeWithEntityArg.() -> Unit) {
    cmd += CommandNodeWithEntityArg(Commands.argument(argName, EntityArgument.entity()), argName).apply(builder)
}
