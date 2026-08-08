package com.atsuishio.superbwarfare.init

import com.atsuishio.superbwarfare.Mod
import com.atsuishio.superbwarfare.block.entity.ChargingStationBlockEntity
import com.atsuishio.superbwarfare.block.entity.BlueprintResearchTableBlockEntity
import com.atsuishio.superbwarfare.block.entity.CreativeChargingStationBlockEntity
import com.atsuishio.superbwarfare.block.entity.FuMO25BlockEntity
import com.atsuishio.superbwarfare.block.entity.SuperbItemInterfaceBlockEntity
import com.atsuishio.superbwarfare.capability.api.IEnergyStorage
import com.atsuishio.superbwarfare.capability.api.IItemHandler
import com.atsuishio.superbwarfare.capability.api.InvWrapper
import com.atsuishio.superbwarfare.capability.api.SidedInvWrapper
import com.atsuishio.superbwarfare.capability.energy.ItemEnergyStorage
import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity
import com.atsuishio.superbwarfare.entity.living.DPSGeneratorEntity
import com.atsuishio.superbwarfare.item.EnergyStorageItem
import com.atsuishio.superbwarfare.item.blockitem.CreativeChargingStationBlockItem
import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup
import net.fabricmc.fabric.api.lookup.v1.entity.EntityApiLookup
import net.fabricmc.fabric.api.lookup.v1.item.ItemApiLookup
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

    @JvmStatic
    fun init() {
        ENERGY_BLOCK.registerForBlockEntity(
            ChargingStationBlockEntity::getEnergyStorage,
            ModBlockEntities.CHARGING_STATION
        )

        ENERGY_BLOCK.registerForBlockEntity(
            CreativeChargingStationBlockEntity::getEnergyStorage,
            ModBlockEntities.CREATIVE_CHARGING_STATION
        )

        ENERGY_BLOCK.registerForBlockEntity(
            FuMO25BlockEntity::getEnergyStorage,
            ModBlockEntities.FUMO_25
        )

        ENERGY_ITEM.registerForItems(
            { stack, _ ->
                (stack.item as CreativeChargingStationBlockItem).energyStorage
            },
            ModItems.CREATIVE_CHARGING_STATION
        )

        EnergyStorage.SIDED.registerForBlockEntity(
            { be: ChargingStationBlockEntity, dir: Direction? ->
                be.getEnergyStorage(dir) as EnergyStorage
            },
            ModBlockEntities.CHARGING_STATION
        )

        EnergyStorage.SIDED.registerForBlockEntity(
            { be: CreativeChargingStationBlockEntity, dir: Direction? ->
                be.getEnergyStorage(dir) as EnergyStorage
            },
            ModBlockEntities.CREATIVE_CHARGING_STATION
        )

        EnergyStorage.SIDED.registerForBlockEntity(
            { be: FuMO25BlockEntity, dir: Direction? ->
                be.getEnergyStorage(dir) as EnergyStorage
            },
            ModBlockEntities.FUMO_25
        )

        ITEM_HANDLER_BLOCK.registerForBlockEntity(
            { be: ChargingStationBlockEntity, ctx: Direction? ->
                if (ctx == null || be.isRemoved) return@registerForBlockEntity null

                val itemHandlers = arrayOf<IItemHandler>(
                    SidedInvWrapper(be, Direction.UP),
                    SidedInvWrapper(be, Direction.DOWN),
                    SidedInvWrapper(be, Direction.NORTH)
                )

                when (ctx) {
                    Direction.UP -> itemHandlers[0]
                    Direction.DOWN -> itemHandlers[1]
                    else -> itemHandlers[2]
                }
            },
            ModBlockEntities.CHARGING_STATION
        )

        ITEM_HANDLER_BLOCK.registerForBlockEntity(
            { obj: SuperbItemInterfaceBlockEntity, _ ->
                InvWrapper(obj)
            },
            ModBlockEntities.SUPERB_ITEM_INTERFACE
        )

        ITEM_HANDLER_BLOCK.registerForBlockEntity(
            { be: BlueprintResearchTableBlockEntity, ctx: Direction? ->
                if (be.isRemoved) null else SidedInvWrapper(
                    be,
                    when (ctx) {
                        Direction.UP, Direction.DOWN, Direction.NORTH, Direction.SOUTH -> ctx
                        else -> Direction.EAST
                    }
                )
            },
            ModBlockEntities.BLUEPRINT_RESEARCH_TABLE
        )

        for (item in BuiltInRegistries.ITEM) {
            if (item is EnergyStorageItem) {
                ENERGY_ITEM.registerForItems(
                    { stack, _ ->
                        ItemEnergyStorage(
                            stack,
                            item::getMaxEnergy,
                            item::getMaxReceiveEnergy,
                            item::getMaxExtractEnergy
                        )
                    },
                    item
                )
            }
        }

        for (item in BuiltInRegistries.ITEM) {
            if (item is EnergyStorageItem) {
                EnergyStorage.ITEM.registerForItems(
                    { stack, _ ->
                        ItemEnergyStorage(
                            stack,
                            item::getMaxEnergy,
                            item::getMaxReceiveEnergy,
                            item::getMaxExtractEnergy
                        )
                    },
                    item
                )
            }
        }

        for (entity in BuiltInRegistries.ENTITY_TYPE) {
            ENERGY_ENTITY.registerForType(
                { obj, _ ->
                    when {
                        obj is DPSGeneratorEntity -> obj.energyStorage
                        obj is VehicleEntity && obj.hasEnergyStorage() -> obj.getEnergyStorage()
                        else -> null
                    }
                },
                entity
            )

            ITEM_HANDLER_ENTITY.registerForType(
                { obj, _ ->
                    when {
                        obj is Player -> InvWrapper(obj.inventory)
                        obj is VehicleEntity && obj.hasContainer() -> obj.inventory
                        else -> null
                    }
                },
                entity
            )
        }
    }
}
