/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Direction
 *  net.minecraft.world.level.block.DoorBlock
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.properties.DoorHingeSide
 *  net.minecraft.world.level.block.state.properties.DoubleBlockHalf
 *  net.minecraft.world.level.block.state.properties.Property
 */
package com.copycatsplus.copycats.content.copycat.door;

import com.copycatsplus.copycats.content.copycat.door.CopycatDoorBlock;
import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.AssemblyTransform;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableCullFace;
import java.util.List;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.Property;

public class CopycatDoorModelCore
extends CopycatModelCore {
    @Override
    public void registerModels(List<CopycatModelCore.ModelEntry> entries) {
        entries.add(new CopycatModelCore.ModelEntry("material", CopycatModelCore.ModelGetter.MATERIAL, this, CopycatDoorModelCore.updatePropertiesIfMatch(DoorBlock.class), CopycatModelCore.EntryType.COPYCAT));
    }

    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
        if (material.getBlock() instanceof DoorBlock) {
            context.assembleAll();
            return;
        }
        if (((Boolean)state.getValue((Property)CopycatDoorBlock.CT)).booleanValue()) {
            CopycatDoorModelCore.assembleWithCT(state, context);
        } else {
            CopycatDoorModelCore.assembleWithoutCT(state, context);
        }
    }

    private static void assembleWithCT(BlockState state, CopycatRenderContext context) {
        int rot = (int)((Direction)state.getValue((Property)DoorBlock.FACING)).toYRot();
        boolean rightHinge = ((DoorHingeSide)state.getValue((Property)DoorBlock.HINGE)).equals((Object)DoorHingeSide.RIGHT);
        DoubleBlockHalf half = (DoubleBlockHalf)state.getValue((Property)DoorBlock.HALF);
        boolean open = (Boolean)state.getValue((Property)DoorBlock.OPEN);
        AssemblyTransform transform = t -> t.rotateY(rot);
        if (half == DoubleBlockHalf.LOWER) {
            if (!open) {
                context.assemblePiece(transform, CopycatRenderContext.vec3(0.0, 0.0, 0.0), CopycatRenderContext.aabb(16.0, 12.0, 2.0), CopycatRenderContext.cull(MutableCullFace.SOUTH | MutableCullFace.UP));
                context.assemblePiece(transform, CopycatRenderContext.vec3(0.0, 12.0, 0.0), CopycatRenderContext.aabb(16.0, 4.0, 2.0).move(0.0, 4.0, 0.0), CopycatRenderContext.cull(MutableCullFace.SOUTH | MutableCullFace.DOWN));
                context.assemblePiece(transform, CopycatRenderContext.vec3(0.0, 0.0, 2.0), CopycatRenderContext.aabb(16.0, 12.0, 1.0).move(0.0, 0.0, 15.0), CopycatRenderContext.cull(MutableCullFace.NORTH | MutableCullFace.UP));
                context.assemblePiece(transform, CopycatRenderContext.vec3(0.0, 12.0, 2.0), CopycatRenderContext.aabb(16.0, 4.0, 1.0).move(0.0, 4.0, 15.0), CopycatRenderContext.cull(MutableCullFace.NORTH | MutableCullFace.DOWN));
            } else {
                if (!rightHinge) {
                    transform = t -> t.flipX(true).rotateY(rot);
                }
                context.assemblePiece(transform, CopycatRenderContext.vec3(0.0, 0.0, 0.0), CopycatRenderContext.aabb(2.0, 12.0, 16.0), CopycatRenderContext.cull(MutableCullFace.EAST | MutableCullFace.UP));
                context.assemblePiece(transform, CopycatRenderContext.vec3(0.0, 12.0, 0.0), CopycatRenderContext.aabb(2.0, 4.0, 16.0).move(0.0, 4.0, 0.0), CopycatRenderContext.cull(MutableCullFace.EAST | MutableCullFace.DOWN));
                context.assemblePiece(transform, CopycatRenderContext.vec3(2.0, 0.0, 0.0), CopycatRenderContext.aabb(1.0, 12.0, 16.0).move(15.0, 0.0, 0.0), CopycatRenderContext.cull(MutableCullFace.WEST | MutableCullFace.UP));
                context.assemblePiece(transform, CopycatRenderContext.vec3(2.0, 12.0, 0.0), CopycatRenderContext.aabb(1.0, 4.0, 16.0).move(15.0, 4.0, 0.0), CopycatRenderContext.cull(MutableCullFace.WEST | MutableCullFace.DOWN));
            }
        } else if (!open) {
            context.assemblePiece(transform, CopycatRenderContext.vec3(0.0, 4.0, 0.0), CopycatRenderContext.aabb(16.0, 12.0, 2.0).move(0.0, 4.0, 0.0), CopycatRenderContext.cull(MutableCullFace.SOUTH | MutableCullFace.DOWN));
            context.assemblePiece(transform, CopycatRenderContext.vec3(0.0, 0.0, 0.0), CopycatRenderContext.aabb(16.0, 4.0, 2.0).move(0.0, 8.0, 0.0), CopycatRenderContext.cull(MutableCullFace.SOUTH | MutableCullFace.UP));
            context.assemblePiece(transform, CopycatRenderContext.vec3(0.0, 4.0, 2.0), CopycatRenderContext.aabb(16.0, 12.0, 1.0).move(0.0, 4.0, 15.0), CopycatRenderContext.cull(MutableCullFace.NORTH | MutableCullFace.DOWN));
            context.assemblePiece(transform, CopycatRenderContext.vec3(0.0, 0.0, 2.0), CopycatRenderContext.aabb(16.0, 4.0, 1.0).move(0.0, 8.0, 15.0), CopycatRenderContext.cull(MutableCullFace.NORTH | MutableCullFace.UP));
        } else {
            if (!rightHinge) {
                transform = t -> t.flipX(true).rotateY(rot);
            }
            context.assemblePiece(transform, CopycatRenderContext.vec3(0.0, 4.0, 0.0), CopycatRenderContext.aabb(2.0, 12.0, 16.0).move(0.0, 4.0, 0.0), CopycatRenderContext.cull(MutableCullFace.EAST | MutableCullFace.DOWN));
            context.assemblePiece(transform, CopycatRenderContext.vec3(0.0, 0.0, 0.0), CopycatRenderContext.aabb(2.0, 4.0, 16.0).move(0.0, 8.0, 0.0), CopycatRenderContext.cull(MutableCullFace.EAST | MutableCullFace.UP));
            context.assemblePiece(transform, CopycatRenderContext.vec3(2.0, 4.0, 0.0), CopycatRenderContext.aabb(1.0, 12.0, 16.0).move(15.0, 4.0, 0.0), CopycatRenderContext.cull(MutableCullFace.WEST | MutableCullFace.DOWN));
            context.assemblePiece(transform, CopycatRenderContext.vec3(2.0, 0.0, 0.0), CopycatRenderContext.aabb(1.0, 4.0, 16.0).move(15.0, 8.0, 0.0), CopycatRenderContext.cull(MutableCullFace.WEST | MutableCullFace.UP));
        }
    }

    private static void assembleWithoutCT(BlockState state, CopycatRenderContext context) {
        int rot = (int)((Direction)state.getValue((Property)DoorBlock.FACING)).toYRot();
        boolean rightHinge = ((DoorHingeSide)state.getValue((Property)DoorBlock.HINGE)).equals((Object)DoorHingeSide.RIGHT);
        DoubleBlockHalf half = (DoubleBlockHalf)state.getValue((Property)DoorBlock.HALF);
        boolean open = (Boolean)state.getValue((Property)DoorBlock.OPEN);
        AssemblyTransform transform = t -> t.rotateY(rot);
        if (half == DoubleBlockHalf.LOWER) {
            if (!open) {
                context.assemblePiece(transform, CopycatRenderContext.vec3(0.0, 0.0, 0.0), CopycatRenderContext.aabb(16.0, 16.0, 2.0), CopycatRenderContext.cull(MutableCullFace.SOUTH));
                context.assemblePiece(transform, CopycatRenderContext.vec3(0.0, 0.0, 2.0), CopycatRenderContext.aabb(16.0, 16.0, 1.0).move(0.0, 0.0, 15.0), CopycatRenderContext.cull(MutableCullFace.NORTH));
            } else {
                if (!rightHinge) {
                    transform = t -> t.flipX(true).rotateY(rot);
                }
                context.assemblePiece(transform, CopycatRenderContext.vec3(0.0, 0.0, 0.0), CopycatRenderContext.aabb(2.0, 16.0, 16.0), CopycatRenderContext.cull(MutableCullFace.EAST));
                context.assemblePiece(transform, CopycatRenderContext.vec3(2.0, 0.0, 0.0), CopycatRenderContext.aabb(1.0, 16.0, 16.0).move(15.0, 0.0, 0.0), CopycatRenderContext.cull(MutableCullFace.WEST));
            }
        } else if (!open) {
            context.assemblePiece(transform, CopycatRenderContext.vec3(0.0, 0.0, 0.0), CopycatRenderContext.aabb(16.0, 16.0, 2.0).move(0.0, 0.0, 0.0), CopycatRenderContext.cull(MutableCullFace.SOUTH));
            context.assemblePiece(transform, CopycatRenderContext.vec3(0.0, 0.0, 2.0), CopycatRenderContext.aabb(16.0, 16.0, 1.0).move(0.0, 0.0, 15.0), CopycatRenderContext.cull(MutableCullFace.NORTH));
        } else {
            if (!rightHinge) {
                transform = t -> t.flipX(true).rotateY(rot);
            }
            context.assemblePiece(transform, CopycatRenderContext.vec3(0.0, 0.0, 0.0), CopycatRenderContext.aabb(2.0, 16.0, 16.0).move(0.0, 0.0, 0.0), CopycatRenderContext.cull(MutableCullFace.EAST));
            context.assemblePiece(transform, CopycatRenderContext.vec3(2.0, 0.0, 0.0), CopycatRenderContext.aabb(1.0, 16.0, 16.0).move(15.0, 0.0, 0.0), CopycatRenderContext.cull(MutableCullFace.WEST));
        }
    }
}

