package com.atsuishio.superbwarfare.entity.vehicle.remote

import com.atsuishio.superbwarfare.entity.vehicle.DroneEntity
import com.atsuishio.superbwarfare.entity.vehicle.base.RCEntity
import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity
import com.atsuishio.superbwarfare.entity.vehicle.utils.VehicleVecUtils
import com.atsuishio.superbwarfare.event.custom.LivingAttackCallback
import com.atsuishio.superbwarfare.init.ModEntities
import com.atsuishio.superbwarfare.item.misc.MonitorItem
import com.atsuishio.superbwarfare.tools.persistentData
import com.atsuishio.superbwarfare.world.saveddata.RemoteBindingData
import net.fabricmc.fabric.api.entity.FakePlayer
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.fabricmc.fabric.api.event.player.AttackEntityCallback
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents
import net.fabricmc.fabric.api.event.player.UseBlockCallback
import net.fabricmc.fabric.api.event.player.UseEntityCallback
import net.fabricmc.fabric.api.event.player.UseItemCallback
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.Tag
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.server.level.TicketType
import net.minecraft.tags.DamageTypeTags
import net.minecraft.world.InteractionResult
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.Pose
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.level.levelgen.Heightmap
import net.minecraft.world.phys.Vec3
import java.util.*

object RemoteControl {
    private const val RETURN_DATA = "SuperbWarfareRemoteReturn"
    // A region distance of 2 applies level 31 to the anchor chunk, so its entities tick.
    private const val ANCHOR_DISTANCE = 2
    private val anchorTicket = TicketType.create("superbwarfare_remote_anchor", Comparator.naturalOrder<UUID>())
    private val sessions = mutableMapOf<UUID, Session>()

    private class Session(
        val player: ServerPlayer,
        val vehicle: RCEntity,
        val body: RCOperatorBodyEntity,
        val carrier: VehicleEntity?,
        val seat: Int,
    ) {
        val anchorLevel = player.serverLevel()
        var anchor = player.position()
        val yaw = player.yRot
        val pitch = player.xRot
        var chunk = carrier?.chunkPosition() ?: ChunkPos(BlockPos.containing(anchor))
        var mounting = true
        var ending = false
    }

    fun register() {
        ServerEntityEvents.ENTITY_LOAD.register { entity, _ ->
            if (entity is DroneEntity) entity.initializeEnergy()
        }
        ServerEntityEvents.ENTITY_UNLOAD.register { entity, _ ->
            if (entity is RCEntity) entity.onUnloaded()
        }
        ServerTickEvents.END_SERVER_TICK.register(::onServerTick)
        ServerLifecycleEvents.SERVER_STOPPING.register { server ->
            sessions.values.filter { it.player.server === server }.forEach { endSession(it.player) }
        }
        ServerPlayConnectionEvents.JOIN.register { handler, _, server ->
            val player = handler.player
            restoreReturn(player)
            RemoteBindingData.get(server).syncTo(player)
        }
        ServerPlayConnectionEvents.DISCONNECT.register { handler, _ -> endSession(handler.player) }

        LivingAttackCallback.EVENT.register { entity, source, _ ->
            !isOperating(entity) || source.`is`(DamageTypeTags.BYPASSES_INVULNERABILITY)
        }
        AttackEntityCallback.EVENT.register { player, _, _, _, _ ->
            if (isOperating(player)) InteractionResult.FAIL else InteractionResult.PASS
        }
        UseBlockCallback.EVENT.register { player, _, _, _ ->
            if (isOperating(player)) InteractionResult.FAIL else InteractionResult.PASS
        }
        UseItemCallback.EVENT.register { player, _, hand ->
            val stack = player.getItemInHand(hand)
            if (isOperating(player) && stack.item !is MonitorItem) InteractionResultHolder.fail(stack)
            else InteractionResultHolder.pass(stack)
        }
        UseEntityCallback.EVENT.register { player, _, _, _, _ ->
            if (isOperating(player)) InteractionResult.FAIL else InteractionResult.PASS
        }
        PlayerBlockBreakEvents.BEFORE.register { _, player, _, _, _ -> !isOperating(player) }
    }

