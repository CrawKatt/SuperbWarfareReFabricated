package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.init.ModEntities;
import net.minecraft.world.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityType.class)
public class EntityTypeMixin {

    @Inject(method = "trackDeltas", at = @At("HEAD"), cancellable = true)
    private void superbwarfare$disableProjectileVelocityUpdates(CallbackInfoReturnable<Boolean> cir) {
        if (ModEntities.NO_VELOCITY_UPDATES.contains((EntityType<?>) (Object) this)) {
            cir.setReturnValue(false);
        }
    }
}
