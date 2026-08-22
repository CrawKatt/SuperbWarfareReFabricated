package com.atsuishio.superbwarfare.client.overlay

import com.atsuishio.superbwarfare.Mod.Companion.loc
import com.atsuishio.superbwarfare.client.RenderHelper
import com.atsuishio.superbwarfare.client.language.ClientLanguageGetter
import com.atsuishio.superbwarfare.config.client.DisplayConfig
import com.atsuishio.superbwarfare.data.gun.Ammo
import com.atsuishio.superbwarfare.data.gun.AmmoConsumer.AmmoConsumeType
import com.atsuishio.superbwarfare.data.gun.GunData
import com.atsuishio.superbwarfare.data.gun.GunData.Companion.from
import com.atsuishio.superbwarfare.data.gun.GunProp
import com.atsuishio.superbwarfare.item.gun.GunItem
import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity
import com.atsuishio.superbwarfare.init.ModItems
import com.atsuishio.superbwarfare.init.ModKeyMappings
import com.atsuishio.superbwarfare.tools.FormatTool.format1DZZ
import com.sighs.apricityui.ApricityUI
import com.sighs.apricityui.init.Document
import net.minecraft.Util
import net.minecraft.client.Minecraft
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.Mth
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import net.minecraftforge.common.capabilities.ForgeCapabilities
import net.minecraftforge.common.util.NonNullFunction
import java.util.regex.Pattern
import kotlin.math.max

@OnlyIn(Dist.CLIENT)
object AmmoBarOverlay : CommonOverlay("ammo_bar") {

    private val LINE = loc("textures/overlay/ammo_bar/fire_mode/line.png")
    private val MOUSE = loc("textures/overlay/ammo_bar/fire_mode/mouse.png")
    private val CHOSEN = loc("textures/gui/attachment/chosen.png")
    private val NOT_CHOSEN = loc("textures/gui/attachment/not_chosen.png")
    private val AMMO_STACK = loc("textures/gui/attachment/ammo_stack.png")

    private val TO_RESOURCE_LOCATION =
        Util.memoize<String, ResourceLocation> { str -> loc("textures/overlay/ammo_bar/fire_mode/$str.png") }

    private const val AUI_AMMO_BAR_PATH = "superbwarfare/ammo_bar.html"

    /** The active AUI overlay document, if any. */
    private var auiDocument: Document? = null

    // --- Drag state (AUI events, listeners removed outside drag to avoid input interference) ---
    private var auiDragging = false
    private var dragStartMouseX = 0.0
    private var dragStartMouseY = 0.0
    private var dragStartRight = 0.0
    private var dragStartBottom = 0.0
    private var auiRight = 0.0
    private var auiBottom = 0.0
    // Stored listener refs for removal after drag
    private var dragMoveListener: java.util.function.Consumer<com.sighs.apricityui.event.Event>? = null
    private var dragUpListener: java.util.function.Consumer<com.sighs.apricityui.event.Event>? = null
    // Track reload/gun-switch state for animation triggers
    private var wasReloading = false
    private var lastGunIconSrc: String? = null
    private var scaleRestartFrames = 0  // countdown for restarting scale animation

    override fun shouldRender(): Boolean {
        val result = super.shouldRender() && DisplayConfig.AMMO_HUD.get()
        if (!result) {
            // Clean up AUI overlay when HUD should be hidden (F1, spectator, etc.)
            removeAUIOverlay()
        }
        return result
    }

