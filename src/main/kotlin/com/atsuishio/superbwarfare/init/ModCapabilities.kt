package com.atsuishio.superbwarfare.init

import com.atsuishio.superbwarfare.Mod
import com.atsuishio.superbwarfare.block.entity.ChargingStationBlockEntity
import com.atsuishio.superbwarfare.block.entity.BlueprintResearchTableBlockEntity
import com.atsuishio.superbwarfare.block.entity.CreativeChargingStationBlockEntity
import com.atsuishio.superbwarfare.block.entity.FuMO25BlockEntity
import com.atsuishio.superbwarfare.block.entity.SuperbItemInterfaceBlockEntity
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
import net.minecraft.core.Direction
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.entity.player.Player
import team.reborn.energy.api.EnergyStorage
import team.reborn.energy.api.base.SimpleEnergyItem

object ModCapabilities {
    @JvmField
    val ENERGY_BLOCK = EnergyStorage.SIDED

    @JvmField
    val ENERGY_ITEM = EnergyStorage.ITEM

    @JvmField
    val ITEM_HANDLER_BLOCK: BlockApiLookup<IItemHandler, Direction?> =
        BlockApiLookup.get(Mod.loc("item_handler"), IItemHandler::class.java, Direction::class.java)

    @JvmField
    val ITEM_HANDLER_ENTITY: EntityApiLookup<IItemHandler, Void?> =
        EntityApiLookup.get(Mod.loc("item_handler"), IItemHandler::class.java, Void::class.java)

    @JvmStatic
    fun init() {
        EnergyStorage.SIDED.registerForBlockEntity(
            ChargingStationBlockEntity::getEnergyStorage,
            ModBlockEntities.CHARGING_STATION
        )

        EnergyStorage.SIDED.registerForBlockEntity(
            CreativeChargingStationBlockEntity::getEnergyStorage,
            ModBlockEntities.CREATIVE_CHARGING_STATION
        )

        EnergyStorage.SIDED.registerForBlockEntity(
            { be: FuMO25BlockEntity, _: Direction? -> be.getEnergyStorage() },
            ModBlockEntities.FUMO_25
        )

        EnergyStorage.ITEM.registerForItems(
            { stack, _ -> (stack.item as CreativeChargingStationBlockItem).energyStorage },
            ModItems.CREATIVE_CHARGING_STATION
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
                EnergyStorage.ITEM.registerForItems(
                    { stack, context ->
                        if (context == null) {
                            ItemEnergyStorage(
                                stack,
                                item::getMaxEnergy,
                                item::getMaxReceiveEnergy,
                                item::getMaxExtractEnergy
                            )
                        } else {
                            SimpleEnergyItem.createStorage(
                                context,
                                item.getMaxEnergy(stack).toLong(),
                                item.getMaxReceiveEnergy(stack).toLong(),
                                item.getMaxExtractEnergy(stack).toLong()
                            )
                        }
                    },
                    item
                )
            }
        }

        for (entity in BuiltInRegistries.ENTITY_TYPE) {
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

    @JvmStatic
    fun getEntityEnergyStorage(entity: net.minecraft.world.entity.Entity): EnergyStorage? = when {
        entity is DPSGeneratorEntity -> entity.energyStorage
        entity is VehicleEntity && entity.hasEnergyStorage() -> entity.getEnergyStorage()
        else -> null
    }
}
