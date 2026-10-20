package com.copycatsplus.copycats.content.copycat.beam;

import com.copycatsplus.copycats.CCShapes;
import com.copycatsplus.copycats.foundation.copycat.CCWaterloggedCopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.IStateType;
import com.copycatsplus.copycats.utility.InteractionUtils;
import com.zurrtum.create.catnip.placement.PlacementHelpers;
import com.zurrtum.create.content.contraptions.StructureTransform;
import com.zurrtum.create.foundation.placement.PoleHelper;
import java.util.function.Predicate;
import javax.annotation.ParametersAreNonnullByDefault;
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
public class CopycatBeamBlock extends CCWaterloggedCopycatBlock implements IStateType {
    public static final EnumProperty<Axis> AXIS = BlockStateProperties.AXIS;
    private static final int placementHelperId = PlacementHelpers.register(new CopycatBeamBlock.PlacementHelper());

    public CopycatBeamBlock(Properties pProperties) {
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
        return CCShapes.BEAM.get(pState.getValue(AXIS)).toShape();
    }

    @Override
    public BlockState transform(BlockState state, StructureTransform transform) {
        if (transform.rotationAxis != null) {
            state = state.setValue(AXIS, transform.rotateAxis(state.getValue(AXIS)));
        }

        return state;
    }

    public boolean supportsExternalFaceHiding(BlockState state) {
        return true;
    }

    public boolean hidesNeighborFace(BlockGetter level, BlockPos pos, BlockState state, BlockState neighborState, Direction dir) {
        return ICopycatBlock.hidesNeighborFace(level, pos, state, neighborState, dir);
    }

    private static class PlacementHelper extends PoleHelper<Axis> {
        private PlacementHelper() {
            super(state -> state.getBlock() instanceof CopycatBeamBlock, state -> state.getValue(CopycatBeamBlock.AXIS), CopycatBeamBlock.AXIS);
        }

        @Override
        public Predicate<ItemStack> getItemPredicate() {
            return i -> i.getItem() instanceof BlockItem && ((BlockItem)i.getItem()).getBlock() instanceof CopycatBeamBlock;
        }
    }
}
