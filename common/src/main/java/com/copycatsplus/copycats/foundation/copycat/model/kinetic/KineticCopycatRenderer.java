package com.copycatsplus.copycats.foundation.copycat.model.kinetic;

import com.copycatsplus.copycats.CopycatsClient;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlockEntity;
import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.engine_room.flywheel.api.model.Model;
import net.createmod.catnip.render.*;
import net.minecraft.client.resources.model.BakedModel;

/**
 * Helper class to render kinetic copycat models.
 */
public class KineticCopycatRenderer {
    public static final SuperByteBufferCache.Compartment<KineticCopycatRenderData> KINETIC_COPYCAT = new SuperByteBufferCache.Compartment<>();
    private static final RendererReloadCache<KineticCopycatRenderData, Model> MODEL_CACHE = new RendererReloadCache<>();

    public static SuperByteBuffer getRenderedBuffer(ICopycatPartialModel partialModel, ICopycatBlockEntity be) {
        return CopycatsClient.BUFFER_CACHE.get(KINETIC_COPYCAT,
                KineticCopycatRenderData.of(partialModel, be),
                () -> renderBuffer(partialModel.getModel(), be, new PoseStack())
        );
    }

    public static SuperByteBuffer getRenderedBuffer(ICopycatPartialModel partialModel, IMultiStateCopycatBlockEntity be, String property) {
        return CopycatsClient.BUFFER_CACHE.get(KINETIC_COPYCAT,
                KineticCopycatRenderData.of(partialModel, be, property),
                () -> renderBuffer(partialModel.getModel(), be, new PoseStack())
        );
    }

    public static Model getInstancedModel(ICopycatPartialModel partialModel, ICopycatBlockEntity be) {
        return MODEL_CACHE.get(
                KineticCopycatRenderData.of(partialModel, be),
                data -> instancedModel(partialModel.getModel(), be)
        );
    }

    public static Model getInstancedModel(ICopycatPartialModel partialModel, IMultiStateCopycatBlockEntity be, String property) {
        return MODEL_CACHE.get(
                KineticCopycatRenderData.of(partialModel, be, property),
                data -> instancedModel(partialModel.getModel(), be)
        );
    }

    @ExpectPlatform
    public static SuperByteBuffer renderBuffer(BakedModel model, ICopycatBlockEntity be, PoseStack ms) {
        return null;
    }

    @ExpectPlatform
    public static Model instancedModel(BakedModel model, ICopycatBlockEntity be) {
        return null;
    }
}
