package com.atsuishio.superbwarfare.capability.energy

import com.atsuishio.superbwarfare.init.ModDataComponents
import net.minecraft.world.item.ItemStack
import team.reborn.energy.api.EnergyStorage
import java.util.function.Function

class ItemEnergyStorage(
    private val stack: ItemStack,
    capacityGetter: Function<ItemStack, Int>,
    maxReceiveGetter: Function<ItemStack, Int>,
    maxExtractGetter: Function<ItemStack, Int>
) : DynamicEnergyStorage(
    { capacityGetter.apply(stack) },
    { maxReceiveGetter.apply(stack) },
    { maxExtractGetter.apply(stack) }
) {

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
        val current = stack.get(EnergyStorage.ENERGY_COMPONENT)
        val legacy = stack.get(ModDataComponents.ENERGY)
        this.energy = current ?: legacy?.toLong() ?: 0L

        if (current == null && legacy != null) {
            stack.set(EnergyStorage.ENERGY_COMPONENT, energy)
            stack.remove(ModDataComponents.ENERGY)
        }
    }

    override fun onFinalCommit() {
        stack.set(EnergyStorage.ENERGY_COMPONENT, amount)
        stack.remove(ModDataComponents.ENERGY)
    }
}
