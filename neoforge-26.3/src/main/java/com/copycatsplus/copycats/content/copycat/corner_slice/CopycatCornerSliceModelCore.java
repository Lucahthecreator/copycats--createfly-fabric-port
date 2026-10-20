package com.copycatsplus.copycats.content.copycat.corner_slice;

import com.copycatsplus.copycats.content.copycat.slice.CopycatSliceBlock;
import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.AssemblyTransform;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableCullFace;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;

public class CopycatCornerSliceModelCore extends CopycatModelCore {
    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
        boolean flipY = state.getValue(CopycatSliceBlock.HALF) == Half.TOP;
        int rot = (int)state.getValue(CopycatSliceBlock.FACING).toYRot();
        int layers = state.getValue(CopycatSliceBlock.LAYERS);
        AssemblyTransform transform = t -> t.rotateY(rot).flipY(flipY);
        context.assemblePiece(
            transform,
            CopycatRenderContext.vec3(16 - layers, 0.0, 16 - layers),
            CopycatRenderContext.aabb(layers, layers, layers).move(16 - layers, 0.0, 16 - layers),
            CopycatRenderContext.cull(MutableCullFace.UP | MutableCullFace.NORTH | MutableCullFace.WEST)
        );
        context.assemblePiece(
            transform,
            CopycatRenderContext.vec3(16 - layers * 2, 0.0, 16 - layers),
            CopycatRenderContext.aabb(layers, layers, layers).move(0.0, 0.0, 16 - layers),
            CopycatRenderContext.cull(MutableCullFace.UP | MutableCullFace.NORTH | MutableCullFace.EAST)
        );
        context.assemblePiece(
            transform,
            CopycatRenderContext.vec3(16 - layers, 0.0, 16 - layers * 2),
            CopycatRenderContext.aabb(layers, layers, layers).move(16 - layers, 0.0, 0.0),
            CopycatRenderContext.cull(MutableCullFace.UP | MutableCullFace.SOUTH | MutableCullFace.WEST)
        );
        context.assemblePiece(
            transform,
            CopycatRenderContext.vec3(16 - layers * 2, 0.0, 16 - layers * 2),
            CopycatRenderContext.aabb(layers, layers, layers).move(0.0, 0.0, 0.0),
            CopycatRenderContext.cull(MutableCullFace.UP | MutableCullFace.SOUTH | MutableCullFace.EAST)
        );
        context.assemblePiece(
            transform,
            CopycatRenderContext.vec3(16 - layers, layers, 16 - layers),
            CopycatRenderContext.aabb(layers, layers, layers).move(16 - layers, 16 - layers, 16 - layers),
            CopycatRenderContext.cull(MutableCullFace.DOWN | MutableCullFace.NORTH | MutableCullFace.WEST)
        );
        context.assemblePiece(
            transform,
            CopycatRenderContext.vec3(16 - layers * 2, layers, 16 - layers),
            CopycatRenderContext.aabb(layers, layers, layers).move(0.0, 16 - layers, 16 - layers),
            CopycatRenderContext.cull(MutableCullFace.DOWN | MutableCullFace.NORTH | MutableCullFace.EAST)
        );
        context.assemblePiece(
            transform,
            CopycatRenderContext.vec3(16 - layers, layers, 16 - layers * 2),
            CopycatRenderContext.aabb(layers, layers, layers).move(16 - layers, 16 - layers, 0.0),
            CopycatRenderContext.cull(MutableCullFace.DOWN | MutableCullFace.SOUTH | MutableCullFace.WEST)
        );
        context.assemblePiece(
            transform,
            CopycatRenderContext.vec3(16 - layers * 2, layers, 16 - layers * 2),
            CopycatRenderContext.aabb(layers, layers, layers).move(0.0, 16 - layers, 0.0),
            CopycatRenderContext.cull(MutableCullFace.DOWN | MutableCullFace.SOUTH | MutableCullFace.EAST)
        );
    }
}
