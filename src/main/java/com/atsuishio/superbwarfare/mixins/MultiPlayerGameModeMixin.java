package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.init.ModItems;
import com.atsuishio.superbwarfare.network.message.send.CreativeContainerStackMessage;
import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.SeededContainerLoot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MultiPlayerGameMode.class)
public class MultiPlayerGameModeMixin {

    @Shadow
    @Final
    private Minecraft minecraft;

    @Inject(method = "isServerControlledInventory", at = @At("HEAD"), cancellable = true)
    public void isServerControlledInventory(CallbackInfoReturnable<Boolean> cir) {
        var player = this.minecraft.player;
        if (player == null) return;
        if (player.isPassenger() && player.getVehicle() instanceof VehicleEntity vehicle) {
            cir.setReturnValue(vehicle.hasMenu());
        }
    }

    @Inject(method = "handleCreativeModeItemAdd", at = @At("TAIL"))
    private void superbwarfare$syncCreativeContainerStack(ItemStack stack, int slot, CallbackInfo ci) {
        if (slot < 1 || slot > 45 || stack.isEmpty()) return;

        CompoundTag tag = superbwarfare$getCreativeContainerTag(stack);
        if (tag == null) return;

        ClientPlayNetworking.send(new CreativeContainerStackMessage(
                slot,
                BuiltInRegistries.ITEM.getKey(stack.getItem()),
                tag
        ));
    }

    @Unique
    private static CompoundTag superbwarfare$getCreativeContainerTag(ItemStack stack) {
        if (stack.is(ModItems.CONTAINER) || stack.is(ModItems.LUCKY_CONTAINER)) {
            CustomData data = stack.get(DataComponents.BLOCK_ENTITY_DATA);
            return data != null && !data.isEmpty() ? data.copyTag() : null;
        }

        if (stack.is(ModItems.SMALL_CONTAINER)) {
            SeededContainerLoot loot = stack.get(DataComponents.CONTAINER_LOOT);
            if (loot == null) return null;

            CompoundTag tag = new CompoundTag();
            tag.putString("LootTable", loot.lootTable().location().toString());
            if (loot.seed() != 0L) {
                tag.putLong("LootTableSeed", loot.seed());
            }
            return tag;
        }

        return null;
    }
}
