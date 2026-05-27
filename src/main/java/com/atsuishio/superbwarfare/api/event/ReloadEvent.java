package com.atsuishio.superbwarfare.api.event;

import com.atsuishio.superbwarfare.data.gun.GunData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

@ApiStatus.Internal
@ApiStatus.AvailableSince("0.8.0")
public class ReloadEvent {
    private final Entity shooter;
    private final GunData data;
    private final ItemStack stack;

    public ReloadEvent(@Nullable Entity shooter, GunData data) {
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

    public static class Pre extends ReloadEvent {
        public Pre(@Nullable Entity shooter, GunData data) {
            super(shooter, data);
        }
    }

    public static class Post extends ReloadEvent {
        public Post(@Nullable Entity shooter, GunData data) {
            super(shooter, data);
        }
    }
}