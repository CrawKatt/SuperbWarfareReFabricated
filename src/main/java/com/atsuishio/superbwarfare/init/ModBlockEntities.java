package com.atsuishio.superbwarfare.init;

import com.atsuishio.superbwarfare.Mod;
import com.atsuishio.superbwarfare.block.entity.*;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ModBlockEntities {

    public static final BlockEntityType<ContainerBlockEntity> CONTAINER = register("container",
            BlockEntityType.Builder.of(ContainerBlockEntity::new, ModBlocks.CONTAINER).build(null));

    public static final BlockEntityType<SmallContainerBlockEntity> SMALL_CONTAINER = register("small_container",
            BlockEntityType.Builder.of(SmallContainerBlockEntity::new, ModBlocks.SMALL_CONTAINER).build(null));

    public static final BlockEntityType<ChargingStationBlockEntity> CHARGING_STATION = register("charging_station",
            BlockEntityType.Builder.of(ChargingStationBlockEntity::new, ModBlocks.CHARGING_STATION).build(null));

    public static final BlockEntityType<CreativeChargingStationBlockEntity> CREATIVE_CHARGING_STATION = register("creative_charging_station",
            BlockEntityType.Builder.of(CreativeChargingStationBlockEntity::new, ModBlocks.CREATIVE_CHARGING_STATION).build(null));

    public static final BlockEntityType<FuMO25BlockEntity> FUMO_25 = register("fumo_25",
            BlockEntityType.Builder.of(FuMO25BlockEntity::new, ModBlocks.FUMO_25).build(null));

    public static final BlockEntityType<VehicleDeployerBlockEntity> VEHICLE_DEPLOYER = register("vehicle_deployer",
            BlockEntityType.Builder.of(VehicleDeployerBlockEntity::new, ModBlocks.VEHICLE_DEPLOYER).build(null));

    public static final BlockEntityType<SuperbItemInterfaceBlockEntity> SUPERB_ITEM_INTERFACE = register("superb_item_interface",
            BlockEntityType.Builder.of(SuperbItemInterfaceBlockEntity::new, ModBlocks.SUPERB_ITEM_INTERFACE).build(null));

    public static final BlockEntityType<CreativeSuperbItemInterfaceBlockEntity> CREATIVE_SUPERB_ITEM_INTERFACE = register("creative_superb_item_interface",
            BlockEntityType.Builder.of(CreativeSuperbItemInterfaceBlockEntity::new, ModBlocks.CREATIVE_SUPERB_ITEM_INTERFACE).build(null));

    public static final BlockEntityType<LuckyContainerBlockEntity> LUCKY_CONTAINER = register("lucky_container",
            BlockEntityType.Builder.of(LuckyContainerBlockEntity::new, ModBlocks.LUCKY_CONTAINER).build(null));

    public static final BlockEntityType<VehicleAssemblingTableBlockEntity> VEHICLE_ASSEMBLING_TABLE = register("vehicle_assembling_table",
            BlockEntityType.Builder.of(VehicleAssemblingTableBlockEntity::new, ModBlocks.VEHICLE_ASSEMBLING_TABLE).build(null));

    private static <T extends BlockEntityType<?>> T register(String name, T type) {
        return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Mod.loc(name), type);
    }
}
