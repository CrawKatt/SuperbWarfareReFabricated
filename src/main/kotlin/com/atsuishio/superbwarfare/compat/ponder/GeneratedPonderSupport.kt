@file:Suppress("unused")

package com.atsuishio.superbwarfare.compat.ponder

import com.atsuishio.superbwarfare.tools.mc
import com.atsuishio.superbwarfare.tools.tag
import com.mojang.logging.LogUtils
import net.createmod.catnip.math.Pointing
import net.createmod.ponder.api.PonderPalette
import net.createmod.ponder.api.element.*
import net.createmod.ponder.api.scene.*
import net.createmod.ponder.foundation.PonderScene
import net.createmod.ponder.foundation.instruction.DisplayWorldSectionInstruction
import net.createmod.ponder.foundation.instruction.FadeOutOfSceneInstruction
import net.createmod.ponder.foundation.instruction.TickingInstruction
import net.minecraft.client.Minecraft
import net.minecraft.client.resources.sounds.SimpleSoundInstance
import net.minecraft.client.resources.sounds.SoundInstance
import net.minecraft.commands.arguments.EntityAnchorArgument
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.nbt.*
import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.Rotation
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.Property
import net.minecraft.world.phys.Vec3
import org.slf4j.Logger
import java.io.BufferedInputStream
import java.io.DataInputStream
import java.util.*
import java.util.function.Consumer
import java.util.zip.GZIPInputStream

/**
 * Kotlin port of the helper class exported by The Ponderer.
 *
 * All original static-style calls keep the same shape:
 * `GeneratedPonderSupport.showText(scene, ...)`.
 */
@Suppress("unused", "DEPRECATION")
object GeneratedPonderSupport {
    private val LOGGER: Logger = LogUtils.getLogger()

    class Context {
        internal val sectionLinks = HashMap<String, ElementLink<WorldSectionElement>>()
        internal val entityLinks = HashMap<String, MutableList<ElementLink<EntityElement>>>()
        internal val visibleBlockKeys = HashSet<Long>()
        internal val hiddenBlockKeys = HashSet<Long>()
        internal var allBlocksVisible = false
    }

    private class EntityMoveTarget(
        val entity: Entity,
        val startPos: Vec3
    )

    private class EntityMoveInstruction(
        linkedTargets: List<ElementLink<EntityElement>>?,
        private val selection: Selection?,
        private val entityFilter: ResourceLocation?,
        private val totalOffset: Vec3,
        private val walkAnimation: Boolean,
        duration: Int
    ) : TickingInstruction(false, duration) {
        private val linkedTargets = linkedTargets?.toList()
        private val walkAnimationSpeed = computeWalkAnimationSpeed(totalOffset, duration)
        private val targets = ArrayList<EntityMoveTarget>()

        override fun firstTick(scene: PonderScene) {
            super.firstTick(scene)
            if (linkedTargets != null) {
                for (link in linkedTargets) {
                    val element = scene.resolve(link) ?: continue
                    element.ifPresent { entity ->
                        if (entity != null && entity.isAlive) {
                            collectTarget(entity)
                        }
                    }
                }
                return
            }

            scene.forEachWorldEntity(Entity::class.java) { entity ->
                collectTarget(entity)
            }
        }

        override fun tick(scene: PonderScene) {
            super.tick(scene)
            if (targets.isEmpty()) {
                return
            }

            val elapsedTicks = totalTicks - remainingTicks
            val progress = minOf(1.0, elapsedTicks / totalTicks.toDouble())

            for (target in targets) {
                val entity = target.entity
                if (!entity.isAlive) {
                    continue
                }

                val targetPos = target.startPos.add(totalOffset.scale(progress))
                entity.setOldPosAndRot()
                entity.setPos(targetPos.x, targetPos.y, targetPos.z)
                entity.deltaMovement = Vec3.ZERO
                if (walkAnimation && progress < 1.0) {
                    applyWalkAnimation(entity, walkAnimationSpeed)
                } else {
                    stopWalkAnimation(entity)
                }
            }
        }

        private fun collectTarget(entity: Entity) {
            if (!entity.isAlive || entity is ItemEntity) {
                return
            }
            if (selection != null && !selection.test(entity.blockPosition())) {
                return
            }
            if (!matchesEntityType(entity, entityFilter)) {
                return
            }
            targets.add(EntityMoveTarget(entity, entity.position()))
        }
    }

    private class EntityEntranceInstruction(
        private val entityLink: ElementLink<EntityElement>,
        private val targetPos: Vec3,
        private val entranceOffset: Vec3,
        duration: Int
    ) : TickingInstruction(false, duration) {
        override fun firstTick(scene: PonderScene) {
            super.firstTick(scene)
            positionEntity(scene, targetPos.add(entranceOffset), true)
        }

        override fun tick(scene: PonderScene) {
            super.tick(scene)
            val fade = if (totalTicks <= 0) 0.0 else remainingTicks / totalTicks.toDouble()
            positionEntity(scene, targetPos.add(entranceOffset.scale(fade * fade)), false)
        }

        private fun positionEntity(scene: PonderScene, pos: Vec3, snapOld: Boolean) {
            val element = scene.resolve(entityLink) ?: return
            element.ifPresent { entity ->
                if (entity == null || !entity.isAlive) {
                    return@ifPresent
                }
                if (!snapOld) {
                    entity.setOldPosAndRot()
                }
                entity.setPos(pos.x, pos.y, pos.z)
                entity.deltaMovement = Vec3.ZERO
                if (snapOld) {
                    entity.setOldPosAndRot()
                }
                stopWalkAnimation(entity)
            }
        }
    }

    private class EntityExitInstruction(
        linkedTargets: List<ElementLink<EntityElement>>?,
        private val selection: Selection?,
        private val entityFilter: ResourceLocation?,
        private val exitOffset: Vec3,
        duration: Int
    ) : TickingInstruction(false, duration) {
        private val linkedTargets = linkedTargets?.toList()
        private val targets = ArrayList<EntityMoveTarget>()

        override fun firstTick(scene: PonderScene) {
            super.firstTick(scene)
            if (linkedTargets != null) {
                for (link in linkedTargets) {
                    val element = scene.resolve(link) ?: continue
                    element.ifPresent { entity ->
                        if (entity != null && entity.isAlive) {
                            collectTarget(entity)
                        }
                    }
                }
                return
            }

            scene.forEachWorldEntity(Entity::class.java) { entity ->
                collectTarget(entity)
            }
        }

        override fun tick(scene: PonderScene) {
            super.tick(scene)
            if (targets.isEmpty()) {
                return
            }

            val progress = minOf(
                1.0,
                (totalTicks - remainingTicks) / totalTicks.toDouble()
            )
            val finished = remainingTicks == 0
            for (target in targets) {
                val entity = target.entity
                if (!entity.isAlive) {
                    continue
                }

                val targetPos = target.startPos.add(exitOffset.scale(progress * progress))
                entity.setOldPosAndRot()
                entity.setPos(targetPos.x, targetPos.y, targetPos.z)
                entity.deltaMovement = Vec3.ZERO
                stopWalkAnimation(entity)
                if (finished) {
                    entity.discard()
                }
            }
        }

        private fun collectTarget(entity: Entity) {
            if (!entity.isAlive || entity is ItemEntity) {
                return
            }
            if (selection != null && !selection.test(entity.blockPosition())) {
                return
            }
            if (!matchesEntityType(entity, entityFilter)) {
                return
            }
            targets.add(EntityMoveTarget(entity, entity.position()))
        }
    }

    @JvmStatic
    fun showStructure(
        scene: SceneBuilder,
        context: Context,
        pos1: BlockPos?,
        pos2: BlockPos?,
        scale: Float?,
        rotation: Float?
    ) {
        val selection: Selection
        val everywhere: Boolean
        if (pos1 != null) {
            selection = if (pos2 == null) {
                scene.scene.sceneBuildingUtil.select().position(pos1)
            } else {
                scene.scene.sceneBuildingUtil.select().fromTo(pos1, pos2)
            }
            everywhere = false
        } else {
            selection = scene.scene.sceneBuildingUtil.select().everywhere()
            everywhere = true
        }
        scene.world().showSection(selection, Direction.UP)
        if (everywhere) {
            context.allBlocksVisible = true
            context.visibleBlockKeys.clear()
            context.hiddenBlockKeys.clear()
        } else {
            updateVisibleRange(context, pos1, pos2, true)
        }
        if (scale != null) {
            scene.scaleSceneView(scale)
        }
        val rotationOffset = rotation ?: 0f
        if (rotationOffset != 0f) {
            scene.addInstruction { ps ->
                val yRotation = ps.transform.yRotation
                val target = (yRotation.chaseTarget + rotationOffset).toDouble()
                yRotation.startWithValue(target)
            }
        }
    }

    @JvmStatic
    fun showText(
        scene: SceneBuilder,
        text: String?,
        point: Vec3?,
        duration: Int,
        color: String?,
        placeNearTarget: Boolean
    ) {
        val builder: TextElementBuilder = scene.overlay()
            .showText(duration)
            .text(text ?: "")
        point?.let { builder.pointAt(it) }
        parsePalette(color)?.let { builder.colored(it) }
        if (placeNearTarget) {
            builder.placeNearTarget()
        }
    }

    @JvmStatic
    fun createEntity(
        scene: SceneBuilder,
        entityId: String?,
        pos: Vec3,
        lookAt: Vec3?,
        yaw: Float?,
        pitch: Float?,
        nbt: String?
    ) {
        createEntity(scene, null, entityId, pos, lookAt, yaw, pitch, nbt, null, null, null, null)
    }

    @JvmStatic
    fun createEntity(
        scene: SceneBuilder,
        context: Context?,
        entityId: String?,
        pos: Vec3,
        lookAt: Vec3?,
        yaw: Float?,
        pitch: Float?,
        nbt: String?,
        linkId: String?
    ) {
        createEntity(scene, context, entityId, pos, lookAt, yaw, pitch, nbt, linkId, null, null, null)
    }

    @JvmStatic
    fun createEntity(
        scene: SceneBuilder,
        context: Context?,
        entityId: String?,
        pos: Vec3,
        lookAt: Vec3?,
        yaw: Float?,
        pitch: Float?,
        nbt: String?,
        linkId: String?,
        entranceAnimation: String?,
        entranceDuration: Int?
    ) {
        createEntity(
            scene,
            context,
            entityId,
            pos,
            lookAt,
            yaw,
            pitch,
            nbt,
            linkId,
            entranceAnimation,
            entranceDuration,
            null
        )
    }

