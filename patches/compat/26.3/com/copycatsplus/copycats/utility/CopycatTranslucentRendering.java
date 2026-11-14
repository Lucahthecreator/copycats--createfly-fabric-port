package com.copycatsplus.copycats.utility;

import com.mojang.blaze3d.vertex.PoseStack;
import com.zurrtum.create.client.catnip.render.SuperByteBufferRenderState;
import net.minecraft.client.renderer.OrderedSubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.world.level.block.state.BlockState;

/** Submits translucent animated Copycat geometry in the terrain transparency phase. */
public final class CopycatTranslucentRendering {
    private static final ThreadLocal<Boolean> LATE_COPYCAT = ThreadLocal.withInitial(() -> false);

    private CopycatTranslucentRendering() {
    }

    public static boolean isLateCopycat() {
        return LATE_COPYCAT.get();
    }

    public static boolean isTranslucent(BlockState material) {
        return (Minecraft.getInstance().getModelManager().getBlockStateModelSet().get(material)
                .materialFlags() & BakedQuad.FLAG_TRANSLUCENT) != 0;
    }

    public static void submit(SuperByteBufferRenderState model, boolean translucent, PoseStack pose, OrderedSubmitNodeCollector collector) {
        if (!translucent) {
            model.submit(pose, collector);
            return;
        }
        LATE_COPYCAT.set(true);
        try {
            model.submit(RenderTypes.translucentMovingBlock(), pose, collector);
        } finally {
            LATE_COPYCAT.remove();
        }
    }
}