    override fun RenderContext.render() {
        val stack = player.mainHandItem
        val vehicle = player.vehicle
        val item = stack.item
        if (item is GunItem && !(vehicle is VehicleEntity && vehicle.banHand(player))) {
            val data = from(stack)

            if (player.isCreative) {
                // Creative mode: use ApricityUI overlay
                renderWithAUI(data, player, screenWidth, screenHeight)
                return
            } else {
                // Survival mode: ensure AUI overlay is removed
                removeAUIOverlay()
            }

            val x = screenWidth + DisplayConfig.WEAPON_HUD_X_OFFSET.get()
            val y = screenHeight + DisplayConfig.WEAPON_HUD_Y_OFFSET.get()

            val poseStack = guiGraphics.pose()

            // 渲染图标
            guiGraphics.blit(
                item.getGunIcon(data),
                x - 135,
                y - 40,
                0f,
                0f,
                64,
                16,
                64,
                16
            )

            val font = Minecraft.getInstance().font

            // 渲染开火模式切换按键
            if (item !== ModItems.MINIGUN.get()) {
                val str = "[${ModKeyMappings.FIRE_MODE.key.displayName.string}]"
                guiGraphics.drawString(
                    font,
                    str,
                    (x - 100f) - font.width(str),
                    (y - 20).toFloat(),
                    0xFFFFFF,
                    false
                )
            }

            // 渲染开火模式
            var fireMode: ResourceLocation = getFireMode(data)

            val selectedFireMode = data.selectedFireMode.get()
            val fireModes = data.get(GunProp.AVAILABLE_FIRE_MODES)

            // 如果开火模式种类大于3，渲染开火模式信息
            if (DisplayConfig.ADVANCED_AMMO_HUD.get() && fireModes.size > 3) {
                guiGraphics.drawCenteredString(
                    font,
                    (selectedFireMode + 1).toString() + "/" + fireModes.size,
                    x - 75,
                    y - 20,
                    0xCCCCCC
                )
            }

            if (item === ModItems.MINIGUN.get()) {
                fireMode = MOUSE
                // 渲染加特林射速
                guiGraphics.drawString(
                    font,
                    data.get(GunProp.RPM).toString() + " RPM",
                    x - 111f,
                    (y - 20).toFloat(),
                    0xFFFFFF,
                    false
                )

                guiGraphics.blit(
                    fireMode,
                    x - 126,
                    y - 22,
                    0f,
                    0f,
                    12,
                    12,
                    12,
                    12
                )
            } else {
                guiGraphics.blit(
                    fireMode,
                    x - 95,
                    y - 21,
                    0f,
                    0f,
                    8,
                    8,
                    8,
                    8
                )
                guiGraphics.blit(
                    LINE,
                    x - 95,
                    y - 16,
                    0f,
                    0f,
                    8,
                    8,
                    8,
                    8
                )
            }

            // 如果弹药种类大于1，渲染弹种信息
            val size = data.get(GunProp.AMMO_CONSUMER).size
            if (DisplayConfig.ADVANCED_AMMO_HUD.get()
                && (size > 1 || size == 1 && data.selectedAmmoConsumer().type != AmmoConsumeType.PLAYER_AMMO)
            ) {
                // 如果当前弹药为物品，渲染备弹物品数量
                val ammoConsumer = data.selectedAmmoConsumer()
                RenderHelper.preciseBlit(
                    guiGraphics, AMMO_STACK,
                    (x - 62).toFloat(),
                    y - 20.5f,
                    0f,
                    0f,
                    24f,
                    8.5f,
                    24f,
                    24f
                )

                poseStack.pushPose()

                // 物品
                poseStack.translate((x - 57).toFloat(), (y - 21).toFloat(), 0f)
                poseStack.scale(0.75f, 0.75f, 1f)

                val consumerType = ammoConsumer.type
                val renderStackCount =
                    consumerType == AmmoConsumeType.ITEM || consumerType == AmmoConsumeType.PLAYER_AMMO
                if (renderStackCount) {
                    val ammoStack: ItemStack
                    if (consumerType == AmmoConsumeType.PLAYER_AMMO) {
                        val ammoType = ammoConsumer.playerAmmoType!!
                        ammoStack = when (ammoType) {
                            Ammo.HANDGUN -> ItemStack(ModItems.HANDGUN_AMMO.get())
                            Ammo.RIFLE -> ItemStack(ModItems.RIFLE_AMMO.get())
                            Ammo.SHOTGUN -> ItemStack(ModItems.SHOTGUN_AMMO.get())
                            Ammo.SNIPER -> ItemStack(ModItems.SNIPER_AMMO.get())
                            Ammo.HEAVY -> ItemStack(ModItems.HEAVY_AMMO.get())
                        }
                    } else {
                        ammoStack = ammoConsumer.stack()
                    }

                    poseStack.translate(1.75f, 0f, 0f)
                    guiGraphics.renderFakeItem(ammoStack, 3, -1)
                    poseStack.translate(-1.75f, 0f, 0f)

                    // 数量
                    val text = "" + data.countBackupAmmoItem(player)
                    guiGraphics.drawString(
                        font,
                        text,
                        24,
                        8,
                        0xFFFFFF,
                        true
                    )
                }

                poseStack.popPose()

                // 这里不能和上面合并
                if (!renderStackCount) {
                    when (consumerType) {
                        AmmoConsumeType.INVALID -> {
                            RenderHelper.preciseBlit(
                                guiGraphics, AMMO_STACK,
                                (x - 50).toFloat(),
                                y - 19.5f,
                                12f,
                                8.5f,
                                5f,
                                8f,
                                24f,
                                24f
                            )
                        }

                        AmmoConsumeType.ENERGY -> {
                            RenderHelper.preciseBlit(
                                guiGraphics, AMMO_STACK,
                                (x - 50).toFloat(),
                                y - 19.5f,
                                12f,
                                16.5f,
                                5f,
                                8f,
                                24f,
                                24f
                            )
                        }

                        else -> {
                            RenderHelper.preciseBlit(
                                guiGraphics, AMMO_STACK,
                                x - 51f,
                                (y - 20).toFloat(),
                                0f,
                                8.5f,
                                7f,
                                8f,
                                24f,
                                24f
                            )
                        }
                    }
                }

                // 渲染弹药种类切换提示
                if (size > 1) {
                    val offset = 47f
                    val count = size / 2
                    val posX = (if (size % 2 == 0) x - count * 6 + 1 else x - count * 6 - 2).toFloat()
                    val posY = (y - 8).toFloat()

                    for (i in 0..<size) {
                        RenderHelper.preciseBlit(
                            guiGraphics,
                            if (i == data.selectedAmmoType.get()) CHOSEN else NOT_CHOSEN,
                            posX - offset + 6 * i, posY, 0f, 0f,
                            4f, 4f, 4f, 4f
                        )
                    }
                }
            }

            poseStack.pushPose()
            poseStack.scale(1.5f, 1.5f, 1f)

            // 渲染当前弹药量
            val gunAmmoY = if (data.useBackpackAmmo()) y - 38 else y + 5 - 48

            guiGraphics.drawString(
                font,
                getGunAmmoString(data, player),
                x / 1.5f - 64 / 1.5f,
                gunAmmoY / 1.5f,
                0xFFFFFF,
                true
            )

            poseStack.popPose()

            // 虚拟弹药备弹
            if (data.virtualAmmo.get() > 0 && !data.meleeOnly()) {
                guiGraphics.drawString(
                    font,
                    "+" + data.virtualAmmo.get(),
                    x - 62 + font.width(getGunAmmoString(data, player)) * 1.5f,
                    (y - 46).toFloat(),
                    0x55FFFF,
                    true
                )
            }

            // 渲染备弹量
            guiGraphics.drawString(
                font,
                getBackupAmmoString(data, player),
                x - 64,
                y - 30,
                0xCCCCCC,
                true
            )

            poseStack.pushPose()
            poseStack.scale(0.9f, 0.9f, 1f)

            // 渲染物品名称
            val gunName: String = getGunDisplayName(stack)
            guiGraphics.drawString(
                font,
                gunName,
                x / 0.9f - (100 + font.width(gunName) / 2f) / 0.9f,
                y / 0.9f - 60 / 0.9f,
                0xFFFFFF,
                true
            )

            // 渲染弹药类型
            val ammoName: String = REPLACE_FORMAT_CODE.matcher(getAmmoDisplayName(data)).replaceAll("")

            guiGraphics.drawString(
                font,
                ammoName,
                x / 0.9f - (100 + font.width(ammoName) / 2f) / 0.9f,
                y / 0.9f - 51 / 0.9f,
                0xC8A679,
                true
            )

            poseStack.popPose()
        } else {
            // No gun in hand: remove AUI overlay if active
            removeAUIOverlay()
        }
    }

