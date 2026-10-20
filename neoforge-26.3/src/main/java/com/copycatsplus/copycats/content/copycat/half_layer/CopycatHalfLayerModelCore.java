package com.copycatsplus.copycats.content.copycat.half_layer;

import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.AssemblyTransform;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableCullFace;
import com.zurrtum.create.catnip.data.Iterate;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;

public class CopycatHalfLayerModelCore extends CopycatModelCore {
    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
        boolean flipY = state.getValue(CopycatHalfLayerBlock.HALF) == Half.TOP;
        int rot = state.getValue(CopycatHalfLayerBlock.AXIS) == Axis.X ? 0 : 90;

        for (boolean positive : Iterate.falseAndTrue) {
            int layer = state.getValue(positive ? CopycatHalfLayerBlock.POSITIVE_LAYERS : CopycatHalfLayerBlock.NEGATIVE_LAYERS);
            if (layer != 0) {
                AssemblyTransform transform = t -> t.rotateY(rot + (positive ? 180 : 0)).flipY(flipY);
                context.assemblePiece(
                    transform,
                    CopycatRenderContext.vec3(0.0, 0.0, 0.0),
                    CopycatRenderContext.aabb(4.0, layer, 16.0),
                    CopycatRenderContext.cull(MutableCullFace.EAST | MutableCullFace.UP)
                );
                context.assemblePiece(
                    transform,
                    CopycatRenderContext.vec3(0.0, layer, 0.0),
                    CopycatRenderContext.aabb(4.0, layer, 16.0).move(0.0, 16 - layer, 0.0),
                    CopycatRenderContext.cull(MutableCullFace.EAST | MutableCullFace.DOWN)
                );
                context.assemblePiece(
                    transform,
                    CopycatRenderContext.vec3(4.0, 0.0, 0.0),
                    CopycatRenderContext.aabb(4.0, layer, 16.0).move(12.0, 0.0, 0.0),
                    CopycatRenderContext.cull(MutableCullFace.WEST | MutableCullFace.UP)
                );
                context.assemblePiece(
                    transform,
                    CopycatRenderContext.vec3(4.0, layer, 0.0),
                    CopycatRenderContext.aabb(4.0, layer, 16.0).move(12.0, 16 - layer, 0.0),
                    CopycatRenderContext.cull(MutableCullFace.WEST | MutableCullFace.DOWN)
                );
            }
        }
    }
}
