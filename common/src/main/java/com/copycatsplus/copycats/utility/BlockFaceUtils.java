package com.copycatsplus.copycats.utility;

import com.copycatsplus.copycats.foundation.copycat.model.ScaledBlockAndTintGetter;
import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlock;
import com.copycatsplus.copycats.mixin.copycat.VoxelShapeAccessor;
import it.unimi.dsi.fastutil.objects.Object2ByteLinkedOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.*;

public class BlockFaceUtils {
    private static final ThreadLocal<Object2ByteLinkedOpenHashMap<Block.BlockStatePairKey>> FACE_MATCH_CACHE = ThreadLocal.withInitial(() -> {
        Object2ByteLinkedOpenHashMap<Block.BlockStatePairKey> cacheMap = new Object2ByteLinkedOpenHashMap<>(2048, 0.25f) {

            @Override
            protected void rehash(int i) {
            }
        };
        cacheMap.defaultReturnValue((byte) 127);
        return cacheMap;
    });

    public static boolean canShapeOcclude(BlockGetter level, BlockState occludedState, BlockPos occludedPos, BlockState occludingState, BlockPos occludingPos, Direction occludedFace) {
        if (level instanceof ScaledBlockAndTintGetter scaledWorld) {
            Vec3i scale = scaledWorld.getScale();
            VoxelShape occludedShape;
            BlockPos trueOccludedPos = scaledWorld.getTruePos(occludedPos);
            if (occludedState.getBlock() instanceof IMultiStateCopycatBlock copycatBlock && copycatBlock.vectorScale(occludedState).equals(scale)) {
                String occludedProperty = scaledWorld.getPropertyForRender(occludedState, occludedPos);
                if (!copycatBlock.partExists(occludedState, occludedProperty))
                    return false;
                Vec3i occludedInner = copycatBlock.getVectorFromProperty(occludedState, occludedProperty);
                occludedShape = getPartialFaceShape(occludedState.getOcclusionShape(scaledWorld.getWrapped(), trueOccludedPos),
                        occludedFace,
                        occludedInner.getX() / (double) scale.getX(),
                        occludedInner.getY() / (double) scale.getY(),
                        occludedInner.getZ() / (double) scale.getZ(),
                        1.0 / scale.getX(),
                        1.0 / scale.getY(),
                        1.0 / scale.getZ()
                );
            } else {
                occludedShape = occludedState.getFaceOcclusionShape(scaledWorld.getWrapped(), trueOccludedPos, occludedFace);
            }
            if (occludedShape.isEmpty()) {
                return false;
            }
            VoxelShape occludingShape;
            BlockPos trueOccludingPos = scaledWorld.getTruePos(occludingPos);
            if (occludingState.getBlock() instanceof IMultiStateCopycatBlock copycatBlock2 && copycatBlock2.vectorScale(occludingState).equals(scaledWorld.getScale())) {
                String occludingProperty = scaledWorld.getPropertyForRender(occludingState, occludingPos);
                Vec3i occludingInner = copycatBlock2.getVectorFromProperty(occludingState, occludingProperty);
                occludingShape = getPartialFaceShape(occludingState.getOcclusionShape(scaledWorld.getWrapped(), trueOccludingPos),
                        occludedFace.getOpposite(),
                        occludingInner.getX() / (double) scale.getX(),
                        occludingInner.getY() / (double) scale.getY(),
                        occludingInner.getZ() / (double) scale.getZ(),
                        1.0 / scale.getX(),
                        1.0 / scale.getY(),
                        1.0 / scale.getZ()
                );
            } else {
                occludingShape = occludingState.getFaceOcclusionShape(scaledWorld.getWrapped(), trueOccludingPos, occludedFace.getOpposite());
            }
            return !Shapes.joinIsNotEmpty(occludedShape, occludingShape, BooleanOp.ONLY_FIRST);
        }

        Block.BlockStatePairKey blockStatePair = new Block.BlockStatePairKey(occludedState, occludingState, occludedFace);
        Object2ByteLinkedOpenHashMap<Block.BlockStatePairKey> occlusionMap = FACE_MATCH_CACHE.get();
        byte cached = occlusionMap.getAndMoveToFirst(blockStatePair);
        if (cached != 127) {
            return cached == 0;
        }
        VoxelShape occludedShape = occludedState.getFaceOcclusionShape(level, occludedPos, occludedFace);
        if (occludedShape.isEmpty()) {
            return false;
        }
        VoxelShape occludingShape = occludingState.getFaceOcclusionShape(level, occludingPos, occludedFace.getOpposite());
        boolean mismatch = Shapes.joinIsNotEmpty(occludedShape, occludingShape, BooleanOp.ONLY_FIRST);
        if (occlusionMap.size() == 2048) {
            occlusionMap.removeLastByte();
        }
        occlusionMap.putAndMoveToFirst(blockStatePair, (byte) (mismatch ? 1 : 0));
        return !mismatch;
    }

    public static VoxelShape getPartialFaceShape(VoxelShape voxelShape, Direction direction, double startX, double startY, double startZ, double sizeX, double sizeY, double sizeZ) {
        int i;
        Direction.Axis axis = direction.getAxis();
        double endX = startX + sizeX;
        double endY = startY + sizeY;
        double endZ = startZ + sizeZ;
        VoxelShape bounds = Shapes.box(startX, startY, startZ, endX, endY, endZ);
        voxelShape = Shapes.joinUnoptimized(voxelShape, bounds, BooleanOp.AND);
        int axisSize = ((VoxelShapeAccessor) voxelShape).copycats$getShape().getSize(axis);
        if (direction.getAxisDirection() == Direction.AxisDirection.POSITIVE) {
            i = Mth.floor(Mth.clamp(axisSize * axis.choose(endX, endY, endZ), -1, axisSize)) - 1;
        } else {
            i = Mth.floor(Mth.clamp(axisSize * axis.choose(startX, startY, startZ), -1, axisSize));
        }
        return new SliceShape(voxelShape, axis, i);
    }
}
