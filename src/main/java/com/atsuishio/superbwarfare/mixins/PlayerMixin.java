package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity;
import com.atsuishio.superbwarfare.event.PlayerEventHandler;
import com.atsuishio.superbwarfare.item.weapon.BeastItem;
import com.atsuishio.superbwarfare.item.curio.ParachuteItem;
import com.atsuishio.superbwarfare.tools.PlayerReachTool;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(value = Player.class, priority = 1145)
public abstract class PlayerMixin extends Entity {

    public PlayerMixin(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void superbwarfare$onPlayerTick(CallbackInfo ci) {
        PlayerEventHandler.onPlayerTick((Player) (Object) this);
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

    @ModifyArg(
            method = "attack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;getEntitiesOfClass(Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;)Ljava/util/List;"
            ),
            index = 1
    )
    private AABB superbwarfare$expandBeastSweepBox(AABB box) {
        var player = (Player) (Object) this;
        return player.getMainHandItem().getItem() instanceof BeastItem ? box.inflate(3) : box;
    }

    @ModifyConstant(method = "attack", constant = @Constant(doubleValue = 9.0D))
    private double superbwarfare$useEntityReachForSweep(double original) {
        var player = (Player) (Object) this;
        double reach = PlayerReachTool.getEntityReach(player);
        return reach * reach;
    }
}
