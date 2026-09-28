package com.xperiment.anticheat.config;

public final class AntiCheatConfig {
    private AntiCheatConfig() {}

    public static final double MOVEMENT_VL_PER_FLAG = 1.0;
    public static final double DECAY_PER_SECOND = 0.25;

    public static final double WARNING_VL = 5.0;
    public static final double KICK_VL = 20.0;

    // Conservative defaults; tune for your server's latency and combat rules.
    public static final long MIN_ATTACK_INTERVAL_TICKS = 1;
    public static final int FAST_ATTACK_STREAK_RESET = 8;
    public static final double FAST_ATTACK_VL = 0.5;

    // Vanilla-style starting point. Do not treat a single reach flag as proof.
    public static final double MAX_COMBAT_REACH = 3.1;
}
