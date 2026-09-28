package com.xperiment.anticheat.evidence;

import com.xperiment.anticheat.XperimentAntiCheat;
import net.minecraft.server.network.ServerPlayerEntity;

import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class EvidenceLogger {
    private EvidenceLogger() {}
    private static final int MAX_EVENTS_PER_PLAYER = 50;
    private static final Map<UUID, Deque<String>> EVIDENCE = new ConcurrentHashMap<>();

    public static void record(ServerPlayerEntity player, String check, String details, double value) {
        Deque<String> events = EVIDENCE.computeIfAbsent(player.getUuid(), ignored -> new ArrayDeque<>());
        synchronized (events) {
            if (events.size() >= MAX_EVENTS_PER_PLAYER) events.removeFirst();

            String line = Instant.now() + " | player=" + player.getGameProfile().name()
                + " | uuid=" + player.getUuid()
                + " | check=" + check
                + " | value=" + String.format(java.util.Locale.ROOT, "%.3f", value)
                + " | " + details.replace("|", "/");

            events.addLast(line);
            XperimentAntiCheat.LOGGER.warn(line);
        }
    }

    public static String dump(ServerPlayerEntity player) {
        Deque<String> events = EVIDENCE.get(player.getUuid());
        if (events == null) return "No evidence recorded.";
        synchronized (events) {
            return String.join("\n", events);
        }
    }

    public static void clear(ServerPlayerEntity player) {
        EVIDENCE.remove(player.getUuid());
    }
}
