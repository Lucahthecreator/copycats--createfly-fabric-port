/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap
 *  com.google.common.collect.ImmutableMap$Builder
 *  com.zurrtum.create.api.schematic.requirement.SpecialBlockItemRequirement
 *  com.zurrtum.create.content.contraptions.StructureTransform
 *  com.zurrtum.create.content.equipment.wrench.IWrenchable
 *  com.zurrtum.create.content.schematics.requirement.ItemRequirement
 *  javax.annotation.ParametersAreNonnullByDefault
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.core.Direction$Axis
 *  net.minecraft.core.Vec3i
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
 *  net.minecraft.world.level.block.state.properties.Half
 *  net.minecraft.world.level.block.state.properties.IntegerProperty
 *  net.minecraft.world.level.block.state.properties.Property
 *  net.minecraft.world.level.pathfinder.PathComputationType
 *  net.minecraft.world.phys.Vec3
 *  net.minecraft.world.phys.shapes.BooleanOp
 *  net.minecraft.world.phys.shapes.CollisionContext
 *  net.minecraft.world.phys.shapes.Shapes
 *  net.minecraft.world.phys.shapes.VoxelShape
 *  org.jetbrains.annotations.NotNull
 */
package com.copycatsplus.copycats.content.copycat.half_layer;

import com.copycatsplus.copycats.CCBlocks;
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
import com.zurrtum.create.api.schematic.requirement.SpecialBlockItemRequirement;
import com.zurrtum.create.content.contraptions.StructureTransform;
import com.zurrtum.create.content.equipment.wrench.IWrenchable;
import com.zurrtum.create.content.schematics.requirement.ItemRequirement;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
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
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

