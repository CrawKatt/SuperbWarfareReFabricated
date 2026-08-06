package com.atsuishio.superbwarfare.compat.ponder.storyboard

import com.atsuishio.superbwarfare.entity.living.TargetEntity
import com.atsuishio.superbwarfare.init.ModEntities
import net.createmod.catnip.math.Pointing
import net.createmod.ponder.api.scene.SceneBuilder
import net.createmod.ponder.api.scene.SceneBuildingUtil
import net.minecraft.commands.arguments.EntityAnchorArgument
import net.minecraft.core.Direction
import net.minecraft.world.entity.Entity
import net.minecraft.world.item.Items
import net.minecraft.world.phys.Vec3

object TargetStoryBoard {
    fun infoScene(scene: SceneBuilder, util: SceneBuildingUtil) {
        with(scene) {
            configureBasePlate(0, 0, 5)

            title("target_intro", "Target Introduction")

            showBasePlate()
            world().showSection(util.select().everywhere(), Direction.UP)

            val centerPos = util.grid().at(2, 0, 2)
            val pos = util.vector().topOf(centerPos)
            val entity = world().createEntity {
                val target = ModEntities.TARGET.get().create(it) ?: return@createEntity null
                target.setPosRaw(pos.x, pos.y, pos.z)
                target.lookAt(EntityAnchorArgument.Anchor.EYES, Vec3(pos.x, pos.y, pos.z - 1))
                target.xRot = 0f
                target.xRotO = 0f
                target
            }

            idle(20)
            overlay().showText(40).pointAt(Vec3(3.5, 1.0, 3.5)).placeNearTarget()
                .text("The Target is the first item in Superb Warfare")
            idle(40)

            idle(20)
            addKeyframe()
            overlay().showText(40).pointAt(Vec3(3.5, 1.0, 4.5)).placeNearTarget()
                .text("Attacking the Target with a weapon will display the damage dealt by that attack")
            idle(20)

            overlay().showControls(Vec3(2.5, 2.0, 2.5), Pointing.RIGHT, 40)
                .withItem(Items.DIAMOND_SWORD.defaultInstance)
                .leftClick()
            idle(25)
            world().modifyEntity(entity) {
                val target = it as? TargetEntity ?: return@modifyEntity
                target.downTime = 40
            }

            overlay().showText(40).pointAt(Vec3(3.5, 1.0, 4.5)).placeNearTarget()
                .text("The Target will fall down after taking enough damage, and will recover after a short time")
            idle(55)

            markAsFinished()
        }
    }

    fun interactScene(scene: SceneBuilder, util: SceneBuildingUtil) {
        with(scene) {
            configureBasePlate(0, 0, 5)

            title("target_interact", "Target Interaction")

            showBasePlate()
            world().showSection(util.select().everywhere(), Direction.UP)

            val centerPos = util.grid().at(2, 0, 2)
            val pos = util.vector().topOf(centerPos)
            val entity = world().createEntity {
                val target = ModEntities.TARGET.get().create(it) ?: return@createEntity null
                target.setPosRaw(pos.x, pos.y, pos.z)
                target.lookAt(EntityAnchorArgument.Anchor.EYES, Vec3(pos.x - 1, pos.y, pos.z))
                target.xRot = 0f
                target.xRotO = 0f
                target
            }

            idle(20)
            overlay().showText(40).pointAt(Vec3(2.5, 1.0, 2.5)).placeNearTarget()
                .text("Right-click with an empty hand to make the Target face you")
            idle(20)

            overlay().showControls(Vec3(2.5, 2.0, 2.5), Pointing.RIGHT, 20)
                .rightClick()

            idle(20)
            world().modifyEntity(entity) {
                it.lookAt(EntityAnchorArgument.Anchor.EYES, Vec3(pos.x, pos.y, pos.z - 1))
                it.xRot = 0f
                it.xRotO = 0f
            }
            idle(10)

            idle(20)
            addKeyframe()
            overlay().showText(40).pointAt(Vec3(2.5, 2.0, 2.5)).placeNearTarget()
                .text("Sneak + right-click with an empty hand to dismantle the Target")
            idle(20)

            overlay().showControls(Vec3(2.5, 2.0, 2.5), Pointing.RIGHT, 30)
                .rightClick()
                .whileSneaking()
            idle(10)
            world().modifyEntity(entity, Entity::discard)
            idle(30)

            markAsFinished()
        }
    }
}