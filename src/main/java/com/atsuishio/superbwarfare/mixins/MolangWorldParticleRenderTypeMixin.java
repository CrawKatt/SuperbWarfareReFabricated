package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.Mod;
import com.github.mcmodderanchor.simplebedrockmodel.v1.particle.data.ParticleDescription;
import com.github.mcmodderanchor.simplebedrockmodel.v1.particle.world.MolangWorldParticleRenderType;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.Tesselator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// TODO 这对吗？
@Mixin(MolangWorldParticleRenderType.class)
public abstract class MolangWorldParticleRenderTypeMixin {

    @Unique
    private static final ResourceLocation SUPERBWARFARE$GUN_SMOKE_TEXTURE = Mod.loc("textures/particle/shoot_smoke2.png");

    @Shadow(remap = false)
    @Final
    private ParticleDescription.Material material;

    @Shadow(remap = false)
    @Final
    private ResourceLocation texture;

    @Inject(method = "begin", at = @At("RETURN"), remap = false)
    private void superbwarfare$disableAlphaDepthWrite(
            Tesselator tesselator, TextureManager textureManager, CallbackInfoReturnable<BufferBuilder> cir
    ) {
        if (material == ParticleDescription.Material.PARTICLES_ALPHA
                && SUPERBWARFARE$GUN_SMOKE_TEXTURE.equals(texture)) {
            RenderSystem.depthMask(false);
        }
    }
}
