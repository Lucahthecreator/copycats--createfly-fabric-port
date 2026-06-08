/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.math.DoubleMath
 *  it.unimi.dsi.fastutil.objects.Object2ByteLinkedOpenHashMap
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.core.Direction$Axis
 *  net.minecraft.core.Direction$AxisDirection
 *  net.minecraft.core.Vec3i
 *  net.minecraft.util.Mth
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.phys.shapes.BooleanOp
 *  net.minecraft.world.phys.shapes.Shapes
 *  net.minecraft.world.phys.shapes.SliceShape
 *  net.minecraft.world.phys.shapes.VoxelShape
 */
package com.copycatsplus.copycats.utility;

import com.copycatsplus.copycats.foundation.copycat.CopycatExternalContext;
import com.copycatsplus.copycats.foundation.copycat.model.ScaledBlockAndTintGetter;
import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlock;
import com.copycatsplus.copycats.utility.MathUtils;
import com.google.common.math.DoubleMath;
import it.unimi.dsi.fastutil.objects.Object2ByteLinkedOpenHashMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.SliceShape;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockFaceUtils {
    private static final ThreadLocal<Map<BiFunction<VoxelShape, VoxelShape, Boolean>, Object2ByteLinkedOpenHashMap<ShapeKey>>> SHAPE_OP_CACHE = ThreadLocal.withInitial(HashMap::new);

    private static boolean invokeCachedOperation(BiFunction<VoxelShape, VoxelShape, Boolean> operation, VoxelShape fromShape, VoxelShape toShape) {
        ShapeKey key;
        Object2ByteLinkedOpenHashMap cache = SHAPE_OP_CACHE.get().computeIfAbsent(operation, op -> {
            Object2ByteLinkedOpenHashMap<ShapeKey> cacheMap = new Object2ByteLinkedOpenHashMap<ShapeKey>(2048, 0.25f){

                protected void rehash(int i) {
                }
            };
            cacheMap.defaultReturnValue((byte)127);
            return cacheMap;
        });
        byte result = cache.getAndMoveToFirst((Object)(key = new ShapeKey(fromShape, toShape)));
        if (result == 127) {
            boolean value = operation.apply(fromShape, toShape);
            if (cache.size() >= 2048) {
                cache.removeLastByte();
            }
            cache.putAndMoveToFirst((Object)key, (byte)(value ? 1 : 0));
            return value;
        }
        return result == 1;
    }

    private static boolean processBlockFace(BlockGetter level, BlockState fromState, BlockPos fromPos, BlockState toState, BlockPos toPos, Direction fromFace, BiFunction<VoxelShape, VoxelShape, Boolean> operation) {
        VoxelShape fromShape = null;
        Vec3i scale = new Vec3i(1, 1, 1);
        Vec3i inner = new Vec3i(0, 0, 0);
        BlockPos truePos = fromPos;
        if (level instanceof ScaledBlockAndTintGetter) {
            IMultiStateCopycatBlock copycatBlock;
            ScaledBlockAndTintGetter scaledWorld = (ScaledBlockAndTintGetter)level;
            scale = scaledWorld.getScale();
            truePos = scaledWorld.getTruePos(fromPos);
            Block block = fromState.getBlock();
            if (block instanceof IMultiStateCopycatBlock && (copycatBlock = (IMultiStateCopycatBlock)block).vectorScale(fromState).equals((Object)scaledWorld.getScale())) {
                String property = scaledWorld.getPropertyForRender(fromState, fromPos);
                if (!copycatBlock.partExists(fromState, property)) {
                    return false;
                }
                inner = copycatBlock.getVectorFromProperty(fromState, property);
                fromShape = copycatBlock.getPartialFaceShape((BlockGetter)scaledWorld, fromState, property, fromFace);
            }
        }
        if (fromShape == null) {
            fromShape = fromState.getFaceOcclusionShape(fromFace);
        }
        if (fromShape.isEmpty()) {
            return false;
        }
        VoxelShape toShape = null;
        List<Object> potentialParts = null;
        Block block = toState.getBlock();
        if (block instanceof IMultiStateCopycatBlock) {
            IMultiStateCopycatBlock copycatBlock = (IMultiStateCopycatBlock)block;
            Vec3i toScale = copycatBlock.vectorScale(toState);
            Direction.Axis connectingAxis = fromFace.getAxis();
            BlockGetter world = level;
            BlockPos toTruePos = toPos;
            String fallbackProperty = copycatBlock.defaultProperty();
            if (level instanceof ScaledBlockAndTintGetter) {
                ScaledBlockAndTintGetter scaledWorld = (ScaledBlockAndTintGetter)level;
                world = scaledWorld.getWrapped();
                toTruePos = scaledWorld.getTruePos(toPos);
                fallbackProperty = scaledWorld.getRenderingProperty();
                if (toTruePos.equals((Object)truePos)) {
                    potentialParts = List.of(scaledWorld.getPropertyForRender(toState, toPos));
                }
            }
            if (potentialParts == null) {
                if (MathUtils.replaceAxis(scale, connectingAxis, 0).equals((Object)MathUtils.replaceAxis(toScale, connectingAxis, 0))) {
                    potentialParts = List.of(copycatBlock.getPropertyFromRender(fallbackProperty, toState, world, MathUtils.replaceAxis(inner, connectingAxis, fromFace.getAxisDirection() == Direction.AxisDirection.POSITIVE ? 0 : toScale.get(connectingAxis) - 1), toTruePos));
                } else {
                    potentialParts = new ArrayList(4);
                    int connectingPart = fromFace.getAxisDirection() == Direction.AxisDirection.POSITIVE ? 0 : toScale.get(connectingAxis) - 1;
                    Direction.Axis axis = switch (connectingAxis) {
                        default -> throw new MatchException(null, null);
                        case Direction.Axis.X -> Direction.Axis.Y;
                        case Direction.Axis.Y -> Direction.Axis.Z;
                        case Direction.Axis.Z -> Direction.Axis.X;
                    };
                    Direction.Axis axis2 = switch (connectingAxis) {
                        default -> throw new MatchException(null, null);
                        case Direction.Axis.X -> Direction.Axis.Z;
                        case Direction.Axis.Y -> Direction.Axis.X;
                        case Direction.Axis.Z -> Direction.Axis.Y;
                    };
                    for (int i = 0; i < toScale.get(axis); ++i) {
                        for (int j = 0; j < toScale.get(axis2); ++j) {
                            potentialParts.add(copycatBlock.getPropertyFromRender(fallbackProperty, toState, world, MathUtils.replaceAxis(MathUtils.replaceAxis(new Vec3i(connectingPart, connectingPart, connectingPart), axis, i), axis2, j), toTruePos));
                        }
                    }
                }
            }
            for (String string : potentialParts) {
                toShape = copycatBlock.getPartialFaceShape(world, toState, string, fromFace.getOpposite());
                if (!BlockFaceUtils.invokeCachedOperation(operation, fromShape, toShape)) continue;
                String property = CopycatExternalContext.getPropertyForAppearance();
                if (property == null) {
                    property = copycatBlock.defaultProperty();
                }
                CopycatExternalContext.setPropertyForAppearance(copycatBlock.getPropertyFromRender(property, toState, world, copycatBlock.getVectorFromProperty(toState, string), toTruePos));
                return true;
            }
        }
        if (toShape == null) {
            toShape = toState.getFaceOcclusionShape(fromFace.getOpposite());
        }
        CopycatExternalContext.setPropertyForAppearance(null);
        return BlockFaceUtils.invokeCachedOperation(operation, fromShape, toShape);
    }

    public static boolean canOcclude(BlockGetter level, BlockState occludedState, BlockPos occludedPos, BlockState occludingState, BlockPos occludingPos, Direction occludedFace) {
        return BlockFaceUtils.processBlockFace(level, occludedState, occludedPos, occludingState, occludingPos, occludedFace, (occluded, occluding) -> !Shapes.joinIsNotEmpty((VoxelShape)occluded, (VoxelShape)occluding, (BooleanOp)BooleanOp.ONLY_FIRST));
    }

    public static boolean faceMatch(BlockGetter level, BlockState fromState, BlockPos fromPos, BlockState toState, BlockPos toPos, Direction fromFace) {
        return BlockFaceUtils.processBlockFace(level, fromState, fromPos, toState, toPos, fromFace, (from, to) -> !Shapes.joinIsNotEmpty((VoxelShape)from, (VoxelShape)to, (BooleanOp)BooleanOp.NOT_SAME));
    }

    public static VoxelShape getPartialFaceShape(BlockGetter level, BlockState state, String property, Direction face) {
        IMultiStateCopycatBlock copycatBlock = (IMultiStateCopycatBlock)state.getBlock();
        Vec3i scale = copycatBlock.vectorScale(state);
        Vec3i part = copycatBlock.getVectorFromProperty(state, property);
        return BlockFaceUtils.getPartialFaceShape(state.getOcclusionShape(), face, part, scale);
    }

    public static VoxelShape getPartialFaceShape(VoxelShape voxelShape, Direction direction, Vec3i part, Vec3i scale) {
        int i;
        if (voxelShape == null) {
            voxelShape = Shapes.block();
        }
        Direction.Axis axis = direction.getAxis();
        double startX = (double)part.getX() / (double)scale.getX();
        double startY = (double)part.getY() / (double)scale.getY();
        double startZ = (double)part.getZ() / (double)scale.getZ();
        double sizeX = 1.0 / (double)scale.getX();
        double sizeY = 1.0 / (double)scale.getY();
        double sizeZ = 1.0 / (double)scale.getZ();
        double endX = startX + sizeX;
        double endY = startY + sizeY;
        double endZ = startZ + sizeZ;
        VoxelShape bounds = Shapes.box((double)startX, (double)startY, (double)startZ, (double)endX, (double)endY, (double)endZ);
        voxelShape = Shapes.joinUnoptimized((VoxelShape)voxelShape, (VoxelShape)bounds, (BooleanOp)BooleanOp.AND);
        int axisSize = voxelShape.getCoords(axis).size() - 1;
        boolean isEmpty = false;
        if (direction.getAxisDirection() == Direction.AxisDirection.POSITIVE) {
            double d = voxelShape.max(axis);
            isEmpty = DoubleMath.fuzzyCompare((double)d, (double)(switch (axis) {
                default -> throw new MatchException(null, null);
                case Direction.Axis.X -> endX;
                case Direction.Axis.Y -> endY;
                case Direction.Axis.Z -> endZ;
            }), (double)1.0E-7) < 0;
            i = Mth.floor((double)Mth.clamp((double)((double)axisSize * axis.choose(endX, endY, endZ)), (double)-1.0, (double)axisSize)) - 1;
        } else {
            double d = voxelShape.min(axis);
            isEmpty = DoubleMath.fuzzyCompare((double)d, (double)(switch (axis) {
                default -> throw new MatchException(null, null);
                case Direction.Axis.X -> startX;
                case Direction.Axis.Y -> startY;
                case Direction.Axis.Z -> startZ;
            }), (double)1.0E-7) > 0;
            i = Mth.floor((double)Mth.clamp((double)((double)axisSize * axis.choose(startX, startY, startZ)), (double)-1.0, (double)axisSize));
        }
        if (isEmpty) {
            return Shapes.empty();
        }
        return new SliceShape(voxelShape, axis, i);
    }

    public record ShapeKey(VoxelShape fromShape, VoxelShape toShape) {
    }
}