    @JvmStatic
    fun createEntity(
        scene: SceneBuilder,
        context: Context?,
        entityId: String?,
        pos: Vec3,
        lookAt: Vec3?,
        yaw: Float?,
        pitch: Float?,
        nbt: String?,
        linkId: String?,
        entranceAnimation: String?,
        entranceDuration: Int?,
        direction: String?
    ) {
        val loc = entityId?.let(ResourceLocation::tryParse) ?: return
        val entranceOffset = entityEntranceOffset(entranceAnimation, direction)
        val resolvedEntranceDuration = (entranceDuration ?: 20).coerceAtLeast(0)
        val animatedEntrance = entranceOffset != null && resolvedEntranceDuration > 0
        val spawnPos = if (entranceOffset != null && animatedEntrance) {
            pos.add(entranceOffset)
        } else {
            pos
        }
        val entityLink = scene.world().createEntity { level ->
            val type = BuiltInRegistries.ENTITY_TYPE.getOptional(loc).orElse(null)
                ?: return@createEntity null
            val entity = type.create(level)
                ?: return@createEntity null
            entity.setPosRaw(spawnPos.x, spawnPos.y, spawnPos.z)
            entity.setOldPosAndRot()
            val targetLook = lookAt ?: pos.add(0.0, 0.0, -1.0)
            entity.lookAt(EntityAnchorArgument.Anchor.FEET, targetLook)
            if (yaw != null) {
                entity.yRot = yaw
                entity.yHeadRot = yaw
                entity.setYBodyRot(yaw)
            }
            if (pitch != null) {
                entity.xRot = pitch
            }
            if (entity is Mob) {
                entity.isNoAi = true
            }
            entity.isNoGravity = true
            entity.deltaMovement = Vec3.ZERO
            stopWalkAnimation(entity)
            if (!nbt.isNullOrBlank()) {
                runCatching {
                    val patch = TagParser.parseTag(nbt)
                    val data = CompoundTag()
                    entity.saveWithoutId(data)
                    data.merge(patch)
                    entity.load(data)
                }
            }
            entity
        }
        registerEntityLink(context, linkId, entityLink)
        if (entranceOffset != null && animatedEntrance) {
            scene.addInstruction(
                EntityEntranceInstruction(
                    entityLink,
                    pos,
                    entranceOffset,
                    resolvedEntranceDuration
                )
            )
        }
    }

    @JvmStatic
    fun createItemEntity(
        scene: SceneBuilder,
        itemId: String?,
        count: Int,
        pos: Vec3,
        motion: Vec3,
        nbt: String?
    ) {
        createItemEntity(scene, null, itemId, count, pos, motion, nbt, null)
    }

    @JvmStatic
    fun createItemEntity(
        scene: SceneBuilder,
        context: Context?,
        itemId: String?,
        count: Int,
        pos: Vec3,
        motion: Vec3,
        nbt: String?,
        linkId: String?
    ) {
        val loc = itemId?.let(ResourceLocation::tryParse)
        val item = loc?.let { BuiltInRegistries.ITEM.getOptional(it).orElse(null) }
        if (item == null) {
            return
        }
        val patch = if (!nbt.isNullOrBlank()) {
            runCatching { TagParser.parseTag(nbt) }.getOrNull()
        } else {
            null
        }
        val entityLink = scene.world().createEntity { level ->
            var stack = ItemStack(item, maxOf(1, count))
            if (patch != null) {
                stack = applyPatchToItemStack(stack, patch)
            }
            val entity = ItemEntity(level, pos.x, pos.y, pos.z, stack)
            entity.deltaMovement = motion
            if (patch != null && isLikelyEntityPatch(patch)) {
                mergeEntityNbt(entity, patch)
            }
            entity
        }
        registerEntityLink(context, linkId, entityLink)
    }

    @JvmStatic
    fun rotateCameraY(scene: SceneBuilder, degrees: Float, degreesX: Float, duration: Int) {
        scene.addInstruction { ponderScene ->
            val yRotation = ponderScene.transform.yRotation
            val target = (yRotation.chaseTarget + degrees).toDouble()
            if (duration == 0) {
                yRotation.startWithValue(target)
            } else {
                yRotation.chaseTimed(target, duration)
            }
            if (degreesX != 0f) {
                val xRotation = ponderScene.transform.xRotation
                val targetX = (xRotation.chaseTarget + degreesX).toDouble()
                if (duration == 0) {
                    xRotation.startWithValue(targetX)
                } else {
                    xRotation.chaseTimed(targetX, duration)
                }
            }
        }
        if (duration > 0) {
            scene.idle(duration)
        }
    }

    @JvmStatic
    fun rotateCameraY(scene: SceneBuilder, degrees: Float, duration: Int) {
        rotateCameraY(scene, degrees, 0f, duration)
    }

    @JvmStatic
    fun highlightSection(
        scene: SceneBuilder,
        color: String?,
        pos1: BlockPos,
        pos2: BlockPos?,
        duration: Int
    ) {
        val targetPos2 = pos2 ?: pos1
        val palette = parsePalette(color) ?: PonderPalette.BLUE
        val selection = scene.scene.sceneBuildingUtil.select().fromTo(pos1, targetPos2)
        scene.overlay().showOutline(palette, Any(), selection, duration)
    }

    @JvmStatic
    fun showControls(
        scene: SceneBuilder,
        point: Vec3,
        direction: String?,
        duration: Int,
        action: String?,
        itemSpec: String?,
        nbt: String?,
        whileSneaking: Boolean,
        whileCtrl: Boolean
    ) {
        val builder: InputElementBuilder =
            scene.overlay().showControls(point, parsePointing(direction), duration)
        when ((action ?: "").lowercase(Locale.ROOT)) {
            "left" -> builder.leftClick()
            "right" -> builder.rightClick()
            "scroll" -> builder.scroll()
        }
        if (!itemSpec.isNullOrBlank()) {
            parseItemStackSpec(itemSpec, nbt)?.let(builder::withItem)
        }
        if (whileSneaking) {
            builder.whileSneaking()
        }
        if (whileCtrl) {
            builder.whileCTRL()
        }
    }

    @JvmStatic
    fun encapsulateBounds(scene: SceneBuilder, size: BlockPos) {
        scene.addInstruction { ps ->
            ps.world.getBounds().encapsulate(size)
        }
    }

    @JvmStatic
    fun preScanBounds(scene: SceneBuilder, minCorner: BlockPos?, maxCorner: BlockPos?) {
        if (minCorner == null || maxCorner == null) {
            return
        }
        scene.addInstruction { ps ->
            ps.world.getBounds().encapsulate(minCorner)
            ps.world.getBounds().encapsulate(maxCorner)
        }
    }

    @JvmStatic
    fun playSound(
        scene: SceneBuilder,
        soundId: String?,
        volume: Float,
        pitch: Float,
        source: String?
    ) {
        val loc = soundId?.let(ResourceLocation::tryParse)
        val sound = loc?.let { BuiltInRegistries.SOUND_EVENT.getOptional(it).orElse(null) }
        if (sound == null) {
            return
        }
        val soundSource = parseSoundSource(source)
        scene.addInstruction {
            val player = Minecraft.getInstance().player ?: return@addInstruction
            val instance = SimpleSoundInstance(
                sound,
                soundSource,
                volume,
                pitch,
                SoundInstance.createUnseededRandom(),
                player.blockPosition()
            )
            mc.soundManager.play(instance)
        }
    }

    @JvmStatic
    fun setBlock(
        scene: SceneBuilder,
        context: Context,
        blockId: String?,
        blockProperties: Map<String, String>?,
        pos1: BlockPos?,
        pos2: BlockPos?,
        nbt: String?,
        immediateDisplay: Boolean?,
        spawnParticles: Boolean?,
        entranceAnimation: String?,
        entranceDuration: Int?,
        entranceInterval: Int?,
        smartDisplay: Boolean?,
        linkId: String?,
        direction: String?
    ) {
        val loc = blockId?.let(ResourceLocation::tryParse)
        val block = loc?.let { BuiltInRegistries.BLOCK.getOptional(it).orElse(null) }
        if (block == null || pos1 == null) {
            return
        }
        val state = applyBlockProperties(block.defaultBlockState(), blockProperties)
        val targetPos2 = pos2 ?: pos1
        val immediate = immediateDisplay != false
        val isAir = state.isAir
        val particles = immediate && !isAir && spawnParticles != false
        val normalizedAnimation = normalizeEntranceAnimation(entranceAnimation)
        if (!isAir && normalizedAnimation != null && normalizedAnimation != "none") {
            applyAnimatedSetBlock(
                scene,
                context,
                state,
                pos1,
                targetPos2,
                nbt,
                normalizedAnimation,
                entranceDuration,
                entranceInterval,
                smartDisplay,
                linkId,
                direction
            )
            return
        }
        ensureSceneCanShowRange(scene, pos1, targetPos2, immediate && !isAir)
        updateVisibleRange(context, pos1, targetPos2, immediate && !isAir)
        if (pos1 != targetPos2) {
            val selection = scene.scene.sceneBuildingUtil.select().fromTo(pos1, targetPos2)
            scene.world().setBlocks(selection, state, particles)
            applySetBlockNbtPatch(scene, nbt, selection)
        } else {
            scene.world().setBlock(pos1, state, particles)
            applySetBlockNbtPatch(
                scene,
                nbt,
                scene.scene.sceneBuildingUtil.select().position(pos1)
            )
        }
    }

    @JvmStatic
    fun destroyBlock(
        scene: SceneBuilder,
        context: Context,
        pos1: BlockPos?,
        pos2: BlockPos?,
        destroyParticles: Boolean?
    ) {
        if (pos1 == null) {
            return
        }
        val targetPos2 = pos2 ?: pos1
        val particles = destroyParticles != false
        if (pos1 == targetPos2) {
            if (particles) {
                scene.world().destroyBlock(pos1)
            } else {
                scene.world().setBlock(pos1, Blocks.AIR.defaultBlockState(), false)
            }
        } else {
            for (cursor in BlockPos.betweenClosed(pos1, targetPos2)) {
                val target = cursor.immutable()
                if (particles) {
                    scene.world().destroyBlock(target)
                } else {
                    scene.world().setBlock(target, Blocks.AIR.defaultBlockState(), false)
                }
            }
        }
        updateVisibleRange(context, pos1, targetPos2, false)
    }

    @JvmStatic
    fun destroyBlock(
        scene: SceneBuilder,
        context: Context,
        pos: BlockPos?,
        destroyParticles: Boolean?
    ) {
        destroyBlock(scene, context, pos, null, destroyParticles)
    }

    @JvmStatic
    fun replaceBlocks(
        scene: SceneBuilder,
        context: Context,
        blockId: String?,
        blockProperties: Map<String, String>?,
        pos1: BlockPos?,
        pos2: BlockPos?,
        spawnParticles: Boolean?
    ) {
        val loc = blockId?.let(ResourceLocation::tryParse)
        val block = loc?.let { BuiltInRegistries.BLOCK.getOptional(it).orElse(null) }
        if (block == null || pos1 == null) {
            return
        }
        val targetPos2 = pos2 ?: pos1
        ensureSceneCanShowRange(scene, pos1, targetPos2, true)
        val selection = scene.scene.sceneBuildingUtil.select().fromTo(pos1, targetPos2)
        scene.world().replaceBlocks(
            selection,
            applyBlockProperties(block.defaultBlockState(), blockProperties),
            spawnParticles != false
        )
        updateVisibleRange(context, pos1, targetPos2, true)
    }

    @JvmStatic
    fun hideSection(
        scene: SceneBuilder,
        context: Context,
        pos1: BlockPos?,
        pos2: BlockPos?,
        duration: Int,
        directionRaw: String?
    ) {
        if (pos1 == null) {
            return
        }
        val targetPos2 = pos2 ?: pos1
        val selection = scene.scene.sceneBuildingUtil.select().fromTo(pos1, targetPos2)
        updateVisibleRange(context, pos1, targetPos2, false)
        val existingSectionLinks = ArrayList(context.sectionLinks.values)
        scene.addInstruction { ps ->
            if (ps.baseWorldSection.isEmpty) {
                val all = ps.sceneBuildingUtil.select().everywhere()
                ps.baseWorldSection.set(all)
                ps.baseWorldSection.isVisible = true
                ps.baseWorldSection.setFade(1f)
                ps.baseWorldSection.queueRedraw()
            }
            for (existing in existingSectionLinks) {
                val section = ps.resolve(existing) ?: continue
                section.erase(selection)
            }
        }
        val direction = parseDirection(directionRaw)
        val link = scene.world().makeSectionIndependent(selection)
        if (duration <= 0) {
            scene.addInstruction { ps ->
                val element = ps.resolve(link)
                if (element != null) {
                    element.isVisible = false
                    element.setFade(0f)
                }
            }
            return
        }
        if (duration == 15) {
            scene.world().hideIndependentSection(link, direction)
            return
        }
        scene.addInstruction(FadeOutOfSceneInstruction(duration, direction, link))
    }

