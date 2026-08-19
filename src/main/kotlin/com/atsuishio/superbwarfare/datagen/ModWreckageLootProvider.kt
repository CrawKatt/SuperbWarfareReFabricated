package com.atsuishio.superbwarfare.datagen

import com.atsuishio.superbwarfare.data.loot.WreckageLootData
import com.atsuishio.superbwarfare.datagen.base.SbwWreckageLootProvider
import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity
import com.atsuishio.superbwarfare.init.ModDamageTypes
import com.atsuishio.superbwarfare.init.ModEntities
import com.atsuishio.superbwarfare.init.ModItems
import net.minecraft.data.PackOutput
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Items

typealias LootBuilder = WreckageLootData.Builder
typealias PoolBuilder = WreckageLootData.Pool.Builder
typealias Type = WreckageLootData.Pool.Type
typealias Entry = WreckageLootData.Entry

class ModWreckageLootProvider(output: PackOutput) : SbwWreckageLootProvider(output) {

    override fun generate() {
        this.add(
            ModEntities.A_10A,
            LootBuilder()
                .addPool(
                    PoolBuilder(type = Type.COMPLETE)
                        .source(ModDamageTypes.REPAIR_TOOL)
                        .addEntry(
                            Entry(ModItems.STEEL_BLOCK, 3, 1.0),
                            Entry(ModItems.HEAVY_ARMAMENT_MODULE,1, 0.5),
                            Entry(ModItems.LARGE_BATTERY_PACK,1, 0.5),
                            Entry(ModItems.LARGE_PROPELLER,1, 0.5),
                            Entry(ModItems.WHEEL,1, 0.5),
                            Entry(ModItems.LARGE_MOTOR,1, 0.5),
                        ).build(),
                    PoolBuilder(type = Type.COMPLETE)
                        .addEntry(
                            Entry(ModItems.STEEL_BLOCK, 3, 0.5),
                        ).build()
                )
        )

        this.add(
            ModEntities.AH_6,
            LootBuilder()
                .addPool(
                    PoolBuilder(type = Type.COMPLETE)
                        .source(ModDamageTypes.REPAIR_TOOL)
                        .addEntry(
                            Entry(ModItems.STEEL_BLOCK, 1, 1.0),
                            Entry(ModItems.LIGHT_ARMAMENT_MODULE,1, 0.5),
                            Entry(ModItems.MEDIUM_BATTERY_PACK,1, 0.5),
                            Entry(ModItems.LARGE_PROPELLER,1, 0.5),
                            Entry(ModItems.PROPELLER,1, 0.5),
                            Entry(ModItems.LARGE_MOTOR,1, 0.5),
                        ).build(),
                    PoolBuilder(type = Type.COMPLETE)
                        .addEntry(
                            Entry(ModItems.STEEL_BLOCK, 1, 0.5),
                        ).build()
                )
        )

        this.add(
            ModEntities.ANNIHILATOR,
            LootBuilder()
                .addPool(
                    PoolBuilder(type = Type.COMPLETE)
                        .source(ModDamageTypes.REPAIR_TOOL)
                        .addEntry(
                            Entry(ModItems.STEEL_BLOCK, 12, 1.0),
                            Entry(ModItems.LASER_UNIT,16, 0.5),
                            Entry(ModItems.LARGE_BATTERY_PACK,1, 0.5),
                            Entry(Items.NETHERITE_BLOCK,3, 1.0),
                        ).build(),
                    PoolBuilder(type = Type.COMPLETE)
                        .addEntry(
                            Entry(ModItems.STEEL_BLOCK, 12, 0.5),
                            Entry(Items.NETHERITE_BLOCK,1, 0.25),
                        ).build()
                )
        )

        this.add(
            ModEntities.BL_132,
            LootBuilder()
                .addPool(
                    PoolBuilder(type = Type.COMPLETE)
                        .source(ModDamageTypes.REPAIR_TOOL)
                        .addEntry(
                            Entry(ModItems.STEEL_BLOCK, 5, 1.0),
                            Entry(ModItems.CANNON_CORE,2, 0.5)
                        ).build(),
                    PoolBuilder(type = Type.COMPLETE)
                        .addEntry(
                            Entry(ModItems.STEEL_BLOCK, 4, 0.5)
                        ).build()
                )
        )

        this.add(
            ModEntities.BMP_2,
            LootBuilder()
                .addPool(
                    PoolBuilder(type = Type.COMPLETE)
                        .source(ModDamageTypes.REPAIR_TOOL)
                        .addEntry(
                            Entry(ModItems.STEEL_BLOCK, 4, 1.0),
                            Entry(ModItems.MEDIUM_ARMAMENT_MODULE,1, 0.5),
                            Entry(ModItems.TRACK, 1, 0.5),
                            Entry(ModItems.LARGE_MOTOR, 1, 0.5),
                            Entry(ModItems.MEDIUM_BATTERY_PACK, 1, 0.2),
                        ).build(),
                    PoolBuilder(type = Type.COMPLETE)
                        .addEntry(
                            Entry(ModItems.STEEL_BLOCK, 4, 0.5)
                        ).build(),
                    PoolBuilder(type = Type.TURRET_ONLY)
                        .source(ModDamageTypes.REPAIR_TOOL)
                        .addEntry(
                            Entry(ModItems.STEEL_BLOCK, 1, 1.0),
                            Entry(ModItems.MEDIUM_ARMAMENT_MODULE,1, 0.5),
                        ).build(),
                    PoolBuilder(type = Type.TURRET_ONLY)
                        .addEntry(
                            Entry(ModItems.STEEL_BLOCK, 1, 0.5),
                        ).build(),
                    PoolBuilder(type = Type.VEHICLE_ONLY)
                        .source(ModDamageTypes.REPAIR_TOOL)
                        .addEntry(
                            Entry(ModItems.STEEL_BLOCK, 3, 1.0),
                            Entry(ModItems.TRACK, 1, 0.5),
                            Entry(ModItems.LARGE_MOTOR, 1, 0.5),
                            Entry(ModItems.MEDIUM_BATTERY_PACK, 1, 0.2),
                        ).build(),
                    PoolBuilder(type = Type.VEHICLE_ONLY)
                        .addEntry(
                            Entry(ModItems.STEEL_BLOCK, 3, 0.5)
                        ).build(),
                )
        )

        this.add(
            ModEntities.BRADLEY,
            LootBuilder()
                .addPool(
                    PoolBuilder(type = Type.COMPLETE)
                        .source(ModDamageTypes.REPAIR_TOOL)
                        .addEntry(
                            Entry(ModItems.STEEL_BLOCK, 4, 1.0),
                            Entry(ModItems.MEDIUM_ARMAMENT_MODULE,1, 0.5),
                            Entry(ModItems.TRACK, 1, 0.5),
                            Entry(ModItems.LARGE_MOTOR, 1, 0.5),
                            Entry(ModItems.MEDIUM_BATTERY_PACK, 1, 0.2),
                        ).build(),
                    PoolBuilder(type = Type.COMPLETE)
                        .addEntry(
                            Entry(ModItems.STEEL_BLOCK, 4, 0.5)
                        ).build(),
                    PoolBuilder(type = Type.TURRET_ONLY)
                        .source(ModDamageTypes.REPAIR_TOOL)
                        .addEntry(
                            Entry(ModItems.STEEL_BLOCK, 1, 1.0),
                            Entry(ModItems.MEDIUM_ARMAMENT_MODULE,1, 0.5),
                        ).build(),
                    PoolBuilder(type = Type.TURRET_ONLY)
                        .addEntry(
                            Entry(ModItems.STEEL_BLOCK, 1, 0.5),
                        ).build(),
                    PoolBuilder(type = Type.VEHICLE_ONLY)
                        .source(ModDamageTypes.REPAIR_TOOL)
                        .addEntry(
                            Entry(ModItems.STEEL_BLOCK, 3, 1.0),
                            Entry(ModItems.TRACK, 1, 0.5),
                            Entry(ModItems.LARGE_MOTOR, 1, 0.5),
                            Entry(ModItems.MEDIUM_BATTERY_PACK, 1, 0.2),
                        ).build(),
                    PoolBuilder(type = Type.VEHICLE_ONLY)
                        .addEntry(
                            Entry(ModItems.STEEL_BLOCK, 3, 0.5)
                        ).build(),
                )
        )

        this.add(
            ModEntities.HPJ_11,
            LootBuilder()
                .addPool(
                    PoolBuilder(type = Type.COMPLETE)
                        .source(ModDamageTypes.REPAIR_TOOL)
                        .addEntry(
                            Entry(ModItems.STEEL_BLOCK, 3, 1.0),
                            Entry(ModItems.CANNON_CORE,1, 0.5),
                            Entry(ModItems.LARGE_MOTOR, 1, 0.5),
                            Entry(ModItems.MEDIUM_BATTERY_PACK, 1, 0.2)
                        ).build(),
                    PoolBuilder(type = Type.COMPLETE)
                        .addEntry(
                            Entry(ModItems.STEEL_BLOCK, 3, 0.5)
                        ).build()
                )
        )

        this.add(
            ModEntities.JU_87,
            LootBuilder()
                .addPool(
                    PoolBuilder(type = Type.COMPLETE)
                        .source(ModDamageTypes.REPAIR_TOOL)
                        .addEntry(
                            Entry(ModItems.STEEL_BLOCK, 2, 1.0),
                            Entry(ModItems.MEDIUM_ARMAMENT_MODULE,1, 0.5),
                            Entry(ModItems.MEDIUM_BATTERY_PACK,1, 0.5),
                            Entry(ModItems.LARGE_PROPELLER,1, 0.5),
                            Entry(ModItems.LARGE_MOTOR,1, 0.5),
                        ).build(),
                    PoolBuilder(type = Type.COMPLETE)
                        .addEntry(
                            Entry(ModItems.STEEL_BLOCK, 2, 0.5),
                        ).build()
                )
        )

        this.add(
            ModEntities.KV_16,
            LootBuilder()
                .addPool(
                    PoolBuilder(type = Type.COMPLETE)
                        .source(ModDamageTypes.REPAIR_TOOL)
                        .addEntry(
                            Entry(Items.BUCKET, 2, 1.0),
                            Entry(ModItems.STEEL_INGOT, 4, 1.0),
                            Entry(ModItems.LIGHT_ARMAMENT_MODULE,1, 0.5),
                            Entry(ModItems.SMALL_BATTERY_PACK,1, 0.5),
                            Entry(ModItems.PROPELLER,1, 0.5),
                            Entry(ModItems.LARGE_MOTOR,1, 0.5),
                        ).build(),
                    PoolBuilder(type = Type.COMPLETE)
                        .addEntry(
                            Entry(Items.BUCKET, 2, 1.0),
                            Entry(ModItems.STEEL_INGOT, 4, 0.5)
                        ).build()
                )
        )

        this.add(
            ModEntities.LAV_25,
            LootBuilder()
                .addPool(
                    PoolBuilder(type = Type.COMPLETE)
                        .source(ModDamageTypes.REPAIR_TOOL)
                        .addEntry(
                            Entry(ModItems.STEEL_BLOCK, 4, 1.0),
                            Entry(ModItems.MEDIUM_ARMAMENT_MODULE,1, 0.5),
                            Entry(ModItems.WHEEL, 4, 0.5),
                            Entry(ModItems.LARGE_MOTOR, 1, 0.5),
                            Entry(ModItems.MEDIUM_BATTERY_PACK, 1, 0.2),
                        ).build(),
                    PoolBuilder(type = Type.COMPLETE)
                        .addEntry(
                            Entry(ModItems.STEEL_BLOCK, 4, 0.5)
                        ).build(),
                    PoolBuilder(type = Type.TURRET_ONLY)
                        .source(ModDamageTypes.REPAIR_TOOL)
                        .addEntry(
                            Entry(ModItems.STEEL_BLOCK, 1, 1.0),
                            Entry(ModItems.MEDIUM_ARMAMENT_MODULE,1, 0.5),
                        ).build(),
                    PoolBuilder(type = Type.TURRET_ONLY)
                        .addEntry(
                            Entry(ModItems.STEEL_BLOCK, 1, 0.5),
                        ).build(),
                    PoolBuilder(type = Type.VEHICLE_ONLY)
                        .source(ModDamageTypes.REPAIR_TOOL)
                        .addEntry(
                            Entry(ModItems.STEEL_BLOCK, 3, 1.0),
                            Entry(ModItems.WHEEL, 4, 0.5),
                            Entry(ModItems.LARGE_MOTOR, 1, 0.5),
                            Entry(ModItems.MEDIUM_BATTERY_PACK, 1, 0.2),
                        ).build(),
                    PoolBuilder(type = Type.VEHICLE_ONLY)
                        .addEntry(
                            Entry(ModItems.STEEL_BLOCK, 3, 0.5)
                        ).build(),
                )
        )

        this.add(
            ModEntities.LAV_150,
            LootBuilder()
                .addPool(
                    PoolBuilder(type = Type.COMPLETE)
                        .source(ModDamageTypes.REPAIR_TOOL)
                        .addEntry(
                            Entry(ModItems.STEEL_BLOCK, 3, 1.0),
                            Entry(ModItems.LIGHT_ARMAMENT_MODULE,1, 0.5),
                            Entry(ModItems.WHEEL, 2, 0.5),
                            Entry(ModItems.LARGE_MOTOR, 1, 0.5),
                            Entry(ModItems.MEDIUM_BATTERY_PACK, 1, 0.2),
                        ).build(),
                    PoolBuilder(type = Type.COMPLETE)
                        .addEntry(
                            Entry(ModItems.STEEL_BLOCK, 3, 0.5)
                        ).build(),
                    PoolBuilder(type = Type.TURRET_ONLY)
                        .source(ModDamageTypes.REPAIR_TOOL)
                        .addEntry(
                            Entry(ModItems.STEEL_BLOCK, 1, 1.0),
                            Entry(ModItems.LIGHT_ARMAMENT_MODULE,1, 0.5),
                        ).build(),
                    PoolBuilder(type = Type.TURRET_ONLY)
                        .addEntry(
                            Entry(ModItems.STEEL_BLOCK, 1, 0.5),
                        ).build(),
                    PoolBuilder(type = Type.VEHICLE_ONLY)
                        .source(ModDamageTypes.REPAIR_TOOL)
                        .addEntry(
                            Entry(ModItems.STEEL_BLOCK, 2, 1.0),
                            Entry(ModItems.WHEEL, 2, 0.5),
                            Entry(ModItems.LARGE_MOTOR, 1, 0.5),
                            Entry(ModItems.MEDIUM_BATTERY_PACK, 1, 0.2),
                        ).build(),
                    PoolBuilder(type = Type.VEHICLE_ONLY)
                        .addEntry(
                            Entry(ModItems.STEEL_BLOCK, 2, 0.5)
                        ).build(),
                )
        )

        this.add(
            ModEntities.LAV_AD,
            LootBuilder()
                .addPool(
                    PoolBuilder(type = Type.COMPLETE)
                        .source(ModDamageTypes.REPAIR_TOOL)
                        .addEntry(
                            Entry(ModItems.STEEL_BLOCK, 4, 1.0),
                            Entry(ModItems.MEDIUM_ARMAMENT_MODULE,1, 0.5),
                            Entry(ModItems.WHEEL, 4, 0.5),
                            Entry(ModItems.LARGE_MOTOR, 1, 0.5),
                            Entry(ModItems.MEDIUM_BATTERY_PACK, 1, 0.2),
                        ).build(),
                    PoolBuilder(type = Type.COMPLETE)
                        .addEntry(
                            Entry(ModItems.STEEL_BLOCK, 4, 0.5)
                        ).build(),
                    PoolBuilder(type = Type.TURRET_ONLY)
                        .source(ModDamageTypes.REPAIR_TOOL)
                        .addEntry(
                            Entry(ModItems.STEEL_BLOCK, 1, 1.0),
                            Entry(ModItems.MEDIUM_ARMAMENT_MODULE,1, 0.5),
                        ).build(),
                    PoolBuilder(type = Type.TURRET_ONLY)
                        .addEntry(
                            Entry(ModItems.STEEL_BLOCK, 1, 0.5),
                        ).build(),
                    PoolBuilder(type = Type.VEHICLE_ONLY)
                        .source(ModDamageTypes.REPAIR_TOOL)
                        .addEntry(
                            Entry(ModItems.STEEL_BLOCK, 3, 1.0),
                            Entry(ModItems.WHEEL, 4, 0.5),
                            Entry(ModItems.LARGE_MOTOR, 1, 0.5),
                            Entry(ModItems.MEDIUM_BATTERY_PACK, 1, 0.2),
                        ).build(),
                    PoolBuilder(type = Type.VEHICLE_ONLY)
                        .addEntry(
                            Entry(ModItems.STEEL_BLOCK, 3, 0.5)
                        ).build(),
                )
        )

        this.add(
            ModEntities.M_1A_2,
            LootBuilder()
                .addPool(
                    PoolBuilder(type = Type.COMPLETE)
                        .source(ModDamageTypes.REPAIR_TOOL)
                        .addEntry(
                            Entry(ModItems.STEEL_BLOCK, 6, 1.0),
                            Entry(ModItems.HEAVY_ARMAMENT_MODULE,1, 0.5),
                            Entry(ModItems.TRACK, 1, 0.5),
                            Entry(ModItems.LARGE_MOTOR, 1, 0.5),
                            Entry(ModItems.MEDIUM_BATTERY_PACK, 1, 0.2),
                        ).build(),
                    PoolBuilder(type = Type.COMPLETE)
                        .addEntry(
                            Entry(ModItems.STEEL_BLOCK, 4, 0.5)
                        ).build(),
                    PoolBuilder(type = Type.TURRET_ONLY)
                        .source(ModDamageTypes.REPAIR_TOOL)
                        .addEntry(
                            Entry(ModItems.STEEL_BLOCK, 2, 1.0),
                            Entry(ModItems.HEAVY_ARMAMENT_MODULE,1, 0.5),
                        ).build(),
                    PoolBuilder(type = Type.TURRET_ONLY)
                        .addEntry(
                            Entry(ModItems.STEEL_BLOCK, 2, 0.5),
                        ).build(),
                    PoolBuilder(type = Type.VEHICLE_ONLY)
                        .source(ModDamageTypes.REPAIR_TOOL)
                        .addEntry(
                            Entry(ModItems.STEEL_BLOCK, 4, 1.0),
                            Entry(ModItems.TRACK, 1, 0.5),
                            Entry(ModItems.LARGE_MOTOR, 1, 0.5),
                            Entry(ModItems.MEDIUM_BATTERY_PACK, 1, 0.2),
                        ).build(),
                    PoolBuilder(type = Type.VEHICLE_ONLY)
                        .addEntry(
                            Entry(ModItems.STEEL_BLOCK, 4, 0.5)
                        ).build(),
                )
        )

        this.add(
            ModEntities.T_90A,
            LootBuilder()
                .addPool(
                    PoolBuilder(type = Type.COMPLETE)
                        .source(ModDamageTypes.REPAIR_TOOL)
                        .addEntry(
                            Entry(ModItems.STEEL_BLOCK, 6, 1.0),
                            Entry(ModItems.HEAVY_ARMAMENT_MODULE,1, 0.5),
                            Entry(ModItems.TRACK, 1, 0.5),
                            Entry(ModItems.LARGE_MOTOR, 1, 0.5),
                            Entry(ModItems.MEDIUM_BATTERY_PACK, 1, 0.2),
                        ).build(),
                    PoolBuilder(type = Type.COMPLETE)
                        .addEntry(
                            Entry(ModItems.STEEL_BLOCK, 4, 0.5)
                        ).build(),
                    PoolBuilder(type = Type.TURRET_ONLY)
                        .source(ModDamageTypes.REPAIR_TOOL)
                        .addEntry(
                            Entry(ModItems.STEEL_BLOCK, 2, 1.0),
                            Entry(ModItems.HEAVY_ARMAMENT_MODULE,1, 0.5),
                        ).build(),
                    PoolBuilder(type = Type.TURRET_ONLY)
                        .addEntry(
                            Entry(ModItems.STEEL_BLOCK, 2, 0.5),
                        ).build(),
                    PoolBuilder(type = Type.VEHICLE_ONLY)
                        .source(ModDamageTypes.REPAIR_TOOL)
                        .addEntry(
                            Entry(ModItems.STEEL_BLOCK, 4, 1.0),
                            Entry(ModItems.TRACK, 1, 0.5),
                            Entry(ModItems.LARGE_MOTOR, 1, 0.5),
                            Entry(ModItems.MEDIUM_BATTERY_PACK, 1, 0.2),
                        ).build(),
                    PoolBuilder(type = Type.VEHICLE_ONLY)
                        .addEntry(
                            Entry(ModItems.STEEL_BLOCK, 4, 0.5)
                        ).build(),
                )
        )

        this.add(
            ModEntities.MI_28,
            LootBuilder()
                .addPool(
                    PoolBuilder(type = Type.COMPLETE)
                        .source(ModDamageTypes.REPAIR_TOOL)
                        .addEntry(
                            Entry(ModItems.STEEL_BLOCK, 3, 1.0),
                            Entry(ModItems.HEAVY_ARMAMENT_MODULE,1, 0.5),
                            Entry(ModItems.LARGE_BATTERY_PACK,1, 0.5),
                            Entry(ModItems.LARGE_PROPELLER,1, 0.5),
                            Entry(ModItems.LARGE_MOTOR,1, 0.5),
                        ).build(),
                    PoolBuilder(type = Type.COMPLETE)
                        .addEntry(
                            Entry(ModItems.STEEL_BLOCK, 3, 0.5),
                        ).build()
                )
        )

        this.add(
            ModEntities.MK_42,
            LootBuilder()
                .addPool(
                    PoolBuilder(type = Type.COMPLETE)
                        .source(ModDamageTypes.REPAIR_TOOL)
                        .addEntry(
                            Entry(ModItems.STEEL_BLOCK, 3, 1.0),
                            Entry(ModItems.CANNON_CORE,1, 0.5)
                        ).build(),
                    PoolBuilder(type = Type.COMPLETE)
                        .addEntry(
                            Entry(ModItems.STEEL_BLOCK, 3, 0.5)
                        ).build()
                )
        )

        this.add(
            ModEntities.MLE_1934,
            LootBuilder()
                .addPool(
                    PoolBuilder(type = Type.COMPLETE)
                        .source(ModDamageTypes.REPAIR_TOOL)
                        .addEntry(
                            Entry(ModItems.STEEL_BLOCK, 4, 1.0),
                            Entry(ModItems.CANNON_CORE,2, 0.5)
                        ).build(),
                    PoolBuilder(type = Type.COMPLETE)
                        .addEntry(
                            Entry(ModItems.STEEL_BLOCK, 4, 0.5)
                        ).build()
                )
        )

        this.add(
            ModEntities.PLZ_05,
            LootBuilder()
                .addPool(
                    PoolBuilder(type = Type.COMPLETE)
                        .source(ModDamageTypes.REPAIR_TOOL)
                        .addEntry(
                            Entry(ModItems.STEEL_BLOCK, 5, 1.0),
                            Entry(ModItems.HEAVY_ARMAMENT_MODULE,1, 0.5),
                            Entry(ModItems.TRACK, 1, 0.5),
                            Entry(ModItems.LARGE_MOTOR, 1, 0.5),
                            Entry(ModItems.CANNON_CORE,2, 0.5),
                            Entry(ModItems.MEDIUM_BATTERY_PACK, 1, 0.2),
                        ).build(),
                    PoolBuilder(type = Type.COMPLETE)
                        .addEntry(
                            Entry(ModItems.STEEL_BLOCK, 5, 0.5)
                        ).build(),
                    PoolBuilder(type = Type.TURRET_ONLY)
                        .source(ModDamageTypes.REPAIR_TOOL)
                        .addEntry(
                            Entry(ModItems.STEEL_BLOCK, 2, 1.0),
                            Entry(ModItems.HEAVY_ARMAMENT_MODULE,1, 0.5),
                        ).build(),
                    PoolBuilder(type = Type.TURRET_ONLY)
                        .addEntry(
                            Entry(ModItems.STEEL_BLOCK, 2, 0.5),
                        ).build(),
                    PoolBuilder(type = Type.VEHICLE_ONLY)
                        .source(ModDamageTypes.REPAIR_TOOL)
                        .addEntry(
                            Entry(ModItems.STEEL_BLOCK, 3, 1.0),
                            Entry(ModItems.TRACK, 1, 0.5),
                            Entry(ModItems.LARGE_MOTOR, 1, 0.5),
                            Entry(ModItems.CANNON_CORE,2, 0.5),
                            Entry(ModItems.MEDIUM_BATTERY_PACK, 1, 0.2),
                        ).build(),
                    PoolBuilder(type = Type.VEHICLE_ONLY)
                        .addEntry(
                            Entry(ModItems.STEEL_BLOCK, 3, 0.5)
                        ).build(),
                )
        )

        this.add(
            ModEntities.PRISM_TANK,
            LootBuilder()
                .addPool(
                    PoolBuilder(type = Type.COMPLETE)
                        .source(ModDamageTypes.REPAIR_TOOL)
                        .addEntry(
                            Entry(ModItems.STEEL_BLOCK, 4, 1.0),
                            Entry(ModItems.TRACK,1, 0.5),
                            Entry(ModItems.LASER_UNIT,8, 0.5),
                            Entry(ModItems.LARGE_MOTOR, 1, 0.5),
                            Entry(ModItems.LARGE_BATTERY_PACK, 1, 0.2)
                        ).build(),
                    PoolBuilder(type = Type.COMPLETE)
                        .addEntry(
                            Entry(ModItems.STEEL_BLOCK, 4, 0.5)
                        ).build()
                )
        )

        this.add(
            ModEntities.SODAYO_PICK_UP,
            LootBuilder()
                .addPool(
                    PoolBuilder(type = Type.COMPLETE)
                        .source(ModDamageTypes.REPAIR_TOOL)
                        .addEntry(
                            Entry(ModItems.STEEL_BLOCK, 1, 1.0),
                            Entry(ModItems.WHEEL,2, 0.5),
                            Entry(ModItems.LARGE_MOTOR, 1, 0.5),
                            Entry(ModItems.MEDIUM_BATTERY_PACK, 1, 0.2)
                        ).build(),
                    PoolBuilder(type = Type.COMPLETE)
                        .addEntry(
                            Entry(ModItems.STEEL_BLOCK, 1, 0.5)
                        ).build()
                )
        )

        this.add(
            ModEntities.SODAYO_PICK_UP_HMG,
            LootBuilder()
                .addPool(
                    PoolBuilder(type = Type.COMPLETE)
                        .source(ModDamageTypes.REPAIR_TOOL)
                        .addEntry(
                            Entry(ModItems.STEEL_BLOCK, 1, 1.0),
                            Entry(ModItems.LIGHT_ARMAMENT_MODULE, 1, 0.5),
                            Entry(ModItems.WHEEL,2, 0.5),
                            Entry(ModItems.LARGE_MOTOR, 1, 0.5),
                            Entry(ModItems.MEDIUM_BATTERY_PACK, 1, 0.2)
                        ).build(),
                    PoolBuilder(type = Type.COMPLETE)
                        .addEntry(
                            Entry(ModItems.STEEL_BLOCK, 1, 0.5)
                        ).build()
                )
        )

        this.add(
            ModEntities.SODAYO_PICK_UP_ROCKET,
            LootBuilder()
                .addPool(
                    PoolBuilder(type = Type.COMPLETE)
                        .source(ModDamageTypes.REPAIR_TOOL)
                        .addEntry(
                            Entry(ModItems.STEEL_BLOCK, 1, 1.0),
                            Entry(ModItems.MORTAR_BARREL, 6, 1.0),
                            Entry(ModItems.WHEEL,2, 0.5),
                            Entry(ModItems.LARGE_MOTOR, 1, 0.5),
                            Entry(ModItems.MEDIUM_BATTERY_PACK, 1, 0.2)
                        ).build(),
                    PoolBuilder(type = Type.COMPLETE)
                        .addEntry(
                            Entry(ModItems.STEEL_BLOCK, 1, 0.5)
                        ).build()
                )
        )

        this.add(
            ModEntities.SODAYO_PICK_UP_TOW,
            LootBuilder()
                .addPool(
                    PoolBuilder(type = Type.COMPLETE)
                        .source(ModDamageTypes.REPAIR_TOOL)
                        .addEntry(
                            Entry(ModItems.STEEL_BLOCK, 1, 1.0),
                            Entry(ModItems.MORTAR_BARREL, 1, 0.5),
                            Entry(ModItems.ARTILLERY_INDICATOR, 1, 0.5),
                            Entry(ModItems.WHEEL,2, 0.5),
                            Entry(ModItems.LARGE_MOTOR, 1, 0.5),
                            Entry(ModItems.MEDIUM_BATTERY_PACK, 1, 0.2)
                        ).build(),
                    PoolBuilder(type = Type.COMPLETE)
                        .addEntry(
                            Entry(ModItems.STEEL_BLOCK, 1, 0.5)
                        ).build()
                )
        )

        this.add(
            ModEntities.SPEEDBOAT,
            LootBuilder()
                .addPool(
                    PoolBuilder(type = Type.COMPLETE)
                        .source(ModDamageTypes.REPAIR_TOOL)
                        .addEntry(
                            Entry(ModItems.STEEL_BLOCK, 1, 1.0),
                            Entry(ModItems.LIGHT_ARMAMENT_MODULE, 1, 0.5),
                            Entry(ModItems.LARGE_PROPELLER,1, 0.5),
                            Entry(ModItems.LARGE_MOTOR, 1, 0.5),
                            Entry(ModItems.SMALL_BATTERY_PACK, 1, 0.2)
                        ).build(),
                    PoolBuilder(type = Type.COMPLETE)
                        .addEntry(
                            Entry(ModItems.STEEL_BLOCK, 1, 0.5)
                        ).build()
                )
        )

        this.add(
            ModEntities.TRUCK,
            LootBuilder()
                .addPool(
                    PoolBuilder(type = Type.COMPLETE)
                        .source(ModDamageTypes.REPAIR_TOOL)
                        .addEntry(
                            Entry(ModItems.STEEL_BLOCK, 4, 1.0),
                            Entry(ModItems.WHEEL,3, 0.5),
                            Entry(ModItems.LARGE_MOTOR, 1, 0.5),
                            Entry(ModItems.MEDIUM_BATTERY_PACK, 1, 0.2)
                        ).build(),
                    PoolBuilder(type = Type.COMPLETE)
                        .addEntry(
                            Entry(ModItems.STEEL_BLOCK, 4, 0.5)
                        ).build()
                )
        )

        this.add(
            ModEntities.WAVEFORCE_TOWER,
            LootBuilder()
                .addPool(
                    PoolBuilder(type = Type.COMPLETE)
                        .source(ModDamageTypes.REPAIR_TOOL)
                        .addEntry(
                            Entry(ModItems.STEEL_BLOCK, 5, 1.0),
                            Entry(ModItems.CEMENTED_CARBIDE_BLOCK,1, 0.5),
                            Entry(Items.REDSTONE_BLOCK,4, 0.5),
                            Entry(ModItems.LASER_UNIT,4, 0.5),
                            Entry(ModItems.LARGE_MOTOR, 1, 0.5),
                            Entry(ModItems.LARGE_BATTERY_PACK, 1, 0.2)
                        ).build(),
                    PoolBuilder(type = Type.COMPLETE)
                        .addEntry(
                            Entry(ModItems.STEEL_BLOCK, 5, 0.5)
                        ).build()
                )
        )

        this.add(
            ModEntities.YX_100,
            LootBuilder()
                .addPool(
                    PoolBuilder(type = Type.COMPLETE)
                        .source(ModDamageTypes.REPAIR_TOOL)
                        .addEntry(
                            Entry(ModItems.STEEL_BLOCK, 4, 1.0),
                            Entry(ModItems.CEMENTED_CARBIDE_BLOCK, 12, 1.0),
                            Entry(ModItems.HEAVY_ARMAMENT_MODULE,1, 0.5),
                            Entry(ModItems.MEDIUM_ARMAMENT_MODULE,1, 0.5),
                            Entry(ModItems.TRACK, 1, 0.5),
                            Entry(ModItems.LARGE_MOTOR, 1, 0.5),
                            Entry(ModItems.LARGE_BATTERY_PACK, 1, 0.2),
                        ).build(),
                    PoolBuilder(type = Type.COMPLETE)
                        .addEntry(
                            Entry(ModItems.STEEL_BLOCK, 4, 0.5),
                            Entry(ModItems.CEMENTED_CARBIDE_BLOCK, 12, 0.5)
                        ).build(),
                    PoolBuilder(type = Type.TURRET_ONLY)
                        .source(ModDamageTypes.REPAIR_TOOL)
                        .addEntry(
                            Entry(ModItems.STEEL_BLOCK, 2, 1.0),
                            Entry(ModItems.HEAVY_ARMAMENT_MODULE,1, 0.5),
                            Entry(ModItems.MEDIUM_ARMAMENT_MODULE,1, 0.5),
                            Entry(ModItems.CEMENTED_CARBIDE_BLOCK, 3, 1.0)
                        ).build(),
                    PoolBuilder(type = Type.TURRET_ONLY)
                        .addEntry(
                            Entry(ModItems.STEEL_BLOCK, 2, 0.5),
                            Entry(ModItems.CEMENTED_CARBIDE_BLOCK, 3, 0.5)
                        ).build(),
                    PoolBuilder(type = Type.VEHICLE_ONLY)
                        .source(ModDamageTypes.REPAIR_TOOL)
                        .addEntry(
                            Entry(ModItems.STEEL_BLOCK, 2, 1.0),
                            Entry(ModItems.CEMENTED_CARBIDE_BLOCK, 9, 1.0),
                            Entry(ModItems.TRACK, 1, 0.5),
                            Entry(ModItems.LARGE_MOTOR, 1, 0.5),
                            Entry(ModItems.LARGE_BATTERY_PACK, 1, 0.2),
                        ).build(),
                    PoolBuilder(type = Type.VEHICLE_ONLY)
                        .addEntry(
                            Entry(ModItems.STEEL_BLOCK, 2, 0.5),
                            Entry(ModItems.CEMENTED_CARBIDE_BLOCK, 9, 0.5)
                        ).build(),
                )
        )
    }

    override fun getName(): String = "Superb Warfare Wreckage Loot"

    private fun createDefaultLoot(type: EntityType<out VehicleEntity>) {
        this.add(
            type,
            LootBuilder()
                .addPool(
                    PoolBuilder(type = Type.COMPLETE)
                        .source(ModDamageTypes.REPAIR_TOOL)
                        .addEntry(
                            Entry(ModItems.STEEL_BLOCK, 1, 1.0),
                        ),
                    PoolBuilder(type = Type.COMPLETE)
                        .addEntry(
                            Entry(ModItems.STEEL_BLOCK, 1, 0.2),
                        ),
                )
        )
    }
}