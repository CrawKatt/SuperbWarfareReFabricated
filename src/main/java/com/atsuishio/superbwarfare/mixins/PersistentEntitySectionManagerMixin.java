package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.entity.vehicle.base.ArtilleryEntity;
import com.atsuishio.superbwarfare.event.EntityUseGunEventHandler;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.entity.EntityAccess;
import net.minecraft.world.level.entity.PersistentEntitySectionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PersistentEntitySectionManager.class)
public class PersistentEntitySectionManagerMixin {

    @Inject(method = "addEntity", at = @At("HEAD"))
    private void superbwarfare$onEntityJoin(
            EntityAccess entityAccess, boolean loadedFromDisk,
            CallbackInfoReturnable<Boolean> cir) {
        if (!loadedFromDisk
                && entityAccess instanceof Mob mob
                && mob.level() instanceof ServerLevel) {
            EntityUseGunEventHandler.entityJoin(mob);
        }
    }

    @Inject(method = "addEntity", at = @At("RETURN"))
    private void superbwarfare$onEntityAdded(
            EntityAccess entityAccess, boolean loadedFromDisk,
            CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValueZ()
                && entityAccess instanceof Entity entity
                && entity.level() instanceof ServerLevel
                && entity instanceof ArtilleryEntity artillery) {
            artillery.initializeShootVector();
        }
    }
}
