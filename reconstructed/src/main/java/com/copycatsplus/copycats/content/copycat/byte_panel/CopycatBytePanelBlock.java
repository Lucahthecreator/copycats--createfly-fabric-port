/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap
 *  com.google.common.collect.ImmutableMap$Builder
 *  com.mojang.math.OctahedralGroup
 *  com.zurrtum.create.api.schematic.requirement.SpecialBlockItemRequirement
 *  com.zurrtum.create.content.contraptions.StructureTransform
 *  com.zurrtum.create.content.equipment.wrench.IWrenchable
 *  com.zurrtum.create.content.schematics.requirement.ItemRequirement
 *  javax.annotation.ParametersAreNonnullByDefault
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.core.Direction$Axis
 *  net.minecraft.core.Direction$AxisDirection
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
 *  net.minecraft.world.level.block.Mirror
 *  net.minecraft.world.level.block.Rotation
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.state.BlockBehaviour$Properties
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.StateDefinition$Builder
 *  net.minecraft.world.level.block.state.properties.BlockStateProperties
 *  net.minecraft.world.level.block.state.properties.BooleanProperty
 *  net.minecraft.world.level.block.state.properties.EnumProperty
 *  net.minecraft.world.level.block.state.properties.Property
 *  net.minecraft.world.level.pathfinder.PathComputationType
 *  net.minecraft.world.phys.Vec3
 *  net.minecraft.world.phys.shapes.BooleanOp
 *  net.minecraft.world.phys.shapes.CollisionContext
 *  net.minecraft.world.phys.shapes.Shapes
 *  net.minecraft.world.phys.shapes.VoxelShape
 *  org.jetbrains.annotations.NotNull
 *  org.joml.Vector2i
 */
package com.copycatsplus.copycats.content.copycat.byte_panel;

import com.copycatsplus.copycats.foundation.copycat.ICopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlockEntity;
import com.copycatsplus.copycats.foundation.copycat.multistate.WaterloggedMultiStateCopycatBlock;
import com.copycatsplus.copycats.utility.BlockFaceUtils;
import com.copycatsplus.copycats.utility.MathUtils;
import com.google.common.collect.ImmutableMap;
import com.mojang.math.OctahedralGroup;
import com.zurrtum.create.api.schematic.requirement.SpecialBlockItemRequirement;
import com.zurrtum.create.content.contraptions.StructureTransform;
import com.zurrtum.create.content.equipment.wrench.IWrenchable;
import com.zurrtum.create.content.schematics.requirement.ItemRequirement;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;

