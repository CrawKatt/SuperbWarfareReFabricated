package com.atsuishio.superbwarfare.resource.gun;

import com.atsuishio.superbwarfare.data.IDBasedData;
import com.atsuishio.superbwarfare.data.ModColor;
import com.atsuishio.superbwarfare.init.ModSounds;
import com.atsuishio.superbwarfare.resource.ModelResource;
import com.google.gson.annotations.SerializedName;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.phys.Vec3;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

public class DefaultGunResource implements IDBasedData<DefaultGunResource> {

    private transient String id = "";

    @Override
    public @NotNull String getId() {
        return this.id;
    }

    @Override
    public void setId(@NotNull String id) {
        this.id = id;
    }

    @SerializedName("SlotIcon")
    public String slotIcon = "";

    @SerializedName("ItemDisplay")
    public Map<String, ItemDisplayInfo> itemDisplay = new HashMap<>();

    @SerializedName("Model")
    public ModelResource model = new ModelResource();

    public ModelResource getModel() {
        return model == null ? new ModelResource() : model;
    }

    @SerializedName("Animation")
    public GunAnimation animation = new GunAnimation();

    @SerializedName("UseOldHandRenderer")
    public boolean useOldHandRenderer = false;

    @SerializedName("FlarePosition")
    public Vec3 flarePosition = null;

    @SerializedName("FlareSize")
    public float flareSize = 1;

    @SerializedName("HideCrosshairWhenZoom")
    public boolean hideCrosshairWhenZoom = true;

    @SerializedName("EnergyBarColor")
    public ModColor energyBarColor = new ModColor(0x95E9FF);

    @SerializedName("TriggerSound")
    public SoundEvent triggerSound = ModSounds.TRIGGER_CLICK;
    @SerializedName("DischargeSound")
    public SoundEvent dischargeSound = null;

    @SerializedName("EjectShell")
    public boolean ejectShell = false;

    @SerializedName("ShellEject")
    public ShellEject shellEject = null;

    @SerializedName("CanZoom")
    public boolean canZoom = true;

    @SerializedName("SprintOffset")
    public Vec3 sprintOffset = Vec3.ZERO;

    public static class ItemDisplayInfo {
        @SerializedName("translation")
        public float[] translation = {0f, 0f, 0f};

        @SerializedName("rotation")
        public float[] rotation = {0f, 0f, 0f};

        @SerializedName("scale")
        public float[] scale = {0f, 0f, 0f};
    }

    public static class ShellEject {
        @SerializedName("BoneName")
        public String boneName = "shell";

        @SerializedName("ShellModel")
        public net.minecraft.resources.ResourceLocation shellModel = null;

        @SerializedName("ShellTexture")
        public net.minecraft.resources.ResourceLocation shellTexture = null;

        @SerializedName("Size")
        public float size = 1f;

        @SerializedName("InitialVelocity")
        public Vec3 initialVelocity = new Vec3(1.6, 0.9, 0.25);

        @SerializedName("RandomVelocity")
        public Vec3 randomVelocity = new Vec3(0.4, 0.35, 0.15);

        @SerializedName("Acceleration")
        public Vec3 acceleration = new Vec3(0, -18, 0);

        @SerializedName("AngularVelocity")
        public Vec3 angularVelocity = new Vec3(-1800, -2000, 240);

        @SerializedName("LivingTime")
        public float livingTime = 0.9f;

        @SerializedName("MaxActive")
        public int maxActive = 32;
    }
}
