package com.copycatsplus.copycats.content.copycat.vertical_half_layer;

import com.copycatsplus.copycats.CCShapes;
import net.minecraft.util.Prediction;
import com.copycatsplus.copycats.Copycats;
import com.copycatsplus.copycats.content.copycat.half_layer.CopycatHalfLayerBlock;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlockEntity;
import com.copycatsplus.copycats.foundation.copycat.multistate.WaterloggedMultiStateCopycatBlock;
import com.copycatsplus.copycats.utility.BlockFaceUtils;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.zurrtum.create.api.schematic.requirement.SpecialBlockItemRequirement;
import com.zurrtum.create.content.contraptions.StructureTransform;
import com.zurrtum.create.content.equipment.wrench.IWrenchable;
import com.zurrtum.create.content.schematics.requirement.ItemRequirement;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
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
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

@ParametersAreNonnullByDefault
public class CopycatVerticalHalfLayerBlock extends WaterloggedMultiStateCopycatBlock implements SpecialBlockItemRequirement {
    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
    private final Function<BlockState, VoxelShape> shapesCache;
    private final ImmutableMap<CopycatVerticalHalfLayerBlock.FaceData, VoxelShape> partialFaceCache;

    public CopycatVerticalHalfLayerBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(
            this.defaultBlockState()
                .setValue(FACING, Direction.NORTH)
                .setValue(CopycatHalfLayerBlock.POSITIVE_LAYERS, 0)
                .setValue(CopycatHalfLayerBlock.NEGATIVE_LAYERS, 0)
        );
        this.shapesCache = this.getShapeForEachState(CopycatVerticalHalfLayerBlock::calculateMultiFaceShape);
        Builder<CopycatVerticalHalfLayerBlock.FaceData, VoxelShape> builder = ImmutableMap.builder();

        for (String property : this.storageProperties()) {
            for (Direction facing : FACING.getPossibleValues()) {
                for (int layers : CopycatHalfLayerBlock.POSITIVE_LAYERS.getPossibleValues()) {
                    for (Direction face : Direction.values()) {
                        builder.put(
                            new CopycatVerticalHalfLayerBlock.FaceData(property, facing, layers, face),
                            BlockFaceUtils.getPartialFaceShape(
                                null,
                                this.defaultBlockState()
                                    .setValue(FACING, facing)
                                    .setValue(CopycatHalfLayerBlock.NEGATIVE_LAYERS, layers)
                                    .setValue(CopycatHalfLayerBlock.POSITIVE_LAYERS, layers),
                                property,
                                face
                            )
                        );
                    }
                }
            }
        }

