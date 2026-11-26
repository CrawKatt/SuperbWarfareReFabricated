package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.event.custom.EntitySectionChangeCallback;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.entity.EntityAccess;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(targets = "net.minecraft.world.level.entity.PersistentEntitySectionManager$Callback")
public class PersistentEntitySectionCallbackMixin {
    @Shadow
    @Final
    private EntityAccess entity;

    @Shadow
    private long currentSectionKey;

    @WrapMethod(method = "onMove")
    private void superbwarfare$onSectionChange(Operation<Void> original) {
        long oldSection = this.currentSectionKey;
        original.call();
        if (oldSection != this.currentSectionKey && this.entity instanceof Entity moved) {
            EntitySectionChangeCallback.EVENT.invoker().onSectionChange(moved, oldSection, this.currentSectionKey);
        }
    }
}