    // ========== AUI Creative Mode Rendering ==========

    /**
     * Renders the ammo bar using ApricityUI overlay for creative mode players.
     * Creates the document lazily on first call and updates DOM elements every frame.
     */
    private fun renderWithAUI(data: GunData, player: Player, screenWidth: Int, screenHeight: Int) {
        if (auiDocument == null) {
            val doc = ApricityUI.createDocument(AUI_AMMO_BAR_PATH)
            if (doc != null) {
                auiDocument = doc
                auiRight = (-DisplayConfig.WEAPON_HUD_X_OFFSET.get()).toDouble().coerceAtLeast(0.0)
                auiBottom = (-DisplayConfig.WEAPON_HUD_Y_OFFSET.get()).toDouble().coerceAtLeast(0.0)
                setupDrag(doc)
            } else {
                return
            }
        }
        updateAUIData(auiDocument!!, data, player, screenWidth, screenHeight)

        // Restore scale animation after triggerScale's cooldown
        if (scaleRestartFrames > 0) {
            scaleRestartFrames--
            if (scaleRestartFrames == 0) {
                val el = try { auiDocument?.getElementById("gun-icon") } catch (_: Exception) { null }
                el?.classList?.remove("reloading")
            }
        }
    }

    // ========== Drag via AUI events (listeners added only during drag) ==========

