package com.atsuishio.superbwarfare.init

import com.atsuishio.superbwarfare.Mod
import com.atsuishio.superbwarfare.inventory.menu.BlueprintResearchTableMenu
import com.atsuishio.superbwarfare.inventory.menu.ChargingStationMenu
import com.atsuishio.superbwarfare.inventory.menu.FuMO25Menu
import com.atsuishio.superbwarfare.inventory.menu.HugeVehicleContainerMenu
import com.atsuishio.superbwarfare.inventory.menu.LargeVehicleContainerMenu
import com.atsuishio.superbwarfare.inventory.menu.MediumVehicleContainerMenu
import com.atsuishio.superbwarfare.inventory.menu.MiniVehicleContainerMenu
import com.atsuishio.superbwarfare.inventory.menu.ReforgingTableMenu
import com.atsuishio.superbwarfare.inventory.menu.SmallVehicleContainerMenu
import com.atsuishio.superbwarfare.inventory.menu.SuperbItemInterfaceMenu
import com.atsuishio.superbwarfare.inventory.menu.VehicleAssemblingMenu
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.flag.FeatureFlagSet
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.MenuType

@Suppress("unused")
object ModMenuTypes {
    @JvmField
    val REFORGING_TABLE_MENU: MenuType<ReforgingTableMenu> =
        register("reforging_table_menu", MenuType.MenuSupplier { windowId, inv ->
            ReforgingTableMenu(windowId, inv)
        })

    @JvmField
    val CHARGING_STATION_MENU: MenuType<ChargingStationMenu> =
        register("charging_station_menu", MenuType.MenuSupplier { windowId, inv ->
            ChargingStationMenu(windowId, inv)
        })

    @JvmField
    val MINI_VEHICLE_CONTAINER_MENU: MenuType<MiniVehicleContainerMenu> =
        register("mini_vehicle_container", MiniVehicleContainerMenu.TYPE)

    @JvmField
    val SMALL_VEHICLE_CONTAINER_MENU: MenuType<SmallVehicleContainerMenu> =
        register("small_vehicle_container", SmallVehicleContainerMenu.TYPE)

    @JvmField
    val MEDIUM_VEHICLE_CONTAINER_MENU: MenuType<MediumVehicleContainerMenu> =
        register("medium_vehicle_container", MediumVehicleContainerMenu.TYPE)

    @JvmField
    val LARGE_VEHICLE_CONTAINER_MENU: MenuType<LargeVehicleContainerMenu> =
        register("large_vehicle_container", LargeVehicleContainerMenu.TYPE)

    @JvmField
    val HUGE_VEHICLE_CONTAINER_MENU: MenuType<HugeVehicleContainerMenu> =
        register("huge_vehicle_container", HugeVehicleContainerMenu.TYPE)

    @JvmField
    val SUPERB_ITEM_INTERFACE_MENU: MenuType<SuperbItemInterfaceMenu> =
        register("superb_item_interface_menu", MenuType.MenuSupplier { windowId, inv ->
            SuperbItemInterfaceMenu(windowId, inv)
        })

    @JvmField
    val FUMO_25_MENU: MenuType<FuMO25Menu> =
        register("fumo_25_menu", MenuType.MenuSupplier { windowId, inv ->
            FuMO25Menu(windowId, inv)
        })

    @JvmField
    val VEHICLE_ASSEMBLING_MENU: MenuType<VehicleAssemblingMenu> =
        register("vehicle_assembling_menu", MenuType.MenuSupplier { windowId, inv ->
            VehicleAssemblingMenu(windowId, inv)
        })

    @JvmField
    val BLUEPRINT_RESEARCH_TABLE: MenuType<BlueprintResearchTableMenu> =
        register("blueprint_research_table_menu", MenuType.MenuSupplier { windowId, inv ->
            BlueprintResearchTableMenu(windowId, inv)
        })

    private fun <T : AbstractContainerMenu> register(
        name: String,
        factory: MenuType.MenuSupplier<T>
    ): MenuType<T> {
        return register(name, MenuType(factory, FeatureFlagSet.of()))
    }

    private fun <T : AbstractContainerMenu> register(
        name: String,
        type: MenuType<T>
    ): MenuType<T> {
        return Registry.register(BuiltInRegistries.MENU, Mod.loc(name), type)
    }

    @JvmStatic
    fun init() {
    }
}
