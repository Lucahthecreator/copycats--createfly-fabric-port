/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Direction
 *  net.minecraft.world.level.block.StairBlock
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.properties.Half
 *  net.minecraft.world.level.block.state.properties.Property
 *  net.minecraft.world.level.block.state.properties.StairsShape
 */
package com.copycatsplus.copycats.content.copycat.stairs;

import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.AssemblyTransform;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableCullFace;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.StairsShape;

public class CopycatStairsModelCore
extends CopycatModelCore {
    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
        int facing = (int)((Direction)state.getValue((Property)StairBlock.FACING)).toYRot();
        boolean top = state.getValue((Property)StairBlock.HALF) == Half.TOP;
        StairsShape shape = (StairsShape)state.getValue((Property)StairBlock.SHAPE);
        switch (shape) {
            case STRAIGHT: {
                AssemblyTransform transform = t -> t.rotateY(facing).flipY(top);
                CopycatStairsModelCore.assembleStraight(context, transform, this.enhanced);
                break;
            }
            case INNER_LEFT: 
            case INNER_RIGHT: {
                boolean flipX = shape == StairsShape.INNER_RIGHT;
                AssemblyTransform transform = t -> t.flipX(flipX).rotateY(facing).flipY(top);
                CopycatStairsModelCore.assembleInnerLeft(context, transform, this.enhanced);
                break;
            }
            case OUTER_LEFT: 
            case OUTER_RIGHT: {
                boolean flipX = shape == StairsShape.OUTER_RIGHT;
                AssemblyTransform transform = t -> t.flipX(flipX).rotateY(facing).flipY(top);
                CopycatStairsModelCore.assembleOuterLeft(context, transform, this.enhanced);
            }
        }
    }

    public static void assembleStraight(CopycatRenderContext context, AssemblyTransform transform, boolean enhanced) {
        if (enhanced) {
            context.assemblePiece(transform, CopycatRenderContext.vec3(0.0, 4.0, 12.0), CopycatRenderContext.aabb(16.0, 12.0, 4.0).move(0.0, 4.0, 12.0), CopycatRenderContext.cull(MutableCullFace.NORTH | MutableCullFace.DOWN));
            context.assemblePiece(transform, CopycatRenderContext.vec3(0.0, 8.0, 8.0), CopycatRenderContext.aabb(16.0, 8.0, 2.0).move(0.0, 8.0, 0.0), CopycatRenderContext.cull(MutableCullFace.SOUTH | MutableCullFace.DOWN));
            context.assemblePiece(transform, CopycatRenderContext.vec3(0.0, 7.0, 8.0), CopycatRenderContext.aabb(16.0, 1.0, 2.0).move(0.0, 7.0, 0.0), CopycatRenderContext.cull(MutableCullFace.NORTH | MutableCullFace.SOUTH | MutableCullFace.UP | MutableCullFace.DOWN));
            context.assemblePiece(transform, CopycatRenderContext.vec3(0.0, 5.0, 10.0), CopycatRenderContext.aabb(16.0, 11.0, 2.0).move(0.0, 5.0, 2.0), CopycatRenderContext.cull(MutableCullFace.NORTH | MutableCullFace.SOUTH | MutableCullFace.DOWN));
            context.assemblePiece(transform, CopycatRenderContext.vec3(0.0, 0.0, 0.0), CopycatRenderContext.aabb(16.0, 4.0, 16.0).move(0.0, 0.0, 0.0), CopycatRenderContext.cull(MutableCullFace.UP));
            context.assemblePiece(transform, CopycatRenderContext.vec3(0.0, 7.0, 0.0), CopycatRenderContext.aabb(16.0, 1.0, 8.0).move(0.0, 15.0, 0.0), CopycatRenderContext.cull(MutableCullFace.SOUTH | MutableCullFace.DOWN));
            context.assemblePiece(transform, CopycatRenderContext.vec3(0.0, 5.0, 0.0), CopycatRenderContext.aabb(16.0, 2.0, 10.0).move(0.0, 13.0, 0.0), CopycatRenderContext.cull(MutableCullFace.SOUTH | MutableCullFace.UP | MutableCullFace.DOWN));
            context.assemblePiece(transform, CopycatRenderContext.vec3(0.0, 4.0, 0.0), CopycatRenderContext.aabb(16.0, 1.0, 12.0).move(0.0, 12.0, 0.0), CopycatRenderContext.cull(MutableCullFace.SOUTH | MutableCullFace.UP | MutableCullFace.DOWN));
        } else {
            context.assemblePiece(transform, CopycatRenderContext.vec3(0.0, 0.0, 0.0), CopycatRenderContext.aabb(16.0, 4.0, 8.0), CopycatRenderContext.cull(MutableCullFace.UP | MutableCullFace.SOUTH));
            context.assemblePiece(transform, CopycatRenderContext.vec3(0.0, 4.0, 0.0), CopycatRenderContext.aabb(16.0, 4.0, 8.0).move(0.0, 12.0, 0.0), CopycatRenderContext.cull(MutableCullFace.DOWN | MutableCullFace.SOUTH));
            context.assemblePiece(transform, CopycatRenderContext.vec3(0.0, 0.0, 8.0), CopycatRenderContext.aabb(16.0, 8.0, 8.0).move(0.0, 0.0, 8.0), CopycatRenderContext.cull(MutableCullFace.UP | MutableCullFace.NORTH));
            context.assemblePiece(transform, CopycatRenderContext.vec3(0.0, 8.0, 8.0), CopycatRenderContext.aabb(16.0, 8.0, 4.0).move(0.0, 8.0, 0.0), CopycatRenderContext.cull(MutableCullFace.DOWN | MutableCullFace.SOUTH));
            context.assemblePiece(transform, CopycatRenderContext.vec3(0.0, 8.0, 12.0), CopycatRenderContext.aabb(16.0, 8.0, 4.0).move(0.0, 8.0, 12.0), CopycatRenderContext.cull(MutableCullFace.DOWN | MutableCullFace.NORTH));
        }
    }

    public static void assembleInnerLeft(CopycatRenderContext context, AssemblyTransform transform, boolean enhanced) {
        if (enhanced) {
            context.assemblePiece(transform, CopycatRenderContext.vec3(0.0, 4.0, 12.0), CopycatRenderContext.aabb(16.0, 12.0, 4.0).move(0.0, 4.0, 12.0), CopycatRenderContext.cull(MutableCullFace.NORTH | MutableCullFace.DOWN));
            context.assemblePiece(transform, CopycatRenderContext.vec3(12.0, 4.0, 0.0), CopycatRenderContext.aabb(4.0, 12.0, 12.0).move(12.0, 4.0, 0.0), CopycatRenderContext.cull(MutableCullFace.SOUTH | MutableCullFace.WEST | MutableCullFace.DOWN));
            context.assemblePiece(transform, CopycatRenderContext.vec3(0.0, 0.0, 0.0), CopycatRenderContext.aabb(16.0, 4.0, 16.0).move(0.0, 0.0, 0.0), CopycatRenderContext.cull(MutableCullFace.UP));
            context.assemblePiece(transform, CopycatRenderContext.vec3(0.0, 4.0, 0.0), CopycatRenderContext.aabb(12.0, 1.0, 12.0).move(0.0, 12.0, 0.0), CopycatRenderContext.cull(MutableCullFace.EAST | MutableCullFace.SOUTH | MutableCullFace.UP | MutableCullFace.DOWN));
            context.assemblePiece(transform, CopycatRenderContext.vec3(0.0, 5.0, 0.0), CopycatRenderContext.aabb(10.0, 2.0, 10.0).move(0.0, 13.0, 0.0), CopycatRenderContext.cull(MutableCullFace.EAST | MutableCullFace.SOUTH | MutableCullFace.UP | MutableCullFace.DOWN));
            context.assemblePiece(transform, CopycatRenderContext.vec3(0.0, 7.0, 0.0), CopycatRenderContext.aabb(8.0, 1.0, 8.0).move(0.0, 15.0, 0.0), CopycatRenderContext.cull(MutableCullFace.EAST | MutableCullFace.SOUTH | MutableCullFace.DOWN));
            context.assemblePiece(transform, CopycatRenderContext.vec3(0.0, 8.0, 8.0), CopycatRenderContext.aabb(8.0, 8.0, 2.0).move(0.0, 8.0, 0.0), CopycatRenderContext.cull(MutableCullFace.EAST | MutableCullFace.SOUTH | MutableCullFace.DOWN));
            context.assemblePiece(transform, CopycatRenderContext.vec3(8.0, 8.0, 8.0), CopycatRenderContext.aabb(1.0, 8.0, 2.0).move(8.0, 8.0, 0.0), CopycatRenderContext.cull(MutableCullFace.NORTH | MutableCullFace.EAST | MutableCullFace.SOUTH | MutableCullFace.WEST | MutableCullFace.DOWN));
            context.assemblePiece(transform, CopycatRenderContext.vec3(9.0, 8.0, 8.0), CopycatRenderContext.aabb(1.0, 8.0, 2.0).move(1.0, 8.0, 8.0), CopycatRenderContext.cull(MutableCullFace.NORTH | MutableCullFace.EAST | MutableCullFace.SOUTH | MutableCullFace.WEST | MutableCullFace.DOWN));
            context.assemblePiece(transform, CopycatRenderContext.vec3(0.0, 5.0, 10.0), CopycatRenderContext.aabb(11.0, 11.0, 2.0).move(0.0, 5.0, 2.0), CopycatRenderContext.cull(MutableCullFace.NORTH | MutableCullFace.EAST | MutableCullFace.SOUTH | MutableCullFace.DOWN));
            context.assemblePiece(transform, CopycatRenderContext.vec3(0.0, 7.0, 8.0), CopycatRenderContext.aabb(10.0, 1.0, 2.0).move(0.0, 7.0, 0.0), CopycatRenderContext.cull(MutableCullFace.NORTH | MutableCullFace.EAST | MutableCullFace.SOUTH | MutableCullFace.UP | MutableCullFace.DOWN));
            context.assemblePiece(transform, CopycatRenderContext.vec3(8.0, 7.0, 0.0), CopycatRenderContext.aabb(2.0, 1.0, 8.0).move(0.0, 7.0, 0.0), CopycatRenderContext.cull(MutableCullFace.EAST | MutableCullFace.SOUTH | MutableCullFace.WEST | MutableCullFace.UP | MutableCullFace.DOWN));
            context.assemblePiece(transform, CopycatRenderContext.vec3(8.0, 8.0, 0.0), CopycatRenderContext.aabb(2.0, 8.0, 8.0).move(0.0, 8.0, 0.0), CopycatRenderContext.cull(MutableCullFace.EAST | MutableCullFace.SOUTH | MutableCullFace.DOWN));
            context.assemblePiece(transform, CopycatRenderContext.vec3(10.0, 5.0, 0.0), CopycatRenderContext.aabb(2.0, 11.0, 10.0).move(2.0, 5.0, 0.0), CopycatRenderContext.cull(MutableCullFace.EAST | MutableCullFace.SOUTH | MutableCullFace.WEST | MutableCullFace.DOWN));
            context.assemblePiece(transform, CopycatRenderContext.vec3(11.0, 5.0, 10.0), CopycatRenderContext.aabb(1.0, 11.0, 2.0).move(3.0, 5.0, 10.0), CopycatRenderContext.cull(MutableCullFace.NORTH | MutableCullFace.EAST | MutableCullFace.SOUTH | MutableCullFace.WEST | MutableCullFace.DOWN));
        } else {
            context.assemblePiece(transform, CopycatRenderContext.vec3(0.0, 0.0, 0.0), CopycatRenderContext.aabb(8.0, 4.0, 8.0), CopycatRenderContext.cull(MutableCullFace.UP | MutableCullFace.SOUTH | MutableCullFace.EAST));
            context.assemblePiece(transform, CopycatRenderContext.vec3(0.0, 4.0, 0.0), CopycatRenderContext.aabb(8.0, 4.0, 8.0).move(0.0, 12.0, 0.0), CopycatRenderContext.cull(MutableCullFace.DOWN | MutableCullFace.SOUTH | MutableCullFace.EAST));
            context.assemblePiece(transform, CopycatRenderContext.vec3(0.0, 0.0, 8.0), CopycatRenderContext.aabb(16.0, 8.0, 8.0).move(0.0, 0.0, 8.0), CopycatRenderContext.cull(MutableCullFace.UP | MutableCullFace.NORTH));
            context.assemblePiece(transform, CopycatRenderContext.vec3(8.0, 8.0, 8.0), CopycatRenderContext.aabb(8.0, 8.0, 8.0).move(8.0, 8.0, 8.0), CopycatRenderContext.cull(MutableCullFace.DOWN | MutableCullFace.NORTH | MutableCullFace.WEST));
            context.assemblePiece(transform, CopycatRenderContext.vec3(0.0, 8.0, 12.0), CopycatRenderContext.aabb(8.0, 8.0, 4.0).move(0.0, 8.0, 12.0), CopycatRenderContext.cull(MutableCullFace.DOWN | MutableCullFace.NORTH | MutableCullFace.EAST));
            context.assemblePiece(transform, CopycatRenderContext.vec3(0.0, 8.0, 8.0), CopycatRenderContext.aabb(8.0, 8.0, 4.0).move(0.0, 8.0, 0.0), CopycatRenderContext.cull(MutableCullFace.DOWN | MutableCullFace.SOUTH | MutableCullFace.EAST));
            context.assemblePiece(transform, CopycatRenderContext.vec3(12.0, 8.0, 0.0), CopycatRenderContext.aabb(4.0, 8.0, 8.0).move(12.0, 8.0, 0.0), CopycatRenderContext.cull(MutableCullFace.DOWN | MutableCullFace.SOUTH | MutableCullFace.WEST));
            context.assemblePiece(transform, CopycatRenderContext.vec3(8.0, 8.0, 0.0), CopycatRenderContext.aabb(4.0, 8.0, 8.0).move(0.0, 8.0, 0.0), CopycatRenderContext.cull(MutableCullFace.DOWN | MutableCullFace.SOUTH | MutableCullFace.EAST));
            context.assemblePiece(transform, CopycatRenderContext.vec3(8.0, 0.0, 0.0), CopycatRenderContext.aabb(8.0, 8.0, 8.0).move(8.0, 0.0, 0.0), CopycatRenderContext.cull(MutableCullFace.UP | MutableCullFace.SOUTH | MutableCullFace.WEST));
        }
    }

    public static void assembleOuterLeft(CopycatRenderContext context, AssemblyTransform transform, boolean enhanced) {
        if (enhanced) {
            context.assemblePiece(transform, CopycatRenderContext.vec3(12.0, 4.0, 12.0), CopycatRenderContext.aabb(4.0, 12.0, 4.0).move(12.0, 4.0, 12.0), CopycatRenderContext.cull(MutableCullFace.NORTH | MutableCullFace.WEST | MutableCullFace.DOWN));
            context.assemblePiece(transform, CopycatRenderContext.vec3(12.0, 8.0, 8.0), CopycatRenderContext.aabb(4.0, 8.0, 1.0).move(12.0, 8.0, 0.0), CopycatRenderContext.cull(MutableCullFace.SOUTH | MutableCullFace.WEST | MutableCullFace.DOWN));
            context.assemblePiece(transform, CopycatRenderContext.vec3(12.0, 6.0, 9.0), CopycatRenderContext.aabb(4.0, 10.0, 2.0).move(12.0, 6.0, 1.0), CopycatRenderContext.cull(MutableCullFace.NORTH | MutableCullFace.SOUTH | MutableCullFace.WEST | MutableCullFace.DOWN));
            context.assemblePiece(transform, CopycatRenderContext.vec3(12.0, 4.0, 11.0), CopycatRenderContext.aabb(4.0, 12.0, 1.0).move(12.0, 4.0, 3.0), CopycatRenderContext.cull(MutableCullFace.NORTH | MutableCullFace.SOUTH | MutableCullFace.WEST | MutableCullFace.DOWN));
            context.assemblePiece(transform, CopycatRenderContext.vec3(8.0, 8.0, 12.0), CopycatRenderContext.aabb(1.0, 8.0, 4.0).move(0.0, 8.0, 12.0), CopycatRenderContext.cull(MutableCullFace.NORTH | MutableCullFace.EAST | MutableCullFace.DOWN));
            context.assemblePiece(transform, CopycatRenderContext.vec3(9.0, 6.0, 12.0), CopycatRenderContext.aabb(2.0, 10.0, 4.0).move(1.0, 6.0, 12.0), CopycatRenderContext.cull(MutableCullFace.NORTH | MutableCullFace.EAST | MutableCullFace.WEST | MutableCullFace.DOWN));
            context.assemblePiece(transform, CopycatRenderContext.vec3(11.0, 4.0, 12.0), CopycatRenderContext.aabb(1.0, 12.0, 4.0).move(3.0, 4.0, 12.0), CopycatRenderContext.cull(MutableCullFace.NORTH | MutableCullFace.EAST | MutableCullFace.WEST | MutableCullFace.DOWN));
            context.assemblePiece(transform, CopycatRenderContext.vec3(8.0, 8.0, 8.0), CopycatRenderContext.aabb(4.0, 8.0, 4.0).move(0.0, 8.0, 0.0), CopycatRenderContext.cull(MutableCullFace.EAST | MutableCullFace.SOUTH | MutableCullFace.DOWN));
            context.assemblePiece(transform, CopycatRenderContext.vec3(0.0, 0.0, 0.0), CopycatRenderContext.aabb(16.0, 4.0, 16.0).move(0.0, 0.0, 0.0), CopycatRenderContext.cull(MutableCullFace.UP));
            context.assemblePiece(transform, CopycatRenderContext.vec3(0.0, 4.0, 0.0), CopycatRenderContext.aabb(8.0, 4.0, 8.0).move(0.0, 12.0, 0.0), CopycatRenderContext.cull(MutableCullFace.EAST | MutableCullFace.SOUTH | MutableCullFace.DOWN));
            context.assemblePiece(transform, CopycatRenderContext.vec3(8.0, 6.0, 0.0), CopycatRenderContext.aabb(8.0, 2.0, 8.0).move(8.0, 14.0, 0.0), CopycatRenderContext.cull(MutableCullFace.SOUTH | MutableCullFace.WEST | MutableCullFace.DOWN));
            context.assemblePiece(transform, CopycatRenderContext.vec3(8.0, 4.0, 0.0), CopycatRenderContext.aabb(8.0, 2.0, 11.0).move(8.0, 12.0, 0.0), CopycatRenderContext.cull(MutableCullFace.SOUTH | MutableCullFace.WEST | MutableCullFace.UP | MutableCullFace.DOWN));
            context.assemblePiece(transform, CopycatRenderContext.vec3(8.0, 6.0, 8.0), CopycatRenderContext.aabb(8.0, 2.0, 1.0).move(8.0, 14.0, 8.0), CopycatRenderContext.cull(MutableCullFace.NORTH | MutableCullFace.SOUTH | MutableCullFace.WEST | MutableCullFace.UP | MutableCullFace.DOWN));
            context.assemblePiece(transform, CopycatRenderContext.vec3(0.0, 6.0, 8.0), CopycatRenderContext.aabb(8.0, 2.0, 8.0).move(0.0, 14.0, 8.0), CopycatRenderContext.cull(MutableCullFace.NORTH | MutableCullFace.EAST | MutableCullFace.DOWN));
            context.assemblePiece(transform, CopycatRenderContext.vec3(0.0, 4.0, 8.0), CopycatRenderContext.aabb(11.0, 2.0, 8.0).move(0.0, 12.0, 8.0), CopycatRenderContext.cull(MutableCullFace.NORTH | MutableCullFace.EAST | MutableCullFace.UP | MutableCullFace.DOWN));
            context.assemblePiece(transform, CopycatRenderContext.vec3(8.0, 6.0, 8.0), CopycatRenderContext.aabb(1.0, 2.0, 8.0).move(8.0, 14.0, 8.0), CopycatRenderContext.cull(MutableCullFace.NORTH | MutableCullFace.EAST | MutableCullFace.WEST | MutableCullFace.UP | MutableCullFace.DOWN));
        } else {
            context.assemblePiece(transform, CopycatRenderContext.vec3(0.0, 0.0, 0.0), CopycatRenderContext.aabb(8.0, 4.0, 16.0).move(0.0, 0.0, 0.0), CopycatRenderContext.cull(MutableCullFace.UP | MutableCullFace.EAST));
            context.assemblePiece(transform, CopycatRenderContext.vec3(0.0, 4.0, 0.0), CopycatRenderContext.aabb(8.0, 4.0, 16.0).move(0.0, 12.0, 0.0), CopycatRenderContext.cull(MutableCullFace.DOWN | MutableCullFace.EAST));
            context.assemblePiece(transform, CopycatRenderContext.vec3(8.0, 0.0, 0.0), CopycatRenderContext.aabb(8.0, 4.0, 8.0).move(8.0, 0.0, 0.0), CopycatRenderContext.cull(MutableCullFace.UP | MutableCullFace.SOUTH | MutableCullFace.WEST));
            context.assemblePiece(transform, CopycatRenderContext.vec3(8.0, 4.0, 0.0), CopycatRenderContext.aabb(8.0, 4.0, 8.0).move(8.0, 12.0, 0.0), CopycatRenderContext.cull(MutableCullFace.DOWN | MutableCullFace.SOUTH | MutableCullFace.WEST));
            context.assemblePiece(transform, CopycatRenderContext.vec3(8.0, 0.0, 8.0), CopycatRenderContext.aabb(8.0, 8.0, 8.0).move(8.0, 0.0, 8.0), CopycatRenderContext.cull(MutableCullFace.UP | MutableCullFace.NORTH | MutableCullFace.WEST));
            context.assemblePiece(transform, CopycatRenderContext.vec3(12.0, 8.0, 12.0), CopycatRenderContext.aabb(4.0, 8.0, 4.0).move(12.0, 8.0, 12.0), CopycatRenderContext.cull(MutableCullFace.DOWN | MutableCullFace.NORTH | MutableCullFace.WEST));
            context.assemblePiece(transform, CopycatRenderContext.vec3(8.0, 8.0, 12.0), CopycatRenderContext.aabb(4.0, 8.0, 4.0).move(0.0, 8.0, 12.0), CopycatRenderContext.cull(MutableCullFace.DOWN | MutableCullFace.NORTH | MutableCullFace.EAST));
            context.assemblePiece(transform, CopycatRenderContext.vec3(12.0, 8.0, 8.0), CopycatRenderContext.aabb(4.0, 8.0, 4.0).move(12.0, 8.0, 0.0), CopycatRenderContext.cull(MutableCullFace.DOWN | MutableCullFace.SOUTH | MutableCullFace.WEST));
            context.assemblePiece(transform, CopycatRenderContext.vec3(8.0, 8.0, 8.0), CopycatRenderContext.aabb(4.0, 8.0, 4.0).move(0.0, 8.0, 0.0), CopycatRenderContext.cull(MutableCullFace.DOWN | MutableCullFace.SOUTH | MutableCullFace.EAST));
        }
    }
}

