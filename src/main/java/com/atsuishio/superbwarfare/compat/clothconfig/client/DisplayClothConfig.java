package com.atsuishio.superbwarfare.compat.clothconfig.client;

import com.atsuishio.superbwarfare.config.client.DisplayConfig;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.network.chat.Component;

public class DisplayClothConfig {

    public static void init(ConfigBuilder root, ConfigEntryBuilder entryBuilder) {
        ConfigCategory category = root.getOrCreateCategory(Component.translatable("config.superbwarfare.client.display"));

        category.addEntry(entryBuilder
                .startBooleanToggle(Component.translatable("config.superbwarfare.client.display.enable_gun_lod"), DisplayConfig.ENABLE_GUN_LOD)
                .setDefaultValue(false)
                .setSaveConsumer(v -> { DisplayConfig.ENABLE_GUN_LOD = v; DisplayConfig.save(); })
                .setTooltip(Component.translatable("config.superbwarfare.client.display.enable_gun_lod.des"))
                .build()
        );

        category.addEntry(entryBuilder
                .startIntSlider(Component.translatable("config.superbwarfare.client.display.weapon_hud_x_offset"), DisplayConfig.WEAPON_HUD_X_OFFSET,
                        -1000, 1000)
                .setDefaultValue(0)
                .setSaveConsumer(v -> { DisplayConfig.WEAPON_HUD_X_OFFSET = v; DisplayConfig.save(); })
                .setTooltip(Component.translatable("config.superbwarfare.client.display.weapon_hud_x_offset.des"))
                .build()
        );

        category.addEntry(entryBuilder
                .startIntSlider(Component.translatable("config.superbwarfare.client.display.weapon_hud_y_offset"), DisplayConfig.WEAPON_HUD_Y_OFFSET,
                        -1000, 1000)
                .setDefaultValue(0)
                .setSaveConsumer(v -> { DisplayConfig.WEAPON_HUD_Y_OFFSET = v; DisplayConfig.save(); })
                .setTooltip(Component.translatable("config.superbwarfare.client.display.weapon_hud_y_offset.des"))
                .build()
        );

        category.addEntry(entryBuilder
                .startBooleanToggle(Component.translatable("config.superbwarfare.client.display.enable_heat_bar_hud"), DisplayConfig.ENABLE_HEAT_BAR_HUD)
                .setDefaultValue(true)
                .setSaveConsumer(v -> { DisplayConfig.ENABLE_HEAT_BAR_HUD = v; DisplayConfig.save(); })
                .setTooltip(Component.translatable("config.superbwarfare.client.display.enable_heat_bar_hud.des"))
                .build()
        );

        category.addEntry(entryBuilder
                .startIntSlider(Component.translatable("config.superbwarfare.client.display.heat_bar_hud_x_offset"), DisplayConfig.HEAT_BAR_HUD_X_OFFSET,
                        -1000, 1000)
                .setDefaultValue(0)
                .setSaveConsumer(v -> { DisplayConfig.HEAT_BAR_HUD_X_OFFSET = v; DisplayConfig.save(); })
                .setTooltip(Component.translatable("config.superbwarfare.client.display.heat_bar_hud_x_offset.des"))
                .build()
        );

        category.addEntry(entryBuilder
                .startIntSlider(Component.translatable("config.superbwarfare.client.display.heat_bar_hud_y_offset"), DisplayConfig.HEAT_BAR_HUD_Y_OFFSET,
                        -1000, 1000)
                .setDefaultValue(0)
                .setSaveConsumer(v -> { DisplayConfig.HEAT_BAR_HUD_Y_OFFSET = v; DisplayConfig.save(); })
                .setTooltip(Component.translatable("config.superbwarfare.client.display.heat_bar_hud_y_offset.des"))
                .build()
        );

        category.addEntry(entryBuilder
                .startBooleanToggle(Component.translatable("config.superbwarfare.client.display.kill_indication"), DisplayConfig.KILL_INDICATION)
                .setDefaultValue(true)
                .setSaveConsumer(v -> { DisplayConfig.KILL_INDICATION = v; DisplayConfig.save(); })
                .setTooltip(Component.translatable("config.superbwarfare.client.display.kill_indication.des"))
                .build()
        );

        category.addEntry(entryBuilder
                .startBooleanToggle(Component.translatable("config.superbwarfare.client.display.ammo_hud"), DisplayConfig.AMMO_HUD)
                .setDefaultValue(true)
                .setSaveConsumer(v -> { DisplayConfig.AMMO_HUD = v; DisplayConfig.save(); })
                .setTooltip(Component.translatable("config.superbwarfare.client.display.ammo_hud.des"))
                .build()
        );

        category.addEntry(entryBuilder
                .startBooleanToggle(Component.translatable("config.superbwarfare.client.display.advanced_ammo_hud"), DisplayConfig.ADVANCED_AMMO_HUD)
                .setDefaultValue(true)
                .setSaveConsumer(v -> { DisplayConfig.ADVANCED_AMMO_HUD = v; DisplayConfig.save(); })
                .setTooltip(Component.translatable("config.superbwarfare.client.display.advanced_ammo_hud.des"))
                .build()
        );

        category.addEntry(entryBuilder
                .startBooleanToggle(Component.translatable("config.superbwarfare.client.display.vehicle_info"), DisplayConfig.VEHICLE_INFO)
                .setDefaultValue(true)
                .setSaveConsumer(v -> { DisplayConfig.VEHICLE_INFO = v; DisplayConfig.save(); })
                .setTooltip(Component.translatable("config.superbwarfare.client.display.vehicle_info.des"))
                .build()
        );

        category.addEntry(entryBuilder
                .startBooleanToggle(Component.translatable("config.superbwarfare.client.display.float_cross_hair"), DisplayConfig.FLOAT_CROSS_HAIR)
                .setDefaultValue(true)
                .setSaveConsumer(v -> { DisplayConfig.FLOAT_CROSS_HAIR = v; DisplayConfig.save(); })
                .setTooltip(Component.translatable("config.superbwarfare.client.display.float_cross_hair.des"))
                .build()
        );

        category.addEntry(entryBuilder
                .startBooleanToggle(Component.translatable("config.superbwarfare.client.display.camera_rotate"), DisplayConfig.CAMERA_ROTATE)
                .setDefaultValue(true)
                .setSaveConsumer(v -> { DisplayConfig.CAMERA_ROTATE = v; DisplayConfig.save(); })
                .setTooltip(Component.translatable("config.superbwarfare.client.display.camera_rotate.des"))
                .build()
        );

        category.addEntry(entryBuilder
                .startBooleanToggle(Component.translatable("config.superbwarfare.client.display.armor_plate_hud"), DisplayConfig.ARMOR_PLATE_HUD)
                .setDefaultValue(true)
                .setSaveConsumer(v -> { DisplayConfig.ARMOR_PLATE_HUD = v; DisplayConfig.save(); })
                .setTooltip(Component.translatable("config.superbwarfare.client.display.armor_plate_hud.des"))
                .build()
        );

        category.addEntry(entryBuilder
                .startBooleanToggle(Component.translatable("config.superbwarfare.client.display.stamina_hud"), DisplayConfig.STAMINA_HUD)
                .setDefaultValue(true)
                .setSaveConsumer(v -> { DisplayConfig.STAMINA_HUD = v; DisplayConfig.save(); })
                .setTooltip(Component.translatable("config.superbwarfare.client.display.stamina_hud.des"))
                .build()
        );

        category.addEntry(entryBuilder
                .startBooleanToggle(Component.translatable("config.superbwarfare.client.display.dog_tag_name_visible"), DisplayConfig.DOG_TAG_NAME_VISIBLE)
                .setDefaultValue(true)
                .setSaveConsumer(v -> { DisplayConfig.DOG_TAG_NAME_VISIBLE = v; DisplayConfig.save(); })
                .setTooltip(Component.translatable("config.superbwarfare.client.display.dog_tag_name_visible.des"))
                .build()
        );

        category.addEntry(entryBuilder
                .startBooleanToggle(Component.translatable("config.superbwarfare.client.display.dog_tag_icon_visible"), DisplayConfig.DOG_TAG_ICON_VISIBLE)
                .setDefaultValue(false)
                .setSaveConsumer(v -> { DisplayConfig.DOG_TAG_ICON_VISIBLE = v; DisplayConfig.save(); })
                .setTooltip(Component.translatable("config.superbwarfare.client.display.dog_tag_icon_visible.des"))
                .build()
        );

        category.addEntry(entryBuilder
                .startIntSlider(Component.translatable("config.superbwarfare.client.display.weapon_screen_shake"), DisplayConfig.WEAPON_SCREEN_SHAKE,
                        0, 100)
                .setDefaultValue(100)
                .setSaveConsumer(v -> { DisplayConfig.WEAPON_SCREEN_SHAKE = v; DisplayConfig.save(); })
                .setTooltip(Component.translatable("config.superbwarfare.client.display.weapon_screen_shake.des"))
                .build());

        category.addEntry(entryBuilder
                .startIntSlider(Component.translatable("config.superbwarfare.client.display.explosion_screen_shake"), DisplayConfig.EXPLOSION_SCREEN_SHAKE,
                        0, 100)
                .setDefaultValue(100)
                .setSaveConsumer(v -> { DisplayConfig.EXPLOSION_SCREEN_SHAKE = v; DisplayConfig.save(); })
                .setTooltip(Component.translatable("config.superbwarfare.client.display.explosion_screen_shake.des"))
                .build());

        category.addEntry(entryBuilder
                .startIntSlider(Component.translatable("config.superbwarfare.client.display.shock_screen_shake"), DisplayConfig.SHOCK_SCREEN_SHAKE,
                        0, 100)
                .setDefaultValue(100)
                .setSaveConsumer(v -> { DisplayConfig.SHOCK_SCREEN_SHAKE = v; DisplayConfig.save(); })
                .setTooltip(Component.translatable("config.superbwarfare.client.display.shock_screen_shake.des"))
                .build());
    }
}
