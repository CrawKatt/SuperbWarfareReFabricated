package com.atsuishio.superbwarfare.network.message.send

import com.atsuishio.superbwarfare.Mod
import com.atsuishio.superbwarfare.config.server.MiscConfig
import com.atsuishio.superbwarfare.init.ModComponents
import io.netty.buffer.ByteBuf
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import java.util.function.Function

@JvmRecord
data class TacticalSprintMessage(val sprint: Boolean) : CustomPacketPayload {
    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload?> {
        return TYPE
    }

    companion object {
        val TYPE: CustomPacketPayload.Type<TacticalSprintMessage?> = CustomPacketPayload.Type<TacticalSprintMessage?>(
            Mod.loc("tactical_sprint")
        )

        val STREAM_CODEC: StreamCodec<ByteBuf?, TacticalSprintMessage?> =
            StreamCodec.composite<ByteBuf?, TacticalSprintMessage?, Boolean?>(
                ByteBufCodecs.BOOL,
                TacticalSprintMessage::sprint,
                Function { sprint: Boolean? -> TacticalSprintMessage(sprint!!) }
            )

        fun handler(message: TacticalSprintMessage, context: ServerPlayNetworking.Context) {
            val player = context.player()

            val cap = ModComponents.PLAYER_VARIABLE.get(player)
            cap.tacticalSprint = MiscConfig.ALLOW_TACTICAL_SPRINT.get() && message.sprint
            ModComponents.PLAYER_VARIABLE.sync(player)
        }
    }
}