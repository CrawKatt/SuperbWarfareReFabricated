package com.atsuishio.superbwarfare.network;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientLoginConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientLoginNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

@Environment(EnvType.CLIENT)
public class ClientNetworkRegistry {

    private static volatile String advertisedServerProtocol;

    private ClientNetworkRegistry() {
    }

    static void registerLoginHandshake(ResourceLocation id, String protocolVersion) {
        ClientLoginConnectionEvents.INIT.register((handler, client) -> advertisedServerProtocol = null);

        boolean registered = ClientLoginNetworking.registerGlobalReceiver(id,
                (client, handler, request, listenerAdder) -> {
                    try {
                        advertisedServerProtocol = request.readUtf(32);
                    } catch (RuntimeException exception) {
                        advertisedServerProtocol = "<invalid>";
                    }

                    FriendlyByteBuf response = PacketByteBufs.create();
                    response.writeUtf(protocolVersion);
                    return CompletableFuture.completedFuture(response);
                });

        if (!registered) {
            throw new IllegalStateException("Duplicate Superb Warfare client login protocol receiver");
        }

        ClientPlayConnectionEvents.INIT.register((handler, client) -> {
            String serverProtocol = advertisedServerProtocol;
            if (protocolVersion.equals(serverProtocol)) {
                return;
            }

            Component reason;
            if (serverProtocol == null) {
                reason = Component.literal(
                        "This client requires Superb Warfare on the server with network protocol "
                                + protocolVersion + ". Install the matching server mod version.");
            } else {
                reason = Component.literal(
                        "Incompatible Superb Warfare server network protocol (client: "
                                + protocolVersion + ", server: " + serverProtocol
                                + "). Install the matching server mod version.");
            }
            handler.getConnection().disconnect(reason);
        });
    }

    static <T> void registerS2C(ResourceLocation id, Function<FriendlyByteBuf, T> decoder, Consumer<T> handler) {
        ClientPlayNetworking.registerGlobalReceiver(id, (client, handlerNet, buf, responseSender) -> {
            T message = decoder.apply(buf);
            client.execute(() -> handler.accept(message));
        });
    }

    static <T> void registerS2C(ResourceLocation id, Function<FriendlyByteBuf, T> decoder,
                                BiConsumer<T, PayloadContext> handler) {
        ClientPlayNetworking.registerGlobalReceiver(id, (client, handlerNet, buf, responseSender) -> {
            T message = decoder.apply(buf);
            client.execute(() -> {
                if (client.player != null) {
                    handler.accept(message, new PayloadContext(client.player));
                }
            });
        });
    }

    static void registerS2C(ResourceLocation id, Runnable handler) {
        ClientPlayNetworking.registerGlobalReceiver(id, (client, handlerNet, buf, responseSender) -> client.execute(handler));
    }

    static void sendToServer(ResourceLocation id, FriendlyByteBuf buf) {
        ClientPlayNetworking.send(id, buf);
    }
}
