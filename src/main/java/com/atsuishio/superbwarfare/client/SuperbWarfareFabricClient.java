package com.atsuishio.superbwarfare.client;

import com.atsuishio.superbwarfare.Mod;
import com.atsuishio.superbwarfare.client.language.ClientLanguageGetter;
import com.atsuishio.superbwarfare.client.map.TacticalMapChunkListener;
import com.atsuishio.superbwarfare.client.model.DragonTeethObjModelLoader;
import com.atsuishio.superbwarfare.client.shader.ThermalShaderHandler;
import com.atsuishio.superbwarfare.client.renderer.curio.ParachuteRenderer;
import com.atsuishio.superbwarfare.client.screens.FuMO25ScreenHelper;
import com.atsuishio.superbwarfare.client.renderer.special.ContainerBlockPreview;
import com.atsuishio.superbwarfare.client.renderer.special.TowingChainRenderer;
import com.atsuishio.superbwarfare.client.renderer.SyncedEntityWorldRenderer;
import com.atsuishio.superbwarfare.client.renderer.ModParticleRenderTypes;
import com.atsuishio.superbwarfare.client.molang.MolangVariable;
import com.atsuishio.superbwarfare.client.screens.SnapshotWarningScreen;
import com.atsuishio.superbwarfare.compat.ponder.SBWPonderPlugin;
import com.atsuishio.superbwarfare.data.DataLoader;
import com.atsuishio.superbwarfare.event.ClientEventHandler;
import com.atsuishio.superbwarfare.event.ClientMouseHandler;
import com.atsuishio.superbwarfare.event.KillMessageHandler;
import com.atsuishio.superbwarfare.entity.vehicle.base.ArtilleryEntity;
import com.atsuishio.superbwarfare.init.*;
import com.atsuishio.superbwarfare.item.projectile.PotionMortarShellItem;
import com.atsuishio.superbwarfare.network.NetworkRegistryKt;
import com.atsuishio.superbwarfare.sound.SoundLimit;
import net.createmod.ponder.foundation.PonderIndex;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.fabricmc.loader.api.FabricLoader;

public class SuperbWarfareFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        DragonTeethObjModelLoader.register();
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
        TowingChainRenderer.init();
        SyncedEntityWorldRenderer.init();
        ModParticleRenderTypes.init();
        SnapshotWarningScreen.register();
        TacticalMapChunkListener.init();

        MouseMovementHandler.init();
        MolangVariable.INSTANCE.register();
        ModSoundInstances.init();
        SoundLimit.INSTANCE.init();
        if (FabricLoader.getInstance().isModLoaded("ponder")) {
            PonderIndex.addPlugin(SBWPonderPlugin.INSTANCE);
        }
        ThermalShaderHandler.register();
        ClientEventHandler.register();
        NetworkRegistryKt.registerClientReceivers();
        ClientEntityEvents.ENTITY_LOAD.register((entity, world) -> {
            if (entity instanceof ArtilleryEntity artillery) {
                artillery.initializeShootVec();
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
}
