package com.xperiment.anticheat;

import com.xperiment.anticheat.check.CombatCheck;
import com.xperiment.anticheat.check.MovementCheck;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XperimentAntiCheat implements ModInitializer {
    public static final String MOD_ID = "xperiment_anticheat";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        MovementCheck.register();
        CombatCheck.register();
        AntiCheatTicker.register();
        AntiCheatCommands.register();

        LOGGER.info("Xperiment Anti-Cheat 1.2.0 enabled.");
    }
}
