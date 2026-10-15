package com.atsuishio.superbwarfare.config

import com.atsuishio.superbwarfare.Mod
import com.atsuishio.superbwarfare.config.server.MiscConfig
import com.atsuishio.superbwarfare.network.message.receive.RealisticShootModeSyncMessage
import com.atsuishio.superbwarfare.tools.sendPacketTo
import com.atsuishio.superbwarfare.tools.sendPacketToAll
import fuzs.forgeconfigapiport.api.config.v2.ModConfigEvents
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
import net.minecraft.server.level.ServerPlayer
import net.minecraftforge.common.ForgeConfigSpec
import net.minecraftforge.fml.config.ModConfig

/** Forge 的 SERVER 配置只在登录握手时同步；运行期变更需要通知已经在线的玩家。 */
object RealisticShootModeSync {

    fun register() {
        ServerPlayConnectionEvents.JOIN.register { handler, _, _ -> onPlayerLoggedIn(handler.player) }
        ModConfigEvents.reloading(Mod.MODID).register(ConfigEvents::onReload)
    }

    fun broadcast() {
        if (Mod.getServer() == null || !SERVER_CONFIG.isLoaded) return
        sendPacketToAll(RealisticShootModeSyncMessage(MiscConfig.REALISTIC_SHOOT_MODE.get()))
    }

    fun onPlayerLoggedIn(player: ServerPlayer) {
        if (!SERVER_CONFIG.isLoaded) return
        // 握手后再发送当前值，覆盖握手期间管理员切换配置的情况。
        sendPacketTo(player, RealisticShootModeSyncMessage(MiscConfig.REALISTIC_SHOOT_MODE.get()))
    }

    object ConfigEvents {
        fun onReload(event: ModConfig) {
            if (event.getSpec<ForgeConfigSpec>() !== SERVER_CONFIG) return
            val server = Mod.getServer() ?: return
            // 文件监视器可能从其它线程触发重载，发包与取值都交回服务端线程。
            server.execute { broadcast() }
        }
    }
}
