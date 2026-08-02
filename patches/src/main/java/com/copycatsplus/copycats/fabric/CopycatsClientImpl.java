package com.copycatsplus.copycats.fabric;

import com.copycatsplus.copycats.CopycatsClient;
import com.copycatsplus.copycats.content.copycat.cogwheel.CopycatCogWheelVisuals;
import net.fabricmc.api.ClientModInitializer;

public class CopycatsClientImpl implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        CopycatsClient.init();
        CopycatCogWheelVisuals.register();
    }
}
