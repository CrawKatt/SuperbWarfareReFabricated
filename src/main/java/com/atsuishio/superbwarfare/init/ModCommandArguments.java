package com.atsuishio.superbwarfare.init;

import com.atsuishio.superbwarfare.Mod;
import com.atsuishio.superbwarfare.command.LowerCamelCaseEnumArgument;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;

public class ModCommandArguments {

    public static final ArgumentTypeInfo<?, ?> LOWER_CAMEL_CASE_ENUM =
            Registry.register(BuiltInRegistries.COMMAND_ARGUMENT_TYPE, Mod.loc("lower_camel_case_enum"), new LowerCamelCaseEnumArgument.Info<>());

    public static void init() {
    }
}
