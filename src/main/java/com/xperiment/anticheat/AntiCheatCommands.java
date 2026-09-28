package com.xperiment.anticheat;

import com.xperiment.anticheat.check.ViolationManager;
import com.xperiment.anticheat.evidence.EvidenceLogger;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;

public final class AntiCheatCommands {
    private AntiCheatCommands() {}

    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(Commands.literal("xac")
                .requires(source -> source.hasPermission(2))
                .then(Commands.literal("vl")
                    .then(Commands.argument("player", EntityArgument.player())
                        .executes(context -> {
                            var player = EntityArgument.getPlayer(context, "player");
                            context.getSource().sendSuccess(
                                () -> net.minecraft.network.chat.Component.literal(
                                    player.getGameProfile().name() + " VL=" + ViolationManager.get(player)),
                                false);
                            return 1;
                        })))
                .then(Commands.literal("evidence")
                    .then(Commands.argument("player", EntityArgument.player())
                        .executes(context -> {
                            var player = EntityArgument.getPlayer(context, "player");
                            context.getSource().sendSuccess(
                                () -> net.minecraft.network.chat.Component.literal(EvidenceLogger.dump(player)),
                                false);
                            return 1;
                        })))
                .then(Commands.literal("reset")
                    .then(Commands.argument("player", EntityArgument.player())
                        .executes(context -> {
                            var player = EntityArgument.getPlayer(context, "player");
                            ViolationManager.clear(player);
                            EvidenceLogger.clear(player);
                            context.getSource().sendSuccess(
                                () -> net.minecraft.network.chat.Component.literal(
                                    "Violation level and evidence reset."), false);
                            return 1;
                        }))));
        });
    }
}
