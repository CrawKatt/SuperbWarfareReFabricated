package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.event.custom.RenderNameTagCallback;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin<T extends Entity> {
    @Inject(method = "renderNameTag", at = @At("HEAD"), cancellable = true)
    private void superbwarfare$onRenderNameTag(T entity, Component component, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, CallbackInfo ci) {
        RenderNameTagCallback.Event event = new RenderNameTagCallback.Event(entity);
        RenderNameTagCallback.EVENT.invoker().onRenderNameTag(event);
        if (event.isCanceled()) {
            ci.cancel();
        }
    }
}
