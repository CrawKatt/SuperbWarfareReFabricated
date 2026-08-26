package com.atsuishio.superbwarfare.item.weapon

import com.atsuishio.superbwarfare.client.renderer.item.MilitaryShovelRenderer
import com.atsuishio.superbwarfare.init.ModTags
import com.atsuishio.superbwarfare.tiers.ModItemTier
import com.atsuishio.superbwarfare.tools.mc
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.ChatFormatting
import net.minecraft.advancements.CriteriaTriggers
import net.minecraft.core.Direction
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.tags.BlockTags
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.*
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.CampfireBlock
import net.minecraft.world.level.block.LevelEvent
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.gameevent.GameEvent
import software.bernie.geckolib.animatable.GeoItem
import software.bernie.geckolib.animatable.client.RenderProvider
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache
import software.bernie.geckolib.core.animation.AnimatableManager
import software.bernie.geckolib.util.GeckoLibUtil
import java.util.function.Consumer
import java.util.function.Supplier

open class MilitaryShovelItem :
    AxeItem(
        ModItemTier.CEMENTED_CARBIDE, 2f, -2.6f,
        Properties().rarity(Rarity.RARE).durability(810)
    ), GeoItem {
    private val cache: AnimatableInstanceCache = GeckoLibUtil.createInstanceCache(this)
    private val renderProvider: Supplier<Any> = GeoItem.makeRenderer(this)

    override fun appendHoverText(
        pStack: ItemStack,
        pLevel: Level?,
        pTooltipComponents: MutableList<Component>,
        pIsAdvanced: TooltipFlag
    ) {
        pTooltipComponents.add(
            Component.translatable("des.superbwarfare.military_shovel").withStyle(ChatFormatting.GRAY)
        )
    }

    override fun getDestroySpeed(stack: ItemStack, state: BlockState): Float {
        val speed = if (state.`is`(BlockTags.MINEABLE_WITH_SHOVEL)
            || state.`is`(BlockTags.MINEABLE_WITH_AXE)
            || state.`is`(BlockTags.MINEABLE_WITH_HOE)
        ) this.speed else 1f
        return speed * (if (state.`is`(Blocks.COBWEB)) 3 else 1)
    }

    @Deprecated("Deprecated in Java")
    override fun isCorrectToolForDrops(state: BlockState): Boolean {
        return state.`is`(Blocks.COBWEB) || state.`is`(BlockTags.MINEABLE_WITH_SHOVEL)
            || state.`is`(BlockTags.MINEABLE_WITH_AXE) || state.`is`(BlockTags.MINEABLE_WITH_HOE)
            || state.`is`(ModTags.Blocks.MINEABLE_WITH_MILITARY_SHOVEL)
    }

    /**
     * Code Based on Mekanism-Tools
     */
    override fun useOn(context: UseOnContext): InteractionResult {
        val axeResult = super.useOn(context)
        if (axeResult != InteractionResult.PASS) {
            return axeResult
        }

        val level = context.level
        val blockpos = context.clickedPos
        val player = context.player ?: return InteractionResult.PASS

        val blockstate = level.getBlockState(blockpos)
        var resultToSet: BlockState? = null

        if (player.isShiftKeyDown) {
            val tillable = HoeItem.TILLABLES[blockstate.block] ?: return InteractionResult.PASS
            if (!tillable.first.test(context)) return InteractionResult.PASS

            level.playSound(player, blockpos, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0f, 1.0f)
            if (!level.isClientSide) {
                tillable.second.accept(context)
            }
        } else {
            if (context.clickedFace == Direction.DOWN) {
                return InteractionResult.PASS
            }
            val shovelRes = ShovelItem.FLATTENABLES[blockstate.block]
            if (shovelRes != null && level.isEmptyBlock(blockpos.above())) {
                level.playSound(player, blockpos, SoundEvents.SHOVEL_FLATTEN, SoundSource.BLOCKS, 1.0f, 1.0f)
                resultToSet = shovelRes
            } else if (blockstate.block is CampfireBlock && blockstate.getValue(CampfireBlock.LIT)) {
                if (!level.isClientSide) {
                    level.levelEvent(null, LevelEvent.SOUND_EXTINGUISH_FIRE, blockpos, 0)
                }
                CampfireBlock.dowse(player, level, blockpos, blockstate)
                resultToSet = blockstate.setValue(CampfireBlock.LIT, false)
            }
            if (resultToSet == null) {
                return InteractionResult.PASS
            }
            if (!level.isClientSide) {
                val stack = context.itemInHand
                if (player is ServerPlayer) {
                    CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(player, blockpos, stack)
                }
                level.setBlock(blockpos, resultToSet, Block.UPDATE_ALL_IMMEDIATE)
                stack.hurtAndBreak(1, player) { it.broadcastBreakEvent(context.hand) }
            }
        }

        return InteractionResult.sidedSuccess(level.isClientSide)
    }

    @Environment(EnvType.CLIENT)
    override fun createRenderer(consumer: Consumer<Any>) {
        consumer.accept(object : RenderProvider {
            private var renderer: net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer? = null

            override fun getCustomRenderer(): net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer {
                if (renderer == null) renderer = MilitaryShovelRenderer(mc.blockEntityRenderDispatcher, mc.entityModels)
                return renderer!!
            }
        })
    }

    override fun getRenderProvider(): Supplier<Any> = renderProvider

    override fun registerControllers(data: AnimatableManager.ControllerRegistrar) = Unit

    override fun getAnimatableInstanceCache() = this.cache
}
