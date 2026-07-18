package com.atsuishio.superbwarfare.compat.sable

import com.atsuishio.superbwarfare.compat.CompatHolder
import net.neoforged.fml.ModList

object SableCompatHandler {

    @JvmStatic
    fun hasMod(): Boolean {
        return ModList.get().isLoaded(CompatHolder.SABLE)
    }
}
