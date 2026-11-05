package com.atsuishio.superbwarfare.config

import com.atsuishio.superbwarfare.Mod
import com.atsuishio.superbwarfare.config.server.GunConfig
import com.atsuishio.superbwarfare.network.message.receive.UnrestrictedModificationSyncMessage
import com.atsuishio.superbwarfare.tools.sendPacketTo
import com.atsuishio.superbwarfare.tools.sendPacketToAll
import fuzs.forgeconfigapiport.api.config.v2.ModConfigEvents
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
import net.minecraft.server.level.ServerPlayer
import net.minecraftforge.common.ForgeConfigSpec
import net.minecraftforge.fml.config.ModConfig

/** 把 `unrestricted_modification` 运行期的改动推给在线玩家（含指令切换与配置文件重载）。 */
object UnrestrictedModificationSync {

    fun register() {
        ServerPlayConnectionEvents.JOIN.register { handler, _, _ -> onPlayerLoggedIn(handler.player) }
        ModConfigEvents.reloading(Mod.MODID).register(ConfigEvents::onReload)
    }

    fun broadcast() {
        if (Mod.getServer() == null || !SERVER_CONFIG.isLoaded) return
        sendPacketToAll(UnrestrictedModificationSyncMessage(GunConfig.UNRESTRICTED_MODIFICATION.get()))
    }

    fun onPlayerLoggedIn(player: ServerPlayer) {
        if (!SERVER_CONFIG.isLoaded) return
        // 握手后再发一次当前值，覆盖握手期间管理员切换过配置的情况
        sendPacketTo(player, UnrestrictedModificationSyncMessage(GunConfig.UNRESTRICTED_MODIFICATION.get()))
    }

    object ConfigEvents {
        fun onReload(event: ModConfig) {
            if (event.getSpec<ForgeConfigSpec>() !== SERVER_CONFIG) return
            val server = Mod.getServer() ?: return
            // 文件监听器可能从其它线程触发重载，发包与取值都交回服务端线程
            server.execute { broadcast() }
        }
    }
}
