package com.atsuishio.superbwarfare;

import com.atsuishio.superbwarfare.client.ClickHandler;
import com.atsuishio.superbwarfare.client.ClientRenderHandler;
import com.atsuishio.superbwarfare.client.MouseMovementHandler;
import com.atsuishio.superbwarfare.client.language.ClientLanguageGetter;
import com.atsuishio.superbwarfare.client.molang.MolangVariable;
import com.atsuishio.superbwarfare.client.renderer.curio.ParachuteRenderer;
import com.atsuishio.superbwarfare.client.renderer.special.ContainerBlockPreview;
import com.atsuishio.superbwarfare.client.screens.FuMO25ScreenHelper;
import com.atsuishio.superbwarfare.client.screens.modsell.ModSellWarningScreen;
import com.atsuishio.superbwarfare.client.sound.ModSoundInstances;
import com.atsuishio.superbwarfare.config.ClientConfig;
import com.atsuishio.superbwarfare.event.ClientEventHandler;
import com.atsuishio.superbwarfare.event.ClientMouseHandler;
import com.atsuishio.superbwarfare.event.KillMessageHandler;
import com.atsuishio.superbwarfare.init.*;
import com.atsuishio.superbwarfare.tools.ResourceOnceLogger;
import com.atsuishio.superbwarfare.tools.VectorUtil;
import fuzs.forgeconfigapiport.api.config.v2.ForgeConfigRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraftforge.fml.config.ModConfig;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

public class ClientMod implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ModKeyMappings.register();
        MouseMovementHandler.init();
        MolangVariable.register();
        ModSoundInstances.init();
        ModEntityRenderers.register();
        ModScreens.register();
        ModParticles.register();
        ModProperties.register();
        ModSellWarningScreen.registerEvents();
        ClientMouseHandler.registerEvents();
        ClientEventHandler.registerEvents();
        ParachuteRenderer.registerRenderer();
        FuMO25ScreenHelper.registerEvents();
        ContainerBlockPreview.registerEvents();
        ClientRenderHandler.registerTooltip();
        ClientRenderHandler.onClientSetup();
        ClientRenderHandler.registerBlockRenderers();
        ClientRenderHandler.registerLayerDefinitions();
        ClientLanguageGetter.registerReloadListeners();
        KillMessageHandler.registerEvents();
        VectorUtil.registerEvents();
        ClickHandler.registerEvents();
        ResourceOnceLogger.register();

        ForgeConfigRegistry.INSTANCE.register(Mod.MODID, ModConfig.Type.CLIENT, ClientConfig.init());
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            List<AbstractMap.SimpleEntry<Runnable, Integer>> actions = new ArrayList<>();
            Mod.CLIENT_QUEUE.forEach(work -> {
                work.setValue(work.getValue() - 1);
                if (work.getValue() == 0)
                    actions.add(work);
            });
            actions.forEach(e -> e.getKey().run());
            Mod.CLIENT_QUEUE.removeAll(actions);
        });
    }
}
