package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.item.gun.GunGeoItem;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.BlockStateModelLoader;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@Mixin(ModelBakery.class)
public abstract class ModelBakeryMixin {

    @Shadow
    protected abstract UnbakedModel getModel(ResourceLocation location);

    @Shadow
    protected abstract void registerModelAndLoadDependencies(ModelResourceLocation location, UnbakedModel model);

    @Inject(
            method = "<init>(Lnet/minecraft/client/color/block/BlockColors;Lnet/minecraft/util/profiling/ProfilerFiller;Ljava/util/Map;Ljava/util/Map;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resources/model/ModelBakery;loadSpecialItemModelAndDependencies(Lnet/minecraft/client/resources/model/ModelResourceLocation;)V", ordinal = 0)
    )
    private void onAfterItemModels(BlockColors colors, ProfilerFiller profiler,
                                   Map<ResourceLocation, BlockModel> blockModels,
                                   Map<ResourceLocation, List<BlockStateModelLoader.LoadedJson>> blockStates,
                                   CallbackInfo ci) {
        for (Entry<ResourceKey<Item>, Item> entry : BuiltInRegistries.ITEM.entrySet()) {
            if (entry.getValue() instanceof GunGeoItem) {
                ResourceLocation itemId = entry.getKey().location();
                ResourceLocation iconId = itemId.withPath(path -> path + "_icon");
                ResourceLocation modelPath = iconId.withPrefix("item/");
                UnbakedModel model = this.getModel(modelPath);
                ModelResourceLocation inventoryLocation = ModelResourceLocation.inventory(iconId);
                this.registerModelAndLoadDependencies(inventoryLocation, model);
            }
        }
    }
}
