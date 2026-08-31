package com.atsuishio.superbwarfare.item.gun.rifle

import com.atsuishio.superbwarfare.data.gun.GunData
import com.atsuishio.superbwarfare.item.gun.GeoGunItemV2
import java.util.function.Consumer

object MarlinItem : GeoGunItemV2(Properties()) {

    override fun whenNoAmmo(data: GunData) {
        data.closeStrike.set(true)
    }

    override fun addReloadTimeBehavior(behaviors: MutableMap<Int, Consumer<GunData>?>?) {
        super.addReloadTimeBehavior(behaviors)

        behaviors?.set(7, Consumer { data: GunData ->
            data.closeStrike.set(
                false
            )
        })
    }
}


