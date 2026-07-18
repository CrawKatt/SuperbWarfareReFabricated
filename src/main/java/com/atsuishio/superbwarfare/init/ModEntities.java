package com.atsuishio.superbwarfare.init;

import com.atsuishio.superbwarfare.Mod;
import com.atsuishio.superbwarfare.config.server.SpawnConfig;
import com.atsuishio.superbwarfare.entity.*;
import com.atsuishio.superbwarfare.entity.projectile.*;
import com.atsuishio.superbwarfare.entity.vehicle.*;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ModEntities {

    // Living Entities
    public static final Supplier<EntityType<TargetEntity>> TARGET = register("target",
            FabricEntityTypeBuilder.<TargetEntity>create(MobCategory.CREATURE, TargetEntity::new).trackRangeChunks(64).trackedUpdateRate(3).fireImmune().dimensions(EntityDimensions.scalable(0.875f, 2f)));
    public static final Supplier<EntityType<DPSGeneratorEntity>> DPS_GENERATOR = register("dps_generator",
            FabricEntityTypeBuilder.<DPSGeneratorEntity>create(MobCategory.CREATURE, DPSGeneratorEntity::new).trackRangeChunks(64).trackedUpdateRate(3).fireImmune().dimensions(EntityDimensions.scalable(0.875f, 2f)));
    public static final Supplier<EntityType<SenpaiEntity>> SENPAI = register("senpai",
            FabricEntityTypeBuilder.<SenpaiEntity>create(MobCategory.MONSTER, SenpaiEntity::new).trackRangeChunks(64).trackedUpdateRate(3)
                    .dimensions(EntityDimensions.scalable(0.6f, 2f)));

    // Misc Entities
    public static final Supplier<EntityType<LaserEntity>> LASER = register("laser",
            FabricEntityTypeBuilder.<LaserEntity>create(MobCategory.MISC, LaserEntity::new).dimensions(EntityDimensions.scalable(0.1f, 0.1f)).fireImmune().trackedUpdateRate(1));
    public static final Supplier<EntityType<FlareDecoyEntity>> FLARE_DECOY = register("flare_decoy",
            FabricEntityTypeBuilder.<FlareDecoyEntity>create(MobCategory.MISC, FlareDecoyEntity::new).trackRangeChunks(64).trackedUpdateRate(1).disableSaving().dimensions(EntityDimensions.scalable(1f, 1f)));
    public static final Supplier<EntityType<SmokeDecoyEntity>> SMOKE_DECOY = register("smoke_decoy",
            FabricEntityTypeBuilder.<SmokeDecoyEntity>create(MobCategory.MISC, SmokeDecoyEntity::new).trackRangeChunks(64).trackedUpdateRate(1).disableSaving().dimensions(EntityDimensions.scalable(4.5f, 4.5f)));
    public static final Supplier<EntityType<ClaymoreEntity>> CLAYMORE = register("claymore",
            FabricEntityTypeBuilder.<ClaymoreEntity>create(MobCategory.MISC, ClaymoreEntity::new).trackRangeChunks(64).trackedUpdateRate(1).dimensions(EntityDimensions.scalable(0.25f, 0.25f)));

    public static final Supplier<EntityType<Blu43Entity>> BLU_43 = register("blu_43",
            FabricEntityTypeBuilder.<Blu43Entity>create(MobCategory.MISC, Blu43Entity::new).trackRangeChunks(32).trackedUpdateRate(1).dimensions(EntityDimensions.scalable(0.12f, 0.05f)));

    public static final Supplier<EntityType<Tm62Entity>> TM_62 = register("tm_62",
            FabricEntityTypeBuilder.<Tm62Entity>create(MobCategory.MISC, Tm62Entity::new).trackRangeChunks(32).trackedUpdateRate(1).dimensions(EntityDimensions.scalable(0.5f, 0.15f)));
    public static final Supplier<EntityType<Ptkm1rEntity>> PTKM_1R = register("ptkm_1r",
            FabricEntityTypeBuilder.<Ptkm1rEntity>create(MobCategory.MISC, Ptkm1rEntity::new).trackRangeChunks(64).trackedUpdateRate(1).dimensions(EntityDimensions.scalable(0.2f, 0.7f)));
    public static final Supplier<EntityType<C4Entity>> C4 = register("c4",
            FabricEntityTypeBuilder.<C4Entity>create(MobCategory.MISC, C4Entity::new).trackRangeChunks(64).trackedUpdateRate(1).dimensions(EntityDimensions.scalable(0.5f, 0.5f)));

    public static final Supplier<EntityType<MedicalKitEntity>> MEDICAL_KIT = register("medical_kit",
            FabricEntityTypeBuilder.<MedicalKitEntity>create(MobCategory.MISC, MedicalKitEntity::new).trackRangeChunks(64).trackedUpdateRate(1).dimensions(EntityDimensions.scalable(0.4f, 0.2f)));

    // Projectiles
    public static final Supplier<EntityType<TaserBulletEntity>> TASER_BULLET = register("taser_bullet",
            FabricEntityTypeBuilder.<TaserBulletEntity>create(MobCategory.MISC, TaserBulletEntity::new).trackRangeChunks(64).disableSaving()
                    .trackedUpdateRate(1).dimensions(EntityDimensions.scalable(0.25f, 0.25f)));

    // Fast Projectiles
    public static final Supplier<EntityType<SmallCannonShellEntity>> SMALL_CANNON_SHELL = register("small_cannon_shell",
            FabricEntityTypeBuilder.<SmallCannonShellEntity>create(MobCategory.MISC, SmallCannonShellEntity::new).forceTrackedVelocityUpdates(false).trackRangeChunks(64).trackedUpdateRate(1).disableSaving().dimensions(EntityDimensions.scalable(0.25f, 0.25f)));
    public static final Supplier<EntityType<RpgRocketTBGEntity>> RPG_ROCKET_TBG = register("rpg_rocket_tbg",
            FabricEntityTypeBuilder.<RpgRocketTBGEntity>create(MobCategory.MISC, RpgRocketTBGEntity::new).forceTrackedVelocityUpdates(false).trackRangeChunks(64).trackedUpdateRate(1).disableSaving().dimensions(EntityDimensions.scalable(0.5f, 0.5f)));
    public static final Supplier<EntityType<RpgRocketStandardEntity>> RPG_ROCKET_STANDARD = register("rpg_rocket_standard",
            FabricEntityTypeBuilder.<RpgRocketStandardEntity>create(MobCategory.MISC, RpgRocketStandardEntity::new).forceTrackedVelocityUpdates(false).trackRangeChunks(64).trackedUpdateRate(1).disableSaving().dimensions(EntityDimensions.scalable(0.5f, 0.5f)));
    public static final Supplier<EntityType<MortarShellEntity>> MORTAR_SHELL = register("mortar_shell",
            FabricEntityTypeBuilder.<MortarShellEntity>create(MobCategory.MISC, MortarShellEntity::new).forceTrackedVelocityUpdates(false).trackRangeChunks(64).trackedUpdateRate(1).disableSaving().dimensions(EntityDimensions.scalable(0.5f, 0.5f)));
    public static final Supplier<EntityType<ProjectileEntity>> PROJECTILE = register("projectile",
            FabricEntityTypeBuilder.<ProjectileEntity>create(MobCategory.MISC, ProjectileEntity::new).forceTrackedVelocityUpdates(false).trackRangeChunks(64).disableSaving().disableSummon().dimensions(EntityDimensions.scalable(0.25f, 0.25f)));
    public static final Supplier<EntityType<CannonShellEntity>> CANNON_SHELL = register("cannon_shell",
            FabricEntityTypeBuilder.<CannonShellEntity>create(MobCategory.MISC, CannonShellEntity::new).forceTrackedVelocityUpdates(false).trackRangeChunks(64).trackedUpdateRate(1).disableSaving().dimensions(EntityDimensions.scalable(0.75f, 0.75f)));
    public static final Supplier<EntityType<GunGrenadeEntity>> GUN_GRENADE = register("gun_grenade",
            FabricEntityTypeBuilder.<GunGrenadeEntity>create(MobCategory.MISC, GunGrenadeEntity::new).forceTrackedVelocityUpdates(false).trackRangeChunks(64).trackedUpdateRate(1).disableSaving().dimensions(EntityDimensions.scalable(0.5f, 0.5f)));
    public static final Supplier<EntityType<GrapeshotEntity>> GRAPESHOT = register("grapeshot",
            FabricEntityTypeBuilder.<GrapeshotEntity>create(MobCategory.MISC, GrapeshotEntity::new).forceTrackedVelocityUpdates(false).trackRangeChunks(64).trackedUpdateRate(1).disableSaving().dimensions(EntityDimensions.scalable(0.5f, 0.5f)));
    public static final Supplier<EntityType<MelonBombEntity>> MELON_BOMB = register("melon_bomb",
            FabricEntityTypeBuilder.<MelonBombEntity>create(MobCategory.MISC, MelonBombEntity::new).forceTrackedVelocityUpdates(false).trackRangeChunks(64).trackedUpdateRate(1).disableSaving().dimensions(EntityDimensions.scalable(1f, 1f)));
    public static final Supplier<EntityType<PtkmProjectileEntity>> PTKM_PROJECTILE = register("ptkm_projectile",
            FabricEntityTypeBuilder.<PtkmProjectileEntity>create(MobCategory.MISC, PtkmProjectileEntity::new).forceTrackedVelocityUpdates(false).trackRangeChunks(64).trackedUpdateRate(1).disableSaving().dimensions(EntityDimensions.scalable(0.5f, 0.5f)));
    public static final Supplier<EntityType<HandGrenadeEntity>> HAND_GRENADE = register("hand_grenade",
            FabricEntityTypeBuilder.<HandGrenadeEntity>create(MobCategory.MISC, HandGrenadeEntity::new).forceTrackedVelocityUpdates(true).trackRangeChunks(64).trackedUpdateRate(1).disableSaving().dimensions(EntityDimensions.scalable(0.3f, 0.3f)));
    public static final Supplier<EntityType<RgoGrenadeEntity>> RGO_GRENADE = register("rgo_grenade",
            FabricEntityTypeBuilder.<RgoGrenadeEntity>create(MobCategory.MISC, RgoGrenadeEntity::new).forceTrackedVelocityUpdates(true).trackRangeChunks(64).trackedUpdateRate(1).disableSaving().dimensions(EntityDimensions.scalable(0.3f, 0.3f)));
    public static final Supplier<EntityType<M18SmokeGrenadeEntity>> M18_SMOKE_GRENADE = register("m18_smoke_grenade",
            FabricEntityTypeBuilder.<M18SmokeGrenadeEntity>create(MobCategory.MISC, M18SmokeGrenadeEntity::new).forceTrackedVelocityUpdates(true).trackRangeChunks(64).trackedUpdateRate(1).disableSaving().dimensions(EntityDimensions.scalable(0.3f, 0.3f)));
    public static final Supplier<EntityType<JavelinMissileEntity>> JAVELIN_MISSILE = register("javelin_missile",
            FabricEntityTypeBuilder.<JavelinMissileEntity>create(MobCategory.MISC, JavelinMissileEntity::new).forceTrackedVelocityUpdates(false).trackRangeChunks(64).trackedUpdateRate(1).disableSaving().dimensions(EntityDimensions.scalable(0.5f, 0.5f)));
    public static final Supplier<EntityType<IglaMissileEntity>> IGLA_MISSILE = register("igla_9k38_missile",
            FabricEntityTypeBuilder.<IglaMissileEntity>create(MobCategory.MISC, IglaMissileEntity::new).forceTrackedVelocityUpdates(false).trackRangeChunks(64).trackedUpdateRate(1).disableSaving().dimensions(EntityDimensions.scalable(0.5f, 0.5f)));
    public static final Supplier<EntityType<Ru9m336MissileEntity>> RU_9K33_MISSILE = register("ru_9m336_missile",
            FabricEntityTypeBuilder.<Ru9m336MissileEntity>create(MobCategory.MISC, Ru9m336MissileEntity::new).forceTrackedVelocityUpdates(false).trackRangeChunks(64).trackedUpdateRate(1).disableSaving().dimensions(EntityDimensions.scalable(0.5f, 0.5f)));
    public static final Supplier<EntityType<Agm65Entity>> AGM_65 = register("agm_65",
            FabricEntityTypeBuilder.<Agm65Entity>create(MobCategory.MISC, Agm65Entity::new).forceTrackedVelocityUpdates(false).trackRangeChunks(64).trackedUpdateRate(1).disableSaving().dimensions(EntityDimensions.scalable(0.75f, 0.75f)));
    public static final Supplier<EntityType<Kh39Entity>> KH_39 = register("kh_39",
            FabricEntityTypeBuilder.<Kh39Entity>create(MobCategory.MISC, Kh39Entity::new).forceTrackedVelocityUpdates(false).trackRangeChunks(64).trackedUpdateRate(1).disableSaving().dimensions(EntityDimensions.scalable(0.75f, 0.75f)));
    public static final Supplier<EntityType<SmallRocketEntity>> SMALL_ROCKET = register("small_rocket",
            FabricEntityTypeBuilder.<SmallRocketEntity>create(MobCategory.MISC, SmallRocketEntity::new).forceTrackedVelocityUpdates(false).trackRangeChunks(64).trackedUpdateRate(1).disableSaving().dimensions(EntityDimensions.scalable(0.5f, 0.5f)));
    public static final Supplier<EntityType<MediumRocketEntity>> MEDIUM_ROCKET = register("medium_rocket",
            FabricEntityTypeBuilder.<MediumRocketEntity>create(MobCategory.MISC, MediumRocketEntity::new).forceTrackedVelocityUpdates(false).trackRangeChunks(64).trackedUpdateRate(1).disableSaving().dimensions(EntityDimensions.scalable(0.5f, 0.5f)));
    public static final Supplier<EntityType<WireGuideMissileEntity>> WIRE_GUIDE_MISSILE = register("wire_guide_missile",
            FabricEntityTypeBuilder.<WireGuideMissileEntity>create(MobCategory.MISC, WireGuideMissileEntity::new).forceTrackedVelocityUpdates(false).trackRangeChunks(64).trackedUpdateRate(1).disableSaving().fireImmune().dimensions(EntityDimensions.scalable(0.5f, 0.5f)));
    public static final Supplier<EntityType<SwarmDroneEntity>> SWARM_DRONE = register("swarm_drone",
            FabricEntityTypeBuilder.<SwarmDroneEntity>create(MobCategory.MISC, SwarmDroneEntity::new).forceTrackedVelocityUpdates(false).trackRangeChunks(64).trackedUpdateRate(1).disableSaving().fireImmune().dimensions(EntityDimensions.scalable(0.5f, 0.5f)));
    public static final Supplier<EntityType<Mk82Entity>> MK_82 = register("mk_82",
            FabricEntityTypeBuilder.<Mk82Entity>create(MobCategory.MISC, Mk82Entity::new).forceTrackedVelocityUpdates(false).trackRangeChunks(64).trackedUpdateRate(1).disableSaving().dimensions(EntityDimensions.scalable(0.8f, 0.8f)));

    // Vehicles
    // Turrets
    public static final Supplier<EntityType<Type63Entity>> TYPE_63 = register("type_63",
            FabricEntityTypeBuilder.<Type63Entity>create(MobCategory.MISC, Type63Entity::new).trackRangeChunks(512).trackedUpdateRate(1).fireImmune().dimensions(EntityDimensions.scalable(1f, 1.5f)));
    public static final Supplier<EntityType<Mk42Entity>> MK_42 = register("mk_42",
            FabricEntityTypeBuilder.<Mk42Entity>create(MobCategory.MISC, Mk42Entity::new).trackRangeChunks(512).trackedUpdateRate(3).fireImmune().dimensions(EntityDimensions.scalable(3.4f, 3.5f)));
    public static final Supplier<EntityType<Hpj11Entity>> HPJ_11 = register("hpj_11",
            FabricEntityTypeBuilder.<Hpj11Entity>create(MobCategory.MISC, Hpj11Entity::new).trackRangeChunks(512).trackedUpdateRate(3).fireImmune().dimensions(EntityDimensions.scalable(2.8f, 2.4f)));
    public static final Supplier<EntityType<Mle1934Entity>> MLE_1934 = register("mle_1934",
            FabricEntityTypeBuilder.<Mle1934Entity>create(MobCategory.MISC, Mle1934Entity::new).trackRangeChunks(512).trackedUpdateRate(3).fireImmune().dimensions(EntityDimensions.scalable(4.5f, 2.8f)));
    public static final Supplier<EntityType<Bl132Entity>> BL_132 = register("bl_132",
            FabricEntityTypeBuilder.<Bl132Entity>create(MobCategory.MISC, Bl132Entity::new).trackRangeChunks(512).trackedUpdateRate(3).fireImmune().dimensions(EntityDimensions.scalable(7f, 4.4375f)));
    public static final Supplier<EntityType<AnnihilatorEntity>> ANNIHILATOR = register("annihilator",
            FabricEntityTypeBuilder.<AnnihilatorEntity>create(MobCategory.MISC, AnnihilatorEntity::new).trackRangeChunks(512).trackedUpdateRate(3).fireImmune().dimensions(EntityDimensions.scalable(13f, 4.2f)));
    public static final Supplier<EntityType<LaserTowerEntity>> LASER_TOWER = register("laser_tower",
            FabricEntityTypeBuilder.<LaserTowerEntity>create(MobCategory.MISC, LaserTowerEntity::new).trackRangeChunks(512).trackedUpdateRate(1).fireImmune().dimensions(EntityDimensions.scalable(0.9f, 1.65f)));
    public static final Supplier<EntityType<WaveforceTowerEntity>> WAVEFORCE_TOWER = register("waveforce_tower",
            FabricEntityTypeBuilder.<WaveforceTowerEntity>create(MobCategory.MISC, WaveforceTowerEntity::new).trackRangeChunks(512).trackedUpdateRate(1).fireImmune().dimensions(EntityDimensions.scalable(1.75f, 3.3f)));
    public static final Supplier<EntityType<TowEntity>> TOW = register("tow",
            FabricEntityTypeBuilder.<TowEntity>create(MobCategory.MISC, TowEntity::new).trackRangeChunks(512).trackedUpdateRate(1).fireImmune().dimensions(EntityDimensions.scalable(0.5f, 1.35f)));
//    public static final Supplier<EntityType<SteelCoilEntity>> STEEL_COIL = register("steel_coil",
//            FabricEntityTypeBuilder.<SteelCoilEntity>create(MobCategory.MISC, SteelCoilEntity::new).trackRangeChunks(512).trackedUpdateRate(1).fireImmune().dimensions(EntityDimensions.scalable(2, 2)));

    // Boats
    public static final Supplier<EntityType<SpeedboatEntity>> SPEEDBOAT = register("speedboat",
            FabricEntityTypeBuilder.<SpeedboatEntity>create(MobCategory.MISC, SpeedboatEntity::new).trackRangeChunks(512).trackedUpdateRate(1).fireImmune().dimensions(EntityDimensions.scalable(3.0f, 2.0f)));

    // Land Vehicles
    public static final Supplier<EntityType<WheelChairEntity>> WHEEL_CHAIR = register("wheel_chair",
            FabricEntityTypeBuilder.<WheelChairEntity>create(MobCategory.MISC, WheelChairEntity::new).trackRangeChunks(512).trackedUpdateRate(1).fireImmune().dimensions(EntityDimensions.scalable(1.0f, 1.0f)));
    public static final Supplier<EntityType<Lav150Entity>> LAV_150 = register("lav_150",
            FabricEntityTypeBuilder.<Lav150Entity>create(MobCategory.MISC, Lav150Entity::new).trackRangeChunks(512).trackedUpdateRate(1).fireImmune().dimensions(EntityDimensions.scalable(2.8f, 3.1f)));
    public static final Supplier<EntityType<Bmp2Entity>> BMP_2 = register("bmp_2",
            FabricEntityTypeBuilder.<Bmp2Entity>create(MobCategory.MISC, Bmp2Entity::new).trackRangeChunks(512).trackedUpdateRate(1).fireImmune().dimensions(EntityDimensions.scalable(4f, 3f)));
    public static final Supplier<EntityType<Yx100Entity>> YX_100 = register("yx_100",
            FabricEntityTypeBuilder.<Yx100Entity>create(MobCategory.MISC, Yx100Entity::new).trackRangeChunks(512).trackedUpdateRate(1).fireImmune().dimensions(EntityDimensions.scalable(4.6f, 3.25f)));
    public static final Supplier<EntityType<PrismTankEntity>> PRISM_TANK = register("prism_tank",
            FabricEntityTypeBuilder.<PrismTankEntity>create(MobCategory.MISC, PrismTankEntity::new).trackRangeChunks(512).trackedUpdateRate(1).fireImmune().dimensions(EntityDimensions.scalable(5f, 2.6f)));
    public static final Supplier<EntityType<Plz05Entity>> PLZ_05 = register("plz_05",
            FabricEntityTypeBuilder.<Plz05Entity>create(MobCategory.MISC, Plz05Entity::new).trackRangeChunks(512).trackedUpdateRate(1).fireImmune().dimensions(EntityDimensions.scalable(4.6f, 3.25f)));

    // Aircraft
    public static final Supplier<EntityType<Tom6Entity>> TOM_6 = register("tom_6",
            FabricEntityTypeBuilder.<Tom6Entity>create(MobCategory.MISC, Tom6Entity::new).trackRangeChunks(512).trackedUpdateRate(1).fireImmune().dimensions(EntityDimensions.scalable(1.05f, 1.0f)));
    public static final Supplier<EntityType<Ah6Entity>> AH_6 = register("ah_6",
            FabricEntityTypeBuilder.<Ah6Entity>create(MobCategory.MISC, Ah6Entity::new).trackRangeChunks(512).trackedUpdateRate(1).fireImmune().dimensions(EntityDimensions.scalable(3f, 2.9f)));
    public static final Supplier<EntityType<Mi28Entity>> MI_28 = register("mi_28",
            FabricEntityTypeBuilder.<Mi28Entity>create(MobCategory.MISC, Mi28Entity::new).trackRangeChunks(512).trackedUpdateRate(1).fireImmune().dimensions(EntityDimensions.scalable(4.5f, 4.5f)));
    public static final Supplier<EntityType<A10Entity>> A_10A = register("a_10a",
            FabricEntityTypeBuilder.<A10Entity>create(MobCategory.MISC, A10Entity::new).trackRangeChunks(512).trackedUpdateRate(1).fireImmune().dimensions(EntityDimensions.scalable(4.5f, 3.5f)));

    // Special
    public static final Supplier<EntityType<DroneEntity>> DRONE = register("drone",
            FabricEntityTypeBuilder.<DroneEntity>create(MobCategory.MISC, DroneEntity::new).trackRangeChunks(512).trackedUpdateRate(1).dimensions(EntityDimensions.scalable(0.6f, 0.2f)));
    public static final Supplier<EntityType<MortarEntity>> MORTAR = register("mortar",
            FabricEntityTypeBuilder.<MortarEntity>create(MobCategory.MISC, MortarEntity::new).trackRangeChunks(512).trackedUpdateRate(1).fireImmune().dimensions(EntityDimensions.scalable(0.8f, 1.4f)));

    public static final Supplier<EntityType<VehicleAssemblingTableVehicleEntity>> VEHICLE_ASSEMBLING_TABLE = register("vehicle_assembling_table",
            FabricEntityTypeBuilder.<VehicleAssemblingTableVehicleEntity>create(MobCategory.MISC, VehicleAssemblingTableVehicleEntity::new).trackRangeChunks(512).trackedUpdateRate(1).fireImmune().dimensions(EntityDimensions.scalable(2, 1.875f)));

    public static final Supplier<EntityType<TruckEntity>> TRUCK = register("truck",
            FabricEntityTypeBuilder.<TruckEntity>create(MobCategory.MISC, TruckEntity::new).trackRangeChunks(512).trackedUpdateRate(1).fireImmune().dimensions(EntityDimensions.scalable(2.6f, 3f)));

    public static final Map<Supplier<? extends EntityType<?>>, AttributeSupplier> ATTRIBUTES = new HashMap<>();

    private static <T extends Entity> Supplier<EntityType<T>> register(String name, FabricEntityTypeBuilder<T> entityTypeBuilder) {
        return Registration.entity(name, entityTypeBuilder::build);
    }

    public static void register() {

    }

    public static void registerAttributes() {
        registerAttribute(TARGET, TargetEntity.createAttributes());
        registerAttribute(DPS_GENERATOR, DPSGeneratorEntity.createAttributes());
        registerAttribute(SENPAI, SenpaiEntity.createAttributes());
    }

    public static void registerSpawnPlacements() {
        SpawnPlacements.register(SENPAI.get(), SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                (entityType, world, reason, pos, random) -> world.getDifficulty() != Difficulty.PEACEFUL
                        && SpawnConfig.SPAWN_SENPAI.get()
                        && Monster.isDarkEnoughToSpawn(world, pos, random)
                        && Mob.checkMobSpawnRules(entityType, world, reason, pos, random));
    }

    private static <T extends LivingEntity> void registerAttribute(Supplier<EntityType<T>> type, AttributeSupplier.Builder builder) {
        var attributes = builder.build();
        ATTRIBUTES.put(type, attributes);
        FabricDefaultAttributeRegistry.register(type.get(), attributes);
    }
}
