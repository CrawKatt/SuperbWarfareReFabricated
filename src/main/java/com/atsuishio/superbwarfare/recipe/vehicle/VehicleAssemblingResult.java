package com.atsuishio.superbwarfare.recipe.vehicle;

import com.atsuishio.superbwarfare.Mod;
import com.atsuishio.superbwarfare.item.common.container.ContainerBlockItem;
import com.atsuishio.superbwarfare.tools.TagDataParser;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Items;

public class VehicleAssemblingResult {
    @SerializedName("item")
    public String itemString = "";
    @SerializedName("entity")
    public String entityTypeString = "";
    @SerializedName("count")
    public int count = 1;
    @SerializedName("nbt")
    public JsonObject nbt;

    public transient ItemStack result = null;

    public ItemStack getResult() {
        if (this.result != null) return this.result;

        if (!entityTypeString.isEmpty()) {
            var type = EntityType.byString(entityTypeString).orElse(null);
            if (type == null) {
                Mod.LOGGER.warn("invalid entity type: {}", entityTypeString);
                this.result = ItemStack.EMPTY;
            } else {
                this.result = ContainerBlockItem.createInstance(type).copyWithCount(count);
            }
        } else if (!itemString.isEmpty()) {
            var item = BuiltInRegistries.ITEM.get(new ResourceLocation(itemString));
            if (item == Items.AIR) {
                Mod.LOGGER.warn("invalid item: {}", itemString);
                this.result = ItemStack.EMPTY;
            } else {
                if (nbt != null) {
                    var tag = TagDataParser.parse(nbt);
                    CompoundTag tmp = new CompoundTag();

                    tmp.put("tag", tag);
                    tmp.putString("id", itemString);
                    tmp.putInt("Count", count);

                    this.result = ItemStack.of(tmp);
                } else {
                    this.result = new ItemStack(item, count);
                }
            }
        } else {
            this.result = ItemStack.EMPTY;
        }

        return this.result;
    }
}
