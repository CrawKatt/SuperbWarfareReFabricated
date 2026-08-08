package com.atsuishio.superbwarfare.init

import com.atsuishio.superbwarfare.Mod
import com.atsuishio.superbwarfare.config.server.SpawnConfig
import com.atsuishio.superbwarfare.entity.living.DPSGeneratorEntity
import com.atsuishio.superbwarfare.entity.living.SenpaiEntity
import com.atsuishio.superbwarfare.entity.living.SteelCoilEntity
import com.atsuishio.superbwarfare.entity.living.TargetEntity
import com.atsuishio.superbwarfare.entity.projectile.*
import com.atsuishio.superbwarfare.entity.vehicle.*
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricDefaultAttributeRegistry
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.Difficulty
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.MobCategory
import net.minecraft.world.entity.SpawnPlacementTypes
import net.minecraft.world.entity.SpawnPlacements
import net.minecraft.world.entity.monster.Monster
import net.minecraft.world.level.Level
import net.minecraft.world.level.levelgen.Heightmap

@Suppress("unused")
object ModEntities {
    // Living Entities
    @JvmField
    val TARGET: EntityType<TargetEntity> = register(
        "target",
        EntityType.Builder.of(::TargetEntity, MobCategory.CREATURE)
            .clientTrackingRange(64).updateInterval(3).fireImmune().eyeHeight(1.57f).sized(0.875f, 2f)
    )

    @JvmField
    val DPS_GENERATOR: EntityType<DPSGeneratorEntity> = register(
        "dps_generator",
        EntityType.Builder.of(::DPSGeneratorEntity, MobCategory.CREATURE)
            .clientTrackingRange(64).updateInterval(3).fireImmune().eyeHeight(1.57f).sized(0.875f, 2f)
    )

    @JvmField
    val SENPAI: EntityType<SenpaiEntity> = register(
        "senpai",
        EntityType.Builder.of(::SenpaiEntity, MobCategory.MONSTER)
            .clientTrackingRange(64).updateInterval(3).sized(0.65f, 2f).eyeHeight(1.75f)
    )

    @JvmField
    val STEEL_COIL: EntityType<SteelCoilEntity> = register(
        "steel_coil",
        EntityType.Builder.of(::SteelCoilEntity, MobCategory.MONSTER)
            .clientTrackingRange(64).updateInterval(3).sized(2f, 2f).fireImmune()
    )

    // Misc Entities
    @JvmField
    val FLARE_DECOY: EntityType<FlareDecoyEntity> = register(
        "flare_decoy",
        misc(::FlareDecoyEntity).clientTrackingRange(64).updateInterval(1).noSave().sized(1f, 1f)
    )

    @JvmField
    val PRISMATIC_BOLT: EntityType<PrismaticBoltEntity> = register(
        "prismatic_bolt",
        misc(::PrismaticBoltEntity).clientTrackingRange(64).updateInterval(1).noSave().noSummon().fireImmune()
            .sized(0.05f, 0.05f)
    )

    @JvmField
    val SMOKE_DECOY: EntityType<SmokeDecoyEntity> = register(
        "smoke_decoy",
        misc(::SmokeDecoyEntity).clientTrackingRange(64).updateInterval(1).noSave().sized(5.5f, 5.5f)
    )

    @JvmField
    val CLAYMORE: EntityType<ClaymoreEntity> = register(
        "claymore",
        misc(::ClaymoreEntity).clientTrackingRange(64).updateInterval(1).sized(0.25f, 0.25f)
    )

    @JvmField
    val BLU_43: EntityType<Blu43Entity> = register(
        "blu_43",
        misc(::Blu43Entity).clientTrackingRange(32).updateInterval(1).sized(0.12f, 0.05f)
    )

    @JvmField
    val TM_62: EntityType<Tm62Entity> =
        register("tm_62", misc(::Tm62Entity).clientTrackingRange(32).updateInterval(1).sized(0.5f, 0.15f))

    @JvmField
    val PTKM_1R: EntityType<Ptkm1rEntity> = register(
        "ptkm_1r",
        misc(::Ptkm1rEntity).clientTrackingRange(64).updateInterval(1).sized(0.2f, 0.7f)
    )

    @JvmField
    val C4: EntityType<C4Entity> =
        register("c4", misc(::C4Entity).clientTrackingRange(64).updateInterval(1).sized(0.5f, 0.5f))

