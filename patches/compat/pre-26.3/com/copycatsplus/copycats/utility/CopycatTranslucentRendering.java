package com.copycatsplus.copycats.utility;

import com.mojang.blaze3d.vertex.PoseStack;
import com.zurrtum.create.client.catnip.render.SuperByteBufferRenderState;
import net.minecraft.client.renderer.OrderedSubmitNodeCollector;
import net.minecraft.world.level.block.state.BlockState;

public final class CopycatTranslucentRendering {
    private CopycatTranslucentRendering() {
    }

    public static boolean isTranslucent(BlockState material) {
        return false;
    }

    public static void submit(SuperByteBufferRenderState model, boolean translucent, PoseStack pose, OrderedSubmitNodeCollector collector) {
        model.submit(pose, collector);
    }
}
