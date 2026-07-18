package com.atsuishio.superbwarfare.network;

import com.atsuishio.superbwarfare.Mod;
import com.atsuishio.superbwarfare.network.message.receive.*;
import com.atsuishio.superbwarfare.network.message.send.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.EntityTrackingEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerLoginConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerLoginNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class NetworkRegistry {

    private static final String PROTOCOL_VERSION = "1";
    private static final ResourceLocation PROTOCOL_CHANNEL = new ResourceLocation(Mod.MODID, Mod.MODID);
    private static final Map<Class<?>, MessageEntry<?>> MESSAGES = new HashMap<>();
    private static final Queue<PendingSpawnData> PENDING_SPAWN_DATA = new ConcurrentLinkedQueue<>();

    private record MessageEntry<T>(ResourceLocation id, BiConsumer<T, FriendlyByteBuf> encoder) {
    }

    private record PendingSpawnData(Entity entity, ServerPlayer player, EntitySpawnDataMessage message) {
    }

    public static void register() {
        registerProtocolHandshake();

        // ========== Client-bound (S2C) ==========

        registerS2C(PlayerVariablesSyncMessage.class, PlayerVariablesSyncMessage::buffer,
                PlayerVariablesSyncMessage::new, PlayerVariablesSyncMessage::handler);
        registerS2C(ShakeClientMessage.class, ShakeClientMessage::encode,
                ShakeClientMessage::decode, ShakeClientMessage::handler);
        registerS2C(ClientMotionSyncMessage.class, ClientMotionSyncMessage::encode,
                ClientMotionSyncMessage::decode, ClientMotionSyncMessage::handler);
        registerS2C(ClientIndicatorMessage.class, ClientIndicatorMessage::encode,
                ClientIndicatorMessage::decode, ClientIndicatorMessage::handler);
        registerS2C(LivingGunKillMessage.class, LivingGunKillMessage::encode,
                LivingGunKillMessage::decode, LivingGunKillMessage::handler);
        registerS2C(GunsDataMessage.class, GunsDataMessage::encode,
                GunsDataMessage::decode, GunsDataMessage::handler);
        registerS2C(ContainerDataMessage.class, ContainerDataMessage::encode,
                ContainerDataMessage::decode, ContainerDataMessage::handler);
        registerS2C(ShootClientMessage.class, ShootClientMessage::encode,
                ShootClientMessage::decode, msg -> ShootClientMessage.handler());
        registerS2C(DrawClientMessage.INSTANCE, DrawClientMessage::handler);
        registerS2C(ResetCameraTypeMessage.INSTANCE, ResetCameraTypeMessage::handler);
        registerS2C(RadarMenuOpenMessage.class, RadarMenuOpenMessage::encode,
                RadarMenuOpenMessage::decode, RadarMenuOpenMessage::handler);
        registerS2C(RadarMenuCloseMessage.INSTANCE, RadarMenuCloseMessage::handler);
        registerS2C(ClientTacticalSprintSyncMessage.class, ClientTacticalSprintSyncMessage::encode,
                ClientTacticalSprintSyncMessage::decode, ClientTacticalSprintSyncMessage::handler);
        registerS2C(VehiclesDataMessage.class, VehiclesDataMessage::encode,
                VehiclesDataMessage::decode, VehiclesDataMessage::handler);
        registerS2C(ClientSetMotionMessage.class, ClientSetMotionMessage::encode,
                ClientSetMotionMessage::decode, ClientSetMotionMessage::handler);
        registerS2C(FinishAssemblingVehicleMessage.class, FinishAssemblingVehicleMessage::encode,
                FinishAssemblingVehicleMessage::decode, FinishAssemblingVehicleMessage::handler);
        registerS2C(TDMSyncMessage.class, TDMSyncMessage::encode,
                TDMSyncMessage::decode, TDMSyncMessage::handler);
        registerS2C(SoundClientMessage.class, SoundClientMessage::encode,
                SoundClientMessage::decode, SoundClientMessage::handler);
        registerS2C(EntitySpawnDataMessage.class, EntitySpawnDataMessage::encode,
                EntitySpawnDataMessage::decode, EntitySpawnDataMessage::handler);
        registerS2C(ModVersionMismatchMessage.class, ModVersionMismatchMessage::encode,
                ModVersionMismatchMessage::decode, ModVersionMismatchMessage::handler);

        // ========== Server-bound (C2S) ==========

        registerC2S(LaserShootMessage.class, LaserShootMessage::encode,
                LaserShootMessage::decode, LaserShootMessage::handler);
        registerC2S(ShootMessage.class, ShootMessage::encode,
                ShootMessage::decode, ShootMessage::handler);
        registerC2S(SeekingWeaponWarningMessage.class, SeekingWeaponWarningMessage::encode,
                SeekingWeaponWarningMessage::decode, SeekingWeaponWarningMessage::handler);
        registerC2S(DoubleJumpMessage.INSTANCE, DoubleJumpMessage::handler);
        registerC2S(ParachuteMessage.INSTANCE, ParachuteMessage::handler);
        registerC2S(VehicleMovementMessage.class, VehicleMovementMessage::encode,
                VehicleMovementMessage::decode, VehicleMovementMessage::handler);
        registerC2S(MeleeAttackMessage.class, MeleeAttackMessage::encode,
                MeleeAttackMessage::decode, MeleeAttackMessage::handler);
        registerC2S(LungeMineAttackMessage.class, LungeMineAttackMessage::encode,
                LungeMineAttackMessage::decode, LungeMineAttackMessage::handler);
        registerC2S(VehicleFireMessage.class, VehicleFireMessage::encode,
                VehicleFireMessage::decode, VehicleFireMessage::handler);
        registerC2S(AimVillagerMessage.class, AimVillagerMessage::encode,
                AimVillagerMessage::decode, AimVillagerMessage::handler);
        registerC2S(RadarChangeModeMessage.class, RadarChangeModeMessage::encode,
                RadarChangeModeMessage::decode, RadarChangeModeMessage::handler);
        registerC2S(RadarSetParametersMessage.class, RadarSetParametersMessage::encode,
                RadarSetParametersMessage::decode, RadarSetParametersMessage::handler);
        registerC2S(RadarSetPosMessage.class, RadarSetPosMessage::encode,
                RadarSetPosMessage::decode, RadarSetPosMessage::handler);
        registerC2S(RadarSetTargetMessage.class, RadarSetTargetMessage::encode,
                RadarSetTargetMessage::decode, RadarSetTargetMessage::handler);
        registerC2S(GunReforgeMessage.INSTANCE, GunReforgeMessage::handler);
        registerC2S(SetPerkLevelMessage.class, SetPerkLevelMessage::encode,
                SetPerkLevelMessage::decode, SetPerkLevelMessage::handler);
        registerC2S(SwitchVehicleWeaponMessage.class, SwitchVehicleWeaponMessage::encode,
                SwitchVehicleWeaponMessage::decode, SwitchVehicleWeaponMessage::handler);
        registerC2S(AdjustZoomFovMessage.class, AdjustZoomFovMessage::encode,
                AdjustZoomFovMessage::decode, AdjustZoomFovMessage::handler);
        registerC2S(SwitchScopeMessage.class, SwitchScopeMessage::encode,
                SwitchScopeMessage::decode, SwitchScopeMessage::handler);
        registerC2S(FireKeyMessage.class, FireKeyMessage::encode,
                FireKeyMessage::decode, FireKeyMessage::handler);
        registerC2S(ReloadMessage.INSTANCE, ReloadMessage::handler);
        registerC2S(FireModeMessage.class, FireModeMessage::encode,
                FireModeMessage::decode, FireModeMessage::handler);
        registerC2S(PlayerStopRidingMessage.class, PlayerStopRidingMessage::encode,
                PlayerStopRidingMessage::decode, PlayerStopRidingMessage::handler);
        registerC2S(ZoomMessage.class, ZoomMessage::encode,
                ZoomMessage::decode, ZoomMessage::handler);
        registerC2S(DroneFireMessage.class, DroneFireMessage::encode,
                DroneFireMessage::decode, DroneFireMessage::handler);
        registerC2S(SetFiringParametersMessage.INSTANCE, SetFiringParametersMessage::handler);
        registerC2S(ArtilleryIndicatorFireMessage.INSTANCE, ArtilleryIndicatorFireMessage::handler);
        registerC2S(SensitivityMessage.class, SensitivityMessage::encode,
                SensitivityMessage::decode, SensitivityMessage::handler);
        registerC2S(EditMessage.class, EditMessage::encode,
                EditMessage::decode, EditMessage::handler);
        registerC2S(InteractMessage.INSTANCE, InteractMessage::handler);
        registerC2S(AdjustMortarAngleMessage.class, AdjustMortarAngleMessage::encode,
                AdjustMortarAngleMessage::decode, AdjustMortarAngleMessage::handler);
        registerC2S(ChangeVehicleSeatMessage.class, ChangeVehicleSeatMessage::encode,
                ChangeVehicleSeatMessage::decode, ChangeVehicleSeatMessage::handler);
        registerC2S(ShowChargingRangeMessage.class, ShowChargingRangeMessage::encode,
                ShowChargingRangeMessage::decode, ShowChargingRangeMessage::handler);
        registerC2S(TacticalSprintMessage.class, TacticalSprintMessage::encode,
                TacticalSprintMessage::decode, TacticalSprintMessage::handler);
        registerC2S(DogTagFinishEditMessage.class, DogTagFinishEditMessage::encode,
                DogTagFinishEditMessage::decode, DogTagFinishEditMessage::handler);
        registerC2S(MouseMoveMessage.class, MouseMoveMessage::encode,
                MouseMoveMessage::decode, MouseMoveMessage::handler);
        registerC2S(FiringParametersEditMessage.class, FiringParametersEditMessage::encode,
                FiringParametersEditMessage::decode, FiringParametersEditMessage::handler);
        registerC2S(UnloadMessage.INSTANCE, UnloadMessage::handler);
        registerC2S(AssembleVehicleMessage.class, AssembleVehicleMessage::encode,
                AssembleVehicleMessage::decode, AssembleVehicleMessage::handler);
        registerC2S(WeaponZoomingMessage.class, WeaponZoomingMessage::encode,
                WeaponZoomingMessage::decode, WeaponZoomingMessage::handler);

        registerSpawnDataSync();
    }

    private static void registerProtocolHandshake() {
        boolean receiverRegistered = ServerLoginNetworking.registerGlobalReceiver(PROTOCOL_CHANNEL,
                (server, handler, understood, buffer, synchronizer, responseSender) -> {
                    if (!understood) {
                        handler.disconnect(Component.literal(
                                "This server requires Superb Warfare (Fabric) with network protocol "
                                        + PROTOCOL_VERSION + ". Install the matching mod version."));
                        return;
                    }

                    final String clientProtocol;
                    try {
                        clientProtocol = buffer.readUtf(32);
                    } catch (RuntimeException exception) {
                        handler.disconnect(Component.literal(
                                "Invalid Superb Warfare login response. Install the matching mod version "
                                        + "(network protocol " + PROTOCOL_VERSION + ")."));
                        return;
                    }

                    if (!PROTOCOL_VERSION.equals(clientProtocol)) {
                        handler.disconnect(Component.literal(
                                "Incompatible Superb Warfare network protocol (server: "
                                        + PROTOCOL_VERSION + ", client: " + clientProtocol
                                        + "). Install the matching mod version."));
                    }
                });

        if (!receiverRegistered) {
            throw new IllegalStateException("Duplicate Superb Warfare login protocol receiver");
        }

        ServerLoginConnectionEvents.QUERY_START.register((handler, server, sender, synchronizer) -> {
            FriendlyByteBuf buffer = PacketByteBufs.create();
            buffer.writeUtf(PROTOCOL_VERSION);
            sender.sendPacket(PROTOCOL_CHANNEL, buffer);
        });

        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            ClientNetworkRegistry.registerLoginHandshake(PROTOCOL_CHANNEL, PROTOCOL_VERSION);
        }
    }

    private static void registerSpawnDataSync() {
        EntityTrackingEvents.START_TRACKING.register((entity, player) -> {
            if (entity instanceof CustomSpawnDataEntity) {
                PENDING_SPAWN_DATA.add(new PendingSpawnData(
                        entity, player, new EntitySpawnDataMessage(entity)));
            }
        });

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            PendingSpawnData pending;
            while ((pending = PENDING_SPAWN_DATA.poll()) != null) {
                if (!pending.entity().isRemoved()
                        && PlayerLookup.tracking(pending.entity()).contains(pending.player())) {
                    sendToPlayer(pending.player(), pending.message());
                }
            }
        });

        ServerLifecycleEvents.SERVER_STOPPED.register(server -> PENDING_SPAWN_DATA.clear());
    }

    // ========== S2C (Client-bound) Registration ==========

    private static <T> void registerS2C(Class<T> type, BiConsumer<T, FriendlyByteBuf> encoder,
                                         Function<FriendlyByteBuf, T> decoder, Consumer<T> handler) {
        var id = new ResourceLocation(Mod.MODID, "s2c_" + type.getSimpleName().toLowerCase(Locale.ROOT));
        MESSAGES.put(type, new MessageEntry<>(id, encoder));
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            ClientNetworkRegistry.registerS2C(id, decoder, handler);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> void registerS2C(T instance, Runnable handler) {
        var type = (Class<T>) instance.getClass();
        var id = new ResourceLocation(Mod.MODID, "s2c_" + type.getSimpleName().toLowerCase(Locale.ROOT));
        MESSAGES.put(type, new MessageEntry<>(id, (msg, buf) -> {}));
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            ClientNetworkRegistry.registerS2C(id, handler);
        }
    }

    // ========== C2S (Server-bound) Registration ==========

    private static <T> void registerC2S(Class<T> type, BiConsumer<T, FriendlyByteBuf> encoder,
                                         Function<FriendlyByteBuf, T> decoder, BiConsumer<T, ServerPlayer> handler) {
        var id = new ResourceLocation(Mod.MODID, "c2s_" + type.getSimpleName().toLowerCase(Locale.ROOT));
        MESSAGES.put(type, new MessageEntry<>(id, encoder));
        ServerPlayNetworking.registerGlobalReceiver(id, (server, player, handlerNet, buf, responseSender) -> {
            T message = decoder.apply(buf);
            server.execute(() -> handler.accept(message, player));
        });
    }

    @SuppressWarnings("unchecked")
    private static <T> void registerC2S(T instance, Consumer<ServerPlayer> handler) {
        var type = (Class<T>) instance.getClass();
        var id = new ResourceLocation(Mod.MODID, "c2s_" + type.getSimpleName().toLowerCase(Locale.ROOT));
        MESSAGES.put(type, new MessageEntry<>(id, (msg, buf) -> {}));
        ServerPlayNetworking.registerGlobalReceiver(id, (server, player, handlerNet, buf, responseSender) -> {
            server.execute(() -> handler.accept(player));
        });
    }

    // ========== Send Helpers ==========

    @SuppressWarnings("unchecked")
    public static <T> void sendToServer(T message) {
        var entry = (MessageEntry<T>) MESSAGES.get(message.getClass());
        if (entry == null) throw new IllegalStateException("Unregistered message: " + message.getClass().getName());
        var buf = PacketByteBufs.create();
        entry.encoder.accept(message, buf);
        if (FabricLoader.getInstance().getEnvironmentType() != EnvType.CLIENT) {
            throw new IllegalStateException("Cannot send C2S packet from a dedicated server");
        }
        ClientNetworkRegistry.sendToServer(entry.id, buf);
    }

    @SuppressWarnings("unchecked")
    public static <T> void sendToPlayer(ServerPlayer player, T message) {
        var entry = (MessageEntry<T>) MESSAGES.get(message.getClass());
        if (entry == null) throw new IllegalStateException("Unregistered message: " + message.getClass().getName());
        var buf = PacketByteBufs.create();
        entry.encoder.accept(message, buf);
        ServerPlayNetworking.send(player, entry.id, buf);
    }

    public static <T> void sendToAll(T message) {
        var server = Mod.getServer();
        if (server != null) {
            for (var player : server.getPlayerList().getPlayers()) {
                sendToPlayer(player, message);
            }
        }
    }

    public static <T> void sendToTracking(Entity entity, T message) {
        for (var player : PlayerLookup.tracking(entity)) {
            sendToPlayer(player, message);
        }
    }

    public static <T> void sendToPlayers(List<ServerPlayer> players, T message) {
        for (var player : players) {
            sendToPlayer(player, message);
        }
    }
}
