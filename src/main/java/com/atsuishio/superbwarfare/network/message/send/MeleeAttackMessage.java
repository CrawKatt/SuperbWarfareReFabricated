package com.atsuishio.superbwarfare.network.message.send;

import com.atsuishio.superbwarfare.init.ModSounds;
import com.atsuishio.superbwarfare.tools.EntityFindUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;

import java.util.UUID;

public class MeleeAttackMessage {

    private final UUID uuid;

    public MeleeAttackMessage(UUID uuid) {
        this.uuid = uuid;
    }

    public static MeleeAttackMessage decode(FriendlyByteBuf buffer) {
        return new MeleeAttackMessage(buffer.readUUID());
    }

    public static void encode(MeleeAttackMessage message, FriendlyByteBuf buffer) {
        buffer.writeUUID(message.uuid);
    }

    public static void handler(MeleeAttackMessage message, ServerPlayer player) {
        if (player != null) {
            Entity lookingEntity = EntityFindUtil.findEntity(player.level(), String.valueOf(message.uuid));
            if (lookingEntity != null) {
                player.level().playSound(null, lookingEntity.getOnPos(), ModSounds.MELEE_HIT.get(), SoundSource.PLAYERS, 1, (float) ((2 * org.joml.Math.random() - 1) * 0.1f + 1.0f));
                player.attack(lookingEntity);
            }
        }
    }
}
