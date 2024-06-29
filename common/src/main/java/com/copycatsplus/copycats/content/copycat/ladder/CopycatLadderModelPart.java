package com.copycatsplus.copycats.content.copycat.ladder;

import com.copycatsplus.copycats.content.copycat.base.model.CopycatModelPart;
import com.copycatsplus.copycats.content.copycat.base.model.assembly.GlobalTransform;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.state.BlockState;

import static com.copycatsplus.copycats.content.copycat.base.model.assembly.Assembler.*;

public class CopycatLadderModelPart implements CopycatModelPart {

    @Override
    public void emitCopycatQuads(BlockState state, CopycatRenderContext context, BlockState material) {
        int rot = (int) state.getValue(LadderBlock.FACING).toYRot();
        GlobalTransform transform = t -> t.rotateY(rot);
        assemblePoles(context, transform);
        assembleSteps(context, transform);
    }

    public static void assemblePoles(CopycatRenderContext context, GlobalTransform transform) {
        assemblePiece(context,
                transform,
                vec3(2, 0, 0),
                aabb(2, 16, 1),
                cull(0)
        );
        assemblePiece(context,
                transform,
                vec3(12, 0, 0),
                aabb(2, 16, 1).move(14, 0, 0),
                cull(0)
        );
    }

    public static void assembleSteps(CopycatRenderContext context, GlobalTransform transform) {
        assemblePiece(context,
                transform,
                vec3(1, 1, 0.1),
                aabb(14, 2, 0.8),
                cull(0)
        );
        assemblePiece(context,
                transform,
                vec3(1, 5, 0.1),
                aabb(14, 2, 0.8),
                cull(0)
        );
        assemblePiece(context,
                transform,
                vec3(1, 9, 0.1),
                aabb(14, 2, 0.8),
                cull(0)
        );
        assemblePiece(context,
                transform,
                vec3(1, 13, 0.1),
                aabb(14, 2, 0.8),
                cull(0)
        );
    }
}
