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
    private static final Set<ResourceLocation> CUSTOM_SEPARATE_TRANSFORM_ITEMS = Set.of(
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
    private BakedModel superbwarfare$useSeparateTransformModel(BakedModel bakedModel, ItemStack stack, ItemDisplayContext displayContext) {
        ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(stack.getItem());
        String suffix = superbwarfare$getSeparateModelSuffix(stack, itemId, displayContext);
        if (suffix == null) return bakedModel;

        ModelResourceLocation modelLocation = new ModelResourceLocation(
                itemId.withPath(path -> path + suffix),
                "inventory"
        );
        return this.itemModelShaper.getModelManager().getModel(modelLocation);
    }

    @Unique
    private static String superbwarfare$getSeparateModelSuffix(ItemStack stack, ResourceLocation itemId,
                                                                ItemDisplayContext displayContext) {
        // Forge's generated gun models override only the GUI perspective.
        if (stack.getItem() instanceof GunGeoItem) {
            return displayContext == ItemDisplayContext.GUI ? "_icon" : null;
        }

        if (!CUSTOM_SEPARATE_TRANSFORM_ITEMS.contains(itemId)) return null;

        return switch (displayContext) {
            case GUI, GROUND, FIXED, THIRD_PERSON_LEFT_HAND -> "_icon";
            case THIRD_PERSON_RIGHT_HAND -> "_3d";
            default -> null;
        };
    }
}
