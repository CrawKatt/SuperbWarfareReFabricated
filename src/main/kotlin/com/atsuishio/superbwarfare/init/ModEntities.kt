package com.atsuishio.superbwarfare.init

import com.atsuishio.superbwarfare.config.server.SpawnConfig
import com.atsuishio.superbwarfare.entity.living.DPSGeneratorEntity
import com.atsuishio.superbwarfare.entity.living.SenpaiEntity
import com.atsuishio.superbwarfare.entity.living.SteelCoilEntity
import com.atsuishio.superbwarfare.entity.living.TargetEntity
import com.atsuishio.superbwarfare.entity.projectile.*
import com.atsuishio.superbwarfare.entity.vehicle.*
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricDefaultAttributeRegistry
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricEntityTypeBuilder
import net.minecraft.world.Difficulty
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityDimensions
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.MobCategory
import net.minecraft.world.entity.SpawnPlacements
import net.minecraft.world.entity.monster.Monster
import net.minecraft.world.level.Level
import net.minecraft.world.level.levelgen.Heightmap
import java.util.function.Supplier

@Suppress("unused")
object ModEntities {
    // Living Entities
    @JvmField
    val TARGET: EntityType<TargetEntity> = register(
        "target",
        FabricEntityTypeBuilder.create(MobCategory.CREATURE, ::TargetEntity)
            .trackRangeChunks(64).trackedUpdateRate(3).fireImmune().dimensions(EntityDimensions.scalable(0.875f, 2f))
    )

    @JvmField
    val DPS_GENERATOR: EntityType<DPSGeneratorEntity> = register(
        "dps_generator",
        FabricEntityTypeBuilder.create(MobCategory.CREATURE, ::DPSGeneratorEntity)
            .trackRangeChunks(64).trackedUpdateRate(3).fireImmune().dimensions(EntityDimensions.scalable(0.875f, 2f))
    )

    @JvmField
    val SENPAI: EntityType<SenpaiEntity> = register(
        "senpai",
        FabricEntityTypeBuilder.create(MobCategory.MONSTER, ::SenpaiEntity)
            .trackRangeChunks(64).trackedUpdateRate(3).dimensions(EntityDimensions.scalable(0.65f, 2f))
    )

    @JvmField
    val STEEL_COIL: EntityType<SteelCoilEntity> = register(
        "steel_coil",
        FabricEntityTypeBuilder.create(MobCategory.MONSTER, ::SteelCoilEntity)
            .trackRangeChunks(64).trackedUpdateRate(3).dimensions(EntityDimensions.scalable(2f, 2f)).fireImmune()
    )

    // Misc Entities
    @JvmField
    val FLARE_DECOY: EntityType<FlareDecoyEntity> = register(
        "flare_decoy",
        misc(::FlareDecoyEntity).trackRangeChunks(64).trackedUpdateRate(1).disableSaving().dimensions(EntityDimensions.scalable(1f, 1f))
    )

    @JvmField
    val PRISMATIC_BOLT: EntityType<PrismaticBoltEntity> = register(
        "prismatic_bolt",
        misc(::PrismaticBoltEntity).trackRangeChunks(64).trackedUpdateRate(1).disableSaving().disableSummon().fireImmune()
            .dimensions(EntityDimensions.scalable(0.05f, 0.05f))
    )

    @JvmField
    val SMOKE_DECOY: EntityType<SmokeDecoyEntity> = register(
        "smoke_decoy",
        misc(::SmokeDecoyEntity).trackRangeChunks(64).trackedUpdateRate(1).disableSaving().dimensions(EntityDimensions.scalable(5.5f, 5.5f))
    )

    @JvmField
    val CLAYMORE: EntityType<ClaymoreEntity> = register(
        "claymore",
        misc(::ClaymoreEntity).trackRangeChunks(64).trackedUpdateRate(1).dimensions(EntityDimensions.scalable(0.25f, 0.25f))
    )

    @JvmField
    val BLU_43: EntityType<Blu43Entity> = register(
        "blu_43",
        misc(::Blu43Entity).trackRangeChunks(32).trackedUpdateRate(1).dimensions(EntityDimensions.scalable(0.12f, 0.05f))
    )

    @JvmField
    val TM_62: EntityType<Tm62Entity> =
        register("tm_62", misc(::Tm62Entity).trackRangeChunks(32).trackedUpdateRate(1).dimensions(EntityDimensions.scalable(0.5f, 0.15f)))

