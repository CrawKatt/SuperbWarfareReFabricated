package com.atsuishio.superbwarfare.compat.ponder.scene

import com.atsuishio.superbwarfare.Mod.Companion.loc
import com.atsuishio.superbwarfare.compat.ponder.GeneratedPonderSupport
import com.atsuishio.superbwarfare.compat.ponder.GeneratedPonderSupport.createEntity
import com.atsuishio.superbwarfare.compat.ponder.GeneratedPonderSupport.modifyBlockEntity
import com.atsuishio.superbwarfare.compat.ponder.GeneratedPonderSupport.modifyEntitiesNbt
import com.atsuishio.superbwarfare.compat.ponder.GeneratedPonderSupport.preScanBounds
import com.atsuishio.superbwarfare.compat.ponder.GeneratedPonderSupport.setBlock
import com.atsuishio.superbwarfare.compat.ponder.GeneratedPonderSupport.showControls
import com.atsuishio.superbwarfare.compat.ponder.GeneratedPonderSupport.showStructure
import com.atsuishio.superbwarfare.compat.ponder.GeneratedPonderSupport.showText
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper
import net.createmod.ponder.api.scene.SceneBuilder
import net.createmod.ponder.api.scene.SceneBuildingUtil
import net.minecraft.core.BlockPos
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.phys.Vec3
import java.util.Map

object AircraftCatapultPonderScene {
    fun register(helper: PonderSceneRegistrationHelper<ResourceLocation>) {
        helper.forComponents(loc("aircraft_catapult"))
            .addStoryBoard("basic_15x15", AircraftCatapultPonderScene::introScene)
    }

