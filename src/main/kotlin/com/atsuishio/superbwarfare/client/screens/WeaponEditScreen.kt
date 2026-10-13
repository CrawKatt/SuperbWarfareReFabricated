package com.atsuishio.superbwarfare.client.screens

import com.atsuishio.superbwarfare.client.tooltip.ClientAttachmentImageTooltip
import com.atsuishio.superbwarfare.client.tooltip.component.AttachmentImageComponent
import com.atsuishio.superbwarfare.data.attachment.AttachmentSlots
import com.atsuishio.superbwarfare.data.gun.GunData
import com.atsuishio.superbwarfare.data.gun.GunProp
import com.atsuishio.superbwarfare.data.gun.value.AttachmentType
import com.atsuishio.superbwarfare.event.ClientEventHandler.editModelShake
import com.atsuishio.superbwarfare.event.ClientEventHandler.editingAttachmentType
import com.atsuishio.superbwarfare.event.ClientEventHandler.onCloseEditScreen
import com.atsuishio.superbwarfare.init.ModKeyMappings
import com.atsuishio.superbwarfare.init.ModSounds
import com.atsuishio.superbwarfare.item.attachment.AttachmentProvider
import com.atsuishio.superbwarfare.item.gun.GunItem
import com.atsuishio.superbwarfare.network.message.send.AttachmentInstallMessage
import com.atsuishio.superbwarfare.network.message.send.EditMessage
import com.atsuishio.superbwarfare.tools.BoneScreenPoint
import com.atsuishio.superbwarfare.tools.localPlayer
import com.atsuishio.superbwarfare.tools.sendPacketToServer
import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.VertexConsumer
import com.sighs.apricityui.event.MouseEvent
import com.sighs.apricityui.init.Document
import com.sighs.apricityui.init.Element
import com.sighs.apricityui.layout.Position
import com.sighs.apricityui.render.Base
import com.sighs.apricityui.screen.ApricityScreen
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.renderer.RenderType
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.FormattedCharSequence
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.core.registries.BuiltInRegistries
import org.joml.Matrix4f
import org.lwjgl.glfw.GLFW
import java.util.*
import kotlin.math.*

/**
 * 改装界面：骨骼挂点附近显示圆点，悬停时显示槽位名，已安装的挂点标为橙黄色。
 * 已安装配件另放图标并用白线相连。
 * 点位与图标均可打开二级菜单。可装列表由枪械数据与背包共同决定，安装仍由服务端校验。
 * DOM 只在列表变化时重建；投影位置、显隐与样式增量同步。
 */
@Environment(EnvType.CLIENT)
class WeaponEditScreen(private val stack: ItemStack) : ApricityScreen(DOCUMENT_PATH) {
    private var openingTicks = 0
    private var documentGeneration = -1L
    private val shownItems = mutableMapOf<String, String>()
    private val connectors = mutableMapOf<String, Pair<Box, Box>>()
    private val iconBounds = mutableMapOf<String, Box>()
    private var panelIds: List<ResourceLocation> = emptyList()
    private var panelType: AttachmentType? = null
    private var hoveredItem: String? = null
    private var tooltipItem: String? = null
    private var tooltipLines: List<FormattedCharSequence> = emptyList()
    private var tooltipWrapWidth = 0
    private var viewportWidth = 0f
    private var viewportHeight = 0f

    override fun isPauseScreen(): Boolean = false

    override fun tick() {
        super.tick()
        if (openingTicks < MOUNT_REVEAL_DELAY_TICKS) openingTicks++
    }

    override fun init() {
        super.init()
        clearUiState()
        WeaponEditMountTracker.clear()
        linkedDocument?.let(::prepareDocument)
    }

    override fun removed() {
        openingTicks = 0
        clearUiState()
        WeaponEditMountTracker.clear()
        onCloseEditScreen()
        super.removed()
    }

    private fun clearUiState() {
        shownItems.clear()
        connectors.clear()
        iconBounds.clear()
        panelIds = emptyList()
        panelType = null
        hoveredItem = null
        tooltipItem = null
        tooltipLines = emptyList()
        tooltipWrapWidth = 0
        documentGeneration = -1L
    }

