package com.xperiment.anticheat;

import com.xperiment.anticheat.check.MovementCheck;
import com.xperiment.anticheat.check.ViolationManager;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

public final class AntiCheatTicker {
    private AntiCheatTicker() {}

    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (var player : server.getPlayerList().getPlayers()) {
                MovementCheck.tick(player);

                // Slow decay prevents one brief anomaly from lasting forever.
                if (server.getTickCount() % 20 == 0) {
                    ViolationManager.remove(player, 0.25);
                }
            }
        });
    }
}
