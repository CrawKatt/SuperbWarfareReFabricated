package com.atsuishio.superbwarfare.client

import com.atsuishio.superbwarfare.init.ModItems
import com.atsuishio.superbwarfare.item.curio.ParachuteItem
import com.atsuishio.superbwarfare.network.message.send.ParachuteVisibilityMessage
import com.atsuishio.superbwarfare.tools.mc
import com.atsuishio.superbwarfare.tools.sendPacketToServer
import dev.emi.trinkets.TrinketScreen
import dev.emi.trinkets.TrinketSlot
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents
import net.fabricmc.fabric.api.client.screen.v1.ScreenMouseEvents
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.gui.screens.inventory.InventoryScreen
import net.minecraft.network.chat.Component
import net.minecraft.world.inventory.Slot
import org.lwjgl.glfw.GLFW

object ParachuteVisibilityHandler {
    fun register() {
        ScreenEvents.AFTER_INIT.register { _, screen, _, _ ->
            if (screen !is InventoryScreen || screen !is TrinketScreen) return@register

            ScreenEvents.afterRender(screen).register { current, graphics, mouseX, mouseY, _ ->
                render(current, graphics, mouseX, mouseY)
            }
            ScreenMouseEvents.allowMouseClick(screen).register { current, _, _, button ->
                val slot = hoveredParachute(current)
                if (button != GLFW.GLFW_MOUSE_BUTTON_MIDDLE || slot == null) {
                    true
                } else {
                    val stack = slot.item
                    stack.orCreateTag.putBoolean(ParachuteItem.TAG_VISIBLE, !ParachuteItem.isVisible(stack))
                    sendPacketToServer(ParachuteVisibilityMessage)
                    false
                }
            }
        }
    }

    private fun render(screen: Screen, graphics: GuiGraphics, mouseX: Int, mouseY: Int) {
        val slot = hoveredParachute(screen) ?: return
        val trinketScreen = screen as TrinketScreen
        val x = trinketScreen.`trinkets$getX`() + slot.x
        val y = trinketScreen.`trinkets$getY`() + slot.y
        val visible = ParachuteItem.isVisible(slot.item)

        graphics.fill(x + 9, y + 9, x + 17, y + 17, -0x60000000)
        graphics.drawString(mc.font, if (visible) "V" else "X", x + 10, y + 9, if (visible) 0x55FF55 else 0xFF5555, false)
        graphics.renderTooltip(
            mc.font,
            Component.translatable("gui.superbwarfare.parachute_visibility", Component.translatable(if (visible) "options.on" else "options.off")),
            mouseX,
            mouseY
        )
    }

    private fun hoveredParachute(screen: Screen): Slot? {
        val slot = (screen as? TrinketScreen)?.`trinkets$getFocusedSlot`() ?: return null
        return slot.takeIf { it is TrinketSlot && it.item.`is`(ModItems.PARACHUTE) }
    }
}