    @JvmField
    val PTKM_1R: EntityType<Ptkm1rEntity> = register(
        "ptkm_1r",
        misc(::Ptkm1rEntity).trackRangeChunks(64).trackedUpdateRate(1).dimensions(EntityDimensions.scalable(0.2f, 0.7f))
    )

    @JvmField
    val C4: EntityType<C4Entity> =
        register("c4", misc(::C4Entity).trackRangeChunks(64).trackedUpdateRate(1).dimensions(EntityDimensions.scalable(0.5f, 0.5f)))

    @JvmField
    val MEDICAL_KIT: EntityType<MedicalKitEntity> = register(
        "medical_kit",
        misc(::MedicalKitEntity).trackRangeChunks(64).trackedUpdateRate(1).dimensions(EntityDimensions.scalable(0.4f, 0.2f))
    )

    @JvmField
    val EDD = register("edd", misc(::EDDEntity).trackRangeChunks(10).trackedUpdateRate(Int.MAX_VALUE).dimensions(EntityDimensions.scalable(0.5f, 0.5f)))

    // Projectiles
    @JvmField
    val TASER_BULLET: EntityType<TaserBulletEntity> =
        register("taser_bullet", fastProjectile(::TaserBulletEntity, true).dimensions(EntityDimensions.scalable(0.25f, 0.25f)))

    @JvmField
    val WHITE_PHOSPHORUS_PROJECTILE: EntityType<WhitePhosphorusProjectileEntity> = register(
        "white_phosphorus_projectile",
        fastProjectile(::WhitePhosphorusProjectileEntity, true).dimensions(EntityDimensions.scalable(0.1f, 0.1f))
    )

    // Fast Projectiles
    @JvmField
    val SUPER_STAR_PROJECTILE: EntityType<SuperStarProjectileEntity> =
        register("super_star_projectile", fastProjectile(::SuperStarProjectileEntity).dimensions(EntityDimensions.scalable(0.75f, 0.75f)))

    @JvmField
    val SMALL_CANNON_SHELL: EntityType<SmallCannonShellEntity> =
        register("small_cannon_shell", fastProjectile(::SmallCannonShellEntity).dimensions(EntityDimensions.scalable(0.25f, 0.25f)))

    @JvmField
    val RPG_ROCKET_TBG: EntityType<RpgRocketTBGEntity> =
        register("rpg_rocket_tbg", fastProjectile(::RpgRocketTBGEntity).dimensions(EntityDimensions.scalable(0.5f, 0.5f)))

    @JvmField
    val RPG_ROCKET_STANDARD: EntityType<RpgRocketStandardEntity> =
        register("rpg_rocket_standard", fastProjectile(::RpgRocketStandardEntity).dimensions(EntityDimensions.scalable(0.5f, 0.5f)))

    @JvmField
    val MORTAR_SHELL: EntityType<MortarShellEntity> =
        register("mortar_shell", fastProjectile(::MortarShellEntity).dimensions(EntityDimensions.scalable(0.5f, 0.5f)))

    @JvmField
    val PROJECTILE: EntityType<ProjectileEntity> =
        register("projectile", fastProjectile(::ProjectileEntity).dimensions(EntityDimensions.scalable(0.25f, 0.25f)))

    @JvmField
    val CANNON_SHELL: EntityType<CannonShellEntity> =
        register("cannon_shell", fastProjectile(::CannonShellEntity).dimensions(EntityDimensions.scalable(0.75f, 0.75f)))

    @JvmField
    val GUN_GRENADE: EntityType<GunGrenadeEntity> =
        register("gun_grenade", fastProjectile(::GunGrenadeEntity).dimensions(EntityDimensions.scalable(0.5f, 0.5f)))

    @JvmField
    val GRAPESHOT: EntityType<GrapeshotEntity> =
        register("grapeshot", fastProjectile(::GrapeshotEntity).dimensions(EntityDimensions.scalable(0.5f, 0.5f)))

    @JvmField
    val MELON_BOMB: EntityType<MelonBombEntity> =
        register("melon_bomb", fastProjectile(::MelonBombEntity).dimensions(EntityDimensions.scalable(1f, 1f)))

    @JvmField
    val PTKM_PROJECTILE: EntityType<PtkmProjectileEntity> =
        register("ptkm_projectile", fastProjectile(::PtkmProjectileEntity).dimensions(EntityDimensions.scalable(0.5f, 0.5f)))

    @JvmField
    val HAND_GRENADE: EntityType<HandGrenadeEntity> =
        register("hand_grenade", fastProjectile(::HandGrenadeEntity, true).dimensions(EntityDimensions.scalable(0.3f, 0.3f)))

