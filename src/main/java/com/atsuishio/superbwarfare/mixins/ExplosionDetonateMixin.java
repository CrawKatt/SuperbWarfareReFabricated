package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.event.LivingEventHandler;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.List;

@Mixin(Explosion.class)
public class ExplosionDetonateMixin {

    @ModifyVariable(
            method = "explode",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/Vec3;<init>(DDD)V", ordinal = 1),
            ordinal = 0
    )
    private List<Entity> superbwarfare$filterVehicleEntity(List<Entity> list) {
        Explosion explosion = (Explosion) (Object) this;
        LivingEventHandler.onExplosionDetonate(explosion, list);
        return list;
    }
}
