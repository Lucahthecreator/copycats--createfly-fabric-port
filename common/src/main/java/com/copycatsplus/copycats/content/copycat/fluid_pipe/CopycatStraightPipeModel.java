package com.copycatsplus.copycats.content.copycat.fluid_pipe;

import com.copycatsplus.copycats.content.copycat.base.model.SimpleCopycatPart;
import com.copycatsplus.copycats.content.copycat.base.model.assembly.GlobalTransform;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.level.block.state.BlockState;

import static com.copycatsplus.copycats.content.copycat.base.model.assembly.Assembler.*;
import static com.copycatsplus.copycats.content.copycat.base.model.assembly.MutableCullFace.*;

public class CopycatStraightPipeModel extends CopycatFluidPipeModel implements SimpleCopycatPart {

    @Override
    public void emitCopycatQuads(BlockState state, CopycatRenderContext<?, ?> context, BlockState material) {
        Axis axis = state.getValue(CopycatGlassFluidPipeBlock.AXIS);

        int yRot = axis == Axis.X ? 90 : 0;
        int xRot = axis == Axis.Y ? 90 : 0;
        renderWindowCore(context, t -> t.rotateY(yRot).rotateX(xRot));

        assembleAccessories(context);
    }

    protected void renderWindowCore(CopycatRenderContext<?, ?> context, GlobalTransform transform) {
        assemblePiece(context,
                transform,
                vec3(4, 4, 4),
                aabb(2, 2, 8).move(0, 0, 4),
                cull(EAST | UP | NORTH | SOUTH)
        );
    }
}
