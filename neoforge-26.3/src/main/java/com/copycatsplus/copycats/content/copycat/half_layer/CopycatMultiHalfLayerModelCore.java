package com.copycatsplus.copycats.content.copycat.half_layer;

import com.copycatsplus.copycats.CCBlocks;
import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.AssemblyTransform;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableCullFace;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.quad.QuadAutoCull;
import java.util.List;
import java.util.Objects;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;

public class CopycatMultiHalfLayerModelCore extends CopycatModelCore {
    @Override
    public void registerModels(List<CopycatModelCore.ModelEntry> entries) {
        this.registerForMultiState(entries, CCBlocks.COPYCAT_HALF_LAYER.get(), false);
    }

    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
        if (!Objects.equals(key, CopycatHalfLayerBlock.NEGATIVE_LAYERS.getName()) || state.getValue(CopycatHalfLayerBlock.NEGATIVE_LAYERS) != 0) {
            if (!Objects.equals(key, CopycatHalfLayerBlock.POSITIVE_LAYERS.getName()) || state.getValue(CopycatHalfLayerBlock.POSITIVE_LAYERS) != 0) {
                boolean flipY = state.getValue(CopycatHalfLayerBlock.HALF) == Half.TOP;
                int rot = state.getValue(CopycatHalfLayerBlock.AXIS) == Axis.X ? 0 : 90;
                boolean positive = key.equals(CopycatHalfLayerBlock.POSITIVE_LAYERS.getName());
                int layer = state.getValue(positive ? CopycatHalfLayerBlock.POSITIVE_LAYERS : CopycatHalfLayerBlock.NEGATIVE_LAYERS);
                if (layer != 0) {
                    AssemblyTransform transform = t -> t.rotateY(rot + (positive ? 180 : 0)).flipY(flipY);
                    QuadAutoCull autoCull = CopycatRenderContext.autoCull(CopycatRenderContext.aabb(8.0, 16.0, 16.0));
                    context.assemblePiece(
                        transform,
                        CopycatRenderContext.vec3(0.0, 0.0, 0.0),
                        CopycatRenderContext.aabb(4.0, layer, 16.0),
                        CopycatRenderContext.cull(MutableCullFace.EAST | MutableCullFace.UP),
                        autoCull
                    );
                    context.assemblePiece(
                        transform,
                        CopycatRenderContext.vec3(0.0, layer, 0.0),
                        CopycatRenderContext.aabb(4.0, layer, 16.0).move(0.0, 16 - layer, 0.0),
                        CopycatRenderContext.cull(MutableCullFace.EAST | MutableCullFace.DOWN),
                        autoCull
                    );
                    context.assemblePiece(
                        transform,
                        CopycatRenderContext.vec3(4.0, 0.0, 0.0),
                        CopycatRenderContext.aabb(4.0, layer, 16.0).move(12.0, 0.0, 0.0),
                        CopycatRenderContext.cull(MutableCullFace.WEST | MutableCullFace.UP),
                        autoCull
                    );
                    context.assemblePiece(
                        transform,
                        CopycatRenderContext.vec3(4.0, layer, 0.0),
                        CopycatRenderContext.aabb(4.0, layer, 16.0).move(12.0, 16 - layer, 0.0),
                        CopycatRenderContext.cull(MutableCullFace.WEST | MutableCullFace.DOWN),
                        autoCull
                    );
                }
            }
        }
    }
}
