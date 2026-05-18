package com.atsuishio.superbwarfare.mixins;

import com.google.common.collect.ImmutableList;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Entity.class)
public interface EntityAccessor {

    @Accessor("passengers")
    void setPassengers(ImmutableList<Entity> passengers);

    @Accessor("boardingCooldown")
    void setBoardingCooldown(int boardingCooldown);
}
