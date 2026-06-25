package com.copycatsplus.copycats.compat.render.connectivity;

import com.copycatsplus.copycats.compat.debug.CopycatsDebug;
import com.copycatsplus.copycats.compat.render.CopycatRenderShape;
import com.copycatsplus.copycats.content.copycat.corner_slice.CopycatCornerSliceBlock;
import com.copycatsplus.copycats.content.copycat.slice.CopycatSliceBlock;
import com.copycatsplus.copycats.content.copycat.vertical_slice.CopycatVerticalSliceBlock;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public final class CopycatConnectivity {
    private CopycatConnectivity() {
    }

    public static boolean canConnectForCT(BlockAndTintGetter level, BlockPos fromPos, BlockPos toPos,
                                          BlockState fromState, BlockState toState,
                                          BlockState reference, Direction renderedFace) {
        if (blocksNonFullSlice(level, fromPos, fromState, toPos, toState, "CT")) {
            return false;
        }

        String family = "none";
        boolean result = false;

        if (CopycatDoorConnectivity.handles(fromState, toState)) {
            family = "door";
            result = CopycatDoorConnectivity.canConnectForCT(fromState, toState);
        } else if (CopycatSlabConnectivity.handles(fromState, toState)) {
            family = "slab";
            result = CopycatSlabConnectivity.canConnectForCT(level, fromPos, toPos, fromState, toState, reference, renderedFace);
        } else if (CopycatByteConnectivity.handles(fromState, toState)) {
            family = "byte";
            result = CopycatByteConnectivity.canConnectForCT(level, fromPos, toPos, fromState, toState, reference, renderedFace);
        } else if (CopycatBytePanelConnectivity.handles(fromState, toState)) {
            family = "byte_panel";
            result = CopycatBytePanelConnectivity.canConnectForCT(level, fromPos, toPos, fromState, toState, reference, renderedFace);
        } else if (CopycatFullBlockConnectivity.handles(fromState, toState)) {
            family = "full_block";
            result = CopycatFullBlockConnectivity.canConnectForCT(level, fromPos, toPos, fromState, toState, reference, renderedFace);
        } else if (CopycatGenericConnectivity.handles(fromState, toState)) {
            family = "generic";
            result = CopycatGenericConnectivity.canConnectForCT(level, fromPos, toPos, fromState, toState, reference, renderedFace);
        }

        String selectedFamily = family;
        boolean selectedResult = result;
        CopycatsDebug.log("ct", () -> "family CT family=" + selectedFamily
                + " from=" + fromPos + " " + fromState
                + " to=" + toPos + " " + toState
                + " face=" + renderedFace + " result=" + selectedResult);
        return result;
    }

    public static boolean canHideFace(BlockAndTintGetter level, BlockPos pos, BlockState state,
                                      BlockPos neighborPos, BlockState neighborState, Direction face) {
        if (blocksNonFullSlice(level, pos, state, neighborPos, neighborState, "face hide")) {
            return false;
        }

        String family = "none";
        boolean result = false;

        if (CopycatDoorConnectivity.handlesFaceHiding(state, neighborState)) {
            family = "door";
            result = CopycatDoorConnectivity.canHideFace(pos, state, neighborPos, neighborState, face);
        } else if (CopycatSlabConnectivity.handlesFaceHiding(state, neighborState)) {
            family = "slab";
            result = CopycatSlabConnectivity.canHideFace(level, pos, state, neighborPos, neighborState, face);
        } else if (CopycatByteConnectivity.handlesFaceHiding(state, neighborState)) {
            family = "byte";
            result = CopycatByteConnectivity.canHideFace(level, pos, state, neighborPos, neighborState, face);
        } else if (CopycatBytePanelConnectivity.handlesFaceHiding(state, neighborState)) {
            family = "byte_panel";
            result = CopycatBytePanelConnectivity.canHideFace(level, pos, state, neighborPos, neighborState, face);
        } else if (CopycatFullBlockConnectivity.handlesFaceHiding(state, neighborState)) {
            family = "full_block";
            result = CopycatFullBlockConnectivity.canHideFace(level, pos, state, neighborPos, neighborState, face);
        } else if (CopycatGenericConnectivity.handlesFaceHiding(state, neighborState)) {
            family = "generic";
            result = CopycatGenericConnectivity.canHideFace(level, pos, state, neighborPos, neighborState, face);
        }

        String selectedFamily = family;
        boolean selectedResult = result;
        CopycatsDebug.log("face_hiding", () -> "family face hide family=" + selectedFamily
                + " pos=" + pos + " state=" + state
                + " neighbor=" + neighborPos + " neighborState=" + neighborState
                + " face=" + face + " result=" + selectedResult);
        return result;
    }

    private static boolean blocksNonFullSlice(BlockAndTintGetter level,
                                              BlockPos fromPos, BlockState fromState,
                                              BlockPos toPos, BlockState toState,
                                              String action) {
        boolean fromBlocked = isNonFullSlice(level, fromPos, fromState);
        boolean toBlocked = isNonFullSlice(level, toPos, toState);
        boolean result = fromBlocked || toBlocked;
        if (result) {
            CopycatsDebug.log("ct", () -> "slice " + action + " blocked until full cube"
                    + " from=" + fromPos + " " + fromState + " blocked=" + fromBlocked
                    + " to=" + toPos + " " + toState + " blocked=" + toBlocked);
        }
        return result;
    }

    private static boolean isNonFullSlice(BlockAndTintGetter level, BlockPos pos, BlockState state) {
        if (!isSliceBlock(state)) {
            return false;
        }
        return !CopycatRenderShape.isFullCube(state.getShape(level, pos));
    }

    private static boolean isSliceBlock(BlockState state) {
        return state.getBlock() instanceof CopycatSliceBlock
                || state.getBlock() instanceof CopycatVerticalSliceBlock
                || state.getBlock() instanceof CopycatCornerSliceBlock;
    }
}
