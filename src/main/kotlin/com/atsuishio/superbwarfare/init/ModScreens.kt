package com.atsuishio.superbwarfare.init

import com.atsuishio.superbwarfare.client.screens.*
import net.minecraft.client.gui.screens.MenuScreens

object ModScreens {

    @JvmStatic
    fun init() {
        MenuScreens.register(ModMenuTypes.MINI_VEHICLE_CONTAINER_MENU, ::MiniVehicleContainerScreen)
        MenuScreens.register(ModMenuTypes.SMALL_VEHICLE_CONTAINER_MENU, ::SmallVehicleContainerScreen)
        MenuScreens.register(ModMenuTypes.MEDIUM_VEHICLE_CONTAINER_MENU, ::MediumVehicleContainerScreen)
        MenuScreens.register(ModMenuTypes.LARGE_VEHICLE_CONTAINER_MENU, ::LargeVehicleContainerScreen)
        MenuScreens.register(ModMenuTypes.HUGE_VEHICLE_CONTAINER_MENU, ::HugeVehicleContainerScreen)

        MenuScreens.register(ModMenuTypes.REFORGING_TABLE_MENU, ::ReforgingTableScreen)
        MenuScreens.register(ModMenuTypes.CHARGING_STATION_MENU, ::ChargingStationScreen)
        MenuScreens.register(ModMenuTypes.SUPERB_ITEM_INTERFACE_MENU, ::SuperbItemInterfaceScreen)
        MenuScreens.register(ModMenuTypes.FUMO_25_MENU, ::FuMO25Screen)
        MenuScreens.register(ModMenuTypes.VEHICLE_ASSEMBLING_MENU, ::VehicleAssemblingScreen)
        MenuScreens.register(ModMenuTypes.BLUEPRINT_RESEARCH_TABLE, ::BlueprintResearchTableScreen)
    }
}