    @JvmStatic
    fun showSectionAndMerge(
        scene: SceneBuilder,
        context: Context,
        pos1: BlockPos?,
        pos2: BlockPos?,
        linkId: String?,
        duration: Int,
        directionRaw: String?,
        entranceAnimation: String?,
        entranceDuration: Int?,
        entranceInterval: Int?,
        smartDisplay: Boolean?
    ) {
        if (pos1 == null) {
            return
        }
        val targetPos2 = pos2 ?: pos1
        val selection = scene.scene.sceneBuildingUtil.select().fromTo(pos1, targetPos2)
        var key = linkId?.trim() ?: ""
        if (key.isEmpty()) {
            key = autoLinkId(context)
        }
        val direction = parseDirection(directionRaw)
        val existing = context.sectionLinks[key]
        val normalizedAnimation = normalizeEntranceAnimation(entranceAnimation)
        var resolvedDuration = duration
        if (normalizedAnimation == "none") {
            resolvedDuration = 0
        }
        if (normalizedAnimation != null && normalizedAnimation != "none") {
            val rowDuration = (entranceDuration ?: 20).coerceAtLeast(0)
            val rowInterval = (entranceInterval ?: 1).coerceAtLeast(0)
            applyAnimatedShowSectionAndMerge(
                scene,
                context,
                key,
                existing,
                pos1,
                targetPos2,
                normalizedAnimation,
                direction,
                rowDuration,
                rowInterval,
                smartDisplay != false
            )
            updateVisibleRange(context, pos1, targetPos2, true)
            return
        }
        eraseSelectionFromOtherSections(scene, context, existing, selection)
        if (existing == null) {
            val created: ElementLink<WorldSectionElement>
            if (resolvedDuration <= 0) {
                created = scene.world().showIndependentSectionImmediately(selection)
            } else if (resolvedDuration == 15) {
                created = scene.world().showIndependentSection(selection, direction)
            } else {
                val instruction = DisplayWorldSectionInstruction(
                    resolvedDuration,
                    direction,
                    selection,
                    null
                )
                scene.addInstruction(instruction)
                created = instruction.createLink(scene.scene)
            }
            context.sectionLinks[key] = created
            updateVisibleRange(context, pos1, targetPos2, true)
            return
        }
        if (resolvedDuration <= 0) {
            scene.addInstruction { ps ->
                val element = ps.resolve(existing)
                if (element != null) {
                    element.add(selection)
                    element.queueRedraw()
                }
            }
            updateVisibleRange(context, pos1, targetPos2, true)
            return
        }
        if (resolvedDuration == 15) {
            scene.world().showSectionAndMerge(selection, direction, existing)
            updateVisibleRange(context, pos1, targetPos2, true)
            return
        }
        scene.addInstruction(
            DisplayWorldSectionInstruction(
                resolvedDuration,
                direction,
                selection
            ) {
                scene.scene.resolve(existing)
            }
        )
        updateVisibleRange(context, pos1, targetPos2, true)
    }

    @JvmStatic
    fun rotateSection(
        scene: SceneBuilder,
        context: Context,
        linkId: String?,
        pos1: BlockPos?,
        pos2: BlockPos?,
        rotX: Double,
        rotY: Double,
        rotZ: Double,
        duration: Int
    ) {
        val link = resolveSectionLink(scene, context, linkId, pos1, pos2)
        if (link != null) {
            scene.world().rotateSection(link, rotX, rotY, rotZ, duration)
        }
    }

    @JvmStatic
    fun moveSection(
        scene: SceneBuilder,
        context: Context,
        linkId: String?,
        pos1: BlockPos?,
        pos2: BlockPos?,
        offset: Vec3,
        duration: Int
    ) {
        val link = resolveSectionLink(scene, context, linkId, pos1, pos2)
        if (link != null) {
            scene.world().moveSection(link, offset, duration)
        }
    }

    @JvmStatic
    fun toggleRedstonePower(scene: SceneBuilder, pos1: BlockPos?, pos2: BlockPos?) {
        if (pos1 == null) {
            return
        }
        val targetPos2 = pos2 ?: pos1
        scene.world().toggleRedstonePower(
            scene.scene.sceneBuildingUtil.select().fromTo(pos1, targetPos2)
        )
    }

    @JvmStatic
    fun modifyBlockEntity(
        scene: SceneBuilder,
        blockProperties: Map<String, String>?,
        nbt: String?,
        redraw: Boolean?,
        pos1: BlockPos?,
        pos2: BlockPos?
    ) {
        if (pos1 == null) {
            return
        }
        val hasProps = !blockProperties.isNullOrEmpty()
        val hasNbt = !nbt.isNullOrBlank()
        if (!hasProps && !hasNbt) {
            return
        }
        val targetPos2 = pos2 ?: pos1
        val selection = scene.scene.sceneBuildingUtil.select().fromTo(pos1, targetPos2)
        val props = blockProperties ?: emptyMap()
        if (hasProps) {
            for (cursor in BlockPos.betweenClosed(pos1, targetPos2)) {
                val target = cursor.immutable()
                scene.world().modifyBlock(
                    target,
                    { state -> applyBlockProperties(state, props) },
                    false
                )
            }
        }
        if (hasNbt) {
            runCatching {
                val patch = TagParser.parseTag(nbt)
                scene.world().modifyBlockEntityNBT(
                    selection,
                    BlockEntity::class.java,
                    { it.merge(patch.copy()) },
                    redraw == true
                )
            }
        }
    }

    @JvmStatic
    fun indicateRedstone(scene: SceneBuilder, pos: BlockPos?) {
        if (pos != null) {
            scene.effects().indicateRedstone(pos)
        }
    }

    @JvmStatic
    fun indicateSuccess(scene: SceneBuilder, pos: BlockPos?) {
        if (pos != null) {
            scene.effects().indicateSuccess(pos)
        }
    }

    @JvmStatic
    fun clearEntities(
        scene: SceneBuilder,
        fullScene: Boolean,
        entityId: String?,
        pos1: BlockPos?,
        pos2: BlockPos?
    ) {
        clearEntities(scene, null, fullScene, entityId, null, pos1, pos2, null, null, null)
    }

    @JvmStatic
    fun clearEntities(
        scene: SceneBuilder,
        context: Context?,
        fullScene: Boolean,
        entityId: String?,
        linkId: String?,
        pos1: BlockPos?,
        pos2: BlockPos?
    ) {
        clearEntities(scene, context, fullScene, entityId, linkId, pos1, pos2, null, null, null)
    }

    @JvmStatic
    fun clearEntities(
        scene: SceneBuilder,
        context: Context?,
        fullScene: Boolean,
        entityId: String?,
        linkId: String?,
        pos1: BlockPos?,
        pos2: BlockPos?,
        exitAnimation: String?,
        exitDuration: Int?,
        direction: String?
    ) {
        val filter = tryParseEntityFilter(entityId)
        val selection = selectionFromBounds(scene, pos1, pos2)
        val normalizedLinkId = normalizeEntityLinkId(linkId)
        if (normalizedLinkId != null) {
            scheduleEntityClear(
                scene,
                linkedEntityTargets(context, normalizedLinkId),
                selection,
                filter,
                exitAnimation,
                exitDuration,
                direction
            )
            return
        }
        if (fullScene) {
            scheduleEntityClear(scene, null, null, filter, exitAnimation, exitDuration, direction)
            return
        }
        if (selection == null) {
            return
        }
        scheduleEntityClear(scene, null, selection, filter, exitAnimation, exitDuration, direction)
    }

    @JvmStatic
    fun clearItemEntities(
        scene: SceneBuilder,
        fullScene: Boolean,
        itemId: String?,
        pos1: BlockPos?,
        pos2: BlockPos?
    ) {
        clearItemEntities(scene, null, fullScene, itemId, null, pos1, pos2)
    }

    @JvmStatic
    fun clearItemEntities(
        scene: SceneBuilder,
        context: Context?,
        fullScene: Boolean,
        itemId: String?,
        linkId: String?,
        pos1: BlockPos?,
        pos2: BlockPos?
    ) {
        val filter = tryParseEntityFilter(itemId)
        val selection = selectionFromBounds(scene, pos1, pos2)
        val normalizedLinkId = normalizeEntityLinkId(linkId)
        if (normalizedLinkId != null) {
            applyToLinkedEntities(
                scene,
                linkedEntityTargets(context, normalizedLinkId),
                selection,
                { matchesItemEntity(it, filter) },
                Entity::discard
            )
            return
        }
        if (fullScene) {
            scene.world().modifyEntities(ItemEntity::class.java) { entity ->
                if (matchesItemEntity(entity, filter)) {
                    entity.discard()
                }
            }
            return
        }
        if (selection == null) {
            return
        }
        scene.world().modifyEntitiesInside(ItemEntity::class.java, selection) { entity ->
            if (matchesItemEntity(entity, filter)) {
                entity.discard()
            }
        }
    }

    @JvmStatic
    fun modifyEntitiesNbt(
        scene: SceneBuilder,
        fullScene: Boolean,
        entityId: String?,
        nbt: String?,
        pos1: BlockPos?,
        pos2: BlockPos?
    ) {
        modifyEntitiesNbt(
            scene,
            null,
            fullScene,
            entityId,
            null,
            nbt,
            pos1,
            pos2,
            null,
            null,
            null
        )
    }

    @JvmStatic
    fun modifyEntitiesNbt(
        scene: SceneBuilder,
        context: Context?,
        fullScene: Boolean,
        entityId: String?,
        linkId: String?,
        nbt: String?,
        pos1: BlockPos?,
        pos2: BlockPos?,
        move: Vec3?,
        moveDuration: Int?,
        walkAnimation: Boolean?
    ) {
        val patch = parseEntityPatch(nbt)
        val hasMove = move != null && move.lengthSqr() > 0.0
        if (patch == null && !hasMove) {
            return
        }
        val filter = tryParseEntityFilter(entityId)
        val selection = selectionFromBounds(scene, pos1, pos2)
        val normalizedLinkId = normalizeEntityLinkId(linkId)
        if (normalizedLinkId != null) {
            val links = linkedEntityTargets(context, normalizedLinkId)
            if (patch != null) {
                applyToLinkedEntities(
                    scene,
                    links,
                    selection,
                    { matchesNonItemEntity(it, filter) },
                    { mergeEntityNbt(it, patch) }
                )
            }
            scheduleEntityMove(scene, links, selection, filter, move, moveDuration, walkAnimation)
            return
        }
        if (fullScene) {
            if (patch != null) {
                scene.world().modifyEntities(Entity::class.java) { entity ->
                    if (matchesNonItemEntity(entity, filter)) {
                        mergeEntityNbt(entity, patch)
                    }
                }
            }
            scheduleEntityMove(scene, null, null, filter, move, moveDuration, walkAnimation)
            return
        }
        if (selection == null) {
            return
        }
        if (patch != null) {
            scene.world().modifyEntitiesInside(Entity::class.java, selection) { entity ->
                if (matchesNonItemEntity(entity, filter)) {
                    mergeEntityNbt(entity, patch)
                }
            }
        }
        scheduleEntityMove(scene, null, selection, filter, move, moveDuration, walkAnimation)
    }

