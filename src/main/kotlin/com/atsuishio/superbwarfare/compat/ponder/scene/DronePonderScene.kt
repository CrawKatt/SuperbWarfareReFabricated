package com.atsuishio.superbwarfare.compat.ponder.scene

import com.atsuishio.superbwarfare.Mod.Companion.loc
import com.atsuishio.superbwarfare.compat.ponder.GeneratedPonderSupport
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper
import net.createmod.ponder.api.scene.SceneBuilder
import net.createmod.ponder.api.scene.SceneBuildingUtil
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.phys.Vec3

object DronePonderScene {
    fun register(helper: PonderSceneRegistrationHelper<ResourceLocation>) {
        helper.forComponents(loc("drone"))
            .addStoryBoard("basic_5x5", DronePonderScene::introScene)
            .addStoryBoard("basic_5x5", DronePonderScene::ammoLoadingScene)
    }

    private fun introScene(scene: SceneBuilder, util: SceneBuildingUtil) {
        scene.title("drone_intro", "Drone Introduction")
        val context = GeneratedPonderSupport.Context()
        scene.addKeyframe()
        GeneratedPonderSupport.showStructure(scene, context, null, null, null, null)
        scene.idle(20)
        GeneratedPonderSupport.createEntity(scene, context, "superbwarfare:drone", Vec3(2.5, 1.0, 2.5), null, null, null, "{Ammo:0,CanUpdate:1b,ChargeProgress:0.0f,Controller:\"undefined\",DecoyCount:0,DecoyReloadCoolDown:500,DisplayData:\"1.0,1.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.1,0.35,-1.0\",DisplayEntity:\"\",DisplayEntityTag:\"{}\",DogTagIcon:[[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1]],ForgeData:{},GearRot:0.0f,GearUp:0b,GunXRot:0.0f,GunYRot:0.0f,Health:5.0f,HoverMode:0b,Inventory:{Items:[],Size:0},Invulnerable:0b,IsWreck:0b,Item:{Count:0b,id:\"minecraft:air\",tag:{}},KamikazeMode:0b,LastAttacker:\"undefined\",LastDriver:\"undefined\",LeftWheelDamaged:0b,LeftWheelHealth:50.0f,Linked:0b,Locked:0b,LoiterActive:0b,LoiterR:400.0f,LoiterX:0.0f,LoiterY:318.0f,LoiterZ:0.0f,MainEngineDamaged:0b,MainEngineHealth:50.0f,MaxAmmo:1,Power:0.02f,PropellerRot:0.0f,RightWheelDamaged:0b,RightWheelHealth:50.0f,ServerPitch:0.0f,ServerYaw:0.0f,SubEngineDamaged:0b,SubEngineHealth:50.0f,SympatheticDetonated:0b,TowedByUUID:\"\",TowingUUID:\"\",TowingUUIDs:[],TurretBurnTimer:0,TurretBurned:0b,TurretDamaged:0b,TurretHealth:50.0f,TurretXRot:0.0f,TurretYRot:0.0f,TurretYRotLock:0.0f}", null, "simultaneous", null, "down")
        scene.idle(20)
        scene.addKeyframe()
        GeneratedPonderSupport.showText(scene, "This is the Superb Warfare drone", Vec3(2.5, 1.2, 2.5), 60, null, true)
        scene.idle(80)
        scene.addKeyframe()
        GeneratedPonderSupport.showText(scene, "A device used for reconnaissance and combat, controlled with a Monitor, with a maximum flight distance", Vec3(2.5, 1.2, 2.5), 60, null, true)
        scene.idle(80)
        scene.addKeyframe()
        GeneratedPonderSupport.showText(scene, "Right-click a block while holding it to deploy the drone; sneak + right-click to retrieve it", Vec3(2.5, 1.2, 2.5), 60, null, true)
        scene.idle(80)
        scene.addKeyframe()
        GeneratedPonderSupport.showText(scene, "Right-click an unbound drone while holding an unbound Monitor to bind it", Vec3(2.5, 1.2, 2.5), 60, null, true)
        GeneratedPonderSupport.showControls(scene, Vec3(2.5, 1.3, 2.5), "right", 60, "right", "superbwarfare:monitor", null, false, false)
        scene.idle(80)
        GeneratedPonderSupport.createItemEntity(scene, context, "superbwarfare:monitor", 1, Vec3(0.5, 2.0, 2.5), Vec3(0.0, 0.0, 0.0), null, null)
        scene.idle(20)
        scene.addKeyframe()
        GeneratedPonderSupport.showText(scene, "Right-click a bound drone to control it. Space ascends, Shift descends, and movement uses the same controls as the player (WASD)", Vec3(0.5, 1.3, 2.5), 120, null, true)
        scene.idle(150)
    }

