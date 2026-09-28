package com.xperiment.anticheat.check;

import com.xperiment.anticheat.evidence.EvidenceLogger;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.entity.Entity;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class CombatCheck {
    private CombatCheck() {}

    private static final Map<UUID, Long> LAST_ATTACK_TICK = new ConcurrentHashMap<>();
    private static final Map<UUID, Integer> FAST_ATTACK_STREAK = new ConcurrentHashMap<>();

    private static final double MAX_REACH = 3.15;
    private static final long MIN_ATTACK_INTERVAL_TICKS = 2;
    private static final int FAST_STREAK_FLAG = 4;

    public static void register() {
        AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (!(player instanceof ServerPlayerEntity serverPlayer)) {
                return ActionResult.PASS;
            }

            if (serverPlayer.isSpectator() || !entity.isAttackable()) {
                return ActionResult.PASS;
            }

            checkReach(serverPlayer, entity);
            checkAttackRate(serverPlayer);
            return ActionResult.PASS;
        });
    }

    private static void checkReach(ServerPlayerEntity player, Entity target) {
        double distance = player.distanceTo(target);

        if (distance > MAX_REACH) {
            ViolationManager.add(player, 2.0);
            EvidenceLogger.record(
                player,
                "reach",
                "target=" + target.getType()
                    + " distance=" + String.format(java.util.Locale.ROOT, "%.3f", distance)
                    + " max=" + MAX_REACH,
                distance
            );
        }
    }

    private static void checkAttackRate(ServerPlayerEntity player) {
        long now = player.getServerWorld().getTime();
        Long previous = LAST_ATTACK_TICK.put(player.getUuid(), now);

        if (previous == null) return;

        long interval = now - previous;

        if (interval < MIN_ATTACK_INTERVAL_TICKS) {
            int streak = FAST_ATTACK_STREAK.merge(player.getUuid(), 1, Integer::sum);

            if (streak >= FAST_STREAK_FLAG) {
                double vl = ViolationManager.add(player, 1.0);
                EvidenceLogger.record(
                    player,
                    "attack_rate",
                    "intervalTicks=" + interval + " streak=" + streak + " vl=" + vl,
                    interval
                );
                FAST_ATTACK_STREAK.put(player.getUuid(), 0);
            }
        } else {
            FAST_ATTACK_STREAK.compute(
                player.getUuid(),
                (id, value) -> value == null ? 0 : Math.max(0, value - 1)
            );
        }
    }

    public static void clear(ServerPlayerEntity player) {
        LAST_ATTACK_TICK.remove(player.getUuid());
        FAST_ATTACK_STREAK.remove(player.getUuid());
        EvidenceLogger.clear(player);
    }
}
