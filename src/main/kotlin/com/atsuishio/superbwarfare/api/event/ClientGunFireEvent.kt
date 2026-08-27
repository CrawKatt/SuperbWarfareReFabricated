package com.atsuishio.superbwarfare.api.event

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.Entity
import net.minecraft.world.item.ItemStack
import org.jetbrains.annotations.ApiStatus

@ApiStatus.AvailableSince("0.8.10")
open class ClientGunFireEvent(
    val shooter: Entity,
    val stack: ItemStack,
    val hand: InteractionHand = InteractionHand.MAIN_HAND
) {
    fun interface Callback {
        fun post(event: ClientGunFireEvent)
    }

    companion object {
        @JvmField
        val EVENT: Event<Callback> = EventFactory.createArrayBacked(Callback::class.java) { callbacks ->
            Callback { event -> callbacks.forEach { it.post(event) } }
        }

        @JvmStatic
        fun post(event: ClientGunFireEvent) {
            EVENT.invoker().post(event)
        }
    }
}
