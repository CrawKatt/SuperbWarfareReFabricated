package com.atsuishio.superbwarfare.inventory.handler

import com.atsuishio.superbwarfare.capability.api.IItemHandler
import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity
import net.minecraft.core.HolderLookup
import net.minecraft.core.NonNullList
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.ContainerHelper
import net.minecraft.world.item.ItemStack
import kotlin.math.min

open class VehicleContainerHandler(
    size: Int,
    val vehicle: VehicleEntity
) : IItemHandler {
    protected var stacks: NonNullList<ItemStack> = NonNullList.withSize(size, ItemStack.EMPTY)

    @get:JvmName("getSlotCount")
    val slots: Int
        get() = stacks.size

    override fun getSlots(): Int {
        return stacks.size
    }

    override fun getStackInSlot(slot: Int): ItemStack {
        validateSlotIndex(slot)
        return stacks[slot]
    }

    open fun setStackInSlot(slot: Int, stack: ItemStack) {
        validateSlotIndex(slot)
        stacks[slot] = stack
        onContentsChanged(slot)
    }

    override fun insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack {
        if (stack.isEmpty) return ItemStack.EMPTY

        validateSlotIndex(slot)

        if (!isItemValid(slot, stack)) {
            return stack
        }

        val existing = stacks[slot]
        val limit = getStackLimit(slot, stack)

        if (!existing.isEmpty) {
            if (!ItemStack.isSameItemSameComponents(stack, existing)) {
                return stack
            }

            val available = limit - existing.count
            if (available <= 0) {
                return stack
            }

            val toInsert = min(stack.count, available)

            if (!simulate) {
                existing.grow(toInsert)
                onContentsChanged(slot)
            }

            return if (stack.count > toInsert) {
                stack.copyWithCount(stack.count - toInsert)
            } else {
                ItemStack.EMPTY
            }
        }

        val toInsert = min(stack.count, limit)

        if (!simulate) {
            stacks[slot] = stack.copyWithCount(toInsert)
            onContentsChanged(slot)
        }

        return if (stack.count > toInsert) {
            stack.copyWithCount(stack.count - toInsert)
        } else {
            ItemStack.EMPTY
        }
    }

    override fun extractItem(slot: Int, amount: Int, simulate: Boolean): ItemStack {
        if (amount <= 0) return ItemStack.EMPTY

        validateSlotIndex(slot)

        val existing = stacks[slot]
        if (existing.isEmpty) return ItemStack.EMPTY

        val toExtract = min(amount, existing.count)

        if (simulate) {
            return existing.copyWithCount(toExtract)
        }

        val extracted = existing.split(toExtract)

        if (existing.isEmpty) {
            stacks[slot] = ItemStack.EMPTY
        }

        onContentsChanged(slot)

        return extracted
    }

    override fun getSlotLimit(slot: Int): Int {
        return vehicle.maxStackSize
    }

    override fun isItemValid(slot: Int, stack: ItemStack): Boolean {
        return vehicle.canPlaceItem(slot, stack)
    }

    protected open fun getStackLimit(slot: Int, stack: ItemStack): Int {
        return min(getSlotLimit(slot), stack.maxStackSize)
    }

    protected open fun onContentsChanged(slot: Int) {
        vehicle.setChanged()
    }

    open fun setSize(size: Int) {
        val oldStacks = stacks
        val newStacks = NonNullList.withSize(size, ItemStack.EMPTY)

        for (i in 0 until min(oldStacks.size, newStacks.size)) {
            newStacks[i] = oldStacks[i]
        }

        stacks = newStacks
    }

    open fun clear() {
        stacks.clear()
        vehicle.setChanged()
    }

    fun getItems(): NonNullList<ItemStack> {
        return stacks
    }

    fun setItems(list: NonNullList<ItemStack>) {
        stacks = list
        vehicle.setChanged()
    }

    open fun serializeNBT(provider: HolderLookup.Provider): CompoundTag {
        val tag = CompoundTag()

        tag.putInt("Size", stacks.size)
        ContainerHelper.saveAllItems(tag, stacks, provider)

        return tag
    }

    open fun deserializeNBT(provider: HolderLookup.Provider, tag: CompoundTag) {
        val size = if (tag.contains("Size")) {
            tag.getInt("Size")
        } else {
            stacks.size
        }

        stacks = NonNullList.withSize(size, ItemStack.EMPTY)
        ContainerHelper.loadAllItems(tag, stacks, provider)

        vehicle.setChanged()
    }

    protected fun validateSlotIndex(slot: Int) {
        if (slot < 0 || slot >= stacks.size) {
            throw RuntimeException("Slot $slot not in valid range - [0, ${stacks.size})")
        }
    }
}
