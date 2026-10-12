package com.atsuishio.superbwarfare.client.screens

import com.atsuishio.superbwarfare.config.server.AttachmentConfig
import com.atsuishio.superbwarfare.data.attachment.AttachmentSlots
import com.atsuishio.superbwarfare.data.gun.GunData
import com.atsuishio.superbwarfare.data.gun.GunData.Companion.from
import com.atsuishio.superbwarfare.data.gun.GunProp
import com.atsuishio.superbwarfare.data.gun.value.AttachmentType
import com.atsuishio.superbwarfare.event.ClientEventHandler.editModelShake
import com.atsuishio.superbwarfare.event.ClientEventHandler.editingAttachmentType
import com.atsuishio.superbwarfare.event.ClientEventHandler.onCloseEditScreen
import com.atsuishio.superbwarfare.init.ModKeyMappings
import com.atsuishio.superbwarfare.item.attachment.AttachmentProvider
import com.atsuishio.superbwarfare.item.gun.GunItem
import com.atsuishio.superbwarfare.network.message.send.AttachmentInstallMessage
import com.atsuishio.superbwarfare.network.message.send.EditMessage
import com.atsuishio.superbwarfare.tools.localPlayer
import com.atsuishio.superbwarfare.tools.sendPacketToServer
import com.sighs.apricityui.ApricityUI
import com.sighs.apricityui.init.Document
import com.sighs.apricityui.init.Element
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import org.lwjgl.glfw.GLFW
import java.util.*

/**
 * 改枪界面。
 *
 * 界面本体是一份 ApricityUI 文档（`assets/apricityui/apricity/superbwarfare/weapon_edit.html`）：
 * **每个能改的槽位在它自己挂点骨骼投影到屏幕的位置上**摆一个按钮，点开把镜头聚焦过去，
 * 并在按钮旁边弹出"当前配件 + 背包里能装的配件"面板，面板第一格是"留空 / 卸下"。
 *
 * 能改什么完全由数据决定：[GunData.availableAttachments] 给出这把枪在该槽位支持的配件，
 * 界面再与玩家背包求交集 —— 列表里出现的都能装，且都真的在背包里。
 *
 * 三处外部依赖：
 * - [WeaponEditMountTracker]：本帧各挂点骨骼的屏幕位置，由 `GeoGunRenderer` 在第一人称渲染
 *   枪械模型时写入（投影本身见 `BedrockBoneCoordinateTool`）；
 * - `ClientEventHandler.editingAttachmentType`：当前聚焦的槽位，同时也是相机聚焦的目标；
 * - [AttachmentSlots]：槽位表（有哪些槽位、叫什么、换件要不要退弹、`EditMessage.type` 的下标）。
 *
 * 界面只负责"点哪个"：[AttachmentInstallMessage] 把选择发给服务端，能不能装、要不要扣背包
 * 全部由服务端重做一遍。
 */
@OnlyIn(Dist.CLIENT)
class WeaponEditScreen(private val stack: ItemStack) : Screen(Component.empty()) {

    private var document: Document? = null

    /** 各元素当前显示的物品 id，避免每帧往 DOM 里重写同样的值。 */
    private val shownItems = mutableMapOf<String, String>()

    /** 面板列出的配件；物品格 `i`（`i >= 1`）对应 `panelIds[i - 1]`，格 0 是"留空"。 */
    private var panelIds: List<ResourceLocation> = emptyList()

    override fun isPauseScreen(): Boolean = false

    override fun init() {
        super.init()
        // 窗口缩放会再走一次 init，旧文档必须先销毁 —— 变量置 null 不会销毁它，否则每缩放一次就多叠一层
        document?.remove()
        document = null
        shownItems.clear()
        WeaponEditMountTracker.clear()

        val doc = ApricityUI.createDocument(DOCUMENT_PATH) ?: return
        // 少了这一句文档就只在"没有别的 Screen"时才画，会被本 Screen 整个盖住
        doc.setReloadPersistent(true)
        document = doc

        wireMountButtons(doc)
        wirePanel(doc)
        applyLabels(doc)
    }

