package com.copycatsplus.copycats.content.copycat.vertical_slope;

import com.copycatsplus.copycats.CCBlocks;
import com.copycatsplus.copycats.CCShapes;
import com.copycatsplus.copycats.foundation.copycat.CCWaterloggedCopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.ICustomCTBlocking;
import com.copycatsplus.copycats.foundation.copycat.IStateType;
import com.copycatsplus.copycats.utility.BlockUtils;
import com.copycatsplus.copycats.utility.InteractionUtils;
import com.zurrtum.create.catnip.data.Iterate;
import com.zurrtum.create.catnip.data.Pair;
import com.zurrtum.create.catnip.placement.PlacementHelpers;
import com.zurrtum.create.content.contraptions.StructureTransform;
import com.zurrtum.create.foundation.placement.PoleHelper;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
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
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

@ParametersAreNonnullByDefault
public class CopycatVerticalSlopeBlock extends CCWaterloggedCopycatBlock implements IStateType, ICustomCTBlocking {
    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
    private static final int placementHelperId = PlacementHelpers.register(new CopycatVerticalSlopeBlock.PlacementHelper());
    private static final Map<Pair<Integer, Integer>, Direction> VERTICAL_POSITION_MAP = new HashMap<>();
    private static final Map<Pair<Direction, Integer>, Direction> HORIZONTAL_POSITION_MAP = new HashMap<>();

    public CopycatVerticalSlopeBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH));
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
    public Optional<Boolean> blockCTTowards(BlockAndTintGetter reader, BlockState state, BlockPos pos, BlockPos ctPos, BlockPos connectingPos, Direction face) {
        return reader.getBlockState(ctPos).is(this) ? Optional.of(false) : Optional.empty();
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
            int xOffset = context.getClickLocation().x - context.getClickedPos().getX() > 0.5 ? 1 : -1;
            int zOffset = context.getClickLocation().z - context.getClickedPos().getZ() > 0.5 ? 1 : -1;
            return context.getClickedFace().getAxis() == Axis.Y
                ? stateForPlacement.setValue(FACING, VERTICAL_POSITION_MAP.get(Pair.of(xOffset, zOffset)))
                : stateForPlacement.setValue(
                    FACING, HORIZONTAL_POSITION_MAP.get(Pair.of(context.getClickedFace(), context.getClickedFace().getAxis() == Axis.X ? zOffset : xOffset))
                );
        }
    }

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder.add(FACING));
    }

    @NotNull
    @Override
    public VoxelShape getShape(BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        return CCShapes.VERTICAL_SLOPE.get(pState.getValue(FACING)).toShape();
    }

    public boolean supportsExternalFaceHiding(BlockState state) {
        return true;
    }

    public boolean hidesNeighborFace(BlockGetter level, BlockPos pos, BlockState state, BlockState neighborState, Direction dir) {
        return ICopycatBlock.hidesNeighborFace(level, pos, state, neighborState, dir);
    }

    @Override
    public BlockState transform(BlockState state, StructureTransform transform) {
        return BlockUtils.transformStepLikeVertical(state, transform, CCBlocks.COPYCAT_SLOPE.getDefaultState());
    }

    static {
        for (Direction main : Iterate.horizontalDirections) {
            Direction cross = main.getCounterClockWise();
            int mainOffset = main.getAxisDirection().getStep();
            int crossOffset = cross.getAxisDirection().getStep();
            if (main.getAxis() == Axis.X) {
                VERTICAL_POSITION_MAP.put(Pair.of(mainOffset, crossOffset), main);
            } else {
                VERTICAL_POSITION_MAP.put(Pair.of(crossOffset, mainOffset), main);
            }

            HORIZONTAL_POSITION_MAP.put(Pair.of(main.getOpposite(), crossOffset), main);
            HORIZONTAL_POSITION_MAP.put(Pair.of(cross.getOpposite(), mainOffset), main);
        }
    }

    private static class PlacementHelper extends PoleHelper<Direction> {
        private PlacementHelper() {
            super(state -> state.getBlock() instanceof CopycatVerticalSlopeBlock, $ -> Axis.Y, CopycatVerticalSlopeBlock.FACING);
        }

        @Override
        public Predicate<ItemStack> getItemPredicate() {
            return i -> i.getItem() instanceof BlockItem && ((BlockItem)i.getItem()).getBlock() instanceof CopycatVerticalSlopeBlock;
        }
    }
}
