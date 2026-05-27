package com.atsuishio.superbwarfare.init;

import com.atsuishio.superbwarfare.Mod;
import com.atsuishio.superbwarfare.command.LowerCamelCaseEnumArgument;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;

import java.util.function.Supplier;

public class ModCommandArguments {

    public static final Supplier<ArgumentTypeInfo<?, ?>> LOWER_CAMEL_CASE_ENUM =
            Registration.custom((Registry) BuiltInRegistries.COMMAND_ARGUMENT_TYPE, "lower_camel_case_enum",
                    () -> ArgumentTypeInfos.registerByClass(LowerCamelCaseEnumArgument.class, new LowerCamelCaseEnumArgument.Info()));
}
