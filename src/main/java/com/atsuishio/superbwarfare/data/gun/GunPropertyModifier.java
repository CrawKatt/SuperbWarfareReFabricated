package com.atsuishio.superbwarfare.data.gun;

import com.atsuishio.superbwarfare.data.PropertyModifier;
import com.atsuishio.superbwarfare.data.OldPropertyModifier;
import com.atsuishio.superbwarfare.data.PMC;

public interface GunPropertyModifier extends PropertyModifier<GunData, DefaultGunData>, OldPropertyModifier<GunData, DefaultGunData> {

    @Override
    default void modifyProperty(PMC<GunData, DefaultGunData> modifier) {
    }
}
