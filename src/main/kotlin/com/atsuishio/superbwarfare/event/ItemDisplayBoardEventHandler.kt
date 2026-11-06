package com.atsuishio.superbwarfare.event

import com.atsuishio.superbwarfare.block.ItemDisplayBoardBlock
import com.atsuishio.superbwarfare.block.entity.ItemDisplayBoardBlockEntity
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level

/**
 * 物品展示板的潜行右键取下物品逻辑。
 *
 * 强制放行潜行时的方块交互，并阻止手持物品抢先执行 onItemUseFirst。
 * 取下操作由方块自身完成，空手与持物共用同一条客户端预测/服务端执行路径。
 */
object ItemDisplayBoardEventHandler {

    @JvmStatic
    fun onRightClickBlock(player: Player, level: Level, hitResult: BlockHitResult): InteractionResult {
        val pos = hitResult.blockPos
        val state = level.getBlockState(pos)
        if (state.block !is ItemDisplayBoardBlock) return InteractionResult.PASS

        if (player.isSpectator) return InteractionResult.PASS
        if (!player.isShiftKeyDown) return InteractionResult.PASS

        val blockEntity = level.getBlockEntity(pos) as? ItemDisplayBoardBlockEntity ?: return InteractionResult.PASS
        if (!blockEntity.hasItem) return InteractionResult.PASS

        return state.useWithoutItem(level, player, hitResult)
    }
}
