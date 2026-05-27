package com.atsuishio.superbwarfare.event.custom;

import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.Camera;

public interface ComputeCameraAnglesCallback {
    net.fabricmc.fabric.api.event.Event<ComputeCameraAnglesCallback> EVENT = EventFactory.createArrayBacked(
            ComputeCameraAnglesCallback.class,
            callbacks -> event -> {
                for (ComputeCameraAnglesCallback callback : callbacks) {
                    callback.onComputeCameraAngles(event);
                }
            }
    );

    void onComputeCameraAngles(Event event);

    class Event {
        private final Camera camera;
        private final double partialTick;
        private float yaw;
        private float pitch;
        private float roll;

        public Event(Camera camera, double partialTick, float yaw, float pitch, float roll) {
            this.camera = camera;
            this.partialTick = partialTick;
            this.yaw = yaw;
            this.pitch = pitch;
            this.roll = roll;
        }

        public Camera getCamera() {
            return this.camera;
        }

        public double getPartialTick() {
            return this.partialTick;
        }

        public float getYaw() {
            return this.yaw;
        }

        public void setYaw(float yaw) {
            this.yaw = yaw;
        }

        public float getPitch() {
            return this.pitch;
        }

        public void setPitch(float pitch) {
            this.pitch = pitch;
        }

        public float getRoll() {
            return this.roll;
        }

        public void setRoll(float roll) {
            this.roll = roll;
        }
    }
}