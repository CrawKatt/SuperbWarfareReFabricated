package com.atsuishio.superbwarfare.api.event

import com.atsuishio.superbwarfare.item.container.ContainerBlockItem
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.ItemStack
import org.jetbrains.annotations.ApiStatus

/**
 * Register Entities as a container
 */
@ApiStatus.AvailableSince("0.8.0")
class RegisterContainersEvent {
    companion object {
        @JvmField
        val CONTAINERS = arrayListOf<ItemStack>()
    }

    fun <T : Entity> add(type: EntityType<T>) {
        val stack = ContainerBlockItem.createInstance(type)
        CONTAINERS.add(stack)
    }

    fun add(entity: Entity) {
        val stack = ContainerBlockItem.createInstance(entity)
        CONTAINERS.add(stack)
    }
}
