package com.atsuishio.superbwarfare.compat.ponder.scene

import com.atsuishio.superbwarfare.Mod.Companion.loc
import com.atsuishio.superbwarfare.compat.ponder.GeneratedPonderSupport
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper
import net.createmod.ponder.api.scene.SceneBuilder
import net.createmod.ponder.api.scene.SceneBuildingUtil
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.phys.Vec3

object ContainerPonderScene {

    fun register(helper: PonderSceneRegistrationHelper<ResourceLocation>) {
        helper.forComponents(loc("container"))
            .addStoryBoard("basic_7x7", ContainerPonderScene::introScene)
            .addStoryBoard("basic_7x7", ContainerPonderScene::interactScene)
    }

    private fun introScene(scene: SceneBuilder, util: SceneBuildingUtil) {
        scene.title("container_scene_1", "Scene 1")
        val context = GeneratedPonderSupport.Context()
        scene.addKeyframe()
        GeneratedPonderSupport.showStructure(scene, context, null, null, null, null)
        scene.idle(20)
        GeneratedPonderSupport.createEntity(scene, context, "superbwarfare:type_63", Vec3(3.5, 1.0, 3.5), Vec3(3.5, 2.0, 0.5), null, null, "{CanUpdate:1b,ChargeProgress:0.0f,DecoyCount:0,DecoyReloadCoolDown:500,DogTagIcon:[[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1]],ForgeData:{},GearRot:0.0f,GearUp:0b,GunXRot:0.0f,GunYRot:0.0f,Health:100.0f,HoverMode:0b,Inventory:{Items:[],Size:12},Invulnerable:0b,IsWreck:0b,LastAttacker:\"undefined\",LastDriver:\"undefined\",LeftWheelDamaged:0b,LeftWheelHealth:50.0f,Locked:0b,LoiterActive:0b,LoiterR:400.0f,LoiterX:0.0f,LoiterY:318.0f,LoiterZ:0.0f,MainEngineDamaged:0b,MainEngineHealth:50.0f,Pitch:0.0f,Power:0.0f,PropellerRot:0.0f,RightWheelDamaged:0b,RightWheelHealth:50.0f,ServerPitch:0.0f,ServerYaw:90.0f,SubEngineDamaged:0b,SubEngineHealth:50.0f,SympatheticDetonated:0b,TowedByUUID:\"\",TowingUUID:\"\",TowingUUIDs:[],TurretBurnTimer:0,TurretBurned:0b,TurretDamaged:0b,TurretHealth:50.0f,TurretXRot:0.0f,TurretYRot:0.0f,TurretYRotLock:0.0f,WeaponState:{AP:{tag:{Attachments:{},GunData:{},Perks:{}}},CM:{tag:{Attachments:{},GunData:{},Perks:{}}},HE:{tag:{Attachments:{},GunData:{},Perks:{}}},Main:{tag:{Attachments:{},GunData:{},Perks:{}}}},Yaw:0.0f}", null, "simultaneous", null, "down")
        scene.idle(20)
        scene.addKeyframe()
        GeneratedPonderSupport.showText(scene, "这是63式107mm多管火箭炮", Vec3(3.5, 2.0, 3.5), 60, null, true)
        scene.idle(80)
        GeneratedPonderSupport.rotateCameraY(scene, -90.0f, 0f, 20)
        scene.idle(20)
        scene.addKeyframe()
        GeneratedPonderSupport.showText(scene, "在轮胎的上面有一个有一个调节转盘，大概是这个位置", Vec3(2.5, 2.3, 4.3), 60, null, true)
        scene.idle(80)
        GeneratedPonderSupport.showControls(scene, Vec3(2.5, 2.3, 4.3), "right", 60, "right", null, null, false, false)
        scene.addKeyframe()
        GeneratedPonderSupport.showText(scene, "右键长按顺时针旋转摇把，此时增加俯仰角，炮口调高，潜行右键反之", Vec3(2.5, 2.3, 4.3), 60, null, true)
        scene.idle(80)
        scene.addKeyframe()
        GeneratedPonderSupport.showText(scene, "轮胎右侧有一个凸出来的小转盘,大概是这个位置", Vec3(3.5, 1.0, 3.5), 60, null, true)
        scene.idle(80)
        GeneratedPonderSupport.showControls(scene, Vec3(3.5, 1.0, 3.5), "right", 60, "right", null, null, false, false)
        scene.addKeyframe()
        GeneratedPonderSupport.showText(scene, "右键长按逆时针旋转摇把，此时炮塔微量增加偏航角，炮塔内部向左，潜行右键反之", Vec3(3.5, 1.0, 3.5), 60, null, true)
        scene.idle(80)
        scene.addKeyframe()
        GeneratedPonderSupport.showText(scene, "你也可以通过准星对准炮塔的左右扶手来调节炮塔的偏航角", Vec3(2.5, 1.3, 4.7), 60, null, true)
        scene.idle(80)
        GeneratedPonderSupport.showControls(scene, Vec3(2.5, 1.3, 4.7), "right", 60, "right", null, null, true, false)
        scene.addKeyframe()
        GeneratedPonderSupport.showText(scene, "潜行右键推左边的把手，此时偏航角大幅减少，炮塔向右转", Vec3(2.5, 1.3, 4.7), 60, null, true)
        scene.idle(80)
        GeneratedPonderSupport.rotateCameraY(scene, -90.0f, 0f, 20)
        scene.idle(20)
        GeneratedPonderSupport.showControls(scene, Vec3(4.4, 1.3, 4.7), "right", 60, "right", null, null, true, false)
        scene.addKeyframe()
        GeneratedPonderSupport.showText(scene, "潜行右键推右边的把手，此时偏航角大幅增加，炮塔向左转", Vec3(4.4, 1.3, 4.7), 60, null, true)
        scene.idle(80)
        GeneratedPonderSupport.showControls(scene, Vec3(3.5, 2.0, 3.5), "right", 60, null, "superbwarfare:firing_parameters", null, false, false)
        scene.addKeyframe()
        GeneratedPonderSupport.showText(scene, "手持射击诸元会显示炮塔的目标数值", Vec3(3.5, 2.0, 3.5), 60, null, true)
        scene.idle(80)
    }


