package com.atsuishio.superbwarfare.event.custom;

import com.atsuishio.superbwarfare.api.event.ProjectileHitEvent;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public interface ProjectileHitCallback {

    Event<HitEntity> HIT_ENTITY = EventFactory.createArrayBacked(
            HitEntity.class,
            callbacks -> event -> {
                for (HitEntity callback : callbacks) {
                    callback.onHitEntity(event);

                    if (event.isCanceled()) {
                        return;
                    }
                }
            }
    );

    Event<HitBlock> HIT_BLOCK = EventFactory.createArrayBacked(
            HitBlock.class,
            callbacks -> event -> {
                for (HitBlock callback : callbacks) {
                    callback.onHitBlock(event);

                    if (event.isCanceled()) {
                        return;
                    }
                }
            }
    );

    interface HitEntity {
        void onHitEntity(ProjectileHitEvent.HitEntity event);
    }

    interface HitBlock {
        void onHitBlock(ProjectileHitEvent.HitBlock event);
    }

    static boolean postHitEntity(ProjectileHitEvent.HitEntity event) {
        HIT_ENTITY.invoker().onHitEntity(event);
        if (!event.isCanceled()) {
            ProjectileHitEvent.HIT_ENTITY.invoker().post(event);
        }
        return event.isCanceled();
    }

    static boolean postHitBlock(ProjectileHitEvent.HitBlock event) {
        HIT_BLOCK.invoker().onHitBlock(event);
        if (!event.isCanceled()) {
            ProjectileHitEvent.HIT_BLOCK.invoker().post(event);
        }
        return event.isCanceled();
    }
}
