package com.copycatsplus.copycats.content.copycat.vertical_half_layer;

import com.copycatsplus.copycats.CCBlocks;
import com.copycatsplus.copycats.content.copycat.half_layer.CopycatHalfLayerBlock;
import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.AssemblyTransform;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableCullFace;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.quad.QuadAutoCull;
import java.util.List;
import java.util.Objects;
import net.minecraft.world.level.block.state.BlockState;

public class CopycatVerticalMultiHalfLayerModelCore extends CopycatModelCore {
    @Override
    public void registerModels(List<CopycatModelCore.ModelEntry> entries) {
        this.registerForMultiState(entries, CCBlocks.COPYCAT_VERTICAL_HALF_LAYER.get(), false);
    }

    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
        if (!Objects.equals(key, CopycatHalfLayerBlock.POSITIVE_LAYERS.getName()) || state.getValue(CopycatHalfLayerBlock.POSITIVE_LAYERS) != 0) {
            if (!Objects.equals(key, CopycatHalfLayerBlock.NEGATIVE_LAYERS.getName()) || state.getValue(CopycatHalfLayerBlock.NEGATIVE_LAYERS) != 0) {
                int rot = (int)state.getValue(CopycatVerticalHalfLayerBlock.FACING).toYRot();
                boolean positive = key.equals(CopycatHalfLayerBlock.POSITIVE_LAYERS.getName());
                int layer = state.getValue(positive ? CopycatHalfLayerBlock.POSITIVE_LAYERS : CopycatHalfLayerBlock.NEGATIVE_LAYERS);
                if (layer != 0) {
                    AssemblyTransform transform = t -> t.flipX(!positive).rotateY(rot + 180);
                    QuadAutoCull autoCull = CopycatRenderContext.autoCull(CopycatRenderContext.aabb(8.0, 16.0, 16.0));
                    context.assemblePiece(
                        transform,
                        CopycatRenderContext.vec3(0.0, 0.0, 0.0),
                        CopycatRenderContext.aabb(4.0, 16.0, layer),
                        CopycatRenderContext.cull(MutableCullFace.EAST | MutableCullFace.SOUTH),
                        autoCull
                    );
                    context.assemblePiece(
                        transform,
                        CopycatRenderContext.vec3(0.0, 0.0, layer),
                        CopycatRenderContext.aabb(4.0, 16.0, layer).move(0.0, 0.0, 16 - layer),
                        CopycatRenderContext.cull(MutableCullFace.EAST | MutableCullFace.NORTH),
                        autoCull
                    );
                    context.assemblePiece(
                        transform,
                        CopycatRenderContext.vec3(4.0, 0.0, 0.0),
                        CopycatRenderContext.aabb(4.0, 16.0, layer).move(12.0, 0.0, 0.0),
                        CopycatRenderContext.cull(MutableCullFace.WEST | MutableCullFace.SOUTH),
                        autoCull
                    );
                    context.assemblePiece(
                        transform,
                        CopycatRenderContext.vec3(4.0, 0.0, layer),
                        CopycatRenderContext.aabb(4.0, 16.0, layer).move(12.0, 0.0, 16 - layer),
                        CopycatRenderContext.cull(MutableCullFace.WEST | MutableCullFace.NORTH),
                        autoCull
                    );
                }
            }
        }
    }
}
