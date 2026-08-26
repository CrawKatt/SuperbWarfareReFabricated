package com.atsuishio.superbwarfare.item.gun.special

import com.atsuishio.superbwarfare.client.renderer.gun.GeoGunRenderer
import com.atsuishio.superbwarfare.init.ModRarities
import com.atsuishio.superbwarfare.item.gun.GunItem
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import net.minecraftforge.client.extensions.common.IClientItemExtensions
import java.util.function.Consumer

object ReforgingItem : GunItem(Properties().rarity(ModRarities.SUPERB)) {

    @OnlyIn(Dist.CLIENT)
    override fun initializeClient(consumer: Consumer<IClientItemExtensions>) {
        super.initializeClient(consumer)
        consumer.accept(object : IClientItemExtensions {
            private val renderer by lazy { GeoGunRenderer() }

            override fun getCustomRenderer() = renderer
        })
    }
}