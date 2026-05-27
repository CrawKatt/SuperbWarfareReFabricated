package com.atsuishio.superbwarfare.network.message.send;

import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity;
import com.atsuishio.superbwarfare.init.ModDamageTypes;
import com.atsuishio.superbwarfare.init.ModItems;
import com.atsuishio.superbwarfare.tools.CustomExplosion;
import com.atsuishio.superbwarfare.tools.DamageHandler;
import com.atsuishio.superbwarfare.tools.EntityFindUtil;
import com.atsuishio.superbwarfare.tools.ParticleTool;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

public record LungeMineAttackMessage(int type, UUID uuid, Vec3 pos) {

    public static LungeMineAttackMessage decode(FriendlyByteBuf buffer) {
        return new LungeMineAttackMessage(buffer.readInt(), buffer.readUUID(), new Vec3(buffer.readVector3f()));
    }

    public static void encode(LungeMineAttackMessage message, FriendlyByteBuf buffer) {
        buffer.writeInt(message.type);
        buffer.writeUUID(message.uuid);
        buffer.writeVector3f(message.pos.toVector3f());
    }

    public static void handler(LungeMineAttackMessage message, ServerPlayer player) {
        if (player == null) return;

        ItemStack stack = player.getMainHandItem();
        if (!stack.is(ModItems.LUNGE_MINE.get())) return;

        if (!player.isCreative()) {
            stack.shrink(1);
        }

        if (message.type == 0) {
            Entity lookingEntity = EntityFindUtil.findEntity(player.level(), String.valueOf(message.uuid));
            if (lookingEntity != null) {
                DamageHandler.doDamage(lookingEntity, ModDamageTypes.causeLungeMineDamage(player.level().registryAccess(), player, player), lookingEntity instanceof VehicleEntity ? 600 : 150);
                causeLungeMineExplode(player, lookingEntity);
            }
        } else if (message.type == 1) {
            new CustomExplosion.Builder(player)
                    .damage(60)
                    .radius(4)
                    .damageMultiplier(1.25f)
                    .withParticleType(ParticleTool.ParticleType.MEDIUM)
                    .position(message.pos)
                    .explode();
        }
        player.swing(InteractionHand.MAIN_HAND);
    }

    public static void causeLungeMineExplode(Entity attacker, Entity target) {
        new CustomExplosion.Builder(target)
                .damage(60)
                .radius(4)
                .attacker(attacker)
                .damageMultiplier(1.25f)
                .withParticleType(ParticleTool.ParticleType.MEDIUM)
                .explode();
    }
}
