package com.atsuishio.superbwarfare.client

import com.atsuishio.superbwarfare.accessor.RenderTargetStencilAccessor
import com.atsuishio.superbwarfare.client.ClientRenderHandler.OFFSET_TTL
import com.atsuishio.superbwarfare.client.ClientRenderHandler.bulletRenderOffset
import com.atsuishio.superbwarfare.client.animation.AnimationCurves
import com.atsuishio.superbwarfare.client.decorator.ContainerItemDecorator
import com.atsuishio.superbwarfare.client.decorator.LuckyContainerItemDecorator
import com.atsuishio.superbwarfare.client.decorator.VehicleKeyItemDecorator
import com.atsuishio.superbwarfare.client.gun.MeleeClientHandler
import com.atsuishio.superbwarfare.client.model.trinket.ParachuteModel
import com.atsuishio.superbwarfare.client.model.trinket.ThermalImagingGogglesModel
import com.atsuishio.superbwarfare.client.overlay.AmmoBarOverlay
import com.atsuishio.superbwarfare.client.overlay.AmmoCountOverlay
import com.atsuishio.superbwarfare.client.overlay.ArmorPlateOverlay
import com.atsuishio.superbwarfare.client.overlay.CrossHairOverlay
import com.atsuishio.superbwarfare.client.overlay.DroneHudOverlay
import com.atsuishio.superbwarfare.client.overlay.GPWSOverlay
import com.atsuishio.superbwarfare.client.overlay.HandsomeFrameOverlay
import com.atsuishio.superbwarfare.client.overlay.HeatBarOverlay
import com.atsuishio.superbwarfare.client.overlay.IFFOverlay
import com.atsuishio.superbwarfare.client.overlay.IglaHudOverlay
import com.atsuishio.superbwarfare.client.overlay.ItemRendererFixOverlay
import com.atsuishio.superbwarfare.client.overlay.JavelinHudOverlay
import com.atsuishio.superbwarfare.client.overlay.KillMessageOverlay
import com.atsuishio.superbwarfare.client.overlay.MortarInfoOverlay
import com.atsuishio.superbwarfare.client.overlay.OverlayTraceHandler
import com.atsuishio.superbwarfare.client.overlay.RedTriangleOverlay
import com.atsuishio.superbwarfare.client.overlay.SodayoRocketInfoOverlay
import com.atsuishio.superbwarfare.client.overlay.SpyglassRangeOverlay
import com.atsuishio.superbwarfare.client.overlay.StaminaOverlay
import com.atsuishio.superbwarfare.client.overlay.TowOverlay
import com.atsuishio.superbwarfare.client.overlay.Type63InfoOverlay
import com.atsuishio.superbwarfare.client.overlay.VehicleCrosshairOverlay
import com.atsuishio.superbwarfare.client.overlay.VehicleHudOverlay
import com.atsuishio.superbwarfare.client.overlay.VehicleMainWeaponHudOverlay
import com.atsuishio.superbwarfare.client.overlay.VehicleTeamOverlay
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
import com.atsuishio.superbwarfare.client.renderer.special.MeleeDebugRenderer
import com.atsuishio.superbwarfare.client.renderer.curio.ParachuteRenderer
import com.atsuishio.superbwarfare.client.renderer.curio.ThermalImagingGogglesRenderer
import com.atsuishio.superbwarfare.client.renderer.item.*
import com.atsuishio.superbwarfare.client.tooltip.ClientAttachmentImageTooltip
import com.atsuishio.superbwarfare.client.tooltip.ClientBocekImageTooltip
import com.atsuishio.superbwarfare.client.tooltip.ClientCellImageTooltip
import com.atsuishio.superbwarfare.client.tooltip.ClientChargingStationImageTooltip
import com.atsuishio.superbwarfare.client.tooltip.ClientDogTagImageTooltip
import com.atsuishio.superbwarfare.client.tooltip.ClientGunImageTooltip
import com.atsuishio.superbwarfare.client.tooltip.ClientSentinelImageTooltip
import com.atsuishio.superbwarfare.client.tooltip.component.AttachmentImageComponent
import com.atsuishio.superbwarfare.client.tooltip.component.BocekImageComponent
import com.atsuishio.superbwarfare.client.tooltip.component.CellImageComponent
import com.atsuishio.superbwarfare.client.tooltip.component.ChargingStationImageComponent
import com.atsuishio.superbwarfare.client.tooltip.component.DogTagImageComponent
import com.atsuishio.superbwarfare.client.tooltip.component.GunImageComponent
import com.atsuishio.superbwarfare.client.tooltip.component.SentinelImageComponent
import com.atsuishio.superbwarfare.client.renderer.special.RadiationGlowRenderer
import com.atsuishio.superbwarfare.client.tooltip.*
import com.atsuishio.superbwarfare.client.tooltip.component.*
import com.atsuishio.superbwarfare.init.ModBlockEntities
import com.atsuishio.superbwarfare.init.ModItems
import com.atsuishio.superbwarfare.item.gun.GeoGunItemV2
import com.atsuishio.superbwarfare.item.armor.GeHelmetM35Item
import com.atsuishio.superbwarfare.item.armor.HandsomeGogglesItem
import com.atsuishio.superbwarfare.item.armor.RuChest6b43Item
import com.atsuishio.superbwarfare.item.armor.RuHelmet6b47Item
import com.atsuishio.superbwarfare.item.armor.UsChestIotvItem
import com.atsuishio.superbwarfare.item.armor.UsHelmetPasgtItem
import com.atsuishio.superbwarfare.tools.mc
import com.atsuishio.superbwarfare.item.gun.GunItem
import com.atsuishio.superbwarfare.tools.BedrockBoneCoordinateTool
import com.atsuishio.superbwarfare.tools.toVec3
import com.mojang.blaze3d.vertex.PoseStack
import dev.emi.trinkets.api.client.TrinketRendererRegistry
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback
import net.minecraft.client.DeltaTracker
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.LayeredDraw
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.client.model.EntityModel
import net.minecraft.client.renderer.entity.LivingEntityRenderer
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.item.ItemStack
import net.minecraft.world.phys.Vec3

