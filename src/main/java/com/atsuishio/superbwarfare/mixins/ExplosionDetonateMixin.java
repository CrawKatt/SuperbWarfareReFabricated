package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.event.LivingEventHandler;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(Explosion.class)
public class ExplosionDetonateMixin {

    @Inject(
            method = "explode",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/Vec3;<init>(DDD)V"),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void superbwarfare$onExplosionDetonate(CallbackInfo ci, List<Entity> list) {
        Explosion explosion = (Explosion) (Object) this;
        LivingEventHandler.onExplosionDetonate(explosion, list);
    }
}
