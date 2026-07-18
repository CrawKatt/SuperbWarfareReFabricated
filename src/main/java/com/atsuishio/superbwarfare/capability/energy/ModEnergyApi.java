package com.atsuishio.superbwarfare.capability.energy;

import com.atsuishio.superbwarfare.init.ModBlockEntities;
import com.atsuishio.superbwarfare.init.ModItems;
import com.atsuishio.superbwarfare.data.gun.GunData;
import com.atsuishio.superbwarfare.item.BatteryItem;
import com.atsuishio.superbwarfare.item.ElectricBaton;
import com.atsuishio.superbwarfare.item.gun.GunItem;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;
import team.reborn.energy.api.EnergyStorage;
import team.reborn.energy.api.base.SimpleEnergyStorage;

import java.util.ArrayList;
import java.util.List;

public class ModEnergyApi {

    public static void register() {
        EnergyStorage.SIDED.registerForBlockEntity(
                (blockEntity, direction) -> blockEntity.energyStorage,
                ModBlockEntities.CHARGING_STATION.get()
        );

        EnergyStorage.SIDED.registerForBlockEntity(
                (blockEntity, direction) -> blockEntity.energyStorage,
                ModBlockEntities.CREATIVE_CHARGING_STATION.get()
        );

        EnergyStorage.SIDED.registerForBlockEntity(
                (blockEntity, direction) -> blockEntity.energyStorage,
                ModBlockEntities.FUMO_25.get()
        );

        ItemStorage.SIDED.registerForBlockEntity(
                (blockEntity, direction) -> InventoryStorage.of(blockEntity, direction),
                ModBlockEntities.CHARGING_STATION.get()
        );
        ItemStorage.SIDED.registerForBlockEntity(
                (blockEntity, direction) -> InventoryStorage.of(blockEntity, direction),
                ModBlockEntities.SUPERB_ITEM_INTERFACE.get()
        );
        ItemStorage.SIDED.registerForBlockEntity(
                (blockEntity, direction) -> InventoryStorage.of(blockEntity, direction),
                ModBlockEntities.CREATIVE_SUPERB_ITEM_INTERFACE.get()
        );

        EnergyStorage.ITEM.registerForItems((stack, context) -> {
            return new ItemEnergyStorage(stack,
                    ModEnergyApi::getBatteryCapacity,
                    ModEnergyApi::getBatteryCapacity,
                    ModEnergyApi::getBatteryCapacity);
        }, batteryItems());

        EnergyStorage.ITEM.registerForItems((stack, context) -> {
            return new ItemEnergyStorage(stack,
                    s -> (long) ElectricBaton.MAX_ENERGY,
                    s -> (long) ElectricBaton.MAX_ENERGY,
                    s -> (long) ElectricBaton.MAX_ENERGY);
        }, ModItems.ELECTRIC_BATON.get());

        EnergyStorage.ITEM.registerForItems((stack, context) -> new InfinityEnergyStorage(),
                ModItems.CREATIVE_CHARGING_STATION.get());

        EnergyStorage.ITEM.registerForItems((stack, context) -> {
            return new ItemEnergyStorage(stack,
                    s -> (long) GunData.compute(s).maxEnergy,
                    s -> (long) GunData.compute(s).maxReceiveEnergy,
                    s -> (long) GunData.compute(s).maxExtractEnergy);
        }, gunItems());
    }

    private static Item[] batteryItems() {
        List<Item> items = new ArrayList<>();
        for (var supplier : ModItems.ITEMS_LIST) {
            Item item = supplier.get();
            if (item instanceof BatteryItem) {
                items.add(item);
            }
        }
        return items.toArray(Item[]::new);
    }

    private static long getBatteryCapacity(ItemStack stack) {
        if (stack.getItem() instanceof BatteryItem battery) {
            return battery.maxEnergy;
        }
        return 0L;
    }

    private static Item[] gunItems() {
        List<Item> items = new ArrayList<>();
        for (var supplier : ModItems.GUNS_LIST) {
            Item item = supplier.get();
            if (item instanceof GunItem) {
                items.add(item);
            }
        }
        return items.toArray(Item[]::new);
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
