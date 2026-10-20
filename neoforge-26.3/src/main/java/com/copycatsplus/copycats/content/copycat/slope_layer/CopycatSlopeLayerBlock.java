package com.copycatsplus.copycats.content.copycat.slope_layer;

import com.copycatsplus.copycats.CCShapes;
import net.minecraft.util.Prediction;
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
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
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
public class CopycatSlopeLayerBlock extends CCWaterloggedCopycatBlock implements SpecialBlockItemRequirement, IStateType {
    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<Half> HALF = BlockStateProperties.HALF;
    public static final IntegerProperty LAYERS = BlockStateProperties.LAYERS;

    public CopycatSlopeLayerBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH).setValue(HALF, Half.BOTTOM).setValue(LAYERS, 1));
    }

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder.add(FACING, HALF, LAYERS));
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
                    Copycats.LOGGER.warn("Can't figure out where to place a slope layer! Please file an issue if you see this.");
                    return state;
                }
            } else {
                Half half = context.getClickedFace() == Direction.DOWN
                    ? Half.TOP
                    : (
                        context.getClickedFace() == Direction.UP
                            ? Half.BOTTOM
                            : (context.getClickLocation().y - context.getClickedPos().getY() > 0.5 ? Half.TOP : Half.BOTTOM)
                    );
                return stateForPlacement.setValue(FACING, context.getHorizontalDirection()).setValue(HALF, half);
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
            Direction facing = pState.getValue(FACING);
            if (pUseContext.getClickedFace() == facing.getOpposite()) {
                return true;
            } else {
                return half == Half.TOP && pUseContext.getClickedFace() == Direction.DOWN
                    ? true
                    : half == Half.BOTTOM && pUseContext.getClickedFace() == Direction.UP;
            }
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

    private static boolean isOccluded(BlockState state, BlockState other, Direction pDirection) {
        state = state.setValue(WATERLOGGED, false);
        other = other.setValue(WATERLOGGED, false);
        Direction facing = state.getValue(FACING);
        if (facing.getOpposite() == other.getValue(FACING) && pDirection == facing) {
            return true;
        } else {
            return other.getValue(FACING) != facing ? false : pDirection.getAxis() != facing.getAxis();
        }
    }

    @Override
    public BlockState transform(BlockState state, StructureTransform transform) {
        if (transform.rotationAxis != null
            && transform.rotationAxis != Axis.Y
            && (transform.rotation == Rotation.CLOCKWISE_90 || transform.rotation == Rotation.COUNTERCLOCKWISE_90)) {
            transform.rotation = Rotation.NONE;
        }

        return BlockUtils.transformStepLikeHorizontal(state, transform, this.defaultBlockState());
    }

    @Override
    public boolean isPathfindable(BlockState pState, PathComputationType pType) {
        return switch (pType) {
            case LAND -> pState.getValue(LAYERS) < 5 && pState.getValue(FACING).equals(Direction.UP);
            default -> false;
        };
    }

    @NotNull
    @Override
    public VoxelShape getShape(BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        return CCShapes.SLOPE_LAYER.get(pState.getValue(FACING)).get(pState.getValue(HALF)).get(pState.getValue(LAYERS)).toShape();
    }

    @NotNull
    @Override
    public VoxelShape getCollisionShape(BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return CCShapes.SLOPE_LAYER_COLLISION.get(state.getValue(FACING)).get(state.getValue(HALF)).get(state.getValue(LAYERS)).toShape();
    }

    public boolean supportsExternalFaceHiding(BlockState state) {
        return true;
    }

    public boolean hidesNeighborFace(BlockGetter level, BlockPos pos, BlockState state, BlockState neighborState, Direction dir) {
        return ICopycatBlock.hidesNeighborFace(level, pos, state, neighborState, dir);
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState state) {
        return true;
    }

    @Override
    public float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos) {
        return state.getValue(LAYERS) == 8 ? 0.2F : 1.0F;
    }
}
