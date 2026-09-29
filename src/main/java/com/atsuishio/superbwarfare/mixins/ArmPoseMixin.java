package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.client.ArmPoseExtensible;
import fuzs.extensibleenums.api.extensibleenums.v1.EnumAppender;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.Objects;

@Mixin(HumanoidModel.ArmPose.class)
public abstract class ArmPoseMixin implements ArmPoseExtensible {
    @Unique
    private Transformer superbwarfare$transformer;

    @Override
    public HumanoidModel.ArmPose create(String name, boolean twoHanded, Transformer transformer) {
        Objects.requireNonNull(transformer, "Cannot create new ArmPose with null transformer!");
        EnumAppender.create(HumanoidModel.ArmPose.class, boolean.class, Transformer.class)
                .addEnumConstant(name, twoHanded, transformer)
                .applyTo(HumanoidModel.class);
        return HumanoidModel.ArmPose.valueOf(name);
    }

    @Override
    public boolean superbwarfare$hasTransformer() {
        return this.superbwarfare$transformer != null;
    }

    @Override
    public void superbwarfare$applyTransform(HumanoidModel<?> model, LivingEntity entity, HumanoidArm arm) {
        if (this.superbwarfare$transformer != null) {
            this.superbwarfare$transformer.applyTransform(model, entity, arm);
        }
    }
}
