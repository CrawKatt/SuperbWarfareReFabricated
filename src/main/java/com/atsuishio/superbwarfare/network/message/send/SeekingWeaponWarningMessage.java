package com.atsuishio.superbwarfare.network.message.send;

import com.atsuishio.superbwarfare.init.ModSounds;
import com.atsuishio.superbwarfare.tools.EntityFindUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Pig;

import java.util.UUID;

public class SeekingWeaponWarningMessage {
    private final boolean lockOn;
    private final UUID uuid;

    public SeekingWeaponWarningMessage(boolean lockOn, UUID uuid) {
        this.lockOn = lockOn;
        this.uuid = uuid;
    }

    public static SeekingWeaponWarningMessage decode(FriendlyByteBuf buffer) {
        return new SeekingWeaponWarningMessage(buffer.readBoolean(), buffer.readUUID());
    }

    public static void encode(SeekingWeaponWarningMessage message, FriendlyByteBuf buffer) {
        buffer.writeBoolean(message.lockOn);
        buffer.writeUUID(message.uuid);
    }

    public static void handler(SeekingWeaponWarningMessage message, ServerPlayer player) {
        if (player != null) {
            Entity entity = EntityFindUtil.findEntity(player.level(), String.valueOf(message.uuid));

            if (entity != null) {
                entity.level().playSound(null, entity.getOnPos(), entity instanceof Pig ? SoundEvents.PIG_HURT : message.lockOn ? ModSounds.LOCKED_WARNING.get() : ModSounds.LOCKING_WARNING.get(), SoundSource.PLAYERS, 2, 1f);
            }
        }
    }
}