    private fun setupDrag(doc: Document) {
        val ammoBar = doc.getElementById("ammo-bar") ?: return
        applyAuiPosition(ammoBar)

        // Only permanent listener: mousedown on ammoBar starts the drag
        ammoBar.addEventListener("mousedown") { event ->
            if (event is com.sighs.apricityui.event.MouseEvent && event.button == 0) {
                startDrag(doc, ammoBar, event)
            }
        }
    }

    private fun startDrag(doc: Document, ammoBar: com.sighs.apricityui.init.Element, event: com.sighs.apricityui.event.MouseEvent) {
        auiDragging = true
        dragStartMouseX = event.clientX
        dragStartMouseY = event.clientY
        dragStartRight = auiRight
        dragStartBottom = auiBottom

        val body = doc.body ?: return

        // Add mousemove listener on body — only during drag
        dragMoveListener = java.util.function.Consumer { e ->
            if (e is com.sighs.apricityui.event.MouseEvent) {
                val dx = e.clientX - dragStartMouseX
                val dy = e.clientY - dragStartMouseY
                auiRight = (dragStartRight - dx).coerceIn(0.0, 1000.0)
                auiBottom = (dragStartBottom - dy).coerceIn(0.0, 1000.0)
                applyAuiPosition(ammoBar)
            }
        }

        // Add mouseup listener on body — only during drag
        dragUpListener = java.util.function.Consumer {
            finishDrag(doc)
        }

        body.addEventListener("mousemove", dragMoveListener)
        body.addEventListener("mouseup", dragUpListener)
    }

    private fun finishDrag(doc: Document) {
        if (!auiDragging) return
        auiDragging = false

        // Remove temporary listeners immediately
        val body = doc.body
        if (body != null) {
            if (dragMoveListener != null) body.removeEventListener("mousemove", dragMoveListener, false)
            if (dragUpListener != null) body.removeEventListener("mouseup", dragUpListener, false)
        }
        dragMoveListener = null
        dragUpListener = null

        DisplayConfig.WEAPON_HUD_X_OFFSET.set(-auiRight.toInt())
        DisplayConfig.WEAPON_HUD_Y_OFFSET.set(-auiBottom.toInt())
    }

    private fun applyAuiPosition(ammoBar: com.sighs.apricityui.init.Element) {
        ammoBar.setAttribute(
            "style",
            "right:${auiRight.toInt()}px;bottom:${auiBottom.toInt()}px;"
        )
    }

    /** Replay scale by briefly switching to reloading then back — same path as reload end. */
    private fun triggerScale(el: com.sighs.apricityui.init.Element?) {
        if (el == null) return
        el.classList.add("reloading")
        scaleRestartFrames = 5
    }

    /**
     * Updates the AUI document's DOM elements with current ammo bar data.
     */
    private fun updateAUIData(doc: Document, data: GunData, player: Player, screenWidth: Int, screenHeight: Int) {
        // Ensure body fills the viewport
        try {
            val body = doc.body
            body?.setAttribute("style", "width:${screenWidth}px;height:${screenHeight}px;")
        } catch (_: Exception) {}

        // Apply current drag position
        try {
            val ammoBar = doc.getElementById("ammo-bar")
            if (ammoBar != null) applyAuiPosition(ammoBar)
        } catch (_: Exception) {}

        val gunName = getGunDisplayName(data.stack)
        val ammoName = REPLACE_FORMAT_CODE.matcher(getAmmoDisplayName(data)).replaceAll("")
        val ammoCount = getGunAmmoString(data, player)
        val backupAmmo = getBackupAmmoString(data, player)
        val virtualAmmo = if (data.virtualAmmo.get() > 0 && !data.meleeOnly()) "+" + data.virtualAmmo.get() else ""
        val fireModeKey = if (data.stack.item !== ModItems.MINIGUN.get())
            "[" + ModKeyMappings.FIRE_MODE.key.displayName.string + "]"
        else ""

        setElementText(doc, "gun-name", gunName)
        setElementText(doc, "ammo-name", ammoName)
        setElementText(doc, "ammo-count", ammoCount)
        setElementText(doc, "backup-ammo", backupAmmo)
        setElementText(doc, "virtual-ammo", virtualAmmo)
        setElementText(doc, "fire-mode-key", fireModeKey)

        // Set gun icon + animation triggers
        val icon = (data.stack.item as? GunItem)?.getGunIcon(data)
        val iconSrc = icon?.toString() ?: ""
        if (icon != null) {
            setElementAttr(doc, "gun-icon", "src", iconSrc)
        }

        val isReloading = data.reloading()
        val el = try { doc.getElementById("gun-icon") } catch (_: Exception) { null }

        // Reload: toggle .reloading class
        if (isReloading && !wasReloading) {
            el?.classList?.add("reloading")
        } else if (!isReloading && wasReloading) {
            el?.classList?.remove("reloading")
            triggerScale(el)
        }

        // Gun switch or first load: trigger scale when icon changes
        if (!isReloading && iconSrc.isNotEmpty() && iconSrc != lastGunIconSrc) {
            triggerScale(el)
        }

        wasReloading = isReloading
        lastGunIconSrc = iconSrc

        // Set fire mode icon
        if (data.stack.item !== ModItems.MINIGUN.get()) {
            setElementAttr(doc, "fire-mode-icon", "src", getFireMode(data).toString())
        } else {
            setElementAttr(doc, "fire-mode-icon", "src", MOUSE.toString())
        }
    }

