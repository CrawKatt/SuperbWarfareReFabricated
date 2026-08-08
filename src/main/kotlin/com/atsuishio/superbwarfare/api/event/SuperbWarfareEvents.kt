package com.atsuishio.superbwarfare.api.event

import java.util.concurrent.CopyOnWriteArrayList
import java.util.function.Consumer

object SuperbWarfareEvents {
    private val listeners = CopyOnWriteArrayList<Registration<*>>()

    @JvmStatic
    fun <T : Any> register(type: Class<T>, listener: Consumer<in T>) {
        listeners += Registration(type, listener)
    }

    @JvmStatic
    fun <T : Any> post(event: T): T {
        listeners.forEach { it.post(event) }
        return event
    }

    private class Registration<T : Any>(
        private val type: Class<T>,
        private val listener: Consumer<in T>
    ) {
        fun post(event: Any) {
            if (type.isInstance(event)) {
                listener.accept(type.cast(event))
            }
        }
    }
}
