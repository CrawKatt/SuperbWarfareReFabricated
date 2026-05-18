package com.atsuishio.superbwarfare.command;

import com.atsuishio.superbwarfare.Mod;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.Commands;

public class CommandRegister {

    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            var command = Commands.literal("sbw");
            command.then(AmmoCommand.get());
            command.then(ConfigCommand.get());
            command.then(TDMCommand.get());

            var result = dispatcher.register(command);
            dispatcher.register(Commands.literal("superbwarfare").redirect(result));
        });
    }
}
