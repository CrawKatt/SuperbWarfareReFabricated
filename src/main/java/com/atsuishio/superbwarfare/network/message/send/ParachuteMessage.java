package com.atsuishio.superbwarfare.network.message.send;

import com.atsuishio.superbwarfare.Mod;
import com.atsuishio.superbwarfare.init.ModItems;
import com.atsuishio.superbwarfare.item.trinket.ParachuteItem;
import com.atsuishio.superbwarfare.tools.NBTTool;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.sounds.SoundSource;
import dev.emi.trinkets.api.TrinketsApi;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import org.jetbrains.annotations.NotNull;

public enum ParachuteMessage implements CustomPacketPayload {
    INSTANCE;

    public static final Type<ParachuteMessage> TYPE = new Type<>(Mod.loc("parachute"));

    public static final StreamCodec<ByteBuf, ParachuteMessage> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    public static void handler(final ServerPlayNetworking.Context context) {
        var player = context.player();

        TrinketsApi.getTrinketComponent(player).flatMap(c -> c.getEquipped(ModItems.PARACHUTE).stream().findFirst()).ifPresent(pair -> {
            var stack = pair.getB();
            if (!player.getCooldowns().isOnCooldown(stack.getItem())) {
                var tag = NBTTool.getTag(stack);
                if (!tag.getBoolean(ParachuteItem.TAG_OPEN) && player.getDeltaMovement().y < -0.6 && player.fallDistance > 4) {
                    tag.putBoolean(ParachuteItem.TAG_OPEN, true);
                    NBTTool.saveTag(stack, tag);
                    player.getCooldowns().addCooldown(stack.getItem(), 10);
                    player.level().playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.PARACHUTE_OPEN, SoundSource.PLAYERS, 1f, 1);
                } else if (tag.getBoolean(ParachuteItem.TAG_OPEN)) {
                    tag.putBoolean(ParachuteItem.TAG_OPEN, false);
                    NBTTool.saveTag(stack, tag);
                    player.getCooldowns().addCooldown(stack.getItem(), 10);
                    player.level().playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.PARACHUTE_CLOSE, SoundSource.PLAYERS, 1f, 1);
                }
            }
        });
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
