package com.atsuishio.superbwarfare.api.event.converter;

import com.atsuishio.superbwarfare.api.event.RenderPlayerArmEvent;
import com.atsuishio.superbwarfare.api.event.wrapper.RenderPlayerArmEventWrapper;
import io.github.lounode.eventwrapper.forge.event.converter.ForgeEventConverter;
import org.jetbrains.annotations.NotNull;

public class RenderPlayerArmEventConverter implements ForgeEventConverter<RenderPlayerArmEvent, RenderPlayerArmEventWrapper> {

    @Override
    @NotNull
    public RenderPlayerArmEvent toEvent(RenderPlayerArmEventWrapper wrapper) {
        return new RenderPlayerArmEvent(
                wrapper.getLocalPlayer(), wrapper.getTransformType(), wrapper.getStack(),
                wrapper.getArm(), wrapper.getBone(), wrapper.getCurrentBuffer(),
                wrapper.getRenderType(), wrapper.getPackedLightIn(), wrapper.isUseOldHandRender()
        );
    }

    @Override
    @NotNull
    public RenderPlayerArmEventWrapper toWrapper(RenderPlayerArmEvent event) {
        var wrapper = new RenderPlayerArmEventWrapper(
                event.getLocalPlayer(), event.getTransformType(), event.getStack(),
                event.getArm(), event.getBone(), event.getCurrentBuffer(),
                event.getRenderType(), event.getPackedLightIn(), event.isUseOldHandRender()
        );
        wrapper.setCanceled(event.isCanceled());
        return wrapper;
    }
}
