package com.atsuishio.superbwarfare.client

import com.atsuishio.superbwarfare.client.animation.AnimationCurves
import com.atsuishio.superbwarfare.client.decorator.ContainerItemDecorator
import com.atsuishio.superbwarfare.client.decorator.LuckyContainerItemDecorator
import com.atsuishio.superbwarfare.client.decorator.VehicleKeyItemDecorator
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
import com.atsuishio.superbwarfare.client.renderer.curio.ParachuteRenderer
import com.atsuishio.superbwarfare.client.renderer.curio.ThermalImagingGogglesRenderer
import com.atsuishio.superbwarfare.client.renderer.item.*
import com.atsuishio.superbwarfare.client.renderer.gun.GeoGunRenderer
import com.atsuishio.superbwarfare.client.tooltip.ClientBocekImageTooltip
import com.atsuishio.superbwarfare.client.tooltip.ClientCellImageTooltip
import com.atsuishio.superbwarfare.client.tooltip.ClientChargingStationImageTooltip
import com.atsuishio.superbwarfare.client.tooltip.ClientDogTagImageTooltip
import com.atsuishio.superbwarfare.client.tooltip.ClientGunImageTooltip
import com.atsuishio.superbwarfare.client.tooltip.ClientSentinelImageTooltip
import com.atsuishio.superbwarfare.client.tooltip.component.BocekImageComponent
import com.atsuishio.superbwarfare.client.tooltip.component.CellImageComponent
import com.atsuishio.superbwarfare.client.tooltip.component.ChargingStationImageComponent
import com.atsuishio.superbwarfare.client.tooltip.component.DogTagImageComponent
import com.atsuishio.superbwarfare.client.tooltip.component.GunImageComponent
import com.atsuishio.superbwarfare.client.tooltip.component.SentinelImageComponent
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
import com.mojang.blaze3d.vertex.PoseStack
import dev.emi.trinkets.api.client.TrinketRendererRegistry
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback
import net.minecraft.client.DeltaTracker
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.LayeredDraw
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.item.ItemStack
import net.minecraft.world.phys.Vec3
import kotlin.math.min

object ClientRenderHandler {
    @JvmStatic
    var bulletRenderOffset: Vec3? = null

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

    @JvmStatic
    fun transformVirtualRenderPosition(stack: PoseStack, projectile: Projectile, partialTick: Float) {
        val bulletOffset = bulletRenderOffset ?: return
        val player = mc.player

        if (player == null || projectile.owner == null || player.uuid != projectile.owner!!.uuid) return

        val rate = 1 - AnimationCurves.EASE_OUT_CIRC.apply(min(1.0, (projectile.tickCount + partialTick) / 5.0))
        val offset = bulletOffset.subtract(projectile.position()).multiply(rate, rate, rate)
        stack.translate(offset.x, offset.y, offset.z)
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
        val geoGunRenderer = GeoGunRenderer()
        BuiltInRegistries.ITEM.filterIsInstance<GeoGunItemV2>().forEach { item ->
            BuiltinItemRendererRegistry.INSTANCE.register(item, geoGunRenderer)
        }

        val renderers: List<Pair<net.minecraft.world.item.Item, Lazy<BlockEntityWithoutLevelRenderer>>> = listOf(
            ModItems.TM_62 to lazy { Tm62ItemRenderer(mc.blockEntityRenderDispatcher, mc.entityModels) },
            ModItems.BLUEPRINT_RESEARCH_TABLE to lazy { BlueprintResearchingTableBlockItemRenderer(mc.blockEntityRenderDispatcher, mc.entityModels) },
            ModItems.CONTAINER to lazy { ContainerBlockItemRenderer(mc.blockEntityRenderDispatcher, mc.entityModels) },
            ModItems.LUCKY_CONTAINER to lazy { LuckyContainerBlockItemRenderer(mc.blockEntityRenderDispatcher, mc.entityModels) },
            ModItems.SMALL_CONTAINER to lazy { SmallContainerBlockItemRenderer(mc.blockEntityRenderDispatcher, mc.entityModels) },
            ModItems.VEHICLE_ASSEMBLING_TABLE to lazy { VehicleAssemblingTableBlockItemRenderer(mc.blockEntityRenderDispatcher, mc.entityModels) },
            ModItems.HAND_GRENADE to lazy { HandGrenadeRenderer(mc.blockEntityRenderDispatcher, mc.entityModels) },
            ModItems.SKIN_SPRAY to lazy { SkinSprayRenderer(mc.blockEntityRenderDispatcher, mc.entityModels) },
            ModItems.PTKM_1R to lazy { Ptkm1rItemRenderer(mc.blockEntityRenderDispatcher, mc.entityModels) },
            ModItems.MILITARY_SHOVEL to lazy { MilitaryShovelRenderer(mc.blockEntityRenderDispatcher, mc.entityModels) }
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
