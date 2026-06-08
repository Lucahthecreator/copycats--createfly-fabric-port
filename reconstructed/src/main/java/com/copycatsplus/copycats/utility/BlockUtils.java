/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.math.OctahedralGroup
 *  com.zurrtum.create.catnip.data.Iterate
 *  com.zurrtum.create.content.contraptions.StructureTransform
 *  net.minecraft.core.Direction
 *  net.minecraft.core.Direction$Axis
 *  net.minecraft.core.Direction$AxisDirection
 *  net.minecraft.world.level.block.Mirror
 *  net.minecraft.world.level.block.Rotation
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.properties.BlockStateProperties
 *  net.minecraft.world.level.block.state.properties.Half
 *  net.minecraft.world.level.block.state.properties.Property
 *  net.minecraft.world.phys.Vec3
 */
package com.copycatsplus.copycats.utility;

import com.copycatsplus.copycats.content.copycat.slice.CopycatSliceBlock;
import com.mojang.math.OctahedralGroup;
import com.zurrtum.create.catnip.data.Iterate;
import com.zurrtum.create.content.contraptions.StructureTransform;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.Vec3;

public class BlockUtils {
    public static BlockState tryCopyProperties(BlockState from, BlockState to) {
        for (Property property : from.getProperties()) {
            to = BlockUtils.tryCopyProperty(from, to, property);
        }
        return to;
    }

    public static <T extends Comparable<T>> BlockState tryCopyProperty(BlockState from, BlockState to, Property<T> property) {
        if (from.hasProperty(property) && to.hasProperty(property)) {
            return (BlockState)to.setValue(property, from.getValue(property));
        }
        return to;
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
        return BlockUtils.vecToCorner(state, transform.applyWithoutOffset(BlockUtils.cornerToVec(state)));
    }

    public static Vec3 cornerToVec(BlockState state) {
        Direction facing = (Direction)state.getValue((Property)BlockStateProperties.HORIZONTAL_FACING);
        double x = facing == Direction.EAST || facing == Direction.SOUTH ? 1.0 : 0.0;
        double z = facing == Direction.SOUTH || facing == Direction.WEST ? 1.0 : 0.0;
        double y = state.getValue(CopycatSliceBlock.HALF) == Half.TOP ? 1.0 : 0.0;
        return new Vec3(x, y, z);
    }

    public static BlockState vecToCorner(BlockState state, Vec3 vec) {
        Direction facing = vec.x > 0.5 ? (vec.z > 0.5 ? Direction.SOUTH : Direction.EAST) : (vec.z > 0.5 ? Direction.WEST : Direction.NORTH);
        Half half = vec.y > 0.5 ? Half.TOP : Half.BOTTOM;
        return (BlockState)((BlockState)state.setValue((Property)BlockStateProperties.HORIZONTAL_FACING, (Comparable)facing)).setValue(CopycatSliceBlock.HALF, (Comparable)half);
    }

    public static BlockState transformStepLikeHorizontal(BlockState state, StructureTransform transform, BlockState verticalState) {
        if (transform.mirror != null && transform.mirror != Mirror.NONE) {
            state = transform.mirror.rotation() == OctahedralGroup.INVERT_Y ? (BlockState)state.cycle(CopycatSliceBlock.HALF) : (BlockState)state.setValue((Property)BlockStateProperties.HORIZONTAL_FACING, (Comparable)transform.mirror.mirror((Direction)state.getValue((Property)BlockStateProperties.HORIZONTAL_FACING)));
        }
        if (transform.rotationAxis != null) {
            if (transform.rotationAxis == Direction.Axis.Y) {
                state = (BlockState)state.setValue((Property)BlockStateProperties.HORIZONTAL_FACING, (Comparable)transform.rotateFacing((Direction)state.getValue((Property)BlockStateProperties.HORIZONTAL_FACING)));
            } else {
                Direction facing = (Direction)state.getValue((Property)BlockStateProperties.HORIZONTAL_FACING);
                Half half = (Half)state.getValue(CopycatSliceBlock.HALF);
                if (transform.rotationAxis == facing.getAxis()) {
                    if (transform.rotation == Rotation.CLOCKWISE_180) {
                        state = (BlockState)state.cycle(CopycatSliceBlock.HALF);
                    } else if (transform.rotation != Rotation.NONE) {
                        Direction offset = transform.rotateFacing(half == Half.TOP ? Direction.UP : Direction.DOWN);
                        boolean isClockwise = offset == facing.getClockWise();
                        state = (BlockState)BlockUtils.tryCopyProperties(state, verticalState).setValue((Property)BlockStateProperties.HORIZONTAL_FACING, (Comparable)(isClockwise ? facing.getClockWise() : facing));
                    }
                } else {
                    state = BlockUtils.setApparentDirection(state, transform.rotateFacing(BlockUtils.getApparentDirection(state)));
                }
            }
        }
        return state;
    }

