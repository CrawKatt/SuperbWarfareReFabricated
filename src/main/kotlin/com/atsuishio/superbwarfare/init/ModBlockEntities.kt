package com.atsuishio.superbwarfare.init

import com.atsuishio.superbwarfare.Mod
import com.atsuishio.superbwarfare.block.entity.*
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.entity.BlockEntityType

object ModBlockEntities {
    private fun <T : BlockEntityType<*>> registerBlockEntity(
        id: String,
        blockEntity: () -> T
    ): T {
        return Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            ResourceLocation.fromNamespaceAndPath(Mod.MODID, id),
            blockEntity()
        )
    }

    @JvmField
    val CONTAINER = registerBlockEntity("container") {
        BlockEntityType.Builder.of(
            { pos, state -> ContainerBlockEntity(pos, state) },
            ModBlocks.CONTAINER
        ).build(null)
    }

    @JvmField
    val CHARGING_STATION = registerBlockEntity("charging_station") {
        BlockEntityType.Builder.of(
            { pos, state -> ChargingStationBlockEntity(pos, state) },
            ModBlocks.CHARGING_STATION
        ).build(null)
    }

    @JvmField
    val CREATIVE_CHARGING_STATION = registerBlockEntity("creative_charging_station") {
        BlockEntityType.Builder.of(
            { pos, state -> CreativeChargingStationBlockEntity(pos, state) },
            ModBlocks.CREATIVE_CHARGING_STATION
        ).build(null)
    }

    @JvmField
    val FUMO_25 = registerBlockEntity("fumo_25") {
        BlockEntityType.Builder.of(
            { pos, state -> FuMO25BlockEntity(pos, state) },
            ModBlocks.FUMO_25
        ).build(null)
    }

    @JvmField
    val SMALL_CONTAINER = registerBlockEntity("small_container") {
        BlockEntityType.Builder.of(
            { pos, state -> SmallContainerBlockEntity(pos, state) },
            ModBlocks.SMALL_CONTAINER
        ).build(null)
    }

    @JvmField
    val VEHICLE_DEPLOYER = registerBlockEntity("vehicle_deployer") {
        BlockEntityType.Builder.of(
            { pos, state -> VehicleDeployerBlockEntity(pos, state) },
            ModBlocks.VEHICLE_DEPLOYER
        ).build(null)
    }

    @JvmField
    val SUPERB_ITEM_INTERFACE = registerBlockEntity("superb_item_interface") {
        BlockEntityType.Builder.of(
            { pos, state -> SuperbItemInterfaceBlockEntity(pos, state) },
            ModBlocks.SUPERB_ITEM_INTERFACE
        ).build(null)
    }

    @JvmField
    val CREATIVE_SUPERB_ITEM_INTERFACE = registerBlockEntity("creative_superb_item_interface") {
        BlockEntityType.Builder.of(
            { pos, state -> CreativeSuperbItemInterfaceBlockEntity(pos, state) },
            ModBlocks.CREATIVE_SUPERB_ITEM_INTERFACE
        ).build(null)
    }

    @JvmField
    val LUCKY_CONTAINER = registerBlockEntity("lucky_container") {
        BlockEntityType.Builder.of(
            { pos, state -> LuckyContainerBlockEntity(pos, state) },
            ModBlocks.LUCKY_CONTAINER
        ).build(null)
    }

    @JvmField
    val VEHICLE_ASSEMBLING_TABLE = registerBlockEntity("vehicle_assembling_table") {
        BlockEntityType.Builder.of(
            { pos, state -> VehicleAssemblingTableBlockEntity(pos, state) },
            ModBlocks.VEHICLE_ASSEMBLING_TABLE
        ).build(null)
    }

    @JvmField
    val BLUEPRINT_RESEARCH_TABLE = registerBlockEntity("blueprint_research_table") {
        BlockEntityType.Builder.of(
            { pos, state -> BlueprintResearchTableBlockEntity(pos, state) },
            ModBlocks.BLUEPRINT_RESEARCH_TABLE
        ).build(null)
    }

    @JvmField
    val BIOGAS_GENERATOR = registerBlockEntity("biogas_generator") {
        BlockEntityType.Builder.of(
            { pos, state -> BiogasGeneratorBlockEntity(pos, state) },
            ModBlocks.BIOGAS_GENERATOR
        ).build(null)
    }

    @JvmStatic
    fun init() {

    }
}