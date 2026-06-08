/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.math.OctahedralGroup
 *  com.zurrtum.create.content.contraptions.StructureTransform
 *  com.zurrtum.create.foundation.block.IBE
 *  javax.annotation.Nullable
 *  javax.annotation.ParametersAreNonnullByDefault
 *  net.minecraft.client.renderer.block.BlockAndTintGetter
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.core.Direction$Axis
 *  net.minecraft.core.Direction$AxisDirection
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.InteractionResult
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.Blocks
 *  net.minecraft.world.level.block.Mirror
 *  net.minecraft.world.level.block.Rotation
 *  net.minecraft.world.level.block.StairBlock
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.entity.BlockEntityTicker
 *  net.minecraft.world.level.block.entity.BlockEntityType
 *  net.minecraft.world.level.block.state.BlockBehaviour$Properties
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.properties.Half
 *  net.minecraft.world.level.block.state.properties.Property
 *  net.minecraft.world.level.block.state.properties.StairsShape
 *  net.minecraft.world.phys.BlockHitResult
 */
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
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.StairsShape;
import net.minecraft.world.phys.BlockHitResult;

@ParametersAreNonnullByDefault
public class CopycatStairsBlock
extends StairBlock
implements ICopycatBlock,
ICustomCTBlocking,
IBE<CCCopycatBlockEntity>,
IStateType {
    public CopycatStairsBlock(BlockBehaviour.Properties properties) {
        super(Blocks.OAK_PLANKS.defaultBlockState(), properties);
    }

    @Nullable
    public <S extends BlockEntity> BlockEntityTicker<S> getTicker(Level level, BlockState state, BlockEntityType<S> type) {
        return null;
    }

    protected InteractionResult useItemOn(ItemStack heldStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        return InteractionUtils.sequential(() -> ICopycatBlock.super.use(state, level, pos, player, hand, hit), () -> super.useItemOn(heldStack, state, level, pos, player, hand, hit));
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

    public Class<CCCopycatBlockEntity> getBlockEntityClass() {
        return CCCopycatBlockEntity.class;
    }

    public BlockEntityType<? extends CCCopycatBlockEntity> getBlockEntityType() {
        return (BlockEntityType)CCBlockEntityTypes.COPYCAT.get();
    }

    public BlockState rotate(BlockState state, Rotation rotation) {
        return ICopycatBlock.super.rotateCopycat(state, rotation);
    }

    public BlockState mirror(BlockState state, Mirror mirror) {
        return ICopycatBlock.super.mirrorCopycat(state, mirror);
    }

    @Override
    public BlockState transform(BlockState state, StructureTransform transform) {
        if (transform.mirror != null && transform.mirror != Mirror.NONE) {
            state = transform.mirror.rotation() == OctahedralGroup.INVERT_Y ? (BlockState)state.cycle((Property)HALF) : (BlockState)state.setValue((Property)FACING, (Comparable)transform.mirror.mirror((Direction)state.getValue((Property)FACING)));
        }
        if (transform.rotationAxis != null) {
            if (transform.rotationAxis == Direction.Axis.Y) {
                state = (BlockState)state.setValue((Property)FACING, (Comparable)transform.rotateFacing((Direction)state.getValue((Property)FACING)));
            } else {
                Direction facing = (Direction)state.getValue((Property)FACING);
                Half half = (Half)state.getValue((Property)HALF);
                if (transform.rotationAxis == facing.getAxis()) {
                    if (transform.rotation == Rotation.CLOCKWISE_180) {
                        state = (BlockState)state.cycle((Property)HALF);
                    } else if (transform.rotation != Rotation.NONE) {
                        Direction offset = transform.rotateFacing(half == Half.TOP ? Direction.UP : Direction.DOWN);
                        state = (BlockState)((BlockState)BlockUtils.tryCopyProperties(state, CCBlocks.COPYCAT_VERTICAL_STAIRS.getDefaultState()).setValue((Property)FACING, (Comparable)offset)).setValue(CCBlockStateProperties.SIDE, (Comparable)((Object)(offset == facing.getClockWise() ? CCBlockStateProperties.Side.LEFT : CCBlockStateProperties.Side.RIGHT)));
                    }
                } else {
                    state = BlockUtils.setApparentDirection(state, transform.rotateFacing(BlockUtils.getApparentDirection(state)));
                }
            }
        }
        return state;
    }

    @Override
    public Optional<Boolean> isCTBlocked(BlockAndTintGetter reader, BlockState state, BlockPos pos, BlockPos connectingPos, BlockPos blockingPos, Direction face) {
        return connectingPos.getY() >= pos.getY() ? Optional.empty() : Optional.of(false);
    }

    @Override
    public Optional<Boolean> blockCTTowards(BlockAndTintGetter reader, BlockState state, BlockPos pos, BlockPos ctPos, BlockPos connectingPos, Direction face) {
        return Optional.of(false);
    }

    public boolean supportsExternalFaceHiding(BlockState state) {
        return true;
    }

    @Override
    public boolean hidesNeighborFace(BlockGetter level, BlockPos pos, BlockState state, BlockState neighborState, Direction dir) {
        return ICopycatBlock.hidesNeighborFace(level, pos, state, neighborState, dir);
    }

    private static Direction.AxisDirection directionOf(int value) {
        return value >= 0 ? Direction.AxisDirection.POSITIVE : Direction.AxisDirection.NEGATIVE;
    }

    public static FaceShape getFaceShape(BlockState state, Direction face) {
        if (state.getBlock() instanceof CopycatVerticalStairBlock) {
            return CopycatVerticalStairBlock.getFaceShape(state, face);
        }
        boolean top = state.getValue((Property)StairBlock.HALF) == Half.TOP;
        Direction facing = (Direction)state.getValue((Property)StairBlock.FACING);
        StairsShape shape = (StairsShape)state.getValue((Property)StairBlock.SHAPE);
        if (!top && face == Direction.DOWN) {
            return new FaceShape().fillAll();
        }
        if (top && face == Direction.UP) {
            return new FaceShape().fillAll();
        }
        FaceShape faceShape = new FaceShape();
        switch (shape) {
            case STRAIGHT: {
                if (!top && face == Direction.UP || top && face == Direction.DOWN) {
                    return faceShape.fillTop().rotate(facing.toYRot());
                }
                faceShape.fillRow(top);
                if (face == facing) {
                    return faceShape.fillRow(!top);
                }
                if (face == facing.getOpposite()) {
                    return faceShape;
                }
                return faceShape.fillRow(!top, facing.getAxisDirection());
            }
            case INNER_LEFT: {
                if (!top && face == Direction.UP || top && face == Direction.DOWN) {
                    return faceShape.fillTop().fillBottom(Direction.AxisDirection.POSITIVE).rotate(facing.toYRot());
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
                if (face != facing.getClockWise()) break;
                return faceShape.fillRow(!top, facing.getAxisDirection());
            }
            case INNER_RIGHT: {
                if (!top && face == Direction.UP || top && face == Direction.DOWN) {
                    return faceShape.fillTop().fillBottom(Direction.AxisDirection.NEGATIVE).rotate(facing.toYRot());
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
                if (face != facing.getCounterClockWise()) break;
                return faceShape.fillRow(!top, facing.getAxisDirection());
            }
            case OUTER_LEFT: {
                if (!top && face == Direction.UP || top && face == Direction.DOWN) {
                    return faceShape.fillTop(Direction.AxisDirection.POSITIVE).rotate(facing.toYRot());
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
                if (face != facing.getClockWise()) break;
                return faceShape;
            }
            case OUTER_RIGHT: {
                if (!top && face == Direction.UP || top && face == Direction.DOWN) {
                    return faceShape.fillTop(Direction.AxisDirection.NEGATIVE).rotate(facing.toYRot());
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
                if (face != facing.getCounterClockWise()) break;
                return faceShape;
            }
        }
        return faceShape;
    }

    public static class FaceShape {
        public boolean topNegative;
        public boolean topPositive;
        public boolean bottomNegative;
        public boolean bottomPositive;

        public FaceShape fillTop() {
            this.topPositive = true;
            this.topNegative = true;
            return this;
        }

        public FaceShape fillColumn(Direction.AxisDirection direction) {
            switch (direction) {
                case POSITIVE: {
                    this.bottomPositive = true;
                    this.topPositive = true;
                    break;
                }
                case NEGATIVE: {
                    this.bottomNegative = true;
                    this.topNegative = true;
                }
            }
            return this;
        }

        public FaceShape fillNegative() {
            this.bottomNegative = true;
            this.topNegative = true;
            return this;
        }

        public FaceShape fillPositive() {
            this.bottomPositive = true;
            this.topPositive = true;
            return this;
        }

        public FaceShape fillLeft(Direction relativeTo) {
            return this.fillColumn(relativeTo.getClockWise().getAxisDirection());
        }

        public FaceShape fillRight(Direction relativeTo) {
            return this.fillColumn(relativeTo.getCounterClockWise().getAxisDirection());
        }

        public FaceShape fillTop(Direction.AxisDirection direction) {
            switch (direction) {
                case POSITIVE: {
                    this.topPositive = true;
                    break;
                }
                case NEGATIVE: {
                    this.topNegative = true;
                }
            }
            return this;
        }

        public FaceShape fillBottom() {
            this.bottomPositive = true;
            this.bottomNegative = true;
            return this;
        }

        public FaceShape fillBottom(Direction.AxisDirection direction) {
            switch (direction) {
                case POSITIVE: {
                    this.bottomPositive = true;
                    break;
                }
                case NEGATIVE: {
                    this.bottomNegative = true;
                }
            }
            return this;
        }

        public FaceShape fillRow(boolean top) {
            if (top) {
                return this.fillTop();
            }
            return this.fillBottom();
        }

        public FaceShape fillRow(boolean top, Direction.AxisDirection direction) {
            if (top) {
                return this.fillTop(direction);
            }
            return this.fillBottom(direction);
        }

        public FaceShape fillAll() {
            return this.fillTop().fillBottom();
        }

        public FaceShape rotate(float angle) {
            return this.rotate((int)angle);
        }

        public FaceShape rotate(int angle) {
            if ((angle %= 360) < 0) {
                angle += 360;
            }
            return switch (angle) {
                case 90 -> this.set(this.topNegative, this.bottomNegative, this.topPositive, this.bottomPositive);
                case 180 -> this.set(this.topPositive, this.topNegative, this.bottomPositive, this.bottomNegative);
                case 270 -> this.set(this.bottomPositive, this.topPositive, this.bottomNegative, this.topNegative);
                default -> this;
            };
        }

        public FaceShape set(boolean bottomNegative, boolean bottomPositive, boolean topNegative, boolean topPositive) {
            this.bottomNegative = bottomNegative;
            this.bottomPositive = bottomPositive;
            this.topNegative = topNegative;
            this.topPositive = topPositive;
            return this;
        }

        public int countBlocks() {
            int count = 0;
            if (this.bottomNegative) {
                ++count;
            }
            if (this.bottomPositive) {
                ++count;
            }
            if (this.topNegative) {
                ++count;
            }
            if (this.topPositive) {
                ++count;
            }
            return count;
        }

        public boolean canConnect() {
            return this.countBlocks() >= 3;
        }

        public boolean isFull() {
            return this.countBlocks() == 4;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof FaceShape)) {
                return false;
            }
            FaceShape shape = (FaceShape)obj;
            return shape.bottomNegative == this.bottomNegative && shape.bottomPositive == this.bottomPositive && shape.topNegative == this.topNegative && shape.topPositive == this.topPositive;
        }
    }
}

