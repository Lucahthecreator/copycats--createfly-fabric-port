package com.copycatsplus.copycats.content.copycat.stairs;

import com.copycatsplus.copycats.CCBlockEntityTypes;
import com.copycatsplus.copycats.CCBlockStateProperties;
import com.copycatsplus.copycats.CCBlocks;
import com.copycatsplus.copycats.content.copycat.vertical_stairs.CopycatVerticalStairBlock;
import com.copycatsplus.copycats.foundation.copycat.CCCopycatBlockEntity;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.ICustomCTBlocking;
import com.copycatsplus.copycats.foundation.copycat.IStateType;
import com.copycatsplus.copycats.utility.BlockUtils;
import com.copycatsplus.copycats.utility.InteractionUtils;
import com.mojang.math.OctahedralGroup;
import com.zurrtum.create.content.contraptions.StructureTransform;
import com.zurrtum.create.foundation.block.IBE;
import java.util.Optional;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.StairsShape;
import net.minecraft.world.phys.BlockHitResult;

@ParametersAreNonnullByDefault
public class CopycatStairsBlock extends StairBlock implements ICopycatBlock, ICustomCTBlocking, IBE<CCCopycatBlockEntity>, IStateType {
    public CopycatStairsBlock(Properties properties) {
        super(Blocks.OAK_PLANKS.defaultBlockState(), properties);
    }

    @Nullable
    @Override
    public <S extends BlockEntity> BlockEntityTicker<S> getTicker(Level level, BlockState state, BlockEntityType<S> type) {
        return null;
    }

    @Override
    protected InteractionResult useItemOn(
        ItemStack heldStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit
    ) {
        return InteractionUtils.sequential(
            () -> ICopycatBlock.super.use(state, level, pos, player, hand, hit), () -> super.useItemOn(heldStack, state, level, pos, player, hand, hit)
        );
    }

