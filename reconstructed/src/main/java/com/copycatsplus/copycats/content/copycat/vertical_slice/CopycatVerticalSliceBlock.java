/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.zurrtum.create.api.schematic.requirement.SpecialBlockItemRequirement
 *  com.zurrtum.create.catnip.data.Iterate
 *  com.zurrtum.create.catnip.data.Pair
 *  com.zurrtum.create.content.contraptions.StructureTransform
 *  com.zurrtum.create.content.equipment.wrench.IWrenchable
 *  com.zurrtum.create.content.schematics.requirement.ItemRequirement
 *  javax.annotation.ParametersAreNonnullByDefault
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.core.Direction$Axis
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.world.InteractionResult
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemInstance
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.context.BlockPlaceContext
 *  net.minecraft.world.item.context.UseOnContext
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.state.BlockBehaviour$Properties
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.StateDefinition$Builder
 *  net.minecraft.world.level.block.state.properties.BlockStateProperties
 *  net.minecraft.world.level.block.state.properties.EnumProperty
 *  net.minecraft.world.level.block.state.properties.IntegerProperty
 *  net.minecraft.world.level.block.state.properties.Property
 *  net.minecraft.world.level.pathfinder.PathComputationType
 *  net.minecraft.world.phys.shapes.CollisionContext
 *  net.minecraft.world.phys.shapes.VoxelShape
 *  org.jetbrains.annotations.NotNull
 */
package com.copycatsplus.copycats.content.copycat.vertical_slice;

import com.copycatsplus.copycats.CCBlocks;
import com.copycatsplus.copycats.CCShapes;
import com.copycatsplus.copycats.Copycats;
import com.copycatsplus.copycats.foundation.copycat.CCWaterloggedCopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.IStateType;
import com.copycatsplus.copycats.utility.BlockUtils;
import com.zurrtum.create.api.schematic.requirement.SpecialBlockItemRequirement;
import com.zurrtum.create.catnip.data.Iterate;
import com.zurrtum.create.catnip.data.Pair;
import com.zurrtum.create.content.contraptions.StructureTransform;
import com.zurrtum.create.content.equipment.wrench.IWrenchable;
import com.zurrtum.create.content.schematics.requirement.ItemRequirement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

@ParametersAreNonnullByDefault
public class CopycatVerticalSliceBlock
extends CCWaterloggedCopycatBlock
implements SpecialBlockItemRequirement,
IStateType {
    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final IntegerProperty LAYERS = BlockStateProperties.LAYERS;
    private static final Map<Pair<Integer, Integer>, Direction> VERTICAL_POSITION_MAP = new HashMap<Pair<Integer, Integer>, Direction>();
    private static final Map<Pair<Direction, Integer>, Direction> HORIZONTAL_POSITION_MAP = new HashMap<Pair<Direction, Integer>, Direction>();

    public CopycatVerticalSliceBlock(BlockBehaviour.Properties pProperties) {
        super(pProperties);
        this.registerDefaultState((BlockState)((BlockState)this.defaultBlockState().setValue(FACING, (Comparable)Direction.NORTH)).setValue((Property)LAYERS, (Comparable)Integer.valueOf(1)));
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
        BlockPos blockPos = context.getClickedPos();
        BlockState state = context.getLevel().getBlockState(blockPos);
        if (state.is((Object)this)) {
            if ((Integer)state.getValue((Property)LAYERS) < 8) {
                return (BlockState)state.cycle((Property)LAYERS);
            }
            Copycats.LOGGER.warn("Can't figure out where to place a slice! Please file an issue if you see this.");
            return state;
        }
        int xOffset = context.getClickLocation().x - (double)context.getClickedPos().getX() > 0.5 ? 1 : -1;
        int n = zOffset = context.getClickLocation().z - (double)context.getClickedPos().getZ() > 0.5 ? 1 : -1;
        if (context.getClickedFace().getAxis() == Direction.Axis.Y) {
            return (BlockState)stateForPlacement.setValue(FACING, (Comparable)VERTICAL_POSITION_MAP.get(Pair.of((Object)xOffset, (Object)zOffset)));
        }
        return (BlockState)stateForPlacement.setValue(FACING, (Comparable)HORIZONTAL_POSITION_MAP.get(Pair.of((Object)context.getClickedFace(), (Object)(context.getClickedFace().getAxis() == Direction.Axis.X ? zOffset : xOffset))));
    }

    public boolean canBeReplaced(@NotNull BlockState pState, BlockPlaceContext pUseContext) {
        ItemStack itemstack = pUseContext.getItemInHand();
        if (!itemstack.is((Object)this.asItem())) {
            return false;
        }
        if ((Integer)pState.getValue((Property)LAYERS) == 8) {
            return false;
        }
        Direction facing = (Direction)pState.getValue(FACING);
        return pUseContext.getClickedFace() == facing.getOpposite() || pUseContext.getClickedFace() == facing.getClockWise();
    }

    @Override
    public InteractionResult onSneakWrenched(BlockState state, UseOnContext context) {
        if ((Integer)state.getValue((Property)LAYERS) <= 1) {
            return super.onSneakWrenched(state, context);
        }
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        if (world instanceof ServerLevel) {
            ServerLevel serverLevel = (ServerLevel)world;
            if (player != null && !player.isCreative()) {
                List drops = Block.getDrops((BlockState)((BlockState)state.setValue((Property)LAYERS, (Comparable)Integer.valueOf(1))), (ServerLevel)serverLevel, (BlockPos)pos, (BlockEntity)world.getBlockEntity(pos), (Entity)player, (ItemInstance)context.getItemInHand());
                for (ItemStack drop : drops) {
                    player.getInventory().placeItemBackInInventory(drop);
                }
            }
            BlockPos up = pos.relative(Direction.UP);
            world.setBlockAndUpdate(pos, (BlockState)state.setValue((Property)LAYERS, (Comparable)Integer.valueOf((Integer)state.getValue((Property)LAYERS) - 1)));
            IWrenchable.playRemoveSound((Level)world, (BlockPos)pos);
        }
        return InteractionResult.SUCCESS;
    }

    public ItemRequirement getRequiredItems(BlockState state, BlockEntity blockEntity) {
        return ICopycatBlock.getRequiredItemsForLayer(state, LAYERS);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition((StateDefinition.Builder<Block, BlockState>)pBuilder.add(new Property[]{FACING, LAYERS}));
    }

    @NotNull
    public VoxelShape getShape(BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        return CCShapes.VERTICAL_SLICE.get(pState.getValue(FACING)).get(pState.getValue((Property)LAYERS)).toShape();
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
        return BlockUtils.transformStepLikeVertical(state, transform, CCBlocks.COPYCAT_SLICE.getDefaultState());
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
}

