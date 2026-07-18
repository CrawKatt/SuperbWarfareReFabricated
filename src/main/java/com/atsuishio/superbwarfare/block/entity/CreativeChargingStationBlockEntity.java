package com.atsuishio.superbwarfare.block.entity;

import com.atsuishio.superbwarfare.capability.energy.InfinityEnergyStorage;
import com.atsuishio.superbwarfare.entity.DPSGeneratorEntity;
import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity;
import com.atsuishio.superbwarfare.init.ModBlockEntities;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import team.reborn.energy.api.EnergyStorage;

import java.util.List;

/**
 * Energy Data Slot Code based on @GoryMoon's Chargers
 */
public class CreativeChargingStationBlockEntity extends BlockEntity {

    public static final int CHARGE_RADIUS = 8;

    public final EnergyStorage energyStorage = new InfinityEnergyStorage();

    public CreativeChargingStationBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CREATIVE_CHARGING_STATION.get(), pos, state);
    }

    public static void serverTick(CreativeChargingStationBlockEntity blockEntity) {
        if (blockEntity.level == null) return;

        blockEntity.chargeEntity();
        blockEntity.chargeBlock();
    }

    private void chargeEntity() {
        if (this.level == null) return;
        if (this.level.getGameTime() % 20 != 0) return;

        List<Entity> entities = this.level.getEntitiesOfClass(Entity.class, new AABB(this.getBlockPos()).inflate(CHARGE_RADIUS));
        for (Entity entity : entities) {
            EnergyStorage targetEnergy = null;
            if (entity instanceof VehicleEntity vehicle && vehicle.hasEnergyStorage()) {
                targetEnergy = vehicle.getEnergyStorage();
            } else if (entity instanceof DPSGeneratorEntity generator) {
                targetEnergy = generator.getEnergy();
            }

            if (targetEnergy == null || !targetEnergy.supportsInsertion() || targetEnergy.getAmount() >= targetEnergy.getCapacity()) {
                continue;
            }

            try (Transaction t = Transaction.openOuter()) {
                targetEnergy.insert(Integer.MAX_VALUE, t);
                t.commit();
            }
        }
    }

    private void chargeBlock() {
        if (this.level == null) return;

        for (Direction direction : Direction.values()) {
            var blockEntity = this.level.getBlockEntity(this.getBlockPos().relative(direction));
            if (blockEntity == null
                    || blockEntity instanceof CreativeChargingStationBlockEntity
            ) continue;

            var targetEnergy = EnergyStorage.SIDED.find(level, blockEntity.getBlockPos(), direction.getOpposite());
            if (targetEnergy != null && targetEnergy.supportsInsertion()) {
                try (Transaction t = Transaction.openOuter()) {
                    targetEnergy.insert(Integer.MAX_VALUE, t);
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
