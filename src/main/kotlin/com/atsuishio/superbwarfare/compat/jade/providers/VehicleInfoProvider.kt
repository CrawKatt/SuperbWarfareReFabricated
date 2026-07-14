package com.atsuishio.superbwarfare.compat.jade.providers

import com.atsuishio.superbwarfare.Mod.Companion.loc
import com.atsuishio.superbwarfare.compat.jade.elements.WrenchHealthElement
import com.atsuishio.superbwarfare.data.vehicle_skin.VehicleSkin
import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.OwnableEntity
import snownee.jade.api.EntityAccessor
import snownee.jade.api.IEntityComponentProvider
import snownee.jade.api.ITooltip
import snownee.jade.api.config.IPluginConfig
import snownee.jade.util.CommonProxy
import java.util.*

object VehicleInfoProvider : IEntityComponentProvider {
    private val ID = loc("vehicle_info")

    override fun appendTooltip(tooltip: ITooltip, accessor: EntityAccessor, config: IPluginConfig?) {
        // 对EntityHealthProvider的拙劣模仿罢了
        val vehicle = accessor.entity as VehicleEntity
        val health = vehicle.health
        val maxHealth = vehicle.getMaxHealth()
        tooltip.add(WrenchHealthElement(maxHealth, health))

        val uuidString = vehicle.lastDriverUUID
        val uuid = try {
            UUID.fromString(uuidString)
        } catch (_: Exception) {
            null
        }
        if (uuid != null && vehicle !is OwnableEntity) {
            val name = CommonProxy.getLastKnownUsername(uuid) ?: "???"
            tooltip.add(Component.translatable("jade.owner", name))
        }

        val skin = VehicleSkin.getSkin(vehicle)
        if (skin != null && skin.id != "vanilla") {
            tooltip.add(
                Component.translatable("config.jade.plugin_superbwarfare.vehicle_skin", skin.id)
                    .withStyle(ChatFormatting.GRAY)
            )
        }
    }

    override fun getUid(): ResourceLocation {
        return ID
    }

    override fun getDefaultPriority(): Int {
        return -4501
    }
}

