/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.zurrtum.create.catnip.placement.IPlacementHelper
 *  com.zurrtum.create.catnip.placement.PlacementHelpers
 *  com.zurrtum.create.catnip.placement.PlacementOffset
 *  com.zurrtum.create.content.contraptions.StructureTransform
 *  com.zurrtum.create.content.equipment.extendoGrip.ExtendoGripItem
 *  com.zurrtum.create.foundation.placement.PoleHelper
 *  com.zurrtum.create.infrastructure.config.AllConfigs
 *  javax.annotation.ParametersAreNonnullByDefault
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.core.Direction$Axis
 *  net.minecraft.core.Direction$AxisDirection
 *  net.minecraft.core.Direction$Plane
 *  net.minecraft.core.Vec3i
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.InteractionResult
 *  net.minecraft.world.entity.ai.attributes.AttributeInstance
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.BlockItem
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.context.BlockPlaceContext
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.state.BlockBehaviour$Properties
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.StateDefinition$Builder
 *  net.minecraft.world.level.block.state.properties.BlockStateProperties
 *  net.minecraft.world.level.block.state.properties.EnumProperty
 *  net.minecraft.world.level.block.state.properties.Property
 *  net.minecraft.world.level.pathfinder.PathComputationType
 *  net.minecraft.world.phys.BlockHitResult
 *  net.minecraft.world.phys.Vec3
 *  net.minecraft.world.phys.shapes.CollisionContext
 *  net.minecraft.world.phys.shapes.VoxelShape
 *  org.jetbrains.annotations.NotNull
 */
package com.copycatsplus.copycats.content.copycat.half_panel;

import com.copycatsplus.copycats.CCShapes;
import com.copycatsplus.copycats.foundation.copycat.CCWaterloggedCopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.IStateType;
import com.copycatsplus.copycats.utility.BlockUtils;
import com.copycatsplus.copycats.utility.InteractionUtils;
import com.zurrtum.create.catnip.placement.IPlacementHelper;
import com.zurrtum.create.catnip.placement.PlacementHelpers;
import com.zurrtum.create.catnip.placement.PlacementOffset;
import com.zurrtum.create.content.contraptions.StructureTransform;
import com.zurrtum.create.content.equipment.extendoGrip.ExtendoGripItem;
import com.zurrtum.create.foundation.placement.PoleHelper;
import com.zurrtum.create.infrastructure.config.AllConfigs;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

