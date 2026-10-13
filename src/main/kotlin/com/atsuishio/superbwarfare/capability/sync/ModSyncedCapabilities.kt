package com.atsuishio.superbwarfare.capability.sync

import com.atsuishio.superbwarfare.capability.ModCapabilities

/**
 * 把所有参与自动同步的 capability 登记进 [CapabilitySync]。
 *
 * 列表来自 [ModCapabilities.ALL]，因此新增一个 capability 时不需要改这里 ——
 * 在 [ModCapabilities] 里登记一行就够了。
 */
object ModSyncedCapabilities {

    private var initialized = false

    fun register() {
        if (initialized) return
        initialized = true

        for (capability in ModCapabilities.ALL) {
            CapabilitySync.register(capability.id) { entity -> capability.syncedHolder(entity) }
        }

        CapabilitySync.registerEvents()
    }
}
