package com.atsuishio.superbwarfare.capability.energy;

import com.atsuishio.superbwarfare.data.vehicle.VehicleData;
import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;

public class VehicleEnergyStorage extends SyncedEntityEnergyStorage {

    protected VehicleEntity vehicle;

    public VehicleEnergyStorage(VehicleEntity vehicle) {
        super(Integer.MAX_VALUE, vehicle.getEntityData(), vehicle.getEnergyDataAccessor());

        this.vehicle = vehicle;
    }

    @Override
    public long extract(long maxAmount, TransactionContext transaction) {
        if (VehicleData.getDefault(vehicle).isDefaultData) return 0;

        setCapacity(getCapacity());
        setMaxExtract(getCapacity());
        return super.extract(maxAmount, transaction);
    }

    @Override
    public long insert(long maxAmount, TransactionContext transaction) {
        if (VehicleData.getDefault(vehicle).isDefaultData) return 0;

        setCapacity(getCapacity());
        setMaxReceive(getCapacity());
        return super.insert(maxAmount, transaction);
    }

    @Override
    public boolean supportsInsertion() {
        return !VehicleData.getDefault(vehicle).isDefaultData && super.supportsInsertion() && vehicle.computed().maxEnergy > 0;
    }

    @Override
    public boolean supportsExtraction() {
        return !VehicleData.getDefault(vehicle).isDefaultData && super.supportsExtraction();
    }

    @Override
    public long getCapacity() {
        return VehicleData.compute(vehicle).maxEnergy;
    }
}
