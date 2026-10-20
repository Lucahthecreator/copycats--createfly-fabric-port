package com.copycatsplus.copycats.neoforge;

import com.copycatsplus.copycats.CCBlockEntityTypes;
import com.copycatsplus.copycats.Copycats;
import com.copycatsplus.copycats.CopycatsClient;
import com.copycatsplus.copycats.content.copycat.cogwheel.CopycatCogWheelVisuals;
import com.copycatsplus.copycats.content.copycat.sliding_door.CopycatSlidingDoorRenderBridge;
import com.copycatsplus.copycats.foundation.copycat.model.CopycatKineticRenderBridge;
import net.minecraft.core.registries.Registries;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

/**
 * NeoForge client entrypoint, replacing the Fabric {@code client} entrypoint ({@code fabric.CopycatsClientImpl}).
 * <p>
 * The copycat model factories go into Create's {@code AllModels} once the blocks and block entity types exist, well
 * before the first model bake. Block entity renderers and tints use NeoForge's events instead of Fabric API's
 * {@code BlockEntityRendererRegistry} and {@code CLIENT_STARTED}.
 */
@Mod(value = Copycats.MODID, dist = Dist.CLIENT)
public class CopycatsNeoForgeClient {
    public CopycatsNeoForgeClient(IEventBus modBus, ModContainer container) {
        modBus.addListener(EventPriority.LOW, (RegisterEvent event) -> {
            if (event.getRegistryKey().equals(Registries.BLOCK_ENTITY_TYPE)) {
                CopycatsClient.init();
                CopycatCogWheelVisuals.register();
            }
        });
        modBus.addListener(CopycatsNeoForgeClient::registerRenderers);
        modBus.addListener(CopycatsClient::registerBlockTints);
    }

    private static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(CCBlockEntityTypes.COPYCAT_SLIDING_DOOR.get(), CopycatSlidingDoorRenderBridge::new);
        event.registerBlockEntityRenderer(CCBlockEntityTypes.COPYCAT_SHAFT.get(), CopycatKineticRenderBridge::new);
        event.registerBlockEntityRenderer(CCBlockEntityTypes.COPYCAT_COGWHEEL.get(), CopycatKineticRenderBridge::new);
    }
}
