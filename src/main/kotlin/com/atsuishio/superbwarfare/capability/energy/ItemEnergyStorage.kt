package com.atsuishio.superbwarfare.capability.energy

import net.minecraft.world.item.ItemStack
import java.util.function.Function

class ItemEnergyStorage(
    private val stack: ItemStack,
    capacityGetter: Function<ItemStack, Int>,
    maxReceiveGetter: Function<ItemStack, Int>,
    maxExtractGetter: Function<ItemStack, Int>
) : DynamicEnergyStorage({
    capacityGetter.apply(stack)
}, { maxReceiveGetter.apply(stack) }, { maxExtractGetter.apply(stack) }) {
    @JvmOverloads
    constructor(
        stack: ItemStack,
        capacity: Int,
        maxReceive: Int = capacity,
        maxExtract: Int = capacity
    ) : this(
        stack,
        { _ -> capacity },
        { _ -> maxReceive },
        { _ -> maxExtract }
    )

    init {
        if (stack.tag != null) {
            this.energy = if (stack.hasTag() && stack.tag!!.contains(NBT_ENERGY)) {
                stack.tag!!.getInt(NBT_ENERGY).toLong()
            } else {
                0L
            }
        }
    }

    override fun onFinalCommit() {
        stack.orCreateTag.putInt(NBT_ENERGY, energyStored)
    }

    companion object {
        private const val NBT_ENERGY = "Energy"
    }
}
