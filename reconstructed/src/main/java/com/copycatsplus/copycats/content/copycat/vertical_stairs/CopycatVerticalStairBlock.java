/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.zurrtum.create.catnip.data.Iterate
 *  com.zurrtum.create.content.contraptions.StructureTransform
 *  javax.annotation.ParametersAreNonnullByDefault
 *  net.minecraft.client.renderer.block.BlockAndTintGetter
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.core.Direction$Axis
 *  net.minecraft.core.Direction$AxisDirection
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.item.context.BlockPlaceContext
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.LevelReader
 *  net.minecraft.world.level.ScheduledTickAccess
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.Mirror
 *  net.minecraft.world.level.block.Rotation
 *  net.minecraft.world.level.block.StairBlock
 *  net.minecraft.world.level.block.state.BlockBehaviour$Properties
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.StateDefinition$Builder
 *  net.minecraft.world.level.block.state.properties.BlockStateProperties
 *  net.minecraft.world.level.block.state.properties.EnumProperty
 *  net.minecraft.world.level.block.state.properties.Half
 *  net.minecraft.world.level.block.state.properties.Property
 *  net.minecraft.world.phys.shapes.CollisionContext
 *  net.minecraft.world.phys.shapes.VoxelShape
 *  org.jetbrains.annotations.NotNull
 */
package com.copycatsplus.copycats.content.copycat.vertical_stairs;

import com.copycatsplus.copycats.CCBlockStateProperties;
import com.copycatsplus.copycats.CCBlocks;
import com.copycatsplus.copycats.CCShapes;
import com.copycatsplus.copycats.content.copycat.stairs.CopycatStairsBlock;
import com.copycatsplus.copycats.foundation.copycat.CCWaterloggedCopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.ICustomCTBlocking;
import com.copycatsplus.copycats.foundation.copycat.IStateType;
import com.copycatsplus.copycats.utility.BlockUtils;
import com.zurrtum.create.catnip.data.Iterate;
import com.zurrtum.create.content.contraptions.StructureTransform;
import java.util.Optional;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

