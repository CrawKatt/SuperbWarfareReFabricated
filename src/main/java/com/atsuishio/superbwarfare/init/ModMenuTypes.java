package com.atsuishio.superbwarfare.init;

import com.atsuishio.superbwarfare.Mod;
import com.atsuishio.superbwarfare.menu.*;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.MenuType;

public class ModMenuTypes {

    public static final MenuType<ReforgingTableMenu> REFORGING_TABLE_MENU =
            register("reforging_table_menu", new MenuType<>(ReforgingTableMenu::new, FeatureFlagSet.of()));
    public static final MenuType<ChargingStationMenu> CHARGING_STATION_MENU =
            register("charging_station_menu", new MenuType<>(ChargingStationMenu::new, FeatureFlagSet.of()));

    public static final MenuType<VehicleMenu> VEHICLE_MENU_MINI =
            register("vehicle_menu_mini", new MenuType<>((id, inv) -> VehicleMenu.mini(id, inv, false), FeatureFlagSet.of()));
    public static final MenuType<VehicleMenu> VEHICLE_MENU_MINI_UPGRADE =
            register("vehicle_menu_mini_upgrade", new MenuType<>((id, inv) -> VehicleMenu.mini(id, inv, true), FeatureFlagSet.of()));

    public static final MenuType<VehicleMenu> VEHICLE_MENU_SMALL =
            register("vehicle_menu_small", new MenuType<>((id, inv) -> VehicleMenu.small(id, inv, false), FeatureFlagSet.of()));
    public static final MenuType<VehicleMenu> VEHICLE_MENU_SMALL_UPGRADE =
            register("vehicle_menu_small_upgrade", new MenuType<>((id, inv) -> VehicleMenu.small(id, inv, true), FeatureFlagSet.of()));

    public static final MenuType<VehicleMenu> VEHICLE_MENU_MEDIUM =
            register("vehicle_menu_medium", new MenuType<>((id, inv) -> VehicleMenu.medium(id, inv, false), FeatureFlagSet.of()));
    public static final MenuType<VehicleMenu> VEHICLE_MENU_MEDIUM_UPGRADE =
            register("vehicle_menu_medium_upgrade", new MenuType<>((id, inv) -> VehicleMenu.medium(id, inv, true), FeatureFlagSet.of()));

    public static final MenuType<VehicleMenu> VEHICLE_MENU_LARGE =
            register("vehicle_menu_large", new MenuType<>((id, inv) -> VehicleMenu.large(id, inv, false), FeatureFlagSet.of()));
    public static final MenuType<VehicleMenu> VEHICLE_MENU_LARGE_UPGRADE =
            register("vehicle_menu_large_upgrade", new MenuType<>((id, inv) -> VehicleMenu.large(id, inv, true), FeatureFlagSet.of()));

    public static final MenuType<VehicleMenu> VEHICLE_MENU_HUGE =
            register("vehicle_menu_huge", new MenuType<>((id, inv) -> VehicleMenu.huge(id, inv, false), FeatureFlagSet.of()));
    public static final MenuType<VehicleMenu> VEHICLE_MENU_HUGE_UPGRADE =
            register("vehicle_menu_huge_upgrade", new MenuType<>((id, inv) -> VehicleMenu.huge(id, inv, true), FeatureFlagSet.of()));

    public static final MenuType<SuperbItemInterfaceMenu> SUPERB_ITEM_INTERFACE_MENU =
            register("superb_item_interface_menu", new MenuType<>(SuperbItemInterfaceMenu::new, FeatureFlagSet.of()));
    public static final MenuType<FuMO25Menu> FUMO_25_MENU =
            register("fumo_25_menu", new MenuType<>(FuMO25Menu::new, FeatureFlagSet.of()));
    public static final MenuType<VehicleAssemblingMenu> VEHICLE_ASSEMBLING_MENU =
            register("vehicle_assembling_menu", new MenuType<>(VehicleAssemblingMenu::new, FeatureFlagSet.of()));

    private static <T extends MenuType<?>> T register(String name, T type) {
        return Registry.register(BuiltInRegistries.MENU, Mod.loc(name), type);
    }
}
