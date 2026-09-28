package com.xperiment.anticheat.evidence;

import com.xperiment.anticheat.XperimentAntiCheat;
import com.xperiment.anticheat.check.ViolationManager;
import net.minecraft.server.level.ServerPlayer;

import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Deque;

public final class EvidenceLogger {
    private EvidenceLogger() {}

    private static final int MAX_ENTRIES = 500;
    private static final Deque<Evidence> ENTRIES = new ArrayDeque<>();

    public static synchronized void record(
        ServerPlayer player,
        String check,
        String reason,
        double value
    ) {
        double vl = ViolationManager.get(player);
        Evidence evidence = new Evidence(
            Instant.now(),
            player.getUUID(),
            player.getGameProfile().name(),
            check,
            reason,
            value,
            vl
        );

        if (ENTRIES.size() >= MAX_ENTRIES) {
            ENTRIES.removeFirst();
        }
        ENTRIES.addLast(evidence);

        XperimentAntiCheat.LOGGER.info(
            "[EVIDENCE] player={} check={} reason={} value={} vl={}",
            evidence.playerName(),
            evidence.check(),
            evidence.reason(),
            evidence.value(),
            evidence.violationLevel()
        );
    }

    public static synchronized Evidence[] recent() {
        return ENTRIES.toArray(Evidence[]::new);
    }
}
