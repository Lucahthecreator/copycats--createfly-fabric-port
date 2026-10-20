package com.copycatsplus.copycats.compat.render.connectivity;

import com.copycatsplus.copycats.compat.render.CopycatRenderMaterial;
import com.copycatsplus.copycats.compat.render.CopycatRenderShape;
import com.copycatsplus.copycats.content.copycat.block.CopycatBlockBlock;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlock;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public final class CopycatFullBlockConnectivity {
    private CopycatFullBlockConnectivity() {
    }

    public static boolean handles(BlockState fromState, BlockState toState) {
        return (isFullCopycatBlock(fromState) || isFullCopycatBlock(toState))
                && !CopycatDoorConnectivity.handles(fromState, toState)
                && !CopycatSlabConnectivity.isSlab(fromState)
                && !CopycatSlabConnectivity.isSlab(toState)
                && !CopycatByteConnectivity.isByte(fromState)
                && !CopycatByteConnectivity.isByte(toState)
                && !CopycatBytePanelConnectivity.isBytePanel(fromState)
                && !CopycatBytePanelConnectivity.isBytePanel(toState);
    }

    public static boolean handlesFaceHiding(BlockState state, BlockState neighborState) {
        return (isFullCopycatBlock(state) || isFullCopycatBlock(neighborState))
                && !CopycatDoorConnectivity.handles(state, neighborState)
                && !CopycatSlabConnectivity.isSlab(state)
                && !CopycatSlabConnectivity.isSlab(neighborState)
                && !CopycatByteConnectivity.isByte(state)
                && !CopycatByteConnectivity.isByte(neighborState)
                && !CopycatBytePanelConnectivity.isBytePanel(state)
                && !CopycatBytePanelConnectivity.isBytePanel(neighborState);
    }

    public static boolean canConnectForCT(BlockAndTintGetter level, BlockPos fromPos, BlockPos toPos,
                                          BlockState fromState, BlockState toState,
                                          BlockState reference, Direction renderedFace) {
        return CopycatRenderShape.areFullCubes(level, fromPos, toPos)
                && CopycatRenderMaterial.hasSharedContactMaterial(level, fromPos, toPos, fromState, toState, reference, renderedFace);
    }

    public static boolean canHideFace(BlockAndTintGetter level, BlockPos pos, BlockState state,
                                      BlockPos neighborPos, BlockState neighborState, Direction face) {
        return CopycatRenderShape.isFaceCovered(level, pos, state, neighborPos, neighborState, face);
    }

    static boolean isFullCopycatBlock(BlockState state) {
        return state.getBlock() instanceof CopycatBlockBlock;
    }

    static boolean isCopycat(BlockState state) {
        return state.getBlock() instanceof ICopycatBlock;
    }
}
