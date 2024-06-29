package com.copycatsplus.copycats.content.copycat.vertical_slope;

import com.copycatsplus.copycats.content.copycat.base.model.CopycatModelPart;
import com.copycatsplus.copycats.content.copycat.base.model.assembly.GlobalTransform;
import com.copycatsplus.copycats.content.copycat.slope.CopycatSlopeModelPart;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

import static com.copycatsplus.copycats.content.copycat.base.model.assembly.Assembler.CopycatRenderContext;

public class CopycatVerticalSlopeModelPart implements CopycatModelPart {

    private final boolean enhanced;

    public CopycatVerticalSlopeModelPart(boolean enhanced) {
        this.enhanced = enhanced;
    }

    @Override
    public void emitCopycatQuads(BlockState state, CopycatRenderContext context, BlockState material) {
        Direction facing = state.getValue(CopycatVerticalSlopeBlock.FACING);
        int rot = (int) facing.toYRot();
        GlobalTransform transform = t -> t.rotateZ(-90).rotateY(rot);
        CopycatSlopeModelPart.assembleSlope(context, transform, 0, 16, enhanced);
    }
}
