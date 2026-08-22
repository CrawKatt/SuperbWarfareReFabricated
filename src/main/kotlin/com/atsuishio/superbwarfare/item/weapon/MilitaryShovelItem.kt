package com.atsuishio.superbwarfare.item.weapon

import com.atsuishio.superbwarfare.init.ModTags
import com.atsuishio.superbwarfare.item.CustomDamageProperty
import com.atsuishio.superbwarfare.tiers.ModItemTier
import net.minecraft.ChatFormatting
import net.minecraft.advancements.CriteriaTriggers
import net.minecraft.core.Direction
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.*
import net.minecraft.world.item.component.Tool
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.block.CampfireBlock
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.LevelEvent
import net.minecraft.world.level.block.RotatedPillarBlock
import net.minecraft.world.level.block.WeatheringCopper
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.gameevent.GameEvent

open class MilitaryShovelItem :
    AxeItem(
        ModItemTier.CEMENTED_CARBIDE,
        CustomDamageProperty(810).rarity(Rarity.RARE)
            .component(
                DataComponents.TOOL, Tool(
                    listOf(
                        Tool.Rule.deniesDrops(ModItemTier.CEMENTED_CARBIDE.incorrectBlocksForDrops),
                        Tool.Rule.minesAndDrops(
                            ModTags.Blocks.MINEABLE_WITH_MILITARY_SHOVEL,
                            ModItemTier.CEMENTED_CARBIDE.speed
                        )
                    ),
                    1f, 1
                )
            )
            .attributes(createAttributes(ModItemTier.CEMENTED_CARBIDE, 2f, -2.6f))
    ) {

    override fun appendHoverText(
        stack: ItemStack,
        context: TooltipContext,
        tooltipComponents: MutableList<Component>,
        tooltipFlag: TooltipFlag
    ) {
        tooltipComponents.add(
            Component.translatable("des.superbwarfare.military_shovel").withStyle(ChatFormatting.GRAY)
        )
    }

    /**
     * Code Based on Mekanism-Tools
     */
    override fun useOn(context: UseOnContext): InteractionResult {
        val level = context.level
        val blockpos = context.clickedPos
        val player = context.player ?: return InteractionResult.PASS

        val blockstate = level.getBlockState(blockpos)
        var resultToSet = getAxeResult(blockstate, context)

        if (resultToSet == null) {
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
                val foundResult = ShovelItem.FLATTENABLES[blockstate.block]
                if (foundResult != null && level.isEmptyBlock(blockpos.above())) {
                    level.playSound(player, blockpos, SoundEvents.SHOVEL_FLATTEN, SoundSource.BLOCKS, 1.0F, 1.0F)
                    resultToSet = foundResult
                } else {
                    if (blockstate.block is CampfireBlock && blockstate.getValue(CampfireBlock.LIT)) {
                        if (!level.isClientSide) {
                            CampfireBlock.dowse(player, level, blockpos, blockstate)
                            level.levelEvent(null, LevelEvent.SOUND_EXTINGUISH_FIRE, blockpos, 0)
                        }
                        resultToSet = blockstate.setValue(CampfireBlock.LIT, false)
                    }
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
                    level.gameEvent(GameEvent.BLOCK_CHANGE, blockpos, GameEvent.Context.of(player, resultToSet))
                    stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(context.hand))
                }
            }
        }

        return InteractionResult.sidedSuccess(level.isClientSide)
    }

    private fun getAxeResult(state: BlockState, context: UseOnContext): BlockState? {
        val level = context.level
        val pos = context.clickedPos
        val player = context.player

        val stripped = AxeItem.STRIPPABLES[state.block]?.defaultBlockState()
            ?.setValue(RotatedPillarBlock.AXIS, state.getValue(RotatedPillarBlock.AXIS))
        if (stripped != null) {
            level.playSound(player, pos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0F, 1.0F)
            return stripped
        }

        val previous = WeatheringCopper.getPrevious(state)
        if (previous.isPresent) {
            level.playSound(player, pos, SoundEvents.AXE_SCRAPE, SoundSource.BLOCKS, 1.0F, 1.0F)
            level.levelEvent(player, LevelEvent.PARTICLES_SCRAPE, pos, 0)
            return previous.get()
        }

        val waxedOff = HoneycombItem.WAX_OFF_BY_BLOCK.get()[state.block]?.withPropertiesOf(state)
        if (waxedOff != null) {
            level.playSound(player, pos, SoundEvents.AXE_WAX_OFF, SoundSource.BLOCKS, 1.0F, 1.0F)
            level.levelEvent(player, LevelEvent.PARTICLES_WAX_OFF, pos, 0)
            return waxedOff
        }
        return null
    }

    override fun getEnchantmentValue(): Int {
        return ModItemTier.CEMENTED_CARBIDE.enchantmentValue
    }

}
