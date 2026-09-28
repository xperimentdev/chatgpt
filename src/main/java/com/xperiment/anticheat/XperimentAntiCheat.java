package com.xperiment.anticheat;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XperimentAntiCheat implements ModInitializer {

    public static final String MOD_ID = "xperiment_anticheat";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("================================");
        LOGGER.info(" Xperiment Anti-Cheat");
        LOGGER.info(" Version 1.0.0");
        LOGGER.info(" Server component enabled");
        LOGGER.info("================================");
    }
}
