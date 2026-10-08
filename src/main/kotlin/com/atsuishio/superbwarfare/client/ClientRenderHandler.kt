package com.atsuishio.superbwarfare.client

import com.atsuishio.superbwarfare.client.animation.AnimationCurves
import com.atsuishio.superbwarfare.client.decorator.ContainerItemDecorator
import com.atsuishio.superbwarfare.client.decorator.LuckyContainerItemDecorator
import com.atsuishio.superbwarfare.client.decorator.VehicleKeyItemDecorator
import com.atsuishio.superbwarfare.client.gun.MeleeClientHandler
import com.atsuishio.superbwarfare.client.model.curio.ParachuteModel
import com.atsuishio.superbwarfare.client.model.curio.ThermalImagingGogglesModel
import com.atsuishio.superbwarfare.client.overlay.*
import com.atsuishio.superbwarfare.client.overlay.weapon.AircraftHud
import com.atsuishio.superbwarfare.client.overlay.weapon.HelicopterHud
import com.atsuishio.superbwarfare.client.overlay.weapon.OldAircraftHud
import com.atsuishio.superbwarfare.client.renderer.block.BlueprintResearchTableBlockEntityRenderer
import com.atsuishio.superbwarfare.client.renderer.block.ChargingStationBlockEntityRenderer
import com.atsuishio.superbwarfare.client.renderer.block.ContainerBlockEntityRenderer
import com.atsuishio.superbwarfare.client.renderer.block.FuMO25BlockEntityRenderer
import com.atsuishio.superbwarfare.client.renderer.block.LuckyContainerBlockEntityRenderer
import com.atsuishio.superbwarfare.client.renderer.block.SmallContainerBlockEntityRenderer
import com.atsuishio.superbwarfare.client.renderer.block.VehicleAssemblingTableBlockEntityRenderer
import com.atsuishio.superbwarfare.client.renderer.curio.ParachuteRenderer
import com.atsuishio.superbwarfare.client.renderer.curio.ThermalImagingGogglesRenderer
import com.atsuishio.superbwarfare.client.renderer.item.BlueprintResearchingTableBlockItemRenderer
import com.atsuishio.superbwarfare.client.renderer.item.Knife6kh2Renderer
import com.atsuishio.superbwarfare.client.renderer.item.KnifeM1917Renderer
import com.atsuishio.superbwarfare.client.renderer.item.KnifeRenderer
import com.atsuishio.superbwarfare.client.renderer.item.KnifeSeitengewehr84Renderer
import com.atsuishio.superbwarfare.client.renderer.item.Tm62ItemRenderer
import com.atsuishio.superbwarfare.client.renderer.item.Type88ClusterGrenadesRenderer
import com.atsuishio.superbwarfare.client.renderer.gun.GeoGunRenderer
import com.atsuishio.superbwarfare.client.renderer.special.MeleeDebugRenderer
import com.atsuishio.superbwarfare.client.tooltip.ClientBocekImageTooltip
import com.atsuishio.superbwarfare.client.tooltip.ClientCellImageTooltip
import com.atsuishio.superbwarfare.client.tooltip.ClientChargingStationImageTooltip
import com.atsuishio.superbwarfare.client.tooltip.ClientDogTagImageTooltip
import com.atsuishio.superbwarfare.client.tooltip.ClientGunImageTooltip
import com.atsuishio.superbwarfare.client.tooltip.ClientSentinelImageTooltip
import com.atsuishio.superbwarfare.client.tooltip.ClientAttachmentImageTooltip
import com.atsuishio.superbwarfare.client.tooltip.component.BocekImageComponent
import com.atsuishio.superbwarfare.client.tooltip.component.CellImageComponent
import com.atsuishio.superbwarfare.client.tooltip.component.ChargingStationImageComponent
import com.atsuishio.superbwarfare.client.tooltip.component.DogTagImageComponent
import com.atsuishio.superbwarfare.client.tooltip.component.GunImageComponent
import com.atsuishio.superbwarfare.client.tooltip.component.SentinelImageComponent
import com.atsuishio.superbwarfare.client.tooltip.component.AttachmentImageComponent
import com.atsuishio.superbwarfare.init.ModBlockEntities
import com.atsuishio.superbwarfare.init.ModItems
import com.atsuishio.superbwarfare.item.armor.GeHelmetM35Item
import com.atsuishio.superbwarfare.item.armor.HandsomeGogglesItem
import com.atsuishio.superbwarfare.item.armor.RuChest6b43Item
import com.atsuishio.superbwarfare.item.armor.RuHelmet6b47Item
import com.atsuishio.superbwarfare.item.armor.UsChestIotvItem
import com.atsuishio.superbwarfare.item.armor.UsHelmetPasgtItem
import com.atsuishio.superbwarfare.item.gun.GeoGunItemV2
import com.atsuishio.superbwarfare.tools.mc
import com.atsuishio.superbwarfare.item.gun.GunItem
import com.atsuishio.superbwarfare.tools.BedrockBoneCoordinateTool
import com.atsuishio.superbwarfare.tools.toVec3
import com.mojang.blaze3d.vertex.PoseStack
import dev.emi.trinkets.api.client.TrinketRendererRegistry
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.item.ItemStack
import net.minecraft.world.phys.Vec3
import org.joml.Vector2f
import org.joml.Vector3f
import kotlin.math.min

