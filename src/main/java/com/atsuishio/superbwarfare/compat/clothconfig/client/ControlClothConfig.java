package com.atsuishio.superbwarfare.compat.clothconfig.client;

import com.atsuishio.superbwarfare.config.client.ControlConfig;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.network.chat.Component;

public class ControlClothConfig {

    public static void init(ConfigBuilder root, ConfigEntryBuilder entryBuilder) {
        ConfigCategory category = root.getOrCreateCategory(Component.translatable("config.superbwarfare.client.control"));

        category.addEntry(entryBuilder
                .startBooleanToggle(Component.translatable("config.superbwarfare.client.control.invert_aircraft_control"), ControlConfig.INVERT_AIRCRAFT_CONTROL)
                .setDefaultValue(true)
                .setSaveConsumer(v -> { ControlConfig.INVERT_AIRCRAFT_CONTROL = v; ControlConfig.save(); })
                .setTooltip(Component.translatable("config.superbwarfare.client.control.invert_aircraft_control.des")).build()
        );

        category.addEntry(entryBuilder
                .startIntSlider(Component.translatable("config.superbwarfare.client.control.mouse_sensitivity"), ControlConfig.MOUSE_SENSITIVITY,
                        10, 200)
                .setDefaultValue(100)
                .setSaveConsumer(v -> { ControlConfig.MOUSE_SENSITIVITY = v; ControlConfig.save(); })
                .setTooltip(Component.translatable("config.superbwarfare.client.control.mouse_sensitivity.des")).build()
        );
    }
}
