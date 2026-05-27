package com.atsuishio.superbwarfare.event.custom;

import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.InteractionHand;

public interface InteractionKeyMappingTriggeredCallback {
    net.fabricmc.fabric.api.event.Event<InteractionKeyMappingTriggeredCallback> EVENT = EventFactory.createArrayBacked(
            InteractionKeyMappingTriggeredCallback.class,
            callbacks -> event -> {
                for (InteractionKeyMappingTriggeredCallback callback : callbacks) {
                    callback.interact(event);
                }
            }
    );

    void interact(Event event);

    class Event {
        private final InteractionHand hand;
        private boolean swingHand = true;

        public Event(InteractionHand hand) {
            this.hand = hand;
        }

        public InteractionHand getHand() {
            return this.hand;
        }

        public boolean shouldSwingHand() {
            return this.swingHand;
        }

        public void setSwingHand(boolean swingHand) {
            this.swingHand = swingHand;
        }
    }
}
