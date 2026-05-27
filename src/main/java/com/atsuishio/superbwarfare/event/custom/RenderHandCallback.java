package com.atsuishio.superbwarfare.event.custom;

import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.InteractionHand;

public interface RenderHandCallback {

    net.fabricmc.fabric.api.event.Event<RenderHandCallback> EVENT = EventFactory.createArrayBacked(
            RenderHandCallback.class,
            callbacks -> event -> {
                for (RenderHandCallback callback : callbacks) {
                    callback.onRenderHand(event);
                }
            }
    );

    void onRenderHand(Event event);

    class Event {
        private final InteractionHand hand;
        private final float partialTick;
        private boolean canceled;

        public Event(InteractionHand hand, float partialTick) {
            this.hand = hand;
            this.partialTick = partialTick;
        }

        public InteractionHand getHand() {
            return this.hand;
        }

        public float getPartialTick() {
            return this.partialTick;
        }

        public boolean isCanceled() {
            return this.canceled;
        }

        public void setCanceled(boolean canceled) {
            this.canceled = canceled;
        }
    }
}