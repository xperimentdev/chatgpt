package com.xperiment.anticheat;

import com.xperiment.anticheat.check.ViolationManager;
import com.xperiment.anticheat.evidence.EvidenceLogger;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public final class AntiCheatCommands {
    private AntiCheatCommands() {}

    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(CommandManager.literal("xac")
                .requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.literal("vl")
                    .then(CommandManager.argument("player", EntityArgumentType.player())
                        .executes(context -> showVl(context))))
                .then(CommandManager.literal("evidence")
                    .then(CommandManager.argument("player", EntityArgumentType.player())
                        .executes(context -> showEvidence(context))))
                .then(CommandManager.literal("reset")
                    .then(CommandManager.argument("player", EntityArgumentType.player())
                        .executes(context -> reset(context)))));
        });
    }

    private static int showVl(CommandContext<ServerCommandSource> context) throws Exception {
        ServerPlayerEntity player = EntityArgumentType.getPlayer(context, "player");
        context.getSource().sendFeedback(
            () -> Text.literal(player.getGameProfile().name() + " VL=" + ViolationManager.get(player)),
            false
        );
        return 1;
    }

    private static int showEvidence(CommandContext<ServerCommandSource> context) throws Exception {
        ServerPlayerEntity player = EntityArgumentType.getPlayer(context, "player");
        context.getSource().sendMessage(Text.literal(EvidenceLogger.dump(player)));
        return 1;
    }

    private static int reset(CommandContext<ServerCommandSource> context) throws Exception {
        ServerPlayerEntity player = EntityArgumentType.getPlayer(context, "player");
        ViolationManager.clear(player);
        EvidenceLogger.clear(player);
        context.getSource().sendFeedback(
            () -> Text.literal("Violation level and evidence reset."),
            false
        );
        return 1;
    }
}
