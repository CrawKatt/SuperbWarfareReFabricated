package com.atsuishio.superbwarfare.capability.player;

import com.atsuishio.superbwarfare.Mod;
import com.atsuishio.superbwarfare.capability.ModCapabilities;
import com.atsuishio.superbwarfare.data.gun.Ammo;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class PlayerVariable implements AutoSyncedComponent {

    public static ResourceLocation ID = Mod.loc("player_variables");
    private PlayerVariable old = null;

    public Map<Ammo, Integer> ammo = new HashMap<>();
    public boolean tacticalSprint = false;

    public static PlayerVariable getOrDefault(Entity entity) {
        return ModCapabilities.PLAYER_VARIABLE.maybeGet(entity).orElseGet(PlayerVariable::new);
    }

    /**
     * Mutates a player's persistent variables on the logical server and synchronizes
     * the component if its contents actually changed.
     */
    public static void modify(Entity entity, Consumer<PlayerVariable> consumer) {
        if (entity.level().isClientSide) return;

        ModCapabilities.PLAYER_VARIABLE.maybeGet(entity).ifPresent(variable -> {
            var old = variable.copy();
            consumer.accept(variable);
            if (!old.equals(variable)) {
                ModCapabilities.PLAYER_VARIABLE.sync(entity);
            }
        });
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

    @Override
    public void writeToNbt(CompoundTag tag) {
        for (var type : Ammo.values()) {
            type.set(tag, type.get(this));
        }

        tag.putBoolean("TacticalSprint", this.tacticalSprint);
    }

    @Override
    public void readFromNbt(CompoundTag tag) {
        for (var type : Ammo.values()) {
            type.set(this, type.get(tag));
        }

        this.tacticalSprint = tag.getBoolean("TacticalSprint");
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
