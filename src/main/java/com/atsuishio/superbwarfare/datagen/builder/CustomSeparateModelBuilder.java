package com.atsuishio.superbwarfare.datagen.builder;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;

public class CustomSeparateModelBuilder {

    public static CustomSeparateModelBuilder begin() {
        return new CustomSeparateModelBuilder();
    }

    public CustomSeparateModelBuilder base(String location) {
        return this;
    }

    public CustomSeparateModelBuilder perspective(ItemDisplayContext perspective, String location) {
        return this;
    }

    public CustomSeparateModelBuilder texture(String name, ResourceLocation location) {
        return this;
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("parent", "minecraft:item/generated");
        return json;
    }
}
