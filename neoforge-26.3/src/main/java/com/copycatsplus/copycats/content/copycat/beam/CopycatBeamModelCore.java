package com.copycatsplus.copycats.content.copycat.beam;

import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.AssemblyTransform;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableCullFace;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.level.block.state.BlockState;

public class CopycatBeamModelCore extends CopycatModelCore {
    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
        Axis axis = state.getValue(CopycatBeamBlock.AXIS);
        AssemblyTransform transform = t -> t.rotateX(axis == Axis.Y ? 90 : 0).rotateY(axis == Axis.X ? 90 : 0);
        context.assemblePiece(
            transform,
            CopycatRenderContext.vec3(4.0, 4.0, 0.0),
            CopycatRenderContext.aabb(4.0, 4.0, 16.0),
            CopycatRenderContext.cull(MutableCullFace.UP | MutableCullFace.EAST)
        );
        context.assemblePiece(
            transform,
            CopycatRenderContext.vec3(8.0, 4.0, 0.0),
            CopycatRenderContext.aabb(4.0, 4.0, 16.0).move(12.0, 0.0, 0.0),
            CopycatRenderContext.cull(MutableCullFace.UP | MutableCullFace.WEST)
        );
        context.assemblePiece(
            transform,
            CopycatRenderContext.vec3(4.0, 8.0, 0.0),
            CopycatRenderContext.aabb(4.0, 4.0, 16.0).move(0.0, 12.0, 0.0),
            CopycatRenderContext.cull(MutableCullFace.DOWN | MutableCullFace.EAST)
        );
        context.assemblePiece(
            transform,
            CopycatRenderContext.vec3(8.0, 8.0, 0.0),
            CopycatRenderContext.aabb(4.0, 4.0, 16.0).move(12.0, 12.0, 0.0),
            CopycatRenderContext.cull(MutableCullFace.DOWN | MutableCullFace.WEST)
        );
    }
}
