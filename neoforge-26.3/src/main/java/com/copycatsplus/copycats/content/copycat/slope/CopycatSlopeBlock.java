package com.copycatsplus.copycats.content.copycat.slope;

import com.copycatsplus.copycats.CCBlocks;
import com.copycatsplus.copycats.CCShapes;
import com.copycatsplus.copycats.foundation.copycat.CCWaterloggedCopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.ICustomCTBlocking;
import com.copycatsplus.copycats.foundation.copycat.IStateType;
import com.copycatsplus.copycats.utility.BlockUtils;
import com.copycatsplus.copycats.utility.InteractionUtils;
import com.zurrtum.create.catnip.placement.PlacementHelpers;
import com.zurrtum.create.content.contraptions.StructureTransform;
import com.zurrtum.create.foundation.placement.PoleHelper;
import java.util.Optional;
import java.util.function.Predicate;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

@ParametersAreNonnullByDefault
public class CopycatSlopeBlock extends CCWaterloggedCopycatBlock implements IStateType, ICustomCTBlocking {
    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<Half> HALF = BlockStateProperties.HALF;
    private static final int placementHelperId = PlacementHelpers.register(new CopycatSlopeBlock.PlacementHelper());

    public CopycatSlopeBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH).setValue(HALF, Half.BOTTOM));
    }

    @NotNull
    @Override
    protected InteractionResult useItemOn(
        ItemStack heldStack,
        @NotNull BlockState state,
        @NotNull Level world,
        @NotNull BlockPos pos,
        @NotNull Player player,
        @NotNull InteractionHand hand,
        @NotNull BlockHitResult ray
    ) {
        return InteractionUtils.sequential(
            () -> InteractionUtils.usePlacementHelper(placementHelperId, state, world, pos, player, hand, ray),
            () -> super.useItemOn(heldStack, state, world, pos, player, hand, ray)
        );
    }

    @Override
    public Optional<Boolean> isCTBlocked(
        BlockAndTintGetter reader, BlockState state, BlockPos pos, BlockPos connectingPos, BlockPos blockingPos, Direction face
    ) {
        return reader.getBlockState(blockingPos).is(this) ? Optional.of(false) : Optional.empty();
    }

    @Override
    public boolean isPathfindable(BlockState pState, PathComputationType pType) {
        return false;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState stateForPlacement = super.getStateForPlacement(context);
        if (stateForPlacement == null) {
            return null;
        } else {
            Direction facing = context.getHorizontalDirection();
            Half half = context.getClickedFace() == Direction.DOWN
                ? Half.TOP
                : (
                    context.getClickedFace() == Direction.UP
                        ? Half.BOTTOM
                        : (context.getClickLocation().y - context.getClickedPos().getY() > 0.5 ? Half.TOP : Half.BOTTOM)
                );
            return stateForPlacement.setValue(FACING, facing).setValue(HALF, half);
        }
    }

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder.add(FACING, HALF));
    }

    @NotNull
    @Override
    public VoxelShape getShape(BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        return CCShapes.SLOPE.get(pState.getValue(FACING)).get(pState.getValue(HALF)).toShape();
    }

    @NotNull
    @Override
    public VoxelShape getCollisionShape(BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return CCShapes.SLOPE_COLLISION.get(state.getValue(FACING)).get(state.getValue(HALF)).toShape();
    }

    public boolean supportsExternalFaceHiding(BlockState state) {
        return true;
    }

    public boolean hidesNeighborFace(BlockGetter level, BlockPos pos, BlockState state, BlockState neighborState, Direction dir) {
        return ICopycatBlock.hidesNeighborFace(level, pos, state, neighborState, dir);
    }

    @Override
    public BlockState transform(BlockState state, StructureTransform transform) {
        return BlockUtils.transformStepLikeHorizontal(state, transform, CCBlocks.COPYCAT_VERTICAL_SLOPE.getDefaultState());
    }

    private static class PlacementHelper extends PoleHelper<Direction> {
        private PlacementHelper() {
            super(
                state -> state.getBlock() instanceof CopycatSlopeBlock,
                state -> state.getValue(CopycatSlopeBlock.FACING).getClockWise().getAxis(),
                CopycatSlopeBlock.FACING
            );
        }

        @Override
        public Predicate<ItemStack> getItemPredicate() {
            return i -> i.getItem() instanceof BlockItem && ((BlockItem)i.getItem()).getBlock() instanceof CopycatSlopeBlock;
        }
    }
}
