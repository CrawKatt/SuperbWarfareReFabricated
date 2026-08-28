package com.atsuishio.superbwarfare.block

import com.atsuishio.superbwarfare.block.entity.CreativeChargingStationBlockEntity
import com.atsuishio.superbwarfare.capability.energy.EnergyStorageHelper
import com.atsuishio.superbwarfare.init.ModBlockEntities
import com.atsuishio.superbwarfare.init.ModCapabilities
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext
import net.minecraft.ChatFormatting
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.*
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.DirectionProperty
import net.minecraft.world.phys.BlockHitResult

@Suppress("OVERRIDE_DEPRECATION", "DEPRECATION")
class CreativeChargingStationBlock :
    BaseEntityBlock(Properties.of().sound(SoundType.METAL).strength(3.0f).requiresCorrectToolForDrops()) {
    init {
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH))
    }

    override fun appendHoverText(
        pStack: ItemStack,
        pLevel: BlockGetter?,
        pTooltip: MutableList<Component?>,
        pFlag: TooltipFlag
    ) {
        pTooltip.add(
            Component.translatable("des.superbwarfare.creative_charging_station").withStyle(ChatFormatting.GRAY)
        )
    }

    override fun use(
        pState: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hand: InteractionHand,
        pHit: BlockHitResult
    ): InteractionResult {
        val stack = player.getItemInHand(hand)
        val energy = ContainerItemContext.forPlayerInteraction(player, hand).find(ModCapabilities.ENERGY_ITEM)
            ?: return InteractionResult.FAIL
        if (energy.supportsInsertion() && energy.amount < energy.capacity) {
            EnergyStorageHelper.insert(energy, Long.MAX_VALUE)
            if (!level.isClientSide) {
                player.displayClientMessage(
                    Component.translatable("des.superbwarfare.creative_charging_station.charge.success")
                        .withStyle(ChatFormatting.GREEN), true
                )
            }
            return InteractionResult.SUCCESS
        } else if (energy.supportsExtraction()) {
            EnergyStorageHelper.extract(energy, Long.MAX_VALUE)
            if (!level.isClientSide) {
                player.displayClientMessage(
                    Component.translatable("des.superbwarfare.creative_charging_station.extract.success")
                        .withStyle(ChatFormatting.GREEN), true
                )
            }
            return InteractionResult.SUCCESS
        } else {
            if (!level.isClientSide) {
                player.displayClientMessage(
                    Component.translatable("des.superbwarfare.creative_charging_station.fail")
                        .withStyle(ChatFormatting.RED), true
                )
            }
            return InteractionResult.FAIL
        }
    }

    override fun getRenderShape(pState: BlockState): RenderShape {
        return RenderShape.MODEL
    }

    override fun newBlockEntity(pPos: BlockPos, pState: BlockState): BlockEntity {
        return CreativeChargingStationBlockEntity(pPos, pState)
    }

    override fun <T : BlockEntity> getTicker(
        pLevel: Level,
        pState: BlockState,
        pBlockEntityType: BlockEntityType<T>
    ): BlockEntityTicker<T>? {
        if (!pLevel.isClientSide) {
            return createTickerHelper<CreativeChargingStationBlockEntity, T>(
                pBlockEntityType,
                ModBlockEntities.CREATIVE_CHARGING_STATION
            ) { _, _, _, blockEntity -> CreativeChargingStationBlockEntity.serverTick(blockEntity) }
        }
        return null
    }

    override fun onRemove(
        pState: BlockState,
        pLevel: Level,
        pPos: BlockPos,
        pNewState: BlockState,
        pMovedByPiston: Boolean
    ) {
        if (!pState.`is`(pNewState.block)) {
            val blockEntity = pLevel.getBlockEntity(pPos)
            if (blockEntity is CreativeChargingStationBlockEntity) {
                pLevel.updateNeighbourForOutputSignal(pPos, this)
            }
        }

        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston)
    }

    override fun createBlockStateDefinition(pBuilder: StateDefinition.Builder<Block?, BlockState?>) {
        pBuilder.add(FACING)
    }

    override fun getStateForPlacement(pContext: BlockPlaceContext): BlockState? {
        return this.defaultBlockState()
            .setValue(FACING, pContext.horizontalDirection.opposite)
    }

    override fun hasAnalogOutputSignal(pState: BlockState): Boolean {
        return true
    }

    override fun getAnalogOutputSignal(
        pState: BlockState,
        pLevel: Level,
        pPos: BlockPos
    ): Int {
        return 15
    }

    companion object {
        @JvmField
        val FACING: DirectionProperty = HorizontalDirectionalBlock.FACING
    }
}
