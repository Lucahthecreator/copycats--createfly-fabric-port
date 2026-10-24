/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.zurrtum.create.catnip.data.Iterate
 *  net.minecraft.core.Direction
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.properties.Property
 */
package com.copycatsplus.copycats.content.copycat.board;

import com.copycatsplus.copycats.content.copycat.board.CopycatBoardBlock;
import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableCullFace;
import com.zurrtum.create.catnip.data.Iterate;
import java.util.HashMap;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

public class CopycatBoardModelCore
extends CopycatModelCore {
    private static int i(boolean b) {
        return b ? 1 : 0;
    }

    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
        HashMap<Direction, Boolean> sides = new HashMap<Direction, Boolean>();
        for (Direction direction : Iterate.directions) {
            sides.put(direction, (Boolean)state.getValue((Property)CopycatBoardBlock.byDirection(direction)));
        }
        for (Direction direction : Iterate.directions) {
            if (!((Boolean)sides.get(direction)).booleanValue()) continue;
            if (direction.getAxis().isVertical()) {
                context.assemblePiece(t -> t.flipY(direction == Direction.UP), CopycatRenderContext.vec3(0.0, 0.0, 0.0), CopycatRenderContext.aabb(16.0, 1.0, 16.0), CopycatRenderContext.cull(MutableCullFace.NORTH * CopycatBoardModelCore.i((Boolean)sides.get(Direction.NORTH)) | MutableCullFace.SOUTH * CopycatBoardModelCore.i((Boolean)sides.get(Direction.SOUTH)) | MutableCullFace.EAST * CopycatBoardModelCore.i((Boolean)sides.get(Direction.EAST)) | MutableCullFace.WEST * CopycatBoardModelCore.i((Boolean)sides.get(Direction.WEST))));
                continue;
            }
            Direction right = direction.getClockWise();
            Direction left = direction.getCounterClockWise();
            context.assemblePiece(t -> t.rotateY((int)direction.toYRot() + 180), CopycatRenderContext.vec3(0.0, 0.0, 0.0), CopycatRenderContext.aabb(16.0, 16.0, 1.0), CopycatRenderContext.cull(MutableCullFace.UP * CopycatBoardModelCore.i((Boolean)sides.get(Direction.UP)) | MutableCullFace.DOWN * CopycatBoardModelCore.i((Boolean)sides.get(Direction.DOWN)) | MutableCullFace.EAST * CopycatBoardModelCore.i((Boolean)sides.get(right)) | MutableCullFace.WEST * CopycatBoardModelCore.i((Boolean)sides.get(left))));
        }
    }
}

