package com.copycatsplus.copycats.foundation.copycat.model.kinetic;

import com.copycatsplus.copycats.CopycatsClient;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlockEntity;
import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.createmod.catnip.render.ShadeSeparatingSuperByteBuffer;
import net.createmod.catnip.render.SuperByteBuffer;
import net.createmod.catnip.render.SuperByteBufferCache;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.BakedModel;

/**
 * Helper class to render kinetic copycat models.
 */
//TODO: Figure out the changes in here
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

    public static BlockModel getInstanceModel(ICopycatPartialModel partialModel, ICopycatBlockEntity be, KineticCopycatRenderData renderData) {
        ShadeSeparatingSuperByteBuffer data = getCopycatBuffer(partialModel.getModel(), be);
        BlockModel blockModel = new BlockModel(data, renderData.toString());
        data.reset();
        return blockModel;
    }

    public static SuperByteBuffer copycatRender(ICopycatPartialModel partialModel, ICopycatBlockEntity be) {
        ShadeSeparatingSuperByteBuffer bufferedData = getCopycatBuffer(partialModel.getModel(), be);
        SuperByteBuffer sbb = new SuperByteBuffer(bufferedData);
        bufferedData.reset();
        return sbb;
    }

    public static ShadeSeparatingSuperByteBuffer getCopycatBuffer(BakedModel partialModel, ICopycatBlockEntity be) {
        return getCopycatBuffer(partialModel, be, new PoseStack());
    }

    @ExpectPlatform
    public static ShadeSeparatingSuperByteBuffer getCopycatBuffer(BakedModel model, ICopycatBlockEntity be, PoseStack ms) {
        return null;
    }
}
