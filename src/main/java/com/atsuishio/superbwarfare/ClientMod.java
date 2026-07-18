package com.atsuishio.superbwarfare;

import com.atsuishio.superbwarfare.client.ClickHandler;
import com.atsuishio.superbwarfare.client.ClientRenderHandler;
import com.atsuishio.superbwarfare.client.MouseMovementHandler;
import com.atsuishio.superbwarfare.client.language.ClientLanguageGetter;
import com.atsuishio.superbwarfare.client.model.DragonTeethObjModelLoader;
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
import com.atsuishio.superbwarfare.event.PlayerEventHandler;
import com.atsuishio.superbwarfare.init.*;
import com.atsuishio.superbwarfare.item.common.ammo.PotionMortarShell;
import com.atsuishio.superbwarfare.item.gun.GunGeoItem;
import com.atsuishio.superbwarfare.tools.ResourceOnceLogger;
import com.atsuishio.superbwarfare.tools.VectorUtil;
import fuzs.forgeconfigapiport.api.config.v2.ForgeConfigRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.config.ModConfig;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ClientMod implements ClientModInitializer {

    private static final Set<ResourceLocation> CUSTOM_GUI_ICON_ITEMS = Set.of(
            new ResourceLocation(Mod.MODID, "lunge_mine")
    );

    @Override
    public void onInitializeClient() {
        DragonTeethObjModelLoader.register();
        registerGuiIconModels();
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.BARBED_WIRE.get(), RenderType.cutout());
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
        ClientRenderHandler.registerBlockRenderers();
        ClientRenderHandler.registerLayerDefinitions();
        ClientLanguageGetter.registerReloadListeners();
        KillMessageHandler.registerEvents();
        VectorUtil.registerEvents();
        ClickHandler.registerEvents();
        ResourceOnceLogger.register();
        PotionMortarShell.registerColorHandlers();

        ForgeConfigRegistry.INSTANCE.register(Mod.MODID, ModConfig.Type.CLIENT, ClientConfig.init());
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player != null) {
                PlayerEventHandler.onPlayerTick(client.player, true);
            }

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
}
