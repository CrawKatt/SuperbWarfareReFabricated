package com.atsuishio.superbwarfare.mixins;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Map;

@Mixin(AttributeSupplier.class)
public interface AttributeSupplierAccessor {

    @Accessor("instances")
    Map<Holder<Attribute>, AttributeInstance> superbWarfare$getInstances();

    @Invoker("<init>")
    static AttributeSupplier superbWarfare$create(Map<Holder<Attribute>, AttributeInstance> instances) {
        throw new AssertionError();
    }
}
