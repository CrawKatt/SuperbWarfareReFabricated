package com.atsuishio.superbwarfare.init;

import com.atsuishio.superbwarfare.capability.player.PlayerVariable;
import com.atsuishio.superbwarfare.event.EntityUseGunEventHandler;
import com.atsuishio.superbwarfare.event.HitboxHelperEventHandler;
import com.atsuishio.superbwarfare.event.LivingEventHandler;
import com.atsuishio.superbwarfare.event.PlayerEventHandler;
import com.atsuishio.superbwarfare.network.message.receive.PlayerVariablesSyncMessage;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.world.InteractionResult;

public class ModEventHandlers {

    public static void init() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            PlayerVariable.onPlayerLogin(handler.getPlayer());
            PlayerEventHandler.onPlayerLoggedIn(handler.getPlayer());
        });

        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            var player = handler.getPlayer();
            PlayerVariable.getOrDefault(player).watch();
            HitboxHelperEventHandler.onPlayerLoggedOut(player);
        });

        ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> {
            PlayerVariable.onPlayerRespawn(newPlayer);
            PlayerEventHandler.onPlayerRespawned(newPlayer, alive);
        });

        ServerEntityWorldChangeEvents.AFTER_ENTITY_CHANGE_WORLD.register((originalEntity, newEntity, origin, destination) -> {
            if (newEntity instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
                PlayerVariable.onPlayerChangeDimension(serverPlayer);
            }
        });

        ServerPlayerEvents.COPY_FROM.register(PlayerVariable::onPlayerClone);

        ServerLivingEntityEvents.AFTER_DEATH.register(LivingEventHandler::onEntityDeath);

        AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            PlayerEventHandler.onAttackEntity(player, entity);
            return InteractionResult.PASS;
        });

        ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> EntityUseGunEventHandler.entityJoin(entity));
    }

    public static void initClient() {
    }
}