object ClientRenderHandler {
    private val containerDecorator = ContainerItemDecorator()
    private val luckyContainerDecorator = LuckyContainerItemDecorator()
    private val vehicleKeyDecorator = VehicleKeyItemDecorator()

    // Matches the 0.8.9 NeoForge below-overlay chain from bottom to top.
    private val overlays = listOf(
        SodayoRocketInfoOverlay,
        Type63InfoOverlay,
        MortarInfoOverlay,
        TowOverlay,
        SpyglassRangeOverlay,
        HandsomeFrameOverlay,
        RedTriangleOverlay,
        DroneHudOverlay,
        HeatBarOverlay,
        CrossHairOverlay,
        ItemRendererFixOverlay,
        AmmoCountOverlay,
        StaminaOverlay,
        VehicleCrosshairOverlay,
        GPWSOverlay,
        VehicleMainWeaponHudOverlay,
        VehicleHudOverlay,
        IglaHudOverlay,
        JavelinHudOverlay,
        VehicleTeamOverlay,
        IFFOverlay,
        AmmoBarOverlay,
        ArmorPlateOverlay,
        KillMessageOverlay
    )

    /** [bulletRenderOffset] 的采样时刻（`System.nanoTime()`），超过 [OFFSET_TTL] 就当它过期 */
    private var bulletRenderOffsetTime: Long = 0L

    /**
     * 本地玩家**枪口相对视角定位点的偏移**（世界轴向量），由枪械渲染在开火窗口里写进来
     * （`GeoGunRenderer.submitMuzzleOffset`）。
     *
     * 子弹是按服务器给的坐标画的，而服务器把它从 `Vec3(x, eyeY, z)` —— **眼睛**上打出去
     * （`GunItem.shoot`），所以刚出膛那几帧它画在脸上而不是枪管上。补上"枪口 − 视角定位点"
     * 这段偏差，它就正好落在枪管上：定位点每帧被 `applyFirstPersonPositioningTransform`
     * 钉在相机原点上，所以这段差值就是枪口相对玩家视角的位置。
     *
     * ⚠ 存的是**偏移量（向量）而不是枪口的世界坐标**，这是它和"记一个目标点再蹭过去"的关键区别。
     * 枪口长在跟着视角转的枪上，一旦换算成世界坐标，那个点就在开火那一瞬间定死了：玩家一转头，
     * 子弹就被拽向一个固定的世界位置 —— 看上去正是"没在枪口上，而且随视角上下左右转乱跑"。
     * 偏移量不会：它跟着枪走，加到子弹自己的位置上，永远只补那一段固定偏差。
     * 同一条教训见 `LaserSightCapture.Beam`：挂在枪上的东西留在渲染空间里用，只有向量/标量才去过世界坐标。
     *
     * 只给"刚出膛"的弹射物用，所以带有效期：开火窗口一过枪械渲染就不再刷新它，
     * 这份偏移会在 [OFFSET_TTL] 内过期作废，免得把不相干的弹射物（别人打的、自己扔的手雷）也挪过去。
     */
    var bulletRenderOffset: Vec3? = null
        set(value) {
            field = value
            bulletRenderOffsetTime = System.nanoTime()
        }

