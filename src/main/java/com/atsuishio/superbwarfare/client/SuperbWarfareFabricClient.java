package com.atsuishio.superbwarfare.client;

import com.atsuishio.superbwarfare.Mod;
import com.atsuishio.superbwarfare.client.language.ClientLanguageGetter;
import com.atsuishio.superbwarfare.client.overlay.CrossHairOverlay;
import com.atsuishio.superbwarfare.client.renderer.curio.ParachuteRenderer;
import com.atsuishio.superbwarfare.client.screens.FuMO25ScreenHelper;
import com.atsuishio.superbwarfare.client.renderer.special.ContainerBlockPreview;
import com.atsuishio.superbwarfare.client.sound.ModSoundInstances;
import com.atsuishio.superbwarfare.client.molang.MolangVariable;
import com.atsuishio.superbwarfare.event.ClientEventHandler;
import com.atsuishio.superbwarfare.event.ClientMouseHandler;
import com.atsuishio.superbwarfare.event.KillMessageHandler;
import com.atsuishio.superbwarfare.init.*;
import com.atsuishio.superbwarfare.network.NetworkRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class SuperbWarfareFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ModScreens.init();
        ModEntityRenderers.init();
        ModKeyMappings.init();
        ModProperties.init();
        ModParticles.init();

        ClientLanguageGetter.register();
        ClientRenderHandler.registerLayer();
        ClientRenderHandler.registerRenderers();
        ClientRenderHandler.registerOverlays();
        ClientRenderHandler.registerTooltip();
        ClientRenderHandler.onClientSetup();
        ClientRenderHandler.registerItemDecorations();
        ParachuteRenderer.onRenderLevelStage();
        ContainerBlockPreview.init();

        MouseMovementHandler.init();
        MolangVariable.register();
        ModSoundInstances.init();
        ModEventHandlers.initClient();
        NetworkRegistry.registerClientReceivers();

        registerClientTicks();
    }

    private static void registerClientTicks() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            Mod.tickClient();
            ClientEventHandler.handleClientTick();
            ClientEventHandler.handleWeaponBreathSway();
            ClientEventHandler.handleWeaponFire();
            ClientEventHandler.handleVehicleFire();
            ClientMouseHandler.handleClientTick(client);
            KillMessageHandler.onClientTick();
            CrossHairOverlay.onClientTick();
            FuMO25ScreenHelper.onClientTick();
        });
    }
}