    override fun render(graphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        val doc = linkedDocument ?: return
        val player = localPlayer ?: return
        val held = player.mainHandItem
        if (held.item !is GunItem || held.item !== stack.item) {
            onClose()
            return
        }
        if (!doc.isCurrentGeneration(documentGeneration)) prepareDocument(doc)
        val viewport = doc.viewportSize
        viewportWidth = viewport.width().toFloat()
        viewportHeight = viewport.height().toFloat()
        connectors.clear()
        iconBounds.clear()
        val data = GunData.from(held)
        var mounts = syncMounts(doc, data)
        val panel = syncPanel(doc, player, data, mounts)
        // 紧凑窗口的点位可能占满菜单四周；先确定菜单，再把点位移到剩余空间。
        if (panel != null && mounts.any { it.bounds.overlaps(panel, 6f) }) {
            mounts = syncMounts(doc, data, panel)
            mounts.firstOrNull { it.type == panelType }?.let {
                connect("panel", it.bounds, panel)
            }
        }
        val obstacles = syncIcons(doc, mounts, panel)
        syncMountLabels(doc, mounts, obstacles)
        // 连线先提交，保持在菜单和物品下方；圆圈和外框则在 AUI 绘制完成后单独提交。
        drawDecorationPass(graphics, doc, false) { vertices, matrix ->
            for ((mount, target) in connectors.values) drawConnector(vertices, matrix, mount, target)
        }
        // 平面 UI 不应与物品模型残留的深度互相遮挡；由 AUI Screen 统一绘制，避免全局 overlay 再画一次。
        graphics.flush()
        Base.pushDepthTest(false)
        try {
            super.render(graphics, mouseX, mouseY, partialTick)
        } finally {
            Base.popDepthTest()
        }
        renderMountDecorations(graphics, doc, mounts)
        renderAttachmentTooltip(graphics, doc)
    }

    // AUI 在 Screen 的 Pre 事件中派发 DOM 滚轮；不把未处理的滚轮传给游戏。
    override fun mouseScrolled(mouseX: Double, mouseY: Double, delta: Double): Boolean = true

