package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.init.ModEnumExtensions;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidModel.class)
public class HumanoidModelMixin<T extends LivingEntity> {

    @Inject(method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V", at = @At("RETURN"))
    private void superbwarfare$applyCustomArmPose(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
        ModEnumExtensions.Client.applyArmPose((HumanoidModel<?>) (Object) this);
    }
}