@ParametersAreNonnullByDefault
public class CopycatHalfLayerBlock
extends WaterloggedMultiStateCopycatBlock
implements SpecialBlockItemRequirement {
    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.HORIZONTAL_AXIS;
    public static final EnumProperty<Half> HALF = BlockStateProperties.HALF;
    public static final IntegerProperty POSITIVE_LAYERS = IntegerProperty.create((String)"positive_layers", (int)0, (int)8);
    public static final IntegerProperty NEGATIVE_LAYERS = IntegerProperty.create((String)"negative_layers", (int)0, (int)8);
    private final Function<BlockState, VoxelShape> shapesCache;
    private final ImmutableMap<FaceData, VoxelShape> partialFaceCache;

    public CopycatHalfLayerBlock(BlockBehaviour.Properties pProperties) {
        super(pProperties);
        this.registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.defaultBlockState().setValue(AXIS, (Comparable)Direction.Axis.X)).setValue(HALF, (Comparable)Half.BOTTOM)).setValue((Property)POSITIVE_LAYERS, (Comparable)Integer.valueOf(0))).setValue((Property)NEGATIVE_LAYERS, (Comparable)Integer.valueOf(0)));
        this.shapesCache = this.getShapeForEachState(CopycatHalfLayerBlock::calculateMultiFaceShape);
        ImmutableMap.Builder builder = ImmutableMap.builder();
        for (String property : this.storageProperties()) {
            for (Direction.Axis axis : AXIS.getPossibleValues()) {
                for (Half half : HALF.getPossibleValues()) {
                    Iterator iterator = POSITIVE_LAYERS.getPossibleValues().iterator();
                    while (iterator.hasNext()) {
                        int layers = (Integer)iterator.next();
                        for (Direction face : Direction.values()) {
                            builder.put((Object)new FaceData(property, axis, half, layers, face), (Object)BlockFaceUtils.getPartialFaceShape(null, (BlockState)((BlockState)((BlockState)((BlockState)this.defaultBlockState().setValue(AXIS, (Comparable)axis)).setValue(HALF, (Comparable)half)).setValue((Property)NEGATIVE_LAYERS, (Comparable)Integer.valueOf(layers))).setValue((Property)POSITIVE_LAYERS, (Comparable)Integer.valueOf(layers)), property, face));
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
        return switch ((Direction.Axis)state.getValue(AXIS)) {
            default -> throw new MatchException(null, null);
            case Direction.Axis.X -> new Vec3i(2, 1, 1);
            case Direction.Axis.Y -> new Vec3i(1, 2, 1);
            case Direction.Axis.Z -> new Vec3i(1, 1, 2);
        };
    }

    @Override
    public boolean partExists(BlockState state, String property) {
        if (property.equals(POSITIVE_LAYERS.getName())) {
            return (Integer)state.getValue((Property)POSITIVE_LAYERS) > 0;
        }
        if (property.equals(NEGATIVE_LAYERS.getName())) {
            return (Integer)state.getValue((Property)NEGATIVE_LAYERS) > 0;
        }
        return false;
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
        if (hitLocation.get((Direction.Axis)state.getValue(AXIS)) > 0) {
            return POSITIVE_LAYERS.getName();
        }
        return NEGATIVE_LAYERS.getName();
    }

    @Override
    public Vec3i getVectorFromProperty(BlockState state, String property) {
        return switch ((Direction.Axis)state.getValue(AXIS)) {
            default -> throw new MatchException(null, null);
            case Direction.Axis.X -> {
                if (property.equals(POSITIVE_LAYERS.getName())) {
                    yield new Vec3i(1, 0, 0);
                }
                yield new Vec3i(0, 0, 0);
            }
            case Direction.Axis.Y -> {
                if (property.equals(POSITIVE_LAYERS.getName())) {
                    yield new Vec3i(0, 1, 0);
                }
                yield new Vec3i(0, 0, 0);
            }
            case Direction.Axis.Z -> property.equals(POSITIVE_LAYERS.getName()) ? new Vec3i(0, 0, 1) : new Vec3i(0, 0, 0);
        };
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
            Vec3 clickPosition = context.getClickLocation().add(Vec3.atLowerCornerOf((Vec3i)context.getClickedFace().getUnitVec3i()).scale(0.0625)).subtract(Vec3.atLowerCornerOf((Vec3i)context.getClickedPos()));
            IntegerProperty targetProp = ((Direction.Axis)state.getValue(AXIS)).choose(clickPosition.x, clickPosition.y, clickPosition.z) < 0.5 ? NEGATIVE_LAYERS : POSITIVE_LAYERS;
            if ((Integer)state.getValue((Property)targetProp) < 8) {
                return (BlockState)state.cycle((Property)targetProp);
            }
            Copycats.LOGGER.warn("Can't figure out where to place a half layer! Please file an issue if you see this.");
            return state;
        }
        Direction.Axis axis = context.getHorizontalDirection().getAxis();
        Vec3 clickPosition = context.getClickLocation().subtract(Vec3.atLowerCornerOf((Vec3i)context.getClickedPos()));
        return (BlockState)((BlockState)((BlockState)stateForPlacement.setValue(AXIS, (Comparable)axis)).setValue(HALF, (Comparable)(clickPosition.y > 0.5 ? Half.TOP : Half.BOTTOM))).setValue((Property)(axis.choose(clickPosition.x, clickPosition.y, clickPosition.z) > 0.5 ? POSITIVE_LAYERS : NEGATIVE_LAYERS), (Comparable)Integer.valueOf(1));
    }

    public boolean canBeReplaced(@NotNull BlockState pState, BlockPlaceContext pUseContext) {
        ItemStack itemstack = pUseContext.getItemInHand();
        if (!itemstack.is((Object)this.asItem())) {
            return false;
        }
        Direction clickFace = pUseContext.getClickedFace();
        if (clickFace.getAxis().isVertical() && pState.getValue(HALF) == Half.TOP == (clickFace == Direction.UP)) {
            return false;
        }
        Vec3 clickPosition = pUseContext.getClickLocation().add(Vec3.atLowerCornerOf((Vec3i)pUseContext.getClickedFace().getUnitVec3i()).scale(0.0625)).subtract(Vec3.atLowerCornerOf((Vec3i)pUseContext.getClickedPos()));
        IntegerProperty targetProp = ((Direction.Axis)pState.getValue(AXIS)).choose(clickPosition.x, clickPosition.y, clickPosition.z) < 0.5 ? NEGATIVE_LAYERS : POSITIVE_LAYERS;
        return (Integer)pState.getValue((Property)targetProp) != 8;
    }

    @Override
    public InteractionResult onSneakWrenched(BlockState state, UseOnContext context) {
        if ((Integer)state.getValue((Property)POSITIVE_LAYERS) + (Integer)state.getValue((Property)NEGATIVE_LAYERS) <= 1) {
            return super.onSneakWrenched(state, context);
        }
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        Vec3 clickPosition = context.getClickLocation().subtract(Vec3.atLowerCornerOf((Vec3i)context.getClickedFace().getUnitVec3i()).scale(0.0625)).subtract(Vec3.atLowerCornerOf((Vec3i)pos));
        IntegerProperty targetProp = ((Direction.Axis)state.getValue(AXIS)).choose(clickPosition.x, clickPosition.y, clickPosition.z) < 0.5 ? NEGATIVE_LAYERS : POSITIVE_LAYERS;
        if ((Integer)state.getValue((Property)targetProp) == 1) {
            this.onWrenched(state, context);
        }
        if (world instanceof ServerLevel) {
            ServerLevel serverLevel = (ServerLevel)world;
            if (player != null) {
                List drops = Block.getDrops((BlockState)((BlockState)((BlockState)((BlockState)state.setValue((Property)POSITIVE_LAYERS, (Comparable)Integer.valueOf(0))).setValue((Property)NEGATIVE_LAYERS, (Comparable)Integer.valueOf(0))).setValue((Property)targetProp, (Comparable)Integer.valueOf(1))), (ServerLevel)serverLevel, (BlockPos)pos, (BlockEntity)world.getBlockEntity(pos), (Entity)player, (ItemInstance)context.getItemInHand());
                if (!player.isCreative()) {
                    for (ItemStack drop : drops) {
                        player.getInventory().placeItemBackInInventory(drop);
                    }
                }
            }
            BlockPos up = pos.relative(Direction.UP);
            world.setBlockAndUpdate(pos, (BlockState)state.setValue((Property)targetProp, (Comparable)Integer.valueOf((Integer)state.getValue((Property)targetProp) - 1)));
            IWrenchable.playRemoveSound((Level)world, (BlockPos)pos);
        }
        return InteractionResult.SUCCESS;
    }

    public ItemRequirement getRequiredItems(BlockState state, BlockEntity blockEntity) {
        return ICopycatBlock.getRequiredItemsForLayer(state, POSITIVE_LAYERS).union(ICopycatBlock.getRequiredItemsForLayer(state, NEGATIVE_LAYERS));
    }

    public static CopycatTransformableState<Integer> toTransformableState(BlockState state) {
        if (state.is((Object)((Block)CCBlocks.COPYCAT_HALF_LAYER.get()))) {
            Direction.Axis axis = (Direction.Axis)state.getValue(AXIS);
            Half half = (Half)state.getValue(HALF);
            return CopycatTransformableState.create(t -> {
                t.addPart(axis == Direction.Axis.X ? 12 : 8, half == Half.BOTTOM ? 0 : 16, axis == Direction.Axis.Z ? 12 : 8).setData((Integer)state.getValue((Property)POSITIVE_LAYERS));
                t.addPart(axis == Direction.Axis.X ? 4 : 8, half == Half.BOTTOM ? 0 : 16, axis == Direction.Axis.Z ? 4 : 8).setData((Integer)state.getValue((Property)NEGATIVE_LAYERS));
            });
        }
        if (state.is((Object)((Block)CCBlocks.COPYCAT_VERTICAL_HALF_LAYER.get()))) {
            Direction facing = (Direction)state.getValue(CopycatVerticalHalfLayerBlock.FACING);
            return CopycatTransformableState.create(t -> {
                t.addPart(12, 8, 16).rotateY(facing).setData((Integer)state.getValue((Property)POSITIVE_LAYERS));
                t.addPart(4, 8, 16).rotateY(facing).setData((Integer)state.getValue((Property)NEGATIVE_LAYERS));
            });
        }
        if (state.is((Object)((Block)CCBlocks.COPYCAT_STACKED_HALF_LAYER.get()))) {
            Direction facing = (Direction)state.getValue(CopycatStackedHalfLayerBlock.FACING);
            return CopycatTransformableState.create(t -> {
                t.addPart(8, 12, 16).rotateY(facing).setData((Integer)state.getValue((Property)POSITIVE_LAYERS));
                t.addPart(8, 4, 16).rotateY(facing).setData((Integer)state.getValue((Property)NEGATIVE_LAYERS));
            });
        }
        throw new IllegalArgumentException("Unsupported block state");
    }

    public static CopycatTransformableState<MaterialItemStorage.MaterialItem> toTransformableStorage(BlockState state, IMultiStateCopycatBlockEntity be) {
        if (state.is((Object)((Block)CCBlocks.COPYCAT_HALF_LAYER.get()))) {
            Direction.Axis axis = (Direction.Axis)state.getValue(AXIS);
            Half half = (Half)state.getValue(HALF);
            return CopycatTransformableState.create(t -> {
                t.addPart(axis == Direction.Axis.X ? 12 : 8, half == Half.BOTTOM ? 0 : 16, axis == Direction.Axis.Z ? 12 : 8).setData(be.getMaterialItemStorage().getMaterialItem(POSITIVE_LAYERS.getName()));
                t.addPart(axis == Direction.Axis.X ? 4 : 8, half == Half.BOTTOM ? 0 : 16, axis == Direction.Axis.Z ? 4 : 8).setData(be.getMaterialItemStorage().getMaterialItem(NEGATIVE_LAYERS.getName()));
            });
        }
        if (state.is((Object)((Block)CCBlocks.COPYCAT_VERTICAL_HALF_LAYER.get()))) {
            Direction facing = (Direction)state.getValue(CopycatVerticalHalfLayerBlock.FACING);
            return CopycatTransformableState.create(t -> {
                t.addPart(12, 8, 16).rotateY(facing).setData(be.getMaterialItemStorage().getMaterialItem(POSITIVE_LAYERS.getName()));
                t.addPart(4, 8, 16).rotateY(facing).setData(be.getMaterialItemStorage().getMaterialItem(NEGATIVE_LAYERS.getName()));
            });
        }
        if (state.is((Object)((Block)CCBlocks.COPYCAT_STACKED_HALF_LAYER.get()))) {
            Direction facing = (Direction)state.getValue(CopycatStackedHalfLayerBlock.FACING);
            return CopycatTransformableState.create(t -> {
                t.addPart(8, 12, 16).rotateY(facing).setData(be.getMaterialItemStorage().getMaterialItem(POSITIVE_LAYERS.getName()));
                t.addPart(8, 4, 16).rotateY(facing).setData(be.getMaterialItemStorage().getMaterialItem(NEGATIVE_LAYERS.getName()));
            });
        }
        throw new IllegalArgumentException("Unsupported block state");
    }

    public static BlockState fromTransformableState(BlockState state, CopycatTransformableState<Integer> transformableState) {
        CopycatTransformableState.Part firstPart = transformableState.parts.get(0);
        Direction facing = firstPart.getFacing();
        if (facing.getAxis().isVertical()) {
            Direction.Axis axis = firstPart.getHorizontalFacing().getAxis();
            Half half = firstPart.isTop() ? Half.TOP : Half.BOTTOM;
            state = (BlockState)((BlockState)BlockUtils.tryCopyProperties(state, CCBlocks.COPYCAT_HALF_LAYER.getDefaultState()).setValue(AXIS, (Comparable)axis)).setValue(HALF, (Comparable)half);
            for (CopycatTransformableState.Part part : transformableState.parts) {
                boolean positive = part.vector.get(axis) > 8;
                state = (BlockState)state.setValue((Property)(positive ? POSITIVE_LAYERS : NEGATIVE_LAYERS), (Comparable)((Integer)part.data));
            }
        } else if (firstPart.vector.getY() == 8) {
            state = (BlockState)BlockUtils.tryCopyProperties(state, CCBlocks.COPYCAT_VERTICAL_HALF_LAYER.getDefaultState()).setValue(CopycatVerticalHalfLayerBlock.FACING, (Comparable)facing);
            for (CopycatTransformableState.Part part : transformableState.parts) {
                boolean right = part.isRight(facing);
                state = (BlockState)state.setValue((Property)(right ? NEGATIVE_LAYERS : POSITIVE_LAYERS), (Comparable)((Integer)part.data));
            }
        } else {
            state = (BlockState)BlockUtils.tryCopyProperties(state, CCBlocks.COPYCAT_STACKED_HALF_LAYER.getDefaultState()).setValue(CopycatStackedHalfLayerBlock.FACING, (Comparable)facing);
            for (CopycatTransformableState.Part part : transformableState.parts) {
                boolean top = part.isTop();
                state = (BlockState)state.setValue((Property)(top ? POSITIVE_LAYERS : NEGATIVE_LAYERS), (Comparable)((Integer)part.data));
            }
        }
        return state;
    }

    public static void fromTransformableStorage(BlockState state, IMultiStateCopycatBlockEntity be, CopycatTransformableState<MaterialItemStorage.MaterialItem> transformableState) {
        CopycatTransformableState.Part firstPart = transformableState.parts.get(0);
        Direction facing = firstPart.getFacing();
        if (facing.getAxis().isVertical()) {
            Direction.Axis axis = firstPart.getHorizontalFacing().getAxis();
            for (CopycatTransformableState.Part part : transformableState.parts) {
                boolean positive = part.vector.get(axis) > 8;
                be.getMaterialItemStorage().storeMaterialItem(positive ? POSITIVE_LAYERS.getName() : NEGATIVE_LAYERS.getName(), (MaterialItemStorage.MaterialItem)part.data);
            }
        } else if (firstPart.vector.getY() == 8) {
            for (CopycatTransformableState.Part part : transformableState.parts) {
                boolean right = part.isRight(facing);
                be.getMaterialItemStorage().storeMaterialItem(right ? NEGATIVE_LAYERS.getName() : POSITIVE_LAYERS.getName(), (MaterialItemStorage.MaterialItem)part.data);
            }
        } else {
            for (CopycatTransformableState.Part part : transformableState.parts) {
                boolean top = part.isTop();
                be.getMaterialItemStorage().storeMaterialItem(top ? POSITIVE_LAYERS.getName() : NEGATIVE_LAYERS.getName(), (MaterialItemStorage.MaterialItem)part.data);
            }
        }
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

    public boolean isPathfindable(BlockState pState, PathComputationType pType) {
        return false;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition((StateDefinition.Builder<Block, BlockState>)pBuilder.add(new Property[]{AXIS, HALF, POSITIVE_LAYERS, NEGATIVE_LAYERS}));
    }

    private static VoxelShape calculateMultiFaceShape(BlockState pState) {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.joinUnoptimized((VoxelShape)shape, (VoxelShape)CCShapes.HALF_LAYER_BOTTOM.get(pState.getValue(AXIS)).get(pState.getValue(HALF)).get(pState.getValue((Property)NEGATIVE_LAYERS)).toShape(), (BooleanOp)BooleanOp.OR);
        shape = Shapes.joinUnoptimized((VoxelShape)shape, (VoxelShape)CCShapes.HALF_LAYER_TOP.get(pState.getValue(AXIS)).get(pState.getValue(HALF)).get(pState.getValue((Property)POSITIVE_LAYERS)).toShape(), (BooleanOp)BooleanOp.OR);
        return shape.optimize();
    }

    @NotNull
    public VoxelShape getShape(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        return Objects.requireNonNull(this.shapesCache.apply(pState));
    }

    @Override
    public VoxelShape getPartialFaceShape(BlockGetter level, BlockState state, String property, Direction face) {
        if (!this.partExists(state, property)) {
            return Shapes.empty();
        }
        return Objects.requireNonNull((VoxelShape)this.partialFaceCache.getOrDefault((Object)new FaceData(property, (Direction.Axis)state.getValue(AXIS), (Half)state.getValue(HALF), (Integer)state.getValue((Property)(property.equals(NEGATIVE_LAYERS.getName()) ? NEGATIVE_LAYERS : POSITIVE_LAYERS)), face), (Object)Shapes.empty()));
    }

    public boolean supportsExternalFaceHiding(BlockState state) {
        return true;
    }

    @Override
    public boolean hidesNeighborFace(BlockGetter level, BlockPos pos, BlockState state, BlockState neighborState, Direction dir) {
        return ICopycatBlock.hidesNeighborFace(level, pos, state, neighborState, dir);
    }

    private record FaceData(String property, Direction.Axis axis, Half half, int layers, Direction face) {
    }
}

