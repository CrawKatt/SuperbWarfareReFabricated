package com.atsuishio.superbwarfare.init

import com.atsuishio.superbwarfare.Mod
import com.atsuishio.superbwarfare.command.LowerCamelCaseEnumArgument
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry

object ModCommandArguments {

    private var initialized = false

    @JvmStatic
    fun init() {
        if (initialized) return
        initialized = true

        ArgumentTypeRegistry.registerArgumentType(
            Mod.loc("lower_camel_case_enum"),
            LowerCamelCaseEnumArgument::class.java,
            LowerCamelCaseEnumArgument.Info()
        )
    }
}
