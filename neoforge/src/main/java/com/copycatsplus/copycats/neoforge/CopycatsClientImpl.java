package com.copycatsplus.copycats.neoforge;

import com.copycatsplus.copycats.CopycatsClient;
import com.copycatsplus.copycats.foundation.copycat.model.kinetic.RendererReloadCache;
import dev.engine_room.flywheel.api.event.ReloadLevelRendererEvent;
import net.neoforged.neoforge.common.NeoForge;

public class CopycatsClientImpl {

    public static void init() {
        CopycatsClient.init();
        NeoForge.EVENT_BUS.<ReloadLevelRendererEvent>addListener(RendererReloadCache::onReloadLevelRenderer);
    }
}
