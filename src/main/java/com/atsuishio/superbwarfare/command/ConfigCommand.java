package com.atsuishio.superbwarfare.command;

import com.atsuishio.superbwarfare.config.server.ExplosionConfig;
import com.atsuishio.superbwarfare.config.server.MiscConfig;
import com.atsuishio.superbwarfare.config.server.ProjectileConfig;
import com.atsuishio.superbwarfare.config.server.VehicleConfig;
import com.atsuishio.superbwarfare.network.message.receive.ClientTacticalSprintSyncMessage;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;

public class ConfigCommand {

    public static LiteralArgumentBuilder<CommandSourceStack> get() {
        return Commands.literal("config").requires(s -> s.hasPermission(0))
                .then(Commands.literal("explosionDestroy").requires(s -> s.hasPermission(2)).then(Commands.argument("value", BoolArgumentType.bool()).executes(context -> {
                    var value = BoolArgumentType.getBool(context, "value");
                    ExplosionConfig.EXPLOSION_DESTROY = value;
                    ExplosionConfig.save();

                    context.getSource().sendSuccess(() -> Component.translatable(value ? "commands.config.explosion_destroy.enabled" : "commands.config.explosion_destroy.disabled"), true);
                    return 0;
                })))
                .then(Commands.literal("collisionDestroy").requires(s -> s.hasPermission(2))
                        .then(Commands.literal("none").executes(context -> {
                            VehicleConfig.COLLISION_DESTROY_SOFT_BLOCKS = false;
                            VehicleConfig.COLLISION_DESTROY_NORMAL_BLOCKS = false;
                            VehicleConfig.COLLISION_DESTROY_HARD_BLOCKS = false;
                            VehicleConfig.COLLISION_DESTROY_BLOCKS_BEASTLY = false;

                            VehicleConfig.save();

                            context.getSource().sendSuccess(() -> Component.translatable("commands.config.collision_destroy.none"), true);
                            return 0;
                        }))
                        .then(Commands.literal("soft").executes(context -> {
                            VehicleConfig.COLLISION_DESTROY_SOFT_BLOCKS = true;
                            VehicleConfig.COLLISION_DESTROY_NORMAL_BLOCKS = false;
                            VehicleConfig.COLLISION_DESTROY_HARD_BLOCKS = false;
                            VehicleConfig.COLLISION_DESTROY_BLOCKS_BEASTLY = false;

                            VehicleConfig.save();

                            context.getSource().sendSuccess(() -> Component.translatable("commands.config.collision_destroy.soft"), true);
                            return 0;
                        }))
                        .then(Commands.literal("normal").executes(context -> {
                            VehicleConfig.COLLISION_DESTROY_SOFT_BLOCKS = true;
                            VehicleConfig.COLLISION_DESTROY_NORMAL_BLOCKS = true;
                            VehicleConfig.COLLISION_DESTROY_HARD_BLOCKS = false;
                            VehicleConfig.COLLISION_DESTROY_BLOCKS_BEASTLY = false;

                            VehicleConfig.save();

                            context.getSource().sendSuccess(() -> Component.translatable("commands.config.collision_destroy.normal"), true);
                            return 0;
                        }))
                        .then(Commands.literal("hard").executes(context -> {
                            VehicleConfig.COLLISION_DESTROY_SOFT_BLOCKS = true;
                            VehicleConfig.COLLISION_DESTROY_NORMAL_BLOCKS = true;
                            VehicleConfig.COLLISION_DESTROY_HARD_BLOCKS = true;
                            VehicleConfig.COLLISION_DESTROY_BLOCKS_BEASTLY = false;

                            VehicleConfig.save();

                            context.getSource().sendSuccess(() -> Component.translatable("commands.config.collision_destroy.hard"), true);
                            return 0;
                        }))
                        .then(Commands.literal("beastly").executes(context -> {
                            VehicleConfig.COLLISION_DESTROY_SOFT_BLOCKS = true;
                            VehicleConfig.COLLISION_DESTROY_NORMAL_BLOCKS = true;
                            VehicleConfig.COLLISION_DESTROY_HARD_BLOCKS = true;
                            VehicleConfig.COLLISION_DESTROY_BLOCKS_BEASTLY = true;

                            VehicleConfig.save();

                            context.getSource().sendSuccess(() -> Component.translatable("commands.config.collision_destroy.beastly"), true);
                            return 0;
                        }))
                )
                .then(Commands.literal("tacticalSprint").requires(s -> s.hasPermission(2)).then(Commands.argument("value", BoolArgumentType.bool()).executes(context -> {
                    var value = BoolArgumentType.getBool(context, "value");
                    MiscConfig.ALLOW_TACTICAL_SPRINT = value;
                    MiscConfig.save();

                    MinecraftServer server = context.getSource().getServer();
                    if (server != null) {
                        server.getPlayerList().getPlayers().forEach(p ->
                                ServerPlayNetworking.send(p, new ClientTacticalSprintSyncMessage(value))
                        );
                    }

                    context.getSource().sendSuccess(() -> Component.translatable(value ? "commands.config.tactical_sprint.enabled" : "commands.config.tactical_sprint.disabled"), true);
                    return 0;
                })))
                .then(Commands.literal("blockDestroy").requires(s -> s.hasPermission(2)).then(Commands.argument("value", BoolArgumentType.bool()).executes(context -> {
                    var value = BoolArgumentType.getBool(context, "value");
                    ProjectileConfig.ALLOW_PROJECTILE_DESTROY_BLOCKS = value;
                    ProjectileConfig.save();

                    context.getSource().sendSuccess(() -> Component.translatable(value ? "commands.config.block_destroy.enabled" : "commands.config.block_destroy.disabled"), true);
                    return 0;
                })))
                .then(Commands.literal("forceDamage").requires(s -> s.hasPermission(2)).then(Commands.argument("value", BoolArgumentType.bool()).executes(context -> {
                    var value = BoolArgumentType.getBool(context, "value");
                    MiscConfig.ALLOW_FORCE_DAMAGE = value;
                    MiscConfig.save();

                    context.getSource().sendSuccess(() -> Component.translatable(value ? "commands.config.force_damage.enabled" : "commands.config.force_damage.disabled"), true);
                    return 0;
                })))
                .then(Commands.literal("dropAmmoBox").requires(s -> s.hasPermission(2)).then(Commands.argument("value", BoolArgumentType.bool()).executes(context -> {
                    var value = BoolArgumentType.getBool(context, "value");
                    MiscConfig.DROP_AMMO_BOX = value;
                    MiscConfig.save();

                    context.getSource().sendSuccess(() -> Component.translatable(value ? "commands.config.drop_ammo_box.enabled" : "commands.config.drop_ammo_box.disabled"), true);
                    return 0;
                })));
    }
}