@ParametersAreNonnullByDefault
public class CopycatBytePanelBlock
extends WaterloggedMultiStateCopycatBlock
implements SpecialBlockItemRequirement {
    public static BooleanProperty BOTTOM_LEFT = BooleanProperty.create((String)"bottom_left");
    public static BooleanProperty BOTTOM_RIGHT = BooleanProperty.create((String)"bottom_right");
    public static BooleanProperty TOP_LEFT = BooleanProperty.create((String)"top_left");
    public static BooleanProperty TOP_RIGHT = BooleanProperty.create((String)"top_right");
    public static EnumProperty<Direction> FACING = BlockStateProperties.FACING;
    private final Function<BlockState, VoxelShape> shapesCache;
    private final ImmutableMap<FaceData, VoxelShape> partialFaceCache;

    public CopycatBytePanelBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.defaultBlockState().setValue((Property)BOTTOM_LEFT, (Comparable)Boolean.valueOf(false))).setValue((Property)BOTTOM_RIGHT, (Comparable)Boolean.valueOf(false))).setValue((Property)TOP_LEFT, (Comparable)Boolean.valueOf(false))).setValue((Property)TOP_RIGHT, (Comparable)Boolean.valueOf(false))).setValue(FACING, (Comparable)Direction.DOWN));
        this.shapesCache = this.getShapeForEachState(CopycatBytePanelBlock::calculateMultiFaceShape);
        ImmutableMap.Builder builder = ImmutableMap.builder();
        BlockState state = (BlockState)((BlockState)((BlockState)((BlockState)this.defaultBlockState().setValue((Property)BOTTOM_LEFT, (Comparable)Boolean.valueOf(true))).setValue((Property)BOTTOM_RIGHT, (Comparable)Boolean.valueOf(true))).setValue((Property)TOP_LEFT, (Comparable)Boolean.valueOf(true))).setValue((Property)TOP_RIGHT, (Comparable)Boolean.valueOf(true));
        for (String property : this.storageProperties()) {
            for (Direction facing : FACING.getPossibleValues()) {
                state = (BlockState)state.setValue(FACING, (Comparable)facing);
                for (Direction face : Direction.values()) {
                    builder.put((Object)new FaceData(property, facing, face), (Object)BlockFaceUtils.getPartialFaceShape(null, state, property, face));
                }
            }
        }
        this.partialFaceCache = builder.build();
    }

    @Override
    public String defaultProperty() {
        return BOTTOM_LEFT.getName();
    }

    @Override
    public Vec3i vectorScale(BlockState state) {
        return new Vec3i(2, 2, 2).relative(((Direction)state.getValue(FACING)).getAxis(), -1);
    }

    @Override
    public boolean partExists(BlockState state, String property) {
        if (property.equals(BOTTOM_LEFT.getName())) {
            return (Boolean)state.getValue((Property)BOTTOM_LEFT);
        }
        if (property.equals(BOTTOM_RIGHT.getName())) {
            return (Boolean)state.getValue((Property)BOTTOM_RIGHT);
        }
        if (property.equals(TOP_LEFT.getName())) {
            return (Boolean)state.getValue((Property)TOP_LEFT);
        }
        if (property.equals(TOP_RIGHT.getName())) {
            return (Boolean)state.getValue((Property)TOP_RIGHT);
        }
        return false;
    }

    @Override
    public Set<String> storageProperties() {
        return Set.of(BOTTOM_LEFT, BOTTOM_RIGHT, TOP_LEFT, TOP_RIGHT).stream().map(Property::getName).collect(Collectors.toSet());
    }

    @Override
    public int getColorIndex(String property) {
        if (property.equals(BOTTOM_LEFT.getName())) {
            return 0;
        }
        if (property.equals(BOTTOM_RIGHT.getName())) {
            return 1;
        }
        if (property.equals(TOP_LEFT.getName())) {
            return 1;
        }
        if (property.equals(TOP_RIGHT.getName())) {
            return 0;
        }
        throw new RuntimeException("Invalid property: " + property);
    }

    @Override
    public String getPropertyFromInteraction(BlockState state, BlockGetter level, Vec3i hitLocation, BlockPos blockPos, Direction facing, Vec3 unscaledHit) {
        Direction blockFacing = (Direction)state.getValue(FACING);
        Direction horizontal = CopycatBytePanelBlock.getHorizontal(blockFacing);
        Direction vertical = CopycatBytePanelBlock.getVertical(blockFacing);
        int horizontalHit = Mth.clamp((int)hitLocation.get(horizontal.getAxis()), (int)0, (int)1);
        if (horizontal.getAxisDirection() == Direction.AxisDirection.NEGATIVE) {
            horizontalHit = 1 - horizontalHit;
        }
        int verticalHit = Mth.clamp((int)hitLocation.get(vertical.getAxis()), (int)0, (int)1);
        if (vertical.getAxisDirection() == Direction.AxisDirection.NEGATIVE) {
            verticalHit = 1 - verticalHit;
        }
        return CopycatBytePanelBlock.getProperty(horizontalHit, verticalHit);
    }

    @Override
    public Vec3i getVectorFromProperty(BlockState state, String property) {
        Direction blockFacing = (Direction)state.getValue(FACING);
        Direction horizontal = CopycatBytePanelBlock.getHorizontal(blockFacing);
        Direction vertical = CopycatBytePanelBlock.getVertical(blockFacing);
        Vector2i vector = CopycatBytePanelBlock.getVector(property);
        return Vec3i.ZERO.relative(horizontal.getAxis(), horizontal.getAxisDirection() == Direction.AxisDirection.NEGATIVE ? 1 - vector.x : vector.x).relative(vertical.getAxis(), vertical.getAxisDirection() == Direction.AxisDirection.NEGATIVE ? 1 - vector.y : vector.y);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition((StateDefinition.Builder<Block, BlockState>)pBuilder.add(new Property[]{BOTTOM_LEFT, BOTTOM_RIGHT, TOP_LEFT, TOP_RIGHT, FACING}));
    }

    private static VoxelShape calculateMultiFaceShape(BlockState pState) {
        VoxelShape shape = Shapes.empty();
        Direction facing = (Direction)pState.getValue(FACING);
        CopycatBytePanelBlock block = (CopycatBytePanelBlock)pState.getBlock();
        Vec3 size = MathUtils.replaceAxis(new Vec3(8.0, 8.0, 8.0), facing.getAxis(), 3.0);
        Direction horizontal = CopycatBytePanelBlock.getHorizontal(facing);
        Direction vertical = CopycatBytePanelBlock.getVertical(facing);
        for (int i = 0; i < 2; ++i) {
            for (int j = 0; j < 2; ++j) {
                if (!block.partExists(pState, CopycatBytePanelBlock.getProperty(i, j))) continue;
                double h = horizontal.getAxisDirection() == Direction.AxisDirection.POSITIVE ? (double)(i * 8) : (double)(8 - i * 8);
                double v = vertical.getAxisDirection() == Direction.AxisDirection.POSITIVE ? (double)(j * 8) : (double)(8 - j * 8);
                double d = facing.getAxisDirection() == Direction.AxisDirection.POSITIVE ? 13.0 : 0.0;
                Vec3 min = MathUtils.replaceAxis(MathUtils.replaceAxis(MathUtils.replaceAxis(Vec3.ZERO, horizontal.getAxis(), h), vertical.getAxis(), v), facing.getAxis(), d);
                Vec3 max = min.add(size);
                shape = Shapes.joinUnoptimized((VoxelShape)shape, (VoxelShape)Block.box((double)min.x, (double)min.y, (double)min.z, (double)max.x, (double)max.y, (double)max.z), (BooleanOp)BooleanOp.OR);
            }
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
        return Objects.requireNonNull((VoxelShape)this.partialFaceCache.getOrDefault((Object)new FaceData(property, (Direction)state.getValue(FACING), face), (Object)Shapes.empty()));
    }

    public boolean isPathfindable(BlockState pState, PathComputationType pType) {
        return switch (pType) {
            case PathComputationType.LAND -> true;
            default -> false;
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
        BlockState interactionState = state.is((Object)this) ? state : (BlockState)stateForPlacement.setValue(FACING, (Comparable)context.getClickedFace().getOpposite());
        String property = this.getPropertyFromInteraction(interactionState, (BlockGetter)context.getLevel(), context.getClickedPos(), context.getClickLocation(), context.getClickedFace(), false);
        if (state.is((Object)this)) {
            if (!((Boolean)state.getValue((Property)CopycatBytePanelBlock.fromProperty(property))).booleanValue()) {
                return (BlockState)state.setValue((Property)CopycatBytePanelBlock.fromProperty(property), (Comparable)Boolean.valueOf(true));
            }
            return state;
        }
        return (BlockState)((BlockState)stateForPlacement.setValue(FACING, (Comparable)context.getClickedFace().getOpposite())).setValue((Property)CopycatBytePanelBlock.fromProperty(property), (Comparable)Boolean.valueOf(true));
    }

    public boolean canBeReplaced(@NotNull BlockState state, BlockPlaceContext context) {
        ItemStack itemstack = context.getItemInHand();
        if (!itemstack.is((Object)this.asItem())) {
            return false;
        }
        String property = this.getPropertyFromInteraction(state, (BlockGetter)context.getLevel(), context.getClickedPos(), context.getClickLocation(), context.getClickedFace(), false);
        return (Boolean)state.getValue((Property)CopycatBytePanelBlock.fromProperty(property)) == false;
    }

    @Override
    public InteractionResult onSneakWrenched(BlockState state, UseOnContext context) {
        this.onWrenched(state, context);
        int byteCount = 0;
        for (String property : this.storageProperties()) {
            if (!((Boolean)state.getValue((Property)CopycatBytePanelBlock.fromProperty(property))).booleanValue()) continue;
            ++byteCount;
        }
        if (byteCount <= 1) {
            return super.onSneakWrenched(state, context);
        }
        String property = this.getPropertyFromInteraction(state, (BlockGetter)context.getLevel(), context.getClickedPos(), context.getClickLocation(), context.getClickedFace(), true);
        Level level = context.getLevel();
        if (level instanceof ServerLevel) {
            ServerLevel world = (ServerLevel)level;
            Player player = context.getPlayer();
            BlockPos pos = context.getClickedPos();
            if (player != null) {
                List drops = Block.getDrops((BlockState)((BlockState)this.defaultBlockState().setValue((Property)CopycatBytePanelBlock.fromProperty(property), (Comparable)Boolean.valueOf(true))), (ServerLevel)world, (BlockPos)pos, (BlockEntity)world.getBlockEntity(pos), (Entity)player, (ItemInstance)context.getItemInHand());
                if (!player.isCreative()) {
                    for (ItemStack drop : drops) {
                        player.getInventory().placeItemBackInInventory(drop);
                    }
                }
            }
            BlockPos up = pos.relative(Direction.UP);
            world.setBlockAndUpdate(pos, (BlockState)state.setValue((Property)CopycatBytePanelBlock.fromProperty(property), (Comparable)Boolean.valueOf(false)));
            IWrenchable.playRemoveSound((Level)world, (BlockPos)pos);
        }
        return InteractionResult.SUCCESS;
    }

    public ItemRequirement getRequiredItems(BlockState state, BlockEntity blockEntity) {
        return IMultiStateCopycatBlock.getRequiredItemsForParts(state, BOTTOM_LEFT, BOTTOM_RIGHT, TOP_LEFT, TOP_RIGHT);
    }

    public boolean supportsExternalFaceHiding(BlockState state) {
        return true;
    }

    @Override
    public boolean hidesNeighborFace(BlockGetter level, BlockPos pos, BlockState state, BlockState neighborState, Direction dir) {
        return ICopycatBlock.hidesNeighborFace(level, pos, state, neighborState, dir);
    }

    private static BlockState flipFaceVertical(BlockState state) {
        return (BlockState)((BlockState)((BlockState)((BlockState)state.setValue((Property)TOP_LEFT, (Comparable)((Boolean)state.getValue((Property)BOTTOM_LEFT)))).setValue((Property)TOP_RIGHT, (Comparable)((Boolean)state.getValue((Property)BOTTOM_RIGHT)))).setValue((Property)BOTTOM_LEFT, (Comparable)((Boolean)state.getValue((Property)TOP_LEFT)))).setValue((Property)BOTTOM_RIGHT, (Comparable)((Boolean)state.getValue((Property)TOP_RIGHT)));
    }

    private static void flipFaceVertical(IMultiStateCopycatBlockEntity be) {
        be.getMaterialItemStorage().remapStorage(key -> {
            if (key.equals(BOTTOM_LEFT.getName())) {
                return TOP_LEFT.getName();
            }
            if (key.equals(BOTTOM_RIGHT.getName())) {
                return TOP_RIGHT.getName();
            }
            if (key.equals(TOP_LEFT.getName())) {
                return BOTTOM_LEFT.getName();
            }
            if (key.equals(TOP_RIGHT.getName())) {
                return BOTTOM_RIGHT.getName();
            }
            return key;
        });
    }

    private static BlockState flipFaceHorizontal(BlockState state) {
        return (BlockState)((BlockState)((BlockState)((BlockState)state.setValue((Property)BOTTOM_LEFT, (Comparable)((Boolean)state.getValue((Property)BOTTOM_RIGHT)))).setValue((Property)BOTTOM_RIGHT, (Comparable)((Boolean)state.getValue((Property)BOTTOM_LEFT)))).setValue((Property)TOP_LEFT, (Comparable)((Boolean)state.getValue((Property)TOP_RIGHT)))).setValue((Property)TOP_RIGHT, (Comparable)((Boolean)state.getValue((Property)TOP_LEFT)));
    }

    private static void flipFaceHorizontal(IMultiStateCopycatBlockEntity be) {
        be.getMaterialItemStorage().remapStorage(key -> {
            if (key.equals(BOTTOM_RIGHT.getName())) {
                return BOTTOM_LEFT.getName();
            }
            if (key.equals(BOTTOM_LEFT.getName())) {
                return BOTTOM_RIGHT.getName();
            }
            if (key.equals(TOP_RIGHT.getName())) {
                return TOP_LEFT.getName();
            }
            if (key.equals(TOP_LEFT.getName())) {
                return TOP_RIGHT.getName();
            }
            return key;
        });
    }

    private static BlockState rotateFaceCounterClockwise(BlockState state) {
        return (BlockState)((BlockState)((BlockState)((BlockState)state.setValue((Property)BOTTOM_LEFT, (Comparable)((Boolean)state.getValue((Property)TOP_LEFT)))).setValue((Property)TOP_LEFT, (Comparable)((Boolean)state.getValue((Property)TOP_RIGHT)))).setValue((Property)TOP_RIGHT, (Comparable)((Boolean)state.getValue((Property)BOTTOM_RIGHT)))).setValue((Property)BOTTOM_RIGHT, (Comparable)((Boolean)state.getValue((Property)BOTTOM_LEFT)));
    }

    private static void rotateFaceCounterClockwise(IMultiStateCopycatBlockEntity be) {
        be.getMaterialItemStorage().remapStorage(key -> {
            if (key.equals(TOP_LEFT.getName())) {
                return BOTTOM_LEFT.getName();
            }
            if (key.equals(TOP_RIGHT.getName())) {
                return TOP_LEFT.getName();
            }
            if (key.equals(BOTTOM_RIGHT.getName())) {
                return TOP_RIGHT.getName();
            }
            if (key.equals(BOTTOM_LEFT.getName())) {
                return BOTTOM_RIGHT.getName();
            }
            return key;
        });
    }

    private static BlockState rotateFaceClockwise(BlockState state) {
        return (BlockState)((BlockState)((BlockState)((BlockState)state.setValue((Property)BOTTOM_LEFT, (Comparable)((Boolean)state.getValue((Property)BOTTOM_RIGHT)))).setValue((Property)BOTTOM_RIGHT, (Comparable)((Boolean)state.getValue((Property)TOP_RIGHT)))).setValue((Property)TOP_RIGHT, (Comparable)((Boolean)state.getValue((Property)TOP_LEFT)))).setValue((Property)TOP_LEFT, (Comparable)((Boolean)state.getValue((Property)BOTTOM_LEFT)));
    }

    private static void rotateFaceClockwise(IMultiStateCopycatBlockEntity be) {
        be.getMaterialItemStorage().remapStorage(key -> {
            if (key.equals(BOTTOM_RIGHT.getName())) {
                return BOTTOM_LEFT.getName();
            }
            if (key.equals(TOP_RIGHT.getName())) {
                return BOTTOM_RIGHT.getName();
            }
            if (key.equals(TOP_LEFT.getName())) {
                return TOP_RIGHT.getName();
            }
            if (key.equals(BOTTOM_LEFT.getName())) {
                return TOP_LEFT.getName();
            }
            return key;
        });
    }

    @Override
    public BlockState transform(BlockState state, StructureTransform transform) {
        Direction facing;
        if (transform.mirror != null && transform.mirror != Mirror.NONE) {
            facing = (Direction)state.getValue(FACING);
            if (transform.mirror.rotation() == OctahedralGroup.INVERT_Y) {
                state = CopycatBytePanelBlock.flipFaceVertical(state);
                if (facing.getAxis().isVertical()) {
                    state = (BlockState)state.setValue(FACING, (Comparable)facing.getOpposite());
                }
            } else {
                state = facing.getAxis().isHorizontal() && transform.mirror.rotation().inverts(facing.getAxis()) ? (BlockState)CopycatBytePanelBlock.flipFaceHorizontal(state).setValue(FACING, (Comparable)facing.getOpposite()) : (facing.getAxis().isHorizontal() ? CopycatBytePanelBlock.flipFaceHorizontal(state) : (transform.mirror.rotation().inverts(Direction.Axis.X) ? CopycatBytePanelBlock.flipFaceHorizontal(state) : (transform.mirror.rotation().inverts(Direction.Axis.Z) ? CopycatBytePanelBlock.flipFaceVertical(state) : (BlockState)CopycatBytePanelBlock.flipFaceVertical(state).setValue(FACING, (Comparable)facing.getOpposite()))));
            }
        }
        if (transform.rotationAxis != null && transform.rotation != Rotation.NONE) {
            facing = (Direction)state.getValue(FACING);
            if (transform.rotationAxis.isVertical() && facing.getAxis().isHorizontal()) {
                state = (BlockState)state.setValue(FACING, (Comparable)transform.rotateFacing(facing));
            } else if (transform.rotationAxis == facing.getAxis()) {
                if (facing.getAxisDirection() == Direction.AxisDirection.NEGATIVE) {
                    for (int i = 0; i < transform.rotation.ordinal(); ++i) {
                        state = CopycatBytePanelBlock.rotateFaceClockwise(state);
                    }
                } else {
                    for (int i = 0; i < transform.rotation.ordinal(); ++i) {
                        state = CopycatBytePanelBlock.rotateFaceCounterClockwise(state);
                    }
                }
            } else if (facing.getAxis().isVertical()) {
                if (transform.rotation == Rotation.CLOCKWISE_180) {
                    state = transform.rotationAxis == Direction.Axis.X ? CopycatBytePanelBlock.flipFaceVertical(state) : CopycatBytePanelBlock.flipFaceHorizontal(state);
                    state = CopycatBytePanelBlock.flipFaceVertical(state);
                } else {
                    state = transform.rotationAxis == Direction.Axis.X ? (facing == Direction.DOWN && transform.rotation == Rotation.CLOCKWISE_90 || facing == Direction.UP && transform.rotation == Rotation.COUNTERCLOCKWISE_90 ? (BlockState)state.setValue(FACING, (Comparable)Direction.SOUTH) : (BlockState)CopycatBytePanelBlock.flipFaceHorizontal(CopycatBytePanelBlock.flipFaceVertical(state)).setValue(FACING, (Comparable)Direction.NORTH)) : (facing == Direction.DOWN ? (transform.rotation == Rotation.CLOCKWISE_90 ? (BlockState)CopycatBytePanelBlock.rotateFaceCounterClockwise(state).setValue(FACING, (Comparable)Direction.WEST) : (BlockState)CopycatBytePanelBlock.rotateFaceClockwise(state).setValue(FACING, (Comparable)Direction.EAST)) : (transform.rotation == Rotation.CLOCKWISE_90 ? (BlockState)CopycatBytePanelBlock.rotateFaceCounterClockwise(state).setValue(FACING, (Comparable)Direction.EAST) : (BlockState)CopycatBytePanelBlock.rotateFaceClockwise(state).setValue(FACING, (Comparable)Direction.WEST)));
                }
            } else {
                state = transform.rotation == Rotation.CLOCKWISE_180 ? (BlockState)CopycatBytePanelBlock.flipFaceVertical(state).setValue(FACING, (Comparable)facing.getOpposite()) : (transform.rotationAxis == Direction.Axis.X ? (facing == Direction.SOUTH ? (transform.rotation == Rotation.CLOCKWISE_90 ? (BlockState)state.setValue(FACING, (Comparable)Direction.UP) : (BlockState)state.setValue(FACING, (Comparable)Direction.DOWN)) : (transform.rotation == Rotation.CLOCKWISE_90 ? (BlockState)CopycatBytePanelBlock.flipFaceHorizontal(CopycatBytePanelBlock.flipFaceVertical(state)).setValue(FACING, (Comparable)Direction.DOWN) : (BlockState)CopycatBytePanelBlock.flipFaceHorizontal(CopycatBytePanelBlock.flipFaceVertical(state)).setValue(FACING, (Comparable)Direction.UP))) : (facing == Direction.EAST ? (transform.rotation == Rotation.CLOCKWISE_90 ? (BlockState)CopycatBytePanelBlock.rotateFaceCounterClockwise(state).setValue(FACING, (Comparable)Direction.DOWN) : (BlockState)CopycatBytePanelBlock.rotateFaceClockwise(state).setValue(FACING, (Comparable)Direction.UP)) : (transform.rotation == Rotation.CLOCKWISE_90 ? (BlockState)CopycatBytePanelBlock.rotateFaceCounterClockwise(state).setValue(FACING, (Comparable)Direction.UP) : (BlockState)CopycatBytePanelBlock.rotateFaceClockwise(state).setValue(FACING, (Comparable)Direction.DOWN))));
            }
        }
        return state;
    }

    @Override
    public void transformStorage(BlockState state, IMultiStateCopycatBlockEntity be, StructureTransform transform) {
        Direction facing;
        if (transform.mirror != null && transform.mirror != Mirror.NONE) {
            facing = (Direction)state.getValue(FACING);
            if (transform.mirror.rotation() == OctahedralGroup.INVERT_Y) {
                CopycatBytePanelBlock.flipFaceVertical(be);
            } else if (facing.getAxis().isHorizontal() && transform.mirror.rotation().inverts(facing.getAxis())) {
                CopycatBytePanelBlock.flipFaceHorizontal(be);
            } else if (facing.getAxis().isHorizontal()) {
                CopycatBytePanelBlock.flipFaceHorizontal(be);
            } else if (transform.mirror.rotation().inverts(Direction.Axis.X)) {
                CopycatBytePanelBlock.flipFaceHorizontal(be);
            } else if (transform.mirror.rotation().inverts(Direction.Axis.Z)) {
                CopycatBytePanelBlock.flipFaceVertical(be);
            } else {
                CopycatBytePanelBlock.flipFaceVertical(be);
            }
        }
        if (transform.rotationAxis != null && transform.rotation != Rotation.NONE) {
            facing = (Direction)state.getValue(FACING);
            if (!transform.rotationAxis.isVertical() || !facing.getAxis().isHorizontal()) {
                if (transform.rotationAxis == facing.getAxis()) {
                    if (facing.getAxisDirection() == Direction.AxisDirection.NEGATIVE) {
                        for (int i = 0; i < transform.rotation.ordinal(); ++i) {
                            CopycatBytePanelBlock.rotateFaceClockwise(be);
                        }
                    } else {
                        for (int i = 0; i < transform.rotation.ordinal(); ++i) {
                            CopycatBytePanelBlock.rotateFaceCounterClockwise(be);
                        }
                    }
                } else if (facing.getAxis().isVertical()) {
                    if (transform.rotation == Rotation.CLOCKWISE_180) {
                        if (transform.rotationAxis == Direction.Axis.X) {
                            CopycatBytePanelBlock.flipFaceVertical(be);
                        } else {
                            CopycatBytePanelBlock.flipFaceHorizontal(be);
                        }
                        CopycatBytePanelBlock.flipFaceVertical(be);
                    } else if (transform.rotationAxis == Direction.Axis.X) {
                        if (!(facing == Direction.UP && transform.rotation == Rotation.CLOCKWISE_90 || facing == Direction.DOWN && transform.rotation == Rotation.COUNTERCLOCKWISE_90)) {
                            CopycatBytePanelBlock.flipFaceVertical(be);
                            CopycatBytePanelBlock.flipFaceHorizontal(be);
                        }
                    } else if (transform.rotation == Rotation.CLOCKWISE_90) {
                        CopycatBytePanelBlock.rotateFaceCounterClockwise(be);
                    } else {
                        CopycatBytePanelBlock.rotateFaceClockwise(be);
                    }
                } else if (transform.rotation == Rotation.CLOCKWISE_180) {
                    CopycatBytePanelBlock.flipFaceVertical(be);
                } else if (transform.rotationAxis == Direction.Axis.X) {
                    if (facing != Direction.SOUTH) {
                        CopycatBytePanelBlock.flipFaceVertical(be);
                        CopycatBytePanelBlock.flipFaceHorizontal(be);
                    }
                } else if (transform.rotation == Rotation.CLOCKWISE_90) {
                    CopycatBytePanelBlock.rotateFaceCounterClockwise(be);
                } else {
                    CopycatBytePanelBlock.rotateFaceClockwise(be);
                }
            }
        }
    }

    public static BooleanProperty fromProperty(String property) {
        if (property.equals(BOTTOM_LEFT.getName())) {
            return BOTTOM_LEFT;
        }
        if (property.equals(BOTTOM_RIGHT.getName())) {
            return BOTTOM_RIGHT;
        }
        if (property.equals(TOP_LEFT.getName())) {
            return TOP_LEFT;
        }
        if (property.equals(TOP_RIGHT.getName())) {
            return TOP_RIGHT;
        }
        throw new RuntimeException("Invalid property: " + property);
    }

    public static String getProperty(int horizontal, int vertical) {
        if (horizontal == 0 && vertical == 0) {
            return BOTTOM_RIGHT.getName();
        }
        if (horizontal == 1 && vertical == 0) {
            return BOTTOM_LEFT.getName();
        }
        if (horizontal == 0 && vertical == 1) {
            return TOP_RIGHT.getName();
        }
        if (horizontal == 1 && vertical == 1) {
            return TOP_LEFT.getName();
        }
        throw new RuntimeException("Invalid horizontal and vertical values: " + horizontal + ", " + vertical);
    }

    public static Vector2i getVector(String property) {
        if (property.equals(BOTTOM_RIGHT.getName())) {
            return new Vector2i(0, 0);
        }
        if (property.equals(BOTTOM_LEFT.getName())) {
            return new Vector2i(1, 0);
        }
        if (property.equals(TOP_RIGHT.getName())) {
            return new Vector2i(0, 1);
        }
        if (property.equals(TOP_LEFT.getName())) {
            return new Vector2i(1, 1);
        }
        throw new RuntimeException("Invalid property: " + property);
    }

    public static Direction getHorizontal(Direction facing) {
        return switch (facing) {
            default -> throw new MatchException(null, null);
            case Direction.DOWN -> Direction.EAST;
            case Direction.UP -> Direction.EAST;
            case Direction.NORTH -> Direction.WEST;
            case Direction.SOUTH -> Direction.EAST;
            case Direction.EAST -> Direction.NORTH;
            case Direction.WEST -> Direction.SOUTH;
        };
    }

    public static Direction getVertical(Direction facing) {
        return switch (facing) {
            default -> throw new MatchException(null, null);
            case Direction.DOWN -> Direction.SOUTH;
            case Direction.UP -> Direction.NORTH;
            case Direction.NORTH -> Direction.UP;
            case Direction.SOUTH -> Direction.UP;
            case Direction.EAST -> Direction.UP;
            case Direction.WEST -> Direction.UP;
        };
    }

    public record FaceData(String property, Direction facing, Direction direction) {
    }
}

