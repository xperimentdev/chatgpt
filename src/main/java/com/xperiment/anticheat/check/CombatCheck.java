package com.xperiment.anticheat.check;

import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.world.InteractionResult;

public final class CombatCheck {
    private CombatCheck() {}

    public static void register() {
        AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            /*
             * Part 2 intentionally records the event point only.
             * Reach/rotation/timing checks belong in server-authoritative
             * packet/event validation rather than trusting the client.
             */
            return InteractionResult.PASS;
        });
    }
}
