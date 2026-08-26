package com.atsuishio.superbwarfare.network;

import com.atsuishio.superbwarfare.Mod;
import com.atsuishio.superbwarfare.event.custom.EntityPairingCallback;
import com.atsuishio.superbwarfare.network.message.receive.EntitySpawnDataMessage;
import com.atsuishio.superbwarfare.network.message.receive.ModVersionMismatchMessage;
import net.fabricmc.api.EnvType;
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
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class NetworkRegistry {

    private static final String PROTOCOL_VERSION = "2";
    private static final ResourceLocation PROTOCOL_CHANNEL = new ResourceLocation(Mod.MODID, Mod.MODID);
    private static final Map<Class<?>, MessageEntry<?>> MESSAGES = new HashMap<>();

    private record MessageEntry<T>(ResourceLocation id, BiConsumer<T, FriendlyByteBuf> encoder) {
    }

    private NetworkRegistry() {
    }

    public static void register() {
        registerProtocolHandshake();

        // Kotlin-serialization payloads from the 0.8.9 NeoForge protocol.
        NetworkRegistryKt.initializeNetwork();

        // Fabric-only compatibility payloads retained from the 0.8.8 port.
        registerS2C(EntitySpawnDataMessage.class, EntitySpawnDataMessage::encode,
                EntitySpawnDataMessage::decode, EntitySpawnDataMessage::handler);
        registerS2C(ModVersionMismatchMessage.class, ModVersionMismatchMessage::encode,
                ModVersionMismatchMessage::decode, ModVersionMismatchMessage::handler);

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
        EntityPairingCallback.EVENT.register((entity, player) -> {
            if (entity instanceof CustomSpawnDataEntity) {
                sendToPlayer(player, new EntitySpawnDataMessage(entity));
            }
        });
    }

    /** Registers a Kotlin-serialization payload sent from the server to the client. */
    public static <T> void playToClient(Class<T> type, BiConsumer<T, FriendlyByteBuf> encoder,
                                        Function<FriendlyByteBuf, T> decoder,
                                        BiConsumer<T, PayloadContext> handler) {
        ResourceLocation id = messageId("s2c", type);
        putMessage(type, id, encoder);
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            ClientNetworkRegistry.registerS2C(id, decoder, handler);
        }
    }

    /** Registers a Kotlin-serialization payload sent from the client to the server. */
    public static <T> void playToServer(Class<T> type, BiConsumer<T, FriendlyByteBuf> encoder,
                                        Function<FriendlyByteBuf, T> decoder,
                                        BiConsumer<T, PayloadContext> handler) {
        ResourceLocation id = messageId("c2s", type);
        putMessage(type, id, encoder);
        ServerPlayNetworking.registerGlobalReceiver(id, (server, player, handlerNet, buf, responseSender) -> {
            T message = decoder.apply(buf);
            server.execute(() -> handler.accept(message, new PayloadContext(player)));
        });
    }

    private static <T> void registerS2C(Class<T> type, BiConsumer<T, FriendlyByteBuf> encoder,
                                        Function<FriendlyByteBuf, T> decoder, Consumer<T> handler) {
        ResourceLocation id = messageId("s2c", type);
        putMessage(type, id, encoder);
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            ClientNetworkRegistry.registerS2C(id, decoder, handler);
        }
    }

    private static ResourceLocation messageId(String direction, Class<?> type) {
        return new ResourceLocation(Mod.MODID,
                direction + "_" + type.getSimpleName().toLowerCase(Locale.ROOT));
    }

    private static <T> void putMessage(Class<T> type, ResourceLocation id,
                                       BiConsumer<T, FriendlyByteBuf> encoder) {
        MessageEntry<?> previous = MESSAGES.put(type, new MessageEntry<>(id, encoder));
        if (previous != null) {
            throw new IllegalStateException("Duplicate Superb Warfare message type: " + type.getName());
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> void sendToServer(T message) {
        MessageEntry<T> entry = (MessageEntry<T>) MESSAGES.get(message.getClass());
        if (entry == null) {
            throw new IllegalStateException("Unregistered message: " + message.getClass().getName());
        }
        if (FabricLoader.getInstance().getEnvironmentType() != EnvType.CLIENT) {
            throw new IllegalStateException("Cannot send C2S packet from a dedicated server");
        }

        FriendlyByteBuf buf = PacketByteBufs.create();
        entry.encoder().accept(message, buf);
        ClientNetworkRegistry.sendToServer(entry.id(), buf);
    }

    @SuppressWarnings("unchecked")
    public static <T> void sendToPlayer(ServerPlayer player, T message) {
        MessageEntry<T> entry = (MessageEntry<T>) MESSAGES.get(message.getClass());
        if (entry == null) {
            throw new IllegalStateException("Unregistered message: " + message.getClass().getName());
        }

        FriendlyByteBuf buf = PacketByteBufs.create();
        entry.encoder().accept(message, buf);
        ServerPlayNetworking.send(player, entry.id(), buf);
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
