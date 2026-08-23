package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.entity.mixin.ExplosionAccess;
import com.atsuishio.superbwarfare.event.LivingEventHandler;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Implements ExplosionAccess to expose the explosion radius field
 * to both client and server code without reflection.
 */
@Mixin(Explosion.class)
public abstract class ExplosionMixin implements ExplosionAccess {

    @Shadow
    @Final
    private float radius;

    @Unique
    @Override
    public float superbwarfare$getRadius() {
        return this.radius;
    }

    @Redirect(
            method = "explode",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Entity;setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V"
            )
    )
    private void superbwarfare$cancelVehicleKnockback(Entity entity, Vec3 deltaMovement) {
        if (!LivingEventHandler.onExplosionKnockback(entity)) {
            entity.setDeltaMovement(deltaMovement);
        }
    }
}
