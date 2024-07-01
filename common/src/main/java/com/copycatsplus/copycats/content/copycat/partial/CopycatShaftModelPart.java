package com.copycatsplus.copycats.content.copycat.partial;

import com.copycatsplus.copycats.content.copycat.base.model.CopycatModelCore;
import com.copycatsplus.copycats.content.copycat.base.model.assembly.CopycatRenderContext;
import com.copycatsplus.copycats.content.copycat.base.model.assembly.AssemblyTransform;
import com.copycatsplus.copycats.content.copycat.shaft.CopycatShaftBlock;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.level.block.state.BlockState;

import static com.copycatsplus.copycats.content.copycat.base.model.assembly.CopycatRenderContext.*;
import static com.copycatsplus.copycats.content.copycat.base.model.assembly.MutableCullFace.*;

public class CopycatShaftModelPart extends CopycatModelCore {

    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
        Axis axis = state.getValue(CopycatShaftBlock.AXIS);

        AssemblyTransform transform = t -> t.rotateY(axis == Axis.X ? 90 : 0).rotateX(axis == Axis.Y ? 90 : 0);
        context.assemblePiece(
                transform,
                vec3(6, 6, 0),
                aabb(2, 2, 16).move(0, 0, 0),
                cull(UP | EAST)
        );
        context.assemblePiece(
                transform,
                vec3(8, 6, 0),
                aabb(2, 2, 16).move(14, 0, 0),
                cull(UP | WEST)
        );
        context.assemblePiece(
                transform,
                vec3(6, 8, 0),
                aabb(2, 2, 16).move(0, 14, 0),
                cull(DOWN | EAST)
        );
        context.assemblePiece(
                transform,
                vec3(8, 8, 0),
                aabb(2, 2, 16).move(14, 14, 0),
                cull(DOWN | WEST)
        );
    }
}
