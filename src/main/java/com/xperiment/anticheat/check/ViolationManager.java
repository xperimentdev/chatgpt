package com.xperiment.anticheat.check;

import net.minecraft.server.network.ServerPlayerEntity;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class ViolationManager {
    private ViolationManager() {}
    private static final Map<UUID, Double> VIOLATIONS = new ConcurrentHashMap<>();

    public static double add(ServerPlayerEntity player, double amount) {
        return VIOLATIONS.merge(player.getUuid(), amount, Double::sum);
    }

    public static double get(ServerPlayerEntity player) {
        return VIOLATIONS.getOrDefault(player.getUuid(), 0.0);
    }

    public static void clear(ServerPlayerEntity player) {
        VIOLATIONS.remove(player.getUuid());
    }

    public static void remove(ServerPlayerEntity player, double amount) {
        VIOLATIONS.computeIfPresent(
            player.getUuid(),
            (id, value) -> Math.max(0.0, value - amount)
        );
    }
}
