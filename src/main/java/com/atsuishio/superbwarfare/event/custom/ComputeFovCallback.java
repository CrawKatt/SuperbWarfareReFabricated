package com.atsuishio.superbwarfare.event.custom;

import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.Camera;

public interface ComputeFovCallback {

    net.fabricmc.fabric.api.event.Event<ComputeFovCallback> EVENT = EventFactory.createArrayBacked(
            ComputeFovCallback.class,
            callbacks -> event -> {
                for (ComputeFovCallback callback : callbacks) {
                    callback.onComputeFov(event);
                }
            }
    );

    void onComputeFov(Event event);

    class Event {
        private final Camera camera;
        private final float partialTick;
        private double fov;
        private final boolean usedConfiguredFov;

        public Event(Camera camera, float partialTick, double fov, boolean usedConfiguredFov) {
            this.camera = camera;
            this.partialTick = partialTick;
            this.fov = fov;
            this.usedConfiguredFov = usedConfiguredFov;
        }

        public Camera getCamera() {
            return this.camera;
        }

        public float getPartialTick() {
            return this.partialTick;
        }

        public double getFOV() {
            return this.fov;
        }

        public void setFOV(double fov) {
            this.fov = fov;
        }

        public boolean usedConfiguredFov() {
            return this.usedConfiguredFov;
        }
    }
}