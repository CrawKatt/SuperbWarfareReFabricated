package com.atsuishio.superbwarfare.mixins;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.nbt.TagParser;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.ShapedRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShapedRecipe.class)
public class ShapedRecipeMixin {
    @Inject(method = "itemStackFromJson", at = @At("RETURN"))
    private static void superbwarfare$readNbtResult(JsonObject json, CallbackInfoReturnable<ItemStack> cir) {
        if (!json.has("nbt")) return;

        try {
            cir.getReturnValue().setTag(TagParser.parseTag(json.getAsJsonObject("nbt").toString()));
        } catch (CommandSyntaxException exception) {
            throw new JsonSyntaxException("Invalid NBT in shaped recipe result", exception);
        }
    }
}
