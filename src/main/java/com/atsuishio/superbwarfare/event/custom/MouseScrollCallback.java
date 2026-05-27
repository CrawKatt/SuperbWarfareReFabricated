package com.atsuishio.superbwarfare.event.custom;

import net.fabricmc.fabric.api.event.EventFactory;

public interface MouseScrollCallback {
    net.fabricmc.fabric.api.event.Event<MouseScrollCallback> EVENT = EventFactory.createArrayBacked(
            MouseScrollCallback.class,
            callbacks -> event -> {
                for (MouseScrollCallback callback : callbacks) {
                    callback.interact(event);

                    if (event.isCanceled()) {
                        return;
                    }
                }
            }
    );

    void interact(Event event);

    class Event {

        private final double scrollDelta;
        private boolean canceled;

        public Event(double scrollDelta) {
            this.scrollDelta = scrollDelta;
        }

        public double getScrollDelta() {
            return this.scrollDelta;
        }

        public boolean isCanceled() {
            return this.canceled;
        }

        public void setCanceled(boolean canceled) {
            this.canceled = canceled;
        }
    }
}