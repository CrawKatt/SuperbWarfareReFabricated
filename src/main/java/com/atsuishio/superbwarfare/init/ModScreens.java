package com.atsuishio.superbwarfare.init;

import com.atsuishio.superbwarfare.client.screens.*;
import com.atsuishio.superbwarfare.data.vehicle.subdata.VehicleContainerType;
import com.atsuishio.superbwarfare.menu.VehicleMenu;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ModScreens {

    public static void init() {
        MenuScreens.register(ModMenuTypes.REFORGING_TABLE_MENU, ReforgingTableScreen::new);
        MenuScreens.register(ModMenuTypes.CHARGING_STATION_MENU, ChargingStationScreen::new);

        MenuScreens.register(ModMenuTypes.VEHICLE_MENU_MINI,
                (VehicleMenu menu, Inventory inventory, Component title) -> new VehicleScreen(menu, inventory, title, VehicleContainerType.MINI));
        MenuScreens.register(ModMenuTypes.VEHICLE_MENU_MINI_UPGRADE,
                (VehicleMenu menu, Inventory inventory, Component title) -> new VehicleScreen(menu, inventory, title, VehicleContainerType.MINI));

        MenuScreens.register(ModMenuTypes.VEHICLE_MENU_SMALL,
                (VehicleMenu menu, Inventory inventory, Component title) -> new VehicleScreen(menu, inventory, title, VehicleContainerType.SMALL));
        MenuScreens.register(ModMenuTypes.VEHICLE_MENU_SMALL_UPGRADE,
                (VehicleMenu menu, Inventory inventory, Component title) -> new VehicleScreen(menu, inventory, title, VehicleContainerType.SMALL));

        MenuScreens.register(ModMenuTypes.VEHICLE_MENU_MEDIUM,
                (VehicleMenu menu, Inventory inventory, Component title) -> new VehicleScreen(menu, inventory, title, VehicleContainerType.MEDIUM));
        MenuScreens.register(ModMenuTypes.VEHICLE_MENU_MEDIUM_UPGRADE,
                (VehicleMenu menu, Inventory inventory, Component title) -> new VehicleScreen(menu, inventory, title, VehicleContainerType.MEDIUM));

        MenuScreens.register(ModMenuTypes.VEHICLE_MENU_LARGE,
                (VehicleMenu menu, Inventory inventory, Component title) -> new VehicleScreen(menu, inventory, title, VehicleContainerType.LARGE));
        MenuScreens.register(ModMenuTypes.VEHICLE_MENU_LARGE_UPGRADE,
                (VehicleMenu menu, Inventory inventory, Component title) -> new VehicleScreen(menu, inventory, title, VehicleContainerType.LARGE));

        MenuScreens.register(ModMenuTypes.VEHICLE_MENU_HUGE,
                (VehicleMenu menu, Inventory inventory, Component title) -> new VehicleScreen(menu, inventory, title, VehicleContainerType.HUGE));
        MenuScreens.register(ModMenuTypes.VEHICLE_MENU_HUGE_UPGRADE,
                (VehicleMenu menu, Inventory inventory, Component title) -> new VehicleScreen(menu, inventory, title, VehicleContainerType.HUGE));

        MenuScreens.register(ModMenuTypes.SUPERB_ITEM_INTERFACE_MENU, SuperbItemInterfaceScreen::new);
        MenuScreens.register(ModMenuTypes.FUMO_25_MENU, FuMO25Screen::new);
        MenuScreens.register(ModMenuTypes.VEHICLE_ASSEMBLING_MENU, VehicleAssemblingScreen::new);
    }
}
