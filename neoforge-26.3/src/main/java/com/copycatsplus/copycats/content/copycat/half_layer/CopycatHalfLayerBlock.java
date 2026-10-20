package com.copycatsplus.copycats.content.copycat.half_layer;

import com.copycatsplus.copycats.CCBlocks;
import net.minecraft.util.Prediction;
import com.copycatsplus.copycats.CCShapes;
import com.copycatsplus.copycats.Copycats;
import com.copycatsplus.copycats.content.copycat.stacked_half_layer.CopycatStackedHalfLayerBlock;
import com.copycatsplus.copycats.content.copycat.vertical_half_layer.CopycatVerticalHalfLayerBlock;
import com.copycatsplus.copycats.foundation.copycat.CopycatTransformableState;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlockEntity;
import com.copycatsplus.copycats.foundation.copycat.multistate.MaterialItemStorage;
import com.copycatsplus.copycats.foundation.copycat.multistate.WaterloggedMultiStateCopycatBlock;
import com.copycatsplus.copycats.utility.BlockFaceUtils;
import com.copycatsplus.copycats.utility.BlockUtils;
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
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

@ParametersAreNonnullByDefault
public class CopycatHalfLayerBlock extends WaterloggedMultiStateCopycatBlock implements SpecialBlockItemRequirement {
    public static final EnumProperty<Axis> AXIS = BlockStateProperties.HORIZONTAL_AXIS;
    public static final EnumProperty<Half> HALF = BlockStateProperties.HALF;
    public static final IntegerProperty POSITIVE_LAYERS = IntegerProperty.create("positive_layers", 0, 8);
    public static final IntegerProperty NEGATIVE_LAYERS = IntegerProperty.create("negative_layers", 0, 8);
    private final Function<BlockState, VoxelShape> shapesCache;
    private final ImmutableMap<CopycatHalfLayerBlock.FaceData, VoxelShape> partialFaceCache;

    public CopycatHalfLayerBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(
            this.defaultBlockState().setValue(AXIS, Axis.X).setValue(HALF, Half.BOTTOM).setValue(POSITIVE_LAYERS, 0).setValue(NEGATIVE_LAYERS, 0)
        );
        this.shapesCache = this.getShapeForEachState(CopycatHalfLayerBlock::calculateMultiFaceShape);
        Builder<CopycatHalfLayerBlock.FaceData, VoxelShape> builder = ImmutableMap.builder();

        for (String property : this.storageProperties()) {
            for (Axis axis : AXIS.getPossibleValues()) {
                for (Half half : HALF.getPossibleValues()) {
                    for (int layers : POSITIVE_LAYERS.getPossibleValues()) {
                        for (Direction face : Direction.values()) {
                            builder.put(
                                new CopycatHalfLayerBlock.FaceData(property, axis, half, layers, face),
                                BlockFaceUtils.getPartialFaceShape(
                                    null,
                                    this.defaultBlockState()
                                        .setValue(AXIS, axis)
                                        .setValue(HALF, half)
                                        .setValue(NEGATIVE_LAYERS, layers)
                                        .setValue(POSITIVE_LAYERS, layers),
                                    property,
                                    face
                                )
                            );
                        }
                    }
                }
            }
        }

