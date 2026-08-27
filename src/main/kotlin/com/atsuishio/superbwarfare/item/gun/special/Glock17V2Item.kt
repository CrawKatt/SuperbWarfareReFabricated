package com.atsuishio.superbwarfare.item.gun.special

import com.atsuishio.superbwarfare.client.renderer.gun.GeoGunRenderer
import com.atsuishio.superbwarfare.data.gun.GunData
import com.atsuishio.superbwarfare.item.gun.GunItem
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import net.minecraftforge.client.extensions.common.IClientItemExtensions
import java.util.function.Consumer

object Glock17V2Item : GunItem(Properties()) {

    @OnlyIn(Dist.CLIENT)
    override fun initializeClient(consumer: Consumer<IClientItemExtensions>) {
        super.initializeClient(consumer)
        consumer.accept(object : IClientItemExtensions {
            private val renderer by lazy { GeoGunRenderer() }

            override fun getCustomRenderer() = renderer
        })
    }

    override fun isOpenBolt(data: GunData) = true


    override fun hasBulletInBarrel(data: GunData) = true

    override fun whenNoAmmo(data: GunData) {
        data.holdOpen.set(true)
    }

    override fun addReloadTimeBehavior(behaviors: MutableMap<Int, Consumer<GunData>?>?) {
        super.addReloadTimeBehavior(behaviors)

        behaviors?.set(9, Consumer { data: GunData ->
            data.holdOpen.set(
                false
            )
        })
    }
}
