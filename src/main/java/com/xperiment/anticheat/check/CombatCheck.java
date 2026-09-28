package com.xperiment.anticheat.check;

import com.xperiment.anticheat.config.AntiCheatConfig;
import com.xperiment.anticheat.evidence.EvidenceLogger;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class CombatCheck {
    private CombatCheck() {}

    private static final Map<UUID, Long> LAST_ATTACK_TICK = new ConcurrentHashMap<>();
    private static final Map<UUID, Integer> FAST_ATTACKS = new ConcurrentHashMap<>();

    public static void register() {
        AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (world.isClientSide() || !(player instanceof ServerPlayer serverPlayer)) {
                return InteractionResult.PASS;
            }

            long tick = serverPlayer.serverLevel().getGameTime();
            Long last = LAST_ATTACK_TICK.put(serverPlayer.getUUID(), tick);

            if (last != null) {
                long interval = tick - last;

                if (interval <= AntiCheatConfig.MIN_ATTACK_INTERVAL_TICKS) {
                    int count = FAST_ATTACKS.merge(serverPlayer.getUUID(), 1, Integer::sum);
                    ViolationManager.add(serverPlayer, AntiCheatConfig.FAST_ATTACK_VL);
                    EvidenceLogger.record(
                        serverPlayer,
                        "attack-rate",
                        "Repeated attacks below the configured tick interval",
                        interval
                    );

                    if (count > AntiCheatConfig.FAST_ATTACK_STREAK_RESET) {
                        FAST_ATTACKS.put(serverPlayer.getUUID(), 0);
                    }
                } else {
                    FAST_ATTACKS.computeIfPresent(
                        serverPlayer.getUUID(),
                        (id, count) -> Math.max(0, count - 1)
                    );
                }
            }

            validateReach(serverPlayer, entity);
            return InteractionResult.PASS;
        });
    }

    private static void validateReach(ServerPlayer player, Entity target) {
        if (!player.isAlive() || target.isRemoved() || target == player) {
            return;
        }

        double distanceSquared = player.distanceToSqr(target);
        double max = AntiCheatConfig.MAX_COMBAT_REACH + target.getPickRadius();
        double maxSquared = max * max;

        if (distanceSquared > maxSquared) {
            ViolationManager.add(player, AntiCheatConfig.REACH_VL);
            EvidenceLogger.record(
                player,
                "reach",
                "Server-side distance exceeded configured combat reach",
                Math.sqrt(distanceSquared)
            );
        }
    }

    public static void remove(UUID uuid) {
        LAST_ATTACK_TICK.remove(uuid);
        FAST_ATTACKS.remove(uuid);
    }
}
