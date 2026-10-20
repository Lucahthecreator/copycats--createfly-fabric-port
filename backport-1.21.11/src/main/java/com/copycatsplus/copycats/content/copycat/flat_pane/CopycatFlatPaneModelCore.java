/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Direction$Axis
 *  net.minecraft.world.level.block.state.BlockState
 */
package com.copycatsplus.copycats.content.copycat.flat_pane;

import com.copycatsplus.copycats.content.copycat.flat_pane.CopycatFlatPaneBlock;
import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.AssemblyTransform;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableCullFace;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public class CopycatFlatPaneModelCore
extends CopycatModelCore {
    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
        Direction.Axis axis = (Direction.Axis)state.getValue(CopycatFlatPaneBlock.AXIS);
        int xRot = axis == Direction.Axis.Z ? 90 : 0;
        int zRot = axis == Direction.Axis.X ? 90 : 0;
        AssemblyTransform transform = t -> t.rotateX(xRot).rotateZ(zRot);
        context.assemblePiece(transform, CopycatRenderContext.vec3(0.0, 7.0, 0.0), CopycatRenderContext.aabb(16.0, 1.0, 16.0), CopycatRenderContext.cull(MutableCullFace.UP));
        context.assemblePiece(transform, CopycatRenderContext.vec3(0.0, 8.0, 0.0), CopycatRenderContext.aabb(16.0, 1.0, 16.0), CopycatRenderContext.cull(MutableCullFace.DOWN));
    }
}

