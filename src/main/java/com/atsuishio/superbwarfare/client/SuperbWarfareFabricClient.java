package com.atsuishio.superbwarfare.client;

import com.atsuishio.superbwarfare.client.renderer.curio.ParachuteRenderer;
import com.atsuishio.superbwarfare.client.renderer.special.ContainerBlockPreview;
import com.atsuishio.superbwarfare.client.sound.ModSoundInstances;
import com.atsuishio.superbwarfare.client.molang.MolangVariable;
import com.atsuishio.superbwarfare.init.*;
import net.fabricmc.api.ClientModInitializer;

public class SuperbWarfareFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ModScreens.init();
        ModEntityRenderers.init();
        ModKeyMappings.init();
        ModProperties.init();
        ModParticles.init();

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
    }
}
