package com.atsuishio.superbwarfare.client.screens

import com.atsuishio.superbwarfare.Mod.Companion.loc
import com.atsuishio.superbwarfare.client.RenderHelper
import com.atsuishio.superbwarfare.data.vehicle_skin.SkinInfo
import com.atsuishio.superbwarfare.data.vehicle_skin.VehicleSkin
import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity
import com.atsuishio.superbwarfare.network.message.send.SetVehicleSkinMessage
import com.atsuishio.superbwarfare.tools.RenderDistanceHelper
import com.atsuishio.superbwarfare.tools.mc
import com.atsuishio.superbwarfare.tools.options
import com.atsuishio.superbwarfare.tools.sendPacketToServer
import com.mojang.blaze3d.platform.Lighting
import com.mojang.math.Axis
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractButton
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.Entity
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
class VehicleSkinScreen(private val entity: Entity) : Screen(Component.empty()) {
    companion object {
        private val TEXTURE = loc("textures/gui/vehicle_skin.png")
        private val BUTTON = loc("textures/gui/vehicle_skin_button.png")

        const val BUTTON_WIDTH = 142
        const val BUTTON_HEIGHT = 94
        const val BUTTON_SIZE = 256

        const val IMAGE_WIDTH = 320
        const val IMAGE_HEIGHT = 200
        const val IMAGE_SIZE = 328

        const val PAGE_SIZE = 4
    }

    private val previewEntities = mutableMapOf<String, VehicleEntity>()
    var currentPage = 0
    var maxPage = 0

    override fun isPauseScreen(): Boolean {
        return false
    }

    override fun init() {
        super.init()
        this.clearWidgets()
        previewEntities.clear()
        this.registerButtons()
    }

    fun registerButtons() {
        val vehicle = entity as? VehicleEntity ?: return
        val currentSkinId = vehicle.skinId
        val vehicleType = vehicle.type

        val skinEntries = mutableListOf<Pair<String, SkinInfo?>>()
        skinEntries.add("" to null)

        val skinData = VehicleSkin.getSkins(vehicleType)
        skinEntries.addAll(
            skinData.skins
                .filter { it.id != "vanilla" }
                .map { it.id to it }
        )

        val clientLevel = mc.level ?: return
        for ((skinId, _) in skinEntries) {
            val previewEntity = vehicleType.create(clientLevel) as? VehicleEntity ?: continue
            previewEntity.skinId = skinId
            previewEntities[skinId] = previewEntity
        }

        val pages = (skinEntries.size + PAGE_SIZE - 1) / PAGE_SIZE
        this.maxPage = if (pages > 0) pages - 1 else 0

        val i = (this.width - IMAGE_WIDTH) / 2
        val j = (this.height - IMAGE_HEIGHT) / 2

        val startX = i + 4
        val startY = j + 4

        for ((index, entry) in skinEntries.withIndex()) {
            if ((index + 1) !in this.currentPage * 4..this.currentPage * 4 + 4) continue

            val (skinId, skinInfo) = entry
            val offset = index % 4
            val x = startX + (BUTTON_WIDTH + 2) * (offset % 2)
            val y = startY + (BUTTON_HEIGHT + 2) * (offset / 2)

            val isSelected = if (skinId.isBlank()) {
                currentSkinId.isBlank()
            } else {
                currentSkinId == skinId
            }

            this.addRenderableWidget(
                SkinSlotButton(
                    x, y,
                    skinId, skinInfo,
                    previewEntities[skinId],
                    entity.id,
                    isSelected
                )
            )
        }

        this.addRenderableWidget(PageButton(i + 296, j + 4, true))
        this.addRenderableWidget(PageButton(i + 296, j + 102, false))
    }

