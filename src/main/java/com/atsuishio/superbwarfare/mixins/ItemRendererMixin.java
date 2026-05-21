package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.item.gun.GunGeoItem;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.ItemModelShaper;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin {
    @Shadow
    @Final
    private ItemModelShaper itemModelShaper;

    @ModifyVariable(
            method = "render(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;ZLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IILnet/minecraft/client/resources/model/BakedModel;)V",
            at = @At(value = "HEAD"),
            argsOnly = true
    )
    public BakedModel renderItem(BakedModel bakedModel, @Local(argsOnly = true) ItemStack stack, @Local(argsOnly = true) ItemDisplayContext displayContext) {
        if (stack.getItem() instanceof GunGeoItem && (displayContext == ItemDisplayContext.GUI || displayContext == ItemDisplayContext.GROUND || displayContext == ItemDisplayContext.FIXED)) {
            ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(stack.getItem());
            ModelResourceLocation iconModelLocation = ModelResourceLocation.inventory(itemId.withPath(path -> path + "_icon"));
            return this.itemModelShaper.getModelManager().getModel(iconModelLocation);
        }

        return bakedModel;
    }
}
