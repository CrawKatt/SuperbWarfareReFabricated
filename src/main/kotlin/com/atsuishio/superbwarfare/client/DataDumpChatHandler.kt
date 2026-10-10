package com.atsuishio.superbwarfare.client

import com.atsuishio.superbwarfare.debug.DataDump
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents
import net.minecraft.network.chat.ClickEvent
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.HoverEvent
import net.minecraft.network.chat.contents.TranslatableContents

/**
 * 把 `/sbw data dump` 输出里的导出目录变成**可点击打开**的链接
 */
object DataDumpChatHandler {

    private const val PATH_KEY = "commands.superbwarfare.data.path"
    private const val PATH_HOVER_KEY = "commands.superbwarfare.data.path.hover"

    @JvmStatic
    fun register() {
        ClientReceiveMessageEvents.MODIFY_GAME.register { message, _ -> onSystemChat(message) }
    }

    private fun onSystemChat(message: Component): Component {
        // 本地没有导出目录（专用服务端的路径）就什么都不做
        val directory = DataDump.dumpDirectory()
        if (!directory.isDirectory) return message

        val path = directory.absolutePath
        if (!message.string.contains(path)) return message

        val click = ClickEvent(ClickEvent.Action.OPEN_FILE, path)
        val hover = HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable(PATH_HOVER_KEY))

        return attach(message, path, click, hover)
    }

    /**
     * 递归找到路径所在的那个组件，把点击/悬停挂上去，其余节点原样保留
     */
    private fun attach(
        component: Component,
        path: String,
        click: ClickEvent,
        hover: HoverEvent
    ): Component {
        if (isPathText(component, path)) {
            return component.copy().withStyle { style ->
                style.withClickEvent(click).withHoverEvent(hover)
            }
        }

        if (component.siblings.isEmpty()) return component

        return component.copy().also { root ->
            root.siblings.replaceAll { sibling -> attach(sibling, path, click, hover) }
        }
    }

    /** 这个组件是不是那条路径文本本身 */
    private fun isPathText(component: Component, path: String): Boolean =
        (component.contents as? TranslatableContents)?.key == PATH_KEY && component.string.contains(path)
}
