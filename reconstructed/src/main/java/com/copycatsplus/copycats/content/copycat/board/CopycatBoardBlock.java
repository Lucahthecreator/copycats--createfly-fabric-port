/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap
 *  com.google.common.collect.ImmutableMap$Builder
 *  com.zurrtum.create.api.schematic.requirement.SpecialBlockItemRequirement
 *  com.zurrtum.create.catnip.data.Iterate
 *  com.zurrtum.create.content.contraptions.StructureTransform
 *  com.zurrtum.create.content.equipment.wrench.IWrenchable
 *  com.zurrtum.create.content.schematics.requirement.ItemRequirement
 *  javax.annotation.Nullable
 *  javax.annotation.ParametersAreNonnullByDefault
 *  net.minecraft.client.renderer.block.BlockAndTintGetter
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.core.Direction$Axis
 *  net.minecraft.core.Direction$AxisDirection
 *  net.minecraft.core.Position
 *  net.minecraft.core.Vec3i
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.util.Mth
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
 *  net.minecraft.world.level.block.PipeBlock
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.state.BlockBehaviour$Properties
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.StateDefinition$Builder
 *  net.minecraft.world.level.block.state.properties.BlockStateProperties
 *  net.minecraft.world.level.block.state.properties.BooleanProperty
 *  net.minecraft.world.level.block.state.properties.Property
 *  net.minecraft.world.level.pathfinder.PathComputationType
 *  net.minecraft.world.phys.Vec3
 *  net.minecraft.world.phys.shapes.BooleanOp
 *  net.minecraft.world.phys.shapes.CollisionContext
 *  net.minecraft.world.phys.shapes.Shapes
 *  net.minecraft.world.phys.shapes.VoxelShape
 *  org.jetbrains.annotations.NotNull
 */
package com.copycatsplus.copycats.content.copycat.board;

import com.copycatsplus.copycats.CCShapes;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.ICustomCTBlocking;
import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlockEntity;
import com.copycatsplus.copycats.foundation.copycat.multistate.WaterloggedMultiStateCopycatBlock;
import com.copycatsplus.copycats.utility.BlockFaceUtils;
import com.copycatsplus.copycats.utility.BlockUtils;
import com.google.common.collect.ImmutableMap;
import com.zurrtum.create.api.schematic.requirement.SpecialBlockItemRequirement;
import com.zurrtum.create.catnip.data.Iterate;
import com.zurrtum.create.content.contraptions.StructureTransform;
import com.zurrtum.create.content.equipment.wrench.IWrenchable;
import com.zurrtum.create.content.schematics.requirement.ItemRequirement;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
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
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

