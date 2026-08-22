package com.atsuishio.superbwarfare.compat.clothconfig.client

import com.atsuishio.superbwarfare.config.client.DisplayConfig
import me.shedaniel.clothconfig2.api.ConfigBuilder
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder
import net.minecraft.network.chat.Component

object DisplayClothConfig {
    fun init(root: ConfigBuilder, entryBuilder: ConfigEntryBuilder) {
        val category = root.getOrCreateCategory(Component.translatable("config.superbwarfare.client.display"))

        category.addEntry(
            entryBuilder
                .startBooleanToggle(
                    Component.translatable("config.superbwarfare.client.display.enable_gun_lod"),
                    DisplayConfig.ENABLE_GUN_LOD.get()
                )
                .setDefaultValue(false)
                .setSaveConsumer { v ->
                    DisplayConfig.ENABLE_GUN_LOD.set(v)
                    DisplayConfig.ENABLE_GUN_LOD.save()
                }
                .setTooltip(Component.translatable("config.superbwarfare.client.display.enable_gun_lod.des"))
                .build()
        )

        category.addEntry(
            entryBuilder
                .startIntSlider(
                    Component.translatable("config.superbwarfare.client.display.vehicle_lod_distance"),
                    DisplayConfig.VEHICLE_LOD_DISTANCE.get(),
                    -1,
                    512
                )
                .setDefaultValue(64)
                .setSaveConsumer { DisplayConfig.VEHICLE_LOD_DISTANCE.set(it) }
                .setTooltip(Component.translatable("config.superbwarfare.client.display.vehicle_lod_distance.des"))
                .build()
        )

        category.addEntry(
            entryBuilder
                .startIntSlider(
                    Component.translatable("config.superbwarfare.client.display.weapon_hud_x_offset"),
                    DisplayConfig.WEAPON_HUD_X_OFFSET.get(),
                    -1000,
                    1000
                )
                .setDefaultValue(0)
                .setSaveConsumer { v ->
                    DisplayConfig.WEAPON_HUD_X_OFFSET.set(v)
                    DisplayConfig.WEAPON_HUD_X_OFFSET.save()
                }
                .setTooltip(Component.translatable("config.superbwarfare.client.display.weapon_hud_x_offset.des"))
                .build()
        )

        category.addEntry(
            entryBuilder
                .startIntSlider(
                    Component.translatable("config.superbwarfare.client.display.weapon_hud_y_offset"),
                    DisplayConfig.WEAPON_HUD_Y_OFFSET.get(),
                    -1000,
                    1000
                )
                .setDefaultValue(0)
                .setSaveConsumer { v ->
                    DisplayConfig.WEAPON_HUD_Y_OFFSET.set(v)
                    DisplayConfig.WEAPON_HUD_Y_OFFSET.save()
                }
                .setTooltip(Component.translatable("config.superbwarfare.client.display.weapon_hud_y_offset.des"))
                .build()
        )

        category.addEntry(
            entryBuilder
                .startBooleanToggle(
                    Component.translatable("config.superbwarfare.client.display.enable_heat_bar_hud"),
                    DisplayConfig.ENABLE_HEAT_BAR_HUD.get()
                )
                .setDefaultValue(true)
                .setSaveConsumer { v ->
                    DisplayConfig.ENABLE_HEAT_BAR_HUD.set(v)
                    DisplayConfig.ENABLE_HEAT_BAR_HUD.save()
                }
                .setTooltip(Component.translatable("config.superbwarfare.client.display.enable_heat_bar_hud.des"))
                .build()
        )

        category.addEntry(
            entryBuilder
                .startIntSlider(
                    Component.translatable("config.superbwarfare.client.display.heat_bar_hud_x_offset"),
                    DisplayConfig.HEAT_BAR_HUD_X_OFFSET.get(),
                    -1000,
                    1000
                )
                .setDefaultValue(0)
                .setSaveConsumer { v ->
                    DisplayConfig.HEAT_BAR_HUD_X_OFFSET.set(v)
                    DisplayConfig.HEAT_BAR_HUD_X_OFFSET.save()
                }
                .setTooltip(Component.translatable("config.superbwarfare.client.display.heat_bar_hud_x_offset.des"))
                .build()
        )

        category.addEntry(
            entryBuilder
                .startIntSlider(
                    Component.translatable("config.superbwarfare.client.display.heat_bar_hud_y_offset"),
                    DisplayConfig.HEAT_BAR_HUD_Y_OFFSET.get(),
                    -1000,
                    1000
                )
                .setDefaultValue(0)
                .setSaveConsumer { v ->
                    DisplayConfig.HEAT_BAR_HUD_Y_OFFSET.set(v)
                    DisplayConfig.HEAT_BAR_HUD_Y_OFFSET.save()
                }
                .setTooltip(Component.translatable("config.superbwarfare.client.display.heat_bar_hud_y_offset.des"))
                .build()
        )

        category.addEntry(
            entryBuilder
                .startBooleanToggle(
                    Component.translatable("config.superbwarfare.client.display.kill_indication"),
                    DisplayConfig.KILL_INDICATION.get()
                )
                .setDefaultValue(true)
                .setSaveConsumer { v ->
                    DisplayConfig.KILL_INDICATION.set(v)
                    DisplayConfig.KILL_INDICATION.save()
                }
                .setTooltip(Component.translatable("config.superbwarfare.client.display.kill_indication.des"))
                .build()
        )

        category.addEntry(
            entryBuilder
                .startBooleanToggle(
                    Component.translatable("config.superbwarfare.client.display.ammo_hud"),
                    DisplayConfig.AMMO_HUD.get()
                )
                .setDefaultValue(true)
                .setSaveConsumer { v ->
                    DisplayConfig.AMMO_HUD.set(v)
                    DisplayConfig.AMMO_HUD.save()
                }
                .setTooltip(Component.translatable("config.superbwarfare.client.display.ammo_hud.des"))
                .build()
        )

        category.addEntry(
            entryBuilder
                .startBooleanToggle(
                    Component.translatable("config.superbwarfare.client.display.advanced_ammo_hud"),
                    DisplayConfig.ADVANCED_AMMO_HUD.get()
                )
                .setDefaultValue(true)
                .setSaveConsumer { v ->
                    DisplayConfig.ADVANCED_AMMO_HUD.set(v)
                    DisplayConfig.ADVANCED_AMMO_HUD.save()
                }
                .setTooltip(Component.translatable("config.superbwarfare.client.display.advanced_ammo_hud.des"))
                .build()
        )

        category.addEntry(
            entryBuilder
                .startBooleanToggle(
                    Component.translatable("config.superbwarfare.client.display.vehicle_info"),
                    DisplayConfig.VEHICLE_INFO.get()
                )
                .setDefaultValue(true)
                .setSaveConsumer { v ->
                    DisplayConfig.VEHICLE_INFO.set(v)
                    DisplayConfig.VEHICLE_INFO.save()
                }
                .setTooltip(Component.translatable("config.superbwarfare.client.display.vehicle_info.des"))
                .build()
        )

        category.addEntry(
            entryBuilder
                .startBooleanToggle(
                    Component.translatable("config.superbwarfare.client.display.iff_hud"),
                    DisplayConfig.IFF_HUD.get()
                )
                .setDefaultValue(true)
                .setSaveConsumer { v ->
                    DisplayConfig.IFF_HUD.set(v)
                    DisplayConfig.IFF_HUD.save()
                }
                .setTooltip(Component.translatable("config.superbwarfare.client.display.iff_hud.des"))
                .build()
        )

        category.addEntry(
            entryBuilder
                .startBooleanToggle(
                    Component.translatable("config.superbwarfare.client.display.float_cross_hair"),
                    DisplayConfig.FLOAT_CROSS_HAIR.get()
                )
                .setDefaultValue(true)
                .setSaveConsumer { v ->
                    DisplayConfig.FLOAT_CROSS_HAIR.set(v)
                    DisplayConfig.FLOAT_CROSS_HAIR.save()
                }
                .setTooltip(Component.translatable("config.superbwarfare.client.display.float_cross_hair.des"))
                .build()
        )

        category.addEntry(
            entryBuilder
                .startBooleanToggle(
                    Component.translatable("config.superbwarfare.client.display.camera_rotate"),
                    DisplayConfig.CAMERA_ROTATE.get()
                )
                .setDefaultValue(true)
                .setSaveConsumer { v ->
                    DisplayConfig.CAMERA_ROTATE.set(v)
                    DisplayConfig.CAMERA_ROTATE.save()
                }
                .setTooltip(Component.translatable("config.superbwarfare.client.display.camera_rotate.des"))
                .build()
        )

        category.addEntry(
            entryBuilder
                .startBooleanToggle(
                    Component.translatable("config.superbwarfare.client.display.armor_plate_hud"),
                    DisplayConfig.ARMOR_PLATE_HUD.get()
                )
                .setDefaultValue(true)
                .setSaveConsumer { v ->
                    DisplayConfig.ARMOR_PLATE_HUD.set(v)
                    DisplayConfig.ARMOR_PLATE_HUD.save()
                }
                .setTooltip(Component.translatable("config.superbwarfare.client.display.armor_plate_hud.des"))
                .build()
        )

        category.addEntry(
            entryBuilder
                .startBooleanToggle(
                    Component.translatable("config.superbwarfare.client.display.stamina_hud"),
                    DisplayConfig.STAMINA_HUD.get()
                )
                .setDefaultValue(true)
                .setSaveConsumer { v ->
                    DisplayConfig.STAMINA_HUD.set(v)
                    DisplayConfig.STAMINA_HUD.save()
                }
                .setTooltip(Component.translatable("config.superbwarfare.client.display.stamina_hud.des"))
                .build()
        )

        category.addEntry(
            entryBuilder
                .startBooleanToggle(
                    Component.translatable("config.superbwarfare.client.display.dog_tag_name_visible"),
                    DisplayConfig.DOG_TAG_NAME_VISIBLE.get()
                )
                .setDefaultValue(true)
                .setSaveConsumer { v ->
                    DisplayConfig.DOG_TAG_NAME_VISIBLE.set(v)
                    DisplayConfig.DOG_TAG_NAME_VISIBLE.save()
                }
                .setTooltip(Component.translatable("config.superbwarfare.client.display.dog_tag_name_visible.des"))
                .build()
        )

        category.addEntry(
            entryBuilder
                .startBooleanToggle(
                    Component.translatable("config.superbwarfare.client.display.dog_tag_icon_visible"),
                    DisplayConfig.DOG_TAG_ICON_VISIBLE.get()
                )
                .setDefaultValue(false)
                .setSaveConsumer { v ->
                    DisplayConfig.DOG_TAG_ICON_VISIBLE.set(v)
                    DisplayConfig.DOG_TAG_ICON_VISIBLE.save()
                }
                .setTooltip(Component.translatable("config.superbwarfare.client.display.dog_tag_icon_visible.des"))
                .build()
        )

        category.addEntry(
            entryBuilder
                .startIntSlider(
                    Component.translatable("config.superbwarfare.client.display.weapon_screen_shake"),
                    DisplayConfig.WEAPON_SCREEN_SHAKE.get(),
                    0,
                    100
                )
                .setDefaultValue(100)
                .setSaveConsumer { v ->
                    DisplayConfig.WEAPON_SCREEN_SHAKE.set(v)
                    DisplayConfig.WEAPON_SCREEN_SHAKE.save()
                }
                .setTooltip(Component.translatable("config.superbwarfare.client.display.weapon_screen_shake.des"))
                .build()
        )

        category.addEntry(
            entryBuilder
                .startIntSlider(
                    Component.translatable("config.superbwarfare.client.display.explosion_screen_shake"),
                    DisplayConfig.EXPLOSION_SCREEN_SHAKE.get(),
                    0,
                    100
                )
                .setDefaultValue(100)
                .setSaveConsumer { v ->
                    DisplayConfig.EXPLOSION_SCREEN_SHAKE.set(v)
                    DisplayConfig.EXPLOSION_SCREEN_SHAKE.save()
                }
                .setTooltip(Component.translatable("config.superbwarfare.client.display.explosion_screen_shake.des"))
                .build()
        )

        category.addEntry(
            entryBuilder
                .startIntSlider(
                    Component.translatable("config.superbwarfare.client.display.shock_screen_shake"),
                    DisplayConfig.SHOCK_SCREEN_SHAKE.get(),
                    0,
                    100
                )
                .setDefaultValue(100)
                .setSaveConsumer { v ->
                    DisplayConfig.SHOCK_SCREEN_SHAKE.set(v)
                    DisplayConfig.SHOCK_SCREEN_SHAKE.save()
                }
                .setTooltip(Component.translatable("config.superbwarfare.client.display.shock_screen_shake.des"))
                .build()
        )

        category.addEntry(
            entryBuilder
                .startBooleanToggle(
                    Component.translatable("config.superbwarfare.client.display.enable_fire_flash_light"),
                    DisplayConfig.ENABLE_FIRE_FLASH_LIGHT.get()
                )
                .setDefaultValue(true)
                .setSaveConsumer { DisplayConfig.ENABLE_FIRE_FLASH_LIGHT.set(it) }
                .setTooltip(Component.translatable("config.superbwarfare.client.display.enable_fire_flash_light.des"))
                .build()
        )
    }
}
