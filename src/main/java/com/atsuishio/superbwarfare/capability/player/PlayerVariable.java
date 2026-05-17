package com.atsuishio.superbwarfare.capability.player;

import com.atsuishio.superbwarfare.Mod;
import com.atsuishio.superbwarfare.data.gun.Ammo;
import com.atsuishio.superbwarfare.init.ModAttachments;
import com.atsuishio.superbwarfare.network.message.receive.PlayerVariablesSyncMessage;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class PlayerVariable {
    private PlayerVariable old = null;

    public Map<Ammo, Integer> ammo = new EnumMap<>(Ammo.class);
    public boolean tacticalSprint = false;

    public void sync(Entity entity) {
        if (!entity.hasAttached(ModAttachments.PLAYER_VARIABLE)) return;

        var newVariable = entity.getAttached(ModAttachments.PLAYER_VARIABLE);
        if (old != null && old.equals(newVariable)) return;

        if (entity instanceof ServerPlayer serverPlayer) {
            ServerPlayNetworking.send(serverPlayer, new PlayerVariablesSyncMessage(entity.getId(), compareAndUpdate()));
        }
    }


    public static PlayerVariable getOrDefault(Entity entity) {
        return entity.getAttached(ModAttachments.PLAYER_VARIABLE);
    }

    public static void onPlayerLogin(ServerPlayer player) {
        ServerPlayNetworking.send(player, new PlayerVariablesSyncMessage(player.getId(), getOrDefault(player).compareAndUpdate()));
    }

    public static void onPlayerRespawn(ServerPlayer player) {
        ServerPlayNetworking.send(player, new PlayerVariablesSyncMessage(player.getId(), getOrDefault(player).compareAndUpdate()));
    }

    public static void onPlayerChangeDimension(ServerPlayer player) {
        ServerPlayNetworking.send(player, new PlayerVariablesSyncMessage(player.getId(), getOrDefault(player).forceUpdate()));
    }

    public PlayerVariable watch() {
        this.old = this.copy();
        return this;
    }

    public Map<Byte, Integer> forceUpdate() {
        var map = new HashMap<Byte, Integer>();

        for (var type : Ammo.values()) {
            map.put((byte) type.ordinal(), type.get(this));
        }

        map.put((byte) -1, this.tacticalSprint ? 1 : 0);

        return map;
    }

    public Map<Byte, Integer> compareAndUpdate() {
        var map = new HashMap<Byte, Integer>();
        var old = this.old == null ? new PlayerVariable() : this.old;

        for (var type : Ammo.values()) {
            var oldCount = old.ammo.getOrDefault(type, 0);
            var newCount = type.get(this);

            if (oldCount != newCount) {
                map.put((byte) type.ordinal(), newCount);
            }
        }

        if (old.tacticalSprint != this.tacticalSprint) {
            map.put((byte) -1, this.tacticalSprint ? 1 : 0);
        }

        return map;
    }


    public CompoundTag writeToNBT() {
        CompoundTag nbt = new CompoundTag();

        for (var type : Ammo.values()) {
            type.set(nbt, type.get(this));
        }

        nbt.putBoolean("TacticalSprint", tacticalSprint);

        return nbt;
    }

    public void readFromNBT(CompoundTag tag) {
        for (var type : Ammo.values()) {
            type.set(this, type.get(tag));
        }

        tacticalSprint = tag.getBoolean("TacticalSprint");

    }

    public PlayerVariable copy() {
        var clone = new PlayerVariable();

        for (var type : Ammo.values()) {
            type.set(clone, type.get(this));
        }

        clone.tacticalSprint = this.tacticalSprint;

        return clone;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PlayerVariable other)) return false;

        for (var type : Ammo.values()) {
            if (type.get(this) != type.get(other)) return false;
        }

        return tacticalSprint == other.tacticalSprint;
    }

    public static void onPlayerClone(ServerPlayer original, ServerPlayer player, boolean alive) {
        var originalVar = original.getAttached(ModAttachments.PLAYER_VARIABLE);
        if (originalVar != null) {
            player.setAttached(ModAttachments.PLAYER_VARIABLE, originalVar.copy());
        }
    }
}
