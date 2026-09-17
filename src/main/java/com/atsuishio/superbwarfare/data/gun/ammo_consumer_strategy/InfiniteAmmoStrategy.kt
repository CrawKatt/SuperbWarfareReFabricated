package com.atsuishio.superbwarfare.data.gun.ammo_consumer_strategy

import com.atsuishio.superbwarfare.data.gun.AmmoConsumer
import com.atsuishio.superbwarfare.data.gun.AmmoSource
import com.atsuishio.superbwarfare.data.gun.GunData
import net.minecraft.world.entity.Entity
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import com.atsuishio.superbwarfare.capability.api.IItemHandler

/**
 * 无限弹药策略 — ammo 字符串形如 "infinite"、 "infinity"
 */
object InfiniteAmmoStrategy : AmmoConsumeStrategy() {

    override val defaultType = AmmoConsumer.AmmoConsumeType.INFINITE

    override fun match(ammo: String) =
        ammo.equals("infinite", ignoreCase = true) || ammo.equals("infinity", ignoreCase = true)

    override fun consume(data: GunData, source: AmmoSource, shooter: Entity?, count: Int) = 0
    override fun consume(data: GunData, source: AmmoSource, handler: IItemHandler, count: Int) = 0
    override fun count(data: GunData, source: AmmoSource, entity: Entity?) = Int.MAX_VALUE
    override fun count(data: GunData, source: AmmoSource, handler: IItemHandler?) = Int.MAX_VALUE
    override fun withdraw(source: AmmoSource, ammoSupplier: Entity, count: Int) = 0
    override fun withdraw(source: AmmoSource, handler: IItemHandler, count: Int) = 0

    @Environment(EnvType.CLIENT)
    override fun getDisplayName(source: AmmoSource) = "Infinite"
}
