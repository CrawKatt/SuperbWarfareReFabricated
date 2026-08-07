package com.atsuishio.superbwarfare.compat.ponder.scene

import com.atsuishio.superbwarfare.Mod.Companion.loc
import com.atsuishio.superbwarfare.init.ModBlocks
import com.atsuishio.superbwarfare.init.ModEntities
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper
import net.createmod.ponder.api.scene.SceneBuilder
import net.createmod.ponder.api.scene.SceneBuildingUtil
import net.minecraft.commands.arguments.EntityAnchorArgument
import net.minecraft.core.Direction
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.phys.Vec3

object ChargingStationPonderScene {
    fun register(helper: PonderSceneRegistrationHelper<ResourceLocation>) {
        helper.forComponents(loc("charging_station"))
            .addStoryBoard("basic_15x15", ChargingStationPonderScene::introScene)
    }

    private fun introScene(scene: SceneBuilder, util: SceneBuildingUtil) {
        with(scene) {
            configureBasePlate(0, 0, 15)

            title("charging_station_intro", "Charging Station Introduction")

            scaleSceneView(0.5f)
            world().showSection(util.select().everywhere(), Direction.UP)

            val blockPos = util.grid().at(7, 1, 7)
            world().setBlock(blockPos, ModBlocks.CHARGING_STATION.get().defaultBlockState(), false)
            idle(20)

            overlay().showText(60).pointAt(Vec3(7.5, 1.0, 7.0)).placeNearTarget()
                .text("充电站是一个为本模组载具和炮塔无线提供电力的方块")
            idle(60)

            val tower = world().createEntity {
                val centerPos = util.grid().at(10, 3, 7)
                val pos = util.vector().topOf(centerPos)
                val entity = ModEntities.WAVEFORCE_TOWER.get().create(it) ?: return@createEntity null
                entity.lookAt(EntityAnchorArgument.Anchor.EYES, Vec3(pos.x - 1, pos.y, pos.z))
                entity.setPosRaw(pos.x, pos.y, pos.z)
                entity
            }
            val vehicle = world().createEntity {
                val centerPos = util.grid().at(4, 3, 7)
                val pos = util.vector().topOf(centerPos)
                val entity = ModEntities.WHEEL_CHAIR.get().create(it) ?: return@createEntity null
                entity.lookAt(EntityAnchorArgument.Anchor.EYES, Vec3(pos.x, pos.y, pos.z - 1))
                entity.setPosRaw(pos.x, pos.y, pos.z)
                entity
            }
            idle(40)

            markAsFinished()
        }
    }
}