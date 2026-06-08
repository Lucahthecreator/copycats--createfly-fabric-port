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
package com.copycatsplus.copycats.content.copycat.stacked_half_layer;

import com.copycatsplus.copycats.CCShapes;
import com.copycatsplus.copycats.Copycats;
import com.copycatsplus.copycats.content.copycat.half_layer.CopycatHalfLayerBlock;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlockEntity;
import com.copycatsplus.copycats.foundation.copycat.multistate.WaterloggedMultiStateCopycatBlock;
import com.copycatsplus.copycats.utility.BlockFaceUtils;
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
public class CopycatStackedHalfLayerBlock
extends WaterloggedMultiStateCopycatBlock
implements SpecialBlockItemRequirement {
    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
    private final Function<BlockState, VoxelShape> shapesCache;
    private final ImmutableMap<FaceData, VoxelShape> partialFaceCache;

    public CopycatStackedHalfLayerBlock(BlockBehaviour.Properties pProperties) {
        super(pProperties);
        this.registerDefaultState((BlockState)((BlockState)((BlockState)this.defaultBlockState().setValue(FACING, (Comparable)Direction.NORTH)).setValue((Property)CopycatHalfLayerBlock.POSITIVE_LAYERS, (Comparable)Integer.valueOf(0))).setValue((Property)CopycatHalfLayerBlock.NEGATIVE_LAYERS, (Comparable)Integer.valueOf(0)));
        this.shapesCache = this.getShapeForEachState(CopycatStackedHalfLayerBlock::calculateMultiFaceShape);
        ImmutableMap.Builder builder = ImmutableMap.builder();
        for (String property : this.storageProperties()) {
            for (Direction facing : FACING.getPossibleValues()) {
                Iterator iterator = CopycatHalfLayerBlock.POSITIVE_LAYERS.getPossibleValues().iterator();
                while (iterator.hasNext()) {
                    int layers = (Integer)iterator.next();
                    for (Direction face : Direction.values()) {
                        builder.put((Object)new FaceData(property, facing, layers, face), (Object)BlockFaceUtils.getPartialFaceShape(null, (BlockState)((BlockState)((BlockState)this.defaultBlockState().setValue(FACING, (Comparable)facing)).setValue((Property)CopycatHalfLayerBlock.NEGATIVE_LAYERS, (Comparable)Integer.valueOf(layers))).setValue((Property)CopycatHalfLayerBlock.POSITIVE_LAYERS, (Comparable)Integer.valueOf(layers)), property, face));
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
        return new Vec3i(1, 2, 1);
    }

    @Override
    public boolean partExists(BlockState state, String property) {
        if (property.equals(CopycatHalfLayerBlock.POSITIVE_LAYERS.getName())) {
            return (Integer)state.getValue((Property)CopycatHalfLayerBlock.POSITIVE_LAYERS) > 0;
        }
        if (property.equals(CopycatHalfLayerBlock.NEGATIVE_LAYERS.getName())) {
            return (Integer)state.getValue((Property)CopycatHalfLayerBlock.NEGATIVE_LAYERS) > 0;
        }
        return false;
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
        return (double)hitLocation.getY() > 0.5 ? CopycatHalfLayerBlock.POSITIVE_LAYERS.getName() : CopycatHalfLayerBlock.NEGATIVE_LAYERS.getName();
    }

    @Override
    public Vec3i getVectorFromProperty(BlockState state, String property) {
        return new Vec3i(0, property.equals(CopycatHalfLayerBlock.POSITIVE_LAYERS.getName()) ? 1 : 0, 0);
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
            String property = this.getPropertyFromInteraction(state, (BlockGetter)context.getLevel(), blockPos, context.getClickLocation(), context.getClickedFace(), false);
            IntegerProperty targetProp = property.equals(CopycatHalfLayerBlock.POSITIVE_LAYERS.getName()) ? CopycatHalfLayerBlock.POSITIVE_LAYERS : CopycatHalfLayerBlock.NEGATIVE_LAYERS;
            if ((Integer)state.getValue((Property)targetProp) < 8) {
                return (BlockState)state.cycle((Property)targetProp);
            }
            Copycats.LOGGER.warn("Can't figure out where to place a half layer! Please file an issue if you see this.");
            return state;
        }
        Direction facing = context.getClickedFace().getOpposite();
        if (facing.getAxis().isVertical()) {
            facing = context.getHorizontalDirection();
        }
        Vec3 clickPosition = context.getClickLocation().subtract(Vec3.atLowerCornerOf((Vec3i)context.getClickedPos()));
        return (BlockState)((BlockState)stateForPlacement.setValue(FACING, (Comparable)facing)).setValue((Property)(clickPosition.y >= 0.5 ? CopycatHalfLayerBlock.POSITIVE_LAYERS : CopycatHalfLayerBlock.NEGATIVE_LAYERS), (Comparable)Integer.valueOf(1));
    }

    public boolean canBeReplaced(@NotNull BlockState state, BlockPlaceContext context) {
        ItemStack itemstack = context.getItemInHand();
        if (!itemstack.is((Object)this.asItem())) {
            return false;
        }
        if (context.getClickedFace() == state.getValue(FACING)) {
            return false;
        }
        String property = this.getPropertyFromInteraction(state, (BlockGetter)context.getLevel(), context.getClickedPos(), context.getClickLocation(), context.getClickedFace(), false);
        IntegerProperty targetProp = property.equals(CopycatHalfLayerBlock.POSITIVE_LAYERS.getName()) ? CopycatHalfLayerBlock.POSITIVE_LAYERS : CopycatHalfLayerBlock.NEGATIVE_LAYERS;
        return (Integer)state.getValue((Property)targetProp) != 8;
    }

    @Override
    public InteractionResult onSneakWrenched(BlockState state, UseOnContext context) {
        if ((Integer)state.getValue((Property)CopycatHalfLayerBlock.POSITIVE_LAYERS) + (Integer)state.getValue((Property)CopycatHalfLayerBlock.NEGATIVE_LAYERS) <= 1) {
            return super.onSneakWrenched(state, context);
        }
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        String property = this.getPropertyFromInteraction(state, (BlockGetter)context.getLevel(), context.getClickedPos(), context.getClickLocation(), context.getClickedFace(), true);
        IntegerProperty targetProp = property.equals(CopycatHalfLayerBlock.POSITIVE_LAYERS.getName()) ? CopycatHalfLayerBlock.POSITIVE_LAYERS : CopycatHalfLayerBlock.NEGATIVE_LAYERS;
        if ((Integer)state.getValue((Property)targetProp) == 1) {
            this.onWrenched(state, context);
        }
        if (world instanceof ServerLevel) {
            ServerLevel serverLevel = (ServerLevel)world;
            if (player != null) {
                List drops = Block.getDrops((BlockState)((BlockState)((BlockState)((BlockState)state.setValue((Property)CopycatHalfLayerBlock.POSITIVE_LAYERS, (Comparable)Integer.valueOf(0))).setValue((Property)CopycatHalfLayerBlock.NEGATIVE_LAYERS, (Comparable)Integer.valueOf(0))).setValue((Property)targetProp, (Comparable)Integer.valueOf(1))), (ServerLevel)serverLevel, (BlockPos)pos, (BlockEntity)world.getBlockEntity(pos), (Entity)player, (ItemInstance)context.getItemInHand());
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
        return ICopycatBlock.getRequiredItemsForLayer(state, CopycatHalfLayerBlock.POSITIVE_LAYERS).union(ICopycatBlock.getRequiredItemsForLayer(state, CopycatHalfLayerBlock.NEGATIVE_LAYERS));
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
        super.createBlockStateDefinition((StateDefinition.Builder<Block, BlockState>)pBuilder.add(new Property[]{FACING, CopycatHalfLayerBlock.POSITIVE_LAYERS, CopycatHalfLayerBlock.NEGATIVE_LAYERS}));
    }

    private static VoxelShape calculateMultiFaceShape(BlockState pState) {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.joinUnoptimized((VoxelShape)shape, (VoxelShape)CCShapes.STACKED_HALF_LAYER_TOP.get(pState.getValue(FACING)).get(pState.getValue((Property)CopycatHalfLayerBlock.POSITIVE_LAYERS)).toShape(), (BooleanOp)BooleanOp.OR);
        shape = Shapes.joinUnoptimized((VoxelShape)shape, (VoxelShape)CCShapes.STACKED_HALF_LAYER_BOTTOM.get(pState.getValue(FACING)).get(pState.getValue((Property)CopycatHalfLayerBlock.NEGATIVE_LAYERS)).toShape(), (BooleanOp)BooleanOp.OR);
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
        return Objects.requireNonNull((VoxelShape)this.partialFaceCache.getOrDefault((Object)new FaceData(property, (Direction)state.getValue(FACING), (Integer)state.getValue((Property)(property.equals(CopycatHalfLayerBlock.NEGATIVE_LAYERS.getName()) ? CopycatHalfLayerBlock.NEGATIVE_LAYERS : CopycatHalfLayerBlock.POSITIVE_LAYERS)), face), (Object)Shapes.empty()));
    }

    public boolean supportsExternalFaceHiding(BlockState state) {
        return true;
    }

    @Override
    public boolean hidesNeighborFace(BlockGetter level, BlockPos pos, BlockState state, BlockState neighborState, Direction dir) {
        return ICopycatBlock.hidesNeighborFace(level, pos, state, neighborState, dir);
    }

    private record FaceData(String property, Direction facing, int layers, Direction face) {
    }
}

