package com.xperiment.anticheat;

import com.xperiment.anticheat.check.ViolationManager;
import com.xperiment.anticheat.evidence.Evidence;
import com.xperiment.anticheat.evidence.EvidenceLogger;
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
                    .then(Commands.literal("evidence")
                        .executes(context -> {
                            Evidence[] entries = EvidenceLogger.recent();
                            context.getSource().sendSuccess(
                                () -> net.minecraft.network.chat.Component.literal(
                                    "Recent XAC evidence entries: " + entries.length
                                ),
                                false
                            );

                            int start = Math.max(0, entries.length - 10);
                            for (int i = start; i < entries.length; i++) {
                                Evidence e = entries[i];
                                context.getSource().sendSuccess(
                                    () -> net.minecraft.network.chat.Component.literal(
                                        e.playerName() + " | " + e.check() +
                                        " | " + e.reason() +
                                        " | value=" + e.value() +
                                        " | VL=" + e.violationLevel()
                                    ),
                                    false
                                );
                            }
                            return entries.length;
                        }))
            );
        });
    }
}
