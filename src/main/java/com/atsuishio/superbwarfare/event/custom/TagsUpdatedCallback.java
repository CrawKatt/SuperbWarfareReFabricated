package com.atsuishio.superbwarfare.event.custom;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public interface TagsUpdatedCallback {
    Event<TagsUpdatedCallback> EVENT = EventFactory.createArrayBacked(
            TagsUpdatedCallback.class,
            callbacks -> shouldUpdateStaticData -> {
                for (TagsUpdatedCallback callback : callbacks) {
                    callback.onTagsUpdated(shouldUpdateStaticData);
                }
            }
    );

    void onTagsUpdated(boolean shouldUpdateStaticData);
}
