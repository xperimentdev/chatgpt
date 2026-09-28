package com.xperiment.anticheat;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.xperiment.anticheat.check.ViolationManager;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.Commands;

public final class AntiCheatCommands {
    private AntiCheatCommands() {}

    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(
                Commands.literal("xac")
                    .requires(source -> source.hasPermission(2))
                    .then(Commands.literal("vl")
                        .then(Commands.argument("player", net.minecraft.commands.arguments.EntityArgument.player())
                            .executes(context -> {
                                var player = net.minecraft.commands.arguments.EntityArgument.getPlayer(context, "player");
                                context.getSource().sendSuccess(
                                    () -> net.minecraft.network.chat.Component.literal(
                                        player.getGameProfile().name() +
                                        " VL=" + ViolationManager.get(player)
                                    ),
                                    false
                                );
                                return 1;
                            })))
                    .then(Commands.literal("reset")
                        .then(Commands.argument("player", net.minecraft.commands.arguments.EntityArgument.player())
                            .executes(context -> {
                                var player = net.minecraft.commands.arguments.EntityArgument.getPlayer(context, "player");
                                ViolationManager.clear(player);
                                context.getSource().sendSuccess(
                                    () -> net.minecraft.network.chat.Component.literal("Violation level reset."),
                                    false
                                );
                                return 1;
                            })))
            );
        });
    }
}
