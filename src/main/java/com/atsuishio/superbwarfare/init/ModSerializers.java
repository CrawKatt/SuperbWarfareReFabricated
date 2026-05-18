package com.atsuishio.superbwarfare.init;

import com.atsuishio.superbwarfare.Mod;
import com.atsuishio.superbwarfare.data.gun.GunData;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModSerializers {

    public static final EntityDataSerializer<List<Integer>> INT_LIST_SERIALIZER = EntityDataSerializer.forValueType(
            ByteBufCodecs.VAR_INT.apply(ByteBufCodecs.list())
    );

    public static final EntityDataSerializer<List<Float>> FLOAT_LIST_SERIALIZER = EntityDataSerializer.forValueType(
            ByteBufCodecs.FLOAT.apply(ByteBufCodecs.list())
    );

    public static final EntityDataSerializer<Map<String, GunData>> VEHICLE_GUN_DATA_MAP_SERIALIZER = new EntityDataSerializer<>() {
        @Override
        public @NotNull StreamCodec<? super RegistryFriendlyByteBuf, Map<String, GunData>> codec() {
            return ByteBufCodecs.map(HashMap::new, ByteBufCodecs.STRING_UTF8, GunData.VEHICLE_GUN_STREAM_CODEC);
        }

        @Override
        public @NotNull Map<String, GunData> copy(@NotNull Map<String, GunData> map) {
            var newMap = new HashMap<String, GunData>();
            map.forEach((key, value) -> newMap.put(key, value.copy()));
            return newMap;
        }
    };

    public static void init() {
        EntityDataSerializers.registerSerializer(INT_LIST_SERIALIZER);
        EntityDataSerializers.registerSerializer(FLOAT_LIST_SERIALIZER);
        EntityDataSerializers.registerSerializer(VEHICLE_GUN_DATA_MAP_SERIALIZER);
    }
}
