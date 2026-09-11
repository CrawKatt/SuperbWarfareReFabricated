package com.atsuishio.superbwarfare.capability.sync

import com.atsuishio.superbwarfare.capability.ModCapabilities
import com.atsuishio.superbwarfare.capability.entity.InfiniteAmmoCapability
import com.atsuishio.superbwarfare.capability.living.PhosphorusFireCapability
import com.atsuishio.superbwarfare.capability.player.PlayerVariable

/**
 * 所有参与自动同步的 capability 的登记入口。
 *
 * 新增一个自动同步的 capability 时，在这里加一行 [CapabilitySync.register]，
 * 并在其写入点调用 [CapabilitySync.markDirty] 即可，无需再写同步包。
 */
object ModSyncedCapabilities {

    private var initialized = false

    fun register() {
        if (initialized) return
        initialized = true

        CapabilitySync.register(InfiniteAmmoCapability.ID) { entity ->
            ModCapabilities.INFINITE_AMMO.maybeGet(entity).orElse(null)
        }

        CapabilitySync.register(PhosphorusFireCapability.ID) { entity ->
            ModCapabilities.PHOSPHORUS_FIRE.maybeGet(entity).orElse(null)
        }

        CapabilitySync.register(PlayerVariable.ID) { entity ->
            ModCapabilities.PLAYER_VARIABLE.maybeGet(entity).orElse(null)
        }

        CapabilitySync.registerEvents()
    }
}