    @JvmStatic
    fun modifyItemEntitiesNbt(
        scene: SceneBuilder,
        fullScene: Boolean,
        itemId: String?,
        nbt: String?,
        pos1: BlockPos?,
        pos2: BlockPos?
    ) {
        modifyItemEntitiesNbt(scene, null, fullScene, itemId, null, nbt, pos1, pos2)
    }

    @JvmStatic
    fun modifyItemEntitiesNbt(
        scene: SceneBuilder,
        context: Context?,
        fullScene: Boolean,
        itemId: String?,
        linkId: String?,
        nbt: String?,
        pos1: BlockPos?,
        pos2: BlockPos?
    ) {
        val patch = parseEntityPatch(nbt) ?: return
        val filter = tryParseEntityFilter(itemId)
        val selection = selectionFromBounds(scene, pos1, pos2)
        val normalizedLinkId = normalizeEntityLinkId(linkId)
        val itemPatchAction: (Entity) -> Unit = { entity ->
            val itemEntity = entity as ItemEntity
            itemEntity.item = applyPatchToItemStack(itemEntity.item, patch)
            if (isLikelyEntityPatch(patch)) {
                mergeEntityNbt(itemEntity, patch)
            }
        }
        if (normalizedLinkId != null) {
            applyToLinkedEntities(
                scene,
                linkedEntityTargets(context, normalizedLinkId),
                selection,
                { matchesItemEntity(it, filter) },
                itemPatchAction
            )
            return
        }
        if (fullScene) {
            scene.world().modifyEntities(ItemEntity::class.java) { entity ->
                if (matchesItemEntity(entity, filter)) {
                    entity.item = applyPatchToItemStack(entity.item, patch)
                    if (isLikelyEntityPatch(patch)) {
                        mergeEntityNbt(entity, patch)
                    }
                }
            }
            return
        }
        if (selection == null) {
            return
        }
        scene.world().modifyEntitiesInside(ItemEntity::class.java, selection) { entity ->
            if (matchesItemEntity(entity, filter)) {
                entity.item = applyPatchToItemStack(entity.item, patch)
                if (isLikelyEntityPatch(patch)) {
                    mergeEntityNbt(entity, patch)
                }
            }
        }
    }

    private fun tryParseEntityFilter(raw: String?): ResourceLocation? {
        if (raw.isNullOrBlank()) {
            return null
        }
        return ResourceLocation.tryParse(raw)
    }

    private fun normalizeEntityLinkId(rawLinkId: String?): String? {
        if (rawLinkId == null) {
            return null
        }
        val normalized = rawLinkId.trim()
        return normalized.ifEmpty { null }
    }

    private fun registerEntityLink(
        context: Context?,
        rawLinkId: String?,
        link: ElementLink<EntityElement>
    ) {
        if (context == null) {
            return
        }
        val linkId = normalizeEntityLinkId(rawLinkId) ?: return
        context.entityLinks.getOrPut(linkId) { ArrayList() }.add(link)
    }

    private fun linkedEntityTargets(
        context: Context?,
        linkId: String?
    ): List<ElementLink<EntityElement>> {
        if (context == null) {
            return emptyList()
        }
        return context.entityLinks[linkId]?.toList() ?: emptyList()
    }

    private fun applyToLinkedEntities(
        scene: SceneBuilder,
        links: List<ElementLink<EntityElement>>,
        selection: Selection?,
        filter: (Entity) -> Boolean,
        action: (Entity) -> Unit
    ) {
        if (links.isEmpty()) {
            return
        }
        val captured = links.toList()
        scene.addInstruction { ponderScene ->
            for (link in captured) {
                val element = ponderScene.resolve(link) ?: continue
                element.ifPresent { entity ->
                    if (entity != null &&
                        entity.isAlive &&
                        (selection == null || selection.test(entity.blockPosition())) &&
                        filter(entity)
                    ) {
                        action(entity)
                    }
                }
            }
        }
    }

    private fun matchesNonItemEntity(
        entity: Entity,
        filterLoc: ResourceLocation?
    ): Boolean {
        return entity !is ItemEntity && matchesEntityType(entity, filterLoc)
    }

    private fun matchesEntityType(entity: Entity, filterLoc: ResourceLocation?): Boolean {
        return filterLoc == null || EntityType.getKey(entity.type) == filterLoc
    }

    private fun matchesItemEntity(entity: Entity, filterLoc: ResourceLocation?): Boolean {
        val itemEntity = entity as? ItemEntity ?: return false
        return filterLoc == null ||
                BuiltInRegistries.ITEM.getKey(itemEntity.item.item) == filterLoc
    }

    private fun selectionFromBounds(
        scene: SceneBuilder,
        pos1: BlockPos?,
        pos2: BlockPos?
    ): Selection? {
        if (pos1 == null) {
            return null
        }
        val targetPos2 = pos2 ?: pos1
        return scene.scene.sceneBuildingUtil.select().fromTo(pos1, targetPos2)
    }

    private fun scheduleEntityMove(
        scene: SceneBuilder,
        linkedTargets: List<ElementLink<EntityElement>>?,
        selection: Selection?,
        filterLoc: ResourceLocation?,
        offset: Vec3?,
        duration: Int?,
        walkAnimation: Boolean?
    ) {
        if (offset == null || offset.lengthSqr() == 0.0) {
            return
        }
        val moveDuration = (duration ?: 20).coerceAtLeast(0)
        if (moveDuration <= 0) {
            applyInstantEntityMove(scene, linkedTargets, selection, filterLoc, offset)
            return
        }
        scene.addInstruction(
            EntityMoveInstruction(
                linkedTargets,
                selection,
                filterLoc,
                offset,
                walkAnimation == true,
                moveDuration
            )
        )
    }

    private fun applyInstantEntityMove(
        scene: SceneBuilder,
        linkedTargets: List<ElementLink<EntityElement>>?,
        selection: Selection?,
        filterLoc: ResourceLocation?,
        offset: Vec3
    ) {
        if (linkedTargets != null) {
            applyToLinkedEntities(
                scene,
                linkedTargets,
                selection,
                { matchesNonItemEntity(it, filterLoc) },
                { moveEntityInstant(it, offset) }
            )
            return
        }
        scene.addInstruction { ponderScene ->
            ponderScene.forEachWorldEntity(Entity::class.java, Consumer { entity ->
                if (!matchesNonItemEntity(entity, filterLoc)) {
                    return@Consumer
                }
                if (selection != null && !selection.test(entity.blockPosition())) {
                    return@Consumer
                }
                moveEntityInstant(entity, offset)
            })
        }
    }

    private fun moveEntityInstant(entity: Entity, offset: Vec3) {
        if (!entity.isAlive) {
            return
        }
        val targetPos = entity.position().add(offset)
        entity.setPos(targetPos.x, targetPos.y, targetPos.z)
        entity.deltaMovement = Vec3.ZERO
        entity.setOldPosAndRot()
        stopWalkAnimation(entity)
    }

    private fun scheduleEntityClear(
        scene: SceneBuilder,
        linkedTargets: List<ElementLink<EntityElement>>?,
        selection: Selection?,
        filterLoc: ResourceLocation?,
        exitAnimation: String?,
        exitDuration: Int?,
        direction: String?
    ) {
        val exitOffset = entityExitOffset(exitAnimation, direction)
        val resolvedDuration = (exitDuration ?: 20).coerceAtLeast(0)
        if (exitOffset == null || resolvedDuration <= 0) {
            applyInstantEntityClear(scene, linkedTargets, selection, filterLoc)
            return
        }
        scene.addInstruction(
            EntityExitInstruction(
                linkedTargets,
                selection,
                filterLoc,
                exitOffset,
                resolvedDuration
            )
        )
    }

    private fun applyInstantEntityClear(
        scene: SceneBuilder,
        linkedTargets: List<ElementLink<EntityElement>>?,
        selection: Selection?,
        filterLoc: ResourceLocation?
    ) {
        if (linkedTargets != null) {
            applyToLinkedEntities(
                scene,
                linkedTargets,
                selection,
                { matchesNonItemEntity(it, filterLoc) },
                Entity::discard
            )
            return
        }
        if (selection == null) {
            scene.world().modifyEntities(Entity::class.java) { entity ->
                if (matchesNonItemEntity(entity, filterLoc)) {
                    entity.discard()
                }
            }
            return
        }
        scene.world().modifyEntitiesInside(Entity::class.java, selection) { entity ->
            if (matchesNonItemEntity(entity, filterLoc)) {
                entity.discard()
            }
        }
    }

    private fun entityEntranceOffset(
        entranceAnimationRaw: String?,
        directionRaw: String?
    ): Vec3? {
        val animation = normalizeEntranceAnimation(entranceAnimationRaw)
        if (animation == null || animation == "none") {
            return null
        }
        val direction = if (animation == "simultaneous") {
            parseDirection(directionRaw)
        } else {
            parseDirection(animation)
        }
        return Vec3.atLowerCornerOf(direction.normal).scale(-0.5)
    }

    private fun entityExitOffset(
        exitAnimationRaw: String?,
        directionRaw: String?
    ): Vec3? {
        val animation = normalizeEntranceAnimation(exitAnimationRaw)
        if (animation == null || animation == "none") {
            return null
        }
        val direction = if (animation == "simultaneous") {
            parseDirection(directionRaw)
        } else {
            parseDirection(animation)
        }
        return Vec3.atLowerCornerOf(direction.normal).scale(0.5)
    }

    private fun computeWalkAnimationSpeed(totalOffset: Vec3, durationTicks: Int): Float {
        if (durationTicks <= 0) {
            return 0.0f
        }
        val seconds = durationTicks / 20.0
        val blocksPerSecond = totalOffset.length() / seconds
        return minOf(1.0, blocksPerSecond / 5.0).toFloat()
    }

    private fun applyWalkAnimation(entity: Entity, speed: Float) {
        val livingEntity = entity as? LivingEntity ?: return
        val clampedSpeed = speed.coerceIn(0.0f, 1.0f)
        livingEntity.walkAnimation.update(clampedSpeed, 0.4f)
    }

    private fun stopWalkAnimation(entity: Entity) {
        val livingEntity = entity as? LivingEntity ?: return
        livingEntity.walkAnimation.update(0.0f, 1.0f)
        if (livingEntity is Mob) {
            livingEntity.setXxa(0.0f)
            livingEntity.setYya(0.0f)
            livingEntity.setZza(0.0f)
        }
    }

    private fun parseEntityPatch(nbt: String?): CompoundTag? {
        if (nbt.isNullOrBlank()) {
            return null
        }
        return runCatching { TagParser.parseTag(nbt) }.getOrNull()
    }

    private fun applyAnimatedSetBlock(
        scene: SceneBuilder,
        context: Context,
        state: BlockState,
        pos1: BlockPos,
        pos2: BlockPos,
        nbt: String?,
        entranceAnimation: String,
        entranceDuration: Int?,
        entranceInterval: Int?,
        smartDisplay: Boolean?,
        linkId: String?,
        direction: String?
    ) {
        ensureSceneCanShowRange(scene, pos1, pos2, false)
        val selection = scene.scene.sceneBuildingUtil.select().fromTo(pos1, pos2)
        scene.world().setBlocks(selection, state, false)
        applySetBlockNbtPatch(scene, nbt, selection)
        var key = linkId?.trim() ?: ""
        if (key.isEmpty()) {
            key = autoLinkId(context)
        }
        val entryDirection = parseDirection(direction)
        val existing = context.sectionLinks[key]
        val rowDuration = (entranceDuration ?: 20).coerceAtLeast(0)
        val rowInterval = (entranceInterval ?: 1).coerceAtLeast(0)
        applyAnimatedShowSectionAndMerge(
            scene,
            context,
            key,
            existing,
            pos1,
            pos2,
            entranceAnimation,
            entryDirection,
            rowDuration,
            rowInterval,
            smartDisplay != false
        )
        updateVisibleRange(context, pos1, pos2, true)
    }

