package com.atsuishio.superbwarfare.item.container

import com.atsuishio.superbwarfare.Mod.loc
import com.atsuishio.superbwarfare.client.renderer.item.LuckyContainerBlockItemRenderer
import com.atsuishio.superbwarfare.init.ModBlockEntities
import com.atsuishio.superbwarfare.init.ModBlocks
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.nbt.CompoundTag
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.DamageTypeTags
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.damagesource.DamageTypes
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Rarity
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.ClipContext
import net.minecraft.world.level.Level
import net.minecraft.world.phys.HitResult
import software.bernie.geckolib.animatable.GeoItem
import software.bernie.geckolib.animatable.client.GeoRenderProvider
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache
import software.bernie.geckolib.animation.AnimatableManager
import software.bernie.geckolib.animation.AnimationController
import software.bernie.geckolib.animation.AnimationState
import software.bernie.geckolib.animation.PlayState
import software.bernie.geckolib.renderer.GeoItemRenderer
import software.bernie.geckolib.util.GeckoLibUtil
import java.util.function.Consumer

class LuckyContainerBlockItem :
    BlockItem(ModBlocks.LUCKY_CONTAINER, Properties().stacksTo(1).rarity(Rarity.EPIC).fireResistant()), GeoItem {
    private val cache: AnimatableInstanceCache = GeckoLibUtil.createInstanceCache(this)

    override fun useOn(context: UseOnContext): InteractionResult {
        return InteractionResult.PASS
    }

    override fun use(level: Level, player: Player, hand: InteractionHand): InteractionResultHolder<ItemStack> {
        val playerPOVHitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.ANY)
        if (playerPOVHitResult.type == HitResult.Type.MISS) {
            return super.use(level, player, hand)
        }
        val blockHitResult = playerPOVHitResult.withPosition(playerPOVHitResult.blockPos.above())
        val interactionResult = super.useOn(UseOnContext(player, hand, blockHitResult))
        return InteractionResultHolder(interactionResult, player.getItemInHand(hand))
    }

    private fun predicate(event: AnimationState<LuckyContainerBlockItem>): PlayState {
        return PlayState.CONTINUE
    }

    @Environment(EnvType.CLIENT)
    override fun createGeoRenderer(consumer: Consumer<GeoRenderProvider>) {
        consumer.accept(object : GeoRenderProvider {
            private var renderer: LuckyContainerBlockItemRenderer? = null

            override fun getGeoItemRenderer(): GeoItemRenderer<*> {
                if (this.renderer == null) {
                    this.renderer = LuckyContainerBlockItemRenderer()
                }
                return this.renderer!!
            }
        })
    }

    override fun registerControllers(data: AnimatableManager.ControllerRegistrar) {
        data.add(
            AnimationController(this, "controller", 0) { this.predicate(it) }
        )
    }

    override fun getAnimatableInstanceCache(): AnimatableInstanceCache {
        return this.cache
    }

    companion object {
        @JvmField
        val LUCKY_CONTAINERS: MutableList<() -> ItemStack> = mutableListOf(
            { createInstance(loc("mobile_vehicles"), loc("textures/gui/vehicle/type/civilian.png")) },
            { createInstance(loc("land_vehicles"), loc("textures/gui/vehicle/type/land.png")) },
            { createInstance(loc("aircraft"), loc("textures/gui/vehicle/type/aircraft.png")) },
            { createInstance(loc("controllable_turrets"), loc("textures/gui/vehicle/type/defense.png")) }
        )

        @JvmOverloads
        fun createInstance(location: ResourceLocation, icon: ResourceLocation? = null): ItemStack {
            val stack = ItemStack(ModBlocks.LUCKY_CONTAINER)
            val tag = CompoundTag()
            tag.putString("Location", location.toString())
            if (icon != null) {
                tag.putString("Icon", icon.toString())
            }
            setBlockEntityData(stack, ModBlockEntities.LUCKY_CONTAINER, tag)
            return stack
        }
    }
}
