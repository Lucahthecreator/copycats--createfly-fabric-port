package com.copycatsplus.copycats.content.copycat.vertical_slope;

import com.copycatsplus.copycats.content.copycat.base.model.CopycatModelCore;
import com.copycatsplus.copycats.content.copycat.base.model.assembly.CopycatRenderContext;
import com.copycatsplus.copycats.content.copycat.base.model.assembly.GlobalTransform;
import com.copycatsplus.copycats.content.copycat.slope.CopycatSlopeModelPart;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public class CopycatVerticalSlopeModelPart extends CopycatModelCore {

    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
        Direction facing = state.getValue(CopycatVerticalSlopeBlock.FACING);
        int rot = (int) facing.toYRot();
        GlobalTransform transform = t -> t.rotateZ(-90).rotateY(rot);
        CopycatSlopeModelPart.assembleSlope(context, transform, 0, 16, enhanced);
    }
}
