/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.zurrtum.create.api.schematic.requirement.SpecialBlockItemRequirement
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
 *  net.minecraft.world.level.block.Rotation
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.state.BlockBehaviour$Properties
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.StateDefinition$Builder
 *  net.minecraft.world.level.block.state.properties.BlockStateProperties
 *  net.minecraft.world.level.block.state.properties.EnumProperty
 *  net.minecraft.world.level.block.state.properties.Half
 *  net.minecraft.world.level.block.state.properties.IntegerProperty
 *  net.minecraft.world.level.block.state.properties.Property
 *  net.minecraft.world.level.pathfinder.PathComputationType
 *  net.minecraft.world.phys.shapes.CollisionContext
 *  net.minecraft.world.phys.shapes.VoxelShape
 *  org.jetbrains.annotations.NotNull
 */
package com.copycatsplus.copycats.content.copycat.slope_layer;

import com.copycatsplus.copycats.CCShapes;
import com.copycatsplus.copycats.Copycats;
import com.copycatsplus.copycats.foundation.copycat.CCWaterloggedCopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.IStateType;
import com.copycatsplus.copycats.utility.BlockUtils;
import com.zurrtum.create.api.schematic.requirement.SpecialBlockItemRequirement;
import com.zurrtum.create.content.contraptions.StructureTransform;
import com.zurrtum.create.content.equipment.wrench.IWrenchable;
import com.zurrtum.create.content.schematics.requirement.ItemRequirement;
import java.util.List;
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
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

@ParametersAreNonnullByDefault
public class CopycatSlopeLayerBlock
extends CCWaterloggedCopycatBlock
implements SpecialBlockItemRequirement,
IStateType {
    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<Half> HALF = BlockStateProperties.HALF;
    public static final IntegerProperty LAYERS = BlockStateProperties.LAYERS;

    public CopycatSlopeLayerBlock(BlockBehaviour.Properties pProperties) {
        super(pProperties);
        this.registerDefaultState((BlockState)((BlockState)((BlockState)this.defaultBlockState().setValue(FACING, (Comparable)Direction.NORTH)).setValue(HALF, (Comparable)Half.BOTTOM)).setValue((Property)LAYERS, (Comparable)Integer.valueOf(1)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition((StateDefinition.Builder<Block, BlockState>)pBuilder.add(new Property[]{FACING, HALF, LAYERS}));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
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
            Copycats.LOGGER.warn("Can't figure out where to place a slope layer! Please file an issue if you see this.");
            return state;
        }
        Half half = context.getClickedFace() == Direction.DOWN ? Half.TOP : (context.getClickedFace() == Direction.UP ? Half.BOTTOM : (context.getClickLocation().y - (double)context.getClickedPos().getY() > 0.5 ? Half.TOP : Half.BOTTOM));
        return (BlockState)((BlockState)stateForPlacement.setValue(FACING, (Comparable)context.getHorizontalDirection())).setValue(HALF, (Comparable)half);
    }

    public boolean canBeReplaced(@NotNull BlockState pState, BlockPlaceContext pUseContext) {
        ItemStack itemstack = pUseContext.getItemInHand();
        if (!itemstack.is((Object)this.asItem())) {
            return false;
        }
        if ((Integer)pState.getValue((Property)LAYERS) == 8) {
            return false;
        }
        Half half = (Half)pState.getValue(HALF);
        Direction facing = (Direction)pState.getValue(FACING);
        if (pUseContext.getClickedFace() == facing.getOpposite()) {
            return true;
        }
        if (half == Half.TOP && pUseContext.getClickedFace() == Direction.DOWN) {
            return true;
        }
        return half == Half.BOTTOM && pUseContext.getClickedFace() == Direction.UP;
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

    private static boolean isOccluded(BlockState state, BlockState other, Direction pDirection) {
        state = (BlockState)state.setValue((Property)WATERLOGGED, (Comparable)Boolean.valueOf(false));
        other = (BlockState)other.setValue((Property)WATERLOGGED, (Comparable)Boolean.valueOf(false));
        Direction facing = (Direction)state.getValue(FACING);
        if (facing.getOpposite() == other.getValue(FACING) && pDirection == facing) {
            return true;
        }
        if (other.getValue(FACING) != facing) {
            return false;
        }
        return pDirection.getAxis() != facing.getAxis();
    }

    @Override
    public BlockState transform(BlockState state, StructureTransform transform) {
        if (transform.rotationAxis != null && transform.rotationAxis != Direction.Axis.Y && (transform.rotation == Rotation.CLOCKWISE_90 || transform.rotation == Rotation.COUNTERCLOCKWISE_90)) {
            transform.rotation = Rotation.NONE;
        }
        return BlockUtils.transformStepLikeHorizontal(state, transform, this.defaultBlockState());
    }

    public boolean isPathfindable(BlockState pState, PathComputationType pType) {
        return switch (pType) {
            case PathComputationType.LAND -> {
                if ((Integer)pState.getValue((Property)LAYERS) < 5 && ((Direction)pState.getValue(FACING)).equals((Object)Direction.UP)) {
                    yield true;
                }
                yield false;
            }
            default -> false;
        };
    }

    @NotNull
    public VoxelShape getShape(BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        return CCShapes.SLOPE_LAYER.get(pState.getValue(FACING)).get(pState.getValue(HALF)).get(pState.getValue((Property)LAYERS)).toShape();
    }

    @NotNull
    public VoxelShape getCollisionShape(BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return CCShapes.SLOPE_LAYER_COLLISION.get(state.getValue(FACING)).get(state.getValue(HALF)).get(state.getValue((Property)LAYERS)).toShape();
    }

    public boolean supportsExternalFaceHiding(BlockState state) {
        return true;
    }

    @Override
    public boolean hidesNeighborFace(BlockGetter level, BlockPos pos, BlockState state, BlockState neighborState, Direction dir) {
        return ICopycatBlock.hidesNeighborFace(level, pos, state, neighborState, dir);
    }

    public boolean useShapeForLightOcclusion(BlockState state) {
        return true;
    }

    public float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos) {
        return (Integer)state.getValue((Property)LAYERS) == 8 ? 0.2f : 1.0f;
    }
}

