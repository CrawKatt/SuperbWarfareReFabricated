package com.atsuishio.superbwarfare.init;

import com.atsuishio.superbwarfare.block.entity.*;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public class ModBlockEntities {

    public static final Supplier<BlockEntityType<ContainerBlockEntity>> CONTAINER = Registration.blockEntity("container",
            () -> BlockEntityType.Builder.of(ContainerBlockEntity::new, ModBlocks.CONTAINER.get()).build(null));
    public static final Supplier<BlockEntityType<ChargingStationBlockEntity>> CHARGING_STATION = Registration.blockEntity("charging_station",
            () -> BlockEntityType.Builder.of(ChargingStationBlockEntity::new, ModBlocks.CHARGING_STATION.get()).build(null));
    public static final Supplier<BlockEntityType<CreativeChargingStationBlockEntity>> CREATIVE_CHARGING_STATION = Registration.blockEntity("creative_charging_station",
            () -> BlockEntityType.Builder.of(CreativeChargingStationBlockEntity::new, ModBlocks.CREATIVE_CHARGING_STATION.get()).build(null));
    public static final Supplier<BlockEntityType<FuMO25BlockEntity>> FUMO_25 = Registration.blockEntity("fumo_25",
            () -> BlockEntityType.Builder.of(FuMO25BlockEntity::new, ModBlocks.FUMO_25.get()).build(null));
    public static final Supplier<BlockEntityType<SmallContainerBlockEntity>> SMALL_CONTAINER = Registration.blockEntity("small_container",
            () -> BlockEntityType.Builder.of(SmallContainerBlockEntity::new, ModBlocks.SMALL_CONTAINER.get()).build(null));
    public static final Supplier<BlockEntityType<VehicleDeployerBlockEntity>> VEHICLE_DEPLOYER = Registration.blockEntity("vehicle_deployer",
            () -> BlockEntityType.Builder.of(VehicleDeployerBlockEntity::new, ModBlocks.VEHICLE_DEPLOYER.get()).build(null));
    public static final Supplier<BlockEntityType<SuperbItemInterfaceBlockEntity>> SUPERB_ITEM_INTERFACE = Registration.blockEntity("superb_item_interface",
            () -> BlockEntityType.Builder.of(SuperbItemInterfaceBlockEntity::new, ModBlocks.SUPERB_ITEM_INTERFACE.get()).build(null));
    public static final Supplier<BlockEntityType<CreativeSuperbItemInterfaceBlockEntity>> CREATIVE_SUPERB_ITEM_INTERFACE = Registration.blockEntity("creative_superb_item_interface",
            () -> BlockEntityType.Builder.of(CreativeSuperbItemInterfaceBlockEntity::new, ModBlocks.CREATIVE_SUPERB_ITEM_INTERFACE.get()).build(null));
    public static final Supplier<BlockEntityType<LuckyContainerBlockEntity>> LUCKY_CONTAINER = Registration.blockEntity("lucky_container",
            () -> BlockEntityType.Builder.of(LuckyContainerBlockEntity::new, ModBlocks.LUCKY_CONTAINER.get()).build(null));
    public static final Supplier<BlockEntityType<VehicleAssemblingTableBlockEntity>> VEHICLE_ASSEMBLING_TABLE = Registration.blockEntity("vehicle_assembling_table",
            () -> BlockEntityType.Builder.of(VehicleAssemblingTableBlockEntity::new, ModBlocks.VEHICLE_ASSEMBLING_TABLE.get()).build(null));
}
