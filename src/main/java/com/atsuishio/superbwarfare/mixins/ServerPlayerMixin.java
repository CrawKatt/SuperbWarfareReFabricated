package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.event.custom.LivingDeathCallback;
import com.atsuishio.superbwarfare.event.custom.LivingDropsCallback;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin {
    @Inject(method = "die", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/GameRules;getBoolean(Lnet/minecraft/world/level/GameRules$Key;)Z", ordinal = 0), cancellable = true)
    private void superbwarfare$onPlayerDeath(DamageSource source, CallbackInfo ci) {
        var event = new LivingDeathCallback.Event((ServerPlayer) (Object) this, source);
        LivingDeathCallback.EVENT.invoker().onDeath(event);
        if (event.isCanceled()) ci.cancel();
    }

    @WrapOperation(method = "drop(Lnet/minecraft/world/item/ItemStack;ZZ)Lnet/minecraft/world/entity/item/ItemEntity;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"))
    private boolean superbwarfare$captureLivingDrop(Level level, Entity drop, Operation<Boolean> original) {
        if (drop instanceof ItemEntity item && LivingDropsCallback.captureDrop((Entity) (Object) this, item)) return true;
        return original.call(level, drop);
    }
}
