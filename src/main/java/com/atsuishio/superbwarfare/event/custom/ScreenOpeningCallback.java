package com.atsuishio.superbwarfare.event.custom;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.Nullable;

public interface ScreenOpeningCallback {
    Event<ScreenOpeningCallback> EVENT = EventFactory.createArrayBacked(
            ScreenOpeningCallback.class,
            callbacks -> (currentScreen, newScreen) -> {
                Screen result = newScreen;

                for (ScreenOpeningCallback callback : callbacks) {
                    Screen modified = callback.onScreenOpening(currentScreen, result);
                    if (modified != result) {
                        result = modified;
                    }
                }

                return result;
            }
    );

    @Nullable
    Screen onScreenOpening(@Nullable Screen currentScreen, @Nullable Screen newScreen);
}
