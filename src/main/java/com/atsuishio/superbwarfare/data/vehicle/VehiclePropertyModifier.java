package com.atsuishio.superbwarfare.data.vehicle;

import com.atsuishio.superbwarfare.data.OldPropertyModifier;
import com.atsuishio.superbwarfare.data.PMC;
import com.atsuishio.superbwarfare.data.PropertyModifier;

public interface VehiclePropertyModifier extends PropertyModifier<VehicleData, DefaultVehicleData>, OldPropertyModifier<VehicleData, DefaultVehicleData> {

    @Override
    default void modifyProperty(PMC<VehicleData, DefaultVehicleData> modifier) {
    }
}
