package com.atsuishio.superbwarfare.network;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;
import java.util.function.Function;

@Environment(EnvType.CLIENT)
public class ClientNetworkRegistry {

    private ClientNetworkRegistry() {
    }

    static <T> void registerS2C(ResourceLocation id, Function<FriendlyByteBuf, T> decoder, Consumer<T> handler) {
        ClientPlayNetworking.registerGlobalReceiver(id, (client, handlerNet, buf, responseSender) -> {
            T message = decoder.apply(buf);
            client.execute(() -> handler.accept(message));
        });
    }

    static void registerS2C(ResourceLocation id, Runnable handler) {
        ClientPlayNetworking.registerGlobalReceiver(id, (client, handlerNet, buf, responseSender) -> client.execute(handler));
    }

    static void sendToServer(ResourceLocation id, FriendlyByteBuf buf) {
        ClientPlayNetworking.send(id, buf);
    }
}
