package com.atsuishio.superbwarfare.item.gun.special

import com.atsuishio.superbwarfare.data.gun.GunData
import com.atsuishio.superbwarfare.item.gun.GunItem
import net.minecraft.world.item.Item.Properties
import java.util.function.Consumer

object Glock17V2Item : GunItem(Properties()) {
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
