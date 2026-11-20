package com.atsuishio.superbwarfare.event.custom;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;

public interface ExplosionStartCallback {
    Event<ExplosionStartCallback> EVENT = EventFactory.createArrayBacked(
            ExplosionStartCallback.class,
            callbacks -> (level, explosion) -> {
                for (ExplosionStartCallback callback : callbacks) {
                    if (callback.onExplosionStart(level, explosion)) {
                        return true;
                    }
                }
                return false;
            }
    );

    boolean onExplosionStart(Level level, Explosion explosion);
}
