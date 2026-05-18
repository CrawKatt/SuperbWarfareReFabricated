package com.atsuishio.superbwarfare.init;

import com.atsuishio.superbwarfare.command.LowerCamelCaseEnumArgument;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.core.registries.BuiltInRegistries;

public class ModCommandArguments {

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static final ArgumentTypeInfo<?, ?> LOWER_CAMEL_CASE_ENUM =
            ArgumentTypeInfos.register(
                    BuiltInRegistries.COMMAND_ARGUMENT_TYPE,
                    "superbwarfare:lower_camel_case_enum",
                    (Class) LowerCamelCaseEnumArgument.class,
                    new LowerCamelCaseEnumArgument.Info()
            );

    public static void init() {
        // fuerza carga de clase
    }
}