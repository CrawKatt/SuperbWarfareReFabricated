package com.atsuishio.superbwarfare.block.entity;

import com.atsuishio.superbwarfare.init.ModBlockEntities;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import team.reborn.energy.api.EnergyStorage;
import team.reborn.energy.api.base.SimpleEnergyStorage;

/**
 * Energy Data Slot Code based on @GoryMoon's Chargers
 */
public class CreativeChargingStationBlockEntity extends BlockEntity {

    public static final int CHARGE_RADIUS = 8;

    public final EnergyStorage energyStorage = new SimpleEnergyStorage(Long.MAX_VALUE, Long.MAX_VALUE, Long.MAX_VALUE) {
        @Override
        protected void onFinalCommit() {
            setChanged();
        }
    };

    public CreativeChargingStationBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CREATIVE_CHARGING_STATION.get(), pos, state);
    }

    public static void serverTick(CreativeChargingStationBlockEntity blockEntity) {
        if (blockEntity.level == null) return;

        blockEntity.chargeBlock();
    }

    private void chargeBlock() {
        if (this.level == null) return;

        for (Direction direction : Direction.values()) {
            var blockEntity = this.level.getBlockEntity(this.getBlockPos().relative(direction));
            if (blockEntity == null
                    || blockEntity instanceof CreativeChargingStationBlockEntity
            ) continue;

            var targetEnergy = EnergyStorage.SIDED.get(blockEntity, direction.getOpposite());
            if (targetEnergy != null && targetEnergy.supportsInsertion()) {
                try (Transaction t = Transaction.openOuter()) {
                    targetEnergy.insert(Long.MAX_VALUE, t);
                    t.commit();
                }
                blockEntity.setChanged();
            }
        }
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