    private fun introScene(scene: SceneBuilder, util: SceneBuildingUtil) {
        scene.title("aircraft_catapult_intro", "Aircraft Catapult Introduction")
        val context = GeneratedPonderSupport.Context()
        preScanBounds(scene, BlockPos(6, 0, 0), BlockPos(7, 1, 14))
        scene.addKeyframe()
        showStructure(scene, context, null, null, null, null)
        scene.idle(20)
        setBlock(
            scene,
            context,
            "superbwarfare:aircraft_catapult",
            null,
            BlockPos(7, 0, 0),
            BlockPos(7, 0, 13),
            null,
            immediateDisplay = false,
            spawnParticles = false,
            entranceAnimation = "simultaneous",
            entranceDuration = 20,
            entranceInterval = 1,
            smartDisplay = false,
            linkId = null,
            direction = "down"
        )
        setBlock(
            scene,
            context,
            "superbwarfare:catapult_controller",
            null,
            BlockPos(7, 0, 14),
            null,
            null,
            immediateDisplay = false,
            spawnParticles = false,
            entranceAnimation = "down",
            entranceDuration = 20,
            entranceInterval = 1,
            smartDisplay = false,
            linkId = null,
            direction = "down"
        )
        setBlock(
            scene,
            context,
            "minecraft:lever",
            Map.ofEntries<String, String>(
                Map.entry<String, String>("face", "floor"),
                Map.entry<String, String>("powered", "false")
            ),
            BlockPos(6, 1, 14),
            null,
            null,
            false,
            spawnParticles = false,
            entranceAnimation = "down",
            entranceDuration = 20,
            entranceInterval = 1,
            smartDisplay = false,
            linkId = null,
            direction = "down"
        )
        scene.idle(20)
        scene.addKeyframe()
        showText(scene, "This is a complete aircraft catapult system", Vec3(7.5, 1.0, 7.5), 60, null, true)
        scene.idle(80)
        scene.addKeyframe()
        showText(scene, "Sneak + right-click the catapult controller to adjust the launch strength", Vec3(7.5, 1.0, 14.5), 60, null, false)
        scene.idle(80)
        scene.addKeyframe()
        showControls(
            scene, Vec3(7.5, 1.0, 14.5), "right", 60, "right", null, null,
            whileSneaking = true,
            whileCtrl = false
        )
        scene.idle(80)
        scene.addKeyframe()
        showText(scene, "Install the catapult shuttle", Vec3(7.5, 1.0, 13.5), 60, null, false)
        scene.idle(60)
        scene.addKeyframe()
        showControls(
            scene,
            Vec3(7.5, 1.0, 13.5),
            "down",
            60,
            "right",
            "superbwarfare:catapult_shuttle",
            null,
            whileSneaking = false,
            whileCtrl = false
        )
        createEntity(
            scene,
            context,
            "superbwarfare:catapult_shuttle",
            Vec3(7.5, 0.0, 13.5),
            Vec3(7.5, 1.0, 0.5),
            null,
            null,
            "{CanUpdate:1b,ForgeData:{},Invulnerable:0b,TowingUUID:\"\"}",
            null,
            null,
            null,
            null
        )
        scene.idle(80)
        showControls(
            scene, Vec3(6.0, 1.5, 14.0), "down", 60, "right", null, null,
            whileSneaking = false,
            whileCtrl = false
        )
        scene.addKeyframe()
        showText(scene, "Pull the lever", Vec3(6.5, 1.5, 14.0), 60, null, true)
        modifyBlockEntity(
            scene,
            Map.ofEntries<String, String>(
                Map.entry<String, String>("face", "floor"),
                Map.entry<String, String>("powered", "true")
            ),
            null,
            true,
            BlockPos(6, 1, 14),
            null
        )
        modifyEntitiesNbt(scene, context, true, null, null, null, null, null, Vec3(0.0, 0.0, -13.0), 5, null)
        scene.idle(80)
        scene.addKeyframe()
        showText(scene, "The catapult module starts", Vec3(7.5, 1.0, 0.5), 60, null, true)
        scene.idle(80)
        showControls(
            scene, Vec3(6.0, 1.5, 14.0), "down", 60, "right", null, null,
            whileSneaking = false,
            whileCtrl = false
        )
        modifyBlockEntity(
            scene,
            Map.ofEntries<String, String>(
                Map.entry<String, String>("face", "floor"),
                Map.entry<String, String>("powered", "false")
            ),
            null,
            true,
            BlockPos(6, 1, 14),
            null
        )
        modifyEntitiesNbt(scene, context, true, null, null, null, null, null, Vec3(0.0, 0.0, 13.0), 5, null)
        scene.idle(80)
        createEntity(
            scene,
            context,
            "superbwarfare:wheel_chair",
            Vec3(7.5, 1.0, 14.5),
            null,
            null,
            null,
            "{CanUpdate:1b,ChargeProgress:0.0f,DecoyCount:0,DecoyReloadCoolDown:500,DogTagIcon:[[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],[I;-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1]],Energy:24000,ForgeData:{},GearRot:0.0f,GearUp:0b,GunXRot:0.0f,GunYRot:0.0f,Health:30.0f,HoverMode:0b,Inventory:{Items:[],Size:0},Invulnerable:0b,IsWreck:0b,LastAttacker:\"undefined\",LastDriver:\"undefined\",LeftWheelDamaged:0b,LeftWheelHealth:50.0f,Locked:0b,LoiterActive:0b,LoiterR:400.0f,LoiterX:0.0f,LoiterY:318.0f,LoiterZ:0.0f,MainEngineDamaged:0b,MainEngineHealth:50.0f,Power:0.0f,PropellerRot:0.0f,RightWheelDamaged:0b,RightWheelHealth:50.0f,SelectedWeapon:[I;0],ServerPitch:-0.053771615f,ServerYaw:172.53662f,SubEngineDamaged:0b,SubEngineHealth:50.0f,SympatheticDetonated:0b,TowedByUUID:\"\",TowingUUID:\"\",TowingUUIDs:[],TurretBurnTimer:0,TurretBurned:0b,TurretDamaged:0b,TurretHealth:50.0f,TurretXRot:0.0f,TurretYRot:0.0f,TurretYRotLock:0.0f}",
            null,
            "simultaneous",
            null,
            "south"
        )
        scene.addKeyframe()
        showText(scene, "Use a tow bar to connect the vehicle to the catapult module with right-click", Vec3(7.5, 1.0, 13.5), 60, null, true)
        scene.idle(60)
        showControls(
            scene, Vec3(7.5, 1.0, 13.5), "right", 60, "right", "superbwarfare:tow_bar", null,
            whileSneaking = false,
            whileCtrl = false
        )
        scene.idle(60)
        showControls(
            scene, Vec3(7.5, 1.5, 14.5), "down", 60, "right", "superbwarfare:tow_bar", null,
            whileSneaking = false,
            whileCtrl = false
        )
        scene.idle(80)
        showControls(
            scene, Vec3(6.0, 1.5, 14.0), "down", 60, "right", null, null,
            whileSneaking = false,
            whileCtrl = false
        )
        scene.addKeyframe()
        showText(scene, "Pull the lever", Vec3(6.5, 1.5, 14.0), 60, null, true)
        modifyBlockEntity(
            scene,
            Map.ofEntries<String, String>(
                Map.entry<String, String>("face", "floor"),
                Map.entry<String, String>("powered", "true")
            ),
            null,
            true,
            BlockPos(6, 1, 14),
            null
        )
        modifyEntitiesNbt(
            scene,
            context,
            true,
            "superbwarfare:catapult_shuttle",
            null,
            null,
            null,
            null,
            Vec3(0.0, 0.0, -13.0),
            5,
            null
        )
        modifyEntitiesNbt(
            scene,
            context,
            true,
            "superbwarfare:wheel_chair",
            null,
            null,
            null,
            null,
            Vec3(0.0, 0.0, -16.0),
            5,
            null
        )
        scene.idle(80)
        scene.addKeyframe()
        showText(scene, "Launch complete", Vec3(7.5, 1.0, 0.5), 60, null, true)
        scene.idle(80)
    }
}