    @JvmField
    val MEDICAL_KIT: EntityType<MedicalKitEntity> = register(
        "medical_kit",
        misc(::MedicalKitEntity).clientTrackingRange(64).updateInterval(1).sized(0.4f, 0.2f)
    )

    @JvmField
    val EDD: EntityType<EDDEntity> =
        register("edd", misc(::EDDEntity).clientTrackingRange(10).eyeHeight(0f).updateInterval(Int.MAX_VALUE).sized(0.5f, 0.5f))

    // Projectiles
    @JvmField
    val TASER_BULLET: EntityType<TaserBulletEntity> =
        register("taser_bullet", fastProjectile(::TaserBulletEntity, true).sized(0.25f, 0.25f))

    @JvmField
    val WHITE_PHOSPHORUS_PROJECTILE: EntityType<WhitePhosphorusProjectileEntity> = register(
        "white_phosphorus_projectile",
        fastProjectile(::WhitePhosphorusProjectileEntity, true).sized(0.1f, 0.1f)
    )

    // Fast Projectiles
    @JvmField
    val SUPER_STAR_PROJECTILE: EntityType<SuperStarProjectileEntity> =
        register("super_star_projectile", fastProjectile(::SuperStarProjectileEntity).sized(0.75f, 0.75f))

    @JvmField
    val SMALL_CANNON_SHELL: EntityType<SmallCannonShellEntity> =
        register("small_cannon_shell", fastProjectile(::SmallCannonShellEntity).sized(0.25f, 0.25f))

    @JvmField
    val RPG_ROCKET_TBG: EntityType<RpgRocketTBGEntity> =
        register("rpg_rocket_tbg", fastProjectile(::RpgRocketTBGEntity).sized(0.5f, 0.5f))

    @JvmField
    val RPG_ROCKET_STANDARD: EntityType<RpgRocketStandardEntity> =
        register("rpg_rocket_standard", fastProjectile(::RpgRocketStandardEntity).sized(0.5f, 0.5f))

    @JvmField
    val MORTAR_SHELL: EntityType<MortarShellEntity> =
        register("mortar_shell", fastProjectile(::MortarShellEntity).sized(0.5f, 0.5f))

    @JvmField
    val PROJECTILE: EntityType<ProjectileEntity> =
        register("projectile", fastProjectile(::ProjectileEntity).sized(0.25f, 0.25f))

    @JvmField
    val CANNON_SHELL: EntityType<CannonShellEntity> =
        register("cannon_shell", fastProjectile(::CannonShellEntity).sized(0.75f, 0.75f))

    @JvmField
    val GUN_GRENADE: EntityType<GunGrenadeEntity> =
        register("gun_grenade", fastProjectile(::GunGrenadeEntity).sized(0.5f, 0.5f))

    @JvmField
    val GRAPESHOT: EntityType<GrapeshotEntity> =
        register("grapeshot", fastProjectile(::GrapeshotEntity).sized(0.5f, 0.5f))

    @JvmField
    val MELON_BOMB: EntityType<MelonBombEntity> =
        register("melon_bomb", fastProjectile(::MelonBombEntity).sized(1f, 1f))

    @JvmField
    val PTKM_PROJECTILE: EntityType<PtkmProjectileEntity> =
        register("ptkm_projectile", fastProjectile(::PtkmProjectileEntity).sized(0.5f, 0.5f))

    @JvmField
    val HAND_GRENADE: EntityType<HandGrenadeEntity> =
        register("hand_grenade", fastProjectile(::HandGrenadeEntity, true).sized(0.3f, 0.3f))

    @JvmField
    val RGO_GRENADE: EntityType<RgoGrenadeEntity> =
        register("rgo_grenade", fastProjectile(::RgoGrenadeEntity, true).sized(0.3f, 0.3f))

    @JvmField
    val M18_SMOKE_GRENADE: EntityType<M18SmokeGrenadeEntity> =
        register("m18_smoke_grenade", fastProjectile(::M18SmokeGrenadeEntity, true).sized(0.3f, 0.3f))

    @JvmField
    val JAVELIN_MISSILE: EntityType<JavelinMissileEntity> =
        register("javelin_missile", fastProjectile(::JavelinMissileEntity).sized(0.5f, 0.5f))

