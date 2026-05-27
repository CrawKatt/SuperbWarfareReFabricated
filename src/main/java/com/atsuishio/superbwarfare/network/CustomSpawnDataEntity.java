package com.atsuishio.superbwarfare.network;

import net.minecraft.network.FriendlyByteBuf;

public interface CustomSpawnDataEntity {
    void writeSpawnData(FriendlyByteBuf buffer);
    void readSpawnData(FriendlyByteBuf buffer);
}
