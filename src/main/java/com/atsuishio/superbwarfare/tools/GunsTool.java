package com.atsuishio.superbwarfare.tools;

import com.atsuishio.superbwarfare.Mod;
import com.atsuishio.superbwarfare.network.NetworkRegistry;
import com.atsuishio.superbwarfare.network.message.receive.GunsDataMessage;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.UUID;

public class GunsTool {

    // TODO: Register in Mod.java using Fabric event API
    public static void onPlayerLogin(ServerPlayer player) {
        var server = player.getServer();
        if (server != null && server.isSingleplayerOwner(player.getGameProfile())) {
            return;
        }

        NetworkRegistry.sendToPlayer(player, GunsDataMessage.create());
    }

    // TODO: Register in Mod.java using Fabric event API
    public static void onDataPackSync(net.minecraft.server.players.PlayerList players) {
        var server = players.getServer();

        var message = GunsDataMessage.create();
        for (var player : players.getPlayers()) {
            if (server.isSingleplayerOwner(player.getGameProfile())) {
                continue;
            }

            NetworkRegistry.sendToPlayer(player, message);
        }
    }

    public static void setGunIntTag(ItemStack stack, String name, int num) {
        CompoundTag tag = stack.getOrCreateTag();
        var data = tag.getCompound("GunData");
        data.putInt(name, num);
        stack.addTagElement("GunData", data);
    }

    public static int getGunIntTag(final CompoundTag tag, String name) {
        return getGunIntTag(tag, name, 0);
    }

    public static int getGunIntTag(final CompoundTag tag, String name, int defaultValue) {
        var data = tag.getCompound("GunData");
        if (!data.contains(name)) return defaultValue;
        return data.getInt(name);
    }

    public static double getGunDoubleTag(ItemStack stack, String name) {
        return getGunDoubleTag(stack, name, 0);
    }

    public static double getGunDoubleTag(ItemStack stack, String name, double defaultValue) {
        var data = stack.getOrCreateTag().getCompound("GunData");
        if (!data.contains(name)) return defaultValue;
        return data.getDouble(name);
    }

    @Nullable
    public static UUID getGunUUID(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag == null || !tag.contains("GunData")) return null;

        CompoundTag data = tag.getCompound("GunData");
        if (!data.hasUUID("UUID")) return null;
        return data.getUUID("UUID");
    }
}