    @JvmField
    val RGO_GRENADE: EntityType<RgoGrenadeEntity> =
        register("rgo_grenade", fastProjectile(::RgoGrenadeEntity, true).dimensions(EntityDimensions.scalable(0.3f, 0.3f)))

    @JvmField
    val M18_SMOKE_GRENADE: EntityType<M18SmokeGrenadeEntity> =
        register("m18_smoke_grenade", fastProjectile(::M18SmokeGrenadeEntity, true).dimensions(EntityDimensions.scalable(0.3f, 0.3f)))

    @JvmField
    val JAVELIN_MISSILE: EntityType<JavelinMissileEntity> =
        register("javelin_missile", fastProjectile(::JavelinMissileEntity).dimensions(EntityDimensions.scalable(0.5f, 0.5f)))

    @JvmField
    val IGLA_MISSILE: EntityType<IglaMissileEntity> =
        register("igla_9k38_missile", fastProjectile(::IglaMissileEntity).dimensions(EntityDimensions.scalable(0.5f, 0.5f)))

    @JvmField
    val RU_9M336_MISSILE: EntityType<Ru9m336MissileEntity> =
        register("ru_9m336_missile", fastProjectile(::Ru9m336MissileEntity).dimensions(EntityDimensions.scalable(0.5f, 0.5f)))

    @JvmField
    val AGM_65: EntityType<Agm65Entity> =
        register("agm_65", fastProjectile(::Agm65Entity).dimensions(EntityDimensions.scalable(0.75f, 0.75f)))

    @JvmField
    val KH_39: EntityType<Kh39Entity> =
        register("kh_39", fastProjectile(::Kh39Entity).dimensions(EntityDimensions.scalable(0.75f, 0.75f)))

    @JvmField
    val SMALL_ROCKET: EntityType<SmallRocketEntity> =
        register("small_rocket", fastProjectile(::SmallRocketEntity).dimensions(EntityDimensions.scalable(0.5f, 0.5f)))

    @JvmField
    val MEDIUM_ROCKET: EntityType<MediumRocketEntity> =
        register("medium_rocket", fastProjectile(::MediumRocketEntity).dimensions(EntityDimensions.scalable(0.5f, 0.5f)))

    @JvmField
    val WIRE_GUIDE_MISSILE: EntityType<WireGuideMissileEntity> =
        register("wire_guide_missile", fastProjectile(::WireGuideMissileEntity).fireImmune().dimensions(EntityDimensions.scalable(0.5f, 0.5f)))

    @JvmField
    val SWARM_DRONE: EntityType<SwarmDroneEntity> =
        register("swarm_drone", fastProjectile(::SwarmDroneEntity).fireImmune().dimensions(EntityDimensions.scalable(0.5f, 0.5f)))

    @JvmField
    val MK_82: EntityType<Mk82Entity> =
        register("mk_82", fastProjectile(::Mk82Entity).dimensions(EntityDimensions.scalable(0.8f, 0.8f)))

    @JvmField
    val SC_250: EntityType<Sc250Entity> =
        register("sc_250", fastProjectile(::Sc250Entity).dimensions(EntityDimensions.scalable(0.7f, 0.7f)))

    @JvmField
    val SC_50: EntityType<Sc50Entity> =
        register("sc_50", fastProjectile(::Sc50Entity).dimensions(EntityDimensions.scalable(0.4f, 0.4f)))

    // Vehicles
    // Turrets
    @JvmField
    val TYPE_63: EntityType<Type63Entity> =
        register("type_63", vehicle(::Type63Entity).dimensions(EntityDimensions.scalable(1f, 1.5f)))

    @JvmField
    val MK_42: EntityType<Mk42Entity> =
        register("mk_42", vehicle(::Mk42Entity).dimensions(EntityDimensions.scalable(3.4f, 3.5f)))

    @JvmField
    val HPJ_11: EntityType<Hpj11Entity> =
        register("hpj_11", vehicle(::Hpj11Entity).dimensions(EntityDimensions.scalable(2.8f, 2.4f)))

    @JvmField
    val MLE_1934: EntityType<Mle1934Entity> =
        register("mle_1934", vehicle(::Mle1934Entity).dimensions(EntityDimensions.scalable(4.5f, 2.8f)))

    @JvmField
    val BL_132: EntityType<Bl132Entity> =
        register("bl_132", vehicle(::Bl132Entity).dimensions(EntityDimensions.scalable(7f, 4.4375f)))

    @JvmField
    val ANNIHILATOR: EntityType<AnnihilatorEntity> =
        register("annihilator", vehicle(::AnnihilatorEntity).dimensions(EntityDimensions.scalable(13f, 4.2f)))

