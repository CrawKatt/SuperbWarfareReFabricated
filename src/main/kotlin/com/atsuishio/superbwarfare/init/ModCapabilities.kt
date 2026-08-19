package com.atsuishio.superbwarfare.init

import com.atsuishio.superbwarfare.Mod
import com.atsuishio.superbwarfare.block.entity.BlueprintResearchTableBlockEntity
import com.atsuishio.superbwarfare.block.entity.ChargingStationBlockEntity
import com.atsuishio.superbwarfare.block.entity.CreativeChargingStationBlockEntity
import com.atsuishio.superbwarfare.block.entity.FuMO25BlockEntity
import com.atsuishio.superbwarfare.block.entity.SuperbItemInterfaceBlockEntity
import com.atsuishio.superbwarfare.capability.api.IEnergyStorage
import com.atsuishio.superbwarfare.capability.api.IItemHandler
import com.atsuishio.superbwarfare.capability.api.InvWrapper
import com.atsuishio.superbwarfare.capability.api.PlayerInvWrapper
import com.atsuishio.superbwarfare.capability.api.SidedInvWrapper
import com.atsuishio.superbwarfare.capability.energy.ItemEnergyStorage
import com.atsuishio.superbwarfare.capability.energy.ModEnergyApi
import com.atsuishio.superbwarfare.entity.living.DPSGeneratorEntity
import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity
import com.atsuishio.superbwarfare.item.EnergyStorageItem
import com.atsuishio.superbwarfare.item.blockitem.CreativeChargingStationBlockItem
import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup
import net.fabricmc.fabric.api.lookup.v1.entity.EntityApiLookup
import net.fabricmc.fabric.api.lookup.v1.item.ItemApiLookup
import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage
import net.minecraft.core.Direction
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.entity.player.Player
import team.reborn.energy.api.EnergyStorage

object ModCapabilities {
    @JvmField
    val ENERGY_BLOCK: BlockApiLookup<IEnergyStorage, Direction?> =
        BlockApiLookup.get(Mod.loc("energy"), IEnergyStorage::class.java, Direction::class.java)

    @JvmField
    val ENERGY_ITEM: ItemApiLookup<IEnergyStorage, Void?> =
        ItemApiLookup.get(Mod.loc("energy"), IEnergyStorage::class.java, Void::class.java)

    @JvmField
    val ENERGY_ENTITY: EntityApiLookup<IEnergyStorage, Void?> =
        EntityApiLookup.get(Mod.loc("energy"), IEnergyStorage::class.java, Void::class.java)

    @JvmField
    val ITEM_HANDLER_BLOCK: BlockApiLookup<IItemHandler, Direction?> =
        BlockApiLookup.get(Mod.loc("item_handler"), IItemHandler::class.java, Direction::class.java)

    @JvmField
    val ITEM_HANDLER_ENTITY: EntityApiLookup<IItemHandler, Void?> =
        EntityApiLookup.get(Mod.loc("item_handler"), IItemHandler::class.java, Void::class.java)

    private var initialized = false

    @JvmStatic
    fun init() {
        if (initialized) return
        initialized = true

        registerEnergyBlocks()
        registerItemHandlers()
        registerEnergyItems()
        registerEntityHandlers()
    }

    private fun registerEnergyBlocks() {
        ENERGY_BLOCK.registerForBlockEntity(
            ChargingStationBlockEntity::getEnergyStorage,
            ModBlockEntities.CHARGING_STATION
        )
        ENERGY_BLOCK.registerForBlockEntity(
            CreativeChargingStationBlockEntity::getEnergyStorage,
            ModBlockEntities.CREATIVE_CHARGING_STATION
        )
        ENERGY_BLOCK.registerForBlockEntity(FuMO25BlockEntity::getEnergyStorage, ModBlockEntities.FUMO_25)

        EnergyStorage.SIDED.registerForBlockEntity(
            { blockEntity: ChargingStationBlockEntity, direction: Direction? ->
                blockEntity.getEnergyStorage(direction) as EnergyStorage
            },
            ModBlockEntities.CHARGING_STATION
        )
        EnergyStorage.SIDED.registerForBlockEntity(
            { blockEntity: CreativeChargingStationBlockEntity, direction: Direction? ->
                blockEntity.getEnergyStorage(direction) as EnergyStorage
            },
            ModBlockEntities.CREATIVE_CHARGING_STATION
        )
        EnergyStorage.SIDED.registerForBlockEntity(
            { blockEntity: FuMO25BlockEntity, direction: Direction? ->
                blockEntity.getEnergyStorage(direction) as EnergyStorage
            },
            ModBlockEntities.FUMO_25
        )

        ENERGY_BLOCK.registerFallback { level, pos, _, _, direction ->
            EnergyStorage.SIDED.find(level, pos, direction)?.let(::EnergyStorageAdapter)
        }
    }

