package com.atsuishio.superbwarfare.command.builder

import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.network.chat.Component

// success/fail 抛出此异常实现 execute 的早退，由 execute 捕获并转换为返回值
internal class EarlyCommandReturn(val result: Int) : RuntimeException() {
    override fun fillInStackTrace() = this
}

// 这才是真正的Builder！
open class CommandNode(val argumentBuilder: ArgumentBuilder<CommandSourceStack, *>, val name: String = "default") {
    val cmd: MutableList<CommandNode> = mutableListOf()

    fun execute(executor: CommandContext<CommandSourceStack>.() -> Int) {
        this.argumentBuilder.executes { ctx ->
            try {
                ctx.executor()
            } catch (e: EarlyCommandReturn) {
                e.result
            }
        }
    }

    fun requires(executor: CommandSourceStack.() -> Boolean) {
        this.argumentBuilder.requires(executor)
    }

    fun requirePermission(level: Int) = requires { hasPermission(level) }

    // literal命令
    inline operator fun String.invoke(builder: CommandNode.() -> Unit) {
        cmd += CommandNode(Commands.literal(this), "$name.$this").apply(builder)
    }

    // 直接添加已有的ArgumentBuilder
    fun add(argument: ArgumentBuilder<CommandSourceStack, *>) {
        cmd += CommandNode(argument, "$name.${cmd.size}")
    }

    // args
    inline fun <A> arg(argName: String = "$name.arg", type: ArgumentType<A>, builder: CommandNode.() -> Unit) {
        cmd += CommandNode(Commands.argument(argName, type), argName).apply(builder)
    }

    fun build(): ArgumentBuilder<CommandSourceStack, *> = run {
        cmd.map { it.build() }.forEach { this.argumentBuilder.then(it) }
        this.argumentBuilder
    }

    fun CommandContext<CommandSourceStack>.success(
        result: Int = 0,
        allowLogging: Boolean = true,
        msg: (() -> Component)? = null
    ): Nothing {
        msg?.let { source.sendSuccess(it, allowLogging) }
        throw EarlyCommandReturn(result)
    }

    fun CommandContext<CommandSourceStack>.fail(result: Int = 0, msg: (() -> Component)? = null): Nothing {
        msg?.let { source.sendFailure(it()) }
        throw EarlyCommandReturn(result)
    }
}

// 带参数命令的抽象基类，负责声明从命令上下文中取值的能力
abstract class CommandNodeWithArg<T>(
    argumentBuilder: ArgumentBuilder<CommandSourceStack, *>,
    name: String
) : CommandNode(argumentBuilder, name) {
    abstract fun CommandContext<CommandSourceStack>.getArg(ctx: CommandNodeWithArg<T>): T
}

fun buildCommand(name: String, builder: CommandNode.() -> Unit) =
    CommandNode(Commands.literal(name), name).apply(builder).build()