package com.atsuishio.superbwarfare.init;

import com.atsuishio.superbwarfare.data.gun.GunData;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModSerializers {

    public static final EntityDataSerializer<IntList> INT_LIST_SERIALIZER =
            EntityDataSerializer.simple(FriendlyByteBuf::writeIntIdList, FriendlyByteBuf::readIntIdList);

    public static final EntityDataSerializer<List<Float>> FLOAT_LIST_SERIALIZER =
            EntityDataSerializer.simple((buf, list) -> {
                buf.writeVarInt(list.size());
                for (Float v : list) {
                    buf.writeFloat(v);
                }
            }, buf -> {
                var length = buf.readVarInt();
                var list = new ArrayList<Float>();
                for (int i = 0; i < length; i++) {
                    list.add(buf.readFloat());
                }
                return list;
            });

    public static final EntityDataSerializer<Map<String, GunData>> VEHICLE_GUN_DATA_MAP_SERIALIZER =
            EntityDataSerializer.simple((buf, map) -> {
                buf.writeVarInt(map.size());
                for (var kv : map.entrySet()) {
                    buf.writeUtf(kv.getKey());
                    buf.writeNbt(kv.getValue().stack.getTag());
                }
            }, buf -> {
                var length = buf.readVarInt();
                var map = new HashMap<String, GunData>();
                for (int i = 0; i < length; i++) {
                    var weaponName = buf.readUtf();

                    var tag = buf.readNbt();
                    var gunItemStack = new ItemStack(ModItems.VEHICLE_GUN.get());
                    gunItemStack.setTag(tag);

                    map.put(weaponName, GunData.from(gunItemStack));
                }

                return map;
            });

    public static void register() {
        EntityDataSerializers.registerSerializer(INT_LIST_SERIALIZER);
        EntityDataSerializers.registerSerializer(FLOAT_LIST_SERIALIZER);
        EntityDataSerializers.registerSerializer(VEHICLE_GUN_DATA_MAP_SERIALIZER);
    }
}