    @JvmStatic
    fun startSession(player: ServerPlayer, vehicle: RCEntity): Boolean {
        if (player is FakePlayer || !player.isAlive || player.isSpectator ||
            player.isSleeping || player.level() !== vehicle.level() ||
            player.containerMenu !== player.inventoryMenu || sessions.containsKey(player.uuid) ||
            sessions.values.any { it.vehicle === vehicle } ||
            player.persistentData.contains(RETURN_DATA) || !vehicle.canStartRemoteControl(player)
        ) return false

        val level = player.serverLevel()
        val carrier = player.vehicle as? VehicleEntity
        val seat = carrier?.getSeatIndex(player) ?: -1
        if (player.isPassenger) {
            if (carrier == null || carrier is RCEntity || carrier.isRemoved || carrier.isWreck || carrier.health <= 0 || seat < 0) return false
        } else if (!player.onGround() || player.isInWater ||
            !level.noCollision(player, player.getDimensions(Pose.STANDING).makeBoundingBox(player.position()))
        ) return false

        val body = ModEntities.RC_OPERATOR_BODY.create(level) ?: return false
        val session = Session(player, vehicle, body, carrier, seat)
        body.copyAppearance(player)
        sessions[player.uuid] = session
        level.chunkSource.addRegionTicket(anchorTicket, session.chunk, ANCHOR_DISTANCE, player.uuid)
        saveReturn(session)

        if (!level.addFreshEntity(body)) {
            endSession(player)
            return false
        }

        if (carrier != null) {
            player.stopRiding()
            if (player.isPassenger || !carrier.boardSeat(body, seat)) {
                endSession(player)
                return false
            }
            carrier.positionRider(body, Entity::setPos)
            if (seat == 0) {
                carrier.processInput(0)
                carrier.mouseInput(0.0, 0.0)
            }
        }

        vehicle.processInput(0)
        VehicleVecUtils.setDriverAngle(vehicle, player)
        player.isSprinting = false
        if (!player.startRiding(vehicle, true) || vehicle.firstPassenger !== player) {
            endSession(player)
            return false
        }
        session.mounting = false
        player.displayClientMessage(Component.translatable("tips.superbwarfare.monitor.controlling", vehicle.displayName), true)
        return true
    }

    @JvmStatic
    fun isMounting(passenger: Entity, vehicle: RCEntity): Boolean =
        sessions[passenger.uuid]?.let { it.mounting && !it.ending && it.player === passenger && it.vehicle === vehicle } == true

    @JvmStatic
    fun endSession(vehicle: RCEntity) {
        sessions.values.firstOrNull { it.vehicle === vehicle }?.let { endSession(it.player) }
    }

    @JvmStatic
    fun endSession(player: ServerPlayer): Boolean {
        val session = sessions[player.uuid] ?: return false
        if (session.player !== player || session.ending) return false
        session.ending = true
        val carrier = session.carrier
        val bodyInSeat = carrier != null && session.body.vehicle === carrier && carrier.getNthEntity(session.seat) === session.body
        if (bodyInSeat) updateAnchor(session)
        session.vehicle.processInput(0)
        if (player.vehicle === session.vehicle) player.stopRiding()
        if (player.vehicle === session.vehicle) {
            session.ending = false
            return false
        }

        try {
            if (bodyInSeat) session.body.stopRiding()
            if (player.isAlive) restoreReturn(player, session.mounting || bodyInSeat)
            else player.persistentData.remove(RETURN_DATA)
        } finally {
            sessions.remove(player.uuid)
            session.body.discard()
            session.anchorLevel.chunkSource.removeRegionTicket(anchorTicket, session.chunk, ANCHOR_DISTANCE, player.uuid)
        }
        return true
    }

    @JvmStatic
    fun hurtBody(body: RCOperatorBodyEntity, source: DamageSource, amount: Float): Boolean {
        val operatorId = body.operatorProfile?.id ?: return false
        val session = sessions[operatorId] ?: return false
        if (session.body !== body || session.mounting || session.ending || !amount.isFinite() || amount <= 0f) return false

        val player = session.player
        val attacker = source.entity as? ServerPlayer
        if (attacker != null && (!attacker.server.isPvpAllowed || !attacker.canHarmPlayer(player))) return false
        if (!LivingAttackCallback.EVENT.invoker().allowAttack(body, source, amount)) return false
        return endSession(player) && player.hurt(source, amount)
    }

    private fun saveReturn(session: Session) {
        session.player.persistentData.put(RETURN_DATA, CompoundTag().apply {
            putString("Dimension", session.anchorLevel.dimension().location().toString())
            putDouble("X", session.anchor.x)
            putDouble("Y", session.anchor.y)
            putDouble("Z", session.anchor.z)
            putFloat("Yaw", session.yaw)
            putFloat("Pitch", session.pitch)
            session.carrier?.let {
                putUUID("Carrier", it.uuid)
                putInt("Seat", session.seat)
            }
        })
    }

    private fun updateAnchor(session: Session) {
        val position = session.body.position()
        val chunk = session.carrier?.chunkPosition() ?: ChunkPos(BlockPos.containing(position))
        if (chunk != session.chunk) {
            val source = session.anchorLevel.chunkSource
            source.addRegionTicket(anchorTicket, chunk, ANCHOR_DISTANCE, session.player.uuid)
            source.removeRegionTicket(anchorTicket, session.chunk, ANCHOR_DISTANCE, session.player.uuid)
            session.chunk = chunk
        }
        if (session.anchor != position) {
            session.anchor = position
            session.player.persistentData.getCompound(RETURN_DATA).apply {
                putDouble("X", position.x)
                putDouble("Y", position.y)
                putDouble("Z", position.z)
            }
        }
        session.player.persistentData.getCompound(RETURN_DATA).apply {
            putFloat("Yaw", session.body.yRot)
            putFloat("Pitch", session.body.xRot)
        }
    }