    override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
        if (keyCode == ModKeyMappings.EDIT_MODE.key.value) {
            onClose()
            return true
        }
        if (keyCode == GLFW.GLFW_KEY_ESCAPE && editingAttachmentType != -1) {
            leaveSlot()
            return true
        }
        return super.keyPressed(keyCode, scanCode, modifiers)
    }

    private fun leaveSlot() {
        editingAttachmentType = -1
        hoveredItem = null
    }

    private fun prepareDocument(doc: Document) {
        clearUiState()
        documentGeneration = doc.refreshGeneration
        doc.addEventListener("wheel", { event ->
            val scroll = doc.getElementById("panel-scroll")
            val inList = panelType != null && AttachmentSlots.slotAt(editingAttachmentType) == panelType &&
                scroll != null && event.composedPath().any { it === scroll }
            // 始终取消默认滚动，列表到边界时也不能把滚动传给 body 或其他祖先。
            event.preventDefault()
            event.stopPropagation()
            if (inList && event is MouseEvent) {
                scroll.scrollBy(0.0, event.deltaY)
            }
        }, true)
        for (type in AttachmentType.entries) {
            val id = mountId(type)
            addMount(doc, id, slotName(type)) { onMountClicked(type) }
            doc.getElementById("icon-layer")?.appendChild(doc.createHTML(
                """<div id="icon-$id" class="attachment-icon">${itemSlot()}</div>"""
            ))
            doc.getElementById("icon-$id")?.let { icon ->
                icon.addEventListener("click") { onMountClicked(type) }
                wireItemHover(icon)
            }
        }
        addMount(doc, AMMO_MOUNT_ID, Component.translatable("attachment.superbwarfare.slot.ammo_type").string) {
            onAmmoClicked()
        }
        doc.getElementById("backdrop")?.addEventListener("click") {
            if (editingAttachmentType != -1) leaveSlot() else onClose()
        }
        doc.getElementById("panel-uninstall")?.addEventListener("click") {
            val type = AttachmentSlots.slotAt(editingAttachmentType) ?: return@addEventListener
            val data = localPlayer?.mainHandItem?.let(GunData::from) ?: return@addEventListener
            if (data.attachment.id(type) != null) {
                hoveredItem = null
                sendPacketToServer(AttachmentInstallMessage(type.attachmentName, ""))
            }
        }
        doc.getElementById("panel-current")?.let(::wireItemHover)
        doc.getElementById("panel-name")?.let { name ->
            name.addEventListener("mouseenter") { hoveredItem = "panel-current" }
            name.addEventListener("mouseleave") { hoveredItem = null }
        }
        doc.getElementById("panel-empty")?.textContent = label("no_available")
    }

    private fun addMount(doc: Document, id: String, name: String, action: () -> Unit) {
        doc.getElementById("mount-layer")?.appendChild(doc.createHTML(
            """<div id="$id" class="mount"></div>"""
        ))
        doc.getElementById(id)?.addEventListener("click") { action() }
        doc.getElementById("mount-layer")?.appendChild(doc.createHTML(
            """<div id="label-$id" class="mount-label"></div>"""
        ))
        doc.getElementById("label-$id")?.textContent = name
    }

    private fun wireItemHover(element: Element) {
        val id = element.getAttribute("id") ?: return
        element.addEventListener("mouseenter") { hoveredItem = id }
        element.addEventListener("mouseleave") { if (hoveredItem == id) hoveredItem = null }
    }

    private fun onMountClicked(type: AttachmentType) {
        val index = AttachmentSlots.editIndex(type)
        if (index < 0 || editingAttachmentType == index) return
        hoveredItem = null
        editingAttachmentType = index
        editModelShake()
        localPlayer?.playSound(ModSounds.EDIT, 1f, 1f)
    }

    private fun onAmmoClicked() {
        hoveredItem = null
        editingAttachmentType = AttachmentSlots.AMMO_TYPE_EDIT_INDEX
        editModelShake()
        sendPacketToServer(EditMessage(AttachmentSlots.AMMO_TYPE_EDIT_INDEX, true, false))
    }

    private data class Mount(val id: String, val type: AttachmentType?, val bounds: Box, val installed: ResourceLocation?)

    private fun syncMounts(doc: Document, data: GunData, reservedPanel: Box? = null): List<Mount> {
        val mounts = mutableListOf<Mount>()
        val selectedType = AttachmentSlots.slotAt(editingAttachmentType)
        fun sync(id: String, type: AttachmentType?, point: BoneScreenPoint?, available: Boolean) {
            val element = doc.getElementById(id) ?: return
            val installed = type?.let { data.attachment.id(it) }
            // 入场动画的前 n tick 隐藏挂点；二级菜单打开后只保留当前挂点。
            // 图标、连线和标签也只按返回的点位列表绘制。
            if (openingTicks < MOUNT_REVEAL_DELAY_TICKS || (selectedType != null && type != selectedType) ||
                point == null || !point.visible || (!available && installed == null)
            ) {
                element.style.display = "none"
                return
            }
            // gui 模式在 GUI scale >= 6 时不再是 1:1，必须使用 AUI 的坐标转换。
            val projected = doc.guiToDocumentPosition(Position(point.x.toDouble(), point.y.toDouble()))
            val bounds = findSpace(projected.x.toFloat(), projected.y.toFloat(), MOUNT_SIZE, MOUNT_SIZE,
                mounts.map { it.bounds } + listOfNotNull(reservedPanel), 3f, step = 4f)
            place(element, bounds)
            val index = type?.let(AttachmentSlots::editIndex) ?: AttachmentSlots.AMMO_TYPE_EDIT_INDEX
            setClass(element, "active", editingAttachmentType == index)
            setClass(element, "filled", installed != null)
            mounts += Mount(id, type, bounds, installed)
        }
        for (type in AttachmentType.entries) {
            sync(mountId(type), type, WeaponEditMountTracker.positionOf(type), data.availableAttachments(type).isNotEmpty())
        }
        sync(AMMO_MOUNT_ID, null, WeaponEditMountTracker.ammoPosition, data.get(GunProp.AMMO_CONSUMER).size > 1)
        return mounts
    }

    private fun syncPanel(doc: Document, player: Player, data: GunData, mounts: List<Mount>): Box? {
        val panel = doc.getElementById("panel") ?: return null
        val type = AttachmentSlots.slotAt(editingAttachmentType)
        val mount = mounts.firstOrNull { it.type == type && type != null }
        if (type == null || mount == null) {
            panel.style.display = "none"
            if (panelType != null) hoveredItem = null
            panelType = null
            return null
        }
        val installed = data.attachment.id(type)
        doc.getElementById("panel-current")?.let {
            setElementItem(it, installed?.toString() ?: AIR)
            setClass(it, "filled", installed != null)
        }
        doc.getElementById("panel-name")?.let {
            val current = itemStack(installed)
            it.textContent = if (current.isEmpty) label("empty") else current.hoverName.string
            it.style.color = if (current.isEmpty) "#bdbdbd" else rarityColor(current)
        }
        doc.getElementById("panel-slot")?.textContent = slotName(type)
        doc.getElementById("panel-uninstall")?.let { setClass(it, "disabled", installed == null) }
        val ids = installableAttachments(player, data, type)
        val changedSlot = panelType != type
        if (changedSlot || panelIds != ids) {
            rebuildGrid(doc, type, ids)
            if (changedSlot) doc.getElementById("panel-scroll")?.scrollTo(0.0, 0.0)
            panelIds = ids
            panelType = type
        }
        for ((index, id) in panelIds.withIndex()) {
            doc.getElementById("cell-$index")?.let { setClass(it, "selected", id == installed) }
        }
        // 小窗口为点位和图标保留半屏空间；所有避让使用缩放后的外框。
        val scale = minOf(1f, (viewportWidth - 8f) / (PANEL_WIDTH * 2f), usableHeight() / PANEL_HEIGHT).coerceAtLeast(0.1f)
        panel.style.transform = "scale($scale)"
        val bounds = findSpace(mount.bounds.right + 24f + PANEL_WIDTH * scale / 2f, mount.bounds.centerY,
            PANEL_WIDTH * scale, PANEL_HEIGHT * scale, mounts.map { it.bounds }, 6f)
        place(panel, bounds)
        connect("panel", mount.bounds, bounds)
        return bounds
    }

    private fun rebuildGrid(doc: Document, type: AttachmentType, ids: List<ResourceLocation>) {
        if (hoveredItem?.startsWith("cell-") == true) hoveredItem = null
        shownItems.keys.removeAll { it.startsWith("cell-") }
        val grid = doc.getElementById("panel-grid") ?: return
        // 每行五格，不依赖 AUI Grid 的隐式轨道；原生滚动区域容纳任意行数。
        grid.innerHTML = ids.chunked(5).mapIndexed { row, entries ->
            entries.mapIndexed { column, id ->
                val index = row * 5 + column
                """<div id="cell-$index" class="cell">${itemSlot(id.toString(), 15)}</div>"""
            }.joinToString("", "<div class=\"row\">", "</div>")
        }.joinToString("")
        for ((index, id) in ids.withIndex()) {
            val cell = doc.getElementById("cell-$index") ?: continue
            shownItems["cell-$index"] = id.toString()
            cell.addEventListener("click") {
                if (AttachmentSlots.slotAt(editingAttachmentType) == type) {
                    hoveredItem = null
                    sendPacketToServer(AttachmentInstallMessage(type.attachmentName, id.toString()))
                }
            }
            wireItemHover(cell)
        }
        doc.getElementById("panel-empty")?.style?.display = if (ids.isEmpty()) "block" else "none"
    }

    private fun syncIcons(doc: Document, mounts: List<Mount>, panel: Box?): MutableList<Box> {
        val obstacles = mounts.mapTo(mutableListOf()) { it.bounds }
        panel?.let { obstacles += it }
        for (type in AttachmentType.entries) {
            val id = mountId(type)
            val icon = doc.getElementById("icon-$id") ?: continue
            val mount = mounts.firstOrNull { it.type == type }
            val installed = mount?.installed
            if (mount == null || installed == null) {
                icon.style.display = "none"
                if (hoveredItem == "icon-$id") hoveredItem = null
                continue
            }
            val bounds = findSpace(mount.bounds.centerX, mount.bounds.centerY - 24f,
                ICON_SIZE, ICON_SIZE, obstacles, 5f)
            obstacles += bounds
            iconBounds[id] = bounds
            place(icon, bounds)
            setElementItem(icon, installed.toString())
            setClass(icon, "filled", true)
            setClass(icon, "active", editingAttachmentType == AttachmentSlots.editIndex(type))
            connect(id, mount.bounds, bounds)
        }
        return obstacles
    }

    /** 只显示鼠标所在圆圈的名称，并避让物品图标和菜单。 */
    private fun syncMountLabels(doc: Document, mounts: List<Mount>, obstacles: MutableList<Box>) {
        val ids = AttachmentType.entries.map(::mountId) + AMMO_MOUNT_ID
        val mouse = doc.mouseDocumentPosition
        for (id in ids) {
            val element = doc.getElementById("label-$id") ?: continue
            val mount = mounts.firstOrNull { it.id == id }
            if (mount == null || !mount.bounds.contains(mouse.x.toFloat(), mouse.y.toFloat())) {
                element.style.display = "none"
                continue
            }
            val labelWidth = (font.width(element.textContent) * 7f / 9f + 4f)
                .coerceIn(1f, (viewportWidth - 8f).coerceAtLeast(1f))
            val bounds = findSpace(mount.bounds.centerX, mount.bounds.bottom + 8f,
                labelWidth, 11f, obstacles, 2f)
            element.style.width = px(labelWidth)
            place(element, bounds)
            setClass(element, "filled", mount.installed != null)
            obstacles += bounds
        }
    }

    /** 复用配件 tooltip 中的槽位、描述与属性，避免 HTML 与配件数据维护两套文案。 */
    private fun renderAttachmentTooltip(graphics: GuiGraphics, doc: Document) {
        val id = hoveredItem?.let { shownItems[it] }?.takeUnless { it == AIR }
        if (id == null) {
            tooltipItem = null
            tooltipLines = emptyList()
            return
        }
        val tooltipWidth = minOf(210f, viewportWidth - 8f).coerceAtLeast(15f)
        val wrapWidth = (tooltipWidth - 14f).toInt().coerceAtLeast(1)
        if (tooltipItem != id || tooltipWrapWidth != wrapWidth) {
            val item = itemStack(ResourceLocation.tryParse(id))
            val lines = listOf(item.hoverName.copy().withStyle { it.withColor(item.displayName.style.color) }.withStyle { it.withBold(false) }) +
                ClientAttachmentImageTooltip(AttachmentImageComponent(item)).buildLines(item)
            tooltipLines = lines.flatMap { font.split(it, wrapWidth) }
            tooltipItem = id
            tooltipWrapWidth = wrapWidth
        }
        if (tooltipLines.isEmpty()) return
        val height = tooltipLines.size * 12f + 14f
        val scale = minOf(1f, (viewportHeight - 8f) / height, (viewportWidth - 8f) / tooltipWidth)
            .coerceAtLeast(0.1f)
        val visibleWidth = tooltipWidth * scale
        val visibleHeight = height * scale
        val mouse = doc.mouseDocumentPosition
        val x = if (mouse.x + 12 + visibleWidth <= viewportWidth - 4) mouse.x.toFloat() + 12f
            else mouse.x.toFloat() - visibleWidth - 12f
        val left = x.coerceIn(4f, (viewportWidth - visibleWidth - 4f).coerceAtLeast(4f))
        val top = (mouse.y.toFloat() + 12f).coerceIn(4f, (viewportHeight - visibleHeight - 4f).coerceAtLeast(4f))

        // 所有 AUI 物品及装饰已提交。背景先提交，文字再提交，整个描述框位于全部文档的物品层上方。
        graphics.flush()
        val pose = graphics.pose()
        val previousColor = RenderSystem.getShaderColor().copyOf()
        pose.pushPose()
        try {
            RenderSystem.setShaderColor(1f, 1f, 1f, 1f)
            val documentScale = doc.viewport.renderScale()
            pose.scale(documentScale, documentScale, 1f)
            pose.translate(left.toDouble(), top.toDouble(), Base.getFlatOverlayZ().toDouble())
            pose.scale(scale, scale, 1f)
            val w = ceil(tooltipWidth).toInt()
            val h = ceil(height).toInt()
            graphics.fill(0, 0, w, h, 0xf20a0c10.toInt())
            graphics.fill(0, 0, w, 1, -1)
            graphics.fill(0, h - 1, w, h, -1)
            graphics.fill(0, 0, 1, h, -1)
            graphics.fill(w - 1, 0, w, h, -1)
            graphics.flush()
            pose.translate(0.0, 0.0, 1.0)
            tooltipLines.forEachIndexed { index, line ->
                graphics.drawString(font, line, 7, 7 + index * 12, 0xffffff, false)
            }
            graphics.flush()
        } finally {
            pose.popPose()
            RenderSystem.setShaderColor(previousColor[0], previousColor[1], previousColor[2], previousColor[3])
        }
    }

    private fun installableAttachments(player: Player, data: GunData, type: AttachmentType): List<ResourceLocation> {
        val available = data.availableAttachments(type).filter { data.canInstall(type, it) }.distinct()
        if (player.isCreative) return available
        val owned = (player.inventory.items + player.inventory.offhand).asSequence()
            .filterNot { it.isEmpty }
            .mapNotNull { (it.item as? AttachmentProvider)?.attachmentId?.let(ResourceLocation::tryParse) }
            .toSet()
        return available.filter { it in owned }
    }

    private data class Box(val left: Float, val top: Float, val width: Float, val height: Float) {
        val right get() = left + width
        val bottom get() = top + height
        val centerX get() = left + width / 2f
        val centerY get() = top + height / 2f
        fun contains(x: Float, y: Float) = x >= left && x < right && y >= top && y < bottom
        fun overlaps(other: Box, gap: Float) = left < other.right + gap && right + gap > other.left &&
            top < other.bottom + gap && bottom + gap > other.top
        fun overlapArea(other: Box, gap: Float): Float =
            (min(right + gap, other.right) - max(left - gap, other.left)).coerceAtLeast(0f) *
                (min(bottom + gap, other.bottom) - max(top - gap, other.top)).coerceAtLeast(0f)
    }

    private fun usableHeight() = (viewportHeight - 28f).coerceAtLeast(1f)

    /** 从期望位置向外搜索，矩形间保留 gap；极小窗口放不下时选择遮挡最少的位置。 */
    private fun findSpace(
        x: Float, y: Float, width: Float, height: Float, obstacles: List<Box>, gap: Float, step: Float = 12f
    ): Box {
        fun candidate(cx: Float, cy: Float) = Box(
            (cx - width / 2f).coerceIn(4f, (viewportWidth - width - 4f).coerceAtLeast(4f)),
            (cy - height / 2f).coerceIn(4f, (usableHeight() - height - 4f).coerceAtLeast(4f)), width, height
        )
        var best = candidate(x, y)
        var bestOverlap = Float.POSITIVE_INFINITY
        val rings = ceil(max(viewportWidth, viewportHeight) / step).toInt()
        for (ring in 0..rings) {
            for (direction in 0 until if (ring == 0) 1 else 24) {
                val angle = direction * PI * 2 / 24
                val box = candidate(x + (cos(angle) * ring * step).toFloat(), y + (sin(angle) * ring * step).toFloat())
                if (obstacles.none { box.overlaps(it, gap) }) return box
                val overlap = obstacles.sumOf { box.overlapArea(it, gap).toDouble() }.toFloat()
                if (overlap < bestOverlap) {
                    best = box
                    bestOverlap = overlap
                }
            }
        }
        return best
    }

    private fun place(element: Element, bounds: Box) {
        element.style.display = "flex"
        element.style.left = px(bounds.left)
        element.style.top = px(bounds.top)
    }

    private fun connect(id: String, mount: Box, target: Box) {
        connectors[id] = mount to target
    }

    /** 装饰几何独立提交，不与 AUI 的悬停文字、描边或背景图形共用批次。 */
    private fun drawDecorationPass(
        graphics: GuiGraphics, doc: Document, foreground: Boolean, draw: (VertexConsumer, Matrix4f) -> Unit
    ) {
        graphics.flush()
        val pose = graphics.pose()
        val previousColor = RenderSystem.getShaderColor().copyOf()
        pose.pushPose()
        try {
            RenderSystem.setShaderColor(1f, 1f, 1f, 1f)
            val scale = doc.viewport.renderScale()
            pose.scale(scale, scale, 1f)
            if (foreground) pose.translate(0.0, 0.0, Base.getFlatOverlayZ().toDouble() - 2.0)
            draw(graphics.bufferSource().getBuffer(RenderType.gui()), pose.last().pose())
            graphics.flush()
        } finally {
            pose.popPose()
            RenderSystem.setShaderColor(previousColor[0], previousColor[1], previousColor[2], previousColor[3])
        }
    }

    private fun renderMountDecorations(graphics: GuiGraphics, doc: Document, mounts: List<Mount>) {
        val mouse = doc.mouseDocumentPosition
        drawDecorationPass(graphics, doc, true) { vertices, matrix ->
            for (mount in mounts) {
                val bounds = mount.bounds
                val selected = editingAttachmentType ==
                    (mount.type?.let(AttachmentSlots::editIndex) ?: AttachmentSlots.AMMO_TYPE_EDIT_INDEX)
                val highlighted = selected || bounds.contains(mouse.x.toFloat(), mouse.y.toFloat())
                val color = if (mount.installed == null) -1 else MOUNT_FILLED_COLOR
                val background = if (highlighted) (color and 0xffffff) or 0x59000000 else 0x1f000000
                val radius = MOUNT_SIZE / 2f
                drawCircle(vertices, matrix, bounds.centerX, bounds.centerY, radius, 0f, background)
                drawCircle(vertices, matrix, bounds.centerX, bounds.centerY, radius,
                    radius - if (highlighted) 1f else 0.5f, color)
                drawCircle(vertices, matrix, bounds.centerX, bounds.centerY, 2.5f, 0f, color)
                iconBounds[mount.id]?.let { icon ->
                    val border = if (selected) 1f else 0.5f
                    drawQuad(vertices, matrix, icon.left, icon.top, icon.right, icon.top + border, color)
                    drawQuad(vertices, matrix, icon.left, icon.bottom - border, icon.right, icon.bottom, color)
                    drawQuad(vertices, matrix, icon.left, icon.top + border, icon.left + border, icon.bottom - border, color)
                    drawQuad(vertices, matrix, icon.right - border, icon.top + border, icon.right, icon.bottom - border, color)
                }
            }
        }
    }

    private fun vertex(vertices: VertexConsumer, matrix: Matrix4f, x: Float, y: Float, color: Int) {
        vertices.vertex(matrix, x, y, 0f)
            .color((color ushr 16) and 255, (color ushr 8) and 255, color and 255, (color ushr 24) and 255)
            .endVertex()
    }

    private fun drawQuad(
        vertices: VertexConsumer, matrix: Matrix4f, left: Float, top: Float, right: Float, bottom: Float, color: Int
    ) {
        // 与 GuiGraphics.fill 的顶点顺序一致；GUI RenderType 默认剔除背面。
        vertex(vertices, matrix, right, bottom, color)
        vertex(vertices, matrix, right, top, color)
        vertex(vertices, matrix, left, top, color)
        vertex(vertices, matrix, left, bottom, color)
    }

    private fun drawCircle(
        vertices: VertexConsumer, matrix: Matrix4f, x: Float, y: Float, outer: Float, inner: Float, color: Int
    ) {
        for (segment in 0 until 48) {
            val angle = segment * PI * 2 / 48
            val next = (segment + 1) * PI * 2 / 48
            val ax = cos(angle).toFloat()
            val ay = sin(angle).toFloat()
            val bx = cos(next).toFloat()
            val by = sin(next).toFloat()
            vertex(vertices, matrix, x + ax * outer, y + ay * outer, color)
            vertex(vertices, matrix, x + ax * inner, y + ay * inner, color)
            vertex(vertices, matrix, x + bx * inner, y + by * inner, color)
            vertex(vertices, matrix, x + bx * outer, y + by * outer, color)
        }
    }

    /** 从圆环外缘连到目标矩形最近的边缘，装饰线不参加命中测试。 */
    private fun drawConnector(vertices: VertexConsumer, matrix: Matrix4f, mount: Box, target: Box) {
        val endX = mount.centerX.coerceIn(target.left, target.right)
        val endY = mount.centerY.coerceIn(target.top, target.bottom)
        val dx = endX - mount.centerX
        val dy = endY - mount.centerY
        val distance = hypot(dx, dy)
        val radius = MOUNT_SIZE / 2f
        if (distance <= radius) return
        val startX = mount.centerX + dx / distance * radius
        val startY = mount.centerY + dy / distance * radius
        val normalX = -dy / distance * 0.5f
        val normalY = dx / distance * 0.5f
        vertex(vertices, matrix, startX - normalX, startY - normalY, -1)
        vertex(vertices, matrix, startX + normalX, startY + normalY, -1)
        vertex(vertices, matrix, endX + normalX, endY + normalY, -1)
        vertex(vertices, matrix, endX - normalX, endY - normalY, -1)
    }

    private fun setElementItem(container: Element, itemId: String) {
        val key = container.getAttribute("id") ?: return
        if (shownItems[key] == itemId) return
        container.querySelector("item")?.textContent = itemId
        shownItems[key] = itemId
    }

    private fun setClass(element: Element, name: String, enabled: Boolean) {
        val classes = element.classList ?: return
        if (classes.contains(name) == enabled) return
        if (enabled) classes.add(name) else classes.remove(name)
    }

    private fun itemStack(id: ResourceLocation?): ItemStack =
        id?.let { BuiltInRegistries.ITEM.getOptional(it).orElse(null) }?.let(::ItemStack) ?: ItemStack.EMPTY

    // Forge 自定义稀有度（例如 VIRTUAL）通过 Style modifier 定义 RGB，不能读旧的 ChatFormatting 字段。
    private fun rarityColor(item: ItemStack) = String.format(
        Locale.ROOT, "#%06x", item.displayName.style.color?.value ?: 0xffffff
    )
    private fun px(value: Float) = String.format(Locale.ROOT, "%.2fpx", value)
    private fun slotName(type: AttachmentType) =
        Component.translatable("attachment.superbwarfare.slot.${type.attachmentName.lowercase(Locale.ROOT)}").string
    private fun label(key: String) = Component.translatable("screen.superbwarfare.weapon_edit.$key").string

    companion object {
        private const val DOCUMENT_PATH = "superbwarfare/weapon_edit.html"
        private val MOUNT_FILLED_COLOR = 0xffffb43b.toInt()
        private const val MOUNT_REVEAL_DELAY_TICKS = 10
        private const val AIR = "minecraft:air"
        private const val AMMO_MOUNT_ID = "mount-ammo_type"
        private const val MOUNT_SIZE = 9f
        private const val ICON_SIZE = 17f
        private const val PANEL_WIDTH = 132f
        private const val PANEL_HEIGHT = 94f
        private fun mountId(type: AttachmentType) = "mount-" + type.attachmentName.lowercase(Locale.ROOT)
        private fun itemSlot(id: String = AIR, size: Int = 16) =
            """<slot render="item" interactive="0" pointer="0" size="$size" iconScale="${size / 16f}"><item>$id</item></slot>"""
    }
}
