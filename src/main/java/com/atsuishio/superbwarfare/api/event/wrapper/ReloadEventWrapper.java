package com.atsuishio.superbwarfare.api.event.wrapper;

import com.atsuishio.superbwarfare.data.gun.GunData;
import io.github.lounode.eventwrapper.eventbus.api.EventWrapper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class ReloadEventWrapper extends EventWrapper {

    private final Entity shooter;
    private final GunData data;
    private final ItemStack stack;

    public ReloadEventWrapper(@Nullable Entity shooter, GunData data) {
        this.shooter = shooter;
        this.data = data;
        this.stack = data.stack;
    }

    @Nullable
    public Entity getEntity() {
        return shooter;
    }

    public GunData getData() {
        return data;
    }

    public ItemStack getStack() {
        return stack;
    }

    public static class Pre extends ReloadEventWrapper {
        public Pre(@Nullable Entity shooter, GunData data) {
            super(shooter, data);
        }
    }

    public static class Post extends ReloadEventWrapper {
        public Post(@Nullable Entity shooter, GunData data) {
            super(shooter, data);
        }
    }
}
