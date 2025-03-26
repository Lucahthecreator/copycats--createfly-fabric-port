package com.copycatsplus.copycats.config.forge;

import com.copycatsplus.copycats.Copycats;
import com.copycatsplus.copycats.config.*;
import net.createmod.catnip.config.ConfigBase;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;

import java.util.Map;

@Mod.EventBusSubscriber(modid = Copycats.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
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
            context.registerConfig(pair.getKey(), pair.getValue().specification);
    }
}
