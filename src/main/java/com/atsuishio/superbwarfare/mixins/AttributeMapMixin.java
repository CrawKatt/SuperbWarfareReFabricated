package com.atsuishio.superbwarfare.mixins;

import com.atsuishio.superbwarfare.init.ModAttributes;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(AttributeMap.class)
public class AttributeMapMixin {

    @Shadow
    @Final
    private Map<Attribute, AttributeInstance> attributes;

    @Shadow
    private void onAttributeModified(AttributeInstance instance) {
        throw new AssertionError();
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void superbwarfare$addModAttributes(AttributeSupplier supplier, CallbackInfo ci) {
        this.superbwarfare$addAttribute(supplier, ModAttributes.BULLET_RESISTANCE.get());

        if (supplier.hasAttribute(Attributes.LUCK)) {
            this.superbwarfare$addAttribute(supplier, ModAttributes.BLOCK_REACH.get());
            this.superbwarfare$addAttribute(supplier, ModAttributes.ENTITY_REACH.get());
        }
    }

    @Unique
    private void superbwarfare$addAttribute(AttributeSupplier supplier, Attribute attribute) {
        this.attributes.computeIfAbsent(attribute, ignored -> {
            AttributeInstance suppliedInstance = supplier.createInstance(this::onAttributeModified, attribute);
            return suppliedInstance != null
                    ? suppliedInstance
                    : new AttributeInstance(attribute, this::onAttributeModified);
        });
    }
}
