package com.copycatsplus.copycats.compat.render;

import com.copycatsplus.copycats.compat.debug.CopycatsDebug;
import com.copycatsplus.copycats.compat.render.connectivity.CopycatConnectivity;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlock;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public final class CopycatRenderShape {
    private CopycatRenderShape() {
    }

    public static boolean areFullCubes(BlockAndTintGetter level, BlockPos fromPos, BlockPos toPos) {
        BlockState fromState = level.getBlockState(fromPos);
        BlockState toState = level.getBlockState(toPos);
        boolean result = isFullCube(fromState.getShape(level, fromPos))
                && isFullCube(toState.getShape(level, toPos));
        CopycatsDebug.log("shape", () -> "full cubes from=" + fromPos + " " + fromState
                + " to=" + toPos + " " + toState + " result=" + result);
        return result;
    }

    public static boolean usesMatchingShapeTextures(BlockState fromState, BlockState toState) {
        boolean result = fromState.getBlock() instanceof ICopycatBlock
                && toState.getBlock() instanceof ICopycatBlock
                && usesMatchingShapeTextures(fromState)
                && usesMatchingShapeTextures(toState);
        CopycatsDebug.log("shape", () -> "matching-shape eligible from=" + fromState
                + " to=" + toState + " result=" + result);
        return result;
    }

    public static boolean usesMatchingShapeTextures(BlockState state) {
        return state.getBlock() instanceof ICopycatBlock;
    }

    public static boolean canConnectMatchingShapes(BlockAndTintGetter level, BlockPos fromPos, BlockPos toPos, Direction renderedFace) {
        int dx = toPos.getX() - fromPos.getX();
        int dy = toPos.getY() - fromPos.getY();
        int dz = toPos.getZ() - fromPos.getZ();
        int changedAxes = (dx == 0 ? 0 : 1) + (dy == 0 ? 0 : 1) + (dz == 0 ? 0 : 1);

        if (changedAxes == 0) {
            return true;
        }

        BlockState fromState = level.getBlockState(fromPos);
        BlockState toState = level.getBlockState(toPos);
        VoxelShape fromShape = fromState.getShape(level, fromPos);
        VoxelShape toShape = toState.getShape(level, toPos);

        if (changedAxes == 1 && Math.abs(dx + dy + dz) == 1) {
            Direction direction = Direction.getApproximateNearest(dx, dy, dz);
            VoxelShape fromFace = fromShape.getFaceShape(direction);
            VoxelShape toFace = toShape.getFaceShape(direction.getOpposite());
            boolean result = shapesOverlap(fromFace, toFace);
            CopycatsDebug.log("shape", () -> "cardinal contact from=" + fromPos + " " + fromState
                    + " to=" + toPos + " " + toState + " direction=" + direction
                    + " fromBounds=" + bounds(fromFace) + " toBounds=" + bounds(toFace)
                    + " overlap=" + result);
            return result;
        }

        VoxelShape fromFace = fromShape.getFaceShape(renderedFace);
        VoxelShape toFace = toShape.getFaceShape(renderedFace);
        boolean result = shapesOverlap(fromFace, toFace);
        CopycatsDebug.log("shape", () -> "diagonal/render face from=" + fromPos + " " + fromState
                + " to=" + toPos + " " + toState + " face=" + renderedFace
                + " fromBounds=" + bounds(fromFace) + " toBounds=" + bounds(toFace)
                + " overlap=" + result);
        return result;
    }

    public static boolean isFullCube(VoxelShape shape) {
        return !shape.isEmpty() && !Shapes.joinIsNotEmpty(shape, Shapes.block(), BooleanOp.NOT_SAME);
    }

    private static boolean shapesOverlap(VoxelShape first, VoxelShape second) {
        return !first.isEmpty() && !second.isEmpty()
                && Shapes.joinIsNotEmpty(first, second, BooleanOp.AND);
    }

    public static boolean isFaceCovered(BlockAndTintGetter level, BlockPos pos, BlockState state,
                                        BlockPos neighborPos, BlockState neighborState, Direction face) {
        VoxelShape faceShape = state.getShape(level, pos).getFaceShape(face);
        VoxelShape neighborFaceShape = neighborState.getShape(level, neighborPos).getFaceShape(face.getOpposite());
        boolean result = !faceShape.isEmpty()
                && !neighborFaceShape.isEmpty()
                && !Shapes.joinIsNotEmpty(faceShape, neighborFaceShape, BooleanOp.ONLY_FIRST);
        CopycatsDebug.log("shape", () -> "face covered pos=" + pos + " " + state
                + " neighbor=" + neighborPos + " " + neighborState
                + " face=" + face
                + " self=" + bounds(faceShape)
                + " neighbor=" + bounds(neighborFaceShape)
                + " result=" + result);
        return result;
    }

    public static boolean renderedFacesOverlap(BlockAndTintGetter level, BlockPos pos, BlockState state,
                                               BlockPos neighborPos, BlockState neighborState, Direction renderedFace) {
        VoxelShape faceShape = state.getShape(level, pos).getFaceShape(renderedFace);
        VoxelShape neighborFaceShape = neighborState.getShape(level, neighborPos).getFaceShape(renderedFace);
        boolean result = shapesOverlap(faceShape, neighborFaceShape);
        CopycatsDebug.log("shape", () -> "rendered faces overlap pos=" + pos + " " + state
                + " neighbor=" + neighborPos + " " + neighborState
                + " face=" + renderedFace
                + " self=" + bounds(faceShape)
                + " neighbor=" + bounds(neighborFaceShape)
                + " result=" + result);
        return result;
    }

    private static String bounds(VoxelShape shape) {
        return shape.isEmpty() ? "empty" : shape.bounds().toString();
    }
}
