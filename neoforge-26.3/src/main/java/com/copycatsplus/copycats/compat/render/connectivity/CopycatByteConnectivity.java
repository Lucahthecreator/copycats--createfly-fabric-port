package com.copycatsplus.copycats.compat.render.connectivity;

import com.copycatsplus.copycats.compat.render.CopycatRenderMaterial;
import com.copycatsplus.copycats.compat.render.CopycatRenderShape;
import com.copycatsplus.copycats.content.copycat.bytes.CopycatByteBlock;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public final class CopycatByteConnectivity {
    private CopycatByteConnectivity() {
    }

    public static boolean handles(BlockState fromState, BlockState toState) {
        return (isByte(fromState) || isByte(toState))
                && !CopycatDoorConnectivity.handles(fromState, toState)
                && supportedNeighbor(fromState)
                && supportedNeighbor(toState);
    }

    public static boolean handlesFaceHiding(BlockState state, BlockState neighborState) {
        return handles(state, neighborState);
    }

    public static boolean canConnectForCT(BlockAndTintGetter level, BlockPos fromPos, BlockPos toPos,
                                          BlockState fromState, BlockState toState,
                                          BlockState reference, Direction renderedFace) {
        return CopycatRenderShape.canConnectMatchingShapes(level, fromPos, toPos, renderedFace)
                && CopycatRenderMaterial.hasSharedContactMaterial(level, fromPos, toPos, fromState, toState, reference, renderedFace);
    }

    public static boolean canHideFace(BlockAndTintGetter level, BlockPos pos, BlockState state,
                                      BlockPos neighborPos, BlockState neighborState, Direction face) {
        return CopycatRenderShape.isFaceCovered(level, pos, state, neighborPos, neighborState, face);
    }

    static boolean isByte(BlockState state) {
        return state.getBlock() instanceof CopycatByteBlock;
    }

    private static boolean supportedNeighbor(BlockState state) {
        return isByte(state)
                || CopycatFullBlockConnectivity.isFullCopycatBlock(state)
                || !CopycatFullBlockConnectivity.isCopycat(state);
    }
}