@ParametersAreNonnullByDefault
public class CopycatHalfPanelBlock
extends CCWaterloggedCopycatBlock
implements IStateType {
    public static final EnumProperty<Direction> FACING = BlockStateProperties.FACING;
    public static final EnumProperty<Direction> OFFSET = EnumProperty.create((String)"offset", Direction.class, (Predicate)Direction.Plane.HORIZONTAL);
    private static final int placementHelperId = PlacementHelpers.register((IPlacementHelper)new PlacementHelper());

    public CopycatHalfPanelBlock(BlockBehaviour.Properties pProperties) {
        super(pProperties);
        this.registerDefaultState((BlockState)((BlockState)this.defaultBlockState().setValue(FACING, (Comparable)Direction.NORTH)).setValue(OFFSET, (Comparable)Direction.NORTH));
    }

    @Override
    @NotNull
    protected InteractionResult useItemOn(ItemStack heldStack, @NotNull BlockState state, @NotNull Level world, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult ray) {
        return InteractionUtils.sequential(() -> InteractionUtils.usePlacementHelper(placementHelperId, state, world, pos, player, hand, ray), () -> super.useItemOn(heldStack, state, world, pos, player, hand, ray));
    }

    public boolean isPathfindable(BlockState pState, PathComputationType pType) {
        return false;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        double offset1;
        BlockState stateForPlacement = super.getStateForPlacement(context);
        if (stateForPlacement == null) {
            return null;
        }
        Direction facing = context.getClickedFace().getOpposite();
        double offset2 = switch (facing.getAxis()) {
            case Direction.Axis.X -> {
                offset1 = context.getClickLocation().y - (double)context.getClickedPos().getY();
                yield context.getClickLocation().z - (double)context.getClickedPos().getZ();
            }
            case Direction.Axis.Y -> {
                offset1 = context.getClickLocation().x - (double)context.getClickedPos().getX();
                yield context.getClickLocation().z - (double)context.getClickedPos().getZ();
            }
            case Direction.Axis.Z -> {
                offset1 = context.getClickLocation().x - (double)context.getClickedPos().getX();
                yield context.getClickLocation().y - (double)context.getClickedPos().getY();
            }
            default -> {
                offset1 = 0.0;
                yield 0.0;
            }
        };
        Direction offset = Math.abs(offset1 - 0.5) > Math.abs(offset2 - 0.5) ? Direction.fromAxisAndDirection((Direction.Axis)Direction.Axis.X, (Direction.AxisDirection)(offset1 > 0.5 ? Direction.AxisDirection.POSITIVE : Direction.AxisDirection.NEGATIVE)) : Direction.fromAxisAndDirection((Direction.Axis)Direction.Axis.Z, (Direction.AxisDirection)(offset2 > 0.5 ? Direction.AxisDirection.POSITIVE : Direction.AxisDirection.NEGATIVE));
        return (BlockState)((BlockState)stateForPlacement.setValue(FACING, (Comparable)facing)).setValue(OFFSET, (Comparable)offset);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition((StateDefinition.Builder<Block, BlockState>)pBuilder.add(new Property[]{FACING, OFFSET}));
    }

    @NotNull
    public VoxelShape getShape(BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        return CCShapes.HALF_PANEL.get(pState.getValue(FACING)).get(pState.getValue(OFFSET)).toShape();
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
        Direction facing = (Direction)state.getValue(FACING);
        Direction offset = (Direction)state.getValue(OFFSET);
        BlockPos offsetNormal = transform.applyWithoutOffset(new BlockPos(CopycatHalfPanelBlock.getOffsetFacing(facing, offset).getUnitVec3i()));
        Direction newFacing = BlockUtils.transformFacing(transform, facing);
        Vec3i facingNormal = newFacing.getUnitVec3i();
        if (offsetNormal.getY() != 0) {
            offsetNormal = offsetNormal.getX() == 0 && facingNormal.getX() != 0 ? new Vec3i(offsetNormal.getY(), offsetNormal.getX(), offsetNormal.getZ()) : new Vec3i(offsetNormal.getX(), offsetNormal.getZ(), offsetNormal.getY());
        }
        return (BlockState)((BlockState)state.setValue(FACING, (Comparable)newFacing)).setValue(OFFSET, (Comparable)Objects.requireNonNull(Direction.getApproximateNearest((float)offsetNormal.getX(), (float)offsetNormal.getY(), (float)offsetNormal.getZ())));
    }

    public static Direction getOffsetFacing(Direction facing, Direction offset) {
        if (offset.getAxis().isVertical()) {
            throw new IllegalArgumentException("offset must be a horizontal direction.");
        }
        Vec3i facingNormal = facing.getUnitVec3i();
        Vec3i offsetNormal = offset.getUnitVec3i();
        if (facingNormal.getX() != 0 && offsetNormal.getX() != 0) {
            offsetNormal = new Vec3i(offsetNormal.getY(), offsetNormal.getX(), offsetNormal.getZ());
        }
        if (facingNormal.getZ() != 0 && offsetNormal.getZ() != 0) {
            offsetNormal = new Vec3i(offsetNormal.getX(), offsetNormal.getZ(), offsetNormal.getY());
        }
        return Objects.requireNonNull(Direction.getApproximateNearest((float)offsetNormal.getX(), (float)offsetNormal.getY(), (float)offsetNormal.getZ()));
    }

    public static Direction.Axis getOffsetAxis(Direction facing, Direction offset) {
        Direction offsetFacing = CopycatHalfPanelBlock.getOffsetFacing(facing, offset);
        if (facing.getAxis().isVertical()) {
            return offsetFacing.getClockWise().getAxis();
        }
        if (offsetFacing.getAxis().isVertical()) {
            return facing.getClockWise().getAxis();
        }
        return Direction.Axis.Y;
    }

    private static class PlacementHelper
    extends PoleHelper<Direction> {
        private PlacementHelper() {
            super(state -> state.getBlock() instanceof CopycatHalfPanelBlock, state -> CopycatHalfPanelBlock.getOffsetAxis((Direction)state.getValue(FACING), (Direction)state.getValue(OFFSET)), FACING);
        }

        public Predicate<ItemStack> getItemPredicate() {
            return i -> i.getItem() instanceof BlockItem && ((BlockItem)i.getItem()).getBlock() instanceof CopycatHalfPanelBlock;
        }

        public PlacementOffset getOffset(Player player, Level world, BlockState state, BlockPos pos, BlockHitResult ray) {
            List directions = IPlacementHelper.orderedByDistance((BlockPos)pos, (Vec3)ray.getLocation(), dir -> dir.getAxis() == this.axisFunction.apply(state));
            for (Direction dir2 : directions) {
                BlockPos newPos;
                BlockState newState;
                int poles;
                AttributeInstance reach;
                int range = (Integer)AllConfigs.server().equipment.placementAssistRange.get();
                if (player != null && (reach = InteractionUtils.getPlayerReach(player)) != null && reach.hasModifier(ExtendoGripItem.singleRangeAttributeModifier.id())) {
                    range += 4;
                }
                if ((poles = this.attachedPoles(world, pos, dir2)) >= range || !(newState = world.getBlockState(newPos = pos.relative(dir2, poles + 1))).canBeReplaced()) continue;
                return PlacementOffset.success((Vec3i)newPos, bState -> (BlockState)((BlockState)bState.setValue(this.property, (Comparable)((Direction)state.getValue(this.property)))).setValue(OFFSET, (Comparable)((Direction)state.getValue(OFFSET))));
            }
            return PlacementOffset.fail();
        }
    }
}

