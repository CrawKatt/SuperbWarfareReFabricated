package com.atsuishio.superbwarfare.data.gun.ammo_consumer_strategy

import com.atsuishio.superbwarfare.data.gun.AmmoConsumer
import com.atsuishio.superbwarfare.data.gun.AmmoSource
import com.atsuishio.superbwarfare.data.gun.GunData
import net.minecraft.world.entity.Entity
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import net.minecraftforge.common.capabilities.ForgeCapabilities
import net.minecraftforge.items.IItemHandler

/**
 * 能量弹药策略 — ammo 字符串形如 "fe"、 "rf"、 "energy"
 *
 * 作为附加来源时（如泰瑟枪的 `"400 fe"`），前缀即每发消耗的 FE 数量。
 */
object EnergyAmmoStrategy : AmmoConsumeStrategy() {

    override val defaultType = AmmoConsumer.AmmoConsumeType.ENERGY

    override fun match(ammo: String) = ammo.lowercase() in setOf("fe", "rf", "energy")

    override fun consume(data: GunData, source: AmmoSource, shooter: Entity?, count: Int): Int {
        return data.getEnergyProvider(shooter).map { it.extractEnergy(count, false) }.orElseGet { 0 }
    }

    override fun consume(data: GunData, source: AmmoSource, handler: IItemHandler, count: Int): Int {
        return data.stack.getCapability(ForgeCapabilities.ENERGY).map { it.extractEnergy(count, false) }.orElseGet { 0 }
    }

    override fun count(data: GunData, source: AmmoSource, entity: Entity?): Int {
        if (entity == null) return 0
        return data.getEnergyProvider(entity).map { it.energyStored }.orElseGet { 0 }
    }

    override fun count(data: GunData, source: AmmoSource, handler: IItemHandler?): Int {
        if (handler == null) return 0
        return data.stack.getCapability(ForgeCapabilities.ENERGY).map { it.energyStored }.orElseGet { 0 }
    }

    override fun withdraw(source: AmmoSource, ammoSupplier: Entity, count: Int) = 0
    override fun withdraw(source: AmmoSource, handler: IItemHandler, count: Int) = 0

    @OnlyIn(Dist.CLIENT)
    override fun getDisplayName(source: AmmoSource) = "Energy"
}