        this.partialFaceCache = builder.build();
    }

    @Override
    public String defaultProperty() {
        return CopycatHalfLayerBlock.POSITIVE_LAYERS.getName();
    }

    @Override
    public Vec3i vectorScale(BlockState state) {
        return switch (state.getValue(FACING).getAxis()) {
            case X -> new Vec3i(1, 1, 2);
            case Y -> new Vec3i(1, 2, 1);
            case Z -> new Vec3i(2, 1, 1);
        };
    }

    @Override
    public boolean partExists(BlockState state, String property) {
        if (property.equals(CopycatHalfLayerBlock.POSITIVE_LAYERS.getName())) {
            return state.getValue(CopycatHalfLayerBlock.POSITIVE_LAYERS) > 0;
        } else {
            return property.equals(CopycatHalfLayerBlock.NEGATIVE_LAYERS.getName()) ? state.getValue(CopycatHalfLayerBlock.NEGATIVE_LAYERS) > 0 : false;
        }
    }

    @Override
    public Set<String> storageProperties() {
        return Set.of(CopycatHalfLayerBlock.POSITIVE_LAYERS.getName(), CopycatHalfLayerBlock.NEGATIVE_LAYERS.getName());
    }

    @Override
    public int getColorIndex(String property) {
        return property.equals(CopycatHalfLayerBlock.POSITIVE_LAYERS.getName()) ? 1 : 0;
    }

    @Override
    public String getPropertyFromInteraction(BlockState state, BlockGetter level, Vec3i hitLocation, BlockPos blockPos, Direction facing, Vec3 unscaledHit) {
        return switch ((Direction)state.getValue(FACING)) {
            case NORTH -> hitLocation.getX() < 0.5 ? CopycatHalfLayerBlock.POSITIVE_LAYERS.getName() : CopycatHalfLayerBlock.NEGATIVE_LAYERS.getName();
            case SOUTH -> hitLocation.getX() < 0.5 ? CopycatHalfLayerBlock.NEGATIVE_LAYERS.getName() : CopycatHalfLayerBlock.POSITIVE_LAYERS.getName();
            case WEST -> hitLocation.getZ() < 0.5 ? CopycatHalfLayerBlock.NEGATIVE_LAYERS.getName() : CopycatHalfLayerBlock.POSITIVE_LAYERS.getName();
            case EAST -> hitLocation.getZ() < 0.5 ? CopycatHalfLayerBlock.POSITIVE_LAYERS.getName() : CopycatHalfLayerBlock.NEGATIVE_LAYERS.getName();
            default -> throw new RuntimeException("Invalid facing");
        };
    }

    @Override
    public Vec3i getVectorFromProperty(BlockState state, String property) {
        return switch ((Direction)state.getValue(FACING)) {
            case NORTH -> property.equals(CopycatHalfLayerBlock.POSITIVE_LAYERS.getName()) ? new Vec3i(0, 0, 0) : new Vec3i(1, 0, 0);
            case SOUTH -> property.equals(CopycatHalfLayerBlock.POSITIVE_LAYERS.getName()) ? new Vec3i(1, 0, 0) : new Vec3i(0, 0, 0);
            case WEST -> property.equals(CopycatHalfLayerBlock.POSITIVE_LAYERS.getName()) ? new Vec3i(0, 0, 1) : new Vec3i(0, 0, 0);
            case EAST -> property.equals(CopycatHalfLayerBlock.POSITIVE_LAYERS.getName()) ? new Vec3i(0, 0, 0) : new Vec3i(0, 0, 1);
            default -> throw new RuntimeException("Invalid facing");
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
                String property = this.getPropertyFromInteraction(
                    state, context.getLevel(), blockPos, context.getClickLocation(), context.getClickedFace(), false
                );
                IntegerProperty targetProp;
                if (property.equals(CopycatHalfLayerBlock.POSITIVE_LAYERS.getName())) {
                    targetProp = CopycatHalfLayerBlock.POSITIVE_LAYERS;
                } else {
                    targetProp = CopycatHalfLayerBlock.NEGATIVE_LAYERS;
                }

                if (state.getValue(targetProp) < 8) {
                    return state.cycle(targetProp);
                } else {
                    Copycats.LOGGER.warn("Can't figure out where to place a half layer! Please file an issue if you see this.");
                    return state;
                }
            } else {
                Direction facing = context.getClickedFace().getOpposite();
                if (facing.getAxis().isVertical()) {
                    facing = context.getHorizontalDirection();
                }

                Vec3 clickPosition = context.getClickLocation().subtract(Vec3.atLowerCornerOf(context.getClickedPos()));
                return facing.getAxis() == Axis.X
                    ? stateForPlacement.setValue(FACING, facing)
                        .setValue(
                            facing.getAxisDirection() == AxisDirection.NEGATIVE == clickPosition.z >= 0.5
                                ? CopycatHalfLayerBlock.POSITIVE_LAYERS
                                : CopycatHalfLayerBlock.NEGATIVE_LAYERS,
                            1
                        )
                    : stateForPlacement.setValue(FACING, facing)
                        .setValue(
                            facing.getAxisDirection() == AxisDirection.NEGATIVE == clickPosition.x < 0.5
                                ? CopycatHalfLayerBlock.POSITIVE_LAYERS
                                : CopycatHalfLayerBlock.NEGATIVE_LAYERS,
                            1
                        );
            }
        }
    }

    @Override
    public boolean canBeReplaced(@NotNull BlockState state, BlockPlaceContext context) {
        ItemStack itemstack = context.getItemInHand();
        if (!itemstack.is(this.asItem())) {
            return false;
        } else if (context.getClickedFace() == state.getValue(FACING)) {
            return false;
        } else {
            String property = this.getPropertyFromInteraction(
                state, context.getLevel(), context.getClickedPos(), context.getClickLocation(), context.getClickedFace(), false
            );
            IntegerProperty targetProp;
            if (property.equals(CopycatHalfLayerBlock.POSITIVE_LAYERS.getName())) {
                targetProp = CopycatHalfLayerBlock.POSITIVE_LAYERS;
            } else {
                targetProp = CopycatHalfLayerBlock.NEGATIVE_LAYERS;
            }

            return state.getValue(targetProp) != 8;
        }
    }

    @Override
    public InteractionResult onSneakWrenched(BlockState state, UseOnContext context) {
        if (state.getValue(CopycatHalfLayerBlock.POSITIVE_LAYERS) + state.getValue(CopycatHalfLayerBlock.NEGATIVE_LAYERS) <= 1) {
            return super.onSneakWrenched(state, context);
        } else {
            Level world = context.getLevel();
            BlockPos pos = context.getClickedPos();
            Player player = context.getPlayer();
            String property = this.getPropertyFromInteraction(
                state, context.getLevel(), context.getClickedPos(), context.getClickLocation(), context.getClickedFace(), true
            );
            IntegerProperty targetProp;
            if (property.equals(CopycatHalfLayerBlock.POSITIVE_LAYERS.getName())) {
                targetProp = CopycatHalfLayerBlock.POSITIVE_LAYERS;
            } else {
                targetProp = CopycatHalfLayerBlock.NEGATIVE_LAYERS;
            }

            if (state.getValue(targetProp) == 1) {
                this.onWrenched(state, context);
            }

            if (world instanceof ServerLevel serverLevel) {
                if (player != null) {
                    List<ItemStack> drops = Block.getDrops(
                        state.setValue(CopycatHalfLayerBlock.POSITIVE_LAYERS, 0).setValue(CopycatHalfLayerBlock.NEGATIVE_LAYERS, 0).setValue(targetProp, 1),
                        serverLevel,
                        pos,
                        world.getBlockEntity(pos),
                        player,
                        context.getItemInHand()
                    );
                    if (!player.isCreative()) {
                        for (ItemStack drop : drops) {
                            player.getInventory().placeItemBackInInventory(drop, Prediction.SERVER_ONLY);
                        }
                    }
                }

                BlockPos up = pos.relative(Direction.UP);
                world.setBlockAndUpdate(pos, state.setValue(targetProp, state.getValue(targetProp) - 1));
                IWrenchable.playRemoveSound(world, pos);
            }

            return InteractionResult.SUCCESS;
        }
    }

    @Override
    public ItemRequirement getRequiredItems(BlockState state, BlockEntity blockEntity) {
        return ICopycatBlock.getRequiredItemsForLayer(state, CopycatHalfLayerBlock.POSITIVE_LAYERS)
            .union(ICopycatBlock.getRequiredItemsForLayer(state, CopycatHalfLayerBlock.NEGATIVE_LAYERS));
    }

    @Override
    public BlockState transform(BlockState state, StructureTransform transform) {
        return CopycatHalfLayerBlock.fromTransformableState(state, CopycatHalfLayerBlock.toTransformableState(state).transform(transform));
    }

    @Override
    public void transformStorage(BlockState state, IMultiStateCopycatBlockEntity be, StructureTransform transform) {
        state = CopycatHalfLayerBlock.fromTransformableState(state, CopycatHalfLayerBlock.toTransformableState(state).untransform(transform));
        CopycatHalfLayerBlock.fromTransformableStorage(state, be, CopycatHalfLayerBlock.toTransformableStorage(state, be).transform(transform));
    }

    @Override
    public boolean isPathfindable(BlockState pState, PathComputationType pType) {
        return false;
    }

    @Override
    protected void createBlockStateDefinition(net.minecraft.world.level.block.state.StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder.add(FACING, CopycatHalfLayerBlock.POSITIVE_LAYERS, CopycatHalfLayerBlock.NEGATIVE_LAYERS));
    }

    private static VoxelShape calculateMultiFaceShape(BlockState pState) {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.joinUnoptimized(
            shape,
            CCShapes.VERTICAL_HALF_LAYER_LEFT.get(pState.getValue(FACING)).get(pState.getValue(CopycatHalfLayerBlock.POSITIVE_LAYERS)).toShape(),
            BooleanOp.OR
        );
        shape = Shapes.joinUnoptimized(
            shape,
            CCShapes.VERTICAL_HALF_LAYER_RIGHT.get(pState.getValue(FACING)).get(pState.getValue(CopycatHalfLayerBlock.NEGATIVE_LAYERS)).toShape(),
            BooleanOp.OR
        );
        return shape.optimize();
    }

    @NotNull
    @Override
    public VoxelShape getShape(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        return Objects.requireNonNull(this.shapesCache.apply(pState));
    }

    @Override
    public VoxelShape getPartialFaceShape(BlockGetter level, BlockState state, String property, Direction face) {
        return !this.partExists(state, property)
            ? Shapes.empty()
            : Objects.requireNonNull(
                (VoxelShape)this.partialFaceCache
                    .getOrDefault(
                        new CopycatVerticalHalfLayerBlock.FaceData(
                            property,
                            state.getValue(FACING),
                            state.getValue(
                                property.equals(CopycatHalfLayerBlock.NEGATIVE_LAYERS.getName())
                                    ? CopycatHalfLayerBlock.NEGATIVE_LAYERS
                                    : CopycatHalfLayerBlock.POSITIVE_LAYERS
                            ),
                            face
                        ),
                        Shapes.empty()
                    )
            );
    }

    public boolean supportsExternalFaceHiding(BlockState state) {
        return true;
    }

    public boolean hidesNeighborFace(BlockGetter level, BlockPos pos, BlockState state, BlockState neighborState, Direction dir) {
        return ICopycatBlock.hidesNeighborFace(level, pos, state, neighborState, dir);
    }

    private record FaceData(String property, Direction facing, int layers, Direction face) {
    }
}
