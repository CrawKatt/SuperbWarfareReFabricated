package com.atsuishio.superbwarfare.item.projectile

import com.atsuishio.superbwarfare.client.renderer.item.Ptkm1rItemRenderer
import com.atsuishio.superbwarfare.entity.projectile.Ptkm1rEntity
import com.atsuishio.superbwarfare.item.misc.AbstractDeployerItem
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Rarity
import net.minecraft.world.level.Level
import software.bernie.geckolib.animatable.GeoItem
import software.bernie.geckolib.animatable.client.RenderProvider
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache
import software.bernie.geckolib.core.animation.AnimatableManager
import software.bernie.geckolib.util.GeckoLibUtil
import java.util.function.Consumer
import java.util.function.Supplier

open class Ptkm1rItem : AbstractDeployerItem(Properties().rarity(Rarity.RARE).stacksTo(2)), GeoItem {
    private val cache: AnimatableInstanceCache = GeckoLibUtil.createInstanceCache(this)
    private val renderProvider: Supplier<Any> = GeoItem.makeRenderer(this)

    @Environment(EnvType.CLIENT)
    override fun createRenderer(consumer: Consumer<Any>) {
        consumer.accept(object : RenderProvider {
            private var renderer: BlockEntityWithoutLevelRenderer? = null

            override fun getCustomRenderer(): BlockEntityWithoutLevelRenderer {
                if (this.renderer == null) {
                    this.renderer = Ptkm1rItemRenderer()
                }
                return this.renderer!!
            }
        })
    }

    override fun getRenderProvider(): Supplier<Any> = renderProvider

    override fun registerControllers(data: AnimatableManager.ControllerRegistrar) {
    }

    override fun getAnimatableInstanceCache(): AnimatableInstanceCache {
        return this.cache
    }

    override fun spawnDeployedEntity(level: Level, player: Player): Entity {
        return Ptkm1rEntity(player, level)
    }
}