    override fun render(pGuiGraphics: GuiGraphics, pMouseX: Int, pMouseY: Int, pPartialTick: Float) {
        this.renderBackground(pGuiGraphics)
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick)
    }

    override fun renderBackground(pGuiGraphics: GuiGraphics) {
        super.renderBackground(pGuiGraphics)
        val i = (this.width - IMAGE_WIDTH) / 2
        val j = (this.height - IMAGE_HEIGHT) / 2
        pGuiGraphics.blit(TEXTURE, i, j, 0f, 0f, IMAGE_WIDTH, IMAGE_HEIGHT, IMAGE_SIZE, IMAGE_SIZE)
    }

    override fun removed() {
        super.removed()
        previewEntities.values.forEach { it.discard() }
        previewEntities.clear()
        this.currentPage = 0
        this.maxPage = 0
    }

    override fun keyPressed(pKeyCode: Int, pScanCode: Int, pModifiers: Int): Boolean {
        if (pKeyCode == options.keyInventory.key.value) {
            this.onClose()
            return true
        }
        return super.keyPressed(pKeyCode, pScanCode, pModifiers)
    }

    fun onPageChanged() {
        this.clearWidgets()
        this.registerButtons()
    }

    @OnlyIn(Dist.CLIENT)
    private inner class SkinSlotButton(
        x: Int, y: Int,
        private val skinId: String,
        private val skinInfo: SkinInfo?,
        private val previewEntity: VehicleEntity?,
        private val vehicleEntityId: Int,
        private val isSelected: Boolean
    ) : AbstractButton(x, y, BUTTON_WIDTH, BUTTON_HEIGHT, Component.empty()) {

        override fun renderWidget(graphics: GuiGraphics, pMouseX: Int, pMouseY: Int, pPartialTick: Float) {
            val pose = graphics.pose()

            if (this.isHovered || this.isSelected) {
                graphics.blit(BUTTON, x, y, 0f, 0f, this.width, this.height, BUTTON_SIZE, BUTTON_SIZE)
            } else {
                graphics.blit(BUTTON, x, y, 0f, 95f, this.width, this.height, BUTTON_SIZE, BUTTON_SIZE)
            }

            // 载具皮肤模型
            if (previewEntity != null) {
                RenderDistanceHelper.markGuiRenderTimestamp()

                pose.pushPose()

                val centerX = this.x + this.width / 2.0
                val centerY = this.y + this.height - 20.0
                pose.translate(centerX, centerY, 80.0)

                val bbSize = previewEntity.boundingBox.size.toFloat()
                val scale = (this.width * 0.3f) / maxOf(bbSize, 1f)
                pose.scale(scale, scale, -scale)

                pose.mulPose(Axis.XP.rotationDegrees(195f))
                pose.mulPose(Axis.YP.rotationDegrees(30f))

                Lighting.setupForEntityInInventory()
                val erd = mc.entityRenderDispatcher
                erd.setRenderShadow(false)
                erd.render(
                    previewEntity,
                    0.0, 0.0, 0.0,
                    0f, 1f,
                    pose,
                    graphics.bufferSource(),
                    15728880
                )
                graphics.flush()
                erd.setRenderShadow(true)
                Lighting.setupFor3DItems()

                pose.popPose()
            }

            // 皮肤信息
            val textY = this.y + this.height - 35
            val displayName = skinInfo?.name ?: "Vanilla"
            val displayId = skinId.ifBlank { "vanilla" }
            val description = skinInfo?.description ?: ""

            val font = this@VehicleSkinScreen.font
            val textColor = if (isSelected) 0xFFD700 else 0xFFFFFF

            val nameText = font.plainSubstrByWidth(displayName, this.width - 4)
            val idText = font.plainSubstrByWidth(displayId, this.width - 4)

            pose.pushPose()
            pose.translate(0.0, 0.0, 200.0)

            RenderHelper.renderCenteredScrollingString(
                graphics, font, Component.literal(nameText), 1f,
                this.x + 1, this.y + 2, this.x + this.width - 1, this.y + 13, textColor
            )

            RenderHelper.renderCenteredScrollingString(
                graphics, font, Component.literal(idText), 1f,
                this.x + 1, textY + 12, this.x + this.width - 1, textY + 23, 0xAAAAAA
            )

            if (description.isNotBlank()) {
                val des = if (description.startsWith("Component#")) {
                    Component.translatable(description.substringAfter("Component#"))
                } else {
                    Component.literal(description)
                }

                RenderHelper.renderCenteredScrollingString(
                    graphics, font, des, 1f,
                    this.x + 1, textY + 23, this.x + this.width - 1, textY + 34, 0x888888
                )
            }

            pose.popPose()
        }

        override fun onPress() {
            if (!this.isActive) return
            sendPacketToServer(SetVehicleSkinMessage(vehicleEntityId, skinId))
            this@VehicleSkinScreen.onClose()
        }

        override fun updateWidgetNarration(pNarrationElementOutput: NarrationElementOutput) {
        }
    }

    @OnlyIn(Dist.CLIENT)
    private inner class PageButton(
        x: Int,
        y: Int,
        val forward: Boolean
    ) : AbstractButton(x, y, 20, 94, Component.empty()) {
        override fun onPress() {
            val page = this@VehicleSkinScreen.currentPage
            val maxPage = this@VehicleSkinScreen.maxPage
            if (this.forward) {
                this@VehicleSkinScreen.currentPage = (page - 1).coerceIn(0, maxPage)
            } else {
                this@VehicleSkinScreen.currentPage = (page + 1).coerceIn(0, maxPage)
            }
            this@VehicleSkinScreen.onPageChanged()
        }

        override fun updateWidgetNarration(pNarrationElementOutput: NarrationElementOutput) {
        }

        override fun renderWidget(
            pGuiGraphics: GuiGraphics,
            pMouseX: Int,
            pMouseY: Int,
            pPartialTick: Float
        ) {
            val uOffset = if (this.forward) 143f else 164f
            val vOffset = if (this.isHovered) 0f else 95f
            pGuiGraphics.blit(BUTTON, this.x, this.y, uOffset, vOffset, 20, 94, BUTTON_SIZE, BUTTON_SIZE)
        }
    }
}
