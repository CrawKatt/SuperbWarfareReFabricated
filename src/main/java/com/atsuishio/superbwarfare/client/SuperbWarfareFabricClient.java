package com.atsuishio.superbwarfare.client;

import com.atsuishio.superbwarfare.Mod;
import com.atsuishio.superbwarfare.client.language.ClientLanguageGetter;
import com.atsuishio.superbwarfare.client.shader.ThermalShaderHandler;
import com.atsuishio.superbwarfare.client.renderer.curio.ParachuteRenderer;
import com.atsuishio.superbwarfare.client.screens.FuMO25ScreenHelper;
import com.atsuishio.superbwarfare.client.renderer.special.ContainerBlockPreview;
import com.atsuishio.superbwarfare.client.molang.MolangVariable;
import com.atsuishio.superbwarfare.config.ClientConfig;
import com.atsuishio.superbwarfare.data.DataLoader;
import com.atsuishio.superbwarfare.event.ClientEventHandler;
import com.atsuishio.superbwarfare.event.ClientMouseHandler;
import com.atsuishio.superbwarfare.event.KillMessageHandler;
import com.atsuishio.superbwarfare.init.*;
import com.atsuishio.superbwarfare.item.projectile.PotionMortarShellItem;
import com.atsuishio.superbwarfare.network.NetworkRegistryKt;
import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.NeoForgeConfigRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.neoforged.fml.config.ModConfig;

public class SuperbWarfareFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        NeoForgeConfigRegistry.INSTANCE.register(Mod.MODID, ModConfig.Type.CLIENT, ClientConfig.init());
        ModScreens.init();
        ModEntityRenderers.init();
        ModKeyMappings.init();
        ModProperties.init();
        ModParticles.init();
        DataLoader.registerClient();

        ClientLanguageGetter.register();
        ClientRenderHandler.registerLayer();
        ClientRenderHandler.registerRenderers();
        ClientRenderHandler.registerOverlays();
        ClientRenderHandler.registerTooltip();
        ClientRenderHandler.onClientSetup();
        ClientRenderHandler.registerItemDecorations();
        PotionMortarShellItem.registerColorHandler();
        ParachuteRenderer.onRenderLevelStage();
        ContainerBlockPreview.init();

        MouseMovementHandler.init();
        MolangVariable.register();
        ModSoundInstances.init();
        ThermalShaderHandler.register();
        ModEventHandlers.initClient();
        NetworkRegistryKt.registerClientReceivers();

        registerClientTicks();
        registerRenderFrames();
    }

    private static void registerClientTicks() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            Mod.tickClient();
            ClientEventHandler.handleClientTick();
            ClientEventHandler.handleWeaponBreathSway();
            ClientMouseHandler.handleClientTick(client);
            KillMessageHandler.onClientTick();
            FuMO25ScreenHelper.onClientTick();
        });
    }

    private static void registerRenderFrames() {
        WorldRenderEvents.START.register(context -> {
            ClientEventHandler.handleWeaponFire();
            ClientEventHandler.handleVehicleFire();
        });
    }
}
