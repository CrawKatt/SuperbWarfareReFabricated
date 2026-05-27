package com.atsuishio.superbwarfare.event.custom;

import net.fabricmc.fabric.api.event.EventFactory;

public interface MouseButtonCallback {

    net.fabricmc.fabric.api.event.Event<MouseButtonCallback> EVENT = EventFactory.createArrayBacked(
            MouseButtonCallback.class,
            callbacks -> event -> {
                for (MouseButtonCallback callback : callbacks) {
                    callback.interact(event);
                }
            }
    );

    void interact(Event event);

    class Event {
        private final long window;
        private final int button;
        private final int action;
        private final int modifiers;

        private boolean canceled;

        public Event(long window, int button, int action, int modifiers) {
            this.window = window;
            this.button = button;
            this.action = action;
            this.modifiers = modifiers;
        }

        public long getWindow() {
            return window;
        }

        public int getButton() {
            return button;
        }

        public int getAction() {
            return action;
        }

        public int getModifiers() {
            return modifiers;
        }

        public boolean isCanceled() {
            return canceled;
        }

        public void setCanceled(boolean canceled) {
            this.canceled = canceled;
        }
    }
}
