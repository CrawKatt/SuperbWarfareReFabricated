package com.atsuishio.superbwarfare.compat.clothconfig.common

import com.atsuishio.superbwarfare.config.common.GameplayConfig
import me.shedaniel.clothconfig2.api.ConfigBuilder
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder
import net.minecraft.network.chat.Component

object GameplayClothConfig {
    fun init(root: ConfigBuilder, entryBuilder: ConfigEntryBuilder) {
        val category = root.getOrCreateCategory(Component.translatable("config.superbwarfare.common.gameplay"))

        category.addEntry(
            entryBuilder
                .startBooleanToggle(
                    Component.translatable("config.superbwarfare.common.gameplay.respawn_reload"),
                    GameplayConfig.RESPAWN_RELOAD.get()
                )
                .setDefaultValue(true)
                .setSaveConsumer { v ->
                    GameplayConfig.RESPAWN_RELOAD.set(v)
                    GameplayConfig.RESPAWN_RELOAD.save()
                }
                .setTooltip(Component.translatable("config.superbwarfare.common.gameplay.respawn_reload.des"))
                .build()
        )

        category.addEntry(
            entryBuilder
                .startBooleanToggle(
                    Component.translatable("config.superbwarfare.common.gameplay.global_indication"),
                    GameplayConfig.GLOBAL_INDICATION.get()
                )
                .setDefaultValue(false)
                .setSaveConsumer { v ->
                    GameplayConfig.GLOBAL_INDICATION.set(v)
                    GameplayConfig.GLOBAL_INDICATION.save()
                }
                .setTooltip(Component.translatable("config.superbwarfare.common.gameplay.global_indication.des"))
                .build()
        )

        category.addEntry(
            entryBuilder
                .startBooleanToggle(
                    Component.translatable("config.superbwarfare.common.gameplay.respawn_auto_armor"),
                    GameplayConfig.RESPAWN_AUTO_ARMOR.get()
                )
                .setDefaultValue(true)
                .setSaveConsumer { v ->
                    GameplayConfig.RESPAWN_AUTO_ARMOR.set(v)
                    GameplayConfig.RESPAWN_AUTO_ARMOR.save()
                }
                .setTooltip(Component.translatable("config.superbwarfare.common.gameplay.respawn_auto_armor.des"))
                .build()
        )
    }
}