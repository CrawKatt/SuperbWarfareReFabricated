package com.atsuishio.superbwarfare.event.custom;

import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public interface LeftClickBlockCallback {
    net.fabricmc.fabric.api.event.Event<LeftClickBlockCallback> EVENT = EventFactory.createArrayBacked(
            LeftClickBlockCallback.class,
            callbacks -> event -> {
                for (LeftClickBlockCallback callback : callbacks) {
                    callback.onLeftClick(event);
                    if (event.isCanceled()) return;
                }
            }
    );

    void onLeftClick(Event event);

    class Event {
        private final Player entity;
        private final BlockPos pos;
        private final Direction face;
        private final ServerboundPlayerActionPacket.Action action;
        private boolean canceled;

        public Event(Player entity, BlockPos pos, Direction face, ServerboundPlayerActionPacket.Action action) {
            this.entity = entity;
            this.pos = pos;
            this.face = face;
            this.action = action;
        }

        public Player getEntity() {
            return entity;
        }

        public Level getLevel() {
            return entity.level();
        }

        public BlockPos getPos() {
            return pos;
        }

        public Direction getFace() {
            return face;
        }

        public ServerboundPlayerActionPacket.Action getAction() {
            return action;
        }

        public boolean isCanceled() {
            return canceled;
        }

        public void setCanceled(boolean canceled) {
            this.canceled = canceled;
        }
    }
}
