package com.xperiment.anticheat.check;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class MovementCheck {
    private MovementCheck() {}
    private static final Map<UUID, Double> LAST_Y = new ConcurrentHashMap<>();

    public static void register() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerPlayerEntity player = handler.getPlayer();
            LAST_Y.put(player.getUuid(), player.getY());
        });

        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            LAST_Y.remove(handler.getPlayer().getUuid());
        });
    }

    public static void tick(ServerPlayerEntity player) {
        Double previousY = LAST_Y.put(player.getUuid(), player.getY());

        if (previousY == null || player.isSpectator() || player.isFallFlying()) {
            return;
        }

        double deltaY = player.getY() - previousY;

        if (deltaY > 1.2 && !player.isOnGround() && !player.isInWater()) {
            ViolationManager.add(player, 1.0);
        }
    }
}
