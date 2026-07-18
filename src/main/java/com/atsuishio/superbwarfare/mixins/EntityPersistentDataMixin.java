package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.entity.mixin.EntityPersistentDataAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityPersistentDataMixin implements EntityPersistentDataAccess {

    @Unique
    @Nullable
    private CompoundTag superbwarfare$persistentData;

    @Override
    public CompoundTag superbwarfare$getPersistentData() {
        if (this.superbwarfare$persistentData == null) {
            this.superbwarfare$persistentData = new CompoundTag();
        }
        return this.superbwarfare$persistentData;
    }

    @Inject(
            method = "saveWithoutId",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Entity;addAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;)V"
            )
    )
    private void superbwarfare$savePersistentData(
            CompoundTag entityTag, CallbackInfoReturnable<CompoundTag> cir) {
        if (this.superbwarfare$persistentData != null) {
            entityTag.put("ForgeData", this.superbwarfare$persistentData.copy());
        }
    }

    @Inject(
            method = "load",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Entity;readAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;)V"
            )
    )
    private void superbwarfare$loadPersistentData(CompoundTag entityTag, CallbackInfo ci) {
        if (entityTag.contains("ForgeData", Tag.TAG_COMPOUND)) {
            this.superbwarfare$persistentData = entityTag.getCompound("ForgeData");
        }
    }
}
