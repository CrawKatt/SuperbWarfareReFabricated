package com.atsuishio.superbwarfare.init;

import com.atsuishio.superbwarfare.Mod;
import com.atsuishio.superbwarfare.block.entity.ChargingStationBlockEntity;
import com.atsuishio.superbwarfare.block.entity.CreativeChargingStationBlockEntity;
import com.atsuishio.superbwarfare.block.entity.FuMO25BlockEntity;
import com.atsuishio.superbwarfare.capability.api.IEnergyStorage;
import com.atsuishio.superbwarfare.capability.api.IItemHandler;
import com.atsuishio.superbwarfare.capability.api.InvWrapper;
import com.atsuishio.superbwarfare.capability.api.SidedInvWrapper;
import com.atsuishio.superbwarfare.capability.energy.ItemEnergyStorage;
import com.atsuishio.superbwarfare.capability.laser.LaserCapability;
import com.atsuishio.superbwarfare.capability.laser.LaserCapabilityProvider;
import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity;
import com.atsuishio.superbwarfare.item.CreativeChargingStationBlockItem;
import com.atsuishio.superbwarfare.item.EnergyStorageItem;
import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;
import net.fabricmc.fabric.api.lookup.v1.entity.EntityApiLookup;
import net.fabricmc.fabric.api.lookup.v1.item.ItemApiLookup;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;

public class ModCapabilities {

    public static final EntityApiLookup<LaserCapability, Void> LASER_CAPABILITY = EntityApiLookup.get(Mod.loc("laser_capability"), LaserCapability.class, Void.class);

    public static final BlockApiLookup<IEnergyStorage, Direction> ENERGY_BLOCK = BlockApiLookup.get(Mod.loc("energy"), IEnergyStorage.class, Direction.class);

    public static final ItemApiLookup<IEnergyStorage, Void> ENERGY_ITEM = ItemApiLookup.get(Mod.loc("energy"), IEnergyStorage.class, Void.class);

    public static final EntityApiLookup<IEnergyStorage, Void> ENERGY_ENTITY = EntityApiLookup.get(Mod.loc("energy"), IEnergyStorage.class, Void.class);

    public static final BlockApiLookup<IItemHandler, Direction> ITEM_HANDLER_BLOCK = BlockApiLookup.get(Mod.loc("item_handler"), IItemHandler.class, Direction.class);

    public static final EntityApiLookup<IItemHandler, Void> ITEM_HANDLER_ENTITY = EntityApiLookup.get(Mod.loc("item_handler"), IItemHandler.class, Void.class);

    public static void init() {
        LASER_CAPABILITY.registerForTypes(new LaserCapabilityProvider(), EntityType.PLAYER);

        ENERGY_BLOCK.registerForBlockEntity(ChargingStationBlockEntity::getEnergyStorage, ModBlockEntities.CHARGING_STATION.value());
        ENERGY_BLOCK.registerForBlockEntity(CreativeChargingStationBlockEntity::getEnergyStorage, ModBlockEntities.CREATIVE_CHARGING_STATION.value());
        ENERGY_BLOCK.registerForBlockEntity(FuMO25BlockEntity::getEnergyStorage, ModBlockEntities.FUMO_25.value());

        ENERGY_ITEM.registerForItems((stack, ctx) -> ((CreativeChargingStationBlockItem) stack.getItem()).getEnergyStorage(), ModItems.CREATIVE_CHARGING_STATION.value());

        ITEM_HANDLER_BLOCK.registerForBlockEntity((ChargingStationBlockEntity be, Direction ctx) -> {
            if (ctx == null || be.isRemoved()) return null;

            var itemHandlers = new IItemHandler[]{
                    new SidedInvWrapper(be, Direction.UP),
                    new SidedInvWrapper(be, Direction.DOWN),
                    new SidedInvWrapper(be, Direction.NORTH),
            };

            return switch (ctx) {
                case UP -> itemHandlers[0];
                case DOWN -> itemHandlers[1];
                default -> itemHandlers[2];
            };
        }, ModBlockEntities.CHARGING_STATION.value());

        ITEM_HANDLER_BLOCK.registerForBlockEntities((object, context) -> new InvWrapper(object), ModBlockEntities.SUPERB_ITEM_INTERFACE.get());

        for (var item : BuiltInRegistries.ITEM) {
            if (item instanceof EnergyStorageItem energyItem) {
                ENERGY_ITEM.registerForItems((stack, ctx) -> new ItemEnergyStorage(
                        stack,
                        s -> energyItem.getMaxEnergy(s),
                        s -> energyItem.getMaxReceiveEnergy(s),
                        s -> energyItem.getMaxExtractEnergy(s)
                ), item);
            }
        }

        for (var entity : BuiltInRegistries.ENTITY_TYPE) {
            ENERGY_ENTITY.registerForTypes((obj, ctx) ->
                    (obj instanceof VehicleEntity vehicle && vehicle.hasEnergyStorage()) ? vehicle.getEnergyStorage() : null, entity);

            ITEM_HANDLER_ENTITY.registerForTypes((obj, ctx) ->
                    (obj instanceof VehicleEntity vehicle && vehicle.hasContainer()) ? new InvWrapper(vehicle) : null, entity);
        }

        ENERGY_ENTITY.registerForTypes((obj, ctx) -> obj.getEnergyStorage(), ModEntities.DPS_GENERATOR.get());
    }
}