    public static BlockState transformStepLikeVertical(BlockState state, StructureTransform transform, BlockState horizontalState) {
        if (transform.mirror != null && transform.mirror != Mirror.NONE) {
            Direction.Axis mirrorAxis = null;
            for (Direction.Axis axis : Iterate.axes) {
                if (!transform.mirror.rotation().inverts(axis)) continue;
                mirrorAxis = axis;
                break;
            }
            if (mirrorAxis != null && !mirrorAxis.isVertical()) {
                Direction facing = (Direction)state.getValue((Property)BlockStateProperties.HORIZONTAL_FACING);
                state = facing.getAxis() != mirrorAxis ? (BlockState)state.setValue((Property)BlockStateProperties.HORIZONTAL_FACING, (Comparable)facing.getClockWise()) : (BlockState)state.setValue((Property)BlockStateProperties.HORIZONTAL_FACING, (Comparable)facing.getCounterClockWise());
            }
        }
        if (transform.rotationAxis != null) {
            if (transform.rotationAxis == Direction.Axis.Y) {
                state = (BlockState)state.setValue((Property)BlockStateProperties.HORIZONTAL_FACING, (Comparable)transform.rotateFacing((Direction)state.getValue((Property)BlockStateProperties.HORIZONTAL_FACING)));
            } else {
                Direction facing = (Direction)state.getValue((Property)BlockStateProperties.HORIZONTAL_FACING);
                if (facing.getAxis() == transform.rotationAxis) {
                    if (transform.rotation == Rotation.CLOCKWISE_180) {
                        state = (BlockState)state.setValue((Property)BlockStateProperties.HORIZONTAL_FACING, (Comparable)facing.getClockWise());
                    } else if (transform.rotation != Rotation.NONE) {
                        state = (BlockState)((BlockState)BlockUtils.tryCopyProperties(state, horizontalState).setValue((Property)BlockStateProperties.HORIZONTAL_FACING, (Comparable)facing)).setValue(CopycatSliceBlock.HALF, (Comparable)(transform.rotation == Rotation.CLOCKWISE_90 == (facing.getAxisDirection() == Direction.AxisDirection.POSITIVE) ? Half.BOTTOM : Half.TOP));
                    }
                } else if (transform.rotation == Rotation.CLOCKWISE_180) {
                    state = (BlockState)state.setValue((Property)BlockStateProperties.HORIZONTAL_FACING, (Comparable)facing.getCounterClockWise());
                } else if (transform.rotation != Rotation.NONE) {
                    state = (BlockState)((BlockState)BlockUtils.tryCopyProperties(state, horizontalState).setValue((Property)BlockStateProperties.HORIZONTAL_FACING, (Comparable)facing.getCounterClockWise())).setValue(CopycatSliceBlock.HALF, (Comparable)(transform.rotation == Rotation.CLOCKWISE_90 == (facing.getAxisDirection() == Direction.AxisDirection.POSITIVE) == (facing.getAxis() == Direction.Axis.X) ? Half.BOTTOM : Half.TOP));
                }
            }
        }
        return state;
    }

    public static Direction getApparentDirection(BlockState state) {
        boolean aligned;
        boolean positive;
        Direction facing = (Direction)state.getValue((Property)BlockStateProperties.HORIZONTAL_FACING);
        Half half = (Half)state.getValue(CopycatSliceBlock.HALF);
        boolean bl = positive = facing.getAxisDirection() == Direction.AxisDirection.POSITIVE;
        if (facing.getAxis() == Direction.Axis.X) {
            boolean aligned2;
            boolean bl2 = aligned2 = positive == (half == Half.TOP);
            return aligned2 ? facing : (positive ? Direction.DOWN : Direction.UP);
        }
        boolean bl3 = aligned = positive == (half == Half.BOTTOM);
        return aligned ? facing : (positive ? Direction.UP : Direction.DOWN);
    }

    public static BlockState setApparentDirection(BlockState state, Direction direction) {
        Direction.Axis axis = ((Direction)state.getValue((Property)BlockStateProperties.HORIZONTAL_FACING)).getAxis();
        if (axis == Direction.Axis.X) {
            return switch (direction) {
                case Direction.UP -> (BlockState)((BlockState)state.setValue((Property)BlockStateProperties.HORIZONTAL_FACING, (Comparable)Direction.WEST)).setValue(CopycatSliceBlock.HALF, (Comparable)Half.TOP);
                case Direction.DOWN -> (BlockState)((BlockState)state.setValue((Property)BlockStateProperties.HORIZONTAL_FACING, (Comparable)Direction.EAST)).setValue(CopycatSliceBlock.HALF, (Comparable)Half.BOTTOM);
                case Direction.EAST -> (BlockState)((BlockState)state.setValue((Property)BlockStateProperties.HORIZONTAL_FACING, (Comparable)Direction.EAST)).setValue(CopycatSliceBlock.HALF, (Comparable)Half.TOP);
                case Direction.WEST -> (BlockState)((BlockState)state.setValue((Property)BlockStateProperties.HORIZONTAL_FACING, (Comparable)Direction.WEST)).setValue(CopycatSliceBlock.HALF, (Comparable)Half.BOTTOM);
                default -> throw new IllegalStateException("Unexpected value: " + String.valueOf(direction));
            };
        }
        return switch (direction) {
            case Direction.UP -> (BlockState)((BlockState)state.setValue((Property)BlockStateProperties.HORIZONTAL_FACING, (Comparable)Direction.SOUTH)).setValue(CopycatSliceBlock.HALF, (Comparable)Half.TOP);
            case Direction.DOWN -> (BlockState)((BlockState)state.setValue((Property)BlockStateProperties.HORIZONTAL_FACING, (Comparable)Direction.NORTH)).setValue(CopycatSliceBlock.HALF, (Comparable)Half.BOTTOM);
            case Direction.NORTH -> (BlockState)((BlockState)state.setValue((Property)BlockStateProperties.HORIZONTAL_FACING, (Comparable)Direction.NORTH)).setValue(CopycatSliceBlock.HALF, (Comparable)Half.TOP);
            case Direction.SOUTH -> (BlockState)((BlockState)state.setValue((Property)BlockStateProperties.HORIZONTAL_FACING, (Comparable)Direction.SOUTH)).setValue(CopycatSliceBlock.HALF, (Comparable)Half.BOTTOM);
            default -> throw new IllegalStateException("Unexpected value: " + String.valueOf(direction));
        };
    }
}

