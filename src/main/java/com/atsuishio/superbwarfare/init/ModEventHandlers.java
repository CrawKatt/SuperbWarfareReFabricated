package com.atsuishio.superbwarfare.init;

import com.atsuishio.superbwarfare.event.EntityUseGunEventHandler;
import com.atsuishio.superbwarfare.event.HitboxHelperEventHandler;
import com.atsuishio.superbwarfare.event.LivingEventHandler;
import com.atsuishio.superbwarfare.event.PlayerEventHandler;
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
            PlayerEventHandler.onPlayerLoggedIn(handler.getPlayer());
        });

        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            HitboxHelperEventHandler.onPlayerLoggedOut(handler.getPlayer());
        });

        ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> {
            PlayerEventHandler.onPlayerRespawned(newPlayer, alive);
        });

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