    override fun removed() {
        // 变量置 null 不会销毁文档，必须显式 remove
        document?.remove()
        document = null
        shownItems.clear()
        panelIds = emptyList()
        // 聚焦状态与骨骼位置都是客户端全局的，关界面时一并清掉
        WeaponEditMountTracker.clear()
        onCloseEditScreen()
        super.removed()
    }

    override fun render(pGuiGraphics: GuiGraphics, pMouseX: Int, pMouseY: Int, pPartialTick: Float) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick)
        syncDocument()
    }

    override fun keyPressed(pKeyCode: Int, pScanCode: Int, pModifiers: Int): Boolean {
        if (pKeyCode == ModKeyMappings.EDIT_MODE.key.value) {
            this.onClose()
            return true
        }
        // ESC 先退出槽位聚焦、回到浮动预览，再按一次才关界面
        if (pKeyCode == GLFW.GLFW_KEY_ESCAPE && editingAttachmentType != -1) {
            editingAttachmentType = -1
            return true
        }
        return super.keyPressed(pKeyCode, pScanCode, pModifiers)
    }

    // ---------- 交互接线（打开时只做一次） ----------

    private fun wireMountButtons(doc: Document) {
        for (type in AttachmentType.entries) {
            doc.getElementById(mountId(type))?.addEventListener("click") { onMountClicked(type) }
        }

        doc.getElementById(AMMO_MOUNT_ID)?.addEventListener("click") { onAmmoClicked() }

        // 空白处与挂点按钮 / 面板是兄弟节点，点按钮不会顺带点到它
        doc.getElementById(BACKDROP_ID)?.addEventListener("click") { onBackdropClicked() }
    }

    /** 空白处先退一级：正在选配件就退回挂点层，已经在挂点层才关界面（与 ESC 同一套）。 */
    private fun onBackdropClicked() {
        if (editingAttachmentType != -1) {
            editingAttachmentType = -1
            return
        }
        onClose()
    }

    private fun wirePanel(doc: Document) {
        for (index in 0 until GRID_CELLS) {
            doc.getElementById(cellId(index))?.addEventListener("click") { onCellClicked(index) }
        }
    }

    /** 挂点按钮下的槽位名用现成的 tooltip 文案，换语言时界面跟着变。 */
    private fun applyLabels(doc: Document) {
        for (type in AttachmentType.entries) {
            doc.getElementById(mountId(type))?.let { element ->
                element.querySelector(".label")?.textContent = slotName(type)
            }
        }
        doc.getElementById(AMMO_MOUNT_ID)?.let { element ->
            element.querySelector(".label")?.textContent =
                Component.translatable("attachment.superbwarfare.slot.ammo_type").string
        }
    }

    private fun slotName(type: AttachmentType): String =
        Component.translatable("attachment.superbwarfare.slot.${type.attachmentName.lowercase(Locale.ROOT)}").string

    // ---------- 点击 ----------

    private fun onMountClicked(type: AttachmentType) {
        val index = AttachmentSlots.editIndex(type)
        if (index < 0) return
        if (editingAttachmentType == index) return

        editingAttachmentType = index
        editModelShake()
    }

    /**
     * 弹种不是一个槽位、也没有配件列表，所以点一下就直接切到下一个（与旧界面的弹种按钮一致），
     * 同时把镜头聚焦过去。
     */
    private fun onAmmoClicked() {
        editingAttachmentType = AttachmentSlots.AMMO_TYPE_EDIT_INDEX
        editModelShake()
        sendPacketToServer(EditMessage(AttachmentSlots.AMMO_TYPE_EDIT_INDEX, true, false))
    }

    private fun onCellClicked(index: Int) {
        val type = AttachmentSlots.slotAt(editingAttachmentType) ?: return
        // 第一格是"留空 / 卸下"，空 id 就是卸下
        val id = if (index == 0) "" else (panelIds.getOrNull(index - 1)?.toString() ?: return)

        sendPacketToServer(AttachmentInstallMessage(type.attachmentName, id))
    }

    // ---------- 每帧同步 ----------

    private fun syncDocument() {
        val doc = document ?: return
        val player = localPlayer ?: return
        val held = player.mainHandItem
        val item = held.item
        // 手里的枪换了（或已经不是枪）就不再更新界面
        if (item !is GunItem || item !== stack.item) return
        val data = from(held)

        doc.getElementById(BACKDROP_ID)?.let {
            it.style.width = "${WeaponEditMountTracker.guiWidth}px"
            it.style.height = "${WeaponEditMountTracker.guiHeight}px"
        }

        syncMountButtons(doc, data)
        syncPanel(doc, player, data)
    }

    private fun syncMountButtons(doc: Document, data: GunData) {
        for (type in AttachmentType.entries) {
            val element = doc.getElementById(mountId(type)) ?: continue
            val point = WeaponEditMountTracker.positionOf(type)
            // 这把枪在这个槽位没有可用配件，或者模型里没有对应骨骼（投影落到相机背面也算）就不出按钮
            if (point == null || !point.visible || data.availableAttachments(type).isEmpty()) {
                element.style.display = "none"
                continue
            }

            place(element, point.x, point.y)
            val installed = data.attachment.id(type)
            setElementItem(element, installed?.toString() ?: AIR)
            setClass(element, "filled", installed != null)
            setClass(element, "active", editingAttachmentType == AttachmentSlots.editIndex(type))
        }

        syncAmmoMount(doc, data)
    }

    private fun syncAmmoMount(doc: Document, data: GunData) {
        val element = doc.getElementById(AMMO_MOUNT_ID) ?: return
        val point = WeaponEditMountTracker.ammoPosition
        if (point == null || !point.visible || data.get(GunProp.AMMO_CONSUMER).size <= 1) {
            element.style.display = "none"
            return
        }

        place(element, point.x, point.y)
        setElementItem(element, AIR)
        setClass(element, "filled", false)
        setClass(element, "active", editingAttachmentType == AttachmentSlots.AMMO_TYPE_EDIT_INDEX)
    }

    private fun syncPanel(doc: Document, player: Player, data: GunData) {
        val panel = doc.getElementById(PANEL_ID) ?: return
        val type = AttachmentSlots.slotAt(editingAttachmentType)

        if (type == null) {
            panel.style.display = "none"
            panelIds = emptyList()
            return
        }

        val point = WeaponEditMountTracker.positionOf(type)
        if (point == null || !point.visible) {
            panel.style.display = "none"
            return
        }

        panel.style.display = "flex"
        doc.getElementById(PANEL_TITLE_ID)?.textContent = slotName(type)
        val installed = data.attachment.id(type)
        doc.getElementById(PANEL_CURRENT_ID)?.let {
            setElementItem(it, installed?.toString() ?: AIR)
            setClass(it, "filled", installed != null)
        }

        panelIds = installableAttachments(player, data, type)
        for (index in 0 until GRID_CELLS) {
            val cell = doc.getElementById(cellId(index)) ?: continue
            val id = if (index == 0) null else panelIds.getOrNull(index - 1)
            if (index > 0 && id == null) {
                cell.style.display = "none"
                continue
            }

            cell.style.display = "flex"
            if (id != null) {
                setElementItem(cell, id.toString())
                setClass(cell, "selected", installed == id)
            }
        }

        val maxLeft = (WeaponEditMountTracker.guiWidth - PANEL_WIDTH).coerceAtLeast(0).toFloat()
        val maxTop = (WeaponEditMountTracker.guiHeight - PANEL_HEIGHT).coerceAtLeast(0).toFloat()
        panel.style.left = px((point.x + PANEL_GAP).coerceIn(0f, maxLeft))
        panel.style.top = px((point.y - PANEL_HEIGHT / 2f).coerceIn(0f, maxTop))
    }

    /**
     * 面板里列出的配件：**背包里真的有、且这把枪装得上**的那些。
     *
     * "装得上"完全由 gundata 决定（[GunData.availableAttachments] 已经过滤掉与已装其它配件互斥的项），
     * 这里只与背包求交集。按 id 去重，顺序沿用 gundata 的声明顺序；装完卸完之后列表自然跟着变。
     */
    private fun installableAttachments(player: Player, data: GunData, type: AttachmentType): List<ResourceLocation> {
        // 与安装校验用同一套判定：列出来的一定装得上（`canInstall` 还会按 `acceptedSlots` 过滤多槽位配件）
        val available = data.availableAttachments(type).filter { data.canInstall(type, it) }
        // 完全自由改装模式下不要求背包里有货 —— 那个模式本来就是"任意枪装任意已注册配件"
        if (AttachmentConfig.fullyFreeAttachmentMode) return available

        val owned = ownedAttachmentIds(player)
        return available.filter { it in owned }
    }

    private fun ownedAttachmentIds(player: Player): Set<ResourceLocation> {
        val result = mutableSetOf<ResourceLocation>()
        for (stack in player.inventory.items) {
            collectAttachmentId(result, stack)
        }
        for (stack in player.inventory.offhand) {
            collectAttachmentId(result, stack)
        }
        return result
    }

    private fun collectAttachmentId(result: MutableSet<ResourceLocation>, stack: ItemStack) {
        val provider = stack.item as? AttachmentProvider ?: return
        ResourceLocation.tryParse(provider.attachmentId)?.let { result += it }
    }

    // ---------- 小工具 ----------

    /** 按钮中心落在骨骼投影点上（[MOUNT_RADIUS] 是按钮外框边长的一半）。 */
    private fun place(element: Element, x: Float, y: Float) {
        element.style.display = "flex"
        element.style.left = px(x - MOUNT_RADIUS)
        element.style.top = px(y - MOUNT_RADIUS)
    }

    /** 位置每帧都在动，取整到像素会让按钮一格一格地跳；AUI 的布局是浮点的，直接给小数。 */
    private fun px(value: Float): String = String.format(Locale.ROOT, "%.2fpx", value)

    /** 把 [container] 里那个 `<item>` 换成 [itemId]；值没变就跳过，避免无谓地让 AUI 重解析。 */
    private fun setElementItem(container: Element, itemId: String) {
        val key = container.getAttribute("id") ?: return
        if (shownItems[key] == itemId) return

        val item = container.querySelector("item") ?: return
        item.textContent = itemId
        shownItems[key] = itemId
    }

    private fun setClass(element: Element, name: String, on: Boolean) {
        if (on) element.classList?.add(name) else element.classList?.remove(name)
    }

    companion object {
        private const val DOCUMENT_PATH = "superbwarfare/weapon_edit.html"

        private const val AIR = "minecraft:air"
        private const val BACKDROP_ID = "backdrop"
        private const val PANEL_ID = "panel"
        private const val PANEL_TITLE_ID = "panel-title"
        private const val PANEL_CURRENT_ID = "panel-current"
        private const val AMMO_MOUNT_ID = "mount-ammo_type"

        /** 面板里的物品格数量：4 排 × 9 格（第一格是"留空"）。必须与 html 里 `cell-N` 的数量一致。 */
        private const val GRID_CELLS = 36

        /** 挂点按钮外框边长的一半（18 的内容 + 1×2 的边框），用来把按钮中心对准骨骼投影点。 */
        private const val MOUNT_RADIUS = 10f

        /** 面板与挂点按钮的水平间距。 */
        private const val PANEL_GAP = 18f

        /** 与 `weapon_edit.html` 里 `#panel` 的实际尺寸保持一致（那里只有 width，高度由内容撑开）。 */
        private const val PANEL_WIDTH = 176
        private const val PANEL_HEIGHT = 123

        private fun mountId(type: AttachmentType): String = "mount-" + type.attachmentName.lowercase(Locale.ROOT)

        private fun cellId(index: Int): String = "cell-$index"
    }
}
