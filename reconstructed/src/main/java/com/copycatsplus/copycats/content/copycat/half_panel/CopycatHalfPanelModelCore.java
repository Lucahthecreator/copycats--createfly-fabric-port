/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Direction
 *  net.minecraft.core.Direction$AxisDirection
 *  net.minecraft.world.level.block.state.BlockState
 */
package com.copycatsplus.copycats.content.copycat.half_panel;

import com.copycatsplus.copycats.content.copycat.half_panel.CopycatHalfPanelBlock;
import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.AssemblyTransform;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableCullFace;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public class CopycatHalfPanelModelCore
extends CopycatModelCore {
    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
        Direction facing = (Direction)state.getValue(CopycatHalfPanelBlock.FACING);
        Direction offset = (Direction)state.getValue(CopycatHalfPanelBlock.OFFSET);
        if (facing.getAxis().isVertical()) {
            boolean flipY = facing == Direction.UP;
            int rot = (int)offset.toYRot();
            AssemblyTransform transform = t -> t.rotateY(rot).flipY(flipY);
            context.assemblePiece(transform, CopycatRenderContext.vec3(0.0, 0.0, 12.0), CopycatRenderContext.aabb(16.0, 1.0, 4.0).move(0.0, 0.0, 12.0), CopycatRenderContext.cull(MutableCullFace.UP | MutableCullFace.NORTH));
            context.assemblePiece(transform, CopycatRenderContext.vec3(0.0, 0.0, 8.0), CopycatRenderContext.aabb(16.0, 1.0, 4.0).move(0.0, 0.0, 0.0), CopycatRenderContext.cull(MutableCullFace.UP | MutableCullFace.SOUTH));
            context.assemblePiece(transform, CopycatRenderContext.vec3(0.0, 1.0, 12.0), CopycatRenderContext.aabb(16.0, 2.0, 4.0).move(0.0, 14.0, 12.0), CopycatRenderContext.cull(MutableCullFace.DOWN | MutableCullFace.NORTH));
            context.assemblePiece(transform, CopycatRenderContext.vec3(0.0, 1.0, 8.0), CopycatRenderContext.aabb(16.0, 2.0, 4.0).move(0.0, 14.0, 0.0), CopycatRenderContext.cull(MutableCullFace.DOWN | MutableCullFace.SOUTH));
        } else if (offset.getAxis() == facing.getAxis()) {
            boolean flipY = offset.getAxisDirection() == Direction.AxisDirection.POSITIVE;
            int rot = (int)facing.toYRot();
            AssemblyTransform transform = t -> t.rotateY(rot).flipY(flipY);
            context.assemblePiece(transform, CopycatRenderContext.vec3(0.0, 0.0, 15.0), CopycatRenderContext.aabb(16.0, 4.0, 1.0).move(0.0, 0.0, 15.0), CopycatRenderContext.cull(MutableCullFace.UP | MutableCullFace.NORTH));
            context.assemblePiece(transform, CopycatRenderContext.vec3(0.0, 4.0, 15.0), CopycatRenderContext.aabb(16.0, 4.0, 1.0).move(0.0, 12.0, 15.0), CopycatRenderContext.cull(MutableCullFace.DOWN | MutableCullFace.NORTH));
            context.assemblePiece(transform, CopycatRenderContext.vec3(0.0, 0.0, 13.0), CopycatRenderContext.aabb(16.0, 4.0, 2.0).move(0.0, 0.0, 0.0), CopycatRenderContext.cull(MutableCullFace.UP | MutableCullFace.SOUTH));
            context.assemblePiece(transform, CopycatRenderContext.vec3(0.0, 4.0, 13.0), CopycatRenderContext.aabb(16.0, 4.0, 2.0).move(0.0, 12.0, 0.0), CopycatRenderContext.cull(MutableCullFace.DOWN | MutableCullFace.SOUTH));
        } else {
            int leftOffset = offset == facing.getCounterClockWise() ? 8 : 0;
            int rot = (int)facing.toYRot();
            AssemblyTransform transform = t -> t.rotateY(rot);
            context.assemblePiece(transform, CopycatRenderContext.vec3(leftOffset, 0.0, 15.0), CopycatRenderContext.aabb(4.0, 16.0, 1.0).move(0.0, 0.0, 15.0), CopycatRenderContext.cull(MutableCullFace.EAST | MutableCullFace.NORTH));
            context.assemblePiece(transform, CopycatRenderContext.vec3(4 + leftOffset, 0.0, 15.0), CopycatRenderContext.aabb(4.0, 16.0, 1.0).move(12.0, 0.0, 15.0), CopycatRenderContext.cull(MutableCullFace.WEST | MutableCullFace.NORTH));
            context.assemblePiece(transform, CopycatRenderContext.vec3(leftOffset, 0.0, 13.0), CopycatRenderContext.aabb(4.0, 16.0, 2.0).move(0.0, 0.0, 0.0), CopycatRenderContext.cull(MutableCullFace.EAST | MutableCullFace.SOUTH));
            context.assemblePiece(transform, CopycatRenderContext.vec3(4 + leftOffset, 0.0, 13.0), CopycatRenderContext.aabb(4.0, 16.0, 2.0).move(12.0, 0.0, 0.0), CopycatRenderContext.cull(MutableCullFace.WEST | MutableCullFace.SOUTH));
        }
    }
}

