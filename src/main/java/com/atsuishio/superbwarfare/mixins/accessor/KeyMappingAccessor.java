package com.atsuishio.superbwarfare.mixins.accessor;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(KeyMapping.class)
public interface KeyMappingAccessor {

    @Accessor("MAP")
    static Map<InputConstants.Key, KeyMapping> superbwarfare$getMap() {
        throw new AssertionError();
    }

    @Accessor("key")
    InputConstants.Key superbwarfare$getKey();

    @Accessor("clickCount")
    int superbwarfare$getClickCount();

    @Accessor("clickCount")
    void superbwarfare$setClickCount(int clickCount);
}
