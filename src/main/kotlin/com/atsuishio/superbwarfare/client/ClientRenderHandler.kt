package com.atsuishio.superbwarfare.client

import com.atsuishio.superbwarfare.client.animation.AnimationCurves
import com.atsuishio.superbwarfare.client.decorator.ContainerItemDecorator
import com.atsuishio.superbwarfare.client.decorator.LuckyContainerItemDecorator
import com.atsuishio.superbwarfare.client.model.trinket.ParachuteModel
import com.atsuishio.superbwarfare.client.model.trinket.ThermalImagingGogglesModel
import com.atsuishio.superbwarfare.client.overlay.AmmoBarOverlay
import com.atsuishio.superbwarfare.client.overlay.AmmoCountOverlay
import com.atsuishio.superbwarfare.client.overlay.ArmorPlateOverlay
import com.atsuishio.superbwarfare.client.overlay.CrossHairOverlay
import com.atsuishio.superbwarfare.client.overlay.DroneHudOverlay
import com.atsuishio.superbwarfare.client.overlay.HandsomeFrameOverlay
import com.atsuishio.superbwarfare.client.overlay.HeatBarOverlay
import com.atsuishio.superbwarfare.client.overlay.IFFOverlay
import com.atsuishio.superbwarfare.client.overlay.IglaHudOverlay
import com.atsuishio.superbwarfare.client.overlay.ItemRendererFixOverlay
import com.atsuishio.superbwarfare.client.overlay.JavelinHudOverlay
import com.atsuishio.superbwarfare.client.overlay.KillMessageOverlay
import com.atsuishio.superbwarfare.client.overlay.MortarInfoOverlay
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
import com.atsuishio.superbwarfare.client.renderer.item.BlueprintResearchingTableBlockItemRenderer
import com.atsuishio.superbwarfare.client.renderer.item.Tm62ItemRenderer
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
import com.atsuishio.superbwarfare.item.armor.GeHelmetM35Item
import com.atsuishio.superbwarfare.item.armor.RuChest6b43Item
import com.atsuishio.superbwarfare.item.armor.RuHelmet6b47Item
import com.atsuishio.superbwarfare.item.armor.UsChestIotvItem
import com.atsuishio.superbwarfare.item.armor.UsHelmetPasgtItem
import com.atsuishio.superbwarfare.tools.mc
import com.mojang.blaze3d.vertex.PoseStack
import dev.emi.trinkets.api.client.TrinketRendererRegistry
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.item.ItemStack
import net.minecraft.world.phys.Vec3
import kotlin.math.min

object ClientRenderHandler {
    @JvmStatic
    var bulletRenderOffset: Vec3? = null

    private val containerDecorator = ContainerItemDecorator()
    private val luckyContainerDecorator = LuckyContainerItemDecorator()

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
            VehicleTeamOverlay.onVehicleTeamOverlayClientTick()
            IFFOverlay.onIFFClientTick()
            VehicleMainWeaponHudOverlay.onVehicleMainWeaponHudOverlayClientTick()
            Type63InfoOverlay.tracingEntity()
            AircraftHud.onAircraftHudClientTick()
            HelicopterHud.onHelicopterHudClientTick()
            OldAircraftHud.onOldAircraftHudClientTick()
        }

        HudRenderCallback.EVENT.register { guiGraphics, deltaTracker ->
            JavelinHudOverlay.render(guiGraphics, deltaTracker)
            ArmorPlateOverlay.render(guiGraphics, deltaTracker)
            AmmoBarOverlay.render(guiGraphics, deltaTracker)
            IFFOverlay.render(guiGraphics, deltaTracker)
            VehicleTeamOverlay.render(guiGraphics, deltaTracker)
            IglaHudOverlay.render(guiGraphics, deltaTracker)
            VehicleMainWeaponHudOverlay.render(guiGraphics, deltaTracker)
            VehicleHudOverlay.render(guiGraphics, deltaTracker)
            VehicleCrosshairOverlay.render(guiGraphics, deltaTracker)
            StaminaOverlay.render(guiGraphics, deltaTracker)
            AmmoCountOverlay.render(guiGraphics, deltaTracker)
            ItemRendererFixOverlay.render(guiGraphics, deltaTracker)
            CrossHairOverlay.render(guiGraphics, deltaTracker)
            HeatBarOverlay.render(guiGraphics, deltaTracker)
            DroneHudOverlay.render(guiGraphics, deltaTracker)
            RedTriangleOverlay.render(guiGraphics, deltaTracker)
            HandsomeFrameOverlay.render(guiGraphics, deltaTracker)
            SpyglassRangeOverlay.render(guiGraphics, deltaTracker)
            TowOverlay.render(guiGraphics, deltaTracker)
            MortarInfoOverlay.render(guiGraphics, deltaTracker)
            Type63InfoOverlay.render(guiGraphics, deltaTracker)
            SodayoRocketInfoOverlay.render(guiGraphics, deltaTracker)
            KillMessageOverlay.render(guiGraphics, deltaTracker)
        }
    }

    @JvmStatic
    fun registerItemDecorations() {
    }

    @JvmStatic
    fun renderItemDecorations(guiGraphics: GuiGraphics, font: Font, stack: ItemStack, x: Int, y: Int) {
        if (containerDecorator.render(guiGraphics, font, stack, x, y)) return
        luckyContainerDecorator.render(guiGraphics, font, stack, x, y)
    }

    @JvmStatic
    fun onClientSetup() {
        val tm62Renderer = lazy { Tm62ItemRenderer(mc.blockEntityRenderDispatcher, mc.entityModels) }
        BuiltinItemRendererRegistry.INSTANCE.register(
            ModItems.TM_62
        ) { stack, displayContext, poseStack, buffer, packedLight, packedOverlay ->
            tm62Renderer.value.renderByItem(stack, displayContext, poseStack, buffer, packedLight, packedOverlay)
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

        TrinketRendererRegistry.registerRenderer(ModItems.PARACHUTE, ParachuteRenderer())
        TrinketRendererRegistry.registerRenderer(ModItems.THERMAL_IMAGING_GOGGLES, ThermalImagingGogglesRenderer())
        GeHelmetM35Item.registerRenderer()
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
