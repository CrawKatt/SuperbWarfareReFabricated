package com.atsuishio.superbwarfare.init;

import com.atsuishio.superbwarfare.api.event.SuperbWarfareEvents;
import com.atsuishio.superbwarfare.event.custom.MobEffectAddedEvent;
import com.atsuishio.superbwarfare.mobeffect.BurnMobEffect;
import com.atsuishio.superbwarfare.mobeffect.PhosphorusFireMobEffect;
import com.atsuishio.superbwarfare.mobeffect.ShockMobEffect;
import com.atsuishio.superbwarfare.event.custom.PlayerXpChangeCallback;
import com.atsuishio.superbwarfare.event.custom.LivingDeathCallback;
import com.atsuishio.superbwarfare.event.custom.LivingDropsCallback;
import com.atsuishio.superbwarfare.event.custom.LivingExperienceDropCallback;
import com.atsuishio.superbwarfare.perk.functional.PowerfulAttraction;
import com.atsuishio.superbwarfare.perk.functional.StrivingForPerfection;
import com.atsuishio.superbwarfare.event.HitboxHelperEventHandler;
import com.atsuishio.superbwarfare.event.GunEventHandler;
import com.atsuishio.superbwarfare.event.LivingEventHandler;
import com.atsuishio.superbwarfare.event.ShieldHandler;
import com.atsuishio.superbwarfare.event.custom.LivingIncomingDamageCallback;
import com.atsuishio.superbwarfare.event.PlayerEventHandler;
import com.atsuishio.superbwarfare.entity.living.DPSGeneratorEntity;
import com.atsuishio.superbwarfare.entity.living.TargetEntity;
import com.atsuishio.superbwarfare.entity.vehicle.base.ArtilleryEntity;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import com.atsuishio.superbwarfare.tools.ServerSyncedEntityHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;

public class ModEventHandlers {

    public static void init() {
        PlayerXpChangeCallback.EVENT.register(StrivingForPerfection.INSTANCE::onXpChange);
        ResourceLocation meleeRespawnPhase = ResourceLocation.fromNamespaceAndPath("superbwarfare", "melee_respawn");
        LivingIncomingDamageCallback.EVENT.addPhaseOrdering(meleeRespawnPhase, Event.DEFAULT_PHASE);
        LivingIncomingDamageCallback.EVENT.register(meleeRespawnPhase, PlayerEventHandler::blockRespawnMeleeDamage);
        AttackEntityCallback.EVENT.addPhaseOrdering(meleeRespawnPhase, Event.DEFAULT_PHASE);
        AttackEntityCallback.EVENT.register(meleeRespawnPhase, (player, world, hand, entity, hitResult) ->
                PlayerEventHandler.blockRespawnMeleeAttack(player) ? InteractionResult.FAIL : InteractionResult.PASS);
        LivingIncomingDamageCallback.EVENT.register(ShieldHandler::onEntityHurt);
        SuperbWarfareEvents.register(MobEffectAddedEvent.class, event ->
                BurnMobEffect.onBurnAdded(event.getEntity(), event.getEffectInstance(), event.getEffectSource()));
        SuperbWarfareEvents.register(MobEffectAddedEvent.class, event ->
                PhosphorusFireMobEffect.onPhosphorusFireAdded(event.getEntity(), event.getEffectInstance(), event.getEffectSource()));
        SuperbWarfareEvents.register(MobEffectAddedEvent.class, event ->
                ShockMobEffect.onShockAdded(event.getEntity(), event.getEffectInstance(), event.getEffectSource()));

        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            HitboxHelperEventHandler.onPlayerLoggedOut(handler.getPlayer());
        });

        ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> {
            PlayerEventHandler.onPlayerRespawned(newPlayer, alive);
        });
        ServerEntityWorldChangeEvents.AFTER_PLAYER_CHANGE_WORLD.register((player, origin, destination) ->
                PlayerEventHandler.onPlayerChangedDimension(player));

        ServerLivingEntityEvents.AFTER_DEATH.register((entity, source) -> {
            if (entity instanceof ServerPlayer player) {
                PlayerEventHandler.onPlayerDeath(player);
            }
        });

        LivingDeathCallback.EVENT.register(event -> LivingEventHandler.onEntityDeath(event.getEntity(), event.getSource()));
        ResourceLocation lowestDeathPhase = ResourceLocation.fromNamespaceAndPath("superbwarfare", "lowest_death");
        LivingDeathCallback.EVENT.addPhaseOrdering(Event.DEFAULT_PHASE, lowestDeathPhase);
        LivingDeathCallback.EVENT.register(lowestDeathPhase, PowerfulAttraction.INSTANCE::onLivingDeath);
        LivingDropsCallback.EVENT.register(event -> LivingEventHandler.onLivingDrops(event.getEntity(), event.getSource(), event.getDrops()));
        LivingDropsCallback.EVENT.register(PowerfulAttraction.INSTANCE::onLivingDrops);
        LivingExperienceDropCallback.EVENT.register(event -> {
            if (LivingEventHandler.onLivingExperienceDrop(event.getEntity(), event.getAttackingPlayer(), event.getDroppedExperience())) {
                event.setCanceled(true);
            }
        });
        LivingExperienceDropCallback.EVENT.register(PowerfulAttraction.INSTANCE::onLivingExperienceDrop);
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