    private const val OFFSET_TTL = 300_000_000L

    private var muzzleDirectionTime: Long = 0L

    var muzzleDirection: Vec3? = null
        set(value) {
            field = value
            muzzleDirectionTime = System.nanoTime()
        }

    @JvmStatic
    fun freshMuzzleDirection(): Vector3f? {
        val direction = muzzleDirection ?: return null
        if (System.nanoTime() - muzzleDirectionTime > DIRECTION_TTL) return null
        if (direction.lengthSqr() < 1e-8) return null
        return Vector3f(
            direction.x.toFloat(),
            direction.y.toFloat(),
            direction.z.toFloat()
        ).normalize()
    }

    private const val DIRECTION_TTL = 200_000_000L
    private var scopeReticleScreenTime: Long = 0L

    var scopeReticleScreen: Vector2f? = null
        set(value) {
            field = value
            scopeReticleScreenTime = System.nanoTime()
        }

    @JvmStatic
    fun freshScopeReticleScreen(): Vector2f? {
        val screen = scopeReticleScreen ?: return null
        if (System.nanoTime() - scopeReticleScreenTime > RETICLE_TTL) return null
        return screen
    }

    private const val RETICLE_TTL = 200_000_000L

    @JvmStatic
    fun shotAimOffset(): Vector2f? {
        val player = Minecraft.getInstance().player ?: return null
        val shot = GunItem.resolveShootDirection(freshMuzzleDirection()?.toVec3(), player)

        val view = BedrockBoneCoordinateTool
            .cameraRotationInverse(Minecraft.getInstance().gameRenderer.mainCamera)
            .invert()
            .transformDirection(
                shot.x.toFloat(), shot.y.toFloat(), shot.z.toFloat(), Vector3f()
            )

        val front = -view.z
        // 60° 的夹子已经挡住背向，这里只是兜底（前向分量非正时 atan2 的符号没有意义）
        if (front <= 1e-6f) return null

        return Vector2f(
            Math.atan2(view.x.toDouble(), front.toDouble()).toFloat(),
            Math.atan2(view.y.toDouble(), front.toDouble()).toFloat()
        )
    }

    private const val FADE_TICKS = 5.0

    @JvmStatic
    fun virtualOffsetRate(projectile: Projectile, partialTick: Float): Double {
        if (!isOwnFreshProjectile(projectile)) return 0.0

        // `- 1`:让第 0 tick 整体落在 0 之前（强度 1），淡出只发生在之后的 [FADE_TICKS] - 1 个 tick 里
        val age = (projectile.tickCount + partialTick - 1.0).coerceAtLeast(0.0)
        return 1 - AnimationCurves.EASE_OUT_CIRC.apply(min(1.0, age / (FADE_TICKS - 1)))
    }

    @JvmStatic
    fun hasVirtualOffset(projectile: Projectile, partialTick: Float): Boolean {
        return virtualOffsetRate(projectile, partialTick) > 0.0
    }

    /** 样本是不是给"本地玩家自己刚打的这一发"准备的：没过期 + 弹射物是本地玩家的 */
    private fun isOwnFreshProjectile(projectile: Projectile): Boolean {
        if (bulletRenderOffset == null) return false
        if (System.nanoTime() - bulletRenderOffsetTime > OFFSET_TTL) return false

        val player = Minecraft.getInstance().player ?: return false
        val owner = projectile.owner ?: return false
        return player.getUUID() == owner.getUUID()
    }

    @JvmStatic
    fun transformVirtualRenderPosition(stack: PoseStack, projectile: Projectile, partialTick: Float) {
        val offset = bulletRenderOffset ?: return

        val rate = virtualOffsetRate(projectile, partialTick)
        if (rate <= 0.0) return

        stack.translate(offset.x * rate, offset.y * rate, offset.z * rate)
    }

