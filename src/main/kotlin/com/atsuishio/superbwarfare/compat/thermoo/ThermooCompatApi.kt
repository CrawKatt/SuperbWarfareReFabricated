package com.atsuishio.superbwarfare.compat.thermoo

import com.github.thedeathlycow.thermoo.api.temperature.TemperatureAware
import net.minecraft.server.level.ServerPlayer

object ThermooCompatApi {
    @JvmStatic
    fun setTemperature(player: ServerPlayer, temperature: Int) {
        TemperatureAware.get(player).`thermoo$setTemperature`(temperature)
    }
}