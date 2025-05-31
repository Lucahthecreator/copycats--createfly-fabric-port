package com.copycatsplus.copycats.neoforge;

import com.copycatsplus.copycats.CCKeys;
import com.copycatsplus.copycats.Copycats;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;

@EventBusSubscriber(modid = Copycats.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class CCKeysImpl {

    public static void register() {
        // no-op: registration is handled by the event subscriber
    }

    @SubscribeEvent
    public static void register(RegisterKeyMappingsEvent event) {
        for (CCKeys key : CCKeys.values()) {
            key.keybind = new KeyMapping(key.description, key.key, Copycats.NAME);
            if (!key.modifiable)
                continue;
            event.register(key.keybind);
        }
    }
}
