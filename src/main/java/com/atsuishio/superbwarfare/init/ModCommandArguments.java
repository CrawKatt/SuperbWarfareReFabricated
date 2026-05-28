package com.atsuishio.superbwarfare.init;

import com.atsuishio.superbwarfare.command.LowerCamelCaseEnumArgument;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.core.registries.BuiltInRegistries;

public class ModCommandArguments {

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static final ArgumentTypeInfo<LowerCamelCaseEnumArgument<?>, ?> INFO =
            ArgumentTypeInfos.register(
                    BuiltInRegistries.COMMAND_ARGUMENT_TYPE,
                    "superbwarfare:lower_camel_case_enum",
                    (Class) LowerCamelCaseEnumArgument.class,
                    new LowerCamelCaseEnumArgument.Info()
            );

    public static void register() {

    }
}
