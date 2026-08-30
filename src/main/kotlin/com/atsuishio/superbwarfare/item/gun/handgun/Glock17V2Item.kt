package com.atsuishio.superbwarfare.item.gun.handgun

import com.atsuishio.superbwarfare.data.gun.GunData
import com.atsuishio.superbwarfare.item.gun.GeoGunItemV2
import java.util.function.Consumer

object Glock17V2Item : GeoGunItemV2(Properties()) {
    override fun isOpenBolt(data: GunData) = true

    override fun hasBulletInBarrel(data: GunData) = true

    override fun whenNoAmmo(data: GunData) {
        data.holdOpen.set(true)
    }

    override fun addReloadTimeBehavior(behaviors: MutableMap<Int, Consumer<GunData>?>?) {
        super.addReloadTimeBehavior(behaviors)

        behaviors?.set(9, Consumer { data: GunData -> data.holdOpen.set(false) })
    }
}
