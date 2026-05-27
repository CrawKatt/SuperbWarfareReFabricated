package com.atsuishio.superbwarfare.init;

import com.atsuishio.superbwarfare.client.renderer.entity.*;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class ModEntityRenderers {

    public static void register() {
        EntityRendererRegistry.register(ModEntities.MORTAR.get(), MortarRenderer::new);
        EntityRendererRegistry.register(ModEntities.SENPAI.get(), SenpaiRenderer::new);
        EntityRendererRegistry.register(ModEntities.CLAYMORE.get(), ClaymoreRenderer::new);
        EntityRendererRegistry.register(ModEntities.C4.get(), C4Renderer::new);
        EntityRendererRegistry.register(ModEntities.TASER_BULLET.get(), TaserBulletProjectileRenderer::new);
        EntityRendererRegistry.register(ModEntities.GUN_GRENADE.get(), GunGrenadeRenderer::new);
        EntityRendererRegistry.register(ModEntities.TARGET.get(), TargetRenderer::new);
        EntityRendererRegistry.register(ModEntities.DPS_GENERATOR.get(), DPSGeneratorRenderer::new);
        EntityRendererRegistry.register(ModEntities.RPG_ROCKET_TBG.get(), RpgRocketTBGRenderer::new);
        EntityRendererRegistry.register(ModEntities.RPG_ROCKET_STANDARD.get(), RpgRocketStandardRenderer::new);
        EntityRendererRegistry.register(ModEntities.SMALL_ROCKET.get(), SmallRocketRenderer::new);
        EntityRendererRegistry.register(ModEntities.MEDIUM_ROCKET.get(), MediumRocketRenderer::new);
        EntityRendererRegistry.register(ModEntities.MORTAR_SHELL.get(), MortarShellRenderer::new);
        EntityRendererRegistry.register(ModEntities.CANNON_SHELL.get(), CannonShellRenderer::new);
        EntityRendererRegistry.register(ModEntities.PROJECTILE.get(), ProjectileEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.MK_42.get(), Mk42Renderer::new);
        EntityRendererRegistry.register(ModEntities.DRONE.get(), DroneRenderer::new);
        EntityRendererRegistry.register(ModEntities.HAND_GRENADE.get(), HandGrenadeRenderer::new);
        EntityRendererRegistry.register(ModEntities.RGO_GRENADE.get(), RgoGrenadeRenderer::new);
        EntityRendererRegistry.register(ModEntities.M18_SMOKE_GRENADE.get(), M18SmokeGrenadeRenderer::new);
        EntityRendererRegistry.register(ModEntities.MLE_1934.get(), Mle1934Renderer::new);
        EntityRendererRegistry.register(ModEntities.JAVELIN_MISSILE.get(), JavelinMissileRenderer::new);
        EntityRendererRegistry.register(ModEntities.LASER.get(), LaserEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.ANNIHILATOR.get(), AnnihilatorRenderer::new);
        EntityRendererRegistry.register(ModEntities.SPEEDBOAT.get(), SpeedboatRenderer::new);
        EntityRendererRegistry.register(ModEntities.WHEEL_CHAIR.get(), WheelChairRenderer::new);
        EntityRendererRegistry.register(ModEntities.AH_6.get(), Ah6Renderer::new);
        EntityRendererRegistry.register(ModEntities.FLARE_DECOY.get(), FlareDecoyEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.SMOKE_DECOY.get(), SmokeDecoyEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.LAV_150.get(), Lav150Renderer::new);
        EntityRendererRegistry.register(ModEntities.SMALL_CANNON_SHELL.get(), SmallCannonShellRenderer::new);
        EntityRendererRegistry.register(ModEntities.TOM_6.get(), Tom6Renderer::new);
        EntityRendererRegistry.register(ModEntities.MELON_BOMB.get(), MelonBombEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.BMP_2.get(), Bmp2Renderer::new);
        EntityRendererRegistry.register(ModEntities.WIRE_GUIDE_MISSILE.get(), WireGuideMissileRenderer::new);
        EntityRendererRegistry.register(ModEntities.LASER_TOWER.get(), LaserTowerRenderer::new);
        EntityRendererRegistry.register(ModEntities.YX_100.get(), Yx100Renderer::new);
        EntityRendererRegistry.register(ModEntities.PRISM_TANK.get(), PrismTankRenderer::new);
        EntityRendererRegistry.register(ModEntities.SWARM_DRONE.get(), SwarmDroneRenderer::new);
        EntityRendererRegistry.register(ModEntities.HPJ_11.get(), Hpj11Renderer::new);
        EntityRendererRegistry.register(ModEntities.A_10A.get(), A10Renderer::new);
        EntityRendererRegistry.register(ModEntities.MK_82.get(), Mk82Renderer::new);
        EntityRendererRegistry.register(ModEntities.AGM_65.get(), Agm65Renderer::new);
        EntityRendererRegistry.register(ModEntities.BLU_43.get(), Blu43Renderer::new);
        EntityRendererRegistry.register(ModEntities.TM_62.get(), Tm62Renderer::new);
        EntityRendererRegistry.register(ModEntities.PTKM_1R.get(), Ptkm1rRenderer::new);
        EntityRendererRegistry.register(ModEntities.PTKM_PROJECTILE.get(), PtkmProjectileRenderer::new);
        EntityRendererRegistry.register(ModEntities.TYPE_63.get(), Type63Renderer::new);
        EntityRendererRegistry.register(ModEntities.MEDICAL_KIT.get(), MedicalKitEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.BL_132.get(), Bl132Renderer::new);
        EntityRendererRegistry.register(ModEntities.GRAPESHOT.get(), GrapeshotRenderer::new);
        EntityRendererRegistry.register(ModEntities.VEHICLE_ASSEMBLING_TABLE.get(), VehicleAssemblingTableVehicleRenderer::new);
        EntityRendererRegistry.register(ModEntities.WAVEFORCE_TOWER.get(), WaveforceTowerRenderer::new);
        EntityRendererRegistry.register(ModEntities.IGLA_MISSILE.get(), IglaMissileRenderer::new);
        EntityRendererRegistry.register(ModEntities.RU_9K33_MISSILE.get(), Ru9m336MissileRenderer::new);
        EntityRendererRegistry.register(ModEntities.TRUCK.get(), TruckRenderer::new);
        EntityRendererRegistry.register(ModEntities.TOW.get(), TowRenderer::new);
//        EntityRendererRegistry.register(ModEntities.STEEL_COIL.get(), SteelCoilRenderer::new);
        EntityRendererRegistry.register(ModEntities.MI_28.get(), Mi28Renderer::new);
        EntityRendererRegistry.register(ModEntities.KH_39.get(), Kh39Renderer::new);
        EntityRendererRegistry.register(ModEntities.PLZ_05.get(), Plz05Renderer::new);
    }
}
