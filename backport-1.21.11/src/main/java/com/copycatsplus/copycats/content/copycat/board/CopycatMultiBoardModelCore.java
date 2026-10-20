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

import com.copycatsplus.copycats.CCBlocks;
import com.copycatsplus.copycats.content.copycat.board.CopycatBoardBlock;
import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableCullFace;
import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlock;
import com.zurrtum.create.catnip.data.Iterate;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

public class CopycatMultiBoardModelCore
extends CopycatModelCore {
    private static int i(boolean b) {
        return b ? 1 : 0;
    }

    @Override
    public void registerModels(List<CopycatModelCore.ModelEntry> entries) {
        this.registerForMultiState(entries, (IMultiStateCopycatBlock)CCBlocks.COPYCAT_BOARD.get(), false);
    }

    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
        HashMap<Direction, Boolean> sides = new HashMap<Direction, Boolean>();
        for (Direction direction : Iterate.directions) {
            sides.put(direction, (Boolean)state.getValue((Property)CopycatBoardBlock.byDirection(direction)));
        }
        Direction direction = Direction.byName((String)key.toLowerCase(Locale.ROOT));
        if (!((Boolean)sides.get(direction)).booleanValue()) {
            return;
        }
        if (direction.getAxis().isVertical()) {
            context.assemblePiece(t -> t.flipY(direction == Direction.UP), CopycatRenderContext.vec3(0.0, 0.0, 0.0), CopycatRenderContext.aabb(16.0, 1.0, 16.0), CopycatRenderContext.cull(MutableCullFace.NORTH * CopycatMultiBoardModelCore.i((Boolean)sides.get(Direction.NORTH)) | MutableCullFace.SOUTH * CopycatMultiBoardModelCore.i((Boolean)sides.get(Direction.SOUTH)) | MutableCullFace.EAST * CopycatMultiBoardModelCore.i((Boolean)sides.get(Direction.EAST)) | MutableCullFace.WEST * CopycatMultiBoardModelCore.i((Boolean)sides.get(Direction.WEST))));
        } else {
            Direction right = direction.getClockWise();
            Direction left = direction.getCounterClockWise();
            context.assemblePiece(t -> t.rotateY((int)direction.toYRot() + 180), CopycatRenderContext.vec3(0.0, 0.0, 0.0), CopycatRenderContext.aabb(16.0, 16.0, 1.0), CopycatRenderContext.cull(MutableCullFace.UP * CopycatMultiBoardModelCore.i((Boolean)sides.get(Direction.UP)) | MutableCullFace.DOWN * CopycatMultiBoardModelCore.i((Boolean)sides.get(Direction.DOWN)) | MutableCullFace.EAST * CopycatMultiBoardModelCore.i((Boolean)sides.get(right)) | MutableCullFace.WEST * CopycatMultiBoardModelCore.i((Boolean)sides.get(left))));
        }
    }
}

