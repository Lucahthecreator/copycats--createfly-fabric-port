package com.copycatsplus.copycats.content.copycat.base;

import com.copycatsplus.copycats.content.copycat.base.multistate.MultiStateCopycatBlock;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllTags;
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
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Indicates that a block functions as a copycat but is not a subclass of {@link CCCopycatBlock}.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public interface ICopycatBlock extends IWrenchable {

    @Nullable
    default ICopycatBlockEntity getCopycatBlockEntity(BlockGetter worldIn, BlockPos pos) {
        BlockEntity blockEntity = worldIn.getBlockEntity(pos);

        if (blockEntity == null)
            return null;
        if (!(blockEntity instanceof ICopycatBlockEntity functionalCopycatBlockEntity))
            return null;

        return functionalCopycatBlockEntity;
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
        ICopycatBlockEntity ufte = getCopycatBlockEntity(context.getLevel(), context.getClickedPos());
        if (ufte == null)
            return InteractionResult.PASS;
        ItemStack consumedItem = ufte.getConsumedItem();
        if (!ufte.hasCustomMaterial())
            return InteractionResult.PASS;
        Player player = context.getPlayer();
        if (!player.isCreative())
            player.getInventory()
                    .placeItemBackInInventory(consumedItem);
        context.getLevel()
                .levelEvent(2001, context.getClickedPos(), Block.getId(ufte.getBlockState()));
        ufte.setMaterial(AllBlocks.COPYCAT_BASE.getDefaultState());
        ufte.setConsumedItem(ItemStack.EMPTY);
        return InteractionResult.SUCCESS;
    }

    @Nullable
    default BlockState getAcceptedBlockState(Level pLevel, BlockPos pPos, ItemStack item, Direction face) {
        if (!(item.getItem() instanceof BlockItem bi))
            return null;

        Block block = bi.getBlock();
        if (block instanceof ICopycatBlock || block instanceof MultiStateCopycatBlock)
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
        ICopycatBlockEntity ufte = getCopycatBlockEntity(world, pos);
        if (ufte == null)
            return InteractionResult.PASS;
        if (ufte.getMaterial()
                .is(material.getBlock())) {
            if (!ufte.cycleMaterial())
                return InteractionResult.PASS;
            ufte.getLevel()
                    .playSound(null, ufte.getBlockPos(), SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS, .75f,
                            .95f);
            return InteractionResult.SUCCESS;
        }
        if (ufte.hasCustomMaterial())
            return InteractionResult.PASS;
        if (world.isClientSide())
            return InteractionResult.SUCCESS;

        ufte.setMaterial(material);
        ufte.setConsumedItem(itemInHand);
        ufte.getLevel()
                .playSound(null, ufte.getBlockPos(), material.getSoundType()
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
        ICopycatBlockEntity ufte = getCopycatBlockEntity(worldIn, pos);
        if (ufte == null)
            return;
        if (ufte.hasCustomMaterial())
            return;

        ufte.setMaterial(appliedState);
        ufte.setConsumedItem(offhandItem);

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
            ICopycatBlockEntity ufte = getCopycatBlockEntity(world, pos);
            if (ufte != null)
                Block.popResource(world, pos, ufte.getConsumedItem());
        }
        world.removeBlockEntity(pos);
    }

    default void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (player.isCreative()) {
            ICopycatBlockEntity ufte = getCopycatBlockEntity(level, pos);
            if (ufte != null) ufte.setConsumedItem(ItemStack.EMPTY);
        }
    }

    static BlockState getMaterial(BlockGetter reader, BlockPos targetPos) {
        if (reader.getBlockEntity(targetPos) instanceof ICopycatBlockEntity cbe)
            return cbe.getMaterial();
        return Blocks.AIR.defaultBlockState();
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
    static class WrappedBlockColor implements BlockColor {

        @Override
        public int getColor(BlockState pState, @Nullable BlockAndTintGetter pLevel, @Nullable BlockPos pPos,
                            int pTintIndex) {
            return Minecraft.getInstance()
                    .getBlockColors()
                    .getColor(getMaterial(pLevel, pPos), pLevel, pPos, pTintIndex);
        }
    }
}
