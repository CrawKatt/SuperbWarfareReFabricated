package com.atsuishio.superbwarfare.api.event.converter;

import com.atsuishio.superbwarfare.api.event.ShootEvent;
import com.atsuishio.superbwarfare.api.event.wrapper.ShootEventWrapper;
import io.github.lounode.eventwrapper.forge.event.converter.ForgeEventConverter;
import org.jetbrains.annotations.NotNull;

public class ShootEventConverter implements ForgeEventConverter<ShootEvent, ShootEventWrapper> {

    @Override
    @NotNull
    public ShootEvent toEvent(ShootEventWrapper wrapper) {
        if (wrapper instanceof ShootEventWrapper.Pre) {
            return new ShootEvent.Pre(wrapper.getShootParameters());
        }
        if (wrapper instanceof ShootEventWrapper.Post) {
            return new ShootEvent.Post(wrapper.getShootParameters());
        }
        return new ShootEvent.Pre(wrapper.getShootParameters());
    }

    @Override
    @NotNull
    public ShootEventWrapper toWrapper(ShootEvent event) {
        if (event instanceof ShootEvent.Pre) {
            return new ShootEventWrapper.Pre(event.getShootParameters());
        }
        if (event instanceof ShootEvent.Post) {
            return new ShootEventWrapper.Post(event.getShootParameters());
        }
        return new ShootEventWrapper(event.getShootParameters());
    }

    public static class PreConverter implements ForgeEventConverter<ShootEvent.Pre, ShootEventWrapper.Pre> {
        @Override
        @NotNull
        public ShootEvent.Pre toEvent(ShootEventWrapper.Pre wrapper) {
            return new ShootEvent.Pre(wrapper.getShootParameters());
        }

        @Override
        @NotNull
        public ShootEventWrapper.Pre toWrapper(ShootEvent.Pre event) {
            return new ShootEventWrapper.Pre(event.getShootParameters());
        }
    }

    public static class PostConverter implements ForgeEventConverter<ShootEvent.Post, ShootEventWrapper.Post> {
        @Override
        @NotNull
        public ShootEvent.Post toEvent(ShootEventWrapper.Post wrapper) {
            return new ShootEvent.Post(wrapper.getShootParameters());
        }

        @Override
        @NotNull
        public ShootEventWrapper.Post toWrapper(ShootEvent.Post event) {
            return new ShootEventWrapper.Post(event.getShootParameters());
        }
    }
}