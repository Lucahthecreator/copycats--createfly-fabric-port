package com.copycatsplus.copycats.utility;

import com.copycatsplus.copycats.content.copycat.slice.CopycatSliceBlock;
import com.mojang.math.OctahedralGroup;
import com.zurrtum.create.catnip.data.Iterate;
import com.zurrtum.create.content.contraptions.StructureTransform;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.Vec3;

public class BlockUtils {
    public static BlockState tryCopyProperties(BlockState from, BlockState to) {
        for (Property<?> property : from.getProperties()) {
            to = tryCopyProperty(from, to, property);
        }

        return to;
    }

    public static <T extends Comparable<T>> BlockState tryCopyProperty(BlockState from, BlockState to, Property<T> property) {
        return from.hasProperty(property) && to.hasProperty(property) ? to.setValue(property, from.getValue(property)) : to;
    }

    public static Direction transformFacing(StructureTransform transform, Direction facing) {
        if (transform.mirror != null && transform.mirror != Mirror.NONE) {
            facing = transform.mirrorFacing(facing);
        }

        if (transform.rotationAxis != null) {
            facing = transform.rotateFacing(facing);
        }

        return facing;
    }

    public static BlockState transformCornerLike(BlockState state, StructureTransform transform) {
        return vecToCorner(state, transform.applyWithoutOffset(cornerToVec(state)));
    }

    public static Vec3 cornerToVec(BlockState state) {
        Direction facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
        double x = facing != Direction.EAST && facing != Direction.SOUTH ? 0.0 : 1.0;
        double z = facing != Direction.SOUTH && facing != Direction.WEST ? 0.0 : 1.0;
        double y = state.getValue(CopycatSliceBlock.HALF) == Half.TOP ? 1.0 : 0.0;
        return new Vec3(x, y, z);
    }

    public static BlockState vecToCorner(BlockState state, Vec3 vec) {
        Direction facing = vec.x > 0.5 ? (vec.z > 0.5 ? Direction.SOUTH : Direction.EAST) : (vec.z > 0.5 ? Direction.WEST : Direction.NORTH);
        Half half = vec.y > 0.5 ? Half.TOP : Half.BOTTOM;
        return state.setValue(BlockStateProperties.HORIZONTAL_FACING, facing).setValue(CopycatSliceBlock.HALF, half);
    }

    public static BlockState transformStepLikeHorizontal(BlockState state, StructureTransform transform, BlockState verticalState) {
        if (transform.mirror != null && transform.mirror != Mirror.NONE) {
            if (transform.mirror.rotation() == OctahedralGroup.INVERT_Y) {
                state = state.cycle(CopycatSliceBlock.HALF);
            } else {
                state = state.setValue(BlockStateProperties.HORIZONTAL_FACING, transform.mirror.mirror(state.getValue(BlockStateProperties.HORIZONTAL_FACING)));
            }
        }

        if (transform.rotationAxis != null) {
            if (transform.rotationAxis == Axis.Y) {
                state = state.setValue(BlockStateProperties.HORIZONTAL_FACING, transform.rotateFacing(state.getValue(BlockStateProperties.HORIZONTAL_FACING)));
            } else {
                Direction facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
                Half half = state.getValue(CopycatSliceBlock.HALF);
                if (transform.rotationAxis == facing.getAxis()) {
                    if (transform.rotation == Rotation.CLOCKWISE_180) {
                        state = state.cycle(CopycatSliceBlock.HALF);
                    } else if (transform.rotation != Rotation.NONE) {
                        Direction offset = transform.rotateFacing(half == Half.TOP ? Direction.UP : Direction.DOWN);
                        boolean isClockwise = offset == facing.getClockWise();
                        state = tryCopyProperties(state, verticalState)
                            .setValue(BlockStateProperties.HORIZONTAL_FACING, isClockwise ? facing.getClockWise() : facing);
                    }
                } else {
                    state = setApparentDirection(state, transform.rotateFacing(getApparentDirection(state)));
                }
            }
        }

        return state;
    }

