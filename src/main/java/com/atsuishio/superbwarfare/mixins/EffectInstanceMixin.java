package com.atsuishio.superbwarfare.mixins;

import net.minecraft.client.renderer.EffectInstance;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Vanilla resolves shader effect definitions and programs against the
 * minecraft namespace only. Post-chain passes and program references that use
 * a mod namespace (e.g. {@code superbwarfare:thermal}) therefore fail to load,
 * which silently disabled the Thermal Imaging and Handsome Goggles shaders.
 *
 * Adapted from the 1.21 fix: in 1.20.1 both call sites build the location via
 * a single-string concatenation followed by {@code new ResourceLocation(String)},
 * so the constructor creation is redirected instead of {@code withDefaultNamespace}.
 *
 * Supported raw forms:
 * <ul>
 *   <li>{@code shaders/program/<name><ext>} (vanilla, unchanged)</li>
 *   <li>{@code shaders/program/<ns>:<name>.json} (effect definition)</li>
 *   <li>{@code <ns>:<name><ext>} (vertex/fragment program lookup)</li>
 * </ul>
 */
@Mixin(EffectInstance.class)
public abstract class EffectInstanceMixin {

    @Unique
    private static final String SUPERBWARFARE$PROGRAM_PREFIX = "shaders/program/";

    @Redirect(
            method = "<init>(Lnet/minecraft/server/packs/resources/ResourceManager;Ljava/lang/String;)V",
            at = @At(
                    value = "NEW",
                    target = "(Ljava/lang/String;)Lnet/minecraft/resources/ResourceLocation;"
            )
    )
    private ResourceLocation superbwarfare$resolveEffectDefinition(String path) {
        return superbwarfare$resolveNamespacedProgram(path);
    }

    @Redirect(
            method = "getOrCreate(Lnet/minecraft/server/packs/resources/ResourceManager;Lcom/mojang/blaze3d/shaders/Program$Type;Ljava/lang/String;)Lcom/mojang/blaze3d/shaders/EffectProgram;",
            at = @At(
                    value = "NEW",
                    target = "(Ljava/lang/String;)Lnet/minecraft/resources/ResourceLocation;"
            )
    )
    private static ResourceLocation superbwarfare$resolveEffectProgram(String path) {
        return superbwarfare$resolveNamespacedProgram(path);
    }

    @Unique
    private static ResourceLocation superbwarfare$resolveNamespacedProgram(String raw) {
        String rest = raw.startsWith(SUPERBWARFARE$PROGRAM_PREFIX)
                ? raw.substring(SUPERBWARFARE$PROGRAM_PREFIX.length())
                : raw;

        int colon = rest.indexOf(':');
        if (colon < 0) {
            return new ResourceLocation(raw);
        }

        String namespace = rest.substring(0, colon);
        String name = rest.substring(colon + 1);
        return new ResourceLocation(namespace, SUPERBWARFARE$PROGRAM_PREFIX + name);
    }
}
