/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Direction
 *  net.minecraft.world.level.block.state.BlockState
 */
package com.copycatsplus.copycats.content.copycat.vertical_step;

import com.copycatsplus.copycats.content.copycat.vertical_step.CopycatVerticalStepBlock;
import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.AssemblyTransform;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableCullFace;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public class CopycatVerticalStepModelCore
extends CopycatModelCore {
    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
        Direction facing = (Direction)state.getValue(CopycatVerticalStepBlock.FACING);
        AssemblyTransform transform = t -> t.rotateY((int)facing.toYRot());
        context.assemblePiece(transform, CopycatRenderContext.vec3(8.0, 0.0, 8.0), CopycatRenderContext.aabb(4.0, 16.0, 4.0), CopycatRenderContext.cull(MutableCullFace.EAST | MutableCullFace.SOUTH));
        context.assemblePiece(transform, CopycatRenderContext.vec3(12.0, 0.0, 8.0), CopycatRenderContext.aabb(4.0, 16.0, 4.0).move(12.0, 0.0, 0.0), CopycatRenderContext.cull(MutableCullFace.WEST | MutableCullFace.SOUTH));
        context.assemblePiece(transform, CopycatRenderContext.vec3(8.0, 0.0, 12.0), CopycatRenderContext.aabb(4.0, 16.0, 4.0).move(0.0, 0.0, 12.0), CopycatRenderContext.cull(MutableCullFace.EAST | MutableCullFace.NORTH));
        context.assemblePiece(transform, CopycatRenderContext.vec3(12.0, 0.0, 12.0), CopycatRenderContext.aabb(4.0, 16.0, 4.0).move(12.0, 0.0, 12.0), CopycatRenderContext.cull(MutableCullFace.WEST | MutableCullFace.NORTH));
    }
}

