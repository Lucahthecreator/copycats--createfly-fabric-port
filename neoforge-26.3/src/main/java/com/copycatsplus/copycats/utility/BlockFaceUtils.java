package com.copycatsplus.copycats.utility;

import com.copycatsplus.copycats.foundation.copycat.CopycatExternalContext;
import com.copycatsplus.copycats.foundation.copycat.model.ScaledBlockAndTintGetter;
import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlock;
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
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.util.Mth;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.SliceShape;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockFaceUtils {
    private static final ThreadLocal<Map<BiFunction<VoxelShape, VoxelShape, Boolean>, Object2ByteLinkedOpenHashMap<BlockFaceUtils.ShapeKey>>> SHAPE_OP_CACHE = ThreadLocal.withInitial(
        HashMap::new
    );

    private static boolean invokeCachedOperation(BiFunction<VoxelShape, VoxelShape, Boolean> operation, VoxelShape fromShape, VoxelShape toShape) {
        Object2ByteLinkedOpenHashMap<BlockFaceUtils.ShapeKey> cache = SHAPE_OP_CACHE.get().computeIfAbsent(operation, op -> {
            Object2ByteLinkedOpenHashMap<BlockFaceUtils.ShapeKey> cacheMap = new Object2ByteLinkedOpenHashMap<BlockFaceUtils.ShapeKey>(2048, 0.25F) {
                protected void rehash(int i) {
                }
            };
            cacheMap.defaultReturnValue((byte)127);
            return cacheMap;
        });
        BlockFaceUtils.ShapeKey key = new BlockFaceUtils.ShapeKey(fromShape, toShape);
        byte result = cache.getAndMoveToFirst(key);
        if (result == 127) {
            boolean value = operation.apply(fromShape, toShape);
            if (cache.size() >= 2048) {
                cache.removeLastByte();
            }

            cache.putAndMoveToFirst(key, (byte)(value ? 1 : 0));
            return value;
        } else {
            return result == 1;
        }
    }

    private static boolean processBlockFace(
        BlockGetter level,
        BlockState fromState,
        BlockPos fromPos,
        BlockState toState,
        BlockPos toPos,
        Direction fromFace,
        BiFunction<VoxelShape, VoxelShape, Boolean> operation
    ) {
        VoxelShape fromShape = null;
        Vec3i scale = new Vec3i(1, 1, 1);
        Vec3i inner = new Vec3i(0, 0, 0);
        BlockPos truePos = fromPos;
        if (level instanceof ScaledBlockAndTintGetter scaledWorld) {
            scale = scaledWorld.getScale();
            truePos = scaledWorld.getTruePos(fromPos);
            if (fromState.getBlock() instanceof IMultiStateCopycatBlock copycatBlock && copycatBlock.vectorScale(fromState).equals(scaledWorld.getScale())) {
                String property = scaledWorld.getPropertyForRender(fromState, fromPos);
                if (!copycatBlock.partExists(fromState, property)) {
                    return false;
                }

                inner = copycatBlock.getVectorFromProperty(fromState, property);
                fromShape = copycatBlock.getPartialFaceShape(scaledWorld, fromState, property, fromFace);
            }
        }

        if (fromShape == null) {
            fromShape = fromState.getFaceOcclusionShape(fromFace);
        }

        if (fromShape.isEmpty()) {
            return false;
        } else {
            VoxelShape toShape = null;
            List<String> potentialParts = null;
            if (toState.getBlock() instanceof IMultiStateCopycatBlock copycatBlock) {
                Vec3i toScale = copycatBlock.vectorScale(toState);
                Axis connectingAxis = fromFace.getAxis();
                BlockGetter world = level;
                BlockPos toTruePos = toPos;
                String fallbackProperty = copycatBlock.defaultProperty();
                if (level instanceof ScaledBlockAndTintGetter scaledWorldx) {
                    world = scaledWorldx.getWrapped();
                    toTruePos = scaledWorldx.getTruePos(toPos);
                    fallbackProperty = scaledWorldx.getRenderingProperty();
                    if (toTruePos.equals(truePos)) {
                        potentialParts = List.of(scaledWorldx.getPropertyForRender(toState, toPos));
                    }
                }

                if (potentialParts == null) {
                    if (MathUtils.replaceAxis(scale, connectingAxis, 0).equals(MathUtils.replaceAxis(toScale, connectingAxis, 0))) {
                        potentialParts = List.of(
                            copycatBlock.getPropertyFromRender(
                                fallbackProperty,
                                toState,
                                world,
                                MathUtils.replaceAxis(
                                    inner, connectingAxis, fromFace.getAxisDirection() == AxisDirection.POSITIVE ? 0 : toScale.get(connectingAxis) - 1
                                ),
                                toTruePos
                            )
                        );
                    } else {
                        potentialParts = new ArrayList<>(4);
                        int connectingPart = fromFace.getAxisDirection() == AxisDirection.POSITIVE ? 0 : toScale.get(connectingAxis) - 1;

                        Axis axis1 = switch (connectingAxis) {
                            case X -> Axis.Y;
                            case Y -> Axis.Z;
                            case Z -> Axis.X;
                        };

                        Axis axis2 = switch (connectingAxis) {
                            case X -> Axis.Z;
                            case Y -> Axis.X;
                            case Z -> Axis.Y;
                        };

                        for (int i = 0; i < toScale.get(axis1); i++) {
                            for (int j = 0; j < toScale.get(axis2); j++) {
                                potentialParts.add(
                                    copycatBlock.getPropertyFromRender(
                                        fallbackProperty,
                                        toState,
                                        world,
                                        MathUtils.replaceAxis(
                                            MathUtils.replaceAxis(new Vec3i(connectingPart, connectingPart, connectingPart), axis1, i), axis2, j
                                        ),
                                        toTruePos
                                    )
                                );
                            }
                        }
                    }
                }

                for (String partProp : potentialParts) {
                    toShape = copycatBlock.getPartialFaceShape(world, toState, partProp, fromFace.getOpposite());
                    if (invokeCachedOperation(operation, fromShape, toShape)) {
                        String property = CopycatExternalContext.getPropertyForAppearance();
                        if (property == null) {
                            property = copycatBlock.defaultProperty();
                        }

                        CopycatExternalContext.setPropertyForAppearance(
                            copycatBlock.getPropertyFromRender(property, toState, world, copycatBlock.getVectorFromProperty(toState, partProp), toTruePos)
                        );
                        return true;
                    }
                }
            }

            if (toShape == null) {
                toShape = toState.getFaceOcclusionShape(fromFace.getOpposite());
            }

            CopycatExternalContext.setPropertyForAppearance(null);
            return invokeCachedOperation(operation, fromShape, toShape);
        }
    }

    public static boolean canOcclude(
        BlockGetter level, BlockState occludedState, BlockPos occludedPos, BlockState occludingState, BlockPos occludingPos, Direction occludedFace
    ) {
        return processBlockFace(
            level,
            occludedState,
            occludedPos,
            occludingState,
            occludingPos,
            occludedFace,
            (occluded, occluding) -> !Shapes.joinIsNotEmpty(occluded, occluding, BooleanOp.ONLY_FIRST)
        );
    }

    public static boolean faceMatch(BlockGetter level, BlockState fromState, BlockPos fromPos, BlockState toState, BlockPos toPos, Direction fromFace) {
        return processBlockFace(level, fromState, fromPos, toState, toPos, fromFace, (from, to) -> !Shapes.joinIsNotEmpty(from, to, BooleanOp.NOT_SAME));
    }

    public static VoxelShape getPartialFaceShape(BlockGetter level, BlockState state, String property, Direction face) {
        IMultiStateCopycatBlock copycatBlock = (IMultiStateCopycatBlock)state.getBlock();
        Vec3i scale = copycatBlock.vectorScale(state);
        Vec3i part = copycatBlock.getVectorFromProperty(state, property);
        return getPartialFaceShape(state.getOcclusionShape(), face, part, scale);
    }

    public static VoxelShape getPartialFaceShape(VoxelShape voxelShape, Direction direction, Vec3i part, Vec3i scale) {
        if (voxelShape == null) {
            voxelShape = Shapes.block();
        }

        Axis axis = direction.getAxis();
        double startX = (double)part.getX() / scale.getX();
        double startY = (double)part.getY() / scale.getY();
        double startZ = (double)part.getZ() / scale.getZ();
        double sizeX = 1.0 / scale.getX();
        double sizeY = 1.0 / scale.getY();
        double sizeZ = 1.0 / scale.getZ();
        double endX = startX + sizeX;
        double endY = startY + sizeY;
        double endZ = startZ + sizeZ;
        VoxelShape bounds = Shapes.box(startX, startY, startZ, endX, endY, endZ);
        voxelShape = Shapes.joinUnoptimized(voxelShape, bounds, BooleanOp.AND);
        int axisSize = voxelShape.getCoords(axis).size() - 1;
        boolean isEmpty = false;
        int i;
        if (direction.getAxisDirection() == AxisDirection.POSITIVE) {
            isEmpty = DoubleMath.fuzzyCompare(voxelShape.max(axis), switch (axis) {
                case X -> endX;
                case Y -> endY;
                case Z -> endZ;
            }, 1.0E-7) < 0;
            i = Mth.floor(Mth.clamp(axisSize * axis.choose(endX, endY, endZ), -1.0, (double)axisSize)) - 1;
        } else {
            isEmpty = DoubleMath.fuzzyCompare(voxelShape.min(axis), switch (axis) {
                case X -> startX;
                case Y -> startY;
                case Z -> startZ;
            }, 1.0E-7) > 0;
            i = Mth.floor(Mth.clamp(axisSize * axis.choose(startX, startY, startZ), -1.0, (double)axisSize));
        }

        return (VoxelShape)(isEmpty ? Shapes.empty() : new SliceShape(voxelShape, axis, i));
    }

    public record ShapeKey(VoxelShape fromShape, VoxelShape toShape) {
    }
}
