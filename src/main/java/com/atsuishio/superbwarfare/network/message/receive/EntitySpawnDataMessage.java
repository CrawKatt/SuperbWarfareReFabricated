package com.atsuishio.superbwarfare.network.message.receive;

import com.atsuishio.superbwarfare.network.CustomSpawnDataEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;

public class EntitySpawnDataMessage {

    private final int entityId;
    private final byte[] data;

    public EntitySpawnDataMessage(Entity entity) {
        this.entityId = entity.getId();

        FriendlyByteBuf buffer = new FriendlyByteBuf(io.netty.buffer.Unpooled.buffer());
        ((CustomSpawnDataEntity) entity).writeSpawnData(buffer);

        this.data = new byte[buffer.readableBytes()];
        buffer.readBytes(this.data);
        buffer.release();
    }

    public EntitySpawnDataMessage(int entityId, FriendlyByteBuf dataBuffer) {
        this.entityId = entityId;
        this.data = new byte[dataBuffer.readableBytes()];
        dataBuffer.readBytes(this.data);
    }

    public static void encode(EntitySpawnDataMessage message, FriendlyByteBuf buffer) {
        buffer.writeInt(message.entityId);
        buffer.writeByteArray(message.data);
    }

    public static EntitySpawnDataMessage decode(FriendlyByteBuf buffer) {
        int entityId = buffer.readInt();
        byte[] data = buffer.readByteArray();

        FriendlyByteBuf dataBuffer = new FriendlyByteBuf(io.netty.buffer.Unpooled.wrappedBuffer(data));
        return new EntitySpawnDataMessage(entityId, dataBuffer);
    }

    public static void handler(EntitySpawnDataMessage message) {
        Minecraft mc = Minecraft.getInstance();

        if (mc.level == null) {
            return;
        }

        Entity entity = mc.level.getEntity(message.entityId);
        if (!(entity instanceof CustomSpawnDataEntity spawnDataEntity)) {
            return;
        }

        FriendlyByteBuf buffer = new FriendlyByteBuf(io.netty.buffer.Unpooled.wrappedBuffer(message.data));
        spawnDataEntity.readSpawnData(buffer);
        buffer.release();
    }
}