    @JvmStatic
    fun registerTooltip() {
        TooltipComponentCallback.EVENT.register { component ->
            when (component) {
                is BocekImageComponent -> ClientBocekImageTooltip(component)
                is CellImageComponent -> ClientCellImageTooltip(component)
                is SentinelImageComponent -> ClientSentinelImageTooltip(component)
                is ChargingStationImageComponent -> ClientChargingStationImageTooltip(component)
                is DogTagImageComponent -> ClientDogTagImageTooltip(component)
                is GunImageComponent -> ClientGunImageTooltip(component)
                is AttachmentImageComponent -> ClientAttachmentImageTooltip(component)
                else -> null
            }
        }
    }

    @JvmStatic
    fun registerRenderers() {
        BlockEntityRenderers.register(ModBlockEntities.CONTAINER) { ContainerBlockEntityRenderer() }
        BlockEntityRenderers.register(ModBlockEntities.FUMO_25) { FuMO25BlockEntityRenderer() }
        BlockEntityRenderers.register(ModBlockEntities.CHARGING_STATION) { ChargingStationBlockEntityRenderer() }
        BlockEntityRenderers.register(ModBlockEntities.SMALL_CONTAINER) { SmallContainerBlockEntityRenderer() }
        BlockEntityRenderers.register(ModBlockEntities.LUCKY_CONTAINER) { LuckyContainerBlockEntityRenderer() }
        BlockEntityRenderers.register(ModBlockEntities.VEHICLE_ASSEMBLING_TABLE) {
            VehicleAssemblingTableBlockEntityRenderer()
        }
        BlockEntityRenderers.register(ModBlockEntities.BLUEPRINT_RESEARCH_TABLE) {
            BlueprintResearchTableBlockEntityRenderer()
        }
    }

    @JvmStatic
    fun registerOverlays() {
        GPWSOverlay.register()
        ClientTickEvents.END_CLIENT_TICK.register {
            VehicleTeamOverlay.onVehicleTeamOverlayClientTick()
            VehicleMainWeaponHudOverlay.onVehicleMainWeaponHudOverlayClientTick()
            Type63InfoOverlay.tracingEntity()
            AircraftHud.onAircraftHudClientTick()
            HelicopterHud.onHelicopterHudClientTick()
            OldAircraftHud.onOldAircraftHudClientTick()
        }
    }

    @JvmStatic
    fun renderOverlays(guiGraphics: GuiGraphics, partialTick: Float) {
        if (mc.player == null) return
        overlays.forEach { it.render(guiGraphics, partialTick) }
    }

    @JvmStatic
    fun registerItemDecorations() {
        // Item decorations are invoked from GuiGraphicsMixin on Fabric 1.20.1.
    }

    @JvmStatic
    fun renderItemDecorations(guiGraphics: GuiGraphics, font: Font, stack: ItemStack, x: Int, y: Int) {
        if (containerDecorator.render(guiGraphics, font, stack, x, y)) return
        if (luckyContainerDecorator.render(guiGraphics, font, stack, x, y)) return
        vehicleKeyDecorator.render(guiGraphics, font, stack, x, y)
    }