    /**
     * Sets an attribute on an element by ID in the given AUI document.
     */
    private fun setElementAttr(doc: Document, id: String, attr: String, value: String) {
        try {
            doc.getElementById(id)?.setAttribute(attr, value)
        } catch (_: Exception) {}
    }

    /**
     * Sets the inner text of an element by ID in the given AUI document.
     */
    private fun setElementText(doc: Document, id: String, text: String) {
        try {
            val element = doc.getElementById(id) ?: return
            element.textContent = text
        } catch (_: Exception) {
            // Silently ignore if the element or method is not available
        }
    }

    /**
     * Removes the AUI overlay document if it is currently active.
     */
    private fun removeAUIOverlay() {
        if (auiDocument != null) {
            ApricityUI.removeDocument(AUI_AMMO_BAR_PATH)
            auiDocument = null
            auiDragging = false
        }
    }

    // ========== Original Rendering Helpers ==========

    private fun getFireMode(data: GunData): ResourceLocation {
        return TO_RESOURCE_LOCATION.apply(toUnderScores(data.selectedFireModeInfo().name))
    }

    private fun toUnderScores(str: String): String {
        val builder = StringBuilder()

        for ((i, element) in str.withIndex()) {
            val c = element
            if (Character.isUpperCase(c)) {
                if (i != 0) {
                    builder.append('_')
                }
                builder.append(c.lowercaseChar())
            } else {
                builder.append(c)
            }
        }

        return builder.toString()
    }

    private fun getGunAmmoString(data: GunData, player: Player?): String {
        if (data.selectedAmmoConsumer().type == AmmoConsumeType.ENERGY) {
            val energy = data.stack.getCapability(ForgeCapabilities.ENERGY)
                .map(NonNullFunction { storage ->
                    Mth.clamp(
                        storage.energyStored.toDouble() / max(1, storage.maxEnergyStored), 0.0, 1.0
                    )
                })
                .orElseGet { 0.0 }
            return format1DZZ(energy * 100) + "%"
        }
        if (data.meleeOnly() || data.useBackpackAmmo() && data.hasInfiniteBackupAmmo(player)) return "∞"
        return if (data.useBackpackAmmo()) (data.countBackupAmmo(player) - data.virtualAmmo.get()).toString() + "" else data.ammo.get()
            .toString() + ""
    }

    private fun getBackupAmmoString(data: GunData, player: Player?): String {
        if (data.meleeOnly() || data.useBackpackAmmo() || data.selectedAmmoConsumer().type == AmmoConsumeType.ENERGY) return ""
        return if (data.hasInfiniteBackupAmmo(player)) "∞" else (data.countBackupAmmo(player) - data.virtualAmmo.get()).toString() + ""
    }

    private val REPLACE_FORMAT_CODE: Pattern = Pattern.compile("§.")

    private fun getGunDisplayName(stack: ItemStack): String {
        return if (!stack.isEmpty) {
            ClientLanguageGetter.EN_US.getOrDefault(stack.descriptionId)
        } else {
            ""
        }
    }

    private fun getAmmoDisplayName(data: GunData): String {
        if (data.meleeOnly()) return "Melee"
        val consumer = data.selectedAmmoConsumer()
        return consumer.strategy.getDisplayName(consumer)
    }
}