    @JvmField
    val IGLA_MISSILE: EntityType<IglaMissileEntity> =
        register("igla_9k38_missile", fastProjectile(::IglaMissileEntity).sized(0.5f, 0.5f))

    @JvmField
    val RU_9M336_MISSILE: EntityType<Ru9m336MissileEntity> =
        register("ru_9m336_missile", fastProjectile(::Ru9m336MissileEntity).sized(0.5f, 0.5f))

    @JvmField
    val AGM_65: EntityType<Agm65Entity> =
        register("agm_65", fastProjectile(::Agm65Entity).sized(0.75f, 0.75f))

    @JvmField
    val KH_39: EntityType<Kh39Entity> =
        register("kh_39", fastProjectile(::Kh39Entity).sized(0.75f, 0.75f))

    @JvmField
    val SMALL_ROCKET: EntityType<SmallRocketEntity> =
        register("small_rocket", fastProjectile(::SmallRocketEntity).sized(0.5f, 0.5f))

    @JvmField
    val MEDIUM_ROCKET: EntityType<MediumRocketEntity> =
        register("medium_rocket", fastProjectile(::MediumRocketEntity).sized(0.5f, 0.5f))

    @JvmField
    val WIRE_GUIDE_MISSILE: EntityType<WireGuideMissileEntity> =
        register("wire_guide_missile", fastProjectile(::WireGuideMissileEntity).fireImmune().sized(0.5f, 0.5f))

    @JvmField
    val SWARM_DRONE: EntityType<SwarmDroneEntity> =
        register("swarm_drone", fastProjectile(::SwarmDroneEntity).fireImmune().sized(0.5f, 0.5f))

    @JvmField
    val MK_82: EntityType<Mk82Entity> =
        register("mk_82", fastProjectile(::Mk82Entity).sized(0.8f, 0.8f))

    @JvmField
    val SC_250: EntityType<Sc250Entity> =
        register("sc_250", fastProjectile(::Sc250Entity).sized(0.7f, 0.7f))

    @JvmField
    val SC_50: EntityType<Sc50Entity> =
        register("sc_50", fastProjectile(::Sc50Entity).sized(0.4f, 0.4f))

    // Vehicles
    // Turrets
    @JvmField
    val TYPE_63: EntityType<Type63Entity> =
        register("type_63", vehicle(::Type63Entity).sized(1f, 1.5f))

    @JvmField
    val MK_42: EntityType<Mk42Entity> =
        register("mk_42", vehicle(::Mk42Entity).sized(3.4f, 3.5f))

    @JvmField
    val HPJ_11: EntityType<Hpj11Entity> =
        register("hpj_11", vehicle(::Hpj11Entity).sized(2.8f, 2.4f))

    @JvmField
    val MLE_1934: EntityType<Mle1934Entity> =
        register("mle_1934", vehicle(::Mle1934Entity).sized(4.5f, 2.8f))

    @JvmField
    val BL_132: EntityType<Bl132Entity> =
        register("bl_132", vehicle(::Bl132Entity).sized(7f, 4.4375f))

    @JvmField
    val ANNIHILATOR: EntityType<AnnihilatorEntity> =
        register("annihilator", vehicle(::AnnihilatorEntity).sized(13f, 4.2f))

    @JvmField
    val LASER_TOWER: EntityType<LaserTowerEntity> =
        register("laser_tower", vehicle(::LaserTowerEntity).sized(0.9f, 1.65f))

    @JvmField
    val WAVEFORCE_TOWER: EntityType<WaveforceTowerEntity> =
        register("waveforce_tower", vehicle(::WaveforceTowerEntity).sized(1.75f, 3.3f))

    @JvmField
    val TOW: EntityType<TowEntity> =
        register("tow", vehicle(::TowEntity).sized(0.5f, 1.35f))

    // Boats
    @JvmField
    val SPEEDBOAT: EntityType<SpeedboatEntity> =
        register("speedboat", vehicle(::SpeedboatEntity).sized(3.0f, 2.0f))

    @JvmField
    val TINY_SPEEDBOAT: EntityType<TinySpeedboatEntity> =
        register("tiny_speedboat", vehicle(::TinySpeedboatEntity).sized(1.4f, 0.6f))

