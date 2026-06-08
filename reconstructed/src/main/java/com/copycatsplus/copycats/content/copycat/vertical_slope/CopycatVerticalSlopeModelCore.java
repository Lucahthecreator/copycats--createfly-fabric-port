/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Direction
 *  net.minecraft.world.level.block.state.BlockState
 */
package com.copycatsplus.copycats.content.copycat.vertical_slope;

import com.copycatsplus.copycats.content.copycat.slope.CopycatSlopeModelCore;
import com.copycatsplus.copycats.content.copycat.vertical_slope.CopycatVerticalSlopeBlock;
import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.AssemblyTransform;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public class CopycatVerticalSlopeModelCore
extends CopycatModelCore {
    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
        Direction facing = (Direction)state.getValue(CopycatVerticalSlopeBlock.FACING);
        int rot = (int)facing.toYRot();
        AssemblyTransform transform = t -> t.rotateZ(-90).rotateY(rot);
        CopycatSlopeModelCore.assembleSlope(context, transform, 0.0, 16.0, this.enhanced);
    }
}

