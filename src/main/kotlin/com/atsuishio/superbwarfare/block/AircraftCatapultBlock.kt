package com.atsuishio.superbwarfare.block

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.HorizontalDirectionalBlock
import net.minecraft.world.level.block.RenderShape
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BooleanProperty
import net.minecraft.world.level.block.state.properties.DirectionProperty
import net.minecraft.world.level.block.state.properties.IntegerProperty
import kotlin.math.max

@Suppress("OVERRIDE_DEPRECATION")
open class AircraftCatapultBlock :
    Block(Properties.of().sound(SoundType.METAL).strength(3.0f)) {
    init {
        this.registerDefaultState(
            this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(LAUNCH_POWER, 0)
                .setValue(UPDATING, false)
                .setValue(CONTROLLED, false)
                .setValue(REVERSED, false)
        )
    }

    override fun onPlace(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        pOldState: BlockState,
        pMovedByPiston: Boolean
    ) {
        if (level is ServerLevel) {
            val behind = pos.relative(state.getValue(FACING).opposite)
            if (level.getBlockState(behind).block is CatapultControllerBlock) {
                val controllerState = level.getBlockState(behind)
                level.setBlock(
                    pos, state
                        .setValue(LAUNCH_POWER, controllerState.getValue(CatapultControllerBlock.LAUNCH_POWER))
                        .setValue(CONTROLLED, true)
                        .setValue(REVERSED, !controllerState.getValue(CatapultControllerBlock.POWERED)), 3
                )
                return
            }
            val neighborPower = this.getFacingPower(level, pos, state)
            if (neighborPower > 0) {
                level.setBlock(pos, state.setValue(LAUNCH_POWER, neighborPower), 3)
            }
        }
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block?, BlockState?>) {
        builder.add(FACING).add(LAUNCH_POWER).add(UPDATING).add(CONTROLLED).add(REVERSED)
    }

    override fun getRenderShape(pState: BlockState): RenderShape {
        return RenderShape.MODEL
    }

    override fun getStateForPlacement(context: BlockPlaceContext): BlockState? {
        return this.defaultBlockState()
            .setValue(FACING, context.horizontalDirection.opposite)
    }

    override fun neighborChanged(
        pState: BlockState,
        pLevel: Level,
        pPos: BlockPos,
        pBlock: Block,
        pFromPos: BlockPos,
        pIsMoving: Boolean
    ) {
        if (!pLevel.isClientSide && !pState.getValue(UPDATING)) {
            pLevel.scheduleTick(pPos, this, 1)
        }
    }

    override fun tick(state: BlockState, level: ServerLevel, pos: BlockPos, pRandom: RandomSource) {
        val currentState = level.getBlockState(pos)
        if (currentState.getValue(CONTROLLED)) {
            val behind = pos.relative(currentState.getValue(FACING).opposite)
            if (level.getBlockState(behind).block !is CatapultControllerBlock) {
                level.setBlock(pos, currentState.setValue(CONTROLLED, false), 3)
            }
            return
        }
        this.updateSignal(currentState, level, pos)
    }

    private fun updateSignal(state: BlockState, level: ServerLevel, pos: BlockPos) {
        if (state.getValue(UPDATING) || state.getValue(CONTROLLED)) return
        level.setBlock(pos, state.setValue(UPDATING, true), 2)

        val neighborPower = this.getFacingPower(level, pos, state)
        if (neighborPower != state.getValue(LAUNCH_POWER)) {
            val newState = level.getBlockState(pos)
            level.setBlock(pos, newState.setValue(LAUNCH_POWER, neighborPower), 3)
        }

        val newState = level.getBlockState(pos)
        level.setBlock(pos, newState.setValue(UPDATING, false), 2)
    }

    private fun getFacingPower(level: Level, pos: BlockPos, state: BlockState): Int {
        var max = 0
        val relative = pos.relative(state.getValue(FACING))
        val blockState = level.getBlockState(relative)
        if (blockState.block is AircraftCatapultBlock) {
            max = max(max, blockState.getValue(LAUNCH_POWER))
        }
        return max
    }

    companion object {
        @JvmField
        val FACING: DirectionProperty = HorizontalDirectionalBlock.FACING

        @JvmField
        val LAUNCH_POWER: IntegerProperty = IntegerProperty.create("launch_power", 0, 15)

        @JvmField
        val UPDATING: BooleanProperty = BooleanProperty.create("updating")

        @JvmField
        val CONTROLLED: BooleanProperty = BooleanProperty.create("controlled")

        @JvmField
        val REVERSED: BooleanProperty = BooleanProperty.create("reversed")
    }
}
