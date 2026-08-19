package com.atsuishio.superbwarfare.compat.kubejs

import com.atsuishio.superbwarfare.compat.kubejs.event.SbwKJSEventHandler
import dev.latvian.mods.kubejs.KubeJSPlugin

class SbwKubeJSPlugin : KubeJSPlugin() {
    override fun registerEvents() {
        SbwKJSEventHandler.GROUP.register()
        SbwKJSEventHandler.init()
    }
}