    // Land Vehicles
    @JvmField
    val WHEEL_CHAIR: EntityType<WheelChairEntity> =
        register("wheel_chair", vehicle(::WheelChairEntity).sized(1.0f, 1.0f))

    @JvmField
    val LAV_150: EntityType<Lav150Entity> =
        register("lav_150", vehicle(::Lav150Entity).sized(2.8f, 2.45f))

    @JvmField
    val LAV_AD: EntityType<LavAdEntity> =
        register("lav_ad", vehicle(::LavAdEntity).sized(2.8f, 2.35f))

    @JvmField
    val LAV_25: EntityType<Lav25Entity> =
        register("lav_25", vehicle(::Lav25Entity).sized(2.8f, 2.35f))

    @JvmField
    val BMP_2: EntityType<Bmp2Entity> =
        register("bmp_2", vehicle(::Bmp2Entity).sized(3.6f, 2.1f))

    @JvmField
    val BRADLEY: EntityType<BradleyEntity> =
        register("bradley", vehicle(::BradleyEntity).sized(3.6f, 2.3f))

    @JvmField
    val ZTZ_99A: EntityType<Ztz99aEntity> =
        register("ztz_99a", vehicle(::Ztz99aEntity).sized(4.62f, 2.2f))

    @JvmField
    val T_90A: EntityType<T90aEntity> =
        register("t_90a", vehicle(::T90aEntity).sized(4.62f, 2f))

    @JvmField
    val M_1A_2: EntityType<M1A2Entity> =
        register("m_1a_2", vehicle(::M1A2Entity).sized(4.62f, 2f))

    @JvmField
    val YX_100: EntityType<Yx100Entity> =
        register("yx_100", vehicle(::Yx100Entity).sized(5.75f, 4.0625f))

    @JvmField
    val PRISM_TANK: EntityType<PrismTankEntity> =
        register("prism_tank", vehicle(::PrismTankEntity).sized(5f, 2.6f))

    @JvmField
    val PLZ_05: EntityType<Plz05Entity> =
        register("plz_05", vehicle(::Plz05Entity).sized(4.6f, 3.25f))

    // Aircraft
    @JvmField
    val TOM_6: EntityType<Tom6Entity> =
        register("tom_6", vehicle(::Tom6Entity).sized(1.05f, 1.0f))

    @JvmField
    val AH_6: EntityType<Ah6Entity> =
        register("ah_6", vehicle(::Ah6Entity).sized(2.25f, 2.175f))

    @JvmField
    val MI_28: EntityType<Mi28Entity> =
        register("mi_28", vehicle(::Mi28Entity).sized(3.375f, 3.375f))

    @JvmField
    val KV_16: EntityType<Kv16Entity> =
        register("kv_16", vehicle(::Kv16Entity).sized(1f, 1f))

    @JvmField
    val JU_87: EntityType<Ju87Entity> =
        register("ju_87", vehicle(::Ju87Entity).sized(3f, 2.5f))

    @JvmField
    val A_10A: EntityType<A10Entity> =
        register("a_10a", vehicle(::A10Entity).sized(3.375f, 2.625f))

    // Special
    @JvmField
    val DRONE: EntityType<DroneEntity> =
        register("drone", misc(::DroneEntity).clientTrackingRange(512).updateInterval(1).sized(0.6f, 0.2f))

    @JvmField
    val MORTAR: EntityType<MortarEntity> =
        register("mortar", vehicle(::MortarEntity).sized(0.8f, 1.4f))

    @JvmField
    val VEHICLE_ASSEMBLING_TABLE: EntityType<VehicleAssemblingTableVehicleEntity> =
        register("vehicle_assembling_table", vehicle(::VehicleAssemblingTableVehicleEntity).sized(2f, 1.875f))

    @JvmField
    val SODAYO_PICK_UP: EntityType<SodayoPickUpEntity> =
        register("sodayo_pick_up", vehicle(::SodayoPickUpEntity).sized(2.4f, 2f))

    @JvmField
    val SODAYO_PICK_UP_HMG: EntityType<SodayoPickUpHmgEntity> =
        register("sodayo_pick_up_hmg", vehicle(::SodayoPickUpHmgEntity).sized(2.4f, 2f))

