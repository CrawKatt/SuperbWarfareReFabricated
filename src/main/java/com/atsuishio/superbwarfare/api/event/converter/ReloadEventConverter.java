package com.atsuishio.superbwarfare.api.event.converter;

import com.atsuishio.superbwarfare.api.event.ReloadEvent;
import com.atsuishio.superbwarfare.api.event.wrapper.ReloadEventWrapper;
import io.github.lounode.eventwrapper.forge.event.converter.ForgeEventConverter;
import org.jetbrains.annotations.NotNull;

public class ReloadEventConverter implements ForgeEventConverter<ReloadEvent, ReloadEventWrapper> {
    @Override
    @NotNull
    public ReloadEvent toEvent(@NotNull ReloadEventWrapper wrapper) {
        if (wrapper instanceof ReloadEventWrapper.Pre) {
            return new ReloadEvent.Pre(wrapper.getEntity(), wrapper.getData());
        }
        if (wrapper instanceof ReloadEventWrapper.Post) {
            return new ReloadEvent.Post(wrapper.getEntity(), wrapper.getData());
        }
        return new ReloadEvent.Pre(wrapper.getEntity(), wrapper.getData());
    }

    @Override
    @NotNull
    public ReloadEventWrapper toWrapper(ReloadEvent event) {
        if (event instanceof ReloadEvent.Pre) {
            return new ReloadEventWrapper.Pre(event.shooter, event.data);
        }
        if (event instanceof ReloadEvent.Post) {
            return new ReloadEventWrapper.Post(event.shooter, event.data);
        }
        return new ReloadEventWrapper(event.shooter, event.data);
    }

    public static class PreConverter implements ForgeEventConverter<ReloadEvent.Pre, ReloadEventWrapper.Pre> {
        @Override
        @NotNull
        public ReloadEvent.Pre toEvent(ReloadEventWrapper.Pre wrapper) {
            return new ReloadEvent.Pre(wrapper.getEntity(), wrapper.getData());
        }

        @Override
        @NotNull
        public ReloadEventWrapper.Pre toWrapper(ReloadEvent.Pre event) {
            return new ReloadEventWrapper.Pre(event.shooter, event.data);
        }
    }

    public static class PostConverter implements ForgeEventConverter<ReloadEvent.Post, ReloadEventWrapper.Post> {
        @Override
        @NotNull
        public ReloadEvent.Post toEvent(ReloadEventWrapper.Post wrapper) {
            return new ReloadEvent.Post(wrapper.getEntity(), wrapper.getData());
        }

        @Override
        @NotNull
        public ReloadEventWrapper.Post toWrapper(ReloadEvent.Post event) {
            return new ReloadEventWrapper.Post(event.shooter, event.data);
        }
    }
}
