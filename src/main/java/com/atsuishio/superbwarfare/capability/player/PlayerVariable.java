package com.atsuishio.superbwarfare.capability.player;

import com.atsuishio.superbwarfare.data.gun.Ammo;
import com.atsuishio.superbwarfare.init.ModComponents;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import org.ladysnake.cca.api.v3.component.Component;
import org.ladysnake.cca.api.v3.component.CopyableComponent;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

import java.util.EnumMap;
import java.util.Map;

public class PlayerVariable implements Component, AutoSyncedComponent, CopyableComponent<PlayerVariable> {

    public Map<Ammo, Integer> ammo = new EnumMap<>(Ammo.class);
    public boolean tacticalSprint = false;

    @Override
    public void readFromNbt(CompoundTag tag, HolderLookup.Provider registryLookup) {
        for (var type : Ammo.values()) {
            type.set(this, type.get(tag));
        }
        tacticalSprint = tag.getBoolean("TacticalSprint");
    }

    @Override
    public void writeToNbt(CompoundTag tag, HolderLookup.Provider registryLookup) {
        for (var type : Ammo.values()) {
            type.set(tag, type.get(this));
        }
        tag.putBoolean("TacticalSprint", tacticalSprint);
    }

    @Override
    public void copyFrom(PlayerVariable original, HolderLookup.Provider registryLookup) {
        for (var type : Ammo.values()) {
            type.set(this, type.get(original));
        }
        tacticalSprint = original.tacticalSprint;
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
}
