package com.atsuishio.superbwarfare.network.message.send

import com.atsuishio.superbwarfare.init.ModItems
import com.atsuishio.superbwarfare.init.ModSounds
import com.atsuishio.superbwarfare.item.trinket.ParachuteItem
import com.atsuishio.superbwarfare.ksp.annotation.RegisterPacket
import com.atsuishio.superbwarfare.network.PayloadContext
import com.atsuishio.superbwarfare.network.ServerPacketPayload
import com.atsuishio.superbwarfare.tools.NBTTool
import dev.emi.trinkets.api.TrinketsApi
import net.minecraft.sounds.SoundSource

@RegisterPacket
object ParachuteMessage : ServerPacketPayload() {
    override fun PayloadContext.handler() {
        val player = sender()

        TrinketsApi.getTrinketComponent(player)
            .flatMap { c -> c.getEquipped(ModItems.PARACHUTE).stream().findFirst() }
            .ifPresent { s ->
                val stack = s.b
                if (player.cooldowns.isOnCooldown(stack.item)) return@ifPresent

                val tag = NBTTool.getTag(stack)
                if (!tag.getBoolean(ParachuteItem.TAG_OPEN) && player.deltaMovement.y < -0.6 && player.fallDistance > 4) {
                    tag.putBoolean(ParachuteItem.TAG_OPEN, true)
                    NBTTool.saveTag(stack, tag)
                    player.cooldowns.addCooldown(stack.item, 10)
                    player.level().playSound(
                        null,
                        player.x,
                        player.y,
                        player.z,
                        ModSounds.PARACHUTE_OPEN,
                        SoundSource.PLAYERS,
                        1f,
                        1f
                    )
                } else if (tag.getBoolean(ParachuteItem.TAG_OPEN)) {
                    tag.putBoolean(ParachuteItem.TAG_OPEN, false)
                    NBTTool.saveTag(stack, tag)
                    player.cooldowns.addCooldown(stack.item, 10)
                    player.level().playSound(
                        null,
                        player.x,
                        player.y,
                        player.z,
                        ModSounds.PARACHUTE_CLOSE,
                        SoundSource.PLAYERS,
                        1f,
                        1f
                    )
                }
            }
    }
}
