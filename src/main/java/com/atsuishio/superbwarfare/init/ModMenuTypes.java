package com.atsuishio.superbwarfare.init;

import com.atsuishio.superbwarfare.Mod;
import com.atsuishio.superbwarfare.menu.*;
import net.minecraft.world.inventory.MenuType;

import java.util.function.Supplier;

public class ModMenuTypes {

    public static final Supplier<MenuType<ReforgingTableMenu>> REFORGING_TABLE_MENU =
            Registration.menu("reforging_table_menu",
                    () -> new MenuType<>((windowId, inv) -> new ReforgingTableMenu(windowId, inv)));
    public static final Supplier<MenuType<ChargingStationMenu>> CHARGING_STATION_MENU =
            Registration.menu("charging_station_menu",
                    () -> new MenuType<>((windowId, inv) -> new ChargingStationMenu(windowId, inv)));

    public static final Supplier<MenuType<VehicleMenu>> VEHICLE_MENU_MINI =
            Registration.menu("vehicle_menu_mini",
                    () -> new MenuType<>((windowId, inv) -> VehicleMenu.mini(windowId, inv, false)));
    public static final Supplier<MenuType<VehicleMenu>> VEHICLE_MENU_MINI_UPGRADE =
            Registration.menu("vehicle_menu_mini_upgrade",
                    () -> new MenuType<>((windowId, inv) -> VehicleMenu.mini(windowId, inv, true)));

    public static final Supplier<MenuType<VehicleMenu>> VEHICLE_MENU_SMALL =
            Registration.menu("vehicle_menu_small",
                    () -> new MenuType<>((windowId, inv) -> VehicleMenu.small(windowId, inv, false)));
    public static final Supplier<MenuType<VehicleMenu>> VEHICLE_MENU_SMALL_UPGRADE =
            Registration.menu("vehicle_menu_small_upgrade",
                    () -> new MenuType<>((windowId, inv) -> VehicleMenu.small(windowId, inv, true)));

    public static final Supplier<MenuType<VehicleMenu>> VEHICLE_MENU_MEDIUM =
            Registration.menu("vehicle_menu_medium",
                    () -> new MenuType<>((windowId, inv) -> VehicleMenu.medium(windowId, inv, false)));
    public static final Supplier<MenuType<VehicleMenu>> VEHICLE_MENU_MEDIUM_UPGRADE =
            Registration.menu("vehicle_menu_medium_upgrade",
                    () -> new MenuType<>((windowId, inv) -> VehicleMenu.medium(windowId, inv, true)));

    public static final Supplier<MenuType<VehicleMenu>> VEHICLE_MENU_LARGE =
            Registration.menu("vehicle_menu_large",
                    () -> new MenuType<>((windowId, inv) -> VehicleMenu.large(windowId, inv, false)));
    public static final Supplier<MenuType<VehicleMenu>> VEHICLE_MENU_LARGE_UPGRADE =
            Registration.menu("vehicle_menu_large_upgrade",
                    () -> new MenuType<>((windowId, inv) -> VehicleMenu.large(windowId, inv, true)));

    public static final Supplier<MenuType<VehicleMenu>> VEHICLE_MENU_HUGE =
            Registration.menu("vehicle_menu_huge",
                    () -> new MenuType<>((windowId, inv) -> VehicleMenu.huge(windowId, inv, false)));
    public static final Supplier<MenuType<VehicleMenu>> VEHICLE_MENU_HUGE_UPGRADE =
            Registration.menu("vehicle_menu_huge_upgrade",
                    () -> new MenuType<>((windowId, inv) -> VehicleMenu.huge(windowId, inv, true)));

    public static final Supplier<MenuType<SuperbItemInterfaceMenu>> SUPERB_ITEM_INTERFACE_MENU =
            Registration.menu("superb_item_interface_menu",
                    () -> new MenuType<>((windowId, inv) -> new SuperbItemInterfaceMenu(windowId, inv)));
    public static final Supplier<MenuType<FuMO25Menu>> FUMO_25_MENU =
            Registration.menu("fumo_25_menu",
                    () -> new MenuType<>((windowId, inv) -> new FuMO25Menu(windowId, inv)));
    public static final Supplier<MenuType<VehicleAssemblingMenu>> VEHICLE_ASSEMBLING_MENU =
            Registration.menu("vehicle_assembling_menu",
                    () -> new MenuType<>((windowId, inv) -> new VehicleAssemblingMenu(windowId, inv)));

    public static void register() {

    }
}
