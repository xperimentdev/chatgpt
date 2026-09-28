package com.xperiment.anticheat;

import net.fabricmc.api.ClientModInitializer;

public class XperimentAntiCheatClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        XperimentAntiCheat.LOGGER.info(
            "Xperiment Anti-Cheat client component loaded."
        );
    }
}