@ParametersAreNonnullByDefault
public class CopycatBoardBlock
extends WaterloggedMultiStateCopycatBlock
implements ICustomCTBlocking,
SpecialBlockItemRequirement {
    public static BooleanProperty UP = BlockStateProperties.UP;
    public static BooleanProperty DOWN = BlockStateProperties.DOWN;
    public static BooleanProperty NORTH = BlockStateProperties.NORTH;
    public static BooleanProperty SOUTH = BlockStateProperties.SOUTH;
    public static BooleanProperty EAST = BlockStateProperties.EAST;
    public static BooleanProperty WEST = BlockStateProperties.WEST;
    public static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION = PipeBlock.PROPERTY_BY_DIRECTION;
    private final Function<BlockState, VoxelShape> shapesCache;
    private final ImmutableMap<FaceData, VoxelShape> partialFaceCache;

    public CopycatBoardBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.defaultBlockState().setValue((Property)UP, (Comparable)Boolean.valueOf(false))).setValue((Property)DOWN, (Comparable)Boolean.valueOf(false))).setValue((Property)NORTH, (Comparable)Boolean.valueOf(false))).setValue((Property)SOUTH, (Comparable)Boolean.valueOf(false))).setValue((Property)EAST, (Comparable)Boolean.valueOf(false))).setValue((Property)WEST, (Comparable)Boolean.valueOf(false)));
        this.shapesCache = this.getShapeForEachState(CopycatBoardBlock::calculateMultifaceShape);
        ImmutableMap.Builder builder = ImmutableMap.builder();
        BlockState state = (BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.defaultBlockState().setValue((Property)UP, (Comparable)Boolean.valueOf(true))).setValue((Property)DOWN, (Comparable)Boolean.valueOf(true))).setValue((Property)NORTH, (Comparable)Boolean.valueOf(true))).setValue((Property)SOUTH, (Comparable)Boolean.valueOf(true))).setValue((Property)EAST, (Comparable)Boolean.valueOf(true))).setValue((Property)WEST, (Comparable)Boolean.valueOf(true));
        for (String property : this.storageProperties()) {
            for (Direction face : Direction.values()) {
                builder.put((Object)new FaceData(property, face), (Object)BlockFaceUtils.getPartialFaceShape(null, state, property, face));
            }
        }
        this.partialFaceCache = builder.build();
    }

    @Override
    public String defaultProperty() {
        return UP.getName();
    }

    @Override
    public Vec3i vectorScale(BlockState state) {
        return new Vec3i(1, 1, 1);
    }

    @Override
    public Set<String> storageProperties() {
        return Set.of(UP, DOWN, NORTH, EAST, SOUTH, WEST).stream().map(Property::getName).collect(Collectors.toSet());
    }

    @Override
    public int getColorIndex(String property) {
        if (property.equals(UP.getName())) {
            return 0;
        }
        if (property.equals(DOWN.getName())) {
            return 0;
        }
        if (property.equals(NORTH.getName())) {
            return 1;
        }
        if (property.equals(SOUTH.getName())) {
            return 1;
        }
        if (property.equals(EAST.getName())) {
            return 2;
        }
        if (property.equals(WEST.getName())) {
            return 2;
        }
        return 0;
    }

    @Override
    public boolean partExists(BlockState state, String property) {
        if (property.equals(UP.getName())) {
            return (Boolean)state.getValue((Property)UP);
        }
        if (property.equals(DOWN.getName())) {
            return (Boolean)state.getValue((Property)DOWN);
        }
        if (property.equals(NORTH.getName())) {
            return (Boolean)state.getValue((Property)NORTH);
        }
        if (property.equals(SOUTH.getName())) {
            return (Boolean)state.getValue((Property)SOUTH);
        }
        if (property.equals(EAST.getName())) {
            return (Boolean)state.getValue((Property)EAST);
        }
        if (property.equals(WEST.getName())) {
            return (Boolean)state.getValue((Property)WEST);
        }
        return false;
    }

    @Override
    public String getPropertyFromInteraction(BlockState state, BlockGetter level, Vec3i hitLocation, BlockPos blockPos, Direction facing, Vec3 unscaledHit) {
        facing = Direction.fromAxisAndDirection((Direction.Axis)facing.getAxis(), (Direction.AxisDirection)(unscaledHit.get(facing.getAxis()) > 0.5 ? Direction.AxisDirection.POSITIVE : Direction.AxisDirection.NEGATIVE));
        BooleanProperty face = CopycatBoardBlock.byDirection(facing);
        return face.getName();
    }

    @Override
    public String getPropertyFromRender(String renderingProperty, BlockState state, BlockGetter level, Vec3i vector, BlockPos blockPos) {
        return renderingProperty;
    }

    @Override
    public Vec3i getVectorFromProperty(BlockState state, String property) {
        return Vec3i.ZERO;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition((StateDefinition.Builder<Block, BlockState>)pBuilder.add(new Property[]{UP, DOWN, NORTH, SOUTH, EAST, WEST}));
    }

    @Override
    public boolean isIgnoredConnectivitySide(BlockAndTintGetter reader, BlockState state, Direction face, BlockPos fromPos, @Nullable BlockPos toPos, @Nullable BlockState toState) {
        if (toPos == null) {
            return true;
        }
        return !reader.getBlockState(toPos).is((Object)this);
    }

    @Override
    public boolean canConnectTexturesToward(String property, BlockAndTintGetter reader, BlockPos fromPos, BlockPos toPos, BlockState state) {
        return reader.getBlockState(toPos).is((Object)this);
    }

    @Override
    public Optional<Boolean> blockCTTowards(BlockAndTintGetter reader, BlockState state, BlockPos pos, BlockPos ctPos, BlockPos connectingPos, Direction face) {
        if (!reader.getBlockState(ctPos).is((Object)this)) {
            return Optional.empty();
        }
        return Optional.of(false);
    }

    public boolean isPathfindable(BlockState pState, PathComputationType pType) {
        return switch (pType) {
            case PathComputationType.LAND -> {
                if (!((Boolean)pState.getValue((Property)UP)).booleanValue() && ((Boolean)pState.getValue((Property)DOWN)).booleanValue() || ((Boolean)pState.getValue((Property)UP)).booleanValue()) {
                    yield true;
                }
                yield false;
            }
            default -> false;
        };
    }

    private static VoxelShape calculateMultifaceShape(BlockState pState) {
        VoxelShape shape = Shapes.empty();
        for (Direction direction : Iterate.directions) {
            if (!((Boolean)pState.getValue((Property)CopycatBoardBlock.byDirection(direction))).booleanValue()) continue;
            shape = Shapes.joinUnoptimized((VoxelShape)shape, (VoxelShape)CCShapes.BOARD.get(direction).toShape(), (BooleanOp)BooleanOp.OR);
        }
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
        return Objects.requireNonNull((VoxelShape)this.partialFaceCache.getOrDefault((Object)new FaceData(property, face), (Object)Shapes.empty()));
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
            if (!((Boolean)state.getValue((Property)CopycatBoardBlock.byDirection(context.getClickedFace().getOpposite()))).booleanValue()) {
                return (BlockState)state.setValue((Property)CopycatBoardBlock.byDirection(context.getClickedFace().getOpposite()), (Comparable)Boolean.valueOf(true));
            }
            return (BlockState)state.setValue((Property)CopycatBoardBlock.byDirection(context.getClickedFace()), (Comparable)Boolean.valueOf(true));
        }
        return (BlockState)stateForPlacement.setValue((Property)CopycatBoardBlock.byDirection(context.getClickedFace().getOpposite()), (Comparable)Boolean.valueOf(true));
    }

    public boolean canBeReplaced(BlockState pState, BlockPlaceContext pUseContext) {
        ItemStack itemstack = pUseContext.getItemInHand();
        if (!itemstack.is((Object)this.asItem())) {
            return false;
        }
        if (!((Boolean)pState.getValue((Property)CopycatBoardBlock.byDirection(pUseContext.getClickedFace().getOpposite()))).booleanValue()) {
            double loc;
            Direction direction = pUseContext.getClickedFace().getOpposite();
            double pos = CopycatBoardBlock.getByAxis((Vec3i)pUseContext.getClickedPos(), direction.getAxis());
            if (CopycatBoardBlock.getByAxis(direction.getUnitVec3i(), direction.getAxis()) > 0) {
                pos += 1.0;
            }
            if (Math.abs(pos - (loc = CopycatBoardBlock.getByAxis((Position)pUseContext.getClickLocation(), direction.getAxis()))) < 0.125) {
                return true;
            }
        }
        if (!((Boolean)pState.getValue((Property)CopycatBoardBlock.byDirection(pUseContext.getClickedFace()))).booleanValue()) {
            double hitLoc = CopycatBoardBlock.getByAxis((Position)pUseContext.getClickLocation(), pUseContext.getClickedFace().getAxis());
            int direction = CopycatBoardBlock.getByAxis(pUseContext.getClickedFace().getUnitVec3i(), pUseContext.getClickedFace().getAxis());
            double offset = hitLoc - (double)Math.round(hitLoc);
            if (Mth.sign((double)direction) == Mth.sign((double)offset) && Math.abs(offset) < 0.125) {
                return true;
            }
        }
        return false;
    }

    @Override
    public InteractionResult onSneakWrenched(BlockState state, UseOnContext context) {
        this.onWrenched(state, context);
        int faceCount = 0;
        for (Direction direction : Iterate.directions) {
            if (!((Boolean)state.getValue((Property)CopycatBoardBlock.byDirection(direction))).booleanValue()) continue;
            ++faceCount;
        }
        if (faceCount <= 1) {
            return super.onSneakWrenched(state, context);
        }
        ArrayList<Direction> options = new ArrayList<Direction>(6);
        for (Direction direction : Iterate.directions) {
            double loc;
            if (!((Boolean)state.getValue((Property)CopycatBoardBlock.byDirection(direction))).booleanValue()) continue;
            double pos = CopycatBoardBlock.getByAxis((Vec3i)context.getClickedPos(), direction.getAxis());
            if (CopycatBoardBlock.getByAxis(direction.getUnitVec3i(), direction.getAxis()) > 0) {
                pos += 1.0;
            }
            if (!(Math.abs(pos - (loc = CopycatBoardBlock.getByAxis((Position)context.getClickLocation(), direction.getAxis()))) < 0.125)) continue;
            options.add(direction);
        }
        if (options.size() > 1) {
            Direction backup = (Direction)options.get(0);
            options.removeIf(d -> d.getAxis() != context.getClickedFace().getAxis());
            if (options.size() == 0) {
                options.add(backup);
            }
        }
        if (options.size() == 0) {
            return super.onSneakWrenched(state, context);
        }
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        if (world instanceof ServerLevel) {
            if (player != null) {
                List drops = Block.getDrops((BlockState)((BlockState)this.defaultBlockState().setValue((Property)CopycatBoardBlock.byDirection((Direction)options.get(0)), (Comparable)Boolean.valueOf(true))), (ServerLevel)((ServerLevel)world), (BlockPos)pos, (BlockEntity)world.getBlockEntity(pos), (Entity)player, (ItemInstance)context.getItemInHand());
                if (!player.isCreative()) {
                    for (ItemStack drop : drops) {
                        player.getInventory().placeItemBackInInventory(drop);
                    }
                }
            }
            BlockPos up = pos.relative(Direction.UP);
            world.setBlockAndUpdate(pos, (BlockState)state.setValue((Property)CopycatBoardBlock.byDirection((Direction)options.get(0)), (Comparable)Boolean.valueOf(false)));
            IWrenchable.playRemoveSound((Level)world, (BlockPos)pos);
        }
        return InteractionResult.SUCCESS;
    }

    public ItemRequirement getRequiredItems(BlockState state, BlockEntity blockEntity) {
        return IMultiStateCopycatBlock.getRequiredItemsForParts(state, UP, DOWN, NORTH, SOUTH, EAST, WEST);
    }

    private static int getByAxis(Vec3i pos, Direction.Axis axis) {
        return switch (axis) {
            default -> throw new MatchException(null, null);
            case Direction.Axis.X -> pos.getX();
            case Direction.Axis.Y -> pos.getY();
            case Direction.Axis.Z -> pos.getZ();
        };
    }

    private static double getByAxis(Position pos, Direction.Axis axis) {
        return switch (axis) {
            default -> throw new MatchException(null, null);
            case Direction.Axis.X -> pos.x();
            case Direction.Axis.Y -> pos.y();
            case Direction.Axis.Z -> pos.z();
        };
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
        return this.mapDirections(state, dir -> BlockUtils.transformFacing(transform, dir));
    }

    @Override
    public void transformStorage(BlockState state, IMultiStateCopycatBlockEntity be, StructureTransform transform) {
        be.getMaterialItemStorage().remapStorage(key -> CopycatBoardBlock.directionToProperty(BlockUtils.transformFacing(transform, CopycatBoardBlock.propertyToDirection(key))));
    }

    private static Direction propertyToDirection(String property) {
        return switch (property) {
            case "up" -> Direction.UP;
            case "down" -> Direction.DOWN;
            case "north" -> Direction.NORTH;
            case "south" -> Direction.SOUTH;
            case "east" -> Direction.EAST;
            case "west" -> Direction.WEST;
            default -> throw new IllegalStateException("Unexpected value: " + property);
        };
    }

    public static String directionToProperty(Direction direction) {
        return direction.getName().toLowerCase(Locale.ROOT);
    }

    private BlockState mapDirections(BlockState pState, Function<Direction, Direction> pDirectionalFunction) {
        BlockState blockstate = pState;
        for (Direction direction : Iterate.directions) {
            blockstate = (BlockState)blockstate.setValue((Property)CopycatBoardBlock.byDirection(pDirectionalFunction.apply(direction)), (Comparable)((Boolean)pState.getValue((Property)CopycatBoardBlock.byDirection(direction))));
        }
        return blockstate;
    }

    public static BooleanProperty byDirection(Direction direction) {
        return PROPERTY_BY_DIRECTION.get(direction);
    }

    public record FaceData(String property, Direction direction) {
    }
}

