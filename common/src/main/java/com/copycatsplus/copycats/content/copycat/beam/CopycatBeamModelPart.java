package com.copycatsplus.copycats.content.copycat.beam;


import com.copycatsplus.copycats.content.copycat.base.model.CopycatModelPart;
import com.copycatsplus.copycats.content.copycat.base.model.assembly.GlobalTransform;
import net.minecraft.world.level.block.state.BlockState;

import static com.copycatsplus.copycats.content.copycat.base.model.assembly.Assembler.*;
import static com.copycatsplus.copycats.content.copycat.base.model.assembly.MutableCullFace.*;
import static net.minecraft.core.Direction.Axis;

public class CopycatBeamModelPart implements CopycatModelPart {

    @Override
    public void emitCopycatQuads(BlockState state, CopycatRenderContext context, BlockState material) {
        Axis axis = state.getValue(CopycatBeamBlock.AXIS);

        GlobalTransform transform = t -> t.rotateX(axis == Axis.Y ? 90 : 0).rotateY(axis == Axis.Z ? 90 : 0);

        assemblePiece(context,
                transform,
                vec3(4, 4, 0),
                aabb(4, 4, 16),
                cull(UP | EAST)
        );
        assemblePiece(context,
                transform,
                vec3(8, 4, 0),
                aabb(4, 4, 16).move(12, 0, 0),
                cull(UP | WEST)
        );
        assemblePiece(context,
                transform,
                vec3(4, 8, 0),
                aabb(4, 4, 16).move(0, 12, 0),
                cull(DOWN | EAST)
        );
        assemblePiece(context,
                transform,
                vec3(8, 8, 0),
                aabb(4, 4, 16).move(12, 12, 0),
                cull(DOWN | WEST)
        );
    }
}
