package com.atsuishio.superbwarfare;

import com.atsuishio.superbwarfare.block.entity.FuMO25BlockEntity;
import com.atsuishio.superbwarfare.command.CommandRegister;
import com.atsuishio.superbwarfare.config.ClientConfig;
import com.atsuishio.superbwarfare.config.CommonConfig;
import com.atsuishio.superbwarfare.config.ServerConfig;
import com.atsuishio.superbwarfare.data.CustomData;
import com.atsuishio.superbwarfare.data.DataLoader;
import com.atsuishio.superbwarfare.data.container.ContainerDataManager;
import com.atsuishio.superbwarfare.data.vehicle.VehicleDataTool;
import com.atsuishio.superbwarfare.init.*;
import com.atsuishio.superbwarfare.item.common.container.ContainerBlockItem;
import com.atsuishio.superbwarfare.network.NetworkRegistry;
import com.atsuishio.superbwarfare.tools.ResourceOnceLogger;
import com.atsuishio.superbwarfare.world.TDMSavedData;
import net.fabricmc.api.ModInitializer;
import software.bernie.geckolib.constant.dataticket.SerializableDataTicket;
import software.bernie.geckolib.util.GeckoLibUtil;

public class SuperbWarfareFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        Mod.LOGGER.info("Initializing Superb Warfare (Fabric)");

        triggerInit();
        callInits();

        NetworkRegistry.register();
        VehicleDataTool.register();
        DataLoader.register();

        ContainerBlockItem.registerContainers();
        ModTabs.init();
        ModCapabilities.init();
        CustomData.load();

        CommandRegister.register();
        ContainerDataManager.register();
        TDMSavedData.register();

        ResourceOnceLogger.register();
        registerDataTickets();
    }

    private void triggerInit() {
        ModItems.registerBlockItems();
        ModItems.registerPerkItems();
        ModItems.registerSpawnEggs();
        ModItems.registerDispenserBehavior();

        ModSerializers.init();
        ModVillagers.init();

        CommonConfig.init();
        ServerConfig.init();
        ClientConfig.init();
    }

    private void callInits() {
        ModBlocks.GALENA_ORE.getDescriptionId();
        ModBlockEntities.CONTAINER.toString();
        ModEntities.TARGET.toString();
        ModMenuTypes.REFORGING_TABLE_MENU.getClass();
        ModSounds.MORTAR_SHELL_EXPLODE.getLocation();
        ModMobEffects.SHOCK.value().getDescriptionId();
        ModParticleTypes.BULLET_DECAL.toString();
        ModPotions.SHOCK.toString();
        ModRecipes.POTION_MORTAR_SHELL_SERIALIZER.getClass();
        ModArmorMaterials.CEMENTED_CARBIDE.toString();
        ModAttributes.BULLET_RESISTANCE.getDescriptionId();
        ModCriteriaTriggers.RPG_MELEE_EXPLOSION.getClass();
        ModAttachments.PLAYER_VARIABLE.getClass();
        ModCommandArguments.LOWER_CAMEL_CASE_ENUM.getClass();
        ModTabs.GUN_TAB.getDisplayName();
        ModVillagers.ARMORY.name();
        ModSerializers.INT_LIST_SERIALIZER.toString();
        ModPerks.AP_BULLET.toString();
        ModDamageTypes.GUN_FIRE.location();
    }

    private void registerDataTickets() {
        FuMO25BlockEntity.FUMO25_TICK = GeckoLibUtil.addDataTicket(SerializableDataTicket.ofInt(Mod.loc("fumo25_tick")));
    }
}
