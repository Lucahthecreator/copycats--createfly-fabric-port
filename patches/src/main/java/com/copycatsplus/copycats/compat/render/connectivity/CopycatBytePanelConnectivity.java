package com.copycatsplus.copycats.compat.render.connectivity;

import com.copycatsplus.copycats.compat.render.CopycatRenderMaterial;
import com.copycatsplus.copycats.compat.render.CopycatRenderShape;
import com.copycatsplus.copycats.content.copycat.byte_panel.CopycatBytePanelBlock;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public final class CopycatBytePanelConnectivity {
    private CopycatBytePanelConnectivity() {
    }

    public static boolean handles(BlockState fromState, BlockState toState) {
        return (isBytePanel(fromState) || isBytePanel(toState))
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

    static boolean isBytePanel(BlockState state) {
        return state.getBlock() instanceof CopycatBytePanelBlock;
    }

    private static boolean supportedNeighbor(BlockState state) {
        return isBytePanel(state)
                || !CopycatFullBlockConnectivity.isCopycat(state);
    }
}
