package com.copycatsplus.copycats.compat.render.connectivity;

import com.copycatsplus.copycats.compat.render.CopycatRenderMaterial;
import com.copycatsplus.copycats.compat.render.CopycatRenderShape;
import com.copycatsplus.copycats.content.copycat.slab.CopycatSlabBlock;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;

public final class CopycatSlabConnectivity {
    private CopycatSlabConnectivity() {
    }

    public static boolean handles(BlockState fromState, BlockState toState) {
        return (isSlab(fromState) || isSlab(toState))
                && !CopycatDoorConnectivity.handles(fromState, toState)
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
        boolean fromSlab = isSlab(fromState);
        boolean toSlab = isSlab(toState);
        boolean sharedMaterial = CopycatRenderMaterial.hasSharedContactMaterial(level, fromPos, toPos, fromState, toState, reference, renderedFace);
        if (fromSlab != toSlab) {
            return sharedMaterial
                    && canSingleSlabConnectToNonSlab(level, fromPos, toPos, fromState, toState, renderedFace);
        }
        return isSlabLike(level, fromPos, fromState)
                && isSlabLike(level, toPos, toState)
                && CopycatRenderShape.canConnectMatchingShapes(level, fromPos, toPos, renderedFace)
                && sharedMaterial;
    }

    public static boolean canHideFace(BlockAndTintGetter level, BlockPos pos, BlockState state,
                                      BlockPos neighborPos, BlockState neighborState, Direction face) {
        if (isSideRenderedSlabToStairContact(state, neighborState, face)) {
            return false;
        }
        boolean stateSlab = isSlab(state);
        boolean neighborSlab = isSlab(neighborState);
        if (stateSlab != neighborSlab) {
            return canSingleSlabConnectToNonSlab(level, pos, neighborPos, state, neighborState, face)
                    && CopycatRenderShape.isFaceCovered(level, pos, state, neighborPos, neighborState, face);
        }
        return isSlabLike(level, pos, state)
                && isSlabLike(level, neighborPos, neighborState)
                && CopycatRenderShape.isFaceCovered(level, pos, state, neighborPos, neighborState, face);
    }

    static boolean isSlab(BlockState state) {
        return state.getBlock() instanceof CopycatSlabBlock;
    }

    private static boolean isDoubleSlab(BlockState state) {
        return isSlab(state) && state.getValue(CopycatSlabBlock.SLAB_TYPE) == SlabType.DOUBLE;
    }

    private static boolean isSlabLike(BlockAndTintGetter level, BlockPos pos, BlockState state) {
        if (isSlab(state)) {
            return true;
        }
        return !CopycatRenderShape.isFullCube(state.getShape(level, pos));
    }

    private static boolean canSingleSlabConnectToNonSlab(BlockAndTintGetter level, BlockPos fromPos, BlockPos toPos,
                                                         BlockState fromState, BlockState toState,
                                                         Direction renderedFace) {
        BlockState slabState = isSlab(fromState) ? fromState : toState;
        if (isDoubleSlab(slabState)) {
            return true;
        }
        if (renderedFace == null) {
            return false;
        }
        return CopycatRenderShape.renderedFacesOverlap(level, fromPos, fromState, toPos, toState, renderedFace);
    }

    private static boolean isSideRenderedSlabToStairContact(BlockState fromState, BlockState toState,
                                                            Direction renderedFace) {
        if (!(isSlab(fromState) && isStairs(toState) || isSlab(toState) && isStairs(fromState))) {
            return false;
        }
        return isHorizontal(renderedFace);
    }

    private static boolean isStairs(BlockState state) {
        return state.is(BlockTags.STAIRS);
    }

    private static boolean isHorizontal(Direction direction) {
        return direction != null && direction.getAxis().isHorizontal();
    }
}
