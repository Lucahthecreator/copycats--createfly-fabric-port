/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.zurrtum.create.AllBlocks
 *  com.zurrtum.create.catnip.data.Iterate
 *  com.zurrtum.create.catnip.data.Pair
 *  com.zurrtum.create.catnip.placement.IPlacementHelper
 *  com.zurrtum.create.catnip.placement.PlacementHelpers
 *  com.zurrtum.create.content.contraptions.StructureTransform
 *  com.zurrtum.create.foundation.placement.PoleHelper
 *  javax.annotation.ParametersAreNonnullByDefault
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.core.Direction$Axis
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.InteractionResult
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.BlockItem
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.context.BlockPlaceContext
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.state.BlockBehaviour$Properties
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.StateDefinition$Builder
 *  net.minecraft.world.level.block.state.properties.BlockStateProperties
 *  net.minecraft.world.level.block.state.properties.EnumProperty
 *  net.minecraft.world.level.block.state.properties.Property
 *  net.minecraft.world.level.pathfinder.PathComputationType
 *  net.minecraft.world.phys.BlockHitResult
 *  net.minecraft.world.phys.shapes.CollisionContext
 *  net.minecraft.world.phys.shapes.VoxelShape
 *  org.jetbrains.annotations.NotNull
 */
package com.copycatsplus.copycats.content.copycat.vertical_step;

import com.copycatsplus.copycats.CCShapes;
import com.copycatsplus.copycats.foundation.copycat.CCWaterloggedCopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.IStateType;
import com.copycatsplus.copycats.utility.BlockUtils;
import com.copycatsplus.copycats.utility.InteractionUtils;
import com.zurrtum.create.AllBlocks;
import com.zurrtum.create.catnip.data.Iterate;
import com.zurrtum.create.catnip.data.Pair;
import com.zurrtum.create.catnip.placement.IPlacementHelper;
import com.zurrtum.create.catnip.placement.PlacementHelpers;
import com.zurrtum.create.content.contraptions.StructureTransform;
import com.zurrtum.create.foundation.placement.PoleHelper;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import javax.annotation.ParametersAreNonnullByDefault;
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
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

@ParametersAreNonnullByDefault
public class CopycatVerticalStepBlock
extends CCWaterloggedCopycatBlock
implements IStateType {
    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
    private static final int placementHelperId = PlacementHelpers.register((IPlacementHelper)new PlacementHelper());
    private static final Map<Pair<Integer, Integer>, Direction> VERTICAL_POSITION_MAP = new HashMap<Pair<Integer, Integer>, Direction>();
    private static final Map<Pair<Direction, Integer>, Direction> HORIZONTAL_POSITION_MAP = new HashMap<Pair<Direction, Integer>, Direction>();

    public CopycatVerticalStepBlock(BlockBehaviour.Properties pProperties) {
        super(pProperties);
        this.registerDefaultState((BlockState)this.defaultBlockState().setValue(FACING, (Comparable)Direction.NORTH));
    }

    @Override
    @NotNull
    protected InteractionResult useItemOn(ItemStack heldStack, @NotNull BlockState state, @NotNull Level world, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult ray) {
        return InteractionUtils.sequential(() -> InteractionUtils.usePlacementHelper(placementHelperId, state, world, pos, player, hand, ray), () -> super.useItemOn(heldStack, state, world, pos, player, hand, ray));
    }

    public boolean isPathfindable(BlockState pState, PathComputationType pType) {
        return false;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        int zOffset;
        BlockState stateForPlacement = super.getStateForPlacement(context);
        if (stateForPlacement == null) {
            return null;
        }
        int xOffset = context.getClickLocation().x - (double)context.getClickedPos().getX() > 0.5 ? 1 : -1;
        int n = zOffset = context.getClickLocation().z - (double)context.getClickedPos().getZ() > 0.5 ? 1 : -1;
        if (context.getClickedFace().getAxis() == Direction.Axis.Y) {
            return (BlockState)stateForPlacement.setValue(FACING, (Comparable)VERTICAL_POSITION_MAP.get(Pair.of((Object)xOffset, (Object)zOffset)));
        }
        return (BlockState)stateForPlacement.setValue(FACING, (Comparable)HORIZONTAL_POSITION_MAP.get(Pair.of((Object)context.getClickedFace(), (Object)(context.getClickedFace().getAxis() == Direction.Axis.X ? zOffset : xOffset))));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition((StateDefinition.Builder<Block, BlockState>)pBuilder.add(new Property[]{FACING}));
    }

    @NotNull
    public VoxelShape getShape(BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        return CCShapes.VERTICAL_STEP.get(pState.getValue(FACING)).toShape();
    }

    public boolean supportsExternalFaceHiding(BlockState state) {
        return true;
    }

    @Override
    public boolean hidesNeighborFace(BlockGetter level, BlockPos pos, BlockState state, BlockState neighborState, Direction dir) {
        return ICopycatBlock.hidesNeighborFace(level, pos, state, neighborState, dir);
    }

    @Override
    public BlockState transform(BlockState state, StructureTransform transform) {
        return BlockUtils.transformStepLikeVertical(state, transform, AllBlocks.COPYCAT_STEP.defaultBlockState());
    }

    static {
        for (Direction main : Iterate.horizontalDirections) {
            Direction cross = main.getCounterClockWise();
            int mainOffset = main.getAxisDirection().getStep();
            int crossOffset = cross.getAxisDirection().getStep();
            if (main.getAxis() == Direction.Axis.X) {
                VERTICAL_POSITION_MAP.put((Pair<Integer, Integer>)Pair.of((Object)mainOffset, (Object)crossOffset), main);
            } else {
                VERTICAL_POSITION_MAP.put((Pair<Integer, Integer>)Pair.of((Object)crossOffset, (Object)mainOffset), main);
            }
            HORIZONTAL_POSITION_MAP.put((Pair<Direction, Integer>)Pair.of((Object)main.getOpposite(), (Object)crossOffset), main);
            HORIZONTAL_POSITION_MAP.put((Pair<Direction, Integer>)Pair.of((Object)cross.getOpposite(), (Object)mainOffset), main);
        }
    }

    private static class PlacementHelper
    extends PoleHelper<Direction> {
        private PlacementHelper() {
            super(state -> state.getBlock() instanceof CopycatVerticalStepBlock, $ -> Direction.Axis.Y, FACING);
        }

        public Predicate<ItemStack> getItemPredicate() {
            return i -> i.getItem() instanceof BlockItem && ((BlockItem)i.getItem()).getBlock() instanceof CopycatVerticalStepBlock;
        }
    }
}

