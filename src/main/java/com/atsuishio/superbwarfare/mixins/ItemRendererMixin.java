package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.item.gun.GunGeoItem;
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
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Set;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin {

    @Unique
    private static final Set<ResourceLocation> CUSTOM_GUI_ICON_ITEMS = Set.of(
            new ResourceLocation("superbwarfare", "lunge_mine")
    );

    @Shadow
    @Final
    private ItemModelShaper itemModelShaper;

    @ModifyVariable(
            method = "render(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;ZLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IILnet/minecraft/client/resources/model/BakedModel;)V",
            at = @At("HEAD"),
            argsOnly = true
    )
    private BakedModel superbwarfare$useGuiIconModel(BakedModel bakedModel, ItemStack stack, ItemDisplayContext displayContext) {
        if (superbwarfare$usesGuiIconModel(stack) && superbwarfare$isGuiLikeDisplay(displayContext)) {
            ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(stack.getItem());
            ModelResourceLocation iconModelLocation = new ModelResourceLocation(itemId.withPath(path -> path + "_icon"), "inventory");
            return this.itemModelShaper.getModelManager().getModel(iconModelLocation);
        }

        return bakedModel;
    }

    @Unique
    private static boolean superbwarfare$usesGuiIconModel(ItemStack stack) {
        if (stack.getItem() instanceof GunGeoItem) {
            return true;
        }

        return CUSTOM_GUI_ICON_ITEMS.contains(BuiltInRegistries.ITEM.getKey(stack.getItem()));
    }

    @Unique
    private static boolean superbwarfare$isGuiLikeDisplay(ItemDisplayContext displayContext) {
        return displayContext == ItemDisplayContext.GUI
                || displayContext == ItemDisplayContext.GROUND
                || displayContext == ItemDisplayContext.FIXED;
    }
}
