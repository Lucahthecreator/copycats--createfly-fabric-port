/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Direction
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.properties.Half
 *  net.minecraft.world.level.block.state.properties.Property
 */
package com.copycatsplus.copycats.content.copycat.slope_layer;

import com.copycatsplus.copycats.content.copycat.slope.CopycatSlopeModelCore;
import com.copycatsplus.copycats.content.copycat.slope_layer.CopycatSlopeLayerBlock;
import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.AssemblyTransform;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.Property;

public class CopycatSlopeLayerModelCore
extends CopycatModelCore {
    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
        int layer = (Integer)state.getValue((Property)CopycatSlopeLayerBlock.LAYERS);
        Direction facing = (Direction)state.getValue(CopycatSlopeLayerBlock.FACING);
        Half half = (Half)state.getValue(CopycatSlopeLayerBlock.HALF);
        AssemblyTransform transform = t -> t.rotateY((int)facing.toYRot()).flipY(half == Half.TOP);
        if (layer <= 4) {
            CopycatSlopeModelCore.assembleSlope(context, transform, 0.0, layer * 4, this.enhanced);
        } else {
            CopycatSlopeModelCore.assembleSlope(context, transform, (layer - 4) * 4, 16.0, this.enhanced);
        }
    }
}

