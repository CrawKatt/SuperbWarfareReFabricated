package com.atsuishio.superbwarfare.item.projectile

import com.atsuishio.superbwarfare.client.renderer.item.Tm62ItemRenderer
import com.atsuishio.superbwarfare.entity.projectile.Tm62Entity
import com.atsuishio.superbwarfare.init.ModEntities
import com.atsuishio.superbwarfare.item.DispenserLaunchable
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.core.Direction
import net.minecraft.core.Position
import net.minecraft.core.dispenser.BlockSource
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior
import net.minecraft.core.dispenser.DispenseItemBehavior
import net.minecraft.util.Mth
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.DispenserBlock
import org.joml.Math
import software.bernie.geckolib.animatable.GeoItem
import software.bernie.geckolib.animatable.client.GeoRenderProvider
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache
import software.bernie.geckolib.animation.AnimatableManager
import software.bernie.geckolib.renderer.GeoItemRenderer
import software.bernie.geckolib.util.GeckoLibUtil
import java.util.function.Consumer

open class Tm62Item : Item(Properties().stacksTo(8)), GeoItem, DispenserLaunchable {
    private val cache: AnimatableInstanceCache = GeckoLibUtil.createInstanceCache(this)

    @Environment(EnvType.CLIENT)
    override fun createGeoRenderer(consumer: Consumer<GeoRenderProvider>) {
        consumer.accept(object : GeoRenderProvider {
            private var renderer: Tm62ItemRenderer? = null

            override fun getGeoItemRenderer(): GeoItemRenderer<*> {
                if (this.renderer == null) {
                    this.renderer = Tm62ItemRenderer()
                }
                return this.renderer!!
            }
        })
    }

    override fun registerControllers(data: AnimatableManager.ControllerRegistrar) {
    }

    override fun getAnimatableInstanceCache(): AnimatableInstanceCache {
        return this.cache
    }

    override fun use(level: Level, player: Player, hand: InteractionHand): InteractionResultHolder<ItemStack> {
        val stack = player.getItemInHand(hand)

        if (!level.isClientSide) {
            val randomRot = Mth.clamp((2 * Math.random() - 1) * 180, -180.0, 180.0).toFloat()
            val entity = Tm62Entity(player, level, player.isShiftKeyDown)
            entity.moveTo(player.x, player.y + 1.1, player.z, randomRot, 0f)
            entity.setYBodyRot(randomRot)
            entity.setYHeadRot(randomRot)
            entity.setDeltaMovement(
                0.5 * player.lookAngle.x,
                0.5 * player.lookAngle.y,
                0.5 * player.lookAngle.z
            )

            level.addFreshEntity(entity)
        }

        player.cooldowns.addCooldown(this, 20)

        if (!player.abilities.instabuild) {
            stack.shrink(1)
        }

        return InteractionResultHolder.success(stack)
    }

    override fun getLaunchBehavior(): DispenseItemBehavior {
        return Tm62DispenseBehavior()
    }

    class Tm62DispenseBehavior : DefaultDispenseItemBehavior() {
        public override fun execute(blockSource: BlockSource, stack: ItemStack): ItemStack {
            val level: Level = blockSource.level()
            val position: Position = DispenserBlock.getDispensePosition(blockSource)
            val direction: Direction = blockSource.state().getValue(DispenserBlock.FACING)

            val tm62 = Tm62Entity(ModEntities.TM_62, level)
            tm62.setPos(position.x(), position.y(), position.z())

            val randomRot = Mth.clamp((2 * Math.random() - 1) * 180, -180.0, 180.0).toFloat()

            val pX = direction.stepX
            val pY = direction.stepY
            val pZ = direction.stepZ

            tm62.shoot(pX.toDouble(), pY.toDouble(), pZ.toDouble(), 0.2f, 25f)
            tm62.yRot = randomRot
            tm62.yRotO = tm62.yRot

            level.addFreshEntity(tm62)
            stack.shrink(1)
            return stack
        }
    }
}