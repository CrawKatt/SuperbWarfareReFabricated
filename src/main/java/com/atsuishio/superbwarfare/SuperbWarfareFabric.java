package com.atsuishio.superbwarfare;

import com.atsuishio.superbwarfare.block.entity.FuMO25BlockEntity;
import com.atsuishio.superbwarfare.command.CommandRegister;
import com.atsuishio.superbwarfare.compat.thermoo.ThermooCompatHandler;
import com.atsuishio.superbwarfare.config.CommonConfig;
import com.atsuishio.superbwarfare.config.ServerConfig;
import com.atsuishio.superbwarfare.data.CustomData;
import com.atsuishio.superbwarfare.data.DataLoader;
import com.atsuishio.superbwarfare.data.container.ContainerDataManager;
import com.atsuishio.superbwarfare.data.vehicle.VehicleDataTool;
import com.atsuishio.superbwarfare.init.*;
import com.atsuishio.superbwarfare.event.HitboxHelperEventHandler;
import com.atsuishio.superbwarfare.event.PlayerEventHandler;
import com.atsuishio.superbwarfare.item.container.ContainerBlockItem;
import com.atsuishio.superbwarfare.tiers.ModArmorMaterials;
import com.atsuishio.superbwarfare.tools.GunsTool;
import com.atsuishio.superbwarfare.tools.ResourceOnceLogger;
import com.atsuishio.superbwarfare.world.saveddata.TDMSavedData;
import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.NeoForgeConfigRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.chat.Component;
import net.neoforged.fml.config.ModConfig;
import software.bernie.geckolib.constant.dataticket.SerializableDataTicket;
import software.bernie.geckolib.util.GeckoLibUtil;

/*
public class SuperbWarfareFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        Mod.LOGGER.info("Initializing Superb Warfare (Fabric)");

        triggerInit();
        callInits();

        NetworkRegistry.registerPayloads();
        VehicleDataTool.register();
        GunsTool.register();
        DataLoader.register();

        ContainerBlockItem.registerContainers();
        ModCapabilities.init();
        CustomData.load();

        CommandRegister.register();
        ContainerDataManager.register();
        TDMSavedData.register();
        ModDataComponents.init();

        ResourceOnceLogger.register();
        ThermooCompatHandler.init();
        registerBuiltinResourcePacks();
        registerDataTickets();
        registerTicks();
    }

    private void registerTicks() {
        ServerTickEvents.START_SERVER_TICK.register(server -> {
            for (var player : server.getPlayerList().getPlayers()) {
                ThermooCompatHandler.onPlayerInVehicle(player);
            }
        });

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            Mod.tickServer();
            for (var player : server.getPlayerList().getPlayers()) {
                PlayerEventHandler.onPlayerTick(player);
                HitboxHelperEventHandler.onPlayerTick(player);
            }
        });
    }

    private void triggerInit() {
        ModItems.registerBlockItems();
        ModItems.registerPerkItems();
        ModItems.registerSpawnEggs();
        ModItems.registerDispenserBehavior();

        NeoForgeConfigRegistry.INSTANCE.register(Mod.MODID, ModConfig.Type.COMMON, CommonConfig.init());
        NeoForgeConfigRegistry.INSTANCE.register(Mod.MODID, ModConfig.Type.SERVER, ServerConfig.init());
    }

    private void callInits() {
        ModBlocks.init();
        ModBlockEntities.init();
        ModEntities.init();
        ModMenuTypes.init();
        ModSounds.init();
        ModMobEffects.init();
        ModParticleTypes.init();
        ModPotions.init();
        ModRecipes.init();
        ModArmorMaterials.register();
        ModAttributes.init();
        ModCriteriaTriggers.init();
        ModCommandArguments.init();
        ModTabs.init();
        ModVillagers.init();
        ModSerializers.init();
        ModPerks.init();
        ModDamageTypes.init();
        ModEventHandlers.init();
        ModTags.init();

        ModEntities.registerAttributes();
        ModEntities.registerSpawnPlacements();
    }

    private void registerDataTickets() {
        FuMO25BlockEntity.FUMO25_TICK = GeckoLibUtil.addDataTicket(SerializableDataTicket.ofInt(Mod.loc("fumo25_tick")));
    }

    private void registerBuiltinResourcePacks() {
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
*/