    @JvmStatic
    fun onClientSetup() {
        MeleeClientHandler.installDebugHooks()
        MeleeDebugRenderer.register()
        val geoGunRenderer = GeoGunRenderer()
        BuiltInRegistries.ITEM.filterIsInstance<GeoGunItemV2>().forEach { item ->
            BuiltinItemRendererRegistry.INSTANCE.register(item, geoGunRenderer)
        }

        val tm62Renderer = lazy { Tm62ItemRenderer(mc.blockEntityRenderDispatcher, mc.entityModels) }
        BuiltinItemRendererRegistry.INSTANCE.register(
            ModItems.TM_62
        ) { stack, displayContext, poseStack, buffer, packedLight, packedOverlay ->
            tm62Renderer.value.renderByItem(stack, displayContext, poseStack, buffer, packedLight, packedOverlay)
        }

        val type88ClusterGrenadesRenderer =
            lazy { Type88ClusterGrenadesRenderer(mc.blockEntityRenderDispatcher, mc.entityModels) }
        BuiltinItemRendererRegistry.INSTANCE.register(
            ModItems.TYPE_88_CLUSTER_GRENADES
        ) { stack, displayContext, poseStack, buffer, packedLight, packedOverlay ->
            type88ClusterGrenadesRenderer.value.renderByItem(
                stack, displayContext, poseStack, buffer, packedLight, packedOverlay
            )
        }

        val blueprintResearchTableRenderer =
            lazy { BlueprintResearchingTableBlockItemRenderer(mc.blockEntityRenderDispatcher, mc.entityModels) }
        BuiltinItemRendererRegistry.INSTANCE.register(
            ModItems.BLUEPRINT_RESEARCH_TABLE
        ) { stack, displayContext, poseStack, buffer, packedLight, packedOverlay ->
            blueprintResearchTableRenderer.value.renderByItem(
                stack,
                displayContext,
                poseStack,
                buffer,
                packedLight,
                packedOverlay
            )
        }

        val knifeRenderer = lazy { KnifeRenderer(mc.blockEntityRenderDispatcher, mc.entityModels) }
        BuiltinItemRendererRegistry.INSTANCE.register(
            ModItems.KNIFE
        ) { stack, displayContext, poseStack, buffer, packedLight, packedOverlay ->
            knifeRenderer.value.renderByItem(stack, displayContext, poseStack, buffer, packedLight, packedOverlay)
        }

        val knife6kh2Renderer = lazy { Knife6kh2Renderer(mc.blockEntityRenderDispatcher, mc.entityModels) }
        BuiltinItemRendererRegistry.INSTANCE.register(
            ModItems.KNIFE_6KH2
        ) { stack, displayContext, poseStack, buffer, packedLight, packedOverlay ->
            knife6kh2Renderer.value.renderByItem(stack, displayContext, poseStack, buffer, packedLight, packedOverlay)
        }

        val knifeSeitengewehr84Renderer =
            lazy { KnifeSeitengewehr84Renderer(mc.blockEntityRenderDispatcher, mc.entityModels) }
        BuiltinItemRendererRegistry.INSTANCE.register(
            ModItems.KNIFE_SEITENGEWEHR_84
        ) { stack, displayContext, poseStack, buffer, packedLight, packedOverlay ->
            knifeSeitengewehr84Renderer.value.renderByItem(
                stack,
                displayContext,
                poseStack,
                buffer,
                packedLight,
                packedOverlay
            )
        }

        val knifeM1917Renderer = lazy { KnifeM1917Renderer(mc.blockEntityRenderDispatcher, mc.entityModels) }
        BuiltinItemRendererRegistry.INSTANCE.register(
            ModItems.KNIFE_M1917
        ) { stack, displayContext, poseStack, buffer, packedLight, packedOverlay ->
            knifeM1917Renderer.value.renderByItem(stack, displayContext, poseStack, buffer, packedLight, packedOverlay)
        }

        TrinketRendererRegistry.registerRenderer(ModItems.PARACHUTE, ParachuteRenderer())
        TrinketRendererRegistry.registerRenderer(
            ModItems.THERMAL_IMAGING_GOGGLES,
            ThermalImagingGogglesRenderer()
        )
        GeHelmetM35Item.registerRenderer()
        RuChest6b43Item.registerRenderer()
        RuHelmet6b47Item.registerRenderer()
        UsChestIotvItem.registerRenderer()
        UsHelmetPasgtItem.registerRenderer()
        HandsomeGogglesItem.registerRenderer()
    }

    @JvmStatic
    fun registerLayer() {
        EntityModelLayerRegistry.registerModelLayer(ParachuteModel.LAYER_LOCATION, ParachuteModel::createBodyLayer)
        EntityModelLayerRegistry.registerModelLayer(
            ThermalImagingGogglesModel.LAYER_LOCATION,
            ThermalImagingGogglesModel::createBodyLayer
        )
    }
}
