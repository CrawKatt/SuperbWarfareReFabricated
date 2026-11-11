package com.atsuishio.superbwarfare.event.custom;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public interface BlockCaughtFireCallback {
    Event<BlockCaughtFireCallback> EVENT = EventFactory.createArrayBacked(
            BlockCaughtFireCallback.class,
            callbacks -> (state, level, pos, face, igniter) -> {
                for (BlockCaughtFireCallback callback : callbacks) {
                    if (callback.onCaughtFire(state, level, pos, face, igniter)) {
                        return true;
                    }
                }
                return false;
            }
    );

    boolean onCaughtFire(
            BlockState state,
            Level level,
            BlockPos pos,
            @Nullable Direction face,
            @Nullable LivingEntity igniter
    );
}
