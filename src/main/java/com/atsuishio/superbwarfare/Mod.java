package com.atsuishio.superbwarfare;

import com.atsuishio.superbwarfare.advancement.CriteriaRegister;
import com.atsuishio.superbwarfare.api.event.RegisterContainersEvent;
import com.atsuishio.superbwarfare.block.entity.FuMO25BlockEntity;
import com.atsuishio.superbwarfare.capability.energy.ModEnergyApi;
import com.atsuishio.superbwarfare.command.CommandRegister;
import com.atsuishio.superbwarfare.config.CommonConfig;
import com.atsuishio.superbwarfare.config.ServerConfig;
import com.atsuishio.superbwarfare.data.CustomData;
import com.atsuishio.superbwarfare.data.DataLoader;
import com.atsuishio.superbwarfare.data.container.ContainerDataManager;
import com.atsuishio.superbwarfare.entity.DPSGeneratorEntity;
import com.atsuishio.superbwarfare.entity.TargetEntity;
import com.atsuishio.superbwarfare.event.CustomEventHandler;
import com.atsuishio.superbwarfare.event.HitboxHelperEventHandler;
import com.atsuishio.superbwarfare.event.LivingEventHandler;
import com.atsuishio.superbwarfare.event.ModVersionEventHandler;
import com.atsuishio.superbwarfare.event.PlayerEventHandler;
import com.atsuishio.superbwarfare.init.*;
import com.atsuishio.superbwarfare.item.common.container.ContainerBlockItem;
import com.atsuishio.superbwarfare.mobeffect.BurnMobEffect;
import com.atsuishio.superbwarfare.mobeffect.ShockMobEffect;
import com.atsuishio.superbwarfare.mobeffect.TraumaMobEffect;
import com.atsuishio.superbwarfare.perk.functional.PowerfulAttraction;
import com.atsuishio.superbwarfare.recipe.ModPotionRecipes;
import dev.emi.trinkets.api.Trinket;
import dev.emi.trinkets.api.TrinketsApi;
import fuzs.forgeconfigapiport.api.config.v2.ForgeConfigRegistry;
import com.atsuishio.superbwarfare.network.NetworkRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.bernie.geckolib.network.SerializableDataTicket;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Mod implements ModInitializer {

    public static final String MODID = "superbwarfare";
    public static final String ATTRIBUTE_MODIFIER = "superbwarfare_attribute_modifier";

    public static final Logger LOGGER = LogManager.getLogger(Mod.class);

    private static MinecraftServer serverInstance;
    private static final Collection<AbstractMap.SimpleEntry<Runnable, Integer>> SERVER_QUEUE = new ConcurrentLinkedQueue<>();
    static final Collection<AbstractMap.SimpleEntry<Runnable, Integer>> CLIENT_QUEUE = new ConcurrentLinkedQueue<>();

    @Override
    public void onInitialize() {
        ForgeConfigRegistry.INSTANCE.register(Mod.MODID, ModConfig.Type.COMMON, CommonConfig.init());
        ForgeConfigRegistry.INSTANCE.register(Mod.MODID, ModConfig.Type.SERVER, ServerConfig.init());

        ModPerks.registerCompatPerks();
        ModItems.register();
        FuelRegistry.INSTANCE.add(ModItems.C4_BOMB.get(), 20_000);
        TrinketsApi.registerTrinket(ModItems.PARACHUTE.get(), (Trinket) ModItems.PARACHUTE.get());
        TrinketsApi.registerTrinket(ModItems.DOG_TAG.get(), (Trinket) ModItems.DOG_TAG.get());
        TrinketsApi.registerTrinket(ModItems.IFF.get(), (Trinket) ModItems.IFF.get());
        ModBlocks.register();
        ModBlockEntities.register();
        ModEntities.register();
        ModSounds.register();
        ModMobEffects.register();
        ModParticleTypes.register();
        ModPotions.register();
        ModMenuTypes.register();
        ModRecipes.register();
        ModSerializers.register();
        ModPerks.register();
        ModTabs.register();
        CommandRegister.registerEvents();
        ModCommandArguments.register();
        ModVillagers.register();
        ModAttributes.register();
        ModWorldgen.register();
        ModEnergyApi.register();
        ModTags.register();
        ModEntities.registerAttributes();
        ModEntities.registerSpawnPlacements();
        ModItems.registerDispenserBehavior();
        ModPotionRecipes.register();
        CriteriaRegister.setup();
        DataLoader.registerReloadListeners();
        ContainerDataManager.register();
        DPSGeneratorEntity.registerEvents();
        ContainerBlockItem.registerContainers(new RegisterContainersEvent());

        ShockMobEffect.registerEvents();
        BurnMobEffect.registerEvents();
        TraumaMobEffect.registerEvents();
        PowerfulAttraction.registerEvents();
        TargetEntity.registerEvents();
        CustomEventHandler.registerEvents();
        HitboxHelperEventHandler.registerEvents();
        LivingEventHandler.registerEvents();
        PlayerEventHandler.registerEvents();
        ModVersionEventHandler.registerEvents();
        ModLootModifier.register();

        registerDataTickets();

        NetworkRegistry.register();

        ServerLifecycleEvents.SERVER_STARTING.register(server -> serverInstance = server);
        ServerLifecycleEvents.SERVER_STOPPING.register(server -> serverInstance = null);

        if (FabricLoader.getInstance().isModLoaded("tacz")) {
            ServerLifecycleEvents.SERVER_STARTING.register(server -> { });
        }
        /*
        if (ColdSweatCompatHandler.hasMod()) {
            ServerTickEvents.END_SERVER_TICK.register(server -> { });
        }
        */

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            List<AbstractMap.SimpleEntry<Runnable, Integer>> actions = new ArrayList<>();
            SERVER_QUEUE.forEach(work -> {
                work.setValue(work.getValue() - 1);
                if (work.getValue() == 0)
                    actions.add(work);
            });
            actions.forEach(e -> e.getKey().run());
            SERVER_QUEUE.removeAll(actions);
        });

        CustomData.load();
    }

    public static ResourceLocation loc(String path) {
        return new ResourceLocation(MODID, path);
    }

    public static void queueServerWork(int tick, Runnable action) {
        SERVER_QUEUE.add(new AbstractMap.SimpleEntry<>(action, tick));
    }

    public static MinecraftServer getServer() {
        return serverInstance;
    }

    public static void queueClientWork(int tick, Runnable action) {
        CLIENT_QUEUE.add(new AbstractMap.SimpleEntry<>(action, tick));
    }

    private void registerDataTickets() {
        FuMO25BlockEntity.FUMO25_TICK = GeckoLibUtil.addDataTicket(SerializableDataTicket.ofInt(loc("fumo25_tick")));
    }
}
