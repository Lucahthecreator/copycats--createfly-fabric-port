package com.copycatsplus.copycats.content.copycat.slice;

import com.copycatsplus.copycats.CCBlocks;
import net.minecraft.util.Prediction;
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
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

@ParametersAreNonnullByDefault
public class CopycatSliceBlock extends CCWaterloggedCopycatBlock implements SpecialBlockItemRequirement, IStateType {
    public static final EnumProperty<Half> HALF = BlockStateProperties.HALF;
    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final IntegerProperty LAYERS = BlockStateProperties.LAYERS;

    public CopycatSliceBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.defaultBlockState().setValue(HALF, Half.BOTTOM).setValue(FACING, Direction.SOUTH).setValue(LAYERS, 1));
    }

    @Override
    public boolean isPathfindable(BlockState pState, PathComputationType pType) {
        return switch (pType) {
            case LAND -> pState.getValue(LAYERS) < 5;
            default -> false;
        };
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState stateForPlacement = super.getStateForPlacement(context);
        if (stateForPlacement == null) {
            return null;
        } else {
            BlockPos blockPos = context.getClickedPos();
            BlockState state = context.getLevel().getBlockState(blockPos);
            if (state.is(this)) {
                if (state.getValue(LAYERS) < 8) {
                    return state.cycle(LAYERS);
                } else {
                    Copycats.LOGGER.warn("Can't figure out where to place a slice! Please file an issue if you see this.");
                    return state;
                }
            } else {
                Direction facing = context.getHorizontalDirection();
                if (facing.getAxis() == Axis.X) {
                    if (facing.getAxisDirection() == AxisDirection.POSITIVE != context.getClickLocation().x - context.getClickedPos().getX() > 0.5) {
                        facing = facing.getOpposite();
                    }
                } else if (facing.getAxisDirection() == AxisDirection.POSITIVE != context.getClickLocation().z - context.getClickedPos().getZ() > 0.5) {
                    facing = facing.getOpposite();
                }

                stateForPlacement = stateForPlacement.setValue(FACING, facing);
                Direction direction = context.getClickedFace();
                if (direction == Direction.UP) {
                    return stateForPlacement;
                } else {
                    return direction != Direction.DOWN && !(context.getClickLocation().y - context.getClickedPos().getY() > 0.5)
                        ? stateForPlacement
                        : stateForPlacement.setValue(HALF, Half.TOP);
                }
            }
        }
    }

    @Override
    public boolean canBeReplaced(@NotNull BlockState pState, BlockPlaceContext pUseContext) {
        ItemStack itemstack = pUseContext.getItemInHand();
        if (!itemstack.is(this.asItem())) {
            return false;
        } else if (pState.getValue(LAYERS) == 8) {
            return false;
        } else {
            Half half = pState.getValue(HALF);
            return (half != Half.TOP || pUseContext.getClickedFace() != Direction.DOWN)
                    && (half != Half.BOTTOM || pUseContext.getClickedFace() != Direction.UP)
                ? pUseContext.getClickedFace() == pState.getValue(FACING).getOpposite()
                : true;
        }
    }

    @Override
    public InteractionResult onSneakWrenched(BlockState state, UseOnContext context) {
        if (state.getValue(LAYERS) <= 1) {
            return super.onSneakWrenched(state, context);
        } else {
            Level world = context.getLevel();
            BlockPos pos = context.getClickedPos();
            Player player = context.getPlayer();
            if (world instanceof ServerLevel serverLevel) {
                if (player != null && !player.isCreative()) {
                    for (ItemStack drop : Block.getDrops(
                        state.setValue(LAYERS, 1), serverLevel, pos, world.getBlockEntity(pos), player, context.getItemInHand()
                    )) {
                        player.getInventory().placeItemBackInInventory(drop, Prediction.SERVER_ONLY);
                    }
                }

                BlockPos up = pos.relative(Direction.UP);
                world.setBlockAndUpdate(pos, state.setValue(LAYERS, state.getValue(LAYERS) - 1));
                IWrenchable.playRemoveSound(world, pos);
            }

            return InteractionResult.SUCCESS;
        }
    }

    @Override
    public ItemRequirement getRequiredItems(BlockState state, BlockEntity blockEntity) {
        return ICopycatBlock.getRequiredItemsForLayer(state, LAYERS);
    }

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder.add(HALF, FACING, LAYERS));
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return CCShapes.SLICE.get(pState.getValue(FACING)).get(pState.getValue(HALF)).get(pState.getValue(LAYERS)).toShape();
    }

    public boolean supportsExternalFaceHiding(BlockState state) {
        return true;
    }

    public boolean hidesNeighborFace(BlockGetter level, BlockPos pos, BlockState state, BlockState neighborState, Direction dir) {
        return ICopycatBlock.hidesNeighborFace(level, pos, state, neighborState, dir);
    }

    @Override
    public BlockState transform(BlockState state, StructureTransform transform) {
        return BlockUtils.transformStepLikeHorizontal(state, transform, CCBlocks.COPYCAT_VERTICAL_SLICE.getDefaultState());
    }
}
