package com.atsuishio.superbwarfare.compat.clothconfig.client;

import com.atsuishio.superbwarfare.config.client.ReloadConfig;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.network.chat.Component;

public class ReloadClothConfig {

    public static void init(ConfigBuilder root, ConfigEntryBuilder entryBuilder) {
        ConfigCategory category = root.getOrCreateCategory(Component.translatable("config.superbwarfare.client.reload"));

        category.addEntry(entryBuilder
                .startBooleanToggle(Component.translatable("config.superbwarfare.client.reload.left_click_reload"), ReloadConfig.LEFT_CLICK_RELOAD)
                .setDefaultValue(true)
                .setSaveConsumer(v -> { ReloadConfig.LEFT_CLICK_RELOAD = v; ReloadConfig.save(); })
                .setTooltip(Component.translatable("config.superbwarfare.client.reload.left_click_reload.des")).build()
        );
    }
}
