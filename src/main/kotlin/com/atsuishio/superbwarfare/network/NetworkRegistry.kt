package com.atsuishio.superbwarfare.network

import com.atsuishio.superbwarfare.Mod.Companion.loc
import com.atsuishio.superbwarfare.network.message.receive.*
import com.atsuishio.superbwarfare.network.message.send.*
import com.atsuishio.superbwarfare.serialization.ByteBufDecoder
import com.atsuishio.superbwarfare.serialization.ByteBufEncoder
import com.atsuishio.superbwarfare.tools.createStreamCodec
import kotlinx.serialization.serializer
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

val payloadTypeMap = mutableMapOf<Class<*>, CustomPacketPayload.Type<*>>()

inline fun <reified T> encodeTo(output: FriendlyByteBuf, value: T) {
    ByteBufEncoder(output).encodeSerializableValue(serializer(), value)
}

inline fun <reified T> decodeFrom(input: FriendlyByteBuf): T {
    return ByteBufDecoder(input).decodeSerializableValue(serializer())
}

/**
 * Builds the [CustomPacketPayload.Type] for [T] (deriving its id from the class name, mirroring the
 * NeoForge 0.8.9 naming scheme) together with its stream codec, and stores the type in
 * [payloadTypeMap].
 */
private inline fun <reified T : PacketPayload> buildType(): Pair<CustomPacketPayload.Type<T>, StreamCodec<FriendlyByteBuf, T>> {
    val codec = createStreamCodec<T>()
    val className = T::class.java.simpleName.substringBefore("Message")

    val name = buildString {
        append(className[0].lowercase())

        for (i in 1 until className.length) {
            val c = className[i]
            if (c.isUpperCase()) {
                append("_")
            }
            append(className[i].lowercase())
        }
    }

    val type = CustomPacketPayload.Type<T>(loc(name))
    payloadTypeMap[T::class.java] = type
    return type to codec
}

/**
 * Registers a server-bound (C2S) payload: the stream codec with the [PayloadTypeRegistry] and the
 * global receiver that runs the payload's [PacketPayload.handler] on the server thread.
 */
private inline fun <reified T : ServerPacketPayload> playToServer() {
    val (type, codec) = buildType<T>()
    PayloadTypeRegistry.playC2S().register(type, codec)
    ServerPlayNetworking.registerGlobalReceiver(type) { msg, context ->
        with(msg) { PayloadContext(context.player()).handler() }
    }
}

/**
 * Registers a client-bound (S2C) payload's stream codec with the [PayloadTypeRegistry]. The client
 * receiver is wired up separately in [registerClientReceivers] to avoid loading client-only classes
 * on a dedicated server.
 */
private inline fun <reified T : ClientPacketPayload> playToClient() {
    val (type, codec) = buildType<T>()
    PayloadTypeRegistry.playS2C().register(type, codec)
}

/**
 * Wires up the global client receiver for a single client-bound payload type. Kept inline so the
 * reified [T] is preserved for the handler receiver dispatch.
 */
private inline fun <reified T : ClientPacketPayload> clientReceiver() {
    val type = payloadTypeMap[T::class.java] as CustomPacketPayload.Type<T>
    ClientPlayNetworking.registerGlobalReceiver(type) { msg, context ->
        with(msg) { PayloadContext(context.player()).handler() }
    }
}

/**
 * Registers the global client receivers for every client-bound payload. Must only be called from
 * the client entrypoint ([net.fabricmc.api.ClientModInitializer]).
 */
