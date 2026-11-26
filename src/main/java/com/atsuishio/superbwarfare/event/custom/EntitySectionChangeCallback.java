package com.atsuishio.superbwarfare.event.custom;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.Entity;

public interface EntitySectionChangeCallback {
    Event<EntitySectionChangeCallback> EVENT = EventFactory.createArrayBacked(EntitySectionChangeCallback.class,
            callbacks -> (entity, oldSection, newSection) -> {
                for (EntitySectionChangeCallback callback : callbacks) {
                    callback.onSectionChange(entity, oldSection, newSection);
                }
            });

    void onSectionChange(Entity entity, long oldSection, long newSection);
}
