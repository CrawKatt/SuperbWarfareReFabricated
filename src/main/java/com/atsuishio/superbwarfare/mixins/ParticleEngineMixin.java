package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.client.renderer.ModParticleRenderTypes;
import com.atsuishio.superbwarfare.client.renderer.special.ExplosionParticleRenderer;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleRenderType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;

@Mixin(ParticleEngine.class)
public class ParticleEngineMixin {
    private static final String RENDER = "render(Lcom/mojang/blaze3d/vertex/PoseStack;"
            + "Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;Lnet/minecraft/client/renderer/LightTexture;"
            + "Lnet/minecraft/client/Camera;F)V";

    @Shadow
    @Final
    private Map<ParticleRenderType, Queue<Particle>> particles;

    @Inject(method = RENDER, at = @At("HEAD"))
    private void superbWarfare$beginParticlePass(CallbackInfo ci) {
        ExplosionParticleRenderer.beginParticlePass();
    }

    @WrapOperation(method = RENDER, at = @At(value = "INVOKE",
            target = "Ljava/util/List;iterator()Ljava/util/Iterator;", remap = false))
    private Iterator<ParticleRenderType> superbWarfare$includeSoftParticles(List<ParticleRenderType> order,
                                                                          Operation<Iterator<ParticleRenderType>> original) {
        // Iris filters this list before iteration and omits translucent types during its opaque pass.
        if (!order.contains(ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT)) return original.call(order);

        List<ParticleRenderType> expanded = null;
        for (var entry : this.particles.entrySet()) {
            ParticleRenderType type = entry.getKey();
            if (entry.getValue().isEmpty() || !ModParticleRenderTypes.isSoftTranslucent(type) || order.contains(type)) continue;
            if (expanded == null) expanded = new ArrayList<>(order);
            expanded.add(type);
        }
        return original.call(expanded == null ? order : expanded);
    }

    @WrapOperation(method = RENDER, at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/particle/Particle;render(Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/client/Camera;F)V"))
    private void superbWarfare$deferExplosionParticle(Particle particle, VertexConsumer vertices, Camera camera,
                                                   float partialTick, Operation<Void> original) {
        if (!ExplosionParticleRenderer.defer(particle, camera, partialTick)) {
            original.call(particle, vertices, camera, partialTick);
        }
    }
}
