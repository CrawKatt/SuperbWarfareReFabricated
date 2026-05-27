package com.atsuishio.superbwarfare.event.custom;

import net.fabricmc.fabric.api.event.EventFactory;

public interface RenderGuiOverlayCallback {

    net.fabricmc.fabric.api.event.Event<RenderGuiOverlayCallback> EVENT = EventFactory.createArrayBacked(
            RenderGuiOverlayCallback.class,
            callbacks -> event -> {
                for (RenderGuiOverlayCallback callback : callbacks) {
                    callback.onRenderOverlay(event);
                }
            }
    );

    void onRenderOverlay(Event event);

    enum Overlay {
        CROSSHAIR,
        HOTBAR
    }

    class Event {

        private final Overlay overlay;
        private boolean canceled;

        public Event(Overlay overlay) {
            this.overlay = overlay;
        }

        public Overlay getOverlay() {
            return this.overlay;
        }

        public boolean isCanceled() {
            return this.canceled;
        }

        public void setCanceled(boolean canceled) {
            this.canceled = canceled;
        }
    }
}