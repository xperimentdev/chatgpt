package com.xperiment.anticheat.detection;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public final class ClientModScanner {
    private ClientModScanner() {}

    private static final Set<String> BLOCKED_MODS = Set.of(
        "example_cheat",
        "example_client"
    );

    public static List<DetectionResult> scan() {
        List<DetectionResult> detections = new ArrayList<>();
        for (ModContainer mod : FabricLoader.getInstance().getAllMods()) {
            String id = mod.getMetadata().getId().toLowerCase(Locale.ROOT);
            String name = mod.getMetadata().getName().toLowerCase(Locale.ROOT);
            if (BLOCKED_MODS.contains(id)) {
                detections.add(new DetectionResult(
                    id, name, "Blocked client mod detected", 100
                ));
            }
        }
        return detections;
    }
}
