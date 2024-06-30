package com.copycatsplus.copycats.content.copycat.base.model.kinetic;

import com.copycatsplus.copycats.CopycatsClient;
import com.copycatsplus.copycats.content.copycat.base.ICopycatBlockEntity;
import com.copycatsplus.copycats.content.copycat.partial.CopycatPartialModel;
import com.jozufozu.flywheel.core.model.BlockModel;
import com.jozufozu.flywheel.core.model.ShadeSeparatedBufferedData;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.render.SuperByteBufferCache;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.resources.model.BakedModel;

public class KineticCopycatRenderer {
    public static final SuperByteBufferCache.Compartment<KineticCopycatRenderData> KINETIC_COPYCAT = new SuperByteBufferCache.Compartment<>();

    public static SuperByteBuffer getBuffer(CopycatPartialModel partialModel, ICopycatBlockEntity be) {
        return CopycatsClient.BUFFER_CACHE.get(KINETIC_COPYCAT,
                KineticCopycatRenderData.of(partialModel, be),
                () -> copycatRender(partialModel, be)
        );
    }

    public static BlockModel getInstanceModel(CopycatPartialModel partialModel, ICopycatBlockEntity be, KineticCopycatRenderData renderData) {
        ShadeSeparatedBufferedData data = getCopycatBuffer(partialModel.getModel(), be);
        BlockModel blockModel = new BlockModel(data, renderData.toString());
        data.release();
        return blockModel;
    }

    public static SuperByteBuffer copycatRender(CopycatPartialModel partialModel, ICopycatBlockEntity be) {
        ShadeSeparatedBufferedData bufferedData = getCopycatBuffer(partialModel.getModel(), be);
        SuperByteBuffer sbb = new SuperByteBuffer(bufferedData);
        bufferedData.release();
        return sbb;
    }

    public static ShadeSeparatedBufferedData getCopycatBuffer(BakedModel partialModel, ICopycatBlockEntity be) {
        return getCopycatBuffer(partialModel, be, new PoseStack());
    }

    @ExpectPlatform
    public static ShadeSeparatedBufferedData getCopycatBuffer(BakedModel model, ICopycatBlockEntity be, PoseStack ms) {
        return null;
    }
}
