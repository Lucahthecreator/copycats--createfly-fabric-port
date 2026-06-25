package com.copycatsplus.copycats.compat.render.connectivity;

import com.copycatsplus.copycats.compat.render.CopycatRenderMaterial;
import com.copycatsplus.copycats.compat.render.CopycatRenderShape;
import com.copycatsplus.copycats.content.copycat.fluid_pipe.CopycatFluidPipeBlock;
import com.copycatsplus.copycats.content.copycat.fluid_pipe.CopycatGlassFluidPipeBlock;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlock;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public final class CopycatGenericConnectivity {
    private CopycatGenericConnectivity() {
    }

    public static boolean handles(BlockState fromState, BlockState toState) {
        return (fromState.getBlock() instanceof ICopycatBlock || toState.getBlock() instanceof ICopycatBlock)
                && !CopycatDoorConnectivity.handles(fromState, toState)
                && !CopycatSlabConnectivity.isSlab(fromState)
                && !CopycatSlabConnectivity.isSlab(toState)
                && !CopycatByteConnectivity.isByte(fromState)
                && !CopycatByteConnectivity.isByte(toState)
                && !CopycatBytePanelConnectivity.isBytePanel(fromState)
                && !CopycatBytePanelConnectivity.isBytePanel(toState);
    }

    public static boolean handlesFaceHiding(BlockState state, BlockState neighborState) {
        return handles(state, neighborState);
    }

    public static boolean canConnectForCT(BlockAndTintGetter level, BlockPos fromPos, BlockPos toPos,
                                          BlockState fromState, BlockState toState,
                                          BlockState reference, Direction renderedFace) {
        if (isPipe(fromState) || isPipe(toState)) {
            return false;
        }
        return CopycatRenderMaterial.hasSharedContactMaterial(level, fromPos, toPos, fromState, toState, reference, renderedFace);
    }

    public static boolean canHideFace(BlockAndTintGetter level, BlockPos pos, BlockState state,
                                      BlockPos neighborPos, BlockState neighborState, Direction face) {
        if (isPipe(state) || isPipe(neighborState)) {
            return false;
        }
        return CopycatRenderShape.isFaceCovered(level, pos, state, neighborPos, neighborState, face);
    }

    private static boolean isPipe(BlockState state) {
        return state.getBlock() instanceof CopycatFluidPipeBlock
                || state.getBlock() instanceof CopycatGlassFluidPipeBlock;
    }
}