fun registerClientReceivers() {
    clientReceiver<ClientIndicatorMessage>()
    clientReceiver<ClientSetMotionMessage>()
    clientReceiver<DataSyncMessage>()
    clientReceiver<ClientMotionSyncMessage>()
    clientReceiver<ClientPhosphorusFireMessage>()
    clientReceiver<ContainerDataMessage>()
    clientReceiver<DrawClientMessage>()
    clientReceiver<FinishAssemblingVehicleMessage>()
    clientReceiver<LivingGunKillMessage>()
    clientReceiver<PlayerVariablesSyncMessage>()
    clientReceiver<RadarMenuCloseMessage>()
    clientReceiver<RadarMenuOpenMessage>()
    clientReceiver<ResetCameraTypeMessage>()
    clientReceiver<ShakeClientMessage>()
    clientReceiver<ShootClientMessage>()
    clientReceiver<SoundClientMessage>()
    clientReceiver<TDMSyncMessage>()
    clientReceiver<EntitySyncMessage>()
    clientReceiver<PlayerInfoSyncMessage>()
    clientReceiver<ClientVehicleItemMessage>()
}

fun registerPayloads() {
    playToClient<ClientIndicatorMessage>()
    playToClient<ClientSetMotionMessage>()
    playToClient<DataSyncMessage>()
    playToClient<ClientMotionSyncMessage>()
    playToClient<ClientPhosphorusFireMessage>()
    playToClient<ContainerDataMessage>()
    playToClient<DrawClientMessage>()
    playToClient<FinishAssemblingVehicleMessage>()
    playToClient<LivingGunKillMessage>()
    playToClient<PlayerVariablesSyncMessage>()
    playToClient<RadarMenuCloseMessage>()
    playToClient<RadarMenuOpenMessage>()
    playToClient<ResetCameraTypeMessage>()
    playToClient<ShakeClientMessage>()
    playToClient<ShootClientMessage>()
    playToClient<SoundClientMessage>()
    playToClient<TDMSyncMessage>()
    playToClient<EntitySyncMessage>()
    playToClient<PlayerInfoSyncMessage>()
    playToClient<ClientVehicleItemMessage>()

    playToServer<AdjustMortarAngleMessage>()
    playToServer<AdjustZoomFovMessage>()
    playToServer<AimVillagerMessage>()
    playToServer<AssembleVehicleMessage>()
    playToServer<ChangeVehicleSeatMessage>()
    playToServer<CreativeContainerStackMessage>()
    playToServer<ActiveThermalImagingMessage>()
    playToServer<ArtilleryIndicatorFireMessage>()
    playToServer<DogTagFinishEditMessage>()
    playToServer<DoubleJumpMessage>()
    playToServer<DroneFireMessage>()
    playToServer<EditMessage>()
    playToServer<FireKeyMessage>()
    playToServer<FireModeMessage>()
    playToServer<FiringParametersEditMessage>()
    playToServer<GunReforgeMessage>()
    playToServer<InteractMessage>()
    playToServer<LaserShootMessage>()
    playToServer<LungeMineAttackMessage>()
    playToServer<MeleeAttackMessage>()
    playToServer<MouseMoveMessage>()
    playToServer<ParachuteMessage>()
    playToServer<PlayerStopRidingMessage>()
    playToServer<RadarChangeModeMessage>()
    playToServer<RadarSetPosMessage>()
    playToServer<RadarSetTargetMessage>()
    playToServer<RadarSetParametersMessage>()
    playToServer<ReloadMessage>()
    playToServer<SeekingWeaponWarningMessage>()
    playToServer<SensitivityMessage>()
    playToServer<SetFiringParametersMessage>()
    playToServer<SetPerkLevelMessage>()
    playToServer<ShootMessage>()
    playToServer<ShowChargingRangeMessage>()
    playToServer<SwitchScopeMessage>()
    playToServer<SwitchVehicleWeaponMessage>()
    playToServer<TacticalSprintMessage>()
    playToServer<UnloadMessage>()
    playToServer<VehicleFireMessage>()
    playToServer<VehicleMovementMessage>()
    playToServer<WeaponZoomingMessage>()
    playToServer<ZoomMessage>()
    playToServer<BlueprintCraftMessage>()
    playToServer<BlueprintSetIndexMessage>()
}
