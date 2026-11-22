package com.atsuishio.superbwarfare.event.custom;

import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.InteractionHand;

public interface InteractionKeyMappingTriggeredCallback {
    net.fabricmc.fabric.api.event.Event<InteractionKeyMappingTriggeredCallback> EVENT = EventFactory.createArrayBacked(
            InteractionKeyMappingTriggeredCallback.class,
            callbacks -> event -> {
                for (var callback : callbacks) callback.interact(event);
            }
    );

    void interact(Event event);

    class Event {
        private final InteractionHand hand;
        private final boolean attack;
        private boolean canceled;
        private boolean swingHand = true;

        public Event(InteractionHand hand, boolean attack) {
            this.hand = hand;
            this.attack = attack;
        }

        public InteractionHand getHand() {
            return hand;
        }

        public boolean isAttack() {
            return attack;
        }

        public boolean isCanceled() {
            return canceled;
        }

        public void setCanceled(boolean canceled) {
            this.canceled = canceled;
        }

        public boolean shouldSwingHand() {
            return swingHand;
        }

        public void setSwingHand(boolean swingHand) {
            this.swingHand = swingHand;
        }
    }
}
