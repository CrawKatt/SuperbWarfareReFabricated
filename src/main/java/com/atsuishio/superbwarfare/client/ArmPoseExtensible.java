package com.atsuishio.superbwarfare.client;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import java.util.Objects;

public interface ArmPoseExtensible {
    HumanoidModel.ArmPose create(String name, boolean twoHanded, Transformer transformer);

    boolean superbwarfare$hasTransformer();

    void superbwarfare$applyTransform(HumanoidModel<?> model, LivingEntity entity, HumanoidArm arm);

    final class Transformer {
        private final Action action;

        public Transformer(Action action) {
            this.action = Objects.requireNonNull(action, "action");
        }

        public void applyTransform(HumanoidModel<?> model, LivingEntity entity, HumanoidArm arm) {
            this.action.applyTransform(model, entity, arm);
        }
    }

    @FunctionalInterface
    interface Action {
        void applyTransform(HumanoidModel<?> model, LivingEntity entity, HumanoidArm arm);
    }
}
