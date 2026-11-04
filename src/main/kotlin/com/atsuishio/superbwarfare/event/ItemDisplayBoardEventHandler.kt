package com.atsuishio.superbwarfare.event

import com.atsuishio.superbwarfare.block.ItemDisplayBoardBlock
import com.atsuishio.superbwarfare.block.entity.ItemDisplayBoardBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level

/**
 * 物品展示板的潜行右键取下物品逻辑。
 *
 * 原版在潜行且手中持有物品时会跳过方块自身的 use()（潜行放置方块用），
 * 因此这里提前拦截右键事件，保证手持任意物品时都能取下展示的物品。
 */
object ItemDisplayBoardEventHandler {

    fun onRightClickBlock(player: Player, level: Level, pos: BlockPos): InteractionResult {
        val state = level.getBlockState(pos)
        if (state.block !is ItemDisplayBoardBlock) return InteractionResult.PASS

        if (player.isSpectator) return InteractionResult.PASS
        if (!player.isShiftKeyDown) return InteractionResult.PASS

        val blockEntity = level.getBlockEntity(pos) as? ItemDisplayBoardBlockEntity ?: return InteractionResult.PASS
        if (blockEntity.item.isEmpty) return InteractionResult.PASS

        if (level.isClientSide) return InteractionResult.SUCCESS

        ItemDisplayBoardBlock.takeDisplayedItem(level, pos, state, player)
        return InteractionResult.SUCCESS
    }
}
