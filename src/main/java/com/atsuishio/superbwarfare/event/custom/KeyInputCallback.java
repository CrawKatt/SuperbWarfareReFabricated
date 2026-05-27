package com.atsuishio.superbwarfare.event.custom;

import net.fabricmc.fabric.api.event.EventFactory;

public interface KeyInputCallback {
    net.fabricmc.fabric.api.event.Event<KeyInputCallback> EVENT = EventFactory.createArrayBacked(
            KeyInputCallback.class,
            callbacks -> event -> {
                for (KeyInputCallback callback : callbacks) {
                    callback.interact(event);

                    if (event.isCanceled()) {
                        return;
                    }
                }
            }
    );

    void interact(Event event);

    class Event {
        private final long window;
        private final int key;
        private final int scanCode;
        private final int action;
        private final int modifiers;

        private boolean canceled;

        public Event(long window, int key, int scanCode, int action, int modifiers) {
            this.window = window;
            this.key = key;
            this.scanCode = scanCode;
            this.action = action;
            this.modifiers = modifiers;
        }

        public long getWindow() {
            return this.window;
        }

        public int getKey() {
            return this.key;
        }

        public int getScanCode() {
            return this.scanCode;
        }

        public int getAction() {
            return this.action;
        }

        public int getModifiers() {
            return this.modifiers;
        }

        public boolean isCanceled() {
            return this.canceled;
        }

        public void setCanceled(boolean canceled) {
            this.canceled = canceled;
        }
    }
}