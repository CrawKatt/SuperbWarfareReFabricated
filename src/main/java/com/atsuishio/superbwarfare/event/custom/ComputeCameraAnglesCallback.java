package com.atsuishio.superbwarfare.event.custom;

import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.Camera;
import net.minecraft.resources.ResourceLocation;

public interface ComputeCameraAnglesCallback {
    ResourceLocation LOWEST_PHASE = new ResourceLocation("superbwarfare", "camera_angles_lowest");
    net.fabricmc.fabric.api.event.Event<ComputeCameraAnglesCallback> EVENT = EventFactory.createWithPhases(
            ComputeCameraAnglesCallback.class,
            callbacks -> event -> {
                for (ComputeCameraAnglesCallback callback : callbacks) {
                    callback.onComputeCameraAngles(event);
                }
            },
            net.fabricmc.fabric.api.event.Event.DEFAULT_PHASE, LOWEST_PHASE
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
