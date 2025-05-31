package com.copycatsplus.copycats.config.neoforge;

import com.copycatsplus.copycats.Copycats;
import com.copycatsplus.copycats.config.*;
import net.createmod.catnip.config.ConfigBase;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import java.util.Map;

@EventBusSubscriber(modid = Copycats.MODID, bus = EventBusSubscriber.Bus.MOD)
public class CCConfigsImpl extends CCConfigs {

    @SubscribeEvent
    public static void onLoad(ModConfigEvent.Loading event) {
        for (ConfigBase config : CONFIGS.values())
            if (config.specification == event.getConfig()
                    .getSpec())
                config.onLoad();
    }

    @SubscribeEvent
    public static void onReload(ModConfigEvent.Reloading event) {
        for (ConfigBase config : CONFIGS.values())
            if (config.specification == event.getConfig()
                    .getSpec())
                config.onReload();
    }

    public static void register() {
        ModLoadingContext context = ModLoadingContext.get();
        client = register(CClient::new, ModConfig.Type.CLIENT);
        common = register(CCommon::new, ModConfig.Type.COMMON);
        server = register(CServer::new, ModConfig.Type.SERVER);

        for (Map.Entry<ModConfig.Type, ConfigBase> pair : CONFIGS.entrySet())
            context.getActiveContainer().registerConfig(pair.getKey(), pair.getValue().specification);
    }
}
