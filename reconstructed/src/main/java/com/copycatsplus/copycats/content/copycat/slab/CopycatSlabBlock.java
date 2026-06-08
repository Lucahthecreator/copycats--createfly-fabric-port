/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap
 *  com.google.common.collect.ImmutableMap$Builder
 *  com.zurrtum.create.AllBlocks
 *  com.zurrtum.create.api.schematic.requirement.SpecialBlockItemRequirement
 *  com.zurrtum.create.catnip.placement.IPlacementHelper
 *  com.zurrtum.create.catnip.placement.PlacementHelpers
 *  com.zurrtum.create.catnip.placement.PlacementOffset
 *  com.zurrtum.create.content.contraptions.StructureTransform
 *  com.zurrtum.create.content.equipment.wrench.IWrenchable
 *  com.zurrtum.create.content.schematics.requirement.ItemRequirement
 *  com.zurrtum.create.content.schematics.requirement.ItemRequirement$ItemUseType
 *  javax.annotation.ParametersAreNonnullByDefault
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.core.Direction$Axis
 *  net.minecraft.core.Direction$AxisDirection
 *  net.minecraft.core.Vec3i
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.InteractionResult
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemInstance
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.context.BlockPlaceContext
 *  net.minecraft.world.item.context.UseOnContext
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.state.BlockBehaviour$Properties
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.StateDefinition$Builder
 *  net.minecraft.world.level.block.state.properties.BlockStateProperties
 *  net.minecraft.world.level.block.state.properties.EnumProperty
 *  net.minecraft.world.level.block.state.properties.Half
 *  net.minecraft.world.level.block.state.properties.Property
 *  net.minecraft.world.level.block.state.properties.SlabType
 *  net.minecraft.world.level.pathfinder.PathComputationType
 *  net.minecraft.world.phys.BlockHitResult
 *  net.minecraft.world.phys.Vec3
 *  net.minecraft.world.phys.shapes.CollisionContext
 *  net.minecraft.world.phys.shapes.Shapes
 *  net.minecraft.world.phys.shapes.VoxelShape
 *  org.jetbrains.annotations.NotNull
 */
package com.copycatsplus.copycats.content.copycat.slab;

import com.copycatsplus.copycats.CCBlocks;
import com.copycatsplus.copycats.CCShapes;
import com.copycatsplus.copycats.foundation.copycat.CopycatTransformableState;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlockEntity;
import com.copycatsplus.copycats.foundation.copycat.multistate.MaterialItemStorage;
import com.copycatsplus.copycats.foundation.copycat.multistate.WaterloggedMultiStateCopycatBlock;
import com.copycatsplus.copycats.utility.BlockFaceUtils;
import com.copycatsplus.copycats.utility.InteractionUtils;
import com.google.common.collect.ImmutableMap;
import com.zurrtum.create.AllBlocks;
import com.zurrtum.create.api.schematic.requirement.SpecialBlockItemRequirement;
import com.zurrtum.create.catnip.placement.IPlacementHelper;
import com.zurrtum.create.catnip.placement.PlacementHelpers;
import com.zurrtum.create.catnip.placement.PlacementOffset;
import com.zurrtum.create.content.contraptions.StructureTransform;
import com.zurrtum.create.content.equipment.wrench.IWrenchable;
import com.zurrtum.create.content.schematics.requirement.ItemRequirement;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

