package com.atsuishio.superbwarfare.block.entity;

import com.atsuishio.superbwarfare.capability.energy.EnergyStorageHelper;
import com.atsuishio.superbwarfare.init.ModBlocks;
import com.atsuishio.superbwarfare.init.ModItems;
import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import team.reborn.energy.api.EnergyStorage;

import java.util.List;

public class ChargingStationBatteryTest implements FabricGameTest {
    @GameTest(template = EMPTY_STRUCTURE)
    public void batteryTransferLimits(GameTestHelper context) {
        long speed = ChargingStationBlockEntity.CHARGE_OTHER_SPEED;
        long capacity = EnergyStorage.ITEM.find(new ItemStack(ModItems.MEDIUM_BATTERY_PACK), null).getCapacity();
        long stationCapacity = ChargingStationBlockEntity.MAX_ENERGY;

        var cases = List.of(
                new TransferCase("charge_normal", ModItems.BATTERY, 1, speed * 2, 0, 100000),
                new TransferCase("charge_small_pack", ModItems.SMALL_BATTERY_PACK, 1, speed * 2, 0, speed),
                new TransferCase("charge_medium_pack", ModItems.MEDIUM_BATTERY_PACK, 1, speed * 2, 0, speed),
                new TransferCase("charge_large_pack", ModItems.LARGE_BATTERY_PACK, 1, speed * 2, 0, speed),
                new TransferCase("charge_partial", ModItems.MEDIUM_BATTERY_PACK, 1, speed * 2, 500, speed),
                new TransferCase("charge_nearly_full", ModItems.MEDIUM_BATTERY_PACK, 1, speed * 2, capacity - 7, 7),
                new TransferCase("charge_full", ModItems.MEDIUM_BATTERY_PACK, 1, speed * 2, capacity, 0),
                new TransferCase("charge_low_station", ModItems.MEDIUM_BATTERY_PACK, 1, 7, 0, 7),
                new TransferCase("charge_empty_station", ModItems.MEDIUM_BATTERY_PACK, 1, 0, 0, 0),
                new TransferCase("fuel_transfer_limit", ModItems.MEDIUM_BATTERY_PACK, 0, 0, speed * 2, speed),
                new TransferCase("fuel_low_battery", ModItems.MEDIUM_BATTERY_PACK, 0, 0, 7, 7),
                new TransferCase("fuel_nearly_full_station", ModItems.MEDIUM_BATTERY_PACK, 0, stationCapacity - 7, speed, 7),
                new TransferCase("fuel_full_station", ModItems.MEDIUM_BATTERY_PACK, 0, stationCapacity, speed, 0),
                new TransferCase("fuel_empty_battery", ModItems.MEDIUM_BATTERY_PACK, 0, 0, 0, 0)
        );

        for (int i = 0; i < cases.size(); i++) {
            var test = cases.get(i);
            var station = station(context, test.stationEnergy(), new BlockPos(1 + i % 4, 1, 1 + i / 4));
            var battery = battery(test.item(), test.batteryEnergy());
            station.setItem(test.slot(), battery);

            tick(context, station);

            long delta = test.slot() == 1 ? test.transferred() : -test.transferred();
            context.assertTrue(station.getItem(test.slot()) == battery, test.name() + ": Battery must remain in its slot");
            context.assertTrue(storedEnergy(battery) == test.batteryEnergy() + delta, test.name() + ": Incorrect battery energy");
            context.assertTrue(station.getEnergyStorage(null).getAmount() == test.stationEnergy() - delta, test.name() + ": Station must debit or receive exactly the transferred energy");
            context.assertTrue(battery.getCount() == 1, test.name() + ": Energy transfer must not consume the battery");
        }
        context.succeed();
    }