import org.joml.Vector2f
import org.joml.Vector3f
import kotlin.math.atan2
import kotlin.math.min

object ClientRenderHandler {
    private val containerDecorator = ContainerItemDecorator()
    private val luckyContainerDecorator = LuckyContainerItemDecorator()
    private val vehicleKeyDecorator = VehicleKeyItemDecorator()
    private val overlays = listOf<LayeredDraw.Layer>(
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

    private var gunRollTime: Long = 0L

    var gunRoll: Float = 0f
        set(value) {
            field = value
            gunRollTime = System.nanoTime()
        }

    @JvmStatic
    fun freshGunRoll(): Float? {
        if (System.nanoTime() - gunRollTime > GUN_ROLL_TTL) return null
        return gunRoll
    }

    private const val GUN_ROLL_TTL = 200_000_000L

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
            atan2(view.x.toDouble(), front.toDouble()).toFloat(),
            atan2(view.y.toDouble(), front.toDouble()).toFloat()
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
        // `/sbw melee force` 的客户端实现挂点（判定只在客户端做）
        MeleeClientHandler.installDebugHooks()
    }

    @JvmStatic
    fun registerRenderers() {
        addRadiationGlowLayers()
        MeleeDebugRenderer.init()
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
    fun addRadiationGlowLayers() {
        net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback.EVENT.register { type, renderer, helper, _ ->
            attachRadiationGlowLayer(type, renderer, helper)
        }
    }

    @JvmStatic
    @Suppress("UNCHECKED_CAST")
    fun attachRadiationGlowLayer(
        type: EntityType<out LivingEntity>,
        renderer: LivingEntityRenderer<*, *>,
        helper: net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback.RegistrationHelper
    ) {
        if (type == EntityType.ENDER_DRAGON) return
        val livingRenderer = try {
            renderer as? LivingEntityRenderer<LivingEntity, EntityModel<LivingEntity>>
        } catch (_: Exception) {
            null
        }
        livingRenderer?.let { helper.register(RadiationGlowRenderer(it)) }
    }

    @JvmStatic
    fun registerOverlays() {
        ClientTickEvents.END_CLIENT_TICK.register {
            OverlayTraceHandler.onOverlayTraceClientTick()
            VehicleMainWeaponHudOverlay.onVehicleMainWeaponHudOverlayClientTick()
            GPWSOverlay.onClientTick()
            AircraftHud.onAircraftHudClientTick()
            HelicopterHud.onHelicopterHudClientTick()
            OldAircraftHud.onOldAircraftHudClientTick()
        }

    }

    @JvmStatic
    fun renderOverlays(guiGraphics: GuiGraphics, deltaTracker: DeltaTracker) {
        guiGraphics.pose().pushPose()
        guiGraphics.pose().translate(0f, 0f, -LayeredDraw.Z_SEPARATION * overlays.size)
        overlays.forEach {
            it.render(guiGraphics, deltaTracker)
            guiGraphics.pose().translate(0f, 0f, LayeredDraw.Z_SEPARATION)
        }
        guiGraphics.pose().popPose()
    }

    @JvmStatic
    fun registerItemDecorations() {
    }

    @JvmStatic
    fun renderItemDecorations(guiGraphics: GuiGraphics, font: Font, stack: ItemStack, x: Int, y: Int) {
        if (containerDecorator.render(guiGraphics, font, stack, x, y)) return
        if (luckyContainerDecorator.render(guiGraphics, font, stack, x, y)) return
        vehicleKeyDecorator.render(guiGraphics, font, stack, x, y)
    }

    @JvmStatic
    fun onClientSetup() {
        val mainRenderTarget = mc.mainRenderTarget as RenderTargetStencilAccessor
        val stencilWasEnabled = mainRenderTarget.`superbwarfare$isStencilEnabled`()
        mainRenderTarget.`superbwarfare$enableStencil`()
        if (!stencilWasEnabled) {
            mc.levelRenderer.graphicsChanged()
        }

        BuiltInRegistries.ITEM.filterIsInstance<GeoGunItemV2>().forEach { item ->
            BuiltinItemRendererRegistry.INSTANCE.register(item, item.createRenderer())
        }

        val renderers: List<Pair<net.minecraft.world.item.Item, Lazy<BlockEntityWithoutLevelRenderer>>> = listOf(
            ModItems.TM_62 to lazy { Tm62ItemRenderer(mc.blockEntityRenderDispatcher, mc.entityModels) },
            ModItems.BLUEPRINT_RESEARCH_TABLE to lazy { BlueprintResearchingTableBlockItemRenderer(mc.blockEntityRenderDispatcher, mc.entityModels) },
            ModItems.CONTAINER to lazy { ContainerBlockItemRenderer(mc.blockEntityRenderDispatcher, mc.entityModels) },
            ModItems.LUCKY_CONTAINER to lazy { LuckyContainerBlockItemRenderer(mc.blockEntityRenderDispatcher, mc.entityModels) },
            ModItems.SMALL_CONTAINER to lazy { SmallContainerBlockItemRenderer(mc.blockEntityRenderDispatcher, mc.entityModels) },
            ModItems.VEHICLE_ASSEMBLING_TABLE to lazy { VehicleAssemblingTableBlockItemRenderer(mc.blockEntityRenderDispatcher, mc.entityModels) },
            ModItems.HAND_GRENADE to lazy { HandGrenadeRenderer(mc.blockEntityRenderDispatcher, mc.entityModels) },
            ModItems.TYPE_88_CLUSTER_GRENADES to lazy { Type88ClusterGrenadesRenderer(mc.blockEntityRenderDispatcher, mc.entityModels) },
            ModItems.SKIN_SPRAY to lazy { SkinSprayRenderer(mc.blockEntityRenderDispatcher, mc.entityModels) },
            ModItems.PTKM_1R to lazy { Ptkm1rItemRenderer(mc.blockEntityRenderDispatcher, mc.entityModels) },
            ModItems.MILITARY_SHOVEL to lazy { MilitaryShovelRenderer(mc.blockEntityRenderDispatcher, mc.entityModels) },
            ModItems.KNIFE to lazy { KnifeRenderer(mc.blockEntityRenderDispatcher, mc.entityModels) },
            ModItems.KNIFE_6KH2 to lazy { Knife6kh2Renderer(mc.blockEntityRenderDispatcher, mc.entityModels) },
            ModItems.KNIFE_SEITENGEWEHR_84 to lazy { KnifeSeitengewehr84Renderer(mc.blockEntityRenderDispatcher, mc.entityModels) },
            ModItems.KNIFE_M1917 to lazy { KnifeM1917Renderer(mc.blockEntityRenderDispatcher, mc.entityModels) }
        )
        renderers.forEach { (item, renderer) ->
            BuiltinItemRendererRegistry.INSTANCE.register(item) { stack, displayContext, poseStack, buffer, light, overlay ->
                renderer.value.renderByItem(stack, displayContext, poseStack, buffer, light, overlay)
            }
        }

        TrinketRendererRegistry.registerRenderer(ModItems.PARACHUTE, ParachuteRenderer())
        TrinketRendererRegistry.registerRenderer(ModItems.THERMAL_IMAGING_GOGGLES, ThermalImagingGogglesRenderer())
        GeHelmetM35Item.registerRenderer()
        HandsomeGogglesItem.registerRenderer()
        RuChest6b43Item.registerRenderer()
        RuHelmet6b47Item.registerRenderer()
        UsChestIotvItem.registerRenderer()
        UsHelmetPasgtItem.registerRenderer()
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