@ParametersAreNonnullByDefault
public class CopycatSlabBlock
extends WaterloggedMultiStateCopycatBlock
implements SpecialBlockItemRequirement {
    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.AXIS;
    public static final EnumProperty<SlabType> SLAB_TYPE = BlockStateProperties.SLAB_TYPE;
    private final ImmutableMap<FaceData, VoxelShape> partialFaceCache;
    private static final int placementHelperId = PlacementHelpers.register((IPlacementHelper)new PlacementHelper());

    public CopycatSlabBlock(BlockBehaviour.Properties pProperties) {
        super(pProperties);
        this.registerDefaultState((BlockState)((BlockState)this.defaultBlockState().setValue(AXIS, (Comparable)Direction.Axis.Y)).setValue(SLAB_TYPE, (Comparable)SlabType.BOTTOM));
        ImmutableMap.Builder builder = ImmutableMap.builder();
        for (String property : this.storageProperties()) {
            for (Direction.Axis axis : AXIS.getPossibleValues()) {
                for (Direction face : Direction.values()) {
                    builder.put((Object)new FaceData(property, axis, face), (Object)BlockFaceUtils.getPartialFaceShape(null, (BlockState)((BlockState)this.defaultBlockState().setValue(SLAB_TYPE, (Comparable)SlabType.DOUBLE)).setValue(AXIS, (Comparable)axis), property, face));
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
        return switch ((Direction.Axis)state.getValue(AXIS)) {
            default -> throw new MatchException(null, null);
            case Direction.Axis.X -> new Vec3i(2, 1, 1);
            case Direction.Axis.Y -> new Vec3i(1, 2, 1);
            case Direction.Axis.Z -> new Vec3i(1, 1, 2);
        };
    }

    @Override
    public String getPropertyFromInteraction(BlockState state, BlockGetter level, Vec3i hitLocation, BlockPos blockPos, Direction facing, Vec3 unscaledHit) {
        if (hitLocation.get((Direction.Axis)state.getValue(AXIS)) > 0) {
            return SlabType.TOP.getSerializedName();
        }
        return SlabType.BOTTOM.getSerializedName();
    }

    @Override
    public Vec3i getVectorFromProperty(BlockState state, String property) {
        return switch ((Direction.Axis)state.getValue(AXIS)) {
            default -> throw new MatchException(null, null);
            case Direction.Axis.X -> {
                if (property.equals(SlabType.TOP.getSerializedName())) {
                    yield new Vec3i(1, 0, 0);
                }
                yield new Vec3i(0, 0, 0);
            }
            case Direction.Axis.Y -> {
                if (property.equals(SlabType.TOP.getSerializedName())) {
                    yield new Vec3i(0, 1, 0);
                }
                yield new Vec3i(0, 0, 0);
            }
            case Direction.Axis.Z -> property.equals(SlabType.TOP.getSerializedName()) ? new Vec3i(0, 0, 1) : new Vec3i(0, 0, 0);
        };
    }

    @Override
    public boolean partExists(BlockState state, String property) {
        SlabType slabType = (SlabType)state.getValue(SLAB_TYPE);
        if (property.equals(SlabType.BOTTOM.getSerializedName())) {
            return slabType == SlabType.DOUBLE || slabType == SlabType.BOTTOM;
        }
        if (property.equals(SlabType.TOP.getSerializedName())) {
            return slabType == SlabType.DOUBLE || slabType == SlabType.TOP;
        }
        return false;
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
    protected InteractionResult useItemOn(ItemStack heldStack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult ray) {
        return InteractionUtils.sequential(() -> InteractionUtils.usePlacementHelper(placementHelperId, state, world, pos, player, hand, ray), () -> super.useItemOn(heldStack, state, world, pos, player, hand, ray));
    }

    @Override
    public InteractionResult onSneakWrenched(BlockState state, UseOnContext context) {
        this.onWrenched(state, context);
        if (state.getValue(SLAB_TYPE) != SlabType.DOUBLE) {
            return super.onSneakWrenched(state, context);
        }
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        String property = this.getPropertyFromInteraction(state, (BlockGetter)context.getLevel(), context.getClickedPos(), context.getClickLocation(), context.getClickedFace(), true);
        if (!this.partExists(state, property)) {
            return InteractionResult.FAIL;
        }
        if (world instanceof ServerLevel) {
            if (player != null) {
                List drops = Block.getDrops((BlockState)((BlockState)this.defaultBlockState().setValue(SLAB_TYPE, (Comparable)(property.equals(SlabType.BOTTOM.getSerializedName()) ? SlabType.BOTTOM : SlabType.TOP))), (ServerLevel)((ServerLevel)world), (BlockPos)pos, (BlockEntity)world.getBlockEntity(pos), (Entity)player, (ItemInstance)context.getItemInHand());
                if (!player.isCreative()) {
                    for (ItemStack drop : drops) {
                        player.getInventory().placeItemBackInInventory(drop);
                    }
                }
            }
            BlockPos up = pos.relative(Direction.UP);
            world.setBlockAndUpdate(pos, (BlockState)state.setValue(SLAB_TYPE, (Comparable)(property.equals(SlabType.BOTTOM.getSerializedName()) ? SlabType.TOP : SlabType.BOTTOM)));
            IWrenchable.playRemoveSound((Level)world, (BlockPos)pos);
        }
        return InteractionResult.SUCCESS;
    }

    public ItemRequirement getRequiredItems(BlockState state, BlockEntity blockEntity) {
        Item item = this.asItem();
        return new ItemRequirement(ItemRequirement.ItemUseType.CONSUME, new ItemStack((ItemLike)item, switch ((SlabType)state.getValue(SLAB_TYPE)) {
            default -> throw new MatchException(null, null);
            case SlabType.BOTTOM, SlabType.TOP -> 1;
            case SlabType.DOUBLE -> 2;
        }));
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
            return (BlockState)((BlockState)state.setValue(SLAB_TYPE, (Comparable)SlabType.DOUBLE)).setValue((Property)WATERLOGGED, (Comparable)Boolean.valueOf(false));
        }
        Direction.Axis axis = context.getNearestLookingDirection().getAxis();
        boolean flag = switch (axis) {
            default -> throw new MatchException(null, null);
            case Direction.Axis.X -> {
                if (context.getClickLocation().x - (double)blockPos.getX() > 0.5) {
                    yield true;
                }
                yield false;
            }
            case Direction.Axis.Y -> {
                if (context.getClickLocation().y - (double)blockPos.getY() > 0.5) {
                    yield true;
                }
                yield false;
            }
            case Direction.Axis.Z -> context.getClickLocation().z - (double)blockPos.getZ() > 0.5;
        };
        Direction clickedFace = context.getClickedFace();
        return (BlockState)((BlockState)stateForPlacement.setValue(AXIS, (Comparable)axis)).setValue(SLAB_TYPE, (Comparable)(clickedFace == Direction.fromAxisAndDirection((Direction.Axis)axis, (Direction.AxisDirection)Direction.AxisDirection.POSITIVE) || clickedFace.getAxis() != axis && !flag ? SlabType.BOTTOM : SlabType.TOP));
    }

    public boolean canBeReplaced(BlockState pState, BlockPlaceContext pUseContext) {
        ItemStack itemstack = pUseContext.getItemInHand();
        SlabType slabtype = (SlabType)pState.getValue(SLAB_TYPE);
        Direction.Axis axis = (Direction.Axis)pState.getValue(AXIS);
        if (slabtype != SlabType.DOUBLE && itemstack.is((Object)this.asItem())) {
            boolean flag = switch (axis) {
                default -> throw new MatchException(null, null);
                case Direction.Axis.X -> {
                    if (pUseContext.getClickLocation().x - (double)pUseContext.getClickedPos().getX() > 0.5) {
                        yield true;
                    }
                    yield false;
                }
                case Direction.Axis.Y -> {
                    if (pUseContext.getClickLocation().y - (double)pUseContext.getClickedPos().getY() > 0.5) {
                        yield true;
                    }
                    yield false;
                }
                case Direction.Axis.Z -> pUseContext.getClickLocation().z - (double)pUseContext.getClickedPos().getZ() > 0.5;
            };
            Direction direction = pUseContext.getClickedFace();
            if (slabtype == SlabType.BOTTOM) {
                return direction == Direction.fromAxisAndDirection((Direction.Axis)axis, (Direction.AxisDirection)Direction.AxisDirection.POSITIVE) || flag;
            }
            return direction == Direction.fromAxisAndDirection((Direction.Axis)axis, (Direction.AxisDirection)Direction.AxisDirection.NEGATIVE) || !flag;
        }
        return false;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition((StateDefinition.Builder<Block, BlockState>)pBuilder.add(new Property[]{AXIS}).add(new Property[]{SLAB_TYPE}));
    }

    public boolean isPathfindable(BlockState pState, PathComputationType pType) {
        return super.isPathfindable(pState, pType);
    }

    @NotNull
    public VoxelShape getShape(BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        SlabType type = (SlabType)pState.getValue(SLAB_TYPE);
        Direction.Axis axis = (Direction.Axis)pState.getValue(AXIS);
        if (type == SlabType.DOUBLE) {
            return Shapes.block();
        }
        if (type == SlabType.BOTTOM) {
            return CCShapes.SLAB_BOTTOM.get(axis).toShape();
        }
        return CCShapes.SLAB_TOP.get(axis).toShape();
    }

    @Override
    public VoxelShape getPartialFaceShape(BlockGetter level, BlockState state, String property, Direction face) {
        if (!this.partExists(state, property)) {
            return Shapes.empty();
        }
        return Objects.requireNonNull((VoxelShape)this.partialFaceCache.getOrDefault((Object)new FaceData(property, (Direction.Axis)state.getValue(AXIS), face), (Object)Shapes.empty()));
    }

    public boolean supportsExternalFaceHiding(BlockState state) {
        return true;
    }

    @Override
    public boolean hidesNeighborFace(BlockGetter level, BlockPos pos, BlockState state, BlockState neighborState, Direction dir) {
        return ICopycatBlock.hidesNeighborFace(level, pos, state, neighborState, dir);
    }

    public static CopycatTransformableState<Void> toTransformableState(BlockState state) {
        return CopycatTransformableState.create(t -> {
            Direction facing = CopycatSlabBlock.getApparentDirection(state);
            Vec3i normal = facing.getUnitVec3i();
            Vec3i part = new Vec3i(normal.getX() == 0 ? 8 : (normal.getX() > 0 ? 16 : 0), normal.getY() == 0 ? 8 : (normal.getY() > 0 ? 16 : 0), normal.getZ() == 0 ? 8 : (normal.getZ() > 0 ? 16 : 0));
            t.addPart(part.getX(), part.getY(), part.getZ());
            if (state.getValue(SLAB_TYPE) == SlabType.DOUBLE) {
                Vec3i part2 = new Vec3i(16, 16, 16).subtract(part);
                t.addPart(part2.getX(), part2.getY(), part2.getZ());
            }
        });
    }

    public static CopycatTransformableState<MaterialItemStorage.MaterialItem> toTransformableStorage(BlockState state, IMultiStateCopycatBlockEntity be) {
        return CopycatTransformableState.create(t -> {
            Direction facing = CopycatSlabBlock.getApparentDirection(state);
            Vec3i normal = facing.getUnitVec3i();
            Vec3i part = new Vec3i(normal.getX() == 0 ? 8 : (normal.getX() > 0 ? 16 : 0), normal.getY() == 0 ? 8 : (normal.getY() > 0 ? 16 : 0), normal.getZ() == 0 ? 8 : (normal.getZ() > 0 ? 16 : 0));
            t.addPart(part.getX(), part.getY(), part.getZ()).setData(be.getMaterialItemStorage().getMaterialItem(facing.getAxisDirection() == Direction.AxisDirection.POSITIVE ? Half.TOP.getSerializedName() : Half.BOTTOM.getSerializedName()));
            if (state.getValue(SLAB_TYPE) == SlabType.DOUBLE) {
                Vec3i part2 = new Vec3i(16, 16, 16).subtract(part);
                t.addPart(part2.getX(), part2.getY(), part2.getZ()).setData(be.getMaterialItemStorage().getMaterialItem(facing.getAxisDirection() == Direction.AxisDirection.POSITIVE ? Half.BOTTOM.getSerializedName() : Half.TOP.getSerializedName()));
            }
        });
    }

    public static BlockState fromTransformableState(BlockState state, CopycatTransformableState<Void> transformableState) {
        SlabType type;
        Direction.Axis axis;
        CopycatTransformableState.Part part = transformableState.parts.get(0);
        if (part.vector.getX() != 8) {
            axis = Direction.Axis.X;
            type = part.vector.getX() > 8 ? SlabType.TOP : SlabType.BOTTOM;
        } else if (part.vector.getY() != 8) {
            axis = Direction.Axis.Y;
            type = part.vector.getY() > 8 ? SlabType.TOP : SlabType.BOTTOM;
        } else {
            axis = Direction.Axis.Z;
            SlabType slabType = type = part.vector.getZ() > 8 ? SlabType.TOP : SlabType.BOTTOM;
        }
        if (transformableState.parts.size() == 2) {
            type = SlabType.DOUBLE;
        }
        return (BlockState)((BlockState)state.setValue(AXIS, (Comparable)axis)).setValue(SLAB_TYPE, (Comparable)type);
    }

    public static void fromTransformableStorage(BlockState state, IMultiStateCopycatBlockEntity be, CopycatTransformableState<MaterialItemStorage.MaterialItem> transformableState) {
        for (String string : be.getMaterialItemStorage().getAllProperties()) {
            be.getMaterialItemStorage().storeMaterialItem(string, new MaterialItemStorage.MaterialItem(AllBlocks.COPYCAT_BASE.defaultBlockState(), ItemStack.EMPTY));
        }
        for (CopycatTransformableState.Part part : transformableState.parts) {
            be.getMaterialItemStorage().storeMaterialItem(part.vector.getX() > 8 || part.vector.getY() > 8 || part.vector.getZ() > 8 ? Half.TOP.getSerializedName() : Half.BOTTOM.getSerializedName(), (MaterialItemStorage.MaterialItem)part.data);
        }
    }

    @Override
    public BlockState transform(BlockState state, StructureTransform transform) {
        return CopycatSlabBlock.fromTransformableState(state, CopycatSlabBlock.toTransformableState(state).transform(transform));
    }

    @Override
    public void transformStorage(BlockState state, IMultiStateCopycatBlockEntity be, StructureTransform transform) {
        state = CopycatSlabBlock.fromTransformableState(state, CopycatSlabBlock.toTransformableState(state).untransform(transform));
        CopycatSlabBlock.fromTransformableStorage(state, be, CopycatSlabBlock.toTransformableStorage(state, be).transform(transform));
    }

    public static FaceShape getFaceShape(BlockState state, Direction face) {
        SlabType slab = (SlabType)state.getValue(SLAB_TYPE);
        if (state.getValue(AXIS) != face.getAxis()) {
            return FaceShape.forSlabSide(slab);
        }
        return switch (slab) {
            default -> throw new MatchException(null, null);
            case SlabType.TOP -> FaceShape.fullOrNone(face.getAxisDirection() == Direction.AxisDirection.POSITIVE);
            case SlabType.BOTTOM -> FaceShape.fullOrNone(face.getAxisDirection() == Direction.AxisDirection.NEGATIVE);
            case SlabType.DOUBLE -> FaceShape.FULL;
        };
    }

    public static Direction getApparentDirection(BlockState state) {
        return Direction.fromAxisAndDirection((Direction.Axis)((Direction.Axis)state.getValue(AXIS)), (Direction.AxisDirection)(state.getValue(SLAB_TYPE) == SlabType.BOTTOM ? Direction.AxisDirection.NEGATIVE : Direction.AxisDirection.POSITIVE));
    }

    public static BlockState setApparentDirection(BlockState state, Direction direction) {
        SlabType type = (SlabType)state.getValue(SLAB_TYPE);
        if (type == SlabType.DOUBLE) {
            return (BlockState)state.setValue(AXIS, (Comparable)direction.getAxis());
        }
        if (CopycatSlabBlock.getApparentDirection(state).getAxisDirection() != direction.getAxisDirection()) {
            return (BlockState)((BlockState)state.setValue(AXIS, (Comparable)direction.getAxis())).setValue(SLAB_TYPE, (Comparable)(type == SlabType.BOTTOM ? SlabType.TOP : SlabType.BOTTOM));
        }
        return (BlockState)state.setValue(AXIS, (Comparable)direction.getAxis());
    }

    private record FaceData(String property, Direction.Axis axis, Direction face) {
    }

    public static enum FaceShape {
        FULL,
        TOP,
        BOTTOM,
        NONE;


        public static FaceShape forSlabSide(SlabType type) {
            return switch (type) {
                default -> throw new MatchException(null, null);
                case SlabType.TOP -> TOP;
                case SlabType.BOTTOM -> BOTTOM;
                case SlabType.DOUBLE -> FULL;
            };
        }

        public static FaceShape fullOrNone(boolean value) {
            return value ? FULL : NONE;
        }

        public static boolean canConnect(FaceShape shape1, FaceShape shape2) {
            return shape1 == shape2 || shape1 == FULL && shape2 != NONE || shape2 == FULL && shape1 != NONE;
        }

        public boolean hasContact() {
            return this != NONE;
        }
    }

    private static class PlacementHelper
    implements IPlacementHelper {
        private PlacementHelper() {
        }

        public Predicate<ItemStack> getItemPredicate() {
            return stack -> stack.is((Object)((CopycatSlabBlock)CCBlocks.COPYCAT_SLAB.get()).asItem());
        }

        public Predicate<BlockState> getStatePredicate() {
            return state -> state.getBlock() instanceof CopycatSlabBlock;
        }

        public PlacementOffset getOffset(Player player, Level world, BlockState state, BlockPos pos, BlockHitResult ray) {
            List directions = IPlacementHelper.orderedByDistanceExceptAxis((BlockPos)pos, (Vec3)ray.getLocation(), (Direction.Axis)((Direction.Axis)state.getValue(AXIS)), dir -> world.getBlockState(pos.relative(dir)).canBeReplaced());
            if (directions.isEmpty()) {
                return PlacementOffset.fail();
            }
            if (((SlabType)state.getValue(SLAB_TYPE)).equals((Object)SlabType.DOUBLE)) {
                return PlacementOffset.success((Vec3i)pos.relative((Direction)directions.get(0)), s -> (BlockState)((BlockState)s.setValue(AXIS, (Comparable)((Direction.Axis)state.getValue(AXIS)))).setValue(SLAB_TYPE, (Comparable)SlabType.BOTTOM));
            }
            return PlacementOffset.success((Vec3i)pos.relative((Direction)directions.get(0)), s -> (BlockState)((BlockState)s.setValue(AXIS, (Comparable)((Direction.Axis)state.getValue(AXIS)))).setValue(SLAB_TYPE, (Comparable)((SlabType)state.getValue(SLAB_TYPE))));
        }
    }
}

