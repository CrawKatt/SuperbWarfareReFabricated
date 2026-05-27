package com.atsuishio.superbwarfare.command;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.Commands;

public class CommandRegister {
    public static void registerEvents() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> registerCommand(dispatcher));
    }

    private static void registerCommand(com.mojang.brigadier.CommandDispatcher<net.minecraft.commands.CommandSourceStack> dispatcher) {
        var command = Commands.literal("sbw");
        command.then(AmmoCommand.get());
        command.then(ConfigCommand.get());
        command.then(TDMCommand.get());

        var result = dispatcher.register(command);
        dispatcher.register(Commands.literal("superbwarfare").redirect(result));
    }
}