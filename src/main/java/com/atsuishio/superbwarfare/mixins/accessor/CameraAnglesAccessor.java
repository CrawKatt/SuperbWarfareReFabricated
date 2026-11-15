package com.atsuishio.superbwarfare.mixins.accessor;

import net.minecraft.client.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Camera.class)
public interface CameraAnglesAccessor {
    @Accessor("yRot")
    void superbwarfare$setYaw(float yaw);

    @Accessor("xRot")
    void superbwarfare$setPitch(float pitch);
}
