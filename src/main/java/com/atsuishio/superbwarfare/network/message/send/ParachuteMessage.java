package com.atsuishio.superbwarfare.network.message.send;

import com.atsuishio.superbwarfare.init.ModItems;
import com.atsuishio.superbwarfare.init.ModSounds;
import com.atsuishio.superbwarfare.item.curio.ParachuteItem;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;

public enum ParachuteMessage {
    INSTANCE;

    public static void handler(ServerPlayer player) {
        if (player == null) return;

        TrinketsApi.getTrinketComponent(player).ifPresent(
                c -> c.getEquipped(ModItems.PARACHUTE.get()).stream().findFirst().ifPresent(
                        pair -> {
                            var stack = pair.getB();
                            if (!player.getCooldowns().isOnCooldown(stack.getItem())) {
                                if (!stack.getOrCreateTag().getBoolean(ParachuteItem.TAG_OPEN) && player.getDeltaMovement().y < -0.6 && player.fallDistance > 4) {
                                    stack.getOrCreateTag().putBoolean(ParachuteItem.TAG_OPEN, true);
                                    player.getCooldowns().addCooldown(stack.getItem(), 10);
                                    player.level().playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.PARACHUTE_OPEN.get(), SoundSource.PLAYERS, 1f, 1);
                                } else if (stack.getOrCreateTag().getBoolean(ParachuteItem.TAG_OPEN)) {
                                    stack.getOrCreateTag().putBoolean(ParachuteItem.TAG_OPEN, false);
                                    player.getCooldowns().addCooldown(stack.getItem(), 10);
                                    player.level().playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.PARACHUTE_CLOSE.get(), SoundSource.PLAYERS, 1f, 1);
                                }
                            }
                        }
                )
        );
    }
}
