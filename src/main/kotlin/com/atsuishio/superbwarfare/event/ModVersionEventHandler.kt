package com.atsuishio.superbwarfare.event

import net.fabricmc.loader.api.Version

object ModVersionEventHandler {
    @JvmField
    var previousVersion: Version? = null

    @JvmField
    var currentVersion: Version? = null
}
