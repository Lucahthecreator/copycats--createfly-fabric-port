package com.copycatsplus.copycats.content.copycat.base;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.contraptions.ITransformableBlock;
import com.simibubi.create.content.contraptions.StructureTransform;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.*;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

/**
 * Indicates that a block functions as a copycat but is not a subclass of {@link CCCopycatBlock}.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public interface ICopycatBlock extends IWrenchable, IStateType, ITransformableBlock {

    @Nullable
    default ICopycatBlockEntity getCopycatBlockEntity(BlockGetter worldIn, BlockPos pos) {
        BlockEntity blockEntity = worldIn.getBlockEntity(pos);

        if (blockEntity == null)
            return null;
        if (!(blockEntity instanceof ICopycatBlockEntity copycatBE))
            return null;

        return copycatBE;
    }

    default boolean canToggleCT(BlockState state, BlockAndTintGetter level, BlockPos pos) {
        return true;
    }

    default boolean isCTEnabled(BlockState state, BlockAndTintGetter level, BlockPos pos) {
        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof ICopycatBlockEntity fbe))
            return true;
        if (!canToggleCT(state, level, pos))
            return true;
        return fbe.isCTEnabled();
    }

    default InteractionResult toggleCT(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pPlayer.isShiftKeyDown() && pPlayer.getItemInHand(pHand).equals(ItemStack.EMPTY)) {
            BlockEntity be = pLevel.getBlockEntity(pPos);
            if (!(be instanceof ICopycatBlockEntity fbe))
                return InteractionResult.PASS;
            if (!canToggleCT(pState, pLevel, pPos))
                return InteractionResult.PASS;
            fbe.setCTEnabled(!fbe.isCTEnabled());
            fbe.redraw();
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    default InteractionResult onSneakWrenched(BlockState state, UseOnContext context) {
        onWrenched(state, context);
        return IWrenchable.super.onSneakWrenched(state, context);
    }

    @Override
    default InteractionResult onWrenched(BlockState state, UseOnContext context) {
        ICopycatBlockEntity copycatBE = getCopycatBlockEntity(context.getLevel(), context.getClickedPos());
        if (copycatBE == null)
            return InteractionResult.PASS;
        ItemStack consumedItem = copycatBE.getConsumedItem();
        if (!copycatBE.hasCustomMaterial())
            return InteractionResult.PASS;
        Player player = context.getPlayer();
        if (!player.isCreative())
            player.getInventory()
                    .placeItemBackInInventory(consumedItem);
        context.getLevel()
                .levelEvent(2001, context.getClickedPos(), Block.getId(getMaterial(context.getLevel(), context.getClickedPos())));
        copycatBE.setMaterial(AllBlocks.COPYCAT_BASE.getDefaultState());
        copycatBE.setConsumedItem(ItemStack.EMPTY);
        return InteractionResult.SUCCESS;
    }

    @Nullable
    default BlockState getAcceptedBlockState(Level pLevel, BlockPos pPos, ItemStack item, Direction face) {
        if (!(item.getItem() instanceof BlockItem bi))
            return null;

        Block block = bi.getBlock();
        if (block instanceof ICopycatBlock)
            return null;

        BlockState appliedState = block.defaultBlockState();
        boolean hardCodedAllow = isAcceptedRegardless(appliedState);

        if (!AllTags.AllBlockTags.COPYCAT_ALLOW.matches(block) && !hardCodedAllow) {

            if (AllTags.AllBlockTags.COPYCAT_DENY.matches(block))
                return null;
            if (block instanceof EntityBlock)
                return null;
            if (block instanceof StairBlock)
                return null;

            if (pLevel != null) {
                VoxelShape shape = appliedState.getShape(pLevel, pPos);
                if (shape.isEmpty() || !shape.bounds()
                        .equals(Shapes.block()
                                .bounds()))
                    return null;

                VoxelShape collisionShape = appliedState.getCollisionShape(pLevel, pPos);
                if (collisionShape.isEmpty())
                    return null;
            }
        }

        if (face != null) {
            Direction.Axis axis = face.getAxis();

            if (appliedState.hasProperty(BlockStateProperties.FACING))
                appliedState = appliedState.setValue(BlockStateProperties.FACING, face);
            if (appliedState.hasProperty(BlockStateProperties.HORIZONTAL_FACING) && axis != Direction.Axis.Y)
                appliedState = appliedState.setValue(BlockStateProperties.HORIZONTAL_FACING, face);
            if (appliedState.hasProperty(BlockStateProperties.AXIS))
                appliedState = appliedState.setValue(BlockStateProperties.AXIS, axis);
            if (appliedState.hasProperty(BlockStateProperties.HORIZONTAL_AXIS) && axis != Direction.Axis.Y)
                appliedState = appliedState.setValue(BlockStateProperties.HORIZONTAL_AXIS, axis);
        }

        return appliedState;
    }

    default boolean isAcceptedRegardless(BlockState material) {
        return false;
    }

    default BlockState prepareMaterial(Level pLevel, BlockPos pPos, BlockState pState, Player pPlayer,
                                       InteractionHand pHand, BlockHitResult pHit, BlockState material) {
        return material;
    }

    default InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult ray) {
        // prioritize wrench interactions over others
        if (player.getItemInHand(hand).is(AllTags.AllItemTags.WRENCH.tag)) {
            InteractionResult result = AllItems.WRENCH.get().useOn(new UseOnContext(player, hand, ray));
            if (result.consumesAction())
                return result;
        }

        InteractionResult result = toggleCT(state, world, pos, player, hand, ray);
        if (result.consumesAction())
            return result;

        if (player == null || !player.mayBuild())
            return InteractionResult.PASS;

        Direction face = ray.getDirection();
        ItemStack itemInHand = player.getItemInHand(hand);
        BlockState materialIn = getAcceptedBlockState(world, pos, itemInHand, face);

        if (materialIn != null)
            materialIn = prepareMaterial(world, pos, state, player, hand, ray, materialIn);
        if (materialIn == null)
            return InteractionResult.PASS;

        BlockState material = materialIn;
        ICopycatBlockEntity copycatBE = getCopycatBlockEntity(world, pos);
        if (copycatBE == null)
            return InteractionResult.PASS;
        if (copycatBE.getMaterial()
                .is(material.getBlock())) {
            if (!copycatBE.cycleMaterial())
                return InteractionResult.PASS;
            copycatBE.getLevel()
                    .playSound(null, copycatBE.getBlockPos(), SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS, .75f,
                            .95f);
            return InteractionResult.SUCCESS;
        }
        if (copycatBE.hasCustomMaterial())
            return InteractionResult.PASS;
        if (world.isClientSide())
            return InteractionResult.SUCCESS;

        copycatBE.setMaterial(material);
        copycatBE.setConsumedItem(itemInHand);
        copycatBE.getLevel()
                .playSound(null, copycatBE.getBlockPos(), material.getSoundType()
                        .getPlaceSound(), SoundSource.BLOCKS, 1, .75f);

        if (player.isCreative())
            return InteractionResult.SUCCESS;

        itemInHand.shrink(1);
        if (itemInHand.isEmpty())
            player.setItemInHand(hand, ItemStack.EMPTY);
        return InteractionResult.SUCCESS;
    }

    default void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if (placer == null)
            return;
        ItemStack offhandItem = placer.getItemInHand(InteractionHand.OFF_HAND);
        BlockState appliedState =
                getAcceptedBlockState(worldIn, pos, offhandItem, Direction.orderedByNearest(placer)[0]);

        if (appliedState == null)
            return;
        ICopycatBlockEntity copycatBE = getCopycatBlockEntity(worldIn, pos);
        if (copycatBE == null)
            return;
        if (copycatBE.hasCustomMaterial())
            return;

        copycatBE.setMaterial(appliedState);
        copycatBE.setConsumedItem(offhandItem);

        if (placer instanceof Player player && player.isCreative())
            return;
        offhandItem.shrink(1);
        if (offhandItem.isEmpty())
            placer.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
    }

    default void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.hasBlockEntity() || state.getBlock() == newState.getBlock())
            return;
        if (!isMoving) {
            ICopycatBlockEntity copycatBE = getCopycatBlockEntity(world, pos);
            if (copycatBE != null)
                Block.popResource(world, pos, copycatBE.getConsumedItem());
        }
        world.removeBlockEntity(pos);
    }

    default void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (player.isCreative()) {
            ICopycatBlockEntity copycatBE = getCopycatBlockEntity(level, pos);
            if (copycatBE != null) copycatBE.setConsumedItem(ItemStack.EMPTY);
        }
    }

    static BlockState getAppearance(ICopycatBlock block, BlockState state, BlockAndTintGetter level, BlockPos pos, Direction side,
                                    BlockState queryState, BlockPos queryPos) {
        if (block.isIgnoredConnectivitySide(level, state, side, pos, queryPos))
            return state;

        BlockState material = getMaterial(level, pos);
        return material.is(Blocks.AIR) ? AllBlocks.COPYCAT_BASE.getDefaultState() : material;
    }

    static BlockState getMaterial(BlockGetter reader, BlockPos targetPos) {
        if (reader.getBlockEntity(targetPos) instanceof ICopycatBlockEntity cbe)
            return cbe.getMaterial();
        return Blocks.AIR.defaultBlockState();
    }

    @Override
    default BlockState transform(BlockState state, StructureTransform transform) {
        Direction.Axis rotationAxis = transform.rotationAxis;
        Rotation rotation = transform.rotation;
        Mirror mirror = transform.mirror;

        Block block = state.getBlock();

        if (mirror != null)
            state = state.mirror(mirror);

        if (rotationAxis == Direction.Axis.Y) {
            if (block instanceof BellBlock) {
                if (state.getValue(BlockStateProperties.BELL_ATTACHMENT) == BellAttachType.DOUBLE_WALL)
                    state = state.setValue(BlockStateProperties.BELL_ATTACHMENT, BellAttachType.SINGLE_WALL);
                return state.setValue(BellBlock.FACING,
                        rotation.rotate(state.getValue(BellBlock.FACING)));
            }

            return state.rotate(rotation);
        }

        if (block instanceof FaceAttachedHorizontalDirectionalBlock) {
            DirectionProperty facingProperty = FaceAttachedHorizontalDirectionalBlock.FACING;
            EnumProperty<AttachFace> faceProperty = FaceAttachedHorizontalDirectionalBlock.FACE;
            Direction stateFacing = state.getValue(facingProperty);
            AttachFace stateFace = state.getValue(faceProperty);
            boolean z = rotationAxis == Direction.Axis.Z;
            Direction forcedAxis = z ? Direction.WEST : Direction.SOUTH;

            if (stateFacing.getAxis() == rotationAxis && stateFace == AttachFace.WALL)
                return state;

            for (int i = 0; i < rotation.ordinal(); i++) {
                stateFace = state.getValue(faceProperty);
                stateFacing = state.getValue(facingProperty);

                boolean b = state.getValue(faceProperty) == AttachFace.CEILING;
                state = state.setValue(facingProperty, b ? forcedAxis : forcedAxis.getOpposite());

                if (stateFace != AttachFace.WALL) {
                    state = state.setValue(faceProperty, AttachFace.WALL);
                    continue;
                }

                if (stateFacing.getAxisDirection() == (z ? Direction.AxisDirection.NEGATIVE : Direction.AxisDirection.POSITIVE)) {
                    state = state.setValue(faceProperty, AttachFace.FLOOR);
                    continue;
                }
                state = state.setValue(faceProperty, AttachFace.CEILING);
            }

            return state;
        }

        boolean halfTurn = rotation == Rotation.CLOCKWISE_180;
        if (block instanceof StairBlock) {
            if (state.getValue(StairBlock.FACING)
                    .getAxis() != rotationAxis) {
                for (int i = 0; i < rotation.ordinal(); i++) {
                    Direction direction = state.getValue(StairBlock.FACING);
                    Half half = state.getValue(StairBlock.HALF);
                    if (direction.getAxisDirection() == Direction.AxisDirection.POSITIVE ^ half == Half.BOTTOM
                            ^ direction.getAxis() == Direction.Axis.Z)
                        state = state.cycle(StairBlock.HALF);
                    else
                        state = state.setValue(StairBlock.FACING, direction.getOpposite());
                }
            } else {
                if (halfTurn) {
                    state = state.cycle(StairBlock.HALF);
                }
            }
            return state;
        }

        if (state.hasProperty(FACING)) {
            state = state.setValue(FACING, transform.rotateFacing(state.getValue(FACING)));
        } else if (state.hasProperty(AXIS)) {
            state = state.setValue(AXIS, transform.rotateAxis(state.getValue(AXIS)));
        } else if (halfTurn) {
            if (state.hasProperty(HORIZONTAL_FACING)) {
                Direction stateFacing = state.getValue(HORIZONTAL_FACING);
                if (stateFacing.getAxis() == rotationAxis)
                    return state;
            }

            state = state.rotate(rotation);

            if (state.hasProperty(SlabBlock.TYPE) && state.getValue(SlabBlock.TYPE) != SlabType.DOUBLE)
                state = state.setValue(SlabBlock.TYPE,
                        state.getValue(SlabBlock.TYPE) == SlabType.BOTTOM ? SlabType.TOP : SlabType.BOTTOM);
        }

        return state;
    }

    default boolean isIgnoredConnectivitySide(BlockAndTintGetter reader, BlockState state, Direction face,
                                              BlockPos fromPos, BlockPos toPos) {
        return false;
    }

    default boolean canConnectTexturesToward(BlockAndTintGetter reader, BlockPos fromPos, BlockPos toPos,
                                             BlockState state) {
        return true;
    }

    default boolean canFaceBeOccluded(BlockState state, Direction face) {
        return false;
    }

    default boolean shouldFaceAlwaysRender(BlockState state, Direction face) {
        return false;
    }

    @Environment(EnvType.CLIENT)
    static BlockColor wrappedColor() {
        return new WrappedBlockColor();
    }

    @Environment(EnvType.CLIENT)
    class WrappedBlockColor implements BlockColor {

        @Override
        public int getColor(BlockState pState, @Nullable BlockAndTintGetter pLevel, @Nullable BlockPos pPos,
                            int pTintIndex) {
            return Minecraft.getInstance()
                    .getBlockColors()
                    .getColor(getMaterial(pLevel, pPos), pLevel, pPos, pTintIndex);
        }
    }
}
