package com.atsuishio.superbwarfare.inventory.handler

import com.atsuishio.superbwarfare.capability.api.IItemHandler
import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity
import net.minecraft.core.NonNullList
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.nbt.Tag
import net.minecraft.world.item.ItemStack
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant
import net.fabricmc.fabric.api.transfer.v1.item.base.SingleStackStorage
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage
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

        if (!isItemValid(slot, stack)) {
            return stack
        }

        validateSlotIndex(slot)

        val existing = stacks[slot]
        var limit = getStackLimit(slot, stack)

        if (!existing.isEmpty) {
            if (!ItemStack.isSameItemSameTags(stack, existing)) {
                return stack
            }
            limit -= existing.count
        }

        if (limit <= 0) return stack

        val reachedLimit = stack.count > limit

        if (!simulate) {
            if (existing.isEmpty) {
                stacks[slot] = if (reachedLimit) stack.copy().also { it.count = limit } else stack
            } else {
                existing.grow(if (reachedLimit) limit else stack.count)
            }
            onContentsChanged(slot)
        }

        return if (reachedLimit) {
            stack.copy().also { it.count = stack.count - limit }
        } else {
            ItemStack.EMPTY
        }
    }

    override fun extractItem(slot: Int, amount: Int, simulate: Boolean): ItemStack {
        if (amount <= 0) return ItemStack.EMPTY

        validateSlotIndex(slot)

        val existing = stacks[slot]
        if (existing.isEmpty) return ItemStack.EMPTY

        val toExtract = min(amount, existing.maxStackSize)

        if (existing.count <= toExtract) {
            if (simulate) return existing.copy()

            stacks[slot] = ItemStack.EMPTY
            onContentsChanged(slot)
            return existing
        }

        if (!simulate) {
            stacks[slot] = existing.copy().also { it.count = existing.count - toExtract }
            onContentsChanged(slot)
        }
        return existing.copy().also { it.count = toExtract }
    }

    override fun getSlotLimit(slot: Int): Int {
        return 64
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
        stacks = NonNullList.withSize(size, ItemStack.EMPTY)
    }

    open fun clear() {
        stacks.clear()
    }

    fun getItems(): NonNullList<ItemStack> {
        return stacks
    }

    fun getSlotStorage(slot: Int): SingleSlotStorage<ItemVariant> {
        validateSlotIndex(slot)

        return object : SingleStackStorage() {
            override fun getStack() = stacks[slot]

            override fun setStack(stack: ItemStack) {
                stacks[slot] = stack
            }

            override fun onFinalCommit() {
                onContentsChanged(slot)
            }
        }
    }

    fun setItems(list: NonNullList<ItemStack>) {
        stacks = list
    }

    open fun serializeNBT(): CompoundTag {
        val items = ListTag()
        for (slot in stacks.indices) {
            if (stacks[slot].isEmpty) continue

            val item = CompoundTag()
            item.putInt("Slot", slot)
            stacks[slot].save(item)
            items.add(item)
        }

        return CompoundTag().also {
            it.put("Items", items)
            it.putInt("Size", stacks.size)
        }
    }

    open fun deserializeNBT(tag: CompoundTag) {
        setSize(if (tag.contains("Size", Tag.TAG_INT.toInt())) tag.getInt("Size") else stacks.size)
        val items = tag.getList("Items", Tag.TAG_COMPOUND.toInt())
        for (i in items.indices) {
            val item = items.getCompound(i)
            val slot = item.getInt("Slot")
            if (slot in stacks.indices) {
                stacks[slot] = ItemStack.of(item)
            }
        }
    }

    protected fun validateSlotIndex(slot: Int) {
        if (slot < 0 || slot >= stacks.size) {
            throw RuntimeException("Slot $slot not in valid range - [0, ${stacks.size})")
        }
    }
}