    private fun interactScene(scene: SceneBuilder, util: SceneBuildingUtil) {
        scene.title("container_s_d13f7", "火箭炮的发射流程")
        val context = GeneratedPonderSupport.Context()
        GeneratedPonderSupport.showStructure(scene, context, null, null, null, null)
        scene.idle(20)
        GeneratedPonderSupport.createEntity(scene, context, "superbwarfare:type_63", Vec3(3.5, 1.0, 3.5), Vec3(3.5, 2.0, 0.5), null, null, "{CanUpdate:1b,ChargeProgress:0.0f,DecoyCount:0,DecoyReloadCoolDown:500,DogTagIcon:[[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1]],ForgeData:{},GearRot:0.0f,GearUp:0b,GunXRot:0.0f,GunYRot:0.0f,Health:100.0f,HoverMode:0b,Inventory:{Items:[],Size:12},Invulnerable:0b,IsWreck:0b,LastAttacker:\"undefined\",LastDriver:\"undefined\",LeftWheelDamaged:0b,LeftWheelHealth:50.0f,Locked:0b,LoiterActive:0b,LoiterR:400.0f,LoiterX:0.0f,LoiterY:318.0f,LoiterZ:0.0f,MainEngineDamaged:0b,MainEngineHealth:50.0f,Pitch:0.0f,Power:0.0f,PropellerRot:0.0f,RightWheelDamaged:0b,RightWheelHealth:50.0f,ServerPitch:0.0f,ServerYaw:90.0f,SubEngineDamaged:0b,SubEngineHealth:50.0f,SympatheticDetonated:0b,TowedByUUID:\"\",TowingUUID:\"\",TowingUUIDs:[],TurretBurnTimer:0,TurretBurned:0b,TurretDamaged:0b,TurretHealth:50.0f,TurretXRot:0.0f,TurretYRot:0.0f,TurretYRotLock:0.0f,WeaponState:{AP:{tag:{Attachments:{},GunData:{},Perks:{}}},CM:{tag:{Attachments:{},GunData:{},Perks:{}}},HE:{tag:{Attachments:{},GunData:{},Perks:{}}},Main:{tag:{Attachments:{},GunData:{},Perks:{}}}},Yaw:0.0f}", null, "simultaneous", null, "down")
        scene.idle(20)
        scene.addKeyframe()
        GeneratedPonderSupport.showText(scene, "目前火箭炮使用三种中口径火箭弹", Vec3(3.5, 2.0, 3.5), 60, null, true)
        GeneratedPonderSupport.createItemEntity(scene, context, "superbwarfare:medium_rocket_cm", 1, Vec3(1.5, 1.0, 0.5), Vec3(0.0, 0.0, 0.0), null, null)
        GeneratedPonderSupport.createItemEntity(scene, context, "superbwarfare:medium_rocket_he", 1, Vec3(3.5, 1.0, 0.5), Vec3(0.0, 0.0, 0.0), null, null)
        GeneratedPonderSupport.createItemEntity(scene, context, "superbwarfare:medium_rocket_ap", 1, Vec3(5.5, 1.0, 0.5), Vec3(0.0, 0.0, 0.0), null, null)
        scene.idle(80)
        GeneratedPonderSupport.rotateCameraY(scene, -145.5f, 0f, 20)
        scene.idle(20)
        GeneratedPonderSupport.clearItemEntities(scene, context, true, null, null, null, null)
        scene.addKeyframe()
        GeneratedPonderSupport.showText(scene, "火箭弹可以放置在光标指向为“空”的炮管", Vec3(3.5, 1.3, 3.5), 60, null, true)
        scene.idle(80)
        GeneratedPonderSupport.showControls(scene, Vec3(3.5, 1.3, 3.5), "right", 60, "right", "superbwarfare:medium_rocket_ap", null, false, false)
        scene.idle(80)
        GeneratedPonderSupport.showControls(scene, Vec3(3.5, 1.3, 3.5), "right", 60, "right", "superbwarfare:crowbar", "{Damage:0}", false, false)
        scene.addKeyframe()
        GeneratedPonderSupport.showText(scene, "使用本模组的撬棍右键炮塔即可按照排序轮流发射", Vec3(3.5, 1.3, 3.5), 60, null, true)
        scene.idle(80)
        scene.addKeyframe()
        GeneratedPonderSupport.showText(scene, "当然，使用打火石也可以", Vec3(3.5, 1.3, 3.5), 60, null, true)
        scene.idle(80)
        scene.addKeyframe()
        GeneratedPonderSupport.showText(scene, "如果不想要轮流发射也可以用光标对着已装填的炮管使用撬棍也可以发射", Vec3(3.3, 1.3, 3.5), 60, null, true)
        scene.idle(80)
    }


}
