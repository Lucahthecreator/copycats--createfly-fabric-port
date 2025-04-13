package com.copycatsplus.copycats.neoforge;

import com.copycatsplus.copycats.Copycats;
import com.copycatsplus.copycats.compat.Mods;
import com.copycatsplus.copycats.compat.neoforge.AdditionalPlacementsCompatNeoForge;
import com.copycatsplus.copycats.datagen.neoforge.CCDatagenImpl;
import com.copycatsplus.copycats.datagen.recipes.neoforge.CCCraftingConditions;
import com.copycatsplus.copycats.foundation.copycat.CopycatMaterialStore;
import com.copycatsplus.copycats.utility.LogicalSidedProvider;
import com.copycatsplus.copycats.utility.Platform;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.ChunkEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

@Mod(Copycats.MODID)
public class CopycatsImpl {

    static IEventBus modBus;

    public CopycatsImpl() {
        modBus = ModLoadingContext.get().getActiveContainer().getEventBus();
        Copycats.init();
        CCCreativeTabsImpl.register(CopycatsImpl.modBus);

        CCCraftingConditions.register(CopycatsImpl.modBus);
        NeoForge.EVENT_BUS.addListener(this::serverStarting);
        NeoForge.EVENT_BUS.addListener(CopycatsImpl::onChunkUnload);
        NeoForge.EVENT_BUS.addListener(CopycatsImpl::onLevelUnload);

        Platform.Environment.CLIENT.runIfCurrent(() -> CopycatsClientImpl::init);
        modBus.addListener(EventPriority.LOWEST, CCDatagenImpl::gatherData);
        Mods.ADDITIONAL_PLACEMENTS.executeIfInstalled(() -> AdditionalPlacementsCompatNeoForge::register);
    }

    private void serverStarting(ServerStartingEvent event) {
        LogicalSidedProvider.setServer(event::getServer);
    }

    public static void finalizeRegistrate() {
        Copycats.getRegistrate().registerEventListeners(modBus);
    }

    static void onChunkUnload(ChunkEvent.Unload event) {
        CopycatMaterialStore.unloadChunk(event.getLevel(), event.getChunk().getPos());
    }

    static void onLevelUnload(LevelEvent.Unload event) {
        CopycatMaterialStore.unloadLevel(event.getLevel());
    }
}