    private fun restoreReturn(player: ServerPlayer, allowCarrier: Boolean = true) {
        if (!player.persistentData.contains(RETURN_DATA, Tag.TAG_COMPOUND.toInt())) return
        val tag = player.persistentData.getCompound(RETURN_DATA)
        val dimension = ResourceLocation.tryParse(tag.getString("Dimension"))
        val level = player.server.allLevels.firstOrNull { it.dimension().location() == dimension }
            ?: player.server.overworld()
        val anchor = Vec3(tag.getDouble("X"), tag.getDouble("Y"), tag.getDouble("Z"))
        val wasMounted = tag.hasUUID("Carrier")
        if (allowCarrier && wasMounted && level.dimension().location() == dimension) {
            val carrier = level.getEntity(tag.getUUID("Carrier")) as? VehicleEntity
            val seat = tag.getInt("Seat")
            if (carrier != null && !carrier.isRemoved && !carrier.isWreck && carrier.health > 0 && player.level() === level) {
                player.yRot = tag.getFloat("Yaw")
                player.xRot = tag.getFloat("Pitch")
                if (carrier.getNthEntity(seat) === player || carrier.boardSeat(player, seat)) {
                    player.fallDistance = 0f
                    player.persistentData.remove(RETURN_DATA)
                    return
                }
            }
        }
        val destination = if (level.dimension().location() == dimension) findReturnPosition(player, level, anchor, wasMounted)
            else Vec3.atBottomCenterOf(level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, level.sharedSpawnPos))

        player.teleportTo(level, destination.x, destination.y, destination.z, tag.getFloat("Yaw"), tag.getFloat("Pitch"))
        player.deltaMovement = Vec3.ZERO
        player.fallDistance = 0f
        player.setJumping(false)
        player.persistentData.remove(RETURN_DATA)
    }

    private fun findReturnPosition(player: ServerPlayer, level: ServerLevel, anchor: Vec3, searchSurface: Boolean): Vec3 {
        val block = BlockPos.containing(anchor)
        if (level.worldBorder.isWithinBounds(block)) level.getChunkAt(block)
        val size = player.getDimensions(Pose.STANDING)
        fun canStand(pos: Vec3): Boolean {
            val block = BlockPos.containing(pos)
            return level.hasChunkAt(block) && level.worldBorder.isWithinBounds(block) &&
                level.getFluidState(block).isEmpty &&
                level.noCollision(player, size.makeBoundingBox(pos)) &&
                !level.noCollision(player, size.makeBoundingBox(pos).move(0.0, -0.1, 0.0))
        }
        if (canStand(anchor)) return anchor
        for (candidate in BlockPos.withinManhattan(block, 2, 2, 2)) {
            val position = Vec3.atBottomCenterOf(candidate)
            if (canStand(position)) return position
        }
        if (searchSurface) {
            for (candidate in BlockPos.withinManhattan(block, 4, 0, 4)) {
                if (!level.hasChunkAt(candidate)) continue
                val surface = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, candidate)
                val position = Vec3.atBottomCenterOf(surface)
                if (canStand(position)) return position
            }
        }
        return Vec3.atBottomCenterOf(level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, level.sharedSpawnPos))
    }

    internal fun isOperating(entity: Entity): Boolean {
        if (entity !is ServerPlayer) return false
        val session = sessions[entity.uuid] ?: return false
        return session.player === entity && !session.mounting && !session.ending && entity.vehicle === session.vehicle
    }

    private fun onServerTick(server: MinecraftServer) {
        for (session in sessions.values.toList()) {
            val player = session.player
            if (player.server !== server || session.mounting || session.ending) continue
            val carrier = session.carrier
            val carrierValid = carrier == null || (!carrier.isRemoved && !carrier.isWreck && carrier.health > 0 &&
                session.body.vehicle === carrier && carrier.getNthEntity(session.seat) === session.body)
            if (!player.isAlive || player.vehicle !== session.vehicle || session.body.isRemoved ||
                session.body.level() !== player.level() || session.vehicle.isWreck || !carrierValid
            ) {
                endSession(player)
            } else if (carrier != null) {
                updateAnchor(session)
            }
        }
    }

    @JvmStatic
    fun canMount(passenger: Entity, vehicle: Entity): Boolean =
        passenger.level().isClientSide || vehicle !is RCEntity || isMounting(passenger, vehicle)

    @JvmStatic
    fun onDismount(body: RCOperatorBodyEntity, vehicle: Entity) {
        if (body.level().isClientSide) return
        val session = body.operatorProfile?.id?.let(sessions::get) ?: return
        if (!session.ending && session.body === body && session.carrier === vehicle) updateAnchor(session)
    }

    @JvmStatic
    fun canTravel(entity: Entity): Boolean = entity !is RCOperatorBodyEntity && !isOperating(entity)
}
