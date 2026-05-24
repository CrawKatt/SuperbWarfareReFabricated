package com.atsuishio.superbwarfare.compat.thermoo;

import com.github.thedeathlycow.thermoo.api.temperature.TemperatureAware;
import net.minecraft.server.level.ServerPlayer;

final class ThermooCompatApi {
    static void setTemperature(ServerPlayer player, int temperature) {
        TemperatureAware.get(player).thermoo$setTemperature(temperature);
    }
}
