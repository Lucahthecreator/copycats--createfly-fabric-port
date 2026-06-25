package com.copycatsplus.copycats.compat.render.connectivity;

import com.copycatsplus.copycats.content.copycat.door.CopycatDoorBlock;
import com.copycatsplus.copycats.content.copycat.sliding_door.CopycatSlidingDoorBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.Property;

public final class CopycatDoorConnectivity {
    private CopycatDoorConnectivity() {
    }

    public static boolean handles(BlockState fromState, BlockState toState) {
        return isDoor(fromState) || isDoor(toState);
    }

    public static boolean isDoor(BlockState state) {
        return state.getBlock() instanceof CopycatDoorBlock
                || state.getBlock() instanceof CopycatSlidingDoorBlock;
    }

    public static boolean canConnectForCT(BlockState fromState, BlockState toState) {
        return isDoor(fromState) && isDoor(toState) && fromState.getBlock() == toState.getBlock();
    }

    public static boolean handlesFaceHiding(BlockState state, BlockState neighborState) {
        return isDoor(state) && isDoor(neighborState) && state.getBlock() == neighborState.getBlock();
    }

    public static boolean canHideFace(BlockPos pos, BlockState state,
                                      BlockPos neighborPos, BlockState neighborState, Direction face) {
        if (face != Direction.UP && face != Direction.DOWN) {
            return false;
        }
        if (pos.relative(face).equals(neighborPos) == false) {
            return false;
        }
        DoubleBlockHalf half = state.getValue((Property<DoubleBlockHalf>) DoorBlock.HALF);
        DoubleBlockHalf neighborHalf = neighborState.getValue((Property<DoubleBlockHalf>) DoorBlock.HALF);
        return (face == Direction.UP && half == DoubleBlockHalf.LOWER && neighborHalf == DoubleBlockHalf.UPPER)
                || (face == Direction.DOWN && half == DoubleBlockHalf.UPPER && neighborHalf == DoubleBlockHalf.LOWER);
    }
}
