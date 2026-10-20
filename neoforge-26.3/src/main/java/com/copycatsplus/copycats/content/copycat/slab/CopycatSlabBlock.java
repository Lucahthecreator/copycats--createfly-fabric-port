package com.copycatsplus.copycats.content.copycat.slab;

import com.copycatsplus.copycats.CCBlocks;
import net.minecraft.util.Prediction;
import com.copycatsplus.copycats.CCShapes;
import com.copycatsplus.copycats.foundation.copycat.CopycatTransformableState;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlockEntity;
import com.copycatsplus.copycats.foundation.copycat.multistate.MaterialItemStorage;
import com.copycatsplus.copycats.foundation.copycat.multistate.WaterloggedMultiStateCopycatBlock;
import com.copycatsplus.copycats.utility.BlockFaceUtils;
import com.copycatsplus.copycats.utility.InteractionUtils;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.zurrtum.create.AllBlocks;
import com.zurrtum.create.api.schematic.requirement.SpecialBlockItemRequirement;
import com.zurrtum.create.catnip.placement.IPlacementHelper;
import com.zurrtum.create.catnip.placement.PlacementHelpers;
import com.zurrtum.create.catnip.placement.PlacementOffset;
import com.zurrtum.create.content.contraptions.StructureTransform;
import com.zurrtum.create.content.equipment.wrench.IWrenchable;
import com.zurrtum.create.content.schematics.requirement.ItemRequirement;
import com.zurrtum.create.content.schematics.requirement.ItemRequirement.ItemUseType;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
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
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

@ParametersAreNonnullByDefault
public class CopycatSlabBlock extends WaterloggedMultiStateCopycatBlock implements SpecialBlockItemRequirement {
    public static final EnumProperty<Axis> AXIS = BlockStateProperties.AXIS;
    public static final EnumProperty<SlabType> SLAB_TYPE = BlockStateProperties.SLAB_TYPE;
    private final ImmutableMap<CopycatSlabBlock.FaceData, VoxelShape> partialFaceCache;
    private static final int placementHelperId = PlacementHelpers.register(new CopycatSlabBlock.PlacementHelper());