    @JvmField
    val SODAYO_PICK_UP_ROCKET: EntityType<SodayoPickUpRocketEntity> =
        register("sodayo_pick_up_rocket", vehicle(::SodayoPickUpRocketEntity).sized(2.4f, 2f))

    @JvmField
    val SODAYO_PICK_UP_TOW: EntityType<SodayoPickUpTowEntity> =
        register("sodayo_pick_up_tow", vehicle(::SodayoPickUpTowEntity).sized(2.4f, 2f))

    @JvmField
    val TRUCK: EntityType<TruckEntity> =
        register("truck", vehicle(::TruckEntity).sized(2.6f, 3f))

    @JvmField
    val TURRET_WRECK: EntityType<TurretWreckEntity> =
        register("turret_wreck", vehicle(::TurretWreckEntity).sized(2.4f, 1.2f))

    @JvmField
    val NO_VELOCITY_UPDATES: Set<EntityType<*>> = setOf(
        SUPER_STAR_PROJECTILE,
        SMALL_CANNON_SHELL,
        RPG_ROCKET_TBG,
        RPG_ROCKET_STANDARD,
        MORTAR_SHELL,
        PROJECTILE,
        CANNON_SHELL,
        GUN_GRENADE,
        GRAPESHOT,
        MELON_BOMB,
        PTKM_PROJECTILE,
        JAVELIN_MISSILE,
        IGLA_MISSILE,
        RU_9M336_MISSILE,
        AGM_65,
        KH_39,
        SMALL_ROCKET,
        MEDIUM_ROCKET,
        WIRE_GUIDE_MISSILE,
        SWARM_DRONE,
        MK_82,
        SC_250,
        SC_50
    )

    private fun <T : Entity> register(
        name: String,
        entityTypeBuilder: EntityType.Builder<T>
    ): EntityType<T> {
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, Mod.loc(name), entityTypeBuilder.build(name))
    }

    private fun <T : Entity> misc(
        entity: (EntityType<T>, Level) -> T
    ): EntityType.Builder<T> = EntityType.Builder.of(entity, MobCategory.MISC)

    private fun <T : Entity> vehicle(
        entity: (EntityType<T>, Level) -> T
    ): EntityType.Builder<T> = misc(entity)
        .clientTrackingRange(512)
        .updateInterval(1)
        .fireImmune()

    private fun <T : Entity> fastProjectile(
        entity: (EntityType<T>, Level) -> T,
        receiveVelocityUpdates: Boolean = false
    ): EntityType.Builder<T> {
        return misc(entity)
            .clientTrackingRange(64)
            .updateInterval(1)
            .noSave()
    }

    @JvmStatic
    fun registerAttributes() {
        FabricDefaultAttributeRegistry.register(TARGET, TargetEntity.createAttributes().build())
        FabricDefaultAttributeRegistry.register(DPS_GENERATOR, DPSGeneratorEntity.createAttributes().build())
        FabricDefaultAttributeRegistry.register(SENPAI, SenpaiEntity.createAttributes().build())
        FabricDefaultAttributeRegistry.register(STEEL_COIL, SteelCoilEntity.createAttributes().build())
    }

    @JvmStatic
    fun registerSpawnPlacements() {
        SpawnPlacements.register(
            SENPAI,
            SpawnPlacementTypes.ON_GROUND,
            Heightmap.Types.MOTION_BLOCKING_NO_LEAVES
        ) { entityType, world, reason, pos, random ->
            world.difficulty != Difficulty.PEACEFUL
                    && SpawnConfig.SPAWN_SENPAI.get()
                    && Monster.isDarkEnoughToSpawn(world, pos, random)
                    && Mob.checkMobSpawnRules(entityType, world, reason, pos, random)
        }

        SpawnPlacements.register(
            STEEL_COIL,
            SpawnPlacementTypes.ON_GROUND,
            Heightmap.Types.MOTION_BLOCKING_NO_LEAVES
        ) { entityType, world, reason, pos, random ->
            world.difficulty != Difficulty.PEACEFUL
                    && SpawnConfig.SPAWN_STEEL_COIL.get()
                    && Monster.isDarkEnoughToSpawn(world, pos, random)
                    && Mob.checkMobSpawnRules(entityType, world, reason, pos, random)
        }
    }

    @JvmStatic
    fun init() {
        registerAttributes()
        registerSpawnPlacements()
    }
}