        this.partialFaceCache = builder.build();
    }

    @Override
    public String defaultProperty() {
        return NEGATIVE_LAYERS.getName();
    }

    @Override
    public Vec3i vectorScale(BlockState state) {
        return switch ((Axis)state.getValue(AXIS)) {
            case X -> new Vec3i(2, 1, 1);
            case Y -> new Vec3i(1, 2, 1);
            case Z -> new Vec3i(1, 1, 2);
        };
    }

    @Override
    public boolean partExists(BlockState state, String property) {
        if (property.equals(POSITIVE_LAYERS.getName())) {
            return state.getValue(POSITIVE_LAYERS) > 0;
        } else {
            return property.equals(NEGATIVE_LAYERS.getName()) ? state.getValue(NEGATIVE_LAYERS) > 0 : false;
        }
    }

    @Override
    public Set<String> storageProperties() {
        return Set.of(POSITIVE_LAYERS.getName(), NEGATIVE_LAYERS.getName());
    }

    @Override
    public int getColorIndex(String property) {
        return property.equals(POSITIVE_LAYERS.getName()) ? 1 : 0;
    }

    @Override
    public String getPropertyFromInteraction(BlockState state, BlockGetter level, Vec3i hitLocation, BlockPos blockPos, Direction facing, Vec3 unscaledHit) {
        return hitLocation.get(state.getValue(AXIS)) > 0 ? POSITIVE_LAYERS.getName() : NEGATIVE_LAYERS.getName();
    }

    @Override
    public Vec3i getVectorFromProperty(BlockState state, String property) {
        return switch ((Axis)state.getValue(AXIS)) {
            case X -> property.equals(POSITIVE_LAYERS.getName()) ? new Vec3i(1, 0, 0) : new Vec3i(0, 0, 0);
            case Y -> property.equals(POSITIVE_LAYERS.getName()) ? new Vec3i(0, 1, 0) : new Vec3i(0, 0, 0);
            case Z -> property.equals(POSITIVE_LAYERS.getName()) ? new Vec3i(0, 0, 1) : new Vec3i(0, 0, 0);
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
                Vec3 clickPosition = context.getClickLocation()
                    .add(Vec3.atLowerCornerOf(context.getClickedFace().getUnitVec3i()).scale(0.0625))
                    .subtract(Vec3.atLowerCornerOf(context.getClickedPos()));
                IntegerProperty targetProp;
                if (state.getValue(AXIS).choose(clickPosition.x, clickPosition.y, clickPosition.z) < 0.5) {
                    targetProp = NEGATIVE_LAYERS;
                } else {
                    targetProp = POSITIVE_LAYERS;
                }

                if (state.getValue(targetProp) < 8) {
                    return state.cycle(targetProp);
                } else {
                    Copycats.LOGGER.warn("Can't figure out where to place a half layer! Please file an issue if you see this.");
                    return state;
                }
            } else {
                Axis axis = context.getHorizontalDirection().getAxis();
                Vec3 clickPositionx = context.getClickLocation().subtract(Vec3.atLowerCornerOf(context.getClickedPos()));
                return stateForPlacement.setValue(AXIS, axis)
                    .setValue(HALF, clickPositionx.y > 0.5 ? Half.TOP : Half.BOTTOM)
                    .setValue(axis.choose(clickPositionx.x, clickPositionx.y, clickPositionx.z) > 0.5 ? POSITIVE_LAYERS : NEGATIVE_LAYERS, 1);
            }
        }
    }

    @Override
    public boolean canBeReplaced(@NotNull BlockState pState, BlockPlaceContext pUseContext) {
        ItemStack itemstack = pUseContext.getItemInHand();
        if (!itemstack.is(this.asItem())) {
            return false;
        } else {
            Direction clickFace = pUseContext.getClickedFace();
            if (clickFace.getAxis().isVertical() && pState.getValue(HALF) == Half.TOP == (clickFace == Direction.UP)) {
                return false;
            } else {
                Vec3 clickPosition = pUseContext.getClickLocation()
                    .add(Vec3.atLowerCornerOf(pUseContext.getClickedFace().getUnitVec3i()).scale(0.0625))
                    .subtract(Vec3.atLowerCornerOf(pUseContext.getClickedPos()));
                IntegerProperty targetProp;
                if (pState.getValue(AXIS).choose(clickPosition.x, clickPosition.y, clickPosition.z) < 0.5) {
                    targetProp = NEGATIVE_LAYERS;
                } else {
                    targetProp = POSITIVE_LAYERS;
                }

                return pState.getValue(targetProp) != 8;
            }
        }
    }

    @Override
    public InteractionResult onSneakWrenched(BlockState state, UseOnContext context) {
        if (state.getValue(POSITIVE_LAYERS) + state.getValue(NEGATIVE_LAYERS) <= 1) {
            return super.onSneakWrenched(state, context);
        } else {
            Level world = context.getLevel();
            BlockPos pos = context.getClickedPos();
            Player player = context.getPlayer();
            Vec3 clickPosition = context.getClickLocation()
                .subtract(Vec3.atLowerCornerOf(context.getClickedFace().getUnitVec3i()).scale(0.0625))
                .subtract(Vec3.atLowerCornerOf(pos));
            IntegerProperty targetProp;
            if (state.getValue(AXIS).choose(clickPosition.x, clickPosition.y, clickPosition.z) < 0.5) {
                targetProp = NEGATIVE_LAYERS;
            } else {
                targetProp = POSITIVE_LAYERS;
            }

            if (state.getValue(targetProp) == 1) {
                this.onWrenched(state, context);
            }

            if (world instanceof ServerLevel serverLevel) {
                if (player != null) {
                    List<ItemStack> drops = Block.getDrops(
                        state.setValue(POSITIVE_LAYERS, 0).setValue(NEGATIVE_LAYERS, 0).setValue(targetProp, 1),
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
        return ICopycatBlock.getRequiredItemsForLayer(state, POSITIVE_LAYERS).union(ICopycatBlock.getRequiredItemsForLayer(state, NEGATIVE_LAYERS));
    }

    public static CopycatTransformableState<Integer> toTransformableState(BlockState state) {
        if (state.is(CCBlocks.COPYCAT_HALF_LAYER.get())) {
            Axis axis = state.getValue(AXIS);
            Half half = state.getValue(HALF);
            return CopycatTransformableState.create(t -> {
                t.addPart(axis == Axis.X ? 12 : 8, half == Half.BOTTOM ? 0 : 16, axis == Axis.Z ? 12 : 8).setData(state.getValue(POSITIVE_LAYERS));
                t.addPart(axis == Axis.X ? 4 : 8, half == Half.BOTTOM ? 0 : 16, axis == Axis.Z ? 4 : 8).setData(state.getValue(NEGATIVE_LAYERS));
            });
        } else if (state.is(CCBlocks.COPYCAT_VERTICAL_HALF_LAYER.get())) {
            Direction facing = state.getValue(CopycatVerticalHalfLayerBlock.FACING);
            return CopycatTransformableState.create(t -> {
                t.addPart(12, 8, 16).rotateY(facing).setData(state.getValue(POSITIVE_LAYERS));
                t.addPart(4, 8, 16).rotateY(facing).setData(state.getValue(NEGATIVE_LAYERS));
            });
        } else if (state.is(CCBlocks.COPYCAT_STACKED_HALF_LAYER.get())) {
            Direction facing = state.getValue(CopycatStackedHalfLayerBlock.FACING);
            return CopycatTransformableState.create(t -> {
                t.addPart(8, 12, 16).rotateY(facing).setData(state.getValue(POSITIVE_LAYERS));
                t.addPart(8, 4, 16).rotateY(facing).setData(state.getValue(NEGATIVE_LAYERS));
            });
        } else {
            throw new IllegalArgumentException("Unsupported block state");
        }
    }

    public static CopycatTransformableState<MaterialItemStorage.MaterialItem> toTransformableStorage(BlockState state, IMultiStateCopycatBlockEntity be) {
        if (state.is(CCBlocks.COPYCAT_HALF_LAYER.get())) {
            Axis axis = state.getValue(AXIS);
            Half half = state.getValue(HALF);
            return CopycatTransformableState.create(
                t -> {
                    t.addPart(axis == Axis.X ? 12 : 8, half == Half.BOTTOM ? 0 : 16, axis == Axis.Z ? 12 : 8)
                        .setData(be.getMaterialItemStorage().getMaterialItem(POSITIVE_LAYERS.getName()));
                    t.addPart(axis == Axis.X ? 4 : 8, half == Half.BOTTOM ? 0 : 16, axis == Axis.Z ? 4 : 8)
                        .setData(be.getMaterialItemStorage().getMaterialItem(NEGATIVE_LAYERS.getName()));
                }
            );
        } else if (state.is(CCBlocks.COPYCAT_VERTICAL_HALF_LAYER.get())) {
            Direction facing = state.getValue(CopycatVerticalHalfLayerBlock.FACING);
            return CopycatTransformableState.create(t -> {
                t.addPart(12, 8, 16).rotateY(facing).setData(be.getMaterialItemStorage().getMaterialItem(POSITIVE_LAYERS.getName()));
                t.addPart(4, 8, 16).rotateY(facing).setData(be.getMaterialItemStorage().getMaterialItem(NEGATIVE_LAYERS.getName()));
            });
        } else if (state.is(CCBlocks.COPYCAT_STACKED_HALF_LAYER.get())) {
            Direction facing = state.getValue(CopycatStackedHalfLayerBlock.FACING);
            return CopycatTransformableState.create(t -> {
                t.addPart(8, 12, 16).rotateY(facing).setData(be.getMaterialItemStorage().getMaterialItem(POSITIVE_LAYERS.getName()));
                t.addPart(8, 4, 16).rotateY(facing).setData(be.getMaterialItemStorage().getMaterialItem(NEGATIVE_LAYERS.getName()));
            });
        } else {
            throw new IllegalArgumentException("Unsupported block state");
        }
    }

    public static BlockState fromTransformableState(BlockState state, CopycatTransformableState<Integer> transformableState) {
        CopycatTransformableState.Part<Integer> firstPart = transformableState.parts.get(0);
        Direction facing = firstPart.getFacing();
        if (facing.getAxis().isVertical()) {
            Axis axis = firstPart.getHorizontalFacing().getAxis();
            Half half = firstPart.isTop() ? Half.TOP : Half.BOTTOM;
            state = BlockUtils.tryCopyProperties(state, CCBlocks.COPYCAT_HALF_LAYER.getDefaultState()).setValue(AXIS, axis).setValue(HALF, half);

            for (CopycatTransformableState.Part<Integer> part : transformableState.parts) {
                boolean positive = part.vector.get(axis) > 8;
                state = state.setValue(positive ? POSITIVE_LAYERS : NEGATIVE_LAYERS, part.data);
            }
        } else if (firstPart.vector.getY() == 8) {
            state = BlockUtils.tryCopyProperties(state, CCBlocks.COPYCAT_VERTICAL_HALF_LAYER.getDefaultState())
                .setValue(CopycatVerticalHalfLayerBlock.FACING, facing);

            for (CopycatTransformableState.Part<Integer> part : transformableState.parts) {
                boolean right = part.isRight(facing);
                state = state.setValue(right ? NEGATIVE_LAYERS : POSITIVE_LAYERS, part.data);
            }
        } else {
            state = BlockUtils.tryCopyProperties(state, CCBlocks.COPYCAT_STACKED_HALF_LAYER.getDefaultState())
                .setValue(CopycatStackedHalfLayerBlock.FACING, facing);

            for (CopycatTransformableState.Part<Integer> part : transformableState.parts) {
                boolean top = part.isTop();
                state = state.setValue(top ? POSITIVE_LAYERS : NEGATIVE_LAYERS, part.data);
            }
        }

        return state;
    }

    public static void fromTransformableStorage(
        BlockState state, IMultiStateCopycatBlockEntity be, CopycatTransformableState<MaterialItemStorage.MaterialItem> transformableState
    ) {
        CopycatTransformableState.Part<MaterialItemStorage.MaterialItem> firstPart = transformableState.parts.get(0);
        Direction facing = firstPart.getFacing();
        if (facing.getAxis().isVertical()) {
            Axis axis = firstPart.getHorizontalFacing().getAxis();

            for (CopycatTransformableState.Part<MaterialItemStorage.MaterialItem> part : transformableState.parts) {
                boolean positive = part.vector.get(axis) > 8;
                be.getMaterialItemStorage().storeMaterialItem(positive ? POSITIVE_LAYERS.getName() : NEGATIVE_LAYERS.getName(), part.data);
            }
        } else if (firstPart.vector.getY() == 8) {
            for (CopycatTransformableState.Part<MaterialItemStorage.MaterialItem> part : transformableState.parts) {
                boolean right = part.isRight(facing);
                be.getMaterialItemStorage().storeMaterialItem(right ? NEGATIVE_LAYERS.getName() : POSITIVE_LAYERS.getName(), part.data);
            }
        } else {
            for (CopycatTransformableState.Part<MaterialItemStorage.MaterialItem> part : transformableState.parts) {
                boolean top = part.isTop();
                be.getMaterialItemStorage().storeMaterialItem(top ? POSITIVE_LAYERS.getName() : NEGATIVE_LAYERS.getName(), part.data);
            }
        }
    }

    @Override
    public BlockState transform(BlockState state, StructureTransform transform) {
        return fromTransformableState(state, toTransformableState(state).transform(transform));
    }

    @Override
    public void transformStorage(BlockState state, IMultiStateCopycatBlockEntity be, StructureTransform transform) {
        state = fromTransformableState(state, toTransformableState(state).untransform(transform));
        fromTransformableStorage(state, be, toTransformableStorage(state, be).transform(transform));
    }

    @Override
    public boolean isPathfindable(BlockState pState, PathComputationType pType) {
        return false;
    }

    @Override
    protected void createBlockStateDefinition(net.minecraft.world.level.block.state.StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder.add(AXIS, HALF, POSITIVE_LAYERS, NEGATIVE_LAYERS));
    }

    private static VoxelShape calculateMultiFaceShape(BlockState pState) {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.joinUnoptimized(
            shape,
            CCShapes.HALF_LAYER_BOTTOM.get(pState.getValue(AXIS)).get(pState.getValue(HALF)).get(pState.getValue(NEGATIVE_LAYERS)).toShape(),
            BooleanOp.OR
        );
        shape = Shapes.joinUnoptimized(
            shape, CCShapes.HALF_LAYER_TOP.get(pState.getValue(AXIS)).get(pState.getValue(HALF)).get(pState.getValue(POSITIVE_LAYERS)).toShape(), BooleanOp.OR
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
                        new CopycatHalfLayerBlock.FaceData(
                            property,
                            state.getValue(AXIS),
                            state.getValue(HALF),
                            state.getValue(property.equals(NEGATIVE_LAYERS.getName()) ? NEGATIVE_LAYERS : POSITIVE_LAYERS),
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

    private record FaceData(String property, Axis axis, Half half, int layers, Direction face) {
    }
}
