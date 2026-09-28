package com.xperiment.anticheat.check;

import net.minecraft.server.level.ServerPlayer;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class ViolationManager {
    private ViolationManager() {}

    private static final Map<UUID, Double> VIOLATIONS = new ConcurrentHashMap<>();

    public static double add(ServerPlayer player, double amount) {
        return VIOLATIONS.merge(player.getUUID(), amount, Double::sum);
    }

    public static double get(ServerPlayer player) {
        return VIOLATIONS.getOrDefault(player.getUUID(), 0.0);
    }

    public static void clear(ServerPlayer player) {
        VIOLATIONS.remove(player.getUUID());
    }

    public static void remove(ServerPlayer player, double amount) {
        VIOLATIONS.computeIfPresent(
            player.getUUID(),
            (id, value) -> Math.max(0.0, value - amount)
        );
    }
}
