package com.copycatsplus.copycats.foundation.copycat.model.kinetic.fabric;

import com.copycatsplus.copycats.foundation.copycat.ICopycatBlockEntity;
import com.copycatsplus.copycats.foundation.copycat.model.kinetic.NonInstancedRenderWorld;
import com.mojang.blaze3d.vertex.PoseStack;
import net.createmod.catnip.render.SuperByteBuffer;
import net.fabricmc.fabric.api.rendering.data.v1.RenderAttachedBlockView;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class KineticCopycatRendererImpl {
    public static SuperByteBuffer getCopycatBuffer(BakedModel model, ICopycatBlockEntity be, PoseStack ms) {
        return new BakedModelWithDataBuilder(model)
                .withRenderWorld(new NonInstancedRenderWorldFabric(be))
                .withRenderPos(be.getBlockPos())
                .withReferenceState(be.getBlockState())
                .withPoseStack(ms)
                .build();
    }

    @SuppressWarnings("deprecation")
    private static class NonInstancedRenderWorldFabric extends NonInstancedRenderWorld implements RenderAttachedBlockView {
        public NonInstancedRenderWorldFabric(ICopycatBlockEntity be) {
            super(be);
        }

        @Override
        @Nullable
        public Object getBlockEntityRenderAttachment(@NotNull BlockPos pos) {
            return ((RenderAttachedBlockView) level).getBlockEntityRenderAttachment(pos);
        }
    }
}