    @JvmField
    val LASER_TOWER: EntityType<LaserTowerEntity> =
        register("laser_tower", vehicle(::LaserTowerEntity).dimensions(EntityDimensions.scalable(0.9f, 1.65f)))

    @JvmField
    val WAVEFORCE_TOWER: EntityType<WaveforceTowerEntity> =
        register("waveforce_tower", vehicle(::WaveforceTowerEntity).dimensions(EntityDimensions.scalable(1.75f, 3.3f)))

    @JvmField
    val TOW: EntityType<TowEntity> =
        register("tow", vehicle(::TowEntity).dimensions(EntityDimensions.scalable(0.5f, 1.35f)))

    // Boats
    @JvmField
    val SPEEDBOAT: EntityType<SpeedboatEntity> =
        register("speedboat", vehicle(::SpeedboatEntity).dimensions(EntityDimensions.scalable(3.0f, 2.0f)))

    @JvmField
    val TINY_SPEEDBOAT: EntityType<TinySpeedboatEntity> =
        register("tiny_speedboat", vehicle(::TinySpeedboatEntity).dimensions(EntityDimensions.scalable(1.4f, 0.6f)))

    // Land Vehicles
    @JvmField
    val WHEEL_CHAIR: EntityType<WheelChairEntity> =
        register("wheel_chair", vehicle(::WheelChairEntity).dimensions(EntityDimensions.scalable(1.0f, 1.0f)))

    @JvmField
    val LAV_150: EntityType<Lav150Entity> =
        register("lav_150", vehicle(::Lav150Entity).dimensions(EntityDimensions.scalable(2.8f, 2.45f)))

    @JvmField
    val LAV_AD: EntityType<LavAdEntity> =
        register("lav_ad", vehicle(::LavAdEntity).dimensions(EntityDimensions.scalable(2.8f, 2.35f)))

    @JvmField
    val LAV_25: EntityType<Lav25Entity> =
        register("lav_25", vehicle(::Lav25Entity).dimensions(EntityDimensions.scalable(2.8f, 2.35f)))

    @JvmField
    val BMP_2: EntityType<Bmp2Entity> =
        register("bmp_2", vehicle(::Bmp2Entity).dimensions(EntityDimensions.scalable(3.6f, 2.1f)))

    @JvmField
    val BRADLEY: EntityType<BradleyEntity> =
        register("bradley", vehicle(::BradleyEntity).dimensions(EntityDimensions.scalable(3.6f, 2.3f)))

    @JvmField
    val ZTZ_99A: EntityType<Ztz99aEntity> =
        register("ztz_99a", vehicle(::Ztz99aEntity).dimensions(EntityDimensions.scalable(4.62f, 2.2f)))

    @JvmField
    val T_90A: EntityType<T90aEntity> =
        register("t_90a", vehicle(::T90aEntity).dimensions(EntityDimensions.scalable(4.62f, 2f)))

    @JvmField
    val M_1A_2: EntityType<M1A2Entity> =
        register("m_1a_2", vehicle(::M1A2Entity).dimensions(EntityDimensions.scalable(4.62f, 2f)))

    @JvmField
    val YX_100: EntityType<Yx100Entity> =
        register("yx_100", vehicle(::Yx100Entity).dimensions(EntityDimensions.scalable(5.75f, 4.0625f)))

    @JvmField
    val PRISM_TANK: EntityType<PrismTankEntity> =
        register("prism_tank", vehicle(::PrismTankEntity).dimensions(EntityDimensions.scalable(5f, 2.6f)))

    @JvmField
    val PLZ_05: EntityType<Plz05Entity> =
        register("plz_05", vehicle(::Plz05Entity).dimensions(EntityDimensions.scalable(4.6f, 3.25f)))

    // Aircraft
    @JvmField
    val TOM_6: EntityType<Tom6Entity> =
        register("tom_6", vehicle(::Tom6Entity).dimensions(EntityDimensions.scalable(1.05f, 1.0f)))

    @JvmField
    val AH_6: EntityType<Ah6Entity> =
        register("ah_6", vehicle(::Ah6Entity).dimensions(EntityDimensions.scalable(2.25f, 2.175f)))

    @JvmField
    val MI_28: EntityType<Mi28Entity> =
        register("mi_28", vehicle(::Mi28Entity).dimensions(EntityDimensions.scalable(3.375f, 3.375f)))

