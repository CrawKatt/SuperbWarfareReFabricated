package com.atsuishio.superbwarfare.api.event.converter;

import com.atsuishio.superbwarfare.api.event.PreKillEvent;
import com.atsuishio.superbwarfare.api.event.wrapper.PreKillEventWrapper;
import io.github.lounode.eventwrapper.forge.event.converter.ForgeEventConverter;

public class PreKillEventConverter implements ForgeEventConverter<PreKillEvent, PreKillEventWrapper> {

    @Override
    public PreKillEvent toEvent(PreKillEventWrapper wrapper) {
        if (wrapper instanceof PreKillEventWrapper.SendKillMessage) {
            var event = new PreKillEvent.SendKillMessage(wrapper.getEntity(), wrapper.getSource(), wrapper.getTarget());
            event.setCanceled(wrapper.isCanceled());
            return event;
        }
        if (wrapper instanceof PreKillEventWrapper.Indicator) {
            var event = new PreKillEvent.Indicator(wrapper.getEntity(), wrapper.getSource(), wrapper.getTarget());
            event.setCanceled(wrapper.isCanceled());
            return event;
        }
        var event = new PreKillEvent.SendKillMessage(wrapper.getEntity(), wrapper.getSource(), wrapper.getTarget());
        event.setCanceled(wrapper.isCanceled());
        return event;
    }

    @Override
    public PreKillEventWrapper toWrapper(PreKillEvent event) {
        PreKillEventWrapper wrapper;
        if (event instanceof PreKillEvent.SendKillMessage) {
            wrapper = new PreKillEventWrapper.SendKillMessage(event.getEntity(), event.getSource(), event.getTarget());
        } else if (event instanceof PreKillEvent.Indicator) {
            wrapper = new PreKillEventWrapper.Indicator(event.getEntity(), event.getSource(), event.getTarget());
        } else {
            wrapper = new PreKillEventWrapper(event.getEntity(), event.getSource(), event.getTarget());
        }
        wrapper.setCanceled(event.isCanceled());
        return wrapper;
    }

    public static class SendKillMessageConverter implements ForgeEventConverter<PreKillEvent.SendKillMessage, PreKillEventWrapper.SendKillMessage> {
        @Override
        public PreKillEvent.SendKillMessage toEvent(PreKillEventWrapper.SendKillMessage wrapper) {
            var event = new PreKillEvent.SendKillMessage(wrapper.getEntity(), wrapper.getSource(), wrapper.getTarget());
            event.setCanceled(wrapper.isCanceled());
            return event;
        }

        @Override
        public PreKillEventWrapper.SendKillMessage toWrapper(PreKillEvent.SendKillMessage event) {
            var wrapper = new PreKillEventWrapper.SendKillMessage(event.getEntity(), event.getSource(), event.getTarget());
            wrapper.setCanceled(event.isCanceled());
            return wrapper;
        }
    }

    public static class IndicatorConverter implements ForgeEventConverter<PreKillEvent.Indicator, PreKillEventWrapper.Indicator> {
        @Override
        public PreKillEvent.Indicator toEvent(PreKillEventWrapper.Indicator wrapper) {
            var event = new PreKillEvent.Indicator(wrapper.getEntity(), wrapper.getSource(), wrapper.getTarget());
            event.setCanceled(wrapper.isCanceled());
            return event;
        }

        @Override
        public PreKillEventWrapper.Indicator toWrapper(PreKillEvent.Indicator event) {
            var wrapper = new PreKillEventWrapper.Indicator(event.getEntity(), event.getSource(), event.getTarget());
            wrapper.setCanceled(event.isCanceled());
            return wrapper;
        }
    }
}
