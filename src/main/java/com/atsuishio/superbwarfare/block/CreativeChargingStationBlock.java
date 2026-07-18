package com.atsuishio.superbwarfare.block;

import com.atsuishio.superbwarfare.block.entity.CreativeChargingStationBlockEntity;
import com.atsuishio.superbwarfare.capability.energy.ModEnergyApi;
import com.atsuishio.superbwarfare.init.ModBlockEntities;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@SuppressWarnings("deprecation")
public class CreativeChargingStationBlock extends BaseEntityBlock {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public CreativeChargingStationBlock() {
        super(Properties.of().sound(SoundType.METAL).strength(3.0f).requiresCorrectToolForDrops());
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable BlockGetter pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
        pTooltip.add(Component.translatable("des.superbwarfare.creative_charging_station").withStyle(ChatFormatting.GRAY));
    }

    @Override
    public InteractionResult use(BlockState pState, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult pHit) {
        ItemStack stack = player.getItemInHand(hand);
        var energy = ModEnergyApi.get(stack);
        if (energy == null) return InteractionResult.FAIL;
        if (energy.supportsInsertion() && energy.getAmount() < energy.getCapacity()) {
            try (Transaction t = Transaction.openOuter()) {
                energy.insert(Integer.MAX_VALUE, t);
                t.commit();
            }
            if (!level.isClientSide) {
                player.displayClientMessage(Component.translatable("des.superbwarfare.creative_charging_station.charge.success").withStyle(ChatFormatting.GREEN), true);
            }
            return InteractionResult.SUCCESS;
        } else if (energy.supportsExtraction()) {
            try (Transaction t = Transaction.openOuter()) {
                energy.extract(Integer.MAX_VALUE, t);
                t.commit();
            }
            if (!level.isClientSide) {
                player.displayClientMessage(Component.translatable("des.superbwarfare.creative_charging_station.extract.success").withStyle(ChatFormatting.GREEN), true);
            }
            return InteractionResult.SUCCESS;
        } else {
            if (!level.isClientSide) {
                player.displayClientMessage(Component.translatable("des.superbwarfare.creative_charging_station.fail").withStyle(ChatFormatting.RED), true);
            }
            return InteractionResult.FAIL;
        }
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new CreativeChargingStationBlockEntity(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if (!pLevel.isClientSide) {
            return createTickerHelper(pBlockEntityType, ModBlockEntities.CREATIVE_CHARGING_STATION.get(), (pLevel1, pPos, pState1, blockEntity) -> CreativeChargingStationBlockEntity.serverTick(blockEntity));
        }
        return null;
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        if (!pState.is(pNewState.getBlock())) {
            BlockEntity blockentity = pLevel.getBlockEntity(pPos);
            if (blockentity instanceof CreativeChargingStationBlockEntity) {
                pLevel.updateNeighbourForOutputSignal(pPos, this);
            }
        }

        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
    }
}
