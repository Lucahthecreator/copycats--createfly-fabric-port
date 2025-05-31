package com.copycatsplus.copycats.foundation.copycat.model.kinetic.fabric;

import com.copycatsplus.copycats.foundation.copycat.ICopycatBlockEntity;
import com.copycatsplus.copycats.foundation.copycat.model.kinetic.WrappedRenderWorld;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.engine_room.flywheel.api.model.Model;
import dev.engine_room.flywheel.lib.model.baked.BakedModelBuilder;
import net.createmod.catnip.render.SuperByteBuffer;
import net.fabricmc.fabric.api.rendering.data.v1.RenderAttachedBlockView;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class KineticCopycatRendererImpl {
    public static SuperByteBuffer renderBuffer(BakedModel model, ICopycatBlockEntity be, PoseStack ms) {
        return new BakedModelWithDataBuilder(model)
                .withRenderWorld(new WrappedRenderWorldFabric(be))
                .withRenderPos(be.getBlockPos())
                .withReferenceState(be.getBlockState())
                .withPoseStack(ms)
                .build();
    }

    public static Model instancedModel(BakedModel model, ICopycatBlockEntity be) {
        return BakedModelBuilder.create(model)
                .level(new WrappedRenderWorldFabric(be))
                .pos(be.getBlockPos())
                .build();
    }

    @SuppressWarnings("deprecation")
    private static class WrappedRenderWorldFabric extends WrappedRenderWorld implements RenderAttachedBlockView {
        public WrappedRenderWorldFabric(ICopycatBlockEntity be) {
            super(be);
        }

        @Override
        @Nullable
        public Object getBlockEntityRenderAttachment(@NotNull BlockPos pos) {
            return ((RenderAttachedBlockView) level).getBlockEntityRenderAttachment(pos);
        }
    }
}
