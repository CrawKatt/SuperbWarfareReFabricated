package com.atsuishio.superbwarfare.item.gun.rifle

import com.atsuishio.superbwarfare.data.gun.GunData
import com.atsuishio.superbwarfare.item.gun.GeoGunItemV2
object AK47V2Item : GeoGunItemV2(Properties()) {

    override fun isOpenBolt(data: GunData) = true
    override fun hasBulletInBarrel(data: GunData) = true
}
