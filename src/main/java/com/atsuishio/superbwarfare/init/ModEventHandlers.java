package com.atsuishio.superbwarfare.init;

import com.atsuishio.superbwarfare.capability.player.PlayerVariable;
import com.atsuishio.superbwarfare.network.message.receive.PlayerVariablesSyncMessage;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

public class ModEventHandlers {

    public static void init() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            PlayerVariable.onPlayerLogin(handler.getPlayer());
        });

        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            PlayerVariable.getOrDefault(handler.getPlayer()).watch();
        });

        ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> {
            PlayerVariable.onPlayerRespawn(newPlayer);
        });

        ServerEntityWorldChangeEvents.AFTER_ENTITY_CHANGE_WORLD.register((originalEntity, newEntity, origin, destination) -> {
            if (newEntity instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
                PlayerVariable.onPlayerChangeDimension(serverPlayer);
            }
        });

        ServerPlayerEvents.COPY_FROM.register(PlayerVariable::onPlayerClone);
    }

    public static void initClient() {
        ClientPlayNetworking.registerGlobalReceiver(PlayerVariablesSyncMessage.TYPE, (payload, context)
                -> context.client().execute(()
                -> PlayerVariablesSyncMessage.handler(payload)));
    }
}