@ParametersAreNonnullByDefault
public class CopycatVerticalStairBlock
extends CCWaterloggedCopycatBlock
implements ICustomCTBlocking,
IStateType {
    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<CCBlockStateProperties.Side> SIDE = CCBlockStateProperties.SIDE;
    public static final EnumProperty<CCBlockStateProperties.VerticalStairShape> SHAPE = CCBlockStateProperties.VERTICAL_STAIR_SHAPE;

    public CopycatVerticalStairBlock(BlockBehaviour.Properties pProperties) {
        super(pProperties);
        this.registerDefaultState((BlockState)((BlockState)((BlockState)this.defaultBlockState().setValue(FACING, (Comparable)Direction.NORTH)).setValue(SIDE, (Comparable)((Object)CCBlockStateProperties.Side.LEFT))).setValue(SHAPE, (Comparable)((Object)CCBlockStateProperties.VerticalStairShape.STRAIGHT)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition((StateDefinition.Builder<Block, BlockState>)pBuilder.add(new Property[]{FACING, SIDE, SHAPE}));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        CCBlockStateProperties.Side side;
        BlockPos blockPos = context.getClickedPos();
        Direction facing = context.getHorizontalDirection();
        CCBlockStateProperties.Side side2 = side = context.getClickLocation().get(facing.getClockWise().getAxis()) - (double)context.getClickedPos().get(facing.getClockWise().getAxis()) > 0.5 ? CCBlockStateProperties.Side.RIGHT : CCBlockStateProperties.Side.LEFT;
        if (facing.getCounterClockWise().getAxisDirection() == Direction.AxisDirection.POSITIVE) {
            side = side.getOpposite();
        }
        BlockState blockState = (BlockState)((BlockState)this.defaultBlockState().setValue(FACING, (Comparable)facing)).setValue(SIDE, (Comparable)((Object)side));
        return this.withWater((BlockState)blockState.setValue(SHAPE, (Comparable)((Object)CopycatVerticalStairBlock.getStairsShape(blockState, (BlockGetter)context.getLevel(), blockPos))), context);
    }

    @NotNull
    public VoxelShape getShape(BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        Direction facing = (Direction)state.getValue(FACING);
        CCBlockStateProperties.Side side = (CCBlockStateProperties.Side)((Object)state.getValue(SIDE));
        CCBlockStateProperties.VerticalStairShape shape = (CCBlockStateProperties.VerticalStairShape)((Object)state.getValue(SHAPE));
        return CCShapes.VERTICAL_STAIR.get(facing).get((Object)side).get((Object)shape).toShape();
    }

    @Override
    public BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        BlockState newState = super.updateShape(state, level, tickView, pos, direction, neighborPos, neighborState, random);
        if (direction.getAxis() != ((Direction)newState.getValue(FACING)).getAxis()) {
            return (BlockState)newState.setValue(SHAPE, (Comparable)((Object)CopycatVerticalStairBlock.getStairsShape(newState, (BlockGetter)level, pos)));
        }
        return newState;
    }

    public boolean useShapeForLightOcclusion(@NotNull BlockState pState) {
        return true;
    }

    private static CCBlockStateProperties.VerticalStairShape getStairsShape(BlockState state, BlockGetter level, BlockPos pos) {
        Direction facing = (Direction)state.getValue(FACING);
        boolean side = ((CCBlockStateProperties.Side)((Object)state.getValue(SIDE))).isRight();
        Direction verticalDirection = null;
        Boolean horizontalConnection = null;
        boolean down = CopycatVerticalStairBlock.canConnect(state, level, pos, Direction.DOWN);
        boolean downParity = CopycatVerticalStairBlock.getVerticalParity(state, level, pos, Direction.DOWN);
        if (down && side == downParity) {
            verticalDirection = Direction.DOWN;
        } else {
            boolean up = CopycatVerticalStairBlock.canConnect(state, level, pos, Direction.UP);
            boolean upParity = CopycatVerticalStairBlock.getVerticalParity(state, level, pos, Direction.UP);
            if (up && side == upParity) {
                verticalDirection = Direction.UP;
            }
        }
        boolean left = CopycatVerticalStairBlock.canConnect(state, level, pos, facing.getCounterClockWise());
        boolean leftParity = CopycatVerticalStairBlock.getHorizontalParity(state, level, pos, facing.getCounterClockWise());
        if (left) {
            horizontalConnection = false;
            if (verticalDirection == null) {
                verticalDirection = leftParity == side ? Direction.DOWN : Direction.UP;
            }
        } else {
            boolean right = CopycatVerticalStairBlock.canConnect(state, level, pos, facing.getClockWise());
            boolean rightParity = CopycatVerticalStairBlock.getHorizontalParity(state, level, pos, facing.getClockWise());
            if (right) {
                horizontalConnection = true;
                if (verticalDirection == null) {
                    Direction direction = verticalDirection = rightParity == side ? Direction.DOWN : Direction.UP;
                }
            }
        }
        if (horizontalConnection == null) {
            return CCBlockStateProperties.VerticalStairShape.STRAIGHT;
        }
        if (!horizontalConnection.booleanValue()) {
            if (verticalDirection == Direction.DOWN) {
                return side ? CCBlockStateProperties.VerticalStairShape.INNER_TOP : CCBlockStateProperties.VerticalStairShape.OUTER_BOTTOM;
            }
            return side ? CCBlockStateProperties.VerticalStairShape.INNER_BOTTOM : CCBlockStateProperties.VerticalStairShape.OUTER_TOP;
        }
        if (verticalDirection == Direction.DOWN) {
            return side ? CCBlockStateProperties.VerticalStairShape.OUTER_BOTTOM : CCBlockStateProperties.VerticalStairShape.INNER_TOP;
        }
        return side ? CCBlockStateProperties.VerticalStairShape.OUTER_TOP : CCBlockStateProperties.VerticalStairShape.INNER_BOTTOM;
    }

    private static boolean getVerticalParity(BlockState state, BlockGetter level, BlockPos pos, Direction face) {
        BlockState blockState = level.getBlockState(pos.relative(face));
        if (!(blockState.getBlock() instanceof CopycatVerticalStairBlock)) {
            return false;
        }
        return ((CCBlockStateProperties.Side)((Object)blockState.getValue(SIDE))).isRight();
    }

    private static boolean getHorizontalParity(BlockState state, BlockGetter level, BlockPos pos, Direction face) {
        BlockState blockState = level.getBlockState(pos.relative(face));
        if (!CopycatVerticalStairBlock.isStairs(blockState)) {
            return false;
        }
        if (blockState.getBlock() instanceof CopycatVerticalStairBlock) {
            return ((Direction)state.getValue(FACING)).getCounterClockWise() == face == ((CCBlockStateProperties.Side)((Object)state.getValue(SIDE))).isRight() == ((CCBlockStateProperties.Side)((Object)blockState.getValue(SIDE))).isRight();
        }
        return blockState.getValue((Property)StairBlock.HALF) == Half.TOP;
    }

    private static boolean canConnect(BlockState state, BlockGetter level, BlockPos pos, Direction face) {
        Direction otherFacing;
        BlockState blockState = level.getBlockState(pos.relative(face));
        if (!CopycatVerticalStairBlock.isStairs(blockState)) {
            return false;
        }
        if (face.getAxis().isVertical() && !(blockState.getBlock() instanceof CopycatVerticalStairBlock)) {
            return false;
        }
        Direction selfFacing = (Direction)state.getValue(FACING);
        if (selfFacing == (otherFacing = (Direction)blockState.getValue(FACING)).getOpposite()) {
            return false;
        }
        if (selfFacing == otherFacing && face.getAxis() != selfFacing.getAxis()) {
            return true;
        }
        return selfFacing == otherFacing;
    }

    public static boolean isStairs(BlockState state) {
        return state.getBlock() instanceof StairBlock || state.getBlock() instanceof CopycatVerticalStairBlock;
    }

    @Override
    public BlockState transform(BlockState state, StructureTransform transform) {
        if (transform.mirror != null && transform.mirror != Mirror.NONE) {
            Direction.Axis mirrorAxis = null;
            for (Direction.Axis axis : Iterate.axes) {
                if (!transform.mirror.rotation().inverts(axis)) continue;
                mirrorAxis = axis;
                break;
            }
            if (mirrorAxis != null && !mirrorAxis.isVertical()) {
                Direction facing = (Direction)state.getValue(FACING);
                state = facing.getAxis() != mirrorAxis ? (BlockState)state.cycle(SIDE) : (BlockState)((BlockState)state.setValue(FACING, (Comparable)facing.getOpposite())).cycle(SIDE);
            }
        }
        if (transform.rotationAxis != null) {
            if (transform.rotationAxis == Direction.Axis.Y) {
                state = (BlockState)state.setValue(FACING, (Comparable)transform.rotateFacing((Direction)state.getValue(FACING)));
            } else {
                Direction facing = (Direction)state.getValue(FACING);
                CCBlockStateProperties.Side side = (CCBlockStateProperties.Side)((Object)state.getValue(SIDE));
                if (facing.getAxis() != transform.rotationAxis) {
                    state = side == CCBlockStateProperties.Side.LEFT ? (BlockState)((BlockState)state.setValue(FACING, (Comparable)facing.getCounterClockWise())).cycle(SIDE) : (BlockState)((BlockState)state.setValue(FACING, (Comparable)facing.getClockWise())).cycle(SIDE);
                }
                facing = (Direction)state.getValue(FACING);
                side = (CCBlockStateProperties.Side)((Object)state.getValue(SIDE));
                if (transform.rotation == Rotation.CLOCKWISE_180) {
                    state = (BlockState)state.cycle(SIDE);
                } else if (transform.rotation != Rotation.NONE) {
                    Direction offset = transform.rotateFacing(side.isRight() ? facing.getClockWise() : facing.getCounterClockWise());
                    state = (BlockState)BlockUtils.tryCopyProperties(state, CCBlocks.COPYCAT_STAIRS.getDefaultState()).setValue((Property)StairBlock.HALF, (Comparable)(offset == Direction.DOWN ? Half.BOTTOM : Half.TOP));
                }
            }
        }
        return state;
    }

    @Override
    public Optional<Boolean> isCTBlocked(BlockAndTintGetter reader, BlockState state, BlockPos pos, BlockPos connectingPos, BlockPos blockingPos, Direction face) {
        if (!CopycatVerticalStairBlock.getFaceShape(state, face).canConnect()) {
            return Optional.of(false);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Boolean> blockCTTowards(BlockAndTintGetter reader, BlockState state, BlockPos pos, BlockPos ctPos, BlockPos connectingPos, Direction face) {
        CopycatStairsBlock.FaceShape sideShape = CopycatVerticalStairBlock.getFaceShape(state, face);
        Direction facing = (Direction)state.getValue(FACING);
        if (!sideShape.canConnect()) {
            return Optional.of(false);
        }
        BlockState connectingState = reader.getBlockState(connectingPos);
        if (connectingState.is((Object)this)) {
            if (sideShape.equals(CopycatVerticalStairBlock.getFaceShape(connectingState, face.getOpposite()))) {
                return Optional.of(true);
            }
        } else if (sideShape.isFull()) {
            BlockState ctState = reader.getBlockState(ctPos);
            if (ctPos.get(facing.getAxis()) == pos.get(facing.getAxis()) || !CopycatVerticalStairBlock.isStairs(ctState) || ctState.getOptionalValue(SIDE).orElse(null) != state.getOptionalValue(SIDE).orElse(null) || ctState.getOptionalValue((Property)StairBlock.HALF).orElse(null) != state.getOptionalValue((Property)StairBlock.HALF).orElse(null)) {
                return Optional.of(true);
            }
        }
        return Optional.empty();
    }

    private static Direction.AxisDirection directionOf(int value) {
        return value >= 0 ? Direction.AxisDirection.POSITIVE : Direction.AxisDirection.NEGATIVE;
    }

    public boolean supportsExternalFaceHiding(BlockState state) {
        return true;
    }

    @Override
    public boolean hidesNeighborFace(BlockGetter level, BlockPos pos, BlockState state, BlockState neighborState, Direction dir) {
        return ICopycatBlock.hidesNeighborFace(level, pos, state, neighborState, dir);
    }

    public static CopycatStairsBlock.FaceShape getFaceShape(BlockState state, Direction face) {
        if (!(state.getBlock() instanceof CopycatVerticalStairBlock)) {
            return CopycatStairsBlock.getFaceShape(state, face);
        }
        boolean right = ((CCBlockStateProperties.Side)((Object)state.getValue(SIDE))).isRight();
        Direction facing = (Direction)state.getValue(FACING);
        CCBlockStateProperties.VerticalStairShape shape = (CCBlockStateProperties.VerticalStairShape)((Object)state.getValue(SHAPE));
        if (face == facing) {
            return new CopycatStairsBlock.FaceShape().fillAll();
        }
        CopycatStairsBlock.FaceShape faceShape = new CopycatStairsBlock.FaceShape();
        switch (shape) {
            case STRAIGHT: {
                if (face == facing.getOpposite()) {
                    return right ? faceShape.fillRight(facing) : faceShape.fillLeft(facing);
                }
                if (face == facing.getCounterClockWise()) {
                    return !right ? faceShape.fillAll() : faceShape.fillLeft(facing);
                }
                if (face == facing.getClockWise()) {
                    return right ? faceShape.fillAll() : faceShape.fillRight(facing);
                }
                faceShape.fillTop();
                if (right) {
                    faceShape.fillPositive();
                } else {
                    faceShape.fillNegative();
                }
                return faceShape.rotate(facing.toYRot());
            }
            case INNER_BOTTOM: {
                if (face == facing.getOpposite()) {
                    faceShape.fillBottom();
                    if (right) {
                        faceShape.fillRight(facing);
                    } else {
                        faceShape.fillLeft(facing);
                    }
                    return faceShape;
                }
                if (face == facing.getCounterClockWise()) {
                    return !right ? faceShape.fillAll() : faceShape.fillLeft(facing).fillBottom();
                }
                if (face == facing.getClockWise()) {
                    return right ? faceShape.fillAll() : faceShape.fillRight(facing).fillBottom();
                }
                if (face == Direction.DOWN) {
                    return faceShape.fillAll();
                }
                faceShape.fillTop();
                if (right) {
                    faceShape.fillPositive();
                } else {
                    faceShape.fillNegative();
                }
                return faceShape.rotate(facing.toYRot());
            }
            case INNER_TOP: {
                if (face == facing.getOpposite()) {
                    faceShape.fillTop();
                    if (right) {
                        faceShape.fillRight(facing);
                    } else {
                        faceShape.fillLeft(facing);
                    }
                    return faceShape;
                }
                if (face == facing.getCounterClockWise()) {
                    return !right ? faceShape.fillAll() : faceShape.fillLeft(facing).fillTop();
                }
                if (face == facing.getClockWise()) {
                    return right ? faceShape.fillAll() : faceShape.fillRight(facing).fillTop();
                }
                if (face == Direction.UP) {
                    return faceShape.fillAll();
                }
                faceShape.fillTop();
                if (right) {
                    faceShape.fillPositive();
                } else {
                    faceShape.fillNegative();
                }
                return faceShape.rotate(facing.toYRot());
            }
            case OUTER_BOTTOM: {
                if (face == facing.getOpposite()) {
                    return faceShape.fillBottom(right ? facing.getCounterClockWise().getAxisDirection() : facing.getClockWise().getAxisDirection());
                }
                if (face == facing.getCounterClockWise()) {
                    return !right ? faceShape.fillLeft(facing).fillBottom() : faceShape.fillLeft(facing);
                }
                if (face == facing.getClockWise()) {
                    return right ? faceShape.fillRight(facing).fillBottom() : faceShape.fillRight(facing);
                }
                if (face == Direction.UP) {
                    return faceShape.fillTop().rotate(facing.toYRot());
                }
                faceShape.fillTop();
                if (right) {
                    faceShape.fillPositive();
                } else {
                    faceShape.fillNegative();
                }
                return faceShape.rotate(facing.toYRot());
            }
            case OUTER_TOP: {
                if (face == facing.getOpposite()) {
                    return faceShape.fillTop(right ? facing.getCounterClockWise().getAxisDirection() : facing.getClockWise().getAxisDirection());
                }
                if (face == facing.getCounterClockWise()) {
                    return !right ? faceShape.fillLeft(facing).fillTop() : faceShape.fillLeft(facing);
                }
                if (face == facing.getClockWise()) {
                    return right ? faceShape.fillRight(facing).fillTop() : faceShape.fillRight(facing);
                }
                if (face == Direction.DOWN) {
                    return faceShape.fillTop().rotate(facing.toYRot());
                }
                faceShape.fillTop();
                if (right) {
                    faceShape.fillPositive();
                } else {
                    faceShape.fillNegative();
                }
                return faceShape.rotate(facing.toYRot());
            }
        }
        return faceShape;
    }
}

