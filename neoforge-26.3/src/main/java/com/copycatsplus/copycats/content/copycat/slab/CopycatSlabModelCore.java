package com.copycatsplus.copycats.content.copycat.slab;

import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.AssemblyTransform;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableCullFace;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;

public class CopycatSlabModelCore extends CopycatModelCore {
    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
        Direction facing = state.getOptionalValue(CopycatSlabBlock.SLAB_TYPE).isPresent() ? CopycatSlabBlock.getApparentDirection(state) : Direction.UP;
        boolean isDouble = state.getOptionalValue(CopycatSlabBlock.SLAB_TYPE).orElse(SlabType.BOTTOM) == SlabType.DOUBLE;
        assembleSlab(context, facing);
        if (isDouble) {
            assembleSlab(context, facing.getOpposite());
        }
    }

    private static void assembleSlab(CopycatRenderContext context, Direction facing) {
        if (facing.getAxis().isHorizontal()) {
            AssemblyTransform transform = t -> t.rotateY((int)facing.toYRot());
            context.assemblePiece(
                transform,
                CopycatRenderContext.vec3(0.0, 0.0, 0.0),
                CopycatRenderContext.aabb(16.0, 16.0, 4.0),
                CopycatRenderContext.cull(MutableCullFace.SOUTH)
            );
            context.assemblePiece(
                transform,
                CopycatRenderContext.vec3(0.0, 0.0, 4.0),
                CopycatRenderContext.aabb(16.0, 16.0, 4.0).move(0.0, 0.0, 12.0),
                CopycatRenderContext.cull(MutableCullFace.NORTH)
            );
        } else {
            AssemblyTransform transform = t -> t.flipY(facing.getAxisDirection() == AxisDirection.NEGATIVE);
            context.assemblePiece(
                transform, CopycatRenderContext.vec3(0.0, 0.0, 0.0), CopycatRenderContext.aabb(16.0, 4.0, 16.0), CopycatRenderContext.cull(MutableCullFace.UP)
            );
            context.assemblePiece(
                transform,
                CopycatRenderContext.vec3(0.0, 4.0, 0.0),
                CopycatRenderContext.aabb(16.0, 4.0, 16.0).move(0.0, 12.0, 0.0),
                CopycatRenderContext.cull(MutableCullFace.DOWN)
            );
        }
    }
}
