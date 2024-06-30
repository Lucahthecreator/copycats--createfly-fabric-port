package com.copycatsplus.copycats.content.copycat.fence;

import com.copycatsplus.copycats.content.copycat.base.model.CopycatModelCore;
import com.copycatsplus.copycats.content.copycat.base.model.assembly.CopycatRenderContext;
import com.copycatsplus.copycats.content.copycat.base.model.assembly.GlobalTransform;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

import static com.copycatsplus.copycats.content.copycat.base.model.assembly.CopycatRenderContext.*;
import static com.copycatsplus.copycats.content.copycat.base.model.assembly.MutableCullFace.*;

public class CopycatFenceModelPart extends CopycatModelCore {

    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
        for (Direction direction : Iterate.horizontalDirections) {
            context.assemblePiece(
                    t -> t.rotateY((int) direction.toYRot()),
                    vec3(6, 0, 6),
                    aabb(2, 16, 2),
                    cull(SOUTH | EAST)
            );
        }

        for (Direction direction : Iterate.horizontalDirections) {
            if (!state.getValue(CopycatFenceBlock.byDirection(direction))) continue;

            int rot = (int) direction.toYRot();
            GlobalTransform transform = t -> t.rotateY(rot);
            context.assemblePiece(
                    transform,
                    vec3(7, 6, 10),
                    aabb(1, 1, 6),
                    cull(UP | NORTH | EAST)
            );
            context.assemblePiece(
                    transform,
                    vec3(8, 6, 10),
                    aabb(1, 1, 6).move(15, 0, 0),
                    cull(UP | NORTH | WEST)
            );
            context.assemblePiece(
                    transform,
                    vec3(7, 7, 10),
                    aabb(1, 2, 6).move(0, 14, 0),
                    cull(DOWN | NORTH | EAST)
            );
            context.assemblePiece(
                    transform,
                    vec3(8, 7, 10),
                    aabb(1, 2, 6).move(15, 14, 0),
                    cull(DOWN | NORTH | WEST)
            );

            context.assemblePiece(
                    transform,
                    vec3(7, 12, 10),
                    aabb(1, 1, 6),
                    cull(UP | NORTH | EAST)
            );
            context.assemblePiece(
                    transform,
                    vec3(8, 12, 10),
                    aabb(1, 1, 6).move(15, 0, 0),
                    cull(UP | NORTH | WEST)
            );
            context.assemblePiece(
                    transform,
                    vec3(7, 13, 10),
                    aabb(1, 2, 6).move(0, 14, 0),
                    cull(DOWN | NORTH | EAST)
            );
            context.assemblePiece(
                    transform,
                    vec3(8, 13, 10),
                    aabb(1, 2, 6).move(15, 14, 0),
                    cull(DOWN | NORTH | WEST)
            );
        }
    }
}
