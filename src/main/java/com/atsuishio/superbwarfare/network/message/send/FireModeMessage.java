package com.atsuishio.superbwarfare.network.message.send;

import com.atsuishio.superbwarfare.Mod;
import com.atsuishio.superbwarfare.data.gun.GunData;
import com.atsuishio.superbwarfare.init.ModItems;
import com.atsuishio.superbwarfare.item.gun.GunItem;
import com.atsuishio.superbwarfare.tools.SoundTool;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import team.reborn.energy.api.EnergyStorage;
import org.jetbrains.annotations.NotNull;

public record FireModeMessage(boolean forward) implements CustomPacketPayload {

    public static final Type<FireModeMessage> TYPE = new Type<>(Mod.loc("fire_mode"));

    public static final StreamCodec<ByteBuf, FireModeMessage> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL,
            FireModeMessage::forward,
            FireModeMessage::new
    );

    public static void handler(FireModeMessage message, final ServerPlayNetworking.Context context) {
        changeFireMode(message, (ServerPlayer) context.player());
    }

    public static void changeFireMode(FireModeMessage message, ServerPlayer player) {
        var stack = player.getMainHandItem();

        if (!(stack.getItem() instanceof GunItem)) return;
        var data = GunData.from(stack);

        var selectedFireMode = data.selectedFireMode.get();
        var fireModes = data.compute().availableFireModes();

        if (fireModes.size() > 1) {
            int mode = (selectedFireMode + (message.forward() ? -1 : 1) + fireModes.size()) % fireModes.size();
            data.selectedFireMode.set(mode);
            SoundTool.playLocalSound(player, ModSounds.FIRE_RATE);
            return;
        }

        if (stack.getItem() == ModItems.SENTINEL
                && !player.isSpectator()
                && !(player.getCooldowns().isOnCooldown(stack.getItem()))
                && GunData.from(stack).reload.time() == 0
                && !GunData.from(stack).charging()) {

            for (var cell : player.getInventory().items) {
                if (cell.is(ModItems.CELL)) {
                    var cap = EnergyStorage.ITEM.find(cell, null);
                    if (cap != null && cap.getAmount() > 0) {
                        data.charge.starter.markStart();
                    }
                }
            }
        }

        if (stack.getItem() == ModItems.JAVELIN) {
            SoundTool.playLocalSound(player, ModSounds.CANNON_ZOOM_OUT);
        }
        data.save();
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
