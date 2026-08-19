package com.atsuishio.superbwarfare.tools;

import com.google.gson.*;
import net.minecraft.nbt.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;

public class NBTTool {

    public static CompoundTag getTag(ItemStack stack) {
        return stack.hasTag() ? stack.getTag().copy() : new CompoundTag();
    }

    /** Do not use this to save gun data; use GunData.save(). */
    public static void saveTag(ItemStack stack, CompoundTag tag) {
        CompoundTag merged = stack.hasTag() ? stack.getTag().copy() : new CompoundTag();
        stack.setTag(merged.merge(tag));
    }

    public static ItemStack withTag(Item item, int count, Consumer<CompoundTag> setter) {
        return withTag(new ItemStack(item, count), setter);
    }

    public static ItemStack withTag(Item item, Consumer<CompoundTag> setter) {
        return withTag(item, 1, setter);
    }

    public static ItemStack withTag(ItemStack stack, Consumer<CompoundTag> setter) {
        CompoundTag tag = new CompoundTag();
        setter.accept(tag);
        saveTag(stack, tag);
        return stack;
    }

    public static JsonObject convertToJson(CompoundTag nbt) {
        JsonObject json = new JsonObject();
        nbt.getAllKeys().forEach(key -> {
            Tag tag = nbt.get(key);
            json.add(key, parseTag(tag));
        });
        return json;
    }

    // 处理单个 NBT 标签
    public static JsonElement parseTag(Tag tag) {
        if (tag instanceof CompoundTag) {
            return convertToJson((CompoundTag) tag);
        } else if (tag instanceof ListTag list) {
            JsonArray array = new JsonArray();
            list.forEach(element -> array.add(parseTag(element)));
            return array;
        } else if (tag instanceof NumericTag numeric) {
            return new JsonPrimitive(numeric.getAsNumber());
        } else if (tag instanceof StringTag) {
            return new JsonPrimitive(tag.getAsString());
        } else if (tag instanceof ByteArrayTag byteArray) {
            JsonArray array = new JsonArray();
            byte[] bytes = byteArray.getAsByteArray();
            for (byte b : bytes) array.add(new JsonPrimitive(b));
            return array;
        } else if (tag instanceof IntArrayTag intArray) {
            JsonArray array = new JsonArray();
            int[] ints = intArray.getAsIntArray();
            for (int i : ints) array.add(new JsonPrimitive(i));
            return array;
        } else if (tag instanceof LongArrayTag longArray) {
            JsonArray array = new JsonArray();
            long[] longs = longArray.getAsLongArray();
            for (long l : longs) array.add(new JsonPrimitive(l));
            return array;
        } else if (tag instanceof EndTag) {
            return JsonNull.INSTANCE; // 处理结束标签
        }
        // 未知类型回退到字符串表示
        return new JsonPrimitive(tag.toString());
    }
}
