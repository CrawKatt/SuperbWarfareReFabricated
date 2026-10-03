package com.atsuishio.superbwarfare.init;

import com.atsuishio.superbwarfare.api.event.SuperbWarfareEvents;
import com.atsuishio.superbwarfare.event.custom.MobEffectAddedEvent;
import com.atsuishio.superbwarfare.mobeffect.BurnMobEffect;
import com.atsuishio.superbwarfare.mobeffect.PhosphorusFireMobEffect;
import com.atsuishio.superbwarfare.mobeffect.ShockMobEffect;
import com.atsuishio.superbwarfare.event.HitboxHelperEventHandler;
import com.atsuishio.superbwarfare.event.GunEventHandler;
import com.atsuishio.superbwarfare.event.LivingEventHandler;
import com.atsuishio.superbwarfare.event.PlayerEventHandler;
import com.atsuishio.superbwarfare.entity.living.DPSGeneratorEntity;
import com.atsuishio.superbwarfare.entity.living.TargetEntity;
import com.atsuishio.superbwarfare.entity.vehicle.base.ArtilleryEntity;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import com.atsuishio.superbwarfare.tools.ServerSyncedEntityHandler;
import net.minecraft.world.InteractionResult;

public class ModEventHandlers {

    public static void init() {
        SuperbWarfareEvents.register(MobEffectAddedEvent.class, event ->
                BurnMobEffect.onBurnAdded(event.getEntity(), event.getEffectInstance(), event.getEffectSource()));
        SuperbWarfareEvents.register(MobEffectAddedEvent.class, event ->
                PhosphorusFireMobEffect.onPhosphorusFireAdded(event.getEntity(), event.getEffectInstance(), event.getEffectSource()));
        SuperbWarfareEvents.register(MobEffectAddedEvent.class, event ->
                ShockMobEffect.onShockAdded(event.getEntity(), event.getEffectInstance(), event.getEffectSource()));

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
        ServerLivingEntityEvents.ALLOW_DEATH.register(TargetEntity::onTargetDown);
        ServerLivingEntityEvents.ALLOW_DEATH.register(DPSGeneratorEntity::onDPSGeneratorDown);
        ServerEntityEvents.EQUIPMENT_CHANGE.register(LivingEventHandler::handleChangeSlot);
        ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
            if (entity instanceof ArtilleryEntity artillery) {
                artillery.initializeShootVec();
            }
        });

        AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            PlayerEventHandler.onAttackEntity(player, entity);
            return InteractionResult.PASS;
        });

        ServerTickEvents.START_SERVER_TICK.register(GunEventHandler::onServerTick);
        ServerTickEvents.END_SERVER_TICK.register(ServerSyncedEntityHandler::onServerTick);
    }
}