    private fun applySetBlockNbtPatch(scene: SceneBuilder, nbt: String?, selection: Selection) {
        if (nbt.isNullOrBlank()) {
            return
        }
        runCatching {
            val patch = TagParser.parseTag(nbt)
            scene.world().modifyBlockEntityNBT(
                selection,
                BlockEntity::class.java,
                { it.merge(patch.copy()) },
                true
            )
        }
    }

    private fun ensureSceneCanShowRange(
        scene: SceneBuilder,
        pos1: BlockPos,
        pos2: BlockPos,
        forceVisibleNow: Boolean
    ) {
        val selection = scene.scene.sceneBuildingUtil.select().fromTo(pos1, pos2)
        val minPos = BlockPos(
            minOf(pos1.x, pos2.x),
            minOf(pos1.y, pos2.y),
            minOf(pos1.z, pos2.z)
        )
        val maxPos = BlockPos(
            maxOf(pos1.x, pos2.x),
            maxOf(pos1.y, pos2.y),
            maxOf(pos1.z, pos2.z)
        )
        scene.addInstruction { ps ->
            ps.world.getBounds().encapsulate(minPos)
            ps.world.getBounds().encapsulate(maxPos)
            if (!forceVisibleNow) {
                if (!ps.baseWorldSection.isEmpty) {
                    ps.baseWorldSection.erase(selection)
                    ps.baseWorldSection.queueRedraw()
                }
                return@addInstruction
            }
            if (ps.baseWorldSection.isEmpty) {
                val all = ps.sceneBuildingUtil.select().everywhere()
                ps.baseWorldSection.set(all)
                ps.baseWorldSection.isVisible = true
                ps.baseWorldSection.setFade(1f)
            } else {
                ps.baseWorldSection.add(selection)
            }
            ps.baseWorldSection.queueRedraw()
        }
    }

    private fun resolveSectionLink(
        scene: SceneBuilder,
        context: Context,
        linkId: String?,
        pos1: BlockPos?,
        pos2: BlockPos?
    ): ElementLink<WorldSectionElement>? {
        val normalized = linkId?.trim() ?: ""
        if (normalized.isNotEmpty()) {
            val existing = context.sectionLinks[normalized]
            if (existing != null) {
                return existing
            }
        }
        if (pos1 == null) {
            return null
        }
        val targetPos2 = pos2 ?: pos1
        val selection = scene.scene.sceneBuildingUtil.select().fromTo(pos1, targetPos2)
        val created = scene.world().showIndependentSectionImmediately(selection)
        val key = normalized.ifEmpty { autoLinkId(context) }
        context.sectionLinks[key] = created
        updateVisibleRange(context, pos1, targetPos2, true)
        return created
    }

    private fun applyAnimatedShowSectionAndMerge(
        scene: SceneBuilder,
        context: Context,
        linkId: String,
        existing: ElementLink<WorldSectionElement>?,
        pos1: BlockPos,
        pos2: BlockPos,
        entranceAnimation: String,
        entryDirection: Direction,
        rowDuration: Int,
        rowInterval: Int,
        smartDisplay: Boolean
    ) {
        var groups = orderedLayerGroups(pos1, pos2, entranceAnimation)
        if (smartDisplay) {
            groups = filterVisibleGroups(groups, context)
        }
        if (groups.isEmpty()) {
            return
        }
        val otherSectionLinks = snapshotOtherSectionLinks(context, existing)
        val groupSelections = prepareAnimatedRevealSelections(
            scene,
            existing,
            groups,
            otherSectionLinks
        )
        if (groupSelections.isEmpty()) {
            return
        }
        var working = existing
        for (groupSelection in groupSelections) {
            if (working == null) {
                val instruction = DisplayWorldSectionInstruction(
                    rowDuration,
                    entryDirection,
                    groupSelection,
                    null
                )
                scene.addInstruction(instruction)
                working = instruction.createLink(scene.scene)
                context.sectionLinks[linkId] = working
            } else {
                val target = working
                scene.addInstruction(
                    DisplayWorldSectionInstruction(
                        rowDuration,
                        entryDirection,
                        groupSelection
                    ) {
                        scene.scene.resolve(target)
                    }
                )
            }
            scene.idle(rowInterval)
        }
    }

    private fun prepareAnimatedRevealSelections(
        scene: SceneBuilder,
        existing: ElementLink<WorldSectionElement>?,
        groups: List<List<BlockPos>>,
        otherSectionLinks: List<ElementLink<WorldSectionElement>>
    ): List<Selection> {
        val selections = ArrayList<Selection>()
        var minX = Int.MAX_VALUE
        var minY = Int.MAX_VALUE
        var minZ = Int.MAX_VALUE
        var maxX = Int.MIN_VALUE
        var maxY = Int.MIN_VALUE
        var maxZ = Int.MIN_VALUE
        for (group in groups) {
            if (group.isEmpty()) {
                continue
            }
            selections.add(selectionForGroup(scene, group))
            for (pos in group) {
                minX = minOf(minX, pos.x)
                minY = minOf(minY, pos.y)
                minZ = minOf(minZ, pos.z)
                maxX = maxOf(maxX, pos.x)
                maxY = maxOf(maxY, pos.y)
                maxZ = maxOf(maxZ, pos.z)
            }
        }
        if (selections.isEmpty()) {
            return selections
        }
        val minPos = BlockPos(minX, minY, minZ)
        val maxPos = BlockPos(maxX, maxY, maxZ)
        val revealSelections = selections.toList()
        val priorSections = otherSectionLinks.toList()
        scene.addInstruction { ps ->
            ps.world.getBounds().encapsulate(minPos)
            ps.world.getBounds().encapsulate(maxPos)
            if (!ps.baseWorldSection.isEmpty) {
                for (revealSelection in revealSelections) {
                    ps.baseWorldSection.erase(revealSelection)
                }
                ps.baseWorldSection.queueRedraw()
            }
            if (existing != null) {
                val element = ps.resolve(existing)
                if (element != null) {
                    for (revealSelection in revealSelections) {
                        element.erase(revealSelection)
                    }
                    element.queueRedraw()
                }
            }
            for (link in priorSections) {
                val element = ps.resolve(link) ?: continue
                for (revealSelection in revealSelections) {
                    element.erase(revealSelection)
                }
                element.queueRedraw()
            }
        }
        return selections
    }

    private fun selectionForGroup(scene: SceneBuilder, group: List<BlockPos>): Selection {
        var selection = scene.scene.sceneBuildingUtil.select().position(group[0])
        for (i in 1 until group.size) {
            selection = selection.add(
                scene.scene.sceneBuildingUtil.select().position(group[i])
            )
        }
        return selection
    }

    private fun orderedLayerGroups(
        pos1: BlockPos,
        pos2: BlockPos,
        entranceAnimation: String?
    ): List<List<BlockPos>> {
        val minX = minOf(pos1.x, pos2.x)
        val minY = minOf(pos1.y, pos2.y)
        val minZ = minOf(pos1.z, pos2.z)
        val maxX = maxOf(pos1.x, pos2.x)
        val maxY = maxOf(pos1.y, pos2.y)
        val maxZ = maxOf(pos1.z, pos2.z)

        val positions = ArrayList<BlockPos>()
        for (y in minY..maxY) {
            for (z in minZ..maxZ) {
                for (x in minX..maxX) {
                    positions.add(BlockPos(x, y, z))
                }
            }
        }

        val tieBreaker = Comparator.comparingInt<BlockPos> { it.y }
            .thenComparingInt { it.z }
            .thenComparingInt { it.x }
        val comparator: Comparator<BlockPos> = when (entranceAnimation) {
            "down" -> Comparator.comparingInt<BlockPos> { it.y }.reversed().thenComparing(tieBreaker)
            "up" -> Comparator.comparingInt<BlockPos> { it.y }.thenComparing(tieBreaker)
            "south" -> Comparator.comparingInt<BlockPos> { it.z }.thenComparing(tieBreaker)
            "north" -> Comparator.comparingInt<BlockPos> { it.z }.reversed().thenComparing(tieBreaker)
            "east" -> Comparator.comparingInt<BlockPos> { it.x }.thenComparing(tieBreaker)
            "west" -> Comparator.comparingInt<BlockPos> { it.x }.reversed().thenComparing(tieBreaker)
            else -> tieBreaker
        }
        positions.sortWith(comparator)

        val layerKey: (BlockPos) -> Int = when (entranceAnimation) {
            "simultaneous" -> { _: BlockPos -> 0 }
            "south", "north" -> { pos: BlockPos -> pos.z }
            "east", "west" -> { pos: BlockPos -> pos.x }
            else -> { pos: BlockPos -> pos.y }
        }

        val groups = ArrayList<List<BlockPos>>()
        var currentGroup: MutableList<BlockPos>? = null
        var currentKey = Int.MIN_VALUE
        for (pos in positions) {
            val key = layerKey(pos)
            if (currentGroup == null || key != currentKey) {
                currentGroup = ArrayList()
                groups.add(currentGroup)
                currentKey = key
            }
            currentGroup.add(pos)
        }
        return groups
    }

    private fun filterVisibleGroups(
        groups: List<List<BlockPos>>,
        context: Context
    ): List<List<BlockPos>> {
        val filtered = ArrayList<List<BlockPos>>()
        for (group in groups) {
            val pending = ArrayList<BlockPos>()
            for (pos in group) {
                if (!isBlockVisible(context, pos.asLong())) {
                    pending.add(pos)
                }
            }
            if (pending.isNotEmpty()) {
                filtered.add(pending)
            }
        }
        return filtered
    }

    private fun updateVisibleRange(
        context: Context,
        pos1: BlockPos?,
        pos2: BlockPos?,
        visible: Boolean
    ) {
        if (pos1 == null) {
            return
        }
        val targetPos2 = pos2 ?: pos1
        val minX = minOf(pos1.x, targetPos2.x)
        val minY = minOf(pos1.y, targetPos2.y)
        val minZ = minOf(pos1.z, targetPos2.z)
        val maxX = maxOf(pos1.x, targetPos2.x)
        val maxY = maxOf(pos1.y, targetPos2.y)
        val maxZ = maxOf(pos1.z, targetPos2.z)
        for (y in minY..maxY) {
            for (z in minZ..maxZ) {
                for (x in minX..maxX) {
                    val key = BlockPos.asLong(x, y, z)
                    if (context.allBlocksVisible) {
                        if (visible) {
                            context.hiddenBlockKeys.remove(key)
                        } else {
                            context.hiddenBlockKeys.add(key)
                        }
                    } else {
                        if (visible) {
                            context.visibleBlockKeys.add(key)
                        } else {
                            context.visibleBlockKeys.remove(key)
                        }
                    }
                }
            }
        }
    }

    private fun isBlockVisible(context: Context, key: Long): Boolean {
        return if (context.allBlocksVisible) {
            key !in context.hiddenBlockKeys
        } else {
            key in context.visibleBlockKeys
        }
    }

    private fun autoLinkId(context: Context): String {
        return "section_" + (context.sectionLinks.size + 1)
    }

