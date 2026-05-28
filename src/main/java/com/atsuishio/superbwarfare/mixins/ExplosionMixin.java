package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.entity.mixin.ExplosionAccess;
import net.minecraft.world.level.Explosion;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Explosion.class)
public class ExplosionMixin implements ExplosionAccess {

    @Shadow
    @Final
    private float radius;

    @Shadow
    @Final
    private double x;

    @Shadow
    @Final
    private double y;

    @Shadow
    @Final
    private double z;

    @Override
    public float superbwarfare$getRadius() {
        return this.radius;
    }

    @Override
    public double superbwarfare$getX() {
        return this.x;
    }

    @Override
    public double superbwarfare$getY() {
        return this.y;
    }

    @Override
    public double superbwarfare$getZ() {
        return this.z;
    }
}
