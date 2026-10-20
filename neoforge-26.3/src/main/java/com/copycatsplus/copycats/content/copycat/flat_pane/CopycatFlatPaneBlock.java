package com.copycatsplus.copycats.content.copycat.flat_pane;

import com.copycatsplus.copycats.CCBlocks;
import com.copycatsplus.copycats.CCShapes;
import com.copycatsplus.copycats.foundation.copycat.CCWaterloggedCopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.ICustomCTBlocking;
import com.copycatsplus.copycats.foundation.copycat.IStateType;
import com.copycatsplus.copycats.utility.InteractionUtils;
import com.zurrtum.create.catnip.placement.IPlacementHelper;
import com.zurrtum.create.catnip.placement.PlacementHelpers;
import com.zurrtum.create.catnip.placement.PlacementOffset;
import com.zurrtum.create.content.contraptions.StructureTransform;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.IronBarsBlock;
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
public class CopycatFlatPaneBlock extends CCWaterloggedCopycatBlock implements IStateType, ICustomCTBlocking {
    public static final EnumProperty<Axis> AXIS = BlockStateProperties.AXIS;
    private static final int placementHelperId = PlacementHelpers.register(new CopycatFlatPaneBlock.PlacementHelper());

    public CopycatFlatPaneBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.defaultBlockState().setValue(AXIS, Axis.Y));
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
    public boolean isPathfindable(BlockState pState, PathComputationType pType) {
        return switch (pType) {
            case LAND -> pState.getValue(AXIS).isHorizontal();
            default -> false;
        };
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState stateForPlacement = super.getStateForPlacement(context);
        if (stateForPlacement == null) {
            return null;
        } else {
            Axis axis = context.getNearestLookingDirection().getAxis();
            return stateForPlacement.setValue(AXIS, axis);
        }
    }

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder.add(AXIS));
    }

    @NotNull
    @Override
    public VoxelShape getShape(BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        return CCShapes.HORIZONTAL_PANE.get(pState.getValue(AXIS)).toShape();
    }

    @Override
    public BlockState transform(BlockState state, StructureTransform transform) {
        if (transform.rotationAxis != null) {
            state = state.setValue(AXIS, transform.rotateAxis(state.getValue(AXIS)));
        }

        return state;
    }

    @Override
    public boolean isIgnoredConnectivitySide(
        BlockAndTintGetter reader, BlockState fromState, Direction face, BlockPos fromPos, @Nullable BlockPos toPos, @Nullable BlockState toState
    ) {
        if (toPos == null) {
            return true;
        } else {
            toState = reader.getBlockState(toPos);
            Vec3i diff = toPos.subtract(fromPos);
            if (diff.equals(Vec3i.ZERO)) {
                return false;
            } else {
                Direction facing = Direction.getApproximateNearest((float)diff.getX(), (float)diff.getY(), (float)diff.getZ());
                if (toState.getBlock() instanceof IronBarsBlock) {
                    return facing == null ? true : face.getAxis().isVertical() || fromState.getValue(AXIS) == facing.getAxis();
                } else if (toState.is(this)) {
                    return facing == null ? true : toState.getValue(AXIS) != fromState.getValue(AXIS) || fromState.getValue(AXIS) == facing.getAxis();
                } else {
                    return true;
                }
            }
        }
    }

    @Override
    public boolean canConnectTexturesToward(BlockAndTintGetter reader, BlockPos fromPos, BlockPos toPos, BlockState fromState) {
        BlockState toState = reader.getBlockState(toPos);
        Vec3i diff = toPos.subtract(fromPos);
        if (diff.equals(Vec3i.ZERO)) {
            return true;
        } else {
            Direction facing = Direction.getApproximateNearest((float)diff.getX(), (float)diff.getY(), (float)diff.getZ());
            if (toState.getBlock() instanceof IronBarsBlock) {
                return facing == null ? false : fromState.getValue(AXIS) != facing.getAxis();
            } else if (toState.is(this)) {
                return facing == null ? false : toState.getValue(AXIS) == fromState.getValue(AXIS) && fromState.getValue(AXIS) != facing.getAxis();
            } else {
                return false;
            }
        }
    }

    @Override
    public Optional<Boolean> blockCTTowards(BlockAndTintGetter reader, BlockState state, BlockPos pos, BlockPos ctPos, BlockPos connectingPos, Direction face) {
        return Optional.of(false);
    }

    @Override
    public Optional<Boolean> isCTBlocked(
        BlockAndTintGetter reader, BlockState state, BlockPos pos, BlockPos connectingPos, BlockPos blockingPos, Direction face
    ) {
        return Optional.of(false);
    }

    public boolean supportsExternalFaceHiding(BlockState state) {
        return true;
    }

    public boolean hidesNeighborFace(BlockGetter level, BlockPos pos, BlockState state, BlockState neighborState, Direction dir) {
        return ICopycatBlock.hidesNeighborFace(level, pos, state, neighborState, dir);
    }

    private static class PlacementHelper implements IPlacementHelper {
        @Override
        public Predicate<ItemStack> getItemPredicate() {
            return stack -> stack.is(CCBlocks.COPYCAT_FLAT_PANE.get().asItem());
        }

        @Override
        public Predicate<BlockState> getStatePredicate() {
            return state -> state.getBlock() instanceof CopycatFlatPaneBlock;
        }

        @Override
        public PlacementOffset getOffset(Player player, Level world, BlockState state, BlockPos pos, BlockHitResult ray) {
            List<Direction> directions = IPlacementHelper.orderedByDistanceExceptAxis(
                pos, ray.getLocation(), state.getValue(CopycatFlatPaneBlock.AXIS), dir -> world.getBlockState(pos.relative(dir)).canBeReplaced()
            );
            return directions.isEmpty()
                ? PlacementOffset.fail()
                : PlacementOffset.success(
                    pos.relative(directions.get(0)), s -> s.setValue(CopycatFlatPaneBlock.AXIS, state.getValue(CopycatFlatPaneBlock.AXIS))
                );
        }
    }
}
