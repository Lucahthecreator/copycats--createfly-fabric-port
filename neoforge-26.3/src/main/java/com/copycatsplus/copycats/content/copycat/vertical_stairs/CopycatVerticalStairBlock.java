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
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

@ParametersAreNonnullByDefault
public class CopycatVerticalStairBlock extends CCWaterloggedCopycatBlock implements ICustomCTBlocking, IStateType {
    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<CCBlockStateProperties.Side> SIDE = CCBlockStateProperties.SIDE;
    public static final EnumProperty<CCBlockStateProperties.VerticalStairShape> SHAPE = CCBlockStateProperties.VERTICAL_STAIR_SHAPE;

    public CopycatVerticalStairBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(
            this.defaultBlockState()
                .setValue(FACING, Direction.NORTH)
                .setValue(SIDE, CCBlockStateProperties.Side.LEFT)
                .setValue(SHAPE, CCBlockStateProperties.VerticalStairShape.STRAIGHT)
        );
    }

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder.add(FACING, SIDE, SHAPE));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos blockPos = context.getClickedPos();
        Direction facing = context.getHorizontalDirection();
        CCBlockStateProperties.Side side = context.getClickLocation().get(facing.getClockWise().getAxis())
                    - context.getClickedPos().get(facing.getClockWise().getAxis())
                > 0.5
            ? CCBlockStateProperties.Side.RIGHT
            : CCBlockStateProperties.Side.LEFT;
        if (facing.getCounterClockWise().getAxisDirection() == AxisDirection.POSITIVE) {
            side = side.getOpposite();
        }

        BlockState blockState = this.defaultBlockState().setValue(FACING, facing).setValue(SIDE, side);
        return this.withWater(blockState.setValue(SHAPE, getStairsShape(blockState, context.getLevel(), blockPos)), context);
    }

    @NotNull
    @Override
    public VoxelShape getShape(BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        Direction facing = state.getValue(FACING);
        CCBlockStateProperties.Side side = state.getValue(SIDE);
        CCBlockStateProperties.VerticalStairShape shape = state.getValue(SHAPE);
        return CCShapes.VERTICAL_STAIR.get(facing).get(side).get(shape).toShape();
    }

    @Override
    public BlockState updateShape(
        BlockState state,
        LevelReader level,
        ScheduledTickAccess tickView,
        BlockPos pos,
        Direction direction,
        BlockPos neighborPos,
        BlockState neighborState,
        RandomSource random
    ) {
        BlockState newState = super.updateShape(state, level, tickView, pos, direction, neighborPos, neighborState, random);
        return direction.getAxis() != newState.getValue(FACING).getAxis() ? newState.setValue(SHAPE, getStairsShape(newState, level, pos)) : newState;
    }

    @Override
    public boolean useShapeForLightOcclusion(@NotNull BlockState pState) {
        return true;
    }

    private static CCBlockStateProperties.VerticalStairShape getStairsShape(BlockState state, BlockGetter level, BlockPos pos) {
        Direction facing = state.getValue(FACING);
        boolean side = state.getValue(SIDE).isRight();
        Direction verticalDirection = null;
        Boolean horizontalConnection = null;
        boolean down = canConnect(state, level, pos, Direction.DOWN);
        boolean downParity = getVerticalParity(state, level, pos, Direction.DOWN);
        if (down && side == downParity) {
            verticalDirection = Direction.DOWN;
        } else {
            boolean up = canConnect(state, level, pos, Direction.UP);
            boolean upParity = getVerticalParity(state, level, pos, Direction.UP);
            if (up && side == upParity) {
                verticalDirection = Direction.UP;
            }
        }

        boolean left = canConnect(state, level, pos, facing.getCounterClockWise());
        boolean leftParity = getHorizontalParity(state, level, pos, facing.getCounterClockWise());
        if (left) {
            horizontalConnection = false;
            if (verticalDirection == null) {
                verticalDirection = leftParity == side ? Direction.DOWN : Direction.UP;
            }
        } else {
            boolean right = canConnect(state, level, pos, facing.getClockWise());
            boolean rightParity = getHorizontalParity(state, level, pos, facing.getClockWise());
            if (right) {
                horizontalConnection = true;
                if (verticalDirection == null) {
                    verticalDirection = rightParity == side ? Direction.DOWN : Direction.UP;
                }
            }
        }

        if (horizontalConnection == null) {
            return CCBlockStateProperties.VerticalStairShape.STRAIGHT;
        } else if (!horizontalConnection) {
            if (verticalDirection == Direction.DOWN) {
                return side ? CCBlockStateProperties.VerticalStairShape.INNER_TOP : CCBlockStateProperties.VerticalStairShape.OUTER_BOTTOM;
            } else {
                return side ? CCBlockStateProperties.VerticalStairShape.INNER_BOTTOM : CCBlockStateProperties.VerticalStairShape.OUTER_TOP;
            }
        } else if (verticalDirection == Direction.DOWN) {
            return side ? CCBlockStateProperties.VerticalStairShape.OUTER_BOTTOM : CCBlockStateProperties.VerticalStairShape.INNER_TOP;
        } else {
            return side ? CCBlockStateProperties.VerticalStairShape.OUTER_TOP : CCBlockStateProperties.VerticalStairShape.INNER_BOTTOM;
        }
    }

    private static boolean getVerticalParity(BlockState state, BlockGetter level, BlockPos pos, Direction face) {
        BlockState blockState = level.getBlockState(pos.relative(face));
        return !(blockState.getBlock() instanceof CopycatVerticalStairBlock) ? false : blockState.getValue(SIDE).isRight();
    }

    private static boolean getHorizontalParity(BlockState state, BlockGetter level, BlockPos pos, Direction face) {
        BlockState blockState = level.getBlockState(pos.relative(face));
        if (!isStairs(blockState)) {
            return false;
        } else {
            return blockState.getBlock() instanceof CopycatVerticalStairBlock
                ? state.getValue(FACING).getCounterClockWise() == face == state.getValue(SIDE).isRight() == blockState.getValue(SIDE).isRight()
                : blockState.getValue(StairBlock.HALF) == Half.TOP;
        }
    }

    private static boolean canConnect(BlockState state, BlockGetter level, BlockPos pos, Direction face) {
        BlockState blockState = level.getBlockState(pos.relative(face));
        if (!isStairs(blockState)) {
            return false;
        } else if (face.getAxis().isVertical() && !(blockState.getBlock() instanceof CopycatVerticalStairBlock)) {
            return false;
        } else {
            Direction selfFacing = state.getValue(FACING);
            Direction otherFacing = blockState.getValue(FACING);
            if (selfFacing == otherFacing.getOpposite()) {
                return false;
            } else {
                return selfFacing == otherFacing && face.getAxis() != selfFacing.getAxis() ? true : selfFacing == otherFacing;
            }
        }
    }

    public static boolean isStairs(BlockState state) {
        return state.getBlock() instanceof StairBlock || state.getBlock() instanceof CopycatVerticalStairBlock;
    }

    @Override
    public BlockState transform(BlockState state, StructureTransform transform) {
        if (transform.mirror != null && transform.mirror != Mirror.NONE) {
            Axis mirrorAxis = null;

            for (Axis axis : Iterate.axes) {
                if (transform.mirror.rotation().inverts(axis)) {
                    mirrorAxis = axis;
                    break;
                }
            }

            if (mirrorAxis != null && !mirrorAxis.isVertical()) {
                Direction facing = state.getValue(FACING);
                if (facing.getAxis() != mirrorAxis) {
                    state = state.cycle(SIDE);
                } else {
                    state = state.setValue(FACING, facing.getOpposite()).cycle(SIDE);
                }
            }
        }

        if (transform.rotationAxis != null) {
            if (transform.rotationAxis == Axis.Y) {
                state = state.setValue(FACING, transform.rotateFacing(state.getValue(FACING)));
            } else {
                Direction facing = state.getValue(FACING);
                CCBlockStateProperties.Side side = state.getValue(SIDE);
                if (facing.getAxis() != transform.rotationAxis) {
                    if (side == CCBlockStateProperties.Side.LEFT) {
                        state = state.setValue(FACING, facing.getCounterClockWise()).cycle(SIDE);
                    } else {
                        state = state.setValue(FACING, facing.getClockWise()).cycle(SIDE);
                    }
                }

                facing = state.getValue(FACING);
                side = state.getValue(SIDE);
                if (transform.rotation == Rotation.CLOCKWISE_180) {
                    state = state.cycle(SIDE);
                } else if (transform.rotation != Rotation.NONE) {
                    Direction offset = transform.rotateFacing(side.isRight() ? facing.getClockWise() : facing.getCounterClockWise());
                    state = BlockUtils.tryCopyProperties(state, CCBlocks.COPYCAT_STAIRS.getDefaultState())
                        .setValue(StairBlock.HALF, offset == Direction.DOWN ? Half.BOTTOM : Half.TOP);
                }
            }
        }

        return state;
    }

    @Override
    public Optional<Boolean> isCTBlocked(
        BlockAndTintGetter reader, BlockState state, BlockPos pos, BlockPos connectingPos, BlockPos blockingPos, Direction face
    ) {
        return !getFaceShape(state, face).canConnect() ? Optional.of(false) : Optional.empty();
    }

    @Override
    public Optional<Boolean> blockCTTowards(BlockAndTintGetter reader, BlockState state, BlockPos pos, BlockPos ctPos, BlockPos connectingPos, Direction face) {
        CopycatStairsBlock.FaceShape sideShape = getFaceShape(state, face);
        Direction facing = state.getValue(FACING);
        if (!sideShape.canConnect()) {
            return Optional.of(false);
        } else {
            BlockState connectingState = reader.getBlockState(connectingPos);
            if (connectingState.is(this)) {
                if (sideShape.equals(getFaceShape(connectingState, face.getOpposite()))) {
                    return Optional.of(true);
                }
            } else if (sideShape.isFull()) {
                BlockState ctState = reader.getBlockState(ctPos);
                if (ctPos.get(facing.getAxis()) == pos.get(facing.getAxis())
                    || !isStairs(ctState)
                    || ctState.getOptionalValue(SIDE).orElse(null) != state.getOptionalValue(SIDE).orElse(null)
                    || ctState.getOptionalValue(StairBlock.HALF).orElse(null) != state.getOptionalValue(StairBlock.HALF).orElse(null)) {
                    return Optional.of(true);
                }
            }

            return Optional.empty();
        }
    }

    private static AxisDirection directionOf(int value) {
        return value >= 0 ? AxisDirection.POSITIVE : AxisDirection.NEGATIVE;
    }

    public boolean supportsExternalFaceHiding(BlockState state) {
        return true;
    }

    public boolean hidesNeighborFace(BlockGetter level, BlockPos pos, BlockState state, BlockState neighborState, Direction dir) {
        return ICopycatBlock.hidesNeighborFace(level, pos, state, neighborState, dir);
    }

    public static CopycatStairsBlock.FaceShape getFaceShape(BlockState state, Direction face) {
        if (!(state.getBlock() instanceof CopycatVerticalStairBlock)) {
            return CopycatStairsBlock.getFaceShape(state, face);
        } else {
            boolean right = state.getValue(SIDE).isRight();
            Direction facing = state.getValue(FACING);
            CCBlockStateProperties.VerticalStairShape shape = state.getValue(SHAPE);
            if (face == facing) {
                return new CopycatStairsBlock.FaceShape().fillAll();
            } else {
                CopycatStairsBlock.FaceShape faceShape = new CopycatStairsBlock.FaceShape();
                switch (shape) {
                    case STRAIGHT:
                        if (face == facing.getOpposite()) {
                            return right ? faceShape.fillRight(facing) : faceShape.fillLeft(facing);
                        } else if (face == facing.getCounterClockWise()) {
                            return !right ? faceShape.fillAll() : faceShape.fillLeft(facing);
                        } else {
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
                    case INNER_BOTTOM:
                        if (face == facing.getOpposite()) {
                            faceShape.fillBottom();
                            if (right) {
                                faceShape.fillRight(facing);
                            } else {
                                faceShape.fillLeft(facing);
                            }

                            return faceShape;
                        } else if (face == facing.getCounterClockWise()) {
                            return !right ? faceShape.fillAll() : faceShape.fillLeft(facing).fillBottom();
                        } else if (face == facing.getClockWise()) {
                            return right ? faceShape.fillAll() : faceShape.fillRight(facing).fillBottom();
                        } else {
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
                    case INNER_TOP:
                        if (face == facing.getOpposite()) {
                            faceShape.fillTop();
                            if (right) {
                                faceShape.fillRight(facing);
                            } else {
                                faceShape.fillLeft(facing);
                            }

                            return faceShape;
                        } else if (face == facing.getCounterClockWise()) {
                            return !right ? faceShape.fillAll() : faceShape.fillLeft(facing).fillTop();
                        } else if (face == facing.getClockWise()) {
                            return right ? faceShape.fillAll() : faceShape.fillRight(facing).fillTop();
                        } else {
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
                    case OUTER_BOTTOM:
                        if (face == facing.getOpposite()) {
                            return faceShape.fillBottom(right ? facing.getCounterClockWise().getAxisDirection() : facing.getClockWise().getAxisDirection());
                        } else if (face == facing.getCounterClockWise()) {
                            return !right ? faceShape.fillLeft(facing).fillBottom() : faceShape.fillLeft(facing);
                        } else if (face == facing.getClockWise()) {
                            return right ? faceShape.fillRight(facing).fillBottom() : faceShape.fillRight(facing);
                        } else {
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
                    case OUTER_TOP:
                        if (face == facing.getOpposite()) {
                            return faceShape.fillTop(right ? facing.getCounterClockWise().getAxisDirection() : facing.getClockWise().getAxisDirection());
                        } else if (face == facing.getCounterClockWise()) {
                            return !right ? faceShape.fillLeft(facing).fillTop() : faceShape.fillLeft(facing);
                        } else if (face == facing.getClockWise()) {
                            return right ? faceShape.fillRight(facing).fillTop() : faceShape.fillRight(facing);
                        } else {
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
                    default:
                        return faceShape;
                }
            }
        }
    }
}
