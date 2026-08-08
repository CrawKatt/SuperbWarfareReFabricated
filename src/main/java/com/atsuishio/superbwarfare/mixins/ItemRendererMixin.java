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
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Set;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin {
    @Unique
    private static final Set<ResourceLocation> CUSTOM_GUI_ICON_ITEMS = Set.of(
            ResourceLocation.fromNamespaceAndPath("superbwarfare", "lunge_mine")
    );

    @Shadow
    @Final
    private ItemModelShaper itemModelShaper;

    @ModifyVariable(
            method = "render(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;ZLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IILnet/minecraft/client/resources/model/BakedModel;)V",
            at = @At(value = "HEAD"),
            argsOnly = true
    )
    public BakedModel renderItem(BakedModel bakedModel, @Local(argsOnly = true) ItemStack stack, @Local(argsOnly = true) ItemDisplayContext displayContext) {
        ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(stack.getItem());
        String suffix = null;

        if (CUSTOM_GUI_ICON_ITEMS.contains(itemId)) {
            suffix = switch (displayContext) {
                case GUI, GROUND, FIXED, THIRD_PERSON_LEFT_HAND -> "_icon";
                case THIRD_PERSON_RIGHT_HAND -> "_3d";
                default -> null;
            };
        } else if (stack.getItem() instanceof GunGeoItem
                && (displayContext == ItemDisplayContext.GUI || displayContext == ItemDisplayContext.GROUND)) {
            suffix = "_icon";
        }

        if (suffix != null) {
            String selectedSuffix = suffix;
            ModelResourceLocation modelLocation = ModelResourceLocation.inventory(itemId.withPath(path -> path + selectedSuffix));
            return this.itemModelShaper.getModelManager().getModel(modelLocation);
        }

        return bakedModel;
    }
}
