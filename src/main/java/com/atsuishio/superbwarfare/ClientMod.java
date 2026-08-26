package com.atsuishio.superbwarfare;

import com.atsuishio.superbwarfare.client.ClientRenderHandler;
import com.atsuishio.superbwarfare.client.MouseMovementHandler;
import com.atsuishio.superbwarfare.client.language.ClientLanguageGetter;
import com.atsuishio.superbwarfare.client.lighting.ClientLightingHandler;
import com.atsuishio.superbwarfare.client.map.TacticalMapChunkListener;
import com.atsuishio.superbwarfare.client.model.DragonTeethObjModelLoader;
import com.atsuishio.superbwarfare.client.molang.MolangVariable;
import com.atsuishio.superbwarfare.client.overlay.OverlayTraceHandler;
import com.atsuishio.superbwarfare.client.renderer.special.ContainerBlockPreview;
import com.atsuishio.superbwarfare.client.renderer.SyncedEntityWorldRenderer;
import com.atsuishio.superbwarfare.client.renderer.curio.ParachuteRenderer;
import com.atsuishio.superbwarfare.client.renderer.special.TowingChainRenderer;
import com.atsuishio.superbwarfare.client.screens.FuMO25ScreenHelper;
import com.atsuishio.superbwarfare.client.screens.SnapshotWarningScreen;
import com.atsuishio.superbwarfare.config.ClientConfig;
import com.atsuishio.superbwarfare.data.DataLoader;
import com.atsuishio.superbwarfare.event.ClientEventHandler;
import com.atsuishio.superbwarfare.event.ClientMouseHandler;
import com.atsuishio.superbwarfare.event.KillMessageHandler;
import com.atsuishio.superbwarfare.entity.vehicle.base.ArtilleryEntity;
import com.atsuishio.superbwarfare.init.ModBlocks;
import com.atsuishio.superbwarfare.init.ModEntityRenderers;
import com.atsuishio.superbwarfare.init.ModKeyMappings;
import com.atsuishio.superbwarfare.init.ModParticles;
import com.atsuishio.superbwarfare.init.ModProperties;
import com.atsuishio.superbwarfare.init.ModScreens;
import com.atsuishio.superbwarfare.init.ModSoundInstances;
import com.atsuishio.superbwarfare.item.gun.GunGeoItem;
import com.atsuishio.superbwarfare.item.projectile.PotionMortarShellItem;
import com.atsuishio.superbwarfare.tools.ResourceOnceLogger;
import fuzs.forgeconfigapiport.api.config.v2.ForgeConfigRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.config.ModConfig;

import java.util.Set;

public class ClientMod implements ClientModInitializer {

    private static final Set<ResourceLocation> CUSTOM_GUI_ICON_ITEMS = Set.of(
            new ResourceLocation(Mod.MODID, "lunge_mine")
    );

    @Override
    public void onInitializeClient() {
        DragonTeethObjModelLoader.register();
        registerGuiIconModels();
        registerBuiltinResourcePacks();

        ForgeConfigRegistry.INSTANCE.register(Mod.MODID, ModConfig.Type.CLIENT, ClientConfig.init());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.BARBED_WIRE, RenderType.cutout());
        ModScreens.INSTANCE.register();
        ModEntityRenderers.init();
        ModKeyMappings.init();
        ModProperties.init();
        ModParticles.init();
        DataLoader.registerClient();

        ClientLanguageGetter.register();
        ResourceOnceLogger.register();
        ClientRenderHandler.registerLayer();
        ClientRenderHandler.registerRenderers();
        ClientRenderHandler.registerOverlays();
        ClientRenderHandler.registerTooltip();
        ClientRenderHandler.onClientSetup();
        ClientRenderHandler.registerItemDecorations();
        PotionMortarShellItem.registerColorHandler();
        ParachuteRenderer.onRenderLevelStage();
        ContainerBlockPreview.init();
        SnapshotWarningScreen.register();

        MouseMovementHandler.init();
        ClientLightingHandler.register();
        MolangVariable.register();
        ModSoundInstances.init();
        ClientEventHandler.register();
        TacticalMapChunkListener.register();
        SyncedEntityWorldRenderer.register();
        TowingChainRenderer.register();
        OverlayTraceHandler.register();
        ClientEntityEvents.ENTITY_LOAD.register((entity, world) -> {
            if (entity instanceof ArtilleryEntity artillery) {
                artillery.initializeShootVector();
            }
        });

        registerClientTicks();
    }

    private static void registerClientTicks() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            Mod.tickClient();
            ClientEventHandler.handleClientTick();
            ClientMouseHandler.handleClientTick(client);
            KillMessageHandler.onClientTick();
            FuMO25ScreenHelper.onClientTick();
        });
    }

    private static void registerGuiIconModels() {
        ModelLoadingPlugin.register(context -> BuiltInRegistries.ITEM.forEach(item -> {
            ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(item);
            if (item instanceof GunGeoItem || CUSTOM_GUI_ICON_ITEMS.contains(itemId)) {
                context.addModels(new ModelResourceLocation(itemId.withPath(path -> path + "_icon"), "inventory"));
            }
            if (CUSTOM_GUI_ICON_ITEMS.contains(itemId)) {
                context.addModels(new ModelResourceLocation(itemId.withPath(path -> path + "_3d"), "inventory"));
            }
        }));
    }

    private static void registerBuiltinResourcePacks() {
        FabricLoader.getInstance().getModContainer(Mod.MODID).ifPresent(container ->
                ResourceManagerHelper.registerBuiltinResourcePack(
                        Mod.loc("sbw_legacy"),
                        container,
                        Component.translatable("pack.superbwarfare.sbw_legacy"),
                        ResourcePackActivationType.NORMAL
                )
        );
    }
}