    @Override
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, @Nullable LivingEntity pPlacer, ItemStack pStack) {
        ICopycatBlock.super.setPlacedBy(pLevel, pPos, pState, pPlacer, pStack);
        super.setPlacedBy(pLevel, pPos, pState, pPlacer, pStack);
    }

    @Override
    public BlockState playerWillDestroy(Level pLevel, BlockPos pPos, BlockState pState, Player pPlayer) {
        ICopycatBlock.super.playerWillDestroy(pLevel, pPos, pState, pPlayer);
        return super.playerWillDestroy(pLevel, pPos, pState, pPlayer);
    }

    @Override
    public Class<CCCopycatBlockEntity> getBlockEntityClass() {
        return CCCopycatBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends CCCopycatBlockEntity> getBlockEntityType() {
        return CCBlockEntityTypes.COPYCAT.get();
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return ICopycatBlock.super.rotateCopycat(state, rotation);
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return ICopycatBlock.super.mirrorCopycat(state, mirror);
    }

    @Override
    public BlockState transform(BlockState state, StructureTransform transform) {
        if (transform.mirror != null && transform.mirror != Mirror.NONE) {
            if (transform.mirror.rotation() == OctahedralGroup.INVERT_Y) {
                state = state.cycle(HALF);
            } else {
                state = state.setValue(FACING, transform.mirror.mirror(state.getValue(FACING)));
            }
        }

        if (transform.rotationAxis != null) {
            if (transform.rotationAxis == Axis.Y) {
                state = state.setValue(FACING, transform.rotateFacing(state.getValue(FACING)));
            } else {
                Direction facing = state.getValue(FACING);
                Half half = state.getValue(HALF);
                if (transform.rotationAxis == facing.getAxis()) {
                    if (transform.rotation == Rotation.CLOCKWISE_180) {
                        state = state.cycle(HALF);
                    } else if (transform.rotation != Rotation.NONE) {
                        Direction offset = transform.rotateFacing(half == Half.TOP ? Direction.UP : Direction.DOWN);
                        state = BlockUtils.tryCopyProperties(state, CCBlocks.COPYCAT_VERTICAL_STAIRS.getDefaultState())
                            .setValue(FACING, offset)
                            .setValue(
                                CCBlockStateProperties.SIDE,
                                offset == facing.getClockWise() ? CCBlockStateProperties.Side.LEFT : CCBlockStateProperties.Side.RIGHT
                            );
                    }
                } else {
                    state = BlockUtils.setApparentDirection(state, transform.rotateFacing(BlockUtils.getApparentDirection(state)));
                }
            }
        }

        return state;
    }

    @Override
    public Optional<Boolean> isCTBlocked(
        BlockAndTintGetter reader, BlockState state, BlockPos pos, BlockPos connectingPos, BlockPos blockingPos, Direction face
    ) {
        return connectingPos.getY() >= pos.getY() ? Optional.empty() : Optional.of(false);
    }

    @Override
    public Optional<Boolean> blockCTTowards(BlockAndTintGetter reader, BlockState state, BlockPos pos, BlockPos ctPos, BlockPos connectingPos, Direction face) {
        return Optional.of(false);
    }

    public boolean supportsExternalFaceHiding(BlockState state) {
        return true;
    }

    public boolean hidesNeighborFace(BlockGetter level, BlockPos pos, BlockState state, BlockState neighborState, Direction dir) {
        return ICopycatBlock.hidesNeighborFace(level, pos, state, neighborState, dir);
    }

    private static AxisDirection directionOf(int value) {
        return value >= 0 ? AxisDirection.POSITIVE : AxisDirection.NEGATIVE;
    }

    public static CopycatStairsBlock.FaceShape getFaceShape(BlockState state, Direction face) {
        if (state.getBlock() instanceof CopycatVerticalStairBlock) {
            return CopycatVerticalStairBlock.getFaceShape(state, face);
        } else {
            boolean top = state.getValue(StairBlock.HALF) == Half.TOP;
            Direction facing = state.getValue(StairBlock.FACING);
            StairsShape shape = state.getValue(StairBlock.SHAPE);
            if (!top && face == Direction.DOWN) {
                return new CopycatStairsBlock.FaceShape().fillAll();
            } else if (top && face == Direction.UP) {
                return new CopycatStairsBlock.FaceShape().fillAll();
            } else {
                CopycatStairsBlock.FaceShape faceShape = new CopycatStairsBlock.FaceShape();
                switch (shape) {
                    case STRAIGHT:
                        if ((top || face != Direction.UP) && (!top || face != Direction.DOWN)) {
                            faceShape.fillRow(top);
                            if (face == facing) {
                                return faceShape.fillRow(!top);
                            }

                            if (face == facing.getOpposite()) {
                                return faceShape;
                            }

                            return faceShape.fillRow(!top, facing.getAxisDirection());
                        }

                        return faceShape.fillTop().rotate(facing.toYRot());
                    case INNER_LEFT:
                        if (!top && face == Direction.UP || top && face == Direction.DOWN) {
                            return faceShape.fillTop().fillBottom(AxisDirection.POSITIVE).rotate(facing.toYRot());
                        }

                        faceShape.fillRow(top);
                        if (face == facing) {
                            return faceShape.fillRow(!top);
                        }

                        if (face == facing.getOpposite()) {
                            return faceShape.fillRow(!top, facing.getCounterClockWise().getAxisDirection());
                        }

                        if (face == facing.getCounterClockWise()) {
                            return faceShape.fillRow(!top);
                        }

                        if (face == facing.getClockWise()) {
                            return faceShape.fillRow(!top, facing.getAxisDirection());
                        }
                        break;
                    case INNER_RIGHT:
                        if (!top && face == Direction.UP || top && face == Direction.DOWN) {
                            return faceShape.fillTop().fillBottom(AxisDirection.NEGATIVE).rotate(facing.toYRot());
                        }

                        faceShape.fillRow(top);
                        if (face == facing) {
                            return faceShape.fillRow(!top);
                        }

                        if (face == facing.getOpposite()) {
                            return faceShape.fillRow(!top, facing.getClockWise().getAxisDirection());
                        }

                        if (face == facing.getClockWise()) {
                            return faceShape.fillRow(!top);
                        }

                        if (face == facing.getCounterClockWise()) {
                            return faceShape.fillRow(!top, facing.getAxisDirection());
                        }
                        break;
                    case OUTER_LEFT:
                        if (!top && face == Direction.UP || top && face == Direction.DOWN) {
                            return faceShape.fillTop(AxisDirection.POSITIVE).rotate(facing.toYRot());
                        }

                        faceShape.fillRow(top);
                        if (face == facing) {
                            return faceShape.fillRow(!top, facing.getCounterClockWise().getAxisDirection());
                        }

                        if (face == facing.getOpposite()) {
                            return faceShape;
                        }

                        if (face == facing.getCounterClockWise()) {
                            return faceShape.fillRow(!top, facing.getAxisDirection());
                        }

                        if (face == facing.getClockWise()) {
                            return faceShape;
                        }
                        break;
                    case OUTER_RIGHT:
                        if (!top && face == Direction.UP || top && face == Direction.DOWN) {
                            return faceShape.fillTop(AxisDirection.NEGATIVE).rotate(facing.toYRot());
                        }

                        faceShape.fillRow(top);
                        if (face == facing) {
                            return faceShape.fillRow(!top, facing.getClockWise().getAxisDirection());
                        }

                        if (face == facing.getOpposite()) {
                            return faceShape;
                        }

                        if (face == facing.getClockWise()) {
                            return faceShape.fillRow(!top, facing.getAxisDirection());
                        }

                        if (face == facing.getCounterClockWise()) {
                            return faceShape;
                        }
                }

                return faceShape;
            }
        }
    }

    public static class FaceShape {
        public boolean topNegative;
        public boolean topPositive;
        public boolean bottomNegative;
        public boolean bottomPositive;

        public CopycatStairsBlock.FaceShape fillTop() {
            this.topNegative = this.topPositive = true;
            return this;
        }

        public CopycatStairsBlock.FaceShape fillColumn(AxisDirection direction) {
            switch (direction) {
                case POSITIVE:
                    this.topPositive = this.bottomPositive = true;
                    break;
                case NEGATIVE:
                    this.topNegative = this.bottomNegative = true;
            }

            return this;
        }

        public CopycatStairsBlock.FaceShape fillNegative() {
            this.topNegative = this.bottomNegative = true;
            return this;
        }

        public CopycatStairsBlock.FaceShape fillPositive() {
            this.topPositive = this.bottomPositive = true;
            return this;
        }

        public CopycatStairsBlock.FaceShape fillLeft(Direction relativeTo) {
            return this.fillColumn(relativeTo.getClockWise().getAxisDirection());
        }

        public CopycatStairsBlock.FaceShape fillRight(Direction relativeTo) {
            return this.fillColumn(relativeTo.getCounterClockWise().getAxisDirection());
        }

        public CopycatStairsBlock.FaceShape fillTop(AxisDirection direction) {
            switch (direction) {
                case POSITIVE:
                    this.topPositive = true;
                    break;
                case NEGATIVE:
                    this.topNegative = true;
            }

            return this;
        }

        public CopycatStairsBlock.FaceShape fillBottom() {
            this.bottomNegative = this.bottomPositive = true;
            return this;
        }

        public CopycatStairsBlock.FaceShape fillBottom(AxisDirection direction) {
            switch (direction) {
                case POSITIVE:
                    this.bottomPositive = true;
                    break;
                case NEGATIVE:
                    this.bottomNegative = true;
            }

            return this;
        }

        public CopycatStairsBlock.FaceShape fillRow(boolean top) {
            return top ? this.fillTop() : this.fillBottom();
        }

        public CopycatStairsBlock.FaceShape fillRow(boolean top, AxisDirection direction) {
            return top ? this.fillTop(direction) : this.fillBottom(direction);
        }

        public CopycatStairsBlock.FaceShape fillAll() {
            return this.fillTop().fillBottom();
        }

        public CopycatStairsBlock.FaceShape rotate(float angle) {
            return this.rotate((int)angle);
        }

        public CopycatStairsBlock.FaceShape rotate(int angle) {
            angle %= 360;
            if (angle < 0) {
                angle += 360;
            }
            return switch (angle) {
                case 90 -> this.set(this.topNegative, this.bottomNegative, this.topPositive, this.bottomPositive);
                case 180 -> this.set(this.topPositive, this.topNegative, this.bottomPositive, this.bottomNegative);
                case 270 -> this.set(this.bottomPositive, this.topPositive, this.bottomNegative, this.topNegative);
                default -> this;
            };
        }

        public CopycatStairsBlock.FaceShape set(boolean bottomNegative, boolean bottomPositive, boolean topNegative, boolean topPositive) {
            this.bottomNegative = bottomNegative;
            this.bottomPositive = bottomPositive;
            this.topNegative = topNegative;
            this.topPositive = topPositive;
            return this;
        }

        public int countBlocks() {
            int count = 0;
            if (this.bottomNegative) {
                count++;
            }

            if (this.bottomPositive) {
                count++;
            }

            if (this.topNegative) {
                count++;
            }

            if (this.topPositive) {
                count++;
            }

            return count;
        }

        public boolean canConnect() {
            return this.countBlocks() >= 3;
        }

        public boolean isFull() {
            return this.countBlocks() == 4;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            } else {
                return !(obj instanceof CopycatStairsBlock.FaceShape shape)
                    ? false
                    : shape.bottomNegative == this.bottomNegative
                        && shape.bottomPositive == this.bottomPositive
                        && shape.topNegative == this.topNegative
                        && shape.topPositive == this.topPositive;
            }
        }
    }
}
