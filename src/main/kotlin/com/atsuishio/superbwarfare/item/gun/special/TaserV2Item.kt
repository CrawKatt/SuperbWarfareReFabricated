package com.atsuishio.superbwarfare.item.gun.special

import com.atsuishio.superbwarfare.client.renderer.gun.GeoGunRenderer
import com.atsuishio.superbwarfare.item.gun.GunItem
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import net.minecraftforge.client.extensions.common.IClientItemExtensions
import java.util.function.Consumer

class TaserV2Item : GunItem(Properties()) {

    @OnlyIn(Dist.CLIENT)
    override fun initializeClient(consumer: Consumer<IClientItemExtensions>) {
        super.initializeClient(consumer)
        consumer.accept(object : IClientItemExtensions {
            private var renderer: GeoGunRenderer? = null

            override fun getCustomRenderer(): BlockEntityWithoutLevelRenderer {
                if (renderer == null) {
                    renderer = GeoGunRenderer()
                }
                return renderer!!
            }
        })
    }
}
