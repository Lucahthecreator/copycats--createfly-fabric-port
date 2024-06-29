package com.copycatsplus.copycats.content.copycat.base.model.functional;

import com.copycatsplus.copycats.CopycatsClient;
import com.copycatsplus.copycats.content.copycat.base.functional.ICopycatBlockEntity;
import com.jozufozu.flywheel.core.model.BlockModel;
import com.jozufozu.flywheel.core.model.ShadeSeparatedBufferedData;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.render.SuperByteBufferCache;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.resources.model.BakedModel;

public class FunctionalCopycatRenderHelper {
    public static final SuperByteBufferCache.Compartment<KineticCopycatRenderData> KINETIC_COPYCAT = new SuperByteBufferCache.Compartment<>();

    public static SuperByteBuffer getBuffer(ICopycatBlockEntity be) {
        return CopycatsClient.BUFFER_CACHE.get(KINETIC_COPYCAT,
                KineticCopycatRenderData.of(be),
                () -> copycatRender(be)
        );
    }

    public static BlockModel getInstanceModel(ICopycatBlockEntity be, KineticCopycatRenderData renderData) {
        ShadeSeparatedBufferedData data = getCopycatBuffer(be);
        BlockModel blockModel = new BlockModel(data, renderData.toString());
        data.release();
        return blockModel;
    }

    public static SuperByteBuffer copycatRender(ICopycatBlockEntity be) {
        ShadeSeparatedBufferedData bufferedData = getCopycatBuffer(be);
        SuperByteBuffer sbb = new SuperByteBuffer(bufferedData);
        bufferedData.release();
        return sbb;
    }

    public static ShadeSeparatedBufferedData getCopycatBuffer(ICopycatBlockEntity be) {
        BlockRenderDispatcher dispatcher = Minecraft.getInstance()
                .getBlockRenderer();
        return getCopycatBuffer(dispatcher.getBlockModel(be.getBlockState()), be, new PoseStack());
    }

    @ExpectPlatform
    public static ShadeSeparatedBufferedData getCopycatBuffer(BakedModel model, ICopycatBlockEntity be, PoseStack ms) {
        return null;
    }
}