    private fun snapshotOtherSectionLinks(
        context: Context,
        exclude: ElementLink<WorldSectionElement>?
    ): List<ElementLink<WorldSectionElement>> {
        return context.sectionLinks.values.filterTo(ArrayList()) { it !== exclude }
    }

    private fun eraseSelectionFromOtherSections(
        scene: SceneBuilder,
        context: Context,
        exclude: ElementLink<WorldSectionElement>?,
        selection: Selection
    ) {
        val others = snapshotOtherSectionLinks(context, exclude)
        if (others.isEmpty()) {
            return
        }
        val snap = others.toList()
        scene.addInstruction { ps ->
            for (link in snap) {
                val element = ps.resolve(link) ?: continue
                element.erase(selection)
                element.queueRedraw()
            }
        }
    }

    private fun normalizeEntranceAnimation(raw: String?): String? {
        if (raw.isNullOrBlank()) {
            return null
        }
        return when (raw.trim().lowercase(Locale.ROOT)) {
            "none", "(无)", "无" -> "none"
            "simultaneous", "同时" -> "simultaneous"
            "down", "从上到下", "上到下", "top_to_bottom", "top-down" -> "down"
            "up", "从下到上", "下到上", "bottom_to_top", "bottom-up" -> "up"
            "south", "从北到南", "北到南", "north_to_south", "north-south" -> "south"
            "north", "从南到北", "南到北", "south_to_north", "south-north" -> "north"
            "east", "从西到东", "西到东", "west_to_east", "west-east" -> "east"
            "west", "从东到西", "东到西", "east_to_west", "east-west" -> "west"
            else -> null
        }
    }

    private fun parseDirection(raw: String?): Direction {
        if (raw.isNullOrBlank()) {
            return Direction.DOWN
        }
        return when (raw.lowercase(Locale.ROOT)) {
            "up" -> Direction.UP
            "north" -> Direction.NORTH
            "south" -> Direction.SOUTH
            "west" -> Direction.WEST
            "east" -> Direction.EAST
            else -> Direction.DOWN
        }
    }

    private fun parsePointing(raw: String?): Pointing {
        if (raw.isNullOrBlank()) {
            return Pointing.DOWN
        }
        return when (raw.lowercase(Locale.ROOT)) {
            "up" -> Pointing.UP
            "left" -> Pointing.LEFT
            "right" -> Pointing.RIGHT
            else -> Pointing.DOWN
        }
    }

    private fun parseSoundSource(raw: String?): SoundSource {
        if (raw.isNullOrBlank()) {
            return SoundSource.MASTER
        }
        return runCatching {
            SoundSource.valueOf(raw.uppercase(Locale.ROOT))
        }.getOrDefault(SoundSource.MASTER)
    }

    private fun parsePalette(raw: String?): PonderPalette? {
        if (raw.isNullOrBlank()) {
            return null
        }
        return when (raw.trim().lowercase(Locale.ROOT)) {
            "white" -> PonderPalette.WHITE
            "black" -> PonderPalette.BLACK
            "red" -> PonderPalette.RED
            "green" -> PonderPalette.GREEN
            "blue" -> PonderPalette.BLUE
            "input" -> PonderPalette.INPUT
            "output" -> PonderPalette.OUTPUT
            "slow" -> PonderPalette.SLOW
            "medium" -> PonderPalette.MEDIUM
            "fast" -> PonderPalette.FAST
            else -> null
        }
    }