    private fun registerItemHandlers() {
        ITEM_HANDLER_BLOCK.registerForBlockEntity(
            { blockEntity: ChargingStationBlockEntity, direction: Direction? ->
                if (blockEntity.isRemoved || direction == null) null else SidedInvWrapper(blockEntity, direction)
            },
            ModBlockEntities.CHARGING_STATION
        )
        ITEM_HANDLER_BLOCK.registerForBlockEntity(
            { blockEntity: SuperbItemInterfaceBlockEntity, _ -> InvWrapper(blockEntity) },
            ModBlockEntities.SUPERB_ITEM_INTERFACE
        )
        ITEM_HANDLER_BLOCK.registerForBlockEntity(
            { blockEntity: SuperbItemInterfaceBlockEntity, _ -> InvWrapper(blockEntity) },
            ModBlockEntities.CREATIVE_SUPERB_ITEM_INTERFACE
        )
        ITEM_HANDLER_BLOCK.registerForBlockEntity(
            { blockEntity: BlueprintResearchTableBlockEntity, direction ->
                if (blockEntity.isRemoved || direction == null) {
                    null
                } else {
                    val side = if (direction == Direction.WEST) Direction.EAST else direction
                    SidedInvWrapper(blockEntity, side)
                }
            },
            ModBlockEntities.BLUEPRINT_RESEARCH_TABLE
        )

        ItemStorage.SIDED.registerForBlockEntity(
            { blockEntity: ChargingStationBlockEntity, direction: Direction? ->
                if (blockEntity.isRemoved || direction == null) null else InventoryStorage.of(blockEntity, direction)
            },
            ModBlockEntities.CHARGING_STATION
        )
        ItemStorage.SIDED.registerForBlockEntity(
            { blockEntity: SuperbItemInterfaceBlockEntity, direction: Direction? ->
                InventoryStorage.of(blockEntity, direction)
            },
            ModBlockEntities.SUPERB_ITEM_INTERFACE
        )
        ItemStorage.SIDED.registerForBlockEntity(
            { blockEntity: SuperbItemInterfaceBlockEntity, direction: Direction? ->
                InventoryStorage.of(blockEntity, direction)
            },
            ModBlockEntities.CREATIVE_SUPERB_ITEM_INTERFACE
        )
        ItemStorage.SIDED.registerForBlockEntity(
            { blockEntity: BlueprintResearchTableBlockEntity, direction: Direction? ->
                if (blockEntity.isRemoved || direction == null) {
                    null
                } else {
                    val side = if (direction == Direction.WEST) Direction.EAST else direction
                    InventoryStorage.of(blockEntity, side)
                }
            },
            ModBlockEntities.BLUEPRINT_RESEARCH_TABLE
        )
    }

    private fun registerEnergyItems() {
        ENERGY_ITEM.registerForItems(
            { stack, _ -> (stack.item as CreativeChargingStationBlockItem).energyStorage },
            ModItems.CREATIVE_CHARGING_STATION
        )
        EnergyStorage.ITEM.registerForItems(
            { stack, _ -> (stack.item as CreativeChargingStationBlockItem).energyStorage as EnergyStorage },
            ModItems.CREATIVE_CHARGING_STATION
        )

        for (item in BuiltInRegistries.ITEM) {
            if (item !is EnergyStorageItem) continue
            ENERGY_ITEM.registerForItems(
                { stack, _ -> ItemEnergyStorage(stack, item::getMaxEnergy, item::getMaxReceiveEnergy, item::getMaxExtractEnergy) },
                item
            )
            EnergyStorage.ITEM.registerForItems(
                { stack, _ -> ItemEnergyStorage(stack, item::getMaxEnergy, item::getMaxReceiveEnergy, item::getMaxExtractEnergy) },
                item
            )
        }
    }

    private fun registerEntityHandlers() {
        for (type in BuiltInRegistries.ENTITY_TYPE) {
            ENERGY_ENTITY.registerForType(
                { entity, _ ->
                    when {
                        entity is DPSGeneratorEntity -> entity.energyStorage
                        entity is VehicleEntity && entity.hasEnergyStorage() -> entity.getEnergyStorage()
                        else -> null
                    }
                },
                type
            )
            ITEM_HANDLER_ENTITY.registerForType(
                { entity, _ ->
                    when {
                        entity is Player -> PlayerInvWrapper(entity.inventory)
                        entity is VehicleEntity && entity.hasContainer() -> entity.inventory
                        else -> null
                    }
                },
                type
            )
        }
    }

    private class EnergyStorageAdapter(private val storage: EnergyStorage) : IEnergyStorage {
        override fun receiveEnergy(maxReceive: Int, simulate: Boolean) =
            ModEnergyApi.receiveEnergy(storage, maxReceive, simulate)

        override fun extractEnergy(maxExtract: Int, simulate: Boolean) =
            ModEnergyApi.extractEnergy(storage, maxExtract, simulate)

        override fun getEnergyStored() = ModEnergyApi.getEnergyStored(storage)

        override fun getMaxEnergyStored() = ModEnergyApi.getMaxEnergyStored(storage)

        override fun canExtract() = storage.supportsExtraction()

        override fun canReceive() = storage.supportsInsertion()
    }
}
