package com.atsuishio.superbwarfare.network.message.send;

import com.atsuishio.superbwarfare.data.gun.GunData;
import com.atsuishio.superbwarfare.item.gun.GunItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.server.level.ServerPlayer;

public class WeaponZoomingMessage {
    private final boolean zooming;

    public WeaponZoomingMessage(boolean zooming) {
        this.zooming = zooming;
    }

    public static WeaponZoomingMessage decode(FriendlyByteBuf buffer) {
        return new WeaponZoomingMessage(buffer.readBoolean());
    }

    public static void encode(WeaponZoomingMessage message, FriendlyByteBuf buffer) {
        buffer.writeBoolean(message.zooming);
    }

    public static void handler(WeaponZoomingMessage message, ServerPlayer player) {
        if (player != null) {
            ItemStack stack = player.getMainHandItem();
            if (!(stack.getItem() instanceof GunItem)) return;
            var data = GunData.from(stack);
            data.zooming.set(message.zooming);
        }
    }
}