    @GameTest(template = EMPTY_STRUCTURE)
    public void emptyOrNonEnergySlotDoesNotConsumeEnergy(GameTestHelper context) {
        var station = station(context, 500);
        tick(context, station);
        context.assertTrue(station.getEnergyStorage(null).getAmount() == 500, "Empty slot consumed energy");

        var stone = new ItemStack(Items.STONE);
        station.setItem(1, stone);
        tick(context, station);

        context.assertTrue(station.getItem(1) == stone, "Non-energy item changed");
        context.assertTrue(station.getEnergyStorage(null).getAmount() == 500, "Non-energy item consumed energy");

        context.succeed();
    }

    @GameTest(template = EMPTY_STRUCTURE)
    public void chargedBatteryRetainsEnergyAndNbtAfterReload(GameTestHelper context) {
        var station = station(context, 750);
        var battery = battery(ModItems.BATTERY, 50);

        battery.setHoverName(Component.literal("Charging regression"));
        battery.getOrCreateTag().putBoolean("Enabled", true);
        station.setItem(1, battery);

        tick(context, station);
        var actual = ItemStack.of(battery.save(new CompoundTag()));

        context.assertTrue(storedEnergy(actual) == 800, "Charged energy was not persisted");
        context.assertTrue(actual.getHoverName().getString().equals("Charging regression"), "Custom name changed");
        context.assertTrue(actual.getTag().getBoolean("Enabled"), "Battery enabled state changed");
        context.assertTrue(actual.getCount() == 1, "Battery count changed");
        context.assertTrue(station.getEnergyStorage(null).getAmount() == 0, "Station energy was not consumed");

        context.succeed();
    }

    @GameTest(template = EMPTY_STRUCTURE)
    public void abortedTransfersDoNotChangeBatteryEnergy(GameTestHelper context) {
        var battery = battery(ModItems.BATTERY, 100);
        var storage = EnergyStorage.ITEM.find(battery, null);
        context.assertTrue(storage != null, "Battery energy storage is missing");

        try (var transaction = Transaction.openOuter()) {
            context.assertTrue(storage.insert(50, transaction) == 50, "Insertion simulation failed");
        }

        context.assertTrue(storage.getAmount() == 100 && storedEnergy(battery) == 100, "Aborted insertion changed energy");

        try (var transaction = Transaction.openOuter()) {
            context.assertTrue(storage.extract(50, transaction) == 50, "Extraction simulation failed");
        }

        context.assertTrue(storage.getAmount() == 100 && storedEnergy(battery) == 100, "Aborted extraction changed energy");
        context.succeed();
    }

    @GameTest(template = EMPTY_STRUCTURE)
    public void worldTickerChargesBattery(GameTestHelper context) {
        var station = station(context, 750);
        station.setItem(1, battery(ModItems.BATTERY, 50));

        context.runAfterDelay(5, () -> {
            context.assertTrue(storedEnergy(station.getItem(1)) == 800, "World ticks did not charge the stored battery");
            context.assertTrue(station.getEnergyStorage(null).getAmount() == 0, "World ticks did not debit station energy");
            context.succeed();
        });
    }

    private static ChargingStationBlockEntity station(GameTestHelper context, long energy) {
        return station(context, energy, new BlockPos(1, 1, 1));
    }

    private static ChargingStationBlockEntity station(GameTestHelper context, long energy, BlockPos pos) {
        context.setBlock(pos, ModBlocks.CHARGING_STATION);
        var station = (ChargingStationBlockEntity) context.getBlockEntity(pos);
        context.assertTrue(EnergyStorageHelper.insert(station.getEnergyStorage(null), energy) == energy, "Could not initialize station energy");
        return station;
    }

    private static ItemStack battery(Item item, long energy) {
        var battery = new ItemStack(item);
        battery.getOrCreateTag().putInt("Energy", (int) energy);
        return battery;
    }

    private static long storedEnergy(ItemStack battery) {
        return battery.getTag().getInt("Energy");
    }

    private static void tick(GameTestHelper context, ChargingStationBlockEntity station) {
        ChargingStationBlockEntity.Companion.serverTick(context.getLevel(), station.getBlockPos(), station.getBlockState(), station);
    }

    private record TransferCase(String name, Item item, int slot, long stationEnergy, long batteryEnergy, long transferred) {
    }
}
