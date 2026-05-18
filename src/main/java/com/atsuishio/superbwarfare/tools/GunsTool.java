package com.atsuishio.superbwarfare.tools;

import com.atsuishio.superbwarfare.Mod;
import com.atsuishio.superbwarfare.network.message.receive.GunsDataMessage;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import javax.annotation.Nullable;
import java.util.UUID;

public class GunsTool {

    public static void onPlayerLogin(ServerPlayer player) {
        var server = player.getServer();
        if (server != null && server.isSingleplayerOwner(player.getGameProfile())) {
            return;
        }

        ServerPlayNetworking.send(player, GunsDataMessage.create());
    }

    public static void onDataPackSync(MinecraftServer server) {
        var message = GunsDataMessage.create();
        for (var player : server.getPlayerList().getPlayers()) {
            if (server.isSingleplayerOwner(player.getGameProfile())) {
                continue;
            }

            ServerPlayNetworking.send(player, message);
        }
    }

    public static void setGunIntTag(final CompoundTag tag, String name, int num) {
        var data = tag.getCompound("GunData");
        data.putInt(name, num);
        tag.put("GunData", data);
    }

    public static int getGunIntTag(final CompoundTag tag, String name) {
        return getGunIntTag(tag, name, 0);
    }

    public static int getGunIntTag(final CompoundTag tag, String name, int defaultValue) {
        var data = tag.getCompound("GunData");
        if (!data.contains(name)) return defaultValue;
        return data.getInt(name);
    }

    public static double getGunDoubleTag(final CompoundTag tag, String name) {
        return getGunDoubleTag(tag, name, 0);
    }

    public static double getGunDoubleTag(final CompoundTag tag, String name, double defaultValue) {
        var data = tag.getCompound("GunData");
        if (!data.contains(name)) return defaultValue;
        return data.getDouble(name);
    }

    @Nullable
    public static UUID getGunUUID(final CompoundTag tag) {
        if (!tag.contains("GunData")) return null;

        CompoundTag data = tag.getCompound("GunData");
        if (!data.hasUUID("UUID")) return null;
        return data.getUUID("UUID");
    }
}
