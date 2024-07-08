package com.copycatsplus.copycats.utility;

import it.unimi.dsi.fastutil.objects.Object2ByteLinkedOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class OcclusionUtils {
    private static final ThreadLocal<Object2ByteLinkedOpenHashMap<Block.BlockStatePairKey>> OCCLUSION_CACHE = ThreadLocal.withInitial(() -> {
        Object2ByteLinkedOpenHashMap<Block.BlockStatePairKey> object2bytelinkedopenhashmap = new Object2ByteLinkedOpenHashMap<>(2048, 0.25f) {

            @Override
            protected void rehash(int i) {
            }
        };
        object2bytelinkedopenhashmap.defaultReturnValue((byte) 127);
        return object2bytelinkedopenhashmap;
    });

    public static boolean facesMatch(BlockGetter level, BlockState fromState, BlockPos fromPos, BlockState toState, BlockPos toPos, Direction fromFace) {
        Block.BlockStatePairKey blockStatePair = new Block.BlockStatePairKey(fromState, toState, fromFace);
        Object2ByteLinkedOpenHashMap<Block.BlockStatePairKey> occlusionMap = OCCLUSION_CACHE.get();
        byte b0 = occlusionMap.getAndMoveToFirst(blockStatePair);
        if (b0 != 127) {
            return b0 == 0;
        }
        VoxelShape fromShape = fromState.getFaceOcclusionShape(level, fromPos, fromFace);
        if (fromShape.isEmpty()) {
            return false;
        }
        VoxelShape toShape = toState.getFaceOcclusionShape(level, toPos, fromFace.getOpposite());
        boolean mismatch = Shapes.joinIsNotEmpty(fromShape, toShape, BooleanOp.ONLY_FIRST);
        if (occlusionMap.size() == 2048) {
            occlusionMap.removeLastByte();
        }
        occlusionMap.putAndMoveToFirst(blockStatePair, (byte) (mismatch ? 1 : 0));
        return !mismatch;
    }
}
