package com.atsuishio.superbwarfare.init;

import com.atsuishio.superbwarfare.Mod;
import com.atsuishio.superbwarfare.entity.*;
import com.atsuishio.superbwarfare.entity.projectile.*;
import com.atsuishio.superbwarfare.entity.vehicle.*;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ModEntities {

    // Living Entities
    public static final Supplier<EntityType<TargetEntity>> TARGET = register("target",
            EntityType.Builder.of(TargetEntity::new, MobCategory.CREATURE).fireImmune().sized(0.875f, 2f));
    public static final Supplier<EntityType<DPSGeneratorEntity>> DPS_GENERATOR = register("dps_generator",
            EntityType.Builder.of(DPSGeneratorEntity::new, MobCategory.CREATURE).fireImmune().sized(0.875f, 2f));
    public static final Supplier<EntityType<SenpaiEntity>> SENPAI = register("senpai",
            EntityType.Builder.of(SenpaiEntity::new, MobCategory.MONSTER)
                    .sized(0.6f, 2f));

    // Misc Entities
    public static final Supplier<EntityType<LaserEntity>> LASER = register("laser",
            EntityType.Builder.<LaserEntity>of(LaserEntity::new, MobCategory.MISC).sized(0.1f, 0.1f).fireImmune());
    public static final Supplier<EntityType<FlareDecoyEntity>> FLARE_DECOY = register("flare_decoy",
            EntityType.Builder.<FlareDecoyEntity>of(FlareDecoyEntity::new, MobCategory.MISC).noSave().sized(1f, 1f));
    public static final Supplier<EntityType<SmokeDecoyEntity>> SMOKE_DECOY = register("smoke_decoy",
            EntityType.Builder.<SmokeDecoyEntity>of(SmokeDecoyEntity::new, MobCategory.MISC).noSave().sized(4.5f, 4.5f));
    public static final Supplier<EntityType<ClaymoreEntity>> CLAYMORE = register("claymore",
            EntityType.Builder.<ClaymoreEntity>of(ClaymoreEntity::new, MobCategory.MISC).sized(0.25f, 0.25f));

    public static final Supplier<EntityType<Blu43Entity>> BLU_43 = register("blu_43",
            EntityType.Builder.<Blu43Entity>of(Blu43Entity::new, MobCategory.MISC).sized(0.12f, 0.05f));

    public static final Supplier<EntityType<Tm62Entity>> TM_62 = register("tm_62",
            EntityType.Builder.<Tm62Entity>of(Tm62Entity::new, MobCategory.MISC).sized(0.5f, 0.15f));
    public static final Supplier<EntityType<Ptkm1rEntity>> PTKM_1R = register("ptkm_1r",
            EntityType.Builder.<Ptkm1rEntity>of(Ptkm1rEntity::new, MobCategory.MISC).sized(0.2f, 0.7f));
    public static final Supplier<EntityType<C4Entity>> C4 = register("c4",
            EntityType.Builder.<C4Entity>of(C4Entity::new, MobCategory.MISC).sized(0.5f, 0.5f));

    public static final Supplier<EntityType<MedicalKitEntity>> MEDICAL_KIT = register("medical_kit",
            EntityType.Builder.of(MedicalKitEntity::new, MobCategory.MISC).sized(0.4f, 0.2f));

    // Projectiles
    public static final Supplier<EntityType<TaserBulletEntity>> TASER_BULLET = register("taser_bullet",
            EntityType.Builder.<TaserBulletEntity>of(TaserBulletEntity::new, MobCategory.MISC).noSave()
                    .sized(0.25f, 0.25f));

    // Fast Projectiles
    public static final Supplier<EntityType<SmallCannonShellEntity>> SMALL_CANNON_SHELL = register("small_cannon_shell",
            EntityType.Builder.<SmallCannonShellEntity>of(SmallCannonShellEntity::new, MobCategory.MISC).noSave().sized(0.25f, 0.25f));
    public static final Supplier<EntityType<RpgRocketTBGEntity>> RPG_ROCKET_TBG = register("rpg_rocket_tbg",
            EntityType.Builder.<RpgRocketTBGEntity>of(RpgRocketTBGEntity::new, MobCategory.MISC).noSave().sized(0.5f, 0.5f));
    public static final Supplier<EntityType<RpgRocketStandardEntity>> RPG_ROCKET_STANDARD = register("rpg_rocket_standard",
            EntityType.Builder.<RpgRocketStandardEntity>of(RpgRocketStandardEntity::new, MobCategory.MISC).noSave().sized(0.5f, 0.5f));
    public static final Supplier<EntityType<MortarShellEntity>> MORTAR_SHELL = register("mortar_shell",
            EntityType.Builder.<MortarShellEntity>of(MortarShellEntity::new, MobCategory.MISC).noSave().sized(0.5f, 0.5f));
    public static final Supplier<EntityType<ProjectileEntity>> PROJECTILE = register("projectile",
            EntityType.Builder.<ProjectileEntity>of(ProjectileEntity::new, MobCategory.MISC).noSave().noSummon().sized(0.25f, 0.25f));
    public static final Supplier<EntityType<CannonShellEntity>> CANNON_SHELL = register("cannon_shell",
            EntityType.Builder.<CannonShellEntity>of(CannonShellEntity::new, MobCategory.MISC).noSave().sized(0.75f, 0.75f));
    public static final Supplier<EntityType<GunGrenadeEntity>> GUN_GRENADE = register("gun_grenade",
            EntityType.Builder.<GunGrenadeEntity>of(GunGrenadeEntity::new, MobCategory.MISC).noSave().sized(0.5f, 0.5f));
    public static final Supplier<EntityType<GrapeshotEntity>> GRAPESHOT = register("grapeshot",
            EntityType.Builder.<GrapeshotEntity>of(GrapeshotEntity::new, MobCategory.MISC).noSave().sized(0.5f, 0.5f));
    public static final Supplier<EntityType<MelonBombEntity>> MELON_BOMB = register("melon_bomb",
            EntityType.Builder.<MelonBombEntity>of(MelonBombEntity::new, MobCategory.MISC).noSave().sized(1f, 1f));
    public static final Supplier<EntityType<PtkmProjectileEntity>> PTKM_PROJECTILE = register("ptkm_projectile",
            EntityType.Builder.<PtkmProjectileEntity>of(PtkmProjectileEntity::new, MobCategory.MISC).noSave().sized(0.5f, 0.5f));
    public static final Supplier<EntityType<HandGrenadeEntity>> HAND_GRENADE = register("hand_grenade",
            EntityType.Builder.<HandGrenadeEntity>of(HandGrenadeEntity::new, MobCategory.MISC).noSave().sized(0.3f, 0.3f));
    public static final Supplier<EntityType<RgoGrenadeEntity>> RGO_GRENADE = register("rgo_grenade",
            EntityType.Builder.<RgoGrenadeEntity>of(RgoGrenadeEntity::new, MobCategory.MISC).noSave().sized(0.3f, 0.3f));
    public static final Supplier<EntityType<M18SmokeGrenadeEntity>> M18_SMOKE_GRENADE = register("m18_smoke_grenade",
            EntityType.Builder.<M18SmokeGrenadeEntity>of(M18SmokeGrenadeEntity::new, MobCategory.MISC).noSave().sized(0.3f, 0.3f));
    public static final Supplier<EntityType<JavelinMissileEntity>> JAVELIN_MISSILE = register("javelin_missile",
            EntityType.Builder.<JavelinMissileEntity>of(JavelinMissileEntity::new, MobCategory.MISC).noSave().sized(0.5f, 0.5f));
    public static final Supplier<EntityType<IglaMissileEntity>> IGLA_MISSILE = register("igla_9k38_missile",
            EntityType.Builder.<IglaMissileEntity>of(IglaMissileEntity::new, MobCategory.MISC).noSave().sized(0.5f, 0.5f));
    public static final Supplier<EntityType<Ru9m336MissileEntity>> RU_9K33_MISSILE = register("ru_9m336_missile",
            EntityType.Builder.<Ru9m336MissileEntity>of(Ru9m336MissileEntity::new, MobCategory.MISC).noSave().sized(0.5f, 0.5f));
    public static final Supplier<EntityType<Agm65Entity>> AGM_65 = register("agm_65",
            EntityType.Builder.<Agm65Entity>of(Agm65Entity::new, MobCategory.MISC).noSave().sized(0.75f, 0.75f));
    public static final Supplier<EntityType<Kh39Entity>> KH_39 = register("kh_39",
            EntityType.Builder.<Kh39Entity>of(Kh39Entity::new, MobCategory.MISC).noSave().sized(0.75f, 0.75f));
    public static final Supplier<EntityType<SmallRocketEntity>> SMALL_ROCKET = register("small_rocket",
            EntityType.Builder.<SmallRocketEntity>of(SmallRocketEntity::new, MobCategory.MISC).noSave().sized(0.5f, 0.5f));
    public static final Supplier<EntityType<MediumRocketEntity>> MEDIUM_ROCKET = register("medium_rocket",
            EntityType.Builder.<MediumRocketEntity>of(MediumRocketEntity::new, MobCategory.MISC).noSave().sized(0.5f, 0.5f));
    public static final Supplier<EntityType<WireGuideMissileEntity>> WIRE_GUIDE_MISSILE = register("wire_guide_missile",
            EntityType.Builder.<WireGuideMissileEntity>of(WireGuideMissileEntity::new, MobCategory.MISC).noSave().fireImmune().sized(0.5f, 0.5f));
    public static final Supplier<EntityType<SwarmDroneEntity>> SWARM_DRONE = register("swarm_drone",
            EntityType.Builder.<SwarmDroneEntity>of(SwarmDroneEntity::new, MobCategory.MISC).noSave().fireImmune().sized(0.5f, 0.5f));
    public static final Supplier<EntityType<Mk82Entity>> MK_82 = register("mk_82",
            EntityType.Builder.<Mk82Entity>of(Mk82Entity::new, MobCategory.MISC).noSave().sized(0.8f, 0.8f));

    // Vehicles
    // Turrets
    public static final Supplier<EntityType<Type63Entity>> TYPE_63 = register("type_63",
            EntityType.Builder.of(Type63Entity::new, MobCategory.MISC).fireImmune().sized(1f, 1.5f));
    public static final Supplier<EntityType<Mk42Entity>> MK_42 = register("mk_42",
            EntityType.Builder.of(Mk42Entity::new, MobCategory.MISC).fireImmune().sized(3.4f, 3.5f));
    public static final Supplier<EntityType<Hpj11Entity>> HPJ_11 = register("hpj_11",
            EntityType.Builder.of(Hpj11Entity::new, MobCategory.MISC).fireImmune().sized(2.8f, 2.4f));
    public static final Supplier<EntityType<Mle1934Entity>> MLE_1934 = register("mle_1934",
            EntityType.Builder.of(Mle1934Entity::new, MobCategory.MISC).fireImmune().sized(4.5f, 2.8f));
    public static final Supplier<EntityType<Bl132Entity>> BL_132 = register("bl_132",
            EntityType.Builder.of(Bl132Entity::new, MobCategory.MISC).fireImmune().sized(7f, 4.4375f));
    public static final Supplier<EntityType<AnnihilatorEntity>> ANNIHILATOR = register("annihilator",
            EntityType.Builder.of(AnnihilatorEntity::new, MobCategory.MISC).fireImmune().sized(13f, 4.2f));
    public static final Supplier<EntityType<LaserTowerEntity>> LASER_TOWER = register("laser_tower",
            EntityType.Builder.of(LaserTowerEntity::new, MobCategory.MISC).fireImmune().sized(0.9f, 1.65f));
    public static final Supplier<EntityType<WaveforceTowerEntity>> WAVEFORCE_TOWER = register("waveforce_tower",
            EntityType.Builder.of(WaveforceTowerEntity::new, MobCategory.MISC).fireImmune().sized(1.75f, 3.3f));
    public static final Supplier<EntityType<TowEntity>> TOW = register("tow",
            EntityType.Builder.of(TowEntity::new, MobCategory.MISC).fireImmune().sized(0.5f, 1.35f));
//    public static final Supplier<EntityType<SteelCoilEntity>> STEEL_COIL = register("steel_coil",
//            EntityType.Builder.of(SteelCoilEntity::new, MobCategory.MISC).fireImmune().sized(2, 2));

    // Boats
    public static final Supplier<EntityType<SpeedboatEntity>> SPEEDBOAT = register("speedboat",
            EntityType.Builder.of(SpeedboatEntity::new, MobCategory.MISC).fireImmune().sized(3.0f, 2.0f));

    // Land Vehicles
    public static final Supplier<EntityType<WheelChairEntity>> WHEEL_CHAIR = register("wheel_chair",
            EntityType.Builder.of(WheelChairEntity::new, MobCategory.MISC).fireImmune().sized(1.0f, 1.0f));
    public static final Supplier<EntityType<Lav150Entity>> LAV_150 = register("lav_150",
            EntityType.Builder.of(Lav150Entity::new, MobCategory.MISC).fireImmune().sized(2.8f, 3.1f));
    public static final Supplier<EntityType<Bmp2Entity>> BMP_2 = register("bmp_2",
            EntityType.Builder.of(Bmp2Entity::new, MobCategory.MISC).fireImmune().sized(4f, 3f));
    public static final Supplier<EntityType<Yx100Entity>> YX_100 = register("yx_100",
            EntityType.Builder.of(Yx100Entity::new, MobCategory.MISC).fireImmune().sized(4.6f, 3.25f));
    public static final Supplier<EntityType<PrismTankEntity>> PRISM_TANK = register("prism_tank",
            EntityType.Builder.of(PrismTankEntity::new, MobCategory.MISC).fireImmune().sized(5f, 2.6f));
    public static final Supplier<EntityType<Plz05Entity>> PLZ_05 = register("plz_05",
            EntityType.Builder.of(Plz05Entity::new, MobCategory.MISC).fireImmune().sized(4.6f, 3.25f));

    // Aircraft
    public static final Supplier<EntityType<Tom6Entity>> TOM_6 = register("tom_6",
            EntityType.Builder.of(Tom6Entity::new, MobCategory.MISC).fireImmune().sized(1.05f, 1.0f));
    public static final Supplier<EntityType<Ah6Entity>> AH_6 = register("ah_6",
            EntityType.Builder.of(Ah6Entity::new, MobCategory.MISC).fireImmune().sized(3f, 2.9f));
    public static final Supplier<EntityType<Mi28Entity>> MI_28 = register("mi_28",
            EntityType.Builder.of(Mi28Entity::new, MobCategory.MISC).fireImmune().sized(4.5f, 4.5f));
    public static final Supplier<EntityType<A10Entity>> A_10A = register("a_10a",
            EntityType.Builder.of(A10Entity::new, MobCategory.MISC).fireImmune().sized(4.5f, 3.5f));

    // Special
    public static final Supplier<EntityType<DroneEntity>> DRONE = register("drone",
            EntityType.Builder.<DroneEntity>of(DroneEntity::new, MobCategory.MISC).sized(0.6f, 0.2f));
    public static final Supplier<EntityType<MortarEntity>> MORTAR = register("mortar",
            EntityType.Builder.<MortarEntity>of(MortarEntity::new, MobCategory.MISC).fireImmune().sized(0.8f, 1.4f));

    public static final Supplier<EntityType<VehicleAssemblingTableVehicleEntity>> VEHICLE_ASSEMBLING_TABLE = register("vehicle_assembling_table",
            EntityType.Builder.<VehicleAssemblingTableVehicleEntity>of(VehicleAssemblingTableVehicleEntity::new, MobCategory.MISC).fireImmune().sized(2, 1.875f));

    public static final Supplier<EntityType<TruckEntity>> TRUCK = register("truck",
            EntityType.Builder.of(TruckEntity::new, MobCategory.MISC).fireImmune().sized(2.6f, 3f));

    public static final Map<Supplier<? extends EntityType<?>>, AttributeSupplier> ATTRIBUTES = new HashMap<>();

    private static <T extends Entity> Supplier<EntityType<T>> register(String name, EntityType.Builder<T> entityTypeBuilder) {
        return Registration.entity(name, () -> entityTypeBuilder.build(name));
    }

    public static void register() {

    }

    public static void registerAttributes() {
        registerAttribute(TARGET, TargetEntity.createAttributes());
        registerAttribute(DPS_GENERATOR, DPSGeneratorEntity.createAttributes());
        registerAttribute(SENPAI, SenpaiEntity.createAttributes());
    }

    private static <T extends LivingEntity> void registerAttribute(Supplier<EntityType<T>> type, AttributeSupplier.Builder builder) {
        var attributes = builder.build();
        ATTRIBUTES.put(type, attributes);
        FabricDefaultAttributeRegistry.register(type.get(), attributes);
    }
}
