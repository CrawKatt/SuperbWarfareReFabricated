package com.atsuishio.superbwarfare.inventory.menu

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.MenuType

class HugeVehicleContainerMenu(id: Int, inventory: Inventory, entityId: Int) :
    AbstractVehicleContainerMenu(TYPE, id, inventory, entityId) {
    override fun getRows(): Int = 6

    override fun addVehicleInventory() {
        for (r in 0 until getRows()) {
            for (c in 0 until 17) {
                this.addSlot(VehicleSlot(this.vehicle, c + r * 17, 8 + c * 18 - 72, 18 + r * 18))
            }
        }
    }

    companion object {
        @JvmField
        val TYPE: MenuType<HugeVehicleContainerMenu> =
            ExtendedScreenHandlerType { id, inventory, buf ->
                HugeVehicleContainerMenu(id, inventory, buf.readInt())
            }
    }
}
