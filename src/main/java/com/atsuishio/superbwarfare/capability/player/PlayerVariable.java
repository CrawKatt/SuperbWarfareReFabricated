package com.atsuishio.superbwarfare.capability.player;

import com.atsuishio.superbwarfare.Mod;
import com.atsuishio.superbwarfare.data.gun.Ammo;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class PlayerVariable {

    public static ResourceLocation ID = Mod.loc("player_variables");
    private PlayerVariable old = null;

    public Map<Ammo, Integer> ammo = new HashMap<>();
    public boolean tacticalSprint = false;

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

    public CompoundTag serializeNBT() {
        return writeToNBT();
    }

    public void deserializeNBT(CompoundTag nbt) {
        readFromNBT(nbt);
    }
}
