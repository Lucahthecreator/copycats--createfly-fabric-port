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
import java.util.Objects;
import java.util.function.Predicate;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.core.Direction.Plane;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

@ParametersAreNonnullByDefault
public class CopycatHalfPanelBlock extends CCWaterloggedCopycatBlock implements IStateType {
    public static final EnumProperty<Direction> FACING = BlockStateProperties.FACING;
    public static final EnumProperty<Direction> OFFSET = EnumProperty.create("offset", Direction.class, Plane.HORIZONTAL);
    private static final int placementHelperId = PlacementHelpers.register(new CopycatHalfPanelBlock.PlacementHelper());

    public CopycatHalfPanelBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH).setValue(OFFSET, Direction.NORTH));
    }

    @NotNull
    @Override
    protected InteractionResult useItemOn(
        ItemStack heldStack,
        @NotNull BlockState state,
        @NotNull Level world,
        @NotNull BlockPos pos,
        @NotNull Player player,
        @NotNull InteractionHand hand,
        @NotNull BlockHitResult ray
    ) {
        return InteractionUtils.sequential(
            () -> InteractionUtils.usePlacementHelper(placementHelperId, state, world, pos, player, hand, ray),
            () -> super.useItemOn(heldStack, state, world, pos, player, hand, ray)
        );
    }

    @Override
    public boolean isPathfindable(BlockState pState, PathComputationType pType) {
        return false;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState stateForPlacement = super.getStateForPlacement(context);
        if (stateForPlacement == null) {
            return null;
        } else {
            Direction facing = context.getClickedFace().getOpposite();
            double offset1;
            double offset2;
            switch (facing.getAxis()) {
                case X:
                    offset1 = context.getClickLocation().y - context.getClickedPos().getY();
                    offset2 = context.getClickLocation().z - context.getClickedPos().getZ();
                    break;
                case Y:
                    offset1 = context.getClickLocation().x - context.getClickedPos().getX();
                    offset2 = context.getClickLocation().z - context.getClickedPos().getZ();
                    break;
                case Z:
                    offset1 = context.getClickLocation().x - context.getClickedPos().getX();
                    offset2 = context.getClickLocation().y - context.getClickedPos().getY();
                    break;
                default:
                    offset1 = 0.0;
                    offset2 = 0.0;
            }

            Direction offset;
            if (Math.abs(offset1 - 0.5) > Math.abs(offset2 - 0.5)) {
                offset = Direction.fromAxisAndDirection(Axis.X, offset1 > 0.5 ? AxisDirection.POSITIVE : AxisDirection.NEGATIVE);
            } else {
                offset = Direction.fromAxisAndDirection(Axis.Z, offset2 > 0.5 ? AxisDirection.POSITIVE : AxisDirection.NEGATIVE);
            }

            return stateForPlacement.setValue(FACING, facing).setValue(OFFSET, offset);
        }
    }

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder.add(FACING, OFFSET));
    }

    @NotNull
    @Override
    public VoxelShape getShape(BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        return CCShapes.HALF_PANEL.get(pState.getValue(FACING)).get(pState.getValue(OFFSET)).toShape();
    }

    public boolean supportsExternalFaceHiding(BlockState state) {
        return true;
    }

    public boolean hidesNeighborFace(BlockGetter level, BlockPos pos, BlockState state, BlockState neighborState, Direction dir) {
        return ICopycatBlock.hidesNeighborFace(level, pos, state, neighborState, dir);
    }

    @Override
    public BlockState transform(BlockState state, StructureTransform transform) {
        Direction facing = state.getValue(FACING);
        Direction offset = state.getValue(OFFSET);
        Vec3i offsetNormal = transform.applyWithoutOffset(BlockPos.ZERO.offset(getOffsetFacing(facing, offset).getUnitVec3i()));
        Direction newFacing = BlockUtils.transformFacing(transform, facing);
        Vec3i facingNormal = newFacing.getUnitVec3i();
        if (offsetNormal.getY() != 0) {
            if (offsetNormal.getX() == 0 && facingNormal.getX() != 0) {
                offsetNormal = new Vec3i(offsetNormal.getY(), offsetNormal.getX(), offsetNormal.getZ());
            } else {
                offsetNormal = new Vec3i(offsetNormal.getX(), offsetNormal.getZ(), offsetNormal.getY());
            }
        }

        return state.setValue(FACING, newFacing)
            .setValue(
                OFFSET,
                Objects.requireNonNull(Direction.getApproximateNearest((float)offsetNormal.getX(), (float)offsetNormal.getY(), (float)offsetNormal.getZ()))
            );
    }

    public static Direction getOffsetFacing(Direction facing, Direction offset) {
        if (offset.getAxis().isVertical()) {
            throw new IllegalArgumentException("offset must be a horizontal direction.");
        } else {
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
    }

    public static Axis getOffsetAxis(Direction facing, Direction offset) {
        Direction offsetFacing = getOffsetFacing(facing, offset);
        if (facing.getAxis().isVertical()) {
            return offsetFacing.getClockWise().getAxis();
        } else {
            return offsetFacing.getAxis().isVertical() ? facing.getClockWise().getAxis() : Axis.Y;
        }
    }

    private static class PlacementHelper extends PoleHelper<Direction> {
        private PlacementHelper() {
            super(
                state -> state.getBlock() instanceof CopycatHalfPanelBlock,
                state -> CopycatHalfPanelBlock.getOffsetAxis(state.getValue(CopycatHalfPanelBlock.FACING), state.getValue(CopycatHalfPanelBlock.OFFSET)),
                CopycatHalfPanelBlock.FACING
            );
        }

        @Override
        public Predicate<ItemStack> getItemPredicate() {
            return i -> i.getItem() instanceof BlockItem && ((BlockItem)i.getItem()).getBlock() instanceof CopycatHalfPanelBlock;
        }

        @Override
        public PlacementOffset getOffset(Player player, Level world, BlockState state, BlockPos pos, BlockHitResult ray) {
            for (Direction dir : IPlacementHelper.orderedByDistance(pos, ray.getLocation(), dirx -> dirx.getAxis() == this.axisFunction.apply(state))) {
                int range = AllConfigs.server().equipment.placementAssistRange.get();
                if (player != null) {
                    AttributeInstance reach = InteractionUtils.getPlayerReach(player);
                    if (reach != null && reach.hasModifier(ExtendoGripItem.singleRangeAttributeModifier.id())) {
                        range += 4;
                    }
                }

                int poles = this.attachedPoles(world, pos, dir);
                if (poles < range) {
                    BlockPos newPos = pos.relative(dir, poles + 1);
                    BlockState newState = world.getBlockState(newPos);
                    if (newState.canBeReplaced()) {
                        return PlacementOffset.success(
                            newPos,
                            bState -> bState.setValue(this.property, state.getValue(this.property))
                                .setValue(CopycatHalfPanelBlock.OFFSET, state.getValue(CopycatHalfPanelBlock.OFFSET))
                        );
                    }
                }
            }

            return PlacementOffset.fail();
        }
    }
}