    public CopycatSlabBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.defaultBlockState().setValue(AXIS, Axis.Y).setValue(SLAB_TYPE, SlabType.BOTTOM));
        Builder<CopycatSlabBlock.FaceData, VoxelShape> builder = ImmutableMap.builder();

        for (String property : this.storageProperties()) {
            for (Axis axis : AXIS.getPossibleValues()) {
                for (Direction face : Direction.values()) {
                    builder.put(
                        new CopycatSlabBlock.FaceData(property, axis, face),
                        BlockFaceUtils.getPartialFaceShape(
                            null, this.defaultBlockState().setValue(SLAB_TYPE, SlabType.DOUBLE).setValue(AXIS, axis), property, face
                        )
                    );
                }
            }
        }

        this.partialFaceCache = builder.build();
    }

    @Override
    public String defaultProperty() {
        return SlabType.TOP.getSerializedName();
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
    public String getPropertyFromInteraction(BlockState state, BlockGetter level, Vec3i hitLocation, BlockPos blockPos, Direction facing, Vec3 unscaledHit) {
        return hitLocation.get(state.getValue(AXIS)) > 0 ? SlabType.TOP.getSerializedName() : SlabType.BOTTOM.getSerializedName();
    }

    @Override
    public Vec3i getVectorFromProperty(BlockState state, String property) {
        return switch ((Axis)state.getValue(AXIS)) {
            case X -> property.equals(SlabType.TOP.getSerializedName()) ? new Vec3i(1, 0, 0) : new Vec3i(0, 0, 0);
            case Y -> property.equals(SlabType.TOP.getSerializedName()) ? new Vec3i(0, 1, 0) : new Vec3i(0, 0, 0);
            case Z -> property.equals(SlabType.TOP.getSerializedName()) ? new Vec3i(0, 0, 1) : new Vec3i(0, 0, 0);
        };
    }

    @Override
    public boolean partExists(BlockState state, String property) {
        SlabType slabType = state.getValue(SLAB_TYPE);
        if (property.equals(SlabType.BOTTOM.getSerializedName())) {
            return slabType == SlabType.DOUBLE || slabType == SlabType.BOTTOM;
        } else {
            return !property.equals(SlabType.TOP.getSerializedName()) ? false : slabType == SlabType.DOUBLE || slabType == SlabType.TOP;
        }
    }

    @Override
    public Set<String> storageProperties() {
        return Set.of(SlabType.TOP.getSerializedName(), SlabType.BOTTOM.getSerializedName());
    }

    @Override
    public int getColorIndex(String property) {
        return property.equals(SlabType.BOTTOM.getSerializedName()) ? 0 : 1;
    }

    @Override
    protected InteractionResult useItemOn(
        ItemStack heldStack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult ray
    ) {
        return InteractionUtils.sequential(
            () -> InteractionUtils.usePlacementHelper(placementHelperId, state, world, pos, player, hand, ray),
            () -> super.useItemOn(heldStack, state, world, pos, player, hand, ray)
        );
    }

    @Override
    public InteractionResult onSneakWrenched(BlockState state, UseOnContext context) {
        this.onWrenched(state, context);
        if (state.getValue(SLAB_TYPE) != SlabType.DOUBLE) {
            return super.onSneakWrenched(state, context);
        } else {
            Level world = context.getLevel();
            BlockPos pos = context.getClickedPos();
            Player player = context.getPlayer();
            String property = this.getPropertyFromInteraction(
                state, context.getLevel(), context.getClickedPos(), context.getClickLocation(), context.getClickedFace(), true
            );
            if (!this.partExists(state, property)) {
                return InteractionResult.FAIL;
            } else {
                if (world instanceof ServerLevel) {
                    if (player != null) {
                        List<ItemStack> drops = Block.getDrops(
                            this.defaultBlockState().setValue(SLAB_TYPE, property.equals(SlabType.BOTTOM.getSerializedName()) ? SlabType.BOTTOM : SlabType.TOP),
                            (ServerLevel)world,
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
                    world.setBlockAndUpdate(
                        pos, state.setValue(SLAB_TYPE, property.equals(SlabType.BOTTOM.getSerializedName()) ? SlabType.TOP : SlabType.BOTTOM)
                    );
                    IWrenchable.playRemoveSound(world, pos);
                }

                return InteractionResult.SUCCESS;
            }
        }
    }

    @Override
    public ItemRequirement getRequiredItems(BlockState state, BlockEntity blockEntity) {
        return new ItemRequirement(ItemUseType.CONSUME, new ItemStack(this.asItem(), switch ((SlabType)state.getValue(SLAB_TYPE)) {
            case BOTTOM, TOP -> 1;
            case DOUBLE -> 2;
        }));
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
                return state.setValue(SLAB_TYPE, SlabType.DOUBLE).setValue(WATERLOGGED, false);
            } else {
                Axis axis = context.getNearestLookingDirection().getAxis();

                boolean flag = switch (axis) {
                    case X -> context.getClickLocation().x - blockPos.getX() > 0.5;
                    case Y -> context.getClickLocation().y - blockPos.getY() > 0.5;
                    case Z -> context.getClickLocation().z - blockPos.getZ() > 0.5;
                };
                Direction clickedFace = context.getClickedFace();
                return stateForPlacement.setValue(AXIS, axis)
                    .setValue(
                        SLAB_TYPE,
                        clickedFace != Direction.fromAxisAndDirection(axis, AxisDirection.POSITIVE) && (clickedFace.getAxis() == axis || flag)
                            ? SlabType.TOP
                            : SlabType.BOTTOM
                    );
            }
        }
    }

    @Override
    public boolean canBeReplaced(BlockState pState, BlockPlaceContext pUseContext) {
        ItemStack itemstack = pUseContext.getItemInHand();
        SlabType slabtype = pState.getValue(SLAB_TYPE);
        Axis axis = pState.getValue(AXIS);
        if (slabtype != SlabType.DOUBLE && itemstack.is(this.asItem())) {
            boolean flag = switch (axis) {
                case X -> pUseContext.getClickLocation().x - pUseContext.getClickedPos().getX() > 0.5;
                case Y -> pUseContext.getClickLocation().y - pUseContext.getClickedPos().getY() > 0.5;
                case Z -> pUseContext.getClickLocation().z - pUseContext.getClickedPos().getZ() > 0.5;
            };
            Direction direction = pUseContext.getClickedFace();
            return slabtype == SlabType.BOTTOM
                ? direction == Direction.fromAxisAndDirection(axis, AxisDirection.POSITIVE) || flag
                : direction == Direction.fromAxisAndDirection(axis, AxisDirection.NEGATIVE) || !flag;
        } else {
            return false;
        }
    }

    @Override
    protected void createBlockStateDefinition(net.minecraft.world.level.block.state.StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder.add(AXIS).add(SLAB_TYPE));
    }

    @Override
    public boolean isPathfindable(BlockState pState, PathComputationType pType) {
        return super.isPathfindable(pState, pType);
    }

    @NotNull
    @Override
    public VoxelShape getShape(BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        SlabType type = pState.getValue(SLAB_TYPE);
        Axis axis = pState.getValue(AXIS);
        if (type == SlabType.DOUBLE) {
            return Shapes.block();
        } else {
            return type == SlabType.BOTTOM ? CCShapes.SLAB_BOTTOM.get(axis).toShape() : CCShapes.SLAB_TOP.get(axis).toShape();
        }
    }

    @Override
    public VoxelShape getPartialFaceShape(BlockGetter level, BlockState state, String property, Direction face) {
        return !this.partExists(state, property)
            ? Shapes.empty()
            : Objects.requireNonNull(
                (VoxelShape)this.partialFaceCache.getOrDefault(new CopycatSlabBlock.FaceData(property, state.getValue(AXIS), face), Shapes.empty())
            );
    }

    public boolean supportsExternalFaceHiding(BlockState state) {
        return true;
    }

    public boolean hidesNeighborFace(BlockGetter level, BlockPos pos, BlockState state, BlockState neighborState, Direction dir) {
        return ICopycatBlock.hidesNeighborFace(level, pos, state, neighborState, dir);
    }

    public static CopycatTransformableState<Void> toTransformableState(BlockState state) {
        return CopycatTransformableState.create(
            t -> {
                Direction facing = getApparentDirection(state);
                Vec3i normal = facing.getUnitVec3i();
                Vec3i part = new Vec3i(
                    normal.getX() == 0 ? 8 : (normal.getX() > 0 ? 16 : 0),
                    normal.getY() == 0 ? 8 : (normal.getY() > 0 ? 16 : 0),
                    normal.getZ() == 0 ? 8 : (normal.getZ() > 0 ? 16 : 0)
                );
                t.addPart(part.getX(), part.getY(), part.getZ());
                if (state.getValue(SLAB_TYPE) == SlabType.DOUBLE) {
                    Vec3i part2 = new Vec3i(16, 16, 16).subtract(part);
                    t.addPart(part2.getX(), part2.getY(), part2.getZ());
                }
            }
        );
    }

    public static CopycatTransformableState<MaterialItemStorage.MaterialItem> toTransformableStorage(BlockState state, IMultiStateCopycatBlockEntity be) {
        return CopycatTransformableState.create(
            t -> {
                Direction facing = getApparentDirection(state);
                Vec3i normal = facing.getUnitVec3i();
                Vec3i part = new Vec3i(
                    normal.getX() == 0 ? 8 : (normal.getX() > 0 ? 16 : 0),
                    normal.getY() == 0 ? 8 : (normal.getY() > 0 ? 16 : 0),
                    normal.getZ() == 0 ? 8 : (normal.getZ() > 0 ? 16 : 0)
                );
                t.addPart(part.getX(), part.getY(), part.getZ())
                    .setData(
                        be.getMaterialItemStorage()
                            .getMaterialItem(
                                facing.getAxisDirection() == AxisDirection.POSITIVE ? Half.TOP.getSerializedName() : Half.BOTTOM.getSerializedName()
                            )
                    );
                if (state.getValue(SLAB_TYPE) == SlabType.DOUBLE) {
                    Vec3i part2 = new Vec3i(16, 16, 16).subtract(part);
                    t.addPart(part2.getX(), part2.getY(), part2.getZ())
                        .setData(
                            be.getMaterialItemStorage()
                                .getMaterialItem(
                                    facing.getAxisDirection() == AxisDirection.POSITIVE ? Half.BOTTOM.getSerializedName() : Half.TOP.getSerializedName()
                                )
                        );
                }
            }
        );
    }

    public static BlockState fromTransformableState(BlockState state, CopycatTransformableState<Void> transformableState) {
        CopycatTransformableState.Part<Void> part = transformableState.parts.get(0);
        SlabType type;
        Axis axis;
        if (part.vector.getX() != 8) {
            axis = Axis.X;
            type = part.vector.getX() > 8 ? SlabType.TOP : SlabType.BOTTOM;
        } else if (part.vector.getY() != 8) {
            axis = Axis.Y;
            type = part.vector.getY() > 8 ? SlabType.TOP : SlabType.BOTTOM;
        } else {
            axis = Axis.Z;
            type = part.vector.getZ() > 8 ? SlabType.TOP : SlabType.BOTTOM;
        }

        if (transformableState.parts.size() == 2) {
            type = SlabType.DOUBLE;
        }

        return state.setValue(AXIS, axis).setValue(SLAB_TYPE, type);
    }

    public static void fromTransformableStorage(
        BlockState state, IMultiStateCopycatBlockEntity be, CopycatTransformableState<MaterialItemStorage.MaterialItem> transformableState
    ) {
        for (String property : be.getMaterialItemStorage().getAllProperties()) {
            be.getMaterialItemStorage()
                .storeMaterialItem(property, new MaterialItemStorage.MaterialItem(AllBlocks.COPYCAT_BASE.defaultBlockState(), ItemStack.EMPTY));
        }

        for (CopycatTransformableState.Part<MaterialItemStorage.MaterialItem> part : transformableState.parts) {
            be.getMaterialItemStorage()
                .storeMaterialItem(
                    part.vector.getX() <= 8 && part.vector.getY() <= 8 && part.vector.getZ() <= 8
                        ? Half.BOTTOM.getSerializedName()
                        : Half.TOP.getSerializedName(),
                    part.data
                );
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

    public static CopycatSlabBlock.FaceShape getFaceShape(BlockState state, Direction face) {
        SlabType slab = state.getValue(SLAB_TYPE);
        if (state.getValue(AXIS) != face.getAxis()) {
            return CopycatSlabBlock.FaceShape.forSlabSide(slab);
        } else {
            return switch (slab) {
                case BOTTOM -> CopycatSlabBlock.FaceShape.fullOrNone(face.getAxisDirection() == AxisDirection.NEGATIVE);
                case TOP -> CopycatSlabBlock.FaceShape.fullOrNone(face.getAxisDirection() == AxisDirection.POSITIVE);
                case DOUBLE -> CopycatSlabBlock.FaceShape.FULL;
            };
        }
    }

    public static Direction getApparentDirection(BlockState state) {
        return Direction.fromAxisAndDirection(
            state.getValue(AXIS), state.getValue(SLAB_TYPE) == SlabType.BOTTOM ? AxisDirection.NEGATIVE : AxisDirection.POSITIVE
        );
    }

    public static BlockState setApparentDirection(BlockState state, Direction direction) {
        SlabType type = state.getValue(SLAB_TYPE);
        if (type == SlabType.DOUBLE) {
            return state.setValue(AXIS, direction.getAxis());
        } else {
            return getApparentDirection(state).getAxisDirection() != direction.getAxisDirection()
                ? state.setValue(AXIS, direction.getAxis()).setValue(SLAB_TYPE, type == SlabType.BOTTOM ? SlabType.TOP : SlabType.BOTTOM)
                : state.setValue(AXIS, direction.getAxis());
        }
    }

    private record FaceData(String property, Axis axis, Direction face) {
    }

    public static enum FaceShape {
        FULL,
        TOP,
        BOTTOM,
        NONE;

        public static CopycatSlabBlock.FaceShape forSlabSide(SlabType type) {
            return switch (type) {
                case BOTTOM -> BOTTOM;
                case TOP -> TOP;
                case DOUBLE -> FULL;
            };
        }

        public static CopycatSlabBlock.FaceShape fullOrNone(boolean value) {
            return value ? FULL : NONE;
        }

        public static boolean canConnect(CopycatSlabBlock.FaceShape shape1, CopycatSlabBlock.FaceShape shape2) {
            return shape1 == shape2 || shape1 == FULL && shape2 != NONE || shape2 == FULL && shape1 != NONE;
        }

        public boolean hasContact() {
            return this != NONE;
        }
    }

    private static class PlacementHelper implements IPlacementHelper {
        @Override
        public Predicate<ItemStack> getItemPredicate() {
            return stack -> stack.is(CCBlocks.COPYCAT_SLAB.get().asItem());
        }

        @Override
        public Predicate<BlockState> getStatePredicate() {
            return state -> state.getBlock() instanceof CopycatSlabBlock;
        }

        @Override
        public PlacementOffset getOffset(Player player, Level world, BlockState state, BlockPos pos, BlockHitResult ray) {
            List<Direction> directions = IPlacementHelper.orderedByDistanceExceptAxis(
                pos, ray.getLocation(), state.getValue(CopycatSlabBlock.AXIS), dir -> world.getBlockState(pos.relative(dir)).canBeReplaced()
            );
            if (directions.isEmpty()) {
                return PlacementOffset.fail();
            } else {
                return state.getValue(CopycatSlabBlock.SLAB_TYPE).equals(SlabType.DOUBLE)
                    ? PlacementOffset.success(
                        pos.relative(directions.get(0)),
                        s -> s.setValue(CopycatSlabBlock.AXIS, state.getValue(CopycatSlabBlock.AXIS)).setValue(CopycatSlabBlock.SLAB_TYPE, SlabType.BOTTOM)
                    )
                    : PlacementOffset.success(
                        pos.relative(directions.get(0)),
                        s -> s.setValue(CopycatSlabBlock.AXIS, state.getValue(CopycatSlabBlock.AXIS))
                            .setValue(CopycatSlabBlock.SLAB_TYPE, state.getValue(CopycatSlabBlock.SLAB_TYPE))
                    );
            }
        }
    }
}
