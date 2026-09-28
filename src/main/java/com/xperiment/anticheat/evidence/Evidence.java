package com.xperiment.anticheat.evidence;

import java.time.Instant;
import java.util.UUID;

public record Evidence(
    Instant time,
    UUID playerId,
    String playerName,
    String check,
    String reason,
    double value,
    double violationLevel
) {}
