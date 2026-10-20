package com.copycatsplus.copycats.content.copycat.board;

import com.copycatsplus.copycats.CCBlocks;
import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableCullFace;
import com.zurrtum.create.catnip.data.Iterate;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public class CopycatMultiBoardModelCore extends CopycatModelCore {
    private static int i(boolean b) {
        return b ? 1 : 0;
    }

    @Override
    public void registerModels(List<CopycatModelCore.ModelEntry> entries) {
        this.registerForMultiState(entries, CCBlocks.COPYCAT_BOARD.get(), false);
    }

    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
        Map<Direction, Boolean> sides = new HashMap<>();

        for (Direction direction : Iterate.directions) {
            sides.put(direction, state.getValue(CopycatBoardBlock.byDirection(direction)));
        }

        Direction direction = Direction.byName(key.toLowerCase(Locale.ROOT));
        if (sides.get(direction)) {
            if (direction.getAxis().isVertical()) {
                context.assemblePiece(
                    t -> t.flipY(direction == Direction.UP),
                    CopycatRenderContext.vec3(0.0, 0.0, 0.0),
                    CopycatRenderContext.aabb(16.0, 1.0, 16.0),
                    CopycatRenderContext.cull(
                        MutableCullFace.NORTH * i(sides.get(Direction.NORTH))
                            | MutableCullFace.SOUTH * i(sides.get(Direction.SOUTH))
                            | MutableCullFace.EAST * i(sides.get(Direction.EAST))
                            | MutableCullFace.WEST * i(sides.get(Direction.WEST))
                    )
                );
            } else {
                Direction right = direction.getClockWise();
                Direction left = direction.getCounterClockWise();
                context.assemblePiece(
                    t -> t.rotateY((int)direction.toYRot() + 180),
                    CopycatRenderContext.vec3(0.0, 0.0, 0.0),
                    CopycatRenderContext.aabb(16.0, 16.0, 1.0),
                    CopycatRenderContext.cull(
                        MutableCullFace.UP * i(sides.get(Direction.UP))
                            | MutableCullFace.DOWN * i(sides.get(Direction.DOWN))
                            | MutableCullFace.EAST * i(sides.get(right))
                            | MutableCullFace.WEST * i(sides.get(left))
                    )
                );
            }
        }
    }
}