    private fun ammoLoadingScene(scene: SceneBuilder, util: SceneBuildingUtil) {
        scene.title("drone_ammo_loading", "Drone Payload Loading")
        val context = GeneratedPonderSupport.Context()
        GeneratedPonderSupport.showStructure(scene, context, null, null, null, null)
        scene.idle(20)
        GeneratedPonderSupport.createEntity(scene, context, "superbwarfare:drone", Vec3(2.5, 1.0, 2.5), null, null, null, "{Ammo:0,CanUpdate:1b,ChargeProgress:0.0f,Controller:\"undefined\",DecoyCount:0,DecoyReloadCoolDown:500,DisplayData:\"1.0,1.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.1,0.35,-1.0\",DisplayEntity:\"\",DisplayEntityTag:\"{}\",DogTagIcon:[[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1]],ForgeData:{},GearRot:0.0f,GearUp:0b,GunXRot:0.0f,GunYRot:0.0f,Health:5.0f,HoverMode:0b,Inventory:{Items:[],Size:0},Invulnerable:0b,IsWreck:0b,Item:{Count:0b,id:\"minecraft:air\",tag:{}},KamikazeMode:0b,LastAttacker:\"undefined\",LastDriver:\"undefined\",LeftWheelDamaged:0b,LeftWheelHealth:50.0f,Linked:0b,Locked:0b,LoiterActive:0b,LoiterR:400.0f,LoiterX:0.0f,LoiterY:318.0f,LoiterZ:0.0f,MainEngineDamaged:0b,MainEngineHealth:50.0f,MaxAmmo:1,Power:0.02f,PropellerRot:0.0f,RightWheelDamaged:0b,RightWheelHealth:50.0f,ServerPitch:0.0f,ServerYaw:0.0f,SubEngineDamaged:0b,SubEngineHealth:50.0f,SympatheticDetonated:0b,TowedByUUID:\"\",TowingUUID:\"\",TowingUUIDs:[],TurretBurnTimer:0,TurretBurned:0b,TurretDamaged:0b,TurretHealth:50.0f,TurretXRot:0.0f,TurretYRot:0.0f,TurretYRotLock:0.0f}", null, "simultaneous", null, "down")
        scene.idle(20)
        scene.addKeyframe()
        GeneratedPonderSupport.showText(scene, "Right-click the drone with a Mortar Shell to load one shell and turn it into a kamikaze drone", Vec3(2.5, 1.2, 2.5), 60, null, true)
        GeneratedPonderSupport.showControls(scene, Vec3(2.5, 1.2, 2.5), "right", 60, "right", "superbwarfare:mortar_shell", null, false, false)
        GeneratedPonderSupport.modifyEntitiesNbt(scene, context, true, null, null, "{Ammo:1,CanUpdate:1b,ChargeProgress:0.0f,Controller:\"undefined\",DecoyCount:0,DecoyReloadCoolDown:500,DisplayData:\"1.0,1.0,1.0,0.0,-0.2,0.0,0.0,0.0,0.0,0.1,0.35,2.0\",DisplayEntity:\"superbwarfare:mortar_shell\",DisplayEntityTag:\"{}\",DogTagIcon:[[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1]],ForgeData:{},GearRot:0.0f,GearUp:0b,GunXRot:0.0f,GunYRot:0.0f,Health:5.0f,HoverMode:0b,Inventory:{Items:[],Size:0},Invulnerable:0b,IsWreck:0b,Item:{Count:1b,id:\"superbwarfare:mortar_shell\"},KamikazeMode:1b,LastAttacker:\"undefined\",LastDriver:\"undefined\",LeftWheelDamaged:0b,LeftWheelHealth:50.0f,Linked:0b,Locked:0b,LoiterActive:0b,LoiterR:400.0f,LoiterX:0.0f,LoiterY:318.0f,LoiterZ:0.0f,MainEngineDamaged:0b,MainEngineHealth:50.0f,MaxAmmo:1,Power:0.02f,PropellerRot:0.0f,RightWheelDamaged:0b,RightWheelHealth:50.0f,ServerPitch:0.0f,ServerYaw:0.0f,SubEngineDamaged:0b,SubEngineHealth:50.0f,SympatheticDetonated:0b,TowedByUUID:\"\",TowingUUID:\"\",TowingUUIDs:[],TurretBurnTimer:0,TurretBurned:0b,TurretDamaged:0b,TurretHealth:50.0f,TurretXRot:0.0f,TurretYRot:0.0f,TurretYRotLock:0.0f}", null, null, null, null, null)
        scene.idle(80)
        scene.addKeyframe()
        GeneratedPonderSupport.showText(scene, "After loading, it enters kamikaze mode. Accelerate into blocks or entities to reduce its health to zero and detonate, or approach a target and left-click to detonate the shell", Vec3(2.5, 1.2, 2.5), 100, null, true)
        scene.idle(120)
        scene.addKeyframe()
        GeneratedPonderSupport.showText(scene, "You can also right-click with an RGO Grenade to load up to six grenades. Left-click in the interface to drop a grenade directly beneath the drone, or crash the drone like a loaded mortar shell to release all loaded payloads at once", Vec3(2.5, 1.2, 2.5), 150, null, true)
        GeneratedPonderSupport.showControls(scene, Vec3(2.5, 1.2, 2.5), "right", 60, "right", "superbwarfare:rgo_grenade", null, false, false)
        GeneratedPonderSupport.modifyEntitiesNbt(scene, context, true, null, null, "{Ammo:6,CanUpdate:1b,ChargeProgress:0.0f,Controller:\"undefined\",DecoyCount:0,DecoyReloadCoolDown:500,DisplayData:\"0.5,0.5,0.5,0.075,0.04,0.025,90.0,90.0,0.0,0.1,0.35,2.0\",DisplayEntity:\"superbwarfare:rgo_grenade\",DisplayEntityTag:\"{Fuse:160.0d}\",DogTagIcon:[[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1]],ForgeData:{},GearRot:0.0f,GearUp:0b,GunXRot:0.0f,GunYRot:0.0f,Health:5.0f,HoverMode:0b,Inventory:{Items:[],Size:0},Invulnerable:0b,IsWreck:0b,Item:{Count:1b,id:\"superbwarfare:rgo_grenade\"},KamikazeMode:0b,LastAttacker:\"undefined\",LastDriver:\"undefined\",LeftWheelDamaged:0b,LeftWheelHealth:50.0f,Linked:0b,Locked:0b,LoiterActive:0b,LoiterR:400.0f,LoiterX:0.0f,LoiterY:318.0f,LoiterZ:0.0f,MainEngineDamaged:0b,MainEngineHealth:50.0f,MaxAmmo:6,Power:0.02f,PropellerRot:0.0f,RightWheelDamaged:0b,RightWheelHealth:50.0f,ServerPitch:0.0f,ServerYaw:0.0f,SubEngineDamaged:0b,SubEngineHealth:50.0f,SympatheticDetonated:0b,TowedByUUID:\"\",TowingUUID:\"\",TowingUUIDs:[],TurretBurnTimer:0,TurretBurned:0b,TurretDamaged:0b,TurretHealth:50.0f,TurretXRot:0.0f,TurretYRot:0.0f,TurretYRotLock:0.0f}", null, null, null, null, null)
        scene.idle(180)
        scene.addKeyframe()
        GeneratedPonderSupport.showText(scene, "Drones can also load C4 and enter kamikaze mode. Aim C4 at the drone to attach one charge, or throw C4 to stack charges on top of the drone", Vec3(2.5, 1.2, 2.5), 120, null, true)
        GeneratedPonderSupport.showControls(scene, Vec3(2.5, 1.2, 2.5), "right", 60, "right", "superbwarfare:c4_bomb", null, false, false)
        GeneratedPonderSupport.modifyEntitiesNbt(scene, context, true, null, null, "{Ammo:1,CanUpdate:1b,ChargeProgress:0.0f,Controller:\"undefined\",DecoyCount:0,DecoyReloadCoolDown:500,DisplayData:\"1.0,1.0,1.0,0.0,0.1,0.0,0.0,-180.0,-90.0,0.1,0.35,2.0\",DisplayEntity:\"superbwarfare:c4\",DisplayEntityTag:\"{Fuse:160.0d}\",DogTagIcon:[[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1]],ForgeData:{},GearRot:0.0f,GearUp:0b,GunXRot:0.0f,GunYRot:0.0f,Health:5.0f,HoverMode:0b,Inventory:{Items:[],Size:0},Invulnerable:0b,IsWreck:0b,Item:{Count:1b,id:\"superbwarfare:c4_bomb\",tag:{}},KamikazeMode:1b,LastAttacker:\"undefined\",LastDriver:\"undefined\",LeftWheelDamaged:0b,LeftWheelHealth:50.0f,Linked:0b,Locked:0b,LoiterActive:0b,LoiterR:400.0f,LoiterX:0.0f,LoiterY:318.0f,LoiterZ:0.0f,MainEngineDamaged:0b,MainEngineHealth:50.0f,MaxAmmo:1,Power:0.02f,PropellerRot:0.0f,RightWheelDamaged:0b,RightWheelHealth:50.0f,ServerPitch:0.0f,ServerYaw:0.0f,SubEngineDamaged:0b,SubEngineHealth:50.0f,SympatheticDetonated:0b,TowedByUUID:\"\",TowingUUID:\"\",TowingUUIDs:[],TurretBurnTimer:0,TurretBurned:0b,TurretDamaged:0b,TurretHealth:50.0f,TurretXRot:0.0f,TurretYRot:0.0f,TurretYRotLock:0.0f}", null, null, null, null, null)
        scene.idle(150)
    }

}

