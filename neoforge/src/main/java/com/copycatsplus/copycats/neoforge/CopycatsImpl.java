package com.copycatsplus.copycats.neoforge;

import com.copycatsplus.copycats.Copycats;
import com.copycatsplus.copycats.compat.Mods;
import com.copycatsplus.copycats.compat.neoforge.AdditionalPlacementsCompatNeoForge;
import com.copycatsplus.copycats.datagen.neoforge.CCDatagenImpl;
import com.copycatsplus.copycats.datagen.recipes.neoforge.CCCraftingConditions;
import com.copycatsplus.copycats.utility.LogicalSidedProvider;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

@Mod(Copycats.MODID)
public class CopycatsImpl {

    static IEventBus modBus;

    public CopycatsImpl(IEventBus modBus, ModContainer container) {
        CopycatsImpl.modBus = modBus;
        Copycats.getRegistrate().registerEventListeners(modBus);
        CCCreativeTabsImpl.register(CopycatsImpl.modBus);
        Copycats.init();

        CCCraftingConditions.register(CopycatsImpl.modBus);
        NeoForge.EVENT_BUS.addListener(this::serverStarting);

        modBus.addListener(EventPriority.HIGHEST, CCDatagenImpl::gatherDataHighPriority);
        modBus.addListener(EventPriority.LOWEST, CCDatagenImpl::gatherData);
        Mods.ADDITIONAL_PLACEMENTS.executeIfInstalled(() -> AdditionalPlacementsCompatNeoForge::register);
    }

    private void serverStarting(ServerStartingEvent event) {
        LogicalSidedProvider.setServer(event::getServer);
    }
}