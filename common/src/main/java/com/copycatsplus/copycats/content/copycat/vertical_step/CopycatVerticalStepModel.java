package com.copycatsplus.copycats.content.copycat.vertical_step;

import com.copycatsplus.copycats.content.copycat.base.model.SimpleCopycatPart;
import com.copycatsplus.copycats.content.copycat.base.model.assembly.GlobalTransform;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

import static com.copycatsplus.copycats.content.copycat.base.model.assembly.Assembler.*;
import static com.copycatsplus.copycats.content.copycat.base.model.assembly.MutableCullFace.*;

public class CopycatVerticalStepModel implements SimpleCopycatPart {

    @Override
    public void emitCopycatQuads(BlockState state, CopycatRenderContext<?, ?> context, BlockState material) {
        Direction facing = state.getValue(CopycatVerticalStepBlock.FACING);

        GlobalTransform transform = t -> t.rotateY((int) facing.toYRot());

        assemblePiece(context,
                transform,
                vec3(8, 0, 8),
                aabb(4, 16, 4),
                cull(EAST | SOUTH)
        );
        assemblePiece(context,
                transform,
                vec3(12, 0, 8),
                aabb(4, 16, 4).move(12, 0, 0),
                cull(WEST | SOUTH)
        );
        assemblePiece(context,
                transform,
                vec3(8, 0, 12),
                aabb(4, 16, 4).move(0, 0, 12),
                cull(EAST | NORTH)
        );
        assemblePiece(context,
                transform,
                vec3(12, 0, 12),
                aabb(4, 16, 4).move(12, 0, 12),
                cull(WEST | NORTH)
        );
    }
}
