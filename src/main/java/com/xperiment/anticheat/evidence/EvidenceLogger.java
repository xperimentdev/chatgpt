package com.xperiment.anticheat.evidence;

import com.xperiment.anticheat.XperimentAntiCheat;
import net.minecraft.server.level.ServerPlayer;

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

    public static void record(ServerPlayer player, String check, String details, double value) {
        Deque<String> events = EVIDENCE.computeIfAbsent(player.getUUID(), ignored -> new ArrayDeque<>());
        synchronized (events) {
            if (events.size() >= MAX_EVENTS_PER_PLAYER) events.removeFirst();
            String line = Instant.now() + " | player=" + player.getGameProfile().name()
                + " | check=" + check + " | value="
                + String.format(java.util.Locale.ROOT, "%.3f", value) + " | " + details;
            events.addLast(line);
            XperimentAntiCheat.LOGGER.warn(line);
        }
    }

    public static String dump(ServerPlayer player) {
        Deque<String> events = EVIDENCE.get(player.getUUID());
        if (events == null) return "No evidence recorded.";
        synchronized (events) { return String.join("\n", events); }
    }

    public static void clear(ServerPlayer player) { EVIDENCE.remove(player.getUUID()); }
}
