package com.atsuishio.superbwarfare.inventory.menu

import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity
import com.atsuishio.superbwarfare.tools.PlayerReachTool
import net.minecraft.world.SimpleContainer
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.MenuType
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack

abstract class AbstractVehicleContainerMenu(type: MenuType<*>?, id: Int, inventory: Inventory, entityId: Int) :
    AbstractContainerMenu(type, id) {
    val vehicle: VehicleEntity? = inventory.player.level().getEntity(entityId) as? VehicleEntity

    init {
        if (vehicle != null) {
            this.addPlayerInventory(inventory)
            this.addVehicleInventory()
        }
    }

    open fun addPlayerInventory(inventory: Inventory) {
        val i = (getRows() - 4) * 18
        for (r in 0 until 3) {
            for (c in 0 until 9) {
                this.addSlot(Slot(inventory, c + r * 9 + 9, 8 + c * 18, 103 + r * 18 + i))
            }
        }
        for (c in 0 until 9) {
            this.addSlot(Slot(inventory, c, 8 + c * 18, 161 + i))
        }
    }

    override fun quickMoveStack(
        player: Player,
        index: Int
    ): ItemStack {
        var stack1 = ItemStack.EMPTY
        val slot = this.slots[index]
        if (slot.hasItem()) {
            val stack2 = slot.item
            stack1 = stack2.copy()
            if (index < 36) {
                if (!this.moveItemStackTo(stack2, 36, this.slots.size, false)) {
                    return ItemStack.EMPTY
                }
            } else if (!this.moveItemStackTo(stack2, 0, 36, true)) {
                return ItemStack.EMPTY
            }

            if (stack2.isEmpty) {
                slot.setByPlayer(ItemStack.EMPTY)
            } else {
                slot.setChanged()
            }

            if (stack2.count == stack1.count) {
                return ItemStack.EMPTY
            }

            slot.onTake(player, stack2)
        }
        return stack1
    }

    abstract fun addVehicleInventory()

    open fun getRows(): Int = 0

    override fun stillValid(pPlayer: Player): Boolean {
        if (vehicle == null) return false
        val reach = PlayerReachTool.getEntityReach(pPlayer) + 3.0
        return vehicle.isAlive
                && vehicle.boundingBox.inflate(vehicle.pickRadius.toDouble()).distanceToSqr(pPlayer.eyePosition) < reach * reach
    }

    class VehicleSlot(
        private val vehicle: VehicleEntity?,
        @get:JvmName("slotIndex") val slotIndex: Int,
        x: Int,
        y: Int
    ) : Slot(SimpleContainer(0), slotIndex, x, y) {
        override fun getItem(): ItemStack {
            return this.vehicle?.inventory?.getStackInSlot(slotIndex) ?: ItemStack.EMPTY
        }

        override fun set(stack: ItemStack) {
            this.vehicle?.inventory?.setStackInSlot(slotIndex, stack)
            this.setChanged()
        }

        override fun setChanged() {
            this.vehicle?.setChanged()
        }

        override fun mayPlace(stack: ItemStack): Boolean {
            return this.vehicle?.canPlaceItem(slotIndex, stack) ?: false
        }

        override fun remove(amount: Int): ItemStack {
            return this.vehicle?.inventory?.extractItem(slotIndex, amount, false) ?: ItemStack.EMPTY
        }

        override fun getMaxStackSize(): Int {
            return this.vehicle?.inventory?.getSlotLimit(slotIndex) ?: 0
        }

        override fun getMaxStackSize(stack: ItemStack): Int {
            return minOf(this.maxStackSize, stack.maxStackSize)
        }

        override fun mayPickup(playerIn: Player): Boolean {
            return this.vehicle?.canTakeItem(slotIndex) ?: false
        }
    }
}
