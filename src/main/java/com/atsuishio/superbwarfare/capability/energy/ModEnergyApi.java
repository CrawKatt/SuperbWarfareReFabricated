package com.atsuishio.superbwarfare.capability.energy;

import com.atsuishio.superbwarfare.init.ModCapabilities;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import team.reborn.energy.api.EnergyStorage;
import team.reborn.energy.api.base.SimpleEnergyStorage;

public class ModEnergyApi {

    public static void register() {
        ModCapabilities.init();
    }

    public static EnergyStorage createSimple(long capacity) {
        return new SimpleEnergyStorage(capacity, capacity, capacity);
    }

    public static EnergyStorage createSimple(long capacity, long maxInsert, long maxExtract) {
        return new SimpleEnergyStorage(capacity, maxInsert, maxExtract);
    }

    public static int getEnergyStored(@Nullable EnergyStorage storage) {
        return storage == null ? 0 : saturatingEnergy(storage.getAmount());
    }

    public static int getMaxEnergyStored(@Nullable EnergyStorage storage) {
        return storage == null ? 0 : saturatingEnergy(storage.getCapacity());
    }

    private static int saturatingEnergy(long amount) {
        if (amount <= 0) return 0;
        return amount >= Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) amount;
    }

    public static int receiveEnergy(@Nullable EnergyStorage storage, int maxReceive, boolean simulate) {
        if (storage == null) return 0;
        try (Transaction t = Transaction.openOuter()) {
            long received = storage.insert(maxReceive, t);
            if (!simulate) {
                t.commit();
            }
            return (int) received;
        }
    }

    public static int extractEnergy(@Nullable EnergyStorage storage, int maxExtract, boolean simulate) {
        if (storage == null) return 0;
        try (Transaction t = Transaction.openOuter()) {
            long extracted = storage.extract(maxExtract, t);
            if (!simulate) {
                t.commit();
            }
            return (int) extracted;
        }
    }

    @Nullable
    public static EnergyStorage get(ItemStack stack) {
        return EnergyStorage.ITEM.find(stack, ContainerItemContext.withConstant(stack));
    }

    public static int getEnergyStored(ItemStack stack) {
        return getEnergyStored(get(stack));
    }

    public static int getMaxEnergyStored(ItemStack stack) {
        return getMaxEnergyStored(get(stack));
    }

    public static int receiveEnergy(ItemStack stack, int amount, boolean simulate) {
        return receiveEnergy(get(stack), amount, simulate);
    }

    public static int extractEnergy(ItemStack stack, int amount, boolean simulate) {
        return extractEnergy(get(stack), amount, simulate);
    }

    public static boolean hasEnergy(ItemStack stack) {
        return get(stack) != null;
    }
}
