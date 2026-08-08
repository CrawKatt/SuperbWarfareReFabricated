package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.event.EntityUseGunEventHandler;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.entity.EntityAccess;
import net.minecraft.world.level.entity.PersistentEntitySectionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PersistentEntitySectionManager.class)
public class PersistentEntitySectionManagerMixin {

    @Inject(method = "addEntity", at = @At("HEAD"))
    private void superbWarfare$onEntityJoin(EntityAccess entity, boolean loadedFromDisk,
                                             CallbackInfoReturnable<Boolean> cir) {
        if (!loadedFromDisk && entity instanceof Entity minecraftEntity) {
            EntityUseGunEventHandler.entityJoin(minecraftEntity);
        }
    }
}
