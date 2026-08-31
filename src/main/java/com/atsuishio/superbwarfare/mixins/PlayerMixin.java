package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity;
import com.atsuishio.superbwarfare.item.trinket.ParachuteItem;
import com.atsuishio.superbwarfare.item.gun.special.BeastGunTestItem;
import com.atsuishio.superbwarfare.item.weapon.BeastItem;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(value = Player.class, priority = 1145)
public abstract class PlayerMixin extends Entity {

    public PlayerMixin(EntityType<?> type, Level world) {
        super(type, world);
    }

    /**
     * Code based on @Luke100000's ImmersiveAircraft
     */
    @Inject(method = "wantsToStopRiding", at = @At("HEAD"), cancellable = true)
    public void shouldDismountInjection(CallbackInfoReturnable<Boolean> cir) {
        if (this.getRootVehicle() instanceof VehicleEntity) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "updatePlayerPose()V", at = @At("TAIL"))
    public void updatePostInjection(CallbackInfo ci) {
        if (getRootVehicle() instanceof VehicleEntity) {
            this.setPose(Pose.STANDING);
        }
        var player = (Player) (Object) this;
        if (ParachuteItem.isParachuteOpen(player)) {
            this.setPose(Pose.STANDING);
        }
    }

    @Inject(method = "attack", at = @At("HEAD"))
    public void superbwarfare$beastOnLeftClickEntity(Entity target, CallbackInfo ci) {
        Player player = (Player) (Object) this;
        ItemStack stack = player.getMainHandItem();

        if (stack.getItem() instanceof BeastItem || stack.getItem() instanceof BeastGunTestItem) {
            BeastItem.onLeftClickEntity(stack, player, target);

            if (target instanceof Player targetPlayer
                    && targetPlayer.isBlocking()
                    && targetPlayer.getUseItem().getItem() instanceof ShieldItem) {
                targetPlayer.getCooldowns().addCooldown(targetPlayer.getUseItem().getItem(), 100);
                targetPlayer.stopUsingItem();
                targetPlayer.level().broadcastEntityEvent(targetPlayer, (byte) 30);
            }
        }
    }

    @ModifyArgs(
            method = "attack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/phys/AABB;inflate(DDD)Lnet/minecraft/world/phys/AABB;",
                    ordinal = 0
            )
    )
    public void superbwarfare$beastSweepHitBox(Args args) {
        Player player = (Player) (Object) this;
        ItemStack stack = player.getMainHandItem();

        if (stack.getItem() instanceof BeastItem || stack.getItem() instanceof BeastGunTestItem) {
            args.set(0, (Double) args.get(0) + 3.0D);
            args.set(1, (Double) args.get(1) + 3.0D);
            args.set(2, (Double) args.get(2) + 3.0D);
        }
    }
}
