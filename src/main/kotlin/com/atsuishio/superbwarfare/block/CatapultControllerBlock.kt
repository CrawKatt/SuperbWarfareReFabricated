package com.atsuishio.superbwarfare.block

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.util.RandomSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.HorizontalDirectionalBlock
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.BooleanProperty
import net.minecraft.world.level.block.state.properties.DirectionProperty
import net.minecraft.world.level.block.state.properties.IntegerProperty
import net.minecraft.world.phys.BlockHitResult

@Suppress("OVERRIDE_DEPRECATION", "DEPRECATION")
class CatapultControllerBlock : Block(Properties.of().sound(SoundType.METAL).strength(3.0f)) {
    init {
        this.registerDefaultState(
            this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(LAUNCH_POWER, 0)
                .setValue(POWERED, false)
        )
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(FACING).add(LAUNCH_POWER).add(POWERED)
    }

    override fun getStateForPlacement(context: BlockPlaceContext): BlockState? {
        return this.defaultBlockState()
            .setValue(FACING, context.horizontalDirection.opposite)
    }

    override fun use(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hand: InteractionHand,
        hit: BlockHitResult
    ): InteractionResult {
        if (level.isClientSide) return InteractionResult.SUCCESS

        if (player.isShiftKeyDown) {
            val newPower = (state.getValue(LAUNCH_POWER) + 1) % 16
            level.setBlock(pos, state.setValue(LAUNCH_POWER, newPower), 3)
            updateConnectedCatapults(level, pos, state.getValue(FACING), newPower, true)
            player.displayClientMessage(Component.translatable("message.superbwarfare.catapult_power", newPower), true)
            level.playSound(null, pos, SoundEvents.NOTE_BLOCK_HAT.value(), SoundSource.BLOCKS, 0.5f, 1f)
            return InteractionResult.SUCCESS
        }

        return super.use(state, level, pos, player, hand, hit)
    }

    override fun neighborChanged(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        block: Block,
        fromPos: BlockPos,
        isMoving: Boolean
    ) {
        if (!level.isClientSide) {
            val currentState = level.getBlockState(pos)
            val hasPower = level.hasNeighborSignal(pos)
            if (hasPower != currentState.getValue(POWERED)) {
                level.setBlock(pos, currentState.setValue(POWERED, hasPower), 3)
                propagateDirection(level, pos, currentState.getValue(FACING), hasPower)
            }
        }
    }

    override fun onPlace(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        oldState: BlockState,
        movedByPiston: Boolean
    ) {
        if (level is ServerLevel) {
            val hasPower = level.hasNeighborSignal(pos)
            if (hasPower) {
                level.setBlock(pos, state.setValue(POWERED, true), 3)
            }
            updateConnectedCatapults(level, pos, state.getValue(FACING), state.getValue(LAUNCH_POWER), true)
            propagateDirection(level, pos, state.getValue(FACING), hasPower)
            level.scheduleTick(pos, this, 2)
        }
    }

    override fun tick(state: BlockState, level: ServerLevel, pos: BlockPos, random: RandomSource) {
        val currentState = level.getBlockState(pos)
        updateConnectedCatapults(level, pos, currentState.getValue(FACING), currentState.getValue(LAUNCH_POWER), true)
        propagateDirection(level, pos, currentState.getValue(FACING), currentState.getValue(POWERED))
        level.scheduleTick(pos, this, 1)
    }

    override fun onRemove(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        newState: BlockState,
        movedByPiston: Boolean
    ) {
        if (!state.`is`(newState.block)) {
            // Release control of connected catapults
            releaseConnectedCatapults(level, pos, state.getValue(FACING))
        }
        super.onRemove(state, level, pos, newState, movedByPiston)
    }

    private fun updateConnectedCatapults(
        level: Level,
        controllerPos: BlockPos,
        facing: Direction,
        power: Int,
        controlled: Boolean
    ) {
        var checkPos = controllerPos.relative(facing)
        while (level.getBlockState(checkPos).block is AircraftCatapultBlock) {
            val catapultState = level.getBlockState(checkPos)
            level.setBlock(
                checkPos,
                catapultState.setValue(AircraftCatapultBlock.LAUNCH_POWER, power)
                    .setValue(AircraftCatapultBlock.CONTROLLED, controlled),
                3
            )
            checkPos = checkPos.relative(facing)
        }
    }

    private fun releaseConnectedCatapults(level: Level, controllerPos: BlockPos, facing: Direction) {
        if (level !is ServerLevel) return
        var checkPos = controllerPos.relative(facing)
        while (level.getBlockState(checkPos).block is AircraftCatapultBlock) {
            val catapultState = level.getBlockState(checkPos)
            if (catapultState.getValue(AircraftCatapultBlock.CONTROLLED)) {
                level.setBlock(checkPos, catapultState.setValue(AircraftCatapultBlock.CONTROLLED, false), 3)
                level.scheduleTick(checkPos, catapultState.block, 1)
            }
            checkPos = checkPos.relative(facing)
        }
    }

    private fun propagateDirection(level: Level, controllerPos: BlockPos, facing: Direction, powered: Boolean) {
        if (level !is ServerLevel) return
        var checkPos = controllerPos.relative(facing)
        while (level.getBlockState(checkPos).block is AircraftCatapultBlock) {
            val catapultState = level.getBlockState(checkPos)
            if (catapultState.getValue(AircraftCatapultBlock.CONTROLLED)) {
                level.setBlock(checkPos, catapultState.setValue(AircraftCatapultBlock.REVERSED, !powered), 3)
            }
            checkPos = checkPos.relative(facing)
        }
    }

    companion object {
        @JvmField
        val FACING: DirectionProperty = HorizontalDirectionalBlock.FACING

        @JvmField
        val LAUNCH_POWER: IntegerProperty = IntegerProperty.create("launch_power", 0, 15)

        @JvmField
        val POWERED: BooleanProperty = BlockStateProperties.POWERED
    }
}
