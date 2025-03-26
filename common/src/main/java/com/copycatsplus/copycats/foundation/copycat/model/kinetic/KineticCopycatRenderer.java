package com.copycatsplus.copycats.foundation.copycat.model.kinetic;

import com.copycatsplus.copycats.CopycatsClient;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlockEntity;
import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.engine_room.flywheel.api.model.Model;
import dev.engine_room.flywheel.lib.model.baked.BakedModelBuilder;
import net.createmod.catnip.render.*;
import net.minecraft.client.resources.model.BakedModel;

/**
 * Helper class to render kinetic copycat models.
 */
public class KineticCopycatRenderer {
    public static final SuperByteBufferCache.Compartment<KineticCopycatRenderData> KINETIC_COPYCAT = new SuperByteBufferCache.Compartment<>();

    public static SuperByteBuffer getBuffer(ICopycatPartialModel partialModel, ICopycatBlockEntity be) {
        return CopycatsClient.BUFFER_CACHE.get(KINETIC_COPYCAT,
                KineticCopycatRenderData.of(partialModel, be),
                () -> copycatRender(partialModel, be)
        );
    }

    public static SuperByteBuffer getBuffer(ICopycatPartialModel partialModel, IMultiStateCopycatBlockEntity be, String property) {
        return CopycatsClient.BUFFER_CACHE.get(KINETIC_COPYCAT,
                KineticCopycatRenderData.of(partialModel, be, property),
                () -> copycatRender(partialModel, be)
        );
    }

    public static Model getInstancedModel(KineticCopycatRenderData renderData) {

        return BakedModelBuilder.create(renderData.partialModel().getModel()).build();
    }

    public static SuperByteBuffer copycatRender(ICopycatPartialModel partialModel, ICopycatBlockEntity be) {
        return getCopycatBuffer(partialModel.getModel(), be);
    }

    public static SuperByteBuffer getCopycatBuffer(BakedModel partialModel, ICopycatBlockEntity be) {
        return getCopycatBuffer(partialModel, be, new PoseStack());
    }

    @ExpectPlatform
    public static SuperByteBuffer getCopycatBuffer(BakedModel model, ICopycatBlockEntity be, PoseStack ms) {
        return null;
    }
}