    public static BlockState transformStepLikeVertical(BlockState state, StructureTransform transform, BlockState horizontalState) {
        if (transform.mirror != null && transform.mirror != Mirror.NONE) {
            Axis mirrorAxis = null;

            for (Axis axis : Iterate.axes) {
                if (transform.mirror.rotation().inverts(axis)) {
                    mirrorAxis = axis;
                    break;
                }
            }

            if (mirrorAxis != null && !mirrorAxis.isVertical()) {
                Direction facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
                if (facing.getAxis() != mirrorAxis) {
                    state = state.setValue(BlockStateProperties.HORIZONTAL_FACING, facing.getClockWise());
                } else {
                    state = state.setValue(BlockStateProperties.HORIZONTAL_FACING, facing.getCounterClockWise());
                }
            }
        }

        if (transform.rotationAxis != null) {
            if (transform.rotationAxis == Axis.Y) {
                state = state.setValue(BlockStateProperties.HORIZONTAL_FACING, transform.rotateFacing(state.getValue(BlockStateProperties.HORIZONTAL_FACING)));
            } else {
                Direction facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
                if (facing.getAxis() == transform.rotationAxis) {
                    if (transform.rotation == Rotation.CLOCKWISE_180) {
                        state = state.setValue(BlockStateProperties.HORIZONTAL_FACING, facing.getClockWise());
                    } else if (transform.rotation != Rotation.NONE) {
                        state = tryCopyProperties(state, horizontalState)
                            .setValue(BlockStateProperties.HORIZONTAL_FACING, facing)
                            .setValue(
                                CopycatSliceBlock.HALF,
                                transform.rotation == Rotation.CLOCKWISE_90 == (facing.getAxisDirection() == AxisDirection.POSITIVE) ? Half.BOTTOM : Half.TOP
                            );
                    }
                } else if (transform.rotation == Rotation.CLOCKWISE_180) {
                    state = state.setValue(BlockStateProperties.HORIZONTAL_FACING, facing.getCounterClockWise());
                } else if (transform.rotation != Rotation.NONE) {
                    state = tryCopyProperties(state, horizontalState)
                        .setValue(BlockStateProperties.HORIZONTAL_FACING, facing.getCounterClockWise())
                        .setValue(
                            CopycatSliceBlock.HALF,
                            transform.rotation
                                    == Rotation.CLOCKWISE_90
                                    == (facing.getAxisDirection() == AxisDirection.POSITIVE)
                                    == (facing.getAxis() == Axis.X)
                                ? Half.BOTTOM
                                : Half.TOP
                        );
                }
            }
        }

        return state;
    }

    public static Direction getApparentDirection(BlockState state) {
        Direction facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
        Half half = state.getValue(CopycatSliceBlock.HALF);
        boolean positive = facing.getAxisDirection() == AxisDirection.POSITIVE;
        if (facing.getAxis() == Axis.X) {
            boolean aligned = positive == (half == Half.TOP);
            return aligned ? facing : (positive ? Direction.DOWN : Direction.UP);
        } else {
            boolean aligned = positive == (half == Half.BOTTOM);
            return aligned ? facing : (positive ? Direction.UP : Direction.DOWN);
        }
    }

    public static BlockState setApparentDirection(BlockState state, Direction direction) {
        Axis axis = state.getValue(BlockStateProperties.HORIZONTAL_FACING).getAxis();
        if (axis == Axis.X) {
            return switch (direction) {
                case UP -> (BlockState)state.setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.WEST).setValue(CopycatSliceBlock.HALF, Half.TOP);
                case DOWN -> (BlockState)state.setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.EAST).setValue(CopycatSliceBlock.HALF, Half.BOTTOM);
                case EAST -> (BlockState)state.setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.EAST).setValue(CopycatSliceBlock.HALF, Half.TOP);
                case WEST -> (BlockState)state.setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.WEST).setValue(CopycatSliceBlock.HALF, Half.BOTTOM);
                default -> throw new IllegalStateException("Unexpected value: " + direction);
            };
        } else {
            return switch (direction) {
                case UP -> (BlockState)state.setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH).setValue(CopycatSliceBlock.HALF, Half.TOP);
                case DOWN -> (BlockState)state.setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH).setValue(CopycatSliceBlock.HALF, Half.BOTTOM);
                default -> throw new IllegalStateException("Unexpected value: " + direction);
                case NORTH -> (BlockState)state.setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH).setValue(CopycatSliceBlock.HALF, Half.TOP);
                case SOUTH -> (BlockState)state.setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH).setValue(CopycatSliceBlock.HALF, Half.BOTTOM);
            };
        }
    }
}
