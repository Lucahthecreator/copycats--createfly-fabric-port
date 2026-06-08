/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Direction
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.properties.Property
 */
package com.copycatsplus.copycats.content.copycat.layer;

import com.copycatsplus.copycats.content.copycat.layer.CopycatLayerBlock;
import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.AssemblyTransform;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableCullFace;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

public class CopycatLayerModelCore
extends CopycatModelCore {
    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
        int layer = (Integer)state.getValue((Property)CopycatLayerBlock.LAYERS);
        Direction facing = (Direction)state.getValue(CopycatLayerBlock.FACING);
        if (facing.getAxis().isVertical()) {
            boolean flipY = facing == Direction.DOWN;
            AssemblyTransform transform = t -> t.flipY(flipY);
            context.assemblePiece(transform, CopycatRenderContext.vec3(0.0, 0.0, 0.0), CopycatRenderContext.aabb(16.0, layer, 16.0), CopycatRenderContext.cull(MutableCullFace.UP));
            context.assemblePiece(transform, CopycatRenderContext.vec3(0.0, layer, 0.0), CopycatRenderContext.aabb(16.0, layer, 16.0).move(0.0, 16 - layer, 0.0), CopycatRenderContext.cull(MutableCullFace.DOWN));
        } else {
            int rot = (int)facing.toYRot();
            AssemblyTransform transform = t -> t.rotateY(rot);
            context.assemblePiece(transform, CopycatRenderContext.vec3(0.0, 0.0, 0.0), CopycatRenderContext.aabb(16.0, 16.0, layer), CopycatRenderContext.cull(MutableCullFace.SOUTH));
            context.assemblePiece(transform, CopycatRenderContext.vec3(0.0, 0.0, layer), CopycatRenderContext.aabb(16.0, 16.0, layer).move(0.0, 0.0, 16 - layer), CopycatRenderContext.cull(MutableCullFace.NORTH));
        }
    }
}

