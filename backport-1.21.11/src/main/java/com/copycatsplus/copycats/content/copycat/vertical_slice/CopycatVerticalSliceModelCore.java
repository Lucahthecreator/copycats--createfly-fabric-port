/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Direction
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.properties.Property
 */
package com.copycatsplus.copycats.content.copycat.vertical_slice;

import com.copycatsplus.copycats.content.copycat.vertical_slice.CopycatVerticalSliceBlock;
import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.AssemblyTransform;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableCullFace;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

public class CopycatVerticalSliceModelCore
extends CopycatModelCore {
    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
        int rot = (int)((Direction)state.getValue(CopycatVerticalSliceBlock.FACING)).toYRot();
        int layers = (Integer)state.getValue((Property)CopycatVerticalSliceBlock.LAYERS);
        AssemblyTransform transform = t -> t.rotateY(rot);
        context.assemblePiece(transform, CopycatRenderContext.vec3(16 - layers, 0.0, 16 - layers), CopycatRenderContext.aabb(layers, 16.0, layers).move(16 - layers, 0.0, 16 - layers), CopycatRenderContext.cull(MutableCullFace.WEST | MutableCullFace.NORTH));
        context.assemblePiece(transform, CopycatRenderContext.vec3(16 - layers, 0.0, 16 - layers * 2), CopycatRenderContext.aabb(layers, 16.0, layers).move(16 - layers, 0.0, 0.0), CopycatRenderContext.cull(MutableCullFace.WEST | MutableCullFace.SOUTH));
        context.assemblePiece(transform, CopycatRenderContext.vec3(16 - layers * 2, 0.0, 16 - layers), CopycatRenderContext.aabb(layers, 16.0, layers).move(0.0, 0.0, 16 - layers), CopycatRenderContext.cull(MutableCullFace.EAST | MutableCullFace.NORTH));
        context.assemblePiece(transform, CopycatRenderContext.vec3(16 - layers * 2, 0.0, 16 - layers * 2), CopycatRenderContext.aabb(layers, 16.0, layers).move(0.0, 0.0, 0.0), CopycatRenderContext.cull(MutableCullFace.EAST | MutableCullFace.SOUTH));
    }
}

