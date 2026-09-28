package com.xperiment.anticheat.check;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.level.ServerPlayer;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class MovementCheck {
    private MovementCheck() {}

    private static final Map<UUID, Double> LAST_Y = new ConcurrentHashMap<>();

    public static void register() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerPlayer player = handler.getPlayer();
            LAST_Y.put(player.getUUID(), player.getY());
        });

        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            LAST_Y.remove(handler.getPlayer().getUUID());
        });
    }

    public static void tick(ServerPlayer player) {
        Double previousY = LAST_Y.put(player.getUUID(), player.getY());

        if (previousY == null || player.isSpectator() || player.isFallFlying()) {
            return;
        }

        double deltaY = player.getY() - previousY;

        // Conservative starter check. Do not punish from this alone.
        if (deltaY > 1.2 && !player.onGround() && !player.isInWater()) {
            ViolationManager.add(player, 1.0);
        }
    }
}
