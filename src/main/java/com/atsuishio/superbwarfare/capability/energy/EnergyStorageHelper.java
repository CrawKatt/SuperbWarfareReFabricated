package com.atsuishio.superbwarfare.capability.energy;

import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import team.reborn.energy.api.EnergyStorage;

public final class EnergyStorageHelper {
    public static long insert(EnergyStorage storage, long maxAmount) {
        if (maxAmount <= 0) return 0;

        try (Transaction transaction = Transaction.openOuter()) {
            long inserted = storage.insert(maxAmount, transaction);
            transaction.commit();
            return inserted;
        }
    }

    public static long extract(EnergyStorage storage, long maxAmount) {
        if (maxAmount <= 0) return 0;

        try (Transaction transaction = Transaction.openOuter()) {
            long extracted = storage.extract(maxAmount, transaction);
            transaction.commit();
            return extracted;
        }
    }

    public static long simulateExtract(EnergyStorage storage, long maxAmount) {
        if (maxAmount <= 0) return 0;

        try (Transaction transaction = Transaction.openOuter()) {
            return storage.extract(maxAmount, transaction);
        }
    }

    private EnergyStorageHelper() {
    }
}