    private fun applyBlockProperties(
        state: BlockState,
        blockProperties: Map<String, String>?
    ): BlockState {
        if (blockProperties.isNullOrEmpty()) {
            return state
        }
        val definition = state.block.getStateDefinition()
        var result = state
        for ((key, value) in blockProperties) {
            val property = definition.getProperty(key)
            if (property != null) {
                @Suppress("UNCHECKED_CAST")
                val typedProperty = property as Property<Comparable<Any>>
                result = setPropertyValue(result, typedProperty, value)
            }
        }
        return result
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T : Comparable<T>> setPropertyValue(
        state: BlockState,
        property: Property<T>,
        value: String
    ): BlockState {
        return property.getValue(value)
            .map { state.setValue(property, it) }
            .orElseGet { state }
    }

    private fun parseItemStackSpec(raw: String?, nbtOverride: String?): ItemStack? {
        if (raw.isNullOrBlank()) {
            return null
        }
        val trimmed = raw.trim()
        var itemIdPart = trimmed
        var nbtPart: String? = null
        val brace = trimmed.indexOf('{')
        if (brace >= 0) {
            itemIdPart = trimmed.substring(0, brace).trim()
            nbtPart = trimmed.substring(brace).trim()
        }
        val itemLoc = ResourceLocation.tryParse(itemIdPart)
        val item = itemLoc?.let { BuiltInRegistries.ITEM.getOptional(it).orElse(null) }
        if (item == null) {
            return null
        }
        val stack = ItemStack(item)
        val finalNbt = if (nbtOverride.isNullOrBlank()) nbtPart else nbtOverride.trim()
        if (!finalNbt.isNullOrBlank() && finalNbt != "{}") {
            runCatching {
                val tag = TagParser.parseTag(finalNbt)
                if (!tag.isEmpty) {
                    stack.tag = tag
                }
            }
        }
        return stack
    }

    private fun applyPatchToItemStack(base: ItemStack, patch: CompoundTag): ItemStack {
        val copy = base.copy()
        var itemPatch = patch
        if (patch.contains("Item", Tag.TAG_COMPOUND.toInt())) {
            val entityItem = patch.getCompound("Item")
            itemPatch = if (entityItem.contains("tag", Tag.TAG_COMPOUND.toInt())) {
                entityItem.getCompound("tag")
            } else {
                CompoundTag()
            }
        }
        if (!itemPatch.isEmpty) {
            val tag = copy.tag
            tag?.merge(itemPatch.copy())
            copy.tag = tag
        }
        return copy
    }

    private fun isLikelyEntityPatch(patch: CompoundTag): Boolean {
        return patch.contains("Item", Tag.TAG_COMPOUND.toInt()) ||
                patch.contains("Age") ||
                patch.contains("PickupDelay") ||
                patch.contains("Health") ||
                patch.contains("Motion") ||
                patch.contains("Pos") ||
                patch.contains("Rotation") ||
                patch.contains("NoGravity") ||
                patch.contains("Glowing") ||
                patch.contains("Invulnerable") ||
                patch.contains("UUID") ||
                patch.contains("Tags")
    }

    private fun mergeEntityNbt(entity: Entity, patch: CompoundTag) {
        val data = CompoundTag()
        entity.saveWithoutId(data)
        data.merge(patch.copy())
        entity.load(data)
    }

    private val EXTRA_SKIPPED_BLOCKS = setOf(
        "minecraft:air",
        "minecraft:cave_air",
        "minecraft:void_air",
        "minecraft:structure_void"
    )
    private val EXTRA_POS_NBT_KEYS = setOf("x", "y", "z")

    private data class PlacedBlock(
        val pos: BlockPos,
        val state: BlockState,
        val nbt: CompoundTag?
    )

    @JvmStatic
    fun showExtraStructure(
        scene: SceneBuilder,
        context: Context,
        structureAssetId: ResourceLocation?,
        base: BlockPos?,
        rotationDegrees: Int,
        replaceAir: Boolean,
        immediateDisplayFlag: Boolean?,
        spawnParticlesFlag: Boolean?,
        entranceAnimation: String?,
        entranceDuration: Int?,
        entranceInterval: Int?,
        smartDisplayFlag: Boolean?,
        linkIdRaw: String?,
        directionRaw: String?
    ) {
        if (structureAssetId == null || base == null) {
            return
        }
        val root: CompoundTag
        try {
            val resourceOpt = mc.resourceManager.getResource(structureAssetId)
            if (resourceOpt.isEmpty) {
                LOGGER.warn("show_extra_structure resource not found: {}", structureAssetId)
                return
            }
            root = resourceOpt.get().open().use { input ->
                NbtIo.read(
                    DataInputStream(BufferedInputStream(GZIPInputStream(input))),
                    NbtAccounter.create(0x20000000L)
                )
            }
        } catch (e: Exception) {
            LOGGER.warn("show_extra_structure failed to read {}: {}", structureAssetId, e.message)
            return
        }

        val placed = planExtraStructure(root, base, rotationDegrees, !replaceAir)
        if (placed.isEmpty()) {
            return
        }

        var minX = Int.MAX_VALUE
        var minY = Int.MAX_VALUE
        var minZ = Int.MAX_VALUE
        var maxX = Int.MIN_VALUE
        var maxY = Int.MIN_VALUE
        var maxZ = Int.MIN_VALUE
        for (b in placed) {
            minX = minOf(minX, b.pos.x)
            minY = minOf(minY, b.pos.y)
            minZ = minOf(minZ, b.pos.z)
            maxX = maxOf(maxX, b.pos.x)
            maxY = maxOf(maxY, b.pos.y)
            maxZ = maxOf(maxZ, b.pos.z)
        }
        val minCorner = BlockPos(minX, minY, minZ)
        val maxCorner = BlockPos(maxX, maxY, maxZ)

        val anim = normalizeEntranceAnimation(entranceAnimation)
        val simultaneous = anim == "simultaneous"
        val directional = anim != null && anim != "none" && !simultaneous
        val animatedReveal = simultaneous || directional
        val placeVisible = !animatedReveal && immediateDisplayFlag != false
        val particles = placeVisible && spawnParticlesFlag != false

        val placedStrips = segmentExtraForAnimation(placed, "up")
        var revealGroups: List<List<BlockPos>> = placedStrips
        var revealInterval = 0
        var linkId = ""
        var direction = Direction.DOWN
        var rowDuration = 20
        if (animatedReveal) {
            linkId = linkIdRaw?.trim() ?: ""
            if (linkId.isEmpty()) {
                linkId = autoLinkId(context)
            }
            direction = parseDirection(directionRaw)
            rowDuration = (entranceDuration ?: 20).coerceAtLeast(0)
            val rowInterval = (entranceInterval ?: 1).coerceAtLeast(0)
            val smartDisplay = smartDisplayFlag != false

            if (simultaneous) {
                revealGroups = placedStrips
                revealInterval = 0
            } else {
                revealGroups = segmentExtraForAnimation(placed, anim)
                revealInterval = rowInterval
            }
            if (smartDisplay) {
                revealGroups = filterVisibleGroups(revealGroups, context)
            }
            ensureSceneCanShowExtra(scene, minCorner, maxCorner, revealGroups, false)
        } else {
            ensureSceneCanShowExtra(scene, minCorner, maxCorner, placedStrips, placeVisible)
        }

        for (b in placed) {
            scene.world().setBlock(b.pos, b.state, particles)
            if (b.nbt != null && !b.nbt.isEmpty) {
                val patch = b.nbt
                val sel = scene.scene.sceneBuildingUtil.select().position(b.pos)
                scene.world().modifyBlockEntityNBT(
                    sel,
                    BlockEntity::class.java,
                    { it.merge(patch.copy()) },
                    true
                )
            }
        }

        if (!animatedReveal) {
            applyExtraPlacedVisibility(context, placed, placeVisible)
            return
        }
        if (revealGroups.isEmpty()) {
            applyExtraPlacedVisibility(context, placed, true)
            return
        }

        var working = context.sectionLinks[linkId]
        for (group in revealGroups) {
            if (group.isEmpty()) {
                continue
            }
            val groupSelection = selectionForGroup(scene, group)
            if (working == null) {
                val inst = DisplayWorldSectionInstruction(
                    rowDuration,
                    direction,
                    groupSelection,
                    null
                )
                scene.addInstruction(inst)
                working = inst.createLink(scene.scene)
                context.sectionLinks[linkId] = working
            } else {
                val target = working
                scene.addInstruction(
                    DisplayWorldSectionInstruction(
                        rowDuration,
                        direction,
                        groupSelection
                    ) {
                        scene.scene.resolve(target)
                    }
                )
            }
            if (revealInterval > 0) {
                scene.idle(revealInterval)
            }
        }
        applyExtraPlacedVisibility(context, placed, true)
    }

    private fun ensureSceneCanShowExtra(
        scene: SceneBuilder,
        minCorner: BlockPos,
        maxCorner: BlockPos,
        placedGroups: List<List<BlockPos>>,
        forceVisibleNow: Boolean
    ) {
        val groupSelections = ArrayList<Selection>(placedGroups.size)
        for (group in placedGroups) {
            if (group.isNotEmpty()) {
                groupSelections.add(selectionForGroup(scene, group))
            }
        }
        val capturedSelections = groupSelections.toList()
        scene.addInstruction { ps ->
            ps.world.getBounds().encapsulate(minCorner)
            ps.world.getBounds().encapsulate(maxCorner)
            if (!forceVisibleNow) {
                if (!ps.baseWorldSection.isEmpty) {
                    for (selection in capturedSelections) {
                        ps.baseWorldSection.erase(selection)
                    }
                    ps.baseWorldSection.queueRedraw()
                }
                return@addInstruction
            }
            if (ps.baseWorldSection.isEmpty) {
                val all = ps.sceneBuildingUtil.select().everywhere()
                ps.baseWorldSection.set(all)
                ps.baseWorldSection.isVisible = true
                ps.baseWorldSection.setFade(1f)
            } else {
                for (selection in capturedSelections) {
                    ps.baseWorldSection.add(selection)
                }
            }
            ps.baseWorldSection.queueRedraw()
        }
    }

    private fun applyExtraPlacedVisibility(
        context: Context,
        placed: List<PlacedBlock>,
        visible: Boolean
    ) {
        for (b in placed) {
            val key = b.pos.asLong()
            val effective = visible && !b.state.isAir
            if (context.allBlocksVisible) {
                if (effective) {
                    context.hiddenBlockKeys.remove(key)
                } else {
                    context.hiddenBlockKeys.add(key)
                }
            } else {
                if (effective) {
                    context.visibleBlockKeys.add(key)
                } else {
                    context.visibleBlockKeys.remove(key)
                }
            }
        }
    }

    private fun toExtraRotation(degrees: Int): Rotation {
        val normalized = ((degrees % 360) + 360) % 360
        return when (normalized) {
            90 -> Rotation.CLOCKWISE_90
            180 -> Rotation.CLOCKWISE_180
            270 -> Rotation.COUNTERCLOCKWISE_90
            else -> Rotation.NONE
        }
    }

    private fun planExtraStructure(
        root: CompoundTag,
        base: BlockPos,
        rotationDegrees: Int,
        skipAir: Boolean
    ): List<PlacedBlock> {
        val rotation = toExtraRotation(rotationDegrees)
        val paletteTag = root.getList("palette", Tag.TAG_COMPOUND.toInt())
        val palette = arrayOfNulls<BlockState>(paletteTag.size)
        for (i in paletteTag.indices) {
            palette[i] = parsePaletteEntry(paletteTag.getCompound(i))
        }
        val blocks = root.getList("blocks", Tag.TAG_COMPOUND.toInt())

        val rotatedPositions = ArrayList<BlockPos>()
        val rotatedStates = ArrayList<BlockState>()
        val blockNbts = ArrayList<CompoundTag?>()

        if (skipAir) {
            for (i in blocks.indices) {
                val entry = blocks.getCompound(i)
                val src = readExtraEntryPos(entry) ?: continue
                val state = resolveExtraEntryState(entry, palette)
                if (state == null || isExtraSkippedBlock(state)) {
                    continue
                }
                rotatedPositions.add(src.rotate(rotation))
                rotatedStates.add(state.rotate(rotation))
                blockNbts.add(readExtraBlockEntityPatch(entry))
            }
        } else {
            val sizeTag = root.getList("size", Tag.TAG_INT.toInt())
            if (sizeTag.size < 3) {
                return emptyList()
            }
            val sizeX = sizeTag.getInt(0)
            val sizeY = sizeTag.getInt(1)
            val sizeZ = sizeTag.getInt(2)

            val entryByPos = HashMap<Long, CompoundTag>(blocks.size)
            for (i in blocks.indices) {
                val entry = blocks.getCompound(i)
                val p = readExtraEntryPos(entry) ?: continue
                entryByPos[p.asLong()] = entry
            }

            val airState = Blocks.AIR.defaultBlockState()
            for (x in 0 until sizeX) {
                for (y in 0 until sizeY) {
                    for (z in 0 until sizeZ) {
                        val src = BlockPos(x, y, z)
                        val entry = entryByPos[src.asLong()]
                        var state: BlockState
                        var patch: CompoundTag? = null
                        if (entry == null) {
                            state = airState
                        } else {
                            val resolved = resolveExtraEntryState(entry, palette)
                            if (resolved == null || isExtraSkippedBlock(resolved)) {
                                state = airState
                            } else {
                                state = resolved
                                patch = readExtraBlockEntityPatch(entry)
                            }
                        }
                        rotatedPositions.add(src.rotate(rotation))
                        rotatedStates.add(state.rotate(rotation))
                        blockNbts.add(patch)
                    }
                }
            }
        }

        if (rotatedPositions.isEmpty()) {
            return emptyList()
        }
        var minX = Int.MAX_VALUE
        var minY = Int.MAX_VALUE
        var minZ = Int.MAX_VALUE
        for (p in rotatedPositions) {
            minX = minOf(minX, p.x)
            minY = minOf(minY, p.y)
            minZ = minOf(minZ, p.z)
        }
        val offsetX = base.x - minX
        val offsetY = base.y - minY
        val offsetZ = base.z - minZ
        val result = ArrayList<PlacedBlock>(rotatedPositions.size)
        for (i in rotatedPositions.indices) {
            val rp = rotatedPositions[i]
            result.add(
                PlacedBlock(
                    BlockPos(rp.x + offsetX, rp.y + offsetY, rp.z + offsetZ),
                    rotatedStates[i],
                    blockNbts[i]
                )
            )
        }
        return result
    }

    private fun readExtraEntryPos(entry: CompoundTag): BlockPos? {
        val pos = entry.getList("pos", Tag.TAG_INT.toInt())
        if (pos.size < 3) {
            return null
        }
        return BlockPos(pos.getInt(0), pos.getInt(1), pos.getInt(2))
    }

    private fun resolveExtraEntryState(
        entry: CompoundTag,
        palette: Array<BlockState?>
    ): BlockState? {
        val stateIdx = entry.getInt("state")
        if (stateIdx < 0 || stateIdx >= palette.size) {
            return null
        }
        return palette[stateIdx]
    }

    private fun isExtraSkippedBlock(state: BlockState): Boolean {
        val key = BuiltInRegistries.BLOCK.getKey(state.block)
        return key.toString() in EXTRA_SKIPPED_BLOCKS
    }

    private fun readExtraBlockEntityPatch(entry: CompoundTag): CompoundTag? {
        if (!entry.contains("nbt", Tag.TAG_COMPOUND.toInt())) {
            return null
        }
        val raw = entry.getCompound("nbt").copy()
        for (absoluteKey in EXTRA_POS_NBT_KEYS) {
            raw.remove(absoluteKey)
        }
        return raw.takeIf { !it.isEmpty }
    }

    private fun parsePaletteEntry(entry: CompoundTag): BlockState? {
        val name = entry.getString("Name")
        val id = ResourceLocation.tryParse(name)
        val block = id?.let { BuiltInRegistries.BLOCK.getOptional(it).orElse(null) }
        if (block == null) {
            return null
        }
        var state = block.defaultBlockState()
        if (entry.contains("Properties", Tag.TAG_COMPOUND.toInt())) {
            val props = entry.getCompound("Properties")
            val def = block.getStateDefinition()
            for (key in props.allKeys) {
                val prop = def.getProperty(key)
                if (prop != null) {
                    state = applyExtraProperty(state, prop, props.getString(key))
                }
            }
        }
        return state
    }

    @Suppress("UNCHECKED_CAST")
    private fun applyExtraProperty(
        state: BlockState,
        prop: Property<*>,
        value: String
    ): BlockState {
        val typed = prop as Property<Comparable<Any>>
        return typed.getValue(value)
            .map { state.setValue(typed, it) }
            .orElseGet { state }
    }

    private fun segmentExtraForAnimation(
        blocks: List<PlacedBlock>,
        anim: String?
    ): List<List<BlockPos>> {
        if (blocks.isEmpty()) {
            return emptyList()
        }
        val a = anim ?: ""
        val layerKey: (BlockPos) -> Int
        val rowKey: (BlockPos) -> Int
        val stripKey: (BlockPos) -> Int
        var reverseLayer: Boolean
        when (a) {
            "down" -> {
                layerKey = { it.y }
                rowKey = { it.z }
                stripKey = { it.x }
                reverseLayer = true
            }

            "up" -> {
                layerKey = { it.y }
                rowKey = { it.z }
                stripKey = { it.x }
                reverseLayer = false
            }

            "south" -> {
                layerKey = { it.z }
                rowKey = { it.y }
                stripKey = { it.x }
                reverseLayer = false
            }

            "north" -> {
                layerKey = { it.z }
                rowKey = { it.y }
                stripKey = { it.x }
                reverseLayer = true
            }

            "east" -> {
                layerKey = { it.x }
                rowKey = { it.y }
                stripKey = { it.z }
                reverseLayer = false
            }

            "west" -> {
                layerKey = { it.x }
                rowKey = { it.y }
                stripKey = { it.z }
                reverseLayer = true
            }

            "simultaneous" -> {
                layerKey = { 0 }
                rowKey = { it.y }
                stripKey = { it.x }
                reverseLayer = false
            }

            else -> {
                layerKey = { it.y }
                rowKey = { it.z }
                stripKey = { it.x }
                reverseLayer = false
            }
        }

        val positions = ArrayList<BlockPos>(blocks.size)
        for (b in blocks) {
            positions.add(b.pos)
        }
        val comparator: Comparator<BlockPos> = if (reverseLayer) {
            Comparator.comparingInt<BlockPos> { layerKey(it) }
                .reversed()
                .thenComparingInt { rowKey(it) }
                .thenComparingInt { stripKey(it) }
        } else {
            Comparator.comparingInt<BlockPos> { layerKey(it) }
                .thenComparingInt { rowKey(it) }
                .thenComparingInt { stripKey(it) }
        }
        positions.sortWith(comparator)

        val groups = ArrayList<List<BlockPos>>()
        var currentStrip: MutableList<BlockPos>? = null
        var curLayer = Int.MIN_VALUE
        var curRow = Int.MIN_VALUE
        var curStrip = Int.MIN_VALUE
        for (p in positions) {
            val lk = layerKey(p)
            val rk = rowKey(p)
            val sk = stripKey(p)
            val breakStrip = currentStrip == null ||
                    lk != curLayer ||
                    rk != curRow ||
                    sk != curStrip + 1
            if (breakStrip) {
                currentStrip = ArrayList()
                groups.add(currentStrip)
                curLayer = lk
                curRow = rk
            }
            currentStrip.add(p)
            curStrip = sk
        }
        return groups
    }
}

/**
 * Kotlin receiver used by [ponder].
 *
 * This wrapper keeps a scene/context pair together and lets a Ponder storyboard read like a
 * small DSL:
 *
 * ```
 * scene.ponder {
 *     preScanBounds(BlockPos(2, 1, 3), BlockPos(3, 2, 4))
 *     showStructure()
 *     setBlock("superbwarfare:vehicle_assembling_table", BlockPos(3, 1, 3))
 *     showText("Ready", Vec3(3.5, 2.0, 3.5))
 * }
 * ```
 */
class PonderSupport(
    val scene: SceneBuilder,
    val context: GeneratedPonderSupport.Context = GeneratedPonderSupport.Context()
) {
    fun idle(ticks: Int) = scene.idle(ticks)

    fun addKeyframe() = scene.addKeyframe()

    fun markAsFinished() = scene.markAsFinished()

    fun title(component: String, subtitle: String) = scene.title(component, subtitle)

    fun world(): WorldInstructions = scene.world()

    fun overlay(): OverlayInstructions = scene.overlay()

    fun effects(): EffectInstructions = scene.effects()

    fun select(): SelectionUtil = scene.scene.sceneBuildingUtil.select()

    fun grid(): PositionUtil = scene.scene.sceneBuildingUtil.grid()

    fun vector(): VectorUtil = scene.scene.sceneBuildingUtil.vector()

    fun configureBasePlate(x: Int, y: Int, size: Int) = scene.configureBasePlate(x, y, size)

    fun showBasePlate() = scene.showBasePlate()

    fun preScanBounds(minCorner: BlockPos?, maxCorner: BlockPos?) {
        GeneratedPonderSupport.preScanBounds(scene, minCorner, maxCorner)
    }

    fun showStructure(
        pos1: BlockPos? = null,
        pos2: BlockPos? = null,
        scale: Float? = null,
        rotation: Float? = null
    ) {
        GeneratedPonderSupport.showStructure(scene, context, pos1, pos2, scale, rotation)
    }

    fun showText(
        text: String?,
        point: Vec3?,
        duration: Int = 40,
        color: String? = null,
        placeNearTarget: Boolean = true
    ) {
        GeneratedPonderSupport.showText(scene, text, point, duration, color, placeNearTarget)
    }

    fun highlightSection(
        color: String?,
        pos1: BlockPos,
        pos2: BlockPos? = null,
        duration: Int = 20
    ) {
        GeneratedPonderSupport.highlightSection(scene, color, pos1, pos2, duration)
    }

    fun showControls(
        point: Vec3,
        direction: String? = "down",
        duration: Int = 30,
        action: String? = null,
        itemSpec: String? = null,
        nbt: String? = null,
        whileSneaking: Boolean = false,
        whileCtrl: Boolean = false
    ) {
        GeneratedPonderSupport.showControls(
            scene,
            point,
            direction,
            duration,
            action,
            itemSpec,
            nbt,
            whileSneaking,
            whileCtrl
        )
    }

    fun encapsulateBounds(size: BlockPos) {
        GeneratedPonderSupport.encapsulateBounds(scene, size)
    }

    fun playSound(
        soundId: String?,
        volume: Float = 1f,
        pitch: Float = 1f,
        source: String? = null
    ) {
        GeneratedPonderSupport.playSound(scene, soundId, volume, pitch, source)
    }

    fun setBlock(
        blockId: String,
        pos1: BlockPos,
        pos2: BlockPos? = null,
        blockProperties: Map<String, String>? = null,
        nbt: String? = null,
        immediateDisplay: Boolean? = null,
        spawnParticles: Boolean? = null,
        entranceAnimation: String? = null,
        entranceDuration: Int? = null,
        entranceInterval: Int? = null,
        smartDisplay: Boolean? = null,
        linkId: String? = null,
        direction: String? = null
    ) {
        GeneratedPonderSupport.setBlock(
            scene,
            context,
            blockId,
            blockProperties,
            pos1,
            pos2,
            nbt,
            immediateDisplay,
            spawnParticles,
            entranceAnimation,
            entranceDuration,
            entranceInterval,
            smartDisplay,
            linkId,
            direction
        )
    }

    fun replaceBlocks(
        blockId: String,
        pos1: BlockPos,
        pos2: BlockPos? = null,
        blockProperties: Map<String, String>? = null,
        spawnParticles: Boolean? = null
    ) {
        GeneratedPonderSupport.replaceBlocks(
            scene,
            context,
            blockId,
            blockProperties,
            pos1,
            pos2,
            spawnParticles
        )
    }

    fun destroyBlock(
        pos1: BlockPos,
        pos2: BlockPos? = null,
        destroyParticles: Boolean? = null
    ) {
        GeneratedPonderSupport.destroyBlock(scene, context, pos1, pos2, destroyParticles)
    }

    fun hideSection(
        pos1: BlockPos,
        pos2: BlockPos? = null,
        duration: Int = 15,
        directionRaw: String? = null
    ) {
        GeneratedPonderSupport.hideSection(scene, context, pos1, pos2, duration, directionRaw)
    }

    fun createEntity(
        entityId: String,
        pos: Vec3,
        lookAt: Vec3? = null,
        yaw: Float? = null,
        pitch: Float? = null,
        nbt: String? = null,
        linkId: String? = null,
        entranceAnimation: String? = null,
        entranceDuration: Int? = null,
        direction: String? = null
    ) {
        GeneratedPonderSupport.createEntity(
            scene,
            context,
            entityId,
            pos,
            lookAt,
            yaw,
            pitch,
            nbt,
            linkId,
            entranceAnimation,
            entranceDuration,
            direction
        )
    }

    fun createItemEntity(
        itemId: String,
        pos: Vec3,
        count: Int = 1,
        motion: Vec3 = Vec3.ZERO,
        nbt: String? = null,
        linkId: String? = null
    ) {
        GeneratedPonderSupport.createItemEntity(
            scene,
            context,
            itemId,
            count,
            pos,
            motion,
            nbt,
            linkId
        )
    }

    fun showSectionAndMerge(
        pos1: BlockPos,
        pos2: BlockPos? = null,
        linkId: String? = null,
        duration: Int = 15,
        directionRaw: String? = null,
        entranceAnimation: String? = null,
        entranceDuration: Int? = null,
        entranceInterval: Int? = null,
        smartDisplay: Boolean? = null
    ) {
        GeneratedPonderSupport.showSectionAndMerge(
            scene,
            context,
            pos1,
            pos2,
            linkId,
            duration,
            directionRaw,
            entranceAnimation,
            entranceDuration,
            entranceInterval,
            smartDisplay
        )
    }

    fun rotateSection(
        linkId: String,
        rotX: Double,
        rotY: Double,
        rotZ: Double,
        pos1: BlockPos? = null,
        pos2: BlockPos? = null,
        duration: Int = 15
    ) {
        GeneratedPonderSupport.rotateSection(
            scene,
            context,
            linkId,
            pos1,
            pos2,
            rotX,
            rotY,
            rotZ,
            duration
        )
    }

    fun moveSection(
        linkId: String,
        offset: Vec3,
        pos1: BlockPos? = null,
        pos2: BlockPos? = null,
        duration: Int = 15
    ) {
        GeneratedPonderSupport.moveSection(
            scene,
            context,
            linkId,
            pos1,
            pos2,
            offset,
            duration
        )
    }

    fun toggleRedstonePower(pos1: BlockPos, pos2: BlockPos? = null) {
        GeneratedPonderSupport.toggleRedstonePower(scene, pos1, pos2)
    }

    fun modifyBlockEntity(
        pos1: BlockPos,
        pos2: BlockPos? = null,
        blockProperties: Map<String, String>? = null,
        nbt: String? = null,
        redraw: Boolean? = null
    ) {
        GeneratedPonderSupport.modifyBlockEntity(
            scene,
            blockProperties,
            nbt,
            redraw,
            pos1,
            pos2
        )
    }

    fun indicateRedstone(pos: BlockPos) {
        GeneratedPonderSupport.indicateRedstone(scene, pos)
    }

    fun indicateSuccess(pos: BlockPos) {
        GeneratedPonderSupport.indicateSuccess(scene, pos)
    }

    fun clearEntities(
        fullScene: Boolean = false,
        entityId: String? = null,
        pos1: BlockPos? = null,
        pos2: BlockPos? = null,
        linkId: String? = null,
        exitAnimation: String? = null,
        exitDuration: Int? = null,
        direction: String? = null
    ) {
        GeneratedPonderSupport.clearEntities(
            scene,
            context,
            fullScene,
            entityId,
            linkId,
            pos1,
            pos2,
            exitAnimation,
            exitDuration,
            direction
        )
    }

    fun clearItemEntities(
        fullScene: Boolean = false,
        itemId: String? = null,
        pos1: BlockPos? = null,
        pos2: BlockPos? = null,
        linkId: String? = null
    ) {
        GeneratedPonderSupport.clearItemEntities(
            scene,
            context,
            fullScene,
            itemId,
            linkId,
            pos1,
            pos2
        )
    }

    fun modifyEntitiesNbt(
        fullScene: Boolean = false,
        entityId: String? = null,
        nbt: String? = null,
        pos1: BlockPos? = null,
        pos2: BlockPos? = null,
        linkId: String? = null,
        move: Vec3? = null,
        moveDuration: Int? = null,
        walkAnimation: Boolean? = null
    ) {
        GeneratedPonderSupport.modifyEntitiesNbt(
            scene,
            context,
            fullScene,
            entityId,
            linkId,
            nbt,
            pos1,
            pos2,
            move,
            moveDuration,
            walkAnimation
        )
    }

    fun modifyItemEntitiesNbt(
        fullScene: Boolean = false,
        itemId: String? = null,
        nbt: String? = null,
        pos1: BlockPos? = null,
        pos2: BlockPos? = null,
        linkId: String? = null
    ) {
        GeneratedPonderSupport.modifyItemEntitiesNbt(
            scene,
            context,
            fullScene,
            itemId,
            linkId,
            nbt,
            pos1,
            pos2
        )
    }

    fun showExtraStructure(
        structureAssetId: ResourceLocation,
        base: BlockPos,
        rotationDegrees: Int = 0,
        replaceAir: Boolean = false,
        immediateDisplayFlag: Boolean? = null,
        spawnParticlesFlag: Boolean? = null,
        entranceAnimation: String? = null,
        entranceDuration: Int? = null,
        entranceInterval: Int? = null,
        smartDisplayFlag: Boolean? = null,
        linkIdRaw: String? = null,
        directionRaw: String? = null
    ) {
        GeneratedPonderSupport.showExtraStructure(
            scene,
            context,
            structureAssetId,
            base,
            rotationDegrees,
            replaceAir,
            immediateDisplayFlag,
            spawnParticlesFlag,
            entranceAnimation,
            entranceDuration,
            entranceInterval,
            smartDisplayFlag,
            linkIdRaw,
            directionRaw
        )
    }

    fun rotateCameraY(degrees: Float, duration: Int, degreesX: Float = 0f) {
        GeneratedPonderSupport.rotateCameraY(scene, degrees, degreesX, duration)
    }
}

/**
 * Opens a Kotlin receiver DSL for Ponder storyboards.
 *
 * ```
 * scene.ponder {
 *     showStructure()
 *     setBlock("superbwarfare:vehicle_assembling_table", BlockPos(3, 1, 3))
 *     idle(20)
 * }
 * ```
 */
inline fun SceneBuilder.ponder(
    context: GeneratedPonderSupport.Context = GeneratedPonderSupport.Context(),
    block: PonderSupport.() -> Unit
): GeneratedPonderSupport.Context {
    val support = PonderSupport(this, context)
    support.block()
    return context
}