    @JvmField
    val KV_16: EntityType<Kv16Entity> =
        register("kv_16", vehicle(::Kv16Entity).dimensions(EntityDimensions.scalable(1f, 1f)))

    @JvmField
    val JU_87: EntityType<Ju87Entity> =
        register("ju_87", vehicle(::Ju87Entity).dimensions(EntityDimensions.scalable(3f, 2.5f)))

    @JvmField
    val A_10A: EntityType<A10Entity> =
        register("a_10a", vehicle(::A10Entity).dimensions(EntityDimensions.scalable(3.375f, 2.625f)))

    // Special
    @JvmField
    val DRONE: EntityType<DroneEntity> =
        register("drone", misc(::DroneEntity).trackRangeChunks(512).trackedUpdateRate(1).dimensions(EntityDimensions.scalable(0.6f, 0.2f)))

    @JvmField
    val MORTAR: EntityType<MortarEntity> =
        register("mortar", vehicle(::MortarEntity).dimensions(EntityDimensions.scalable(0.8f, 1.4f)))

    @JvmField
    val VEHICLE_ASSEMBLING_TABLE: EntityType<VehicleAssemblingTableVehicleEntity> =
        register("vehicle_assembling_table", vehicle(::VehicleAssemblingTableVehicleEntity).dimensions(EntityDimensions.scalable(2f, 1.875f)))

    @JvmField
    val SODAYO_PICK_UP: EntityType<SodayoPickUpEntity> =
        register("sodayo_pick_up", vehicle(::SodayoPickUpEntity).dimensions(EntityDimensions.scalable(2.4f, 2f)))

    @JvmField
    val SODAYO_PICK_UP_HMG: EntityType<SodayoPickUpHmgEntity> =
        register("sodayo_pick_up_hmg", vehicle(::SodayoPickUpHmgEntity).dimensions(EntityDimensions.scalable(2.4f, 2f)))

    @JvmField
    val SODAYO_PICK_UP_ROCKET: EntityType<SodayoPickUpRocketEntity> =
        register("sodayo_pick_up_rocket", vehicle(::SodayoPickUpRocketEntity).dimensions(EntityDimensions.scalable(2.4f, 2f)))

    @JvmField
    val SODAYO_PICK_UP_TOW: EntityType<SodayoPickUpTowEntity> =
        register("sodayo_pick_up_tow", vehicle(::SodayoPickUpTowEntity).dimensions(EntityDimensions.scalable(2.4f, 2f)))

    @JvmField
    val TRUCK: EntityType<TruckEntity> =
        register("truck", vehicle(::TruckEntity).dimensions(EntityDimensions.scalable(2.6f, 3f)))

    @JvmField
    val TURRET_WRECK: EntityType<TurretWreckEntity> =
        register("turret_wreck", vehicle(::TurretWreckEntity).dimensions(EntityDimensions.scalable(2.4f, 1.2f)))

    private fun <T : Entity> register(
        name: String,
        entityTypeBuilder: FabricEntityTypeBuilder<T>
    ): EntityType<T> = Registration.entity(name) { entityTypeBuilder.build() }

    private fun <T : Entity> misc(
        entity: (EntityType<T>, Level) -> T
    ): FabricEntityTypeBuilder<T> = FabricEntityTypeBuilder.create(
        MobCategory.MISC,
        EntityType.EntityFactory { type, level -> entity(type, level) }
    )

    private fun <T : Entity> vehicle(
        entity: (EntityType<T>, Level) -> T
    ): FabricEntityTypeBuilder<T> = misc(entity)
        .trackRangeChunks(512)
        .trackedUpdateRate(1)
        .fireImmune()

    private fun <T : Entity> fastProjectile(
        entity: (EntityType<T>, Level) -> T,
        receiveVelocityUpdates: Boolean = false
    ): FabricEntityTypeBuilder<T> {
        return misc(entity)
            .forceTrackedVelocityUpdates(receiveVelocityUpdates)
            .trackRangeChunks(64)
            .trackedUpdateRate(1)
            .disableSaving()
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
            SpawnPlacements.Type.ON_GROUND,
            Heightmap.Types.MOTION_BLOCKING_NO_LEAVES
        ) { entityType, world, reason, pos, random ->
            world.difficulty != Difficulty.PEACEFUL
                    && SpawnConfig.SPAWN_SENPAI.get()
                    && Monster.isDarkEnoughToSpawn(world, pos, random)
                    && Mob.checkMobSpawnRules(entityType, world, reason, pos, random)
        }

        SpawnPlacements.register(
            STEEL_COIL,
            SpawnPlacements.Type.ON_GROUND,
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
