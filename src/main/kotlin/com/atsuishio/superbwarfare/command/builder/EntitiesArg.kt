package com.atsuishio.superbwarfare.command.builder

import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.commands.arguments.EntityArgument
import net.minecraft.world.entity.Entity

class CommandNodeWithEntitiesArg(builder: ArgumentBuilder<CommandSourceStack, *>, argName: String) :
    CommandNodeWithArg<Collection<Entity>>(builder, argName) {

    val CommandContext<CommandSourceStack>.entities get() = getArg(this@CommandNodeWithEntitiesArg)

    override fun CommandContext<CommandSourceStack>.getArg(
        ctx: CommandNodeWithArg<Collection<Entity>>
    ): Collection<Entity> =
        EntityArgument.getEntities(this, ctx.name)
}

inline fun CommandNode.entitiesArg(argName: String = "$name.entities", builder: CommandNodeWithEntitiesArg.() -> Unit) {
    cmd += CommandNodeWithEntitiesArg(Commands.argument(argName, EntityArgument.entities()), argName).apply(builder)
}
