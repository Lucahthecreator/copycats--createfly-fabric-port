/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.zurrtum.create.AllBlocks
 *  com.zurrtum.create.content.decoration.slidingDoor.SlidingDoorBlock
 *  javax.annotation.ParametersAreNonnullByDefault
 *  net.minecraft.client.renderer.block.BlockAndTintGetter
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.InteractionResult
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.context.UseOnContext
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.entity.BlockEntityType
 *  net.minecraft.world.level.block.state.BlockBehaviour$Properties
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.StateDefinition$Builder
 *  net.minecraft.world.level.block.state.properties.BlockSetType
 *  net.minecraft.world.level.block.state.properties.BooleanProperty
 *  net.minecraft.world.level.block.state.properties.DoubleBlockHalf
 *  net.minecraft.world.level.block.state.properties.Property
 *  net.minecraft.world.phys.BlockHitResult
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.copycatsplus.copycats.content.copycat.sliding_door;

import com.copycatsplus.copycats.CCBlockEntityTypes;
import com.copycatsplus.copycats.content.copycat.door.CopycatDoorBlock;
import com.copycatsplus.copycats.content.copycat.sliding_door.CopycatSlidingDoorBlockEntity;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlockEntity;
import com.copycatsplus.copycats.utility.BlockEntityUtils;
import com.copycatsplus.copycats.utility.InteractionUtils;
import com.zurrtum.create.AllBlocks;
import com.zurrtum.create.content.decoration.slidingDoor.SlidingDoorBlock;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ParametersAreNonnullByDefault
public class CopycatSlidingDoorBlock
extends SlidingDoorBlock
implements ICopycatBlock {
    public static BooleanProperty CT = CopycatDoorBlock.CT;

    public CopycatSlidingDoorBlock(BlockBehaviour.Properties properties, BlockSetType type, boolean folds) {
        super(properties, type, folds);
        this.registerDefaultState((BlockState)this.defaultBlockState().setValue((Property)CT, (Comparable)Boolean.valueOf(true)));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder.add(new Property[]{CT}));
    }

    public static CopycatSlidingDoorBlock metal(BlockBehaviour.Properties properties, boolean folds) {
        return new CopycatSlidingDoorBlock(properties, (BlockSetType)TRAIN_SET_TYPE.get(), folds);
    }

    @Override
    public InteractionResult onSneakWrenched(BlockState state, UseOnContext context) {
        return InteractionUtils.sequential(() -> ICopycatBlock.super.onSneakWrenched(state, context), () -> super.onSneakWrenched(state, context));
    }

    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        return InteractionUtils.sequential(() -> ICopycatBlock.super.onWrenched(state, context), () -> super.onWrenched(state, context));
    }

    protected InteractionResult useItemOn(ItemStack heldStack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult ray) {
        return InteractionUtils.sequential(() -> ICopycatBlock.super.use(state, world, pos, player, hand, ray), () -> super.useItemOn(heldStack, state, world, pos, player, hand, ray));
    }

    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        InteractionResult result = super.useWithoutItem(state, level, pos, player, hit);
        if (result.consumesAction()) {
            BlockPos lowerPos = state.getValue((Property)HALF) == DoubleBlockHalf.LOWER ? pos : pos.below();
            BlockEntityUtils.redrawAt(level, lowerPos);
        }
        return result;
    }

    @Override
    public InteractionResult toggleCT(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pPlayer.isShiftKeyDown() && pPlayer.getItemInHand(pHand).equals(ItemStack.EMPTY)) {
            if (!this.canToggleCT(pState, (BlockGetter)pLevel, pPos)) {
                return InteractionResult.PASS;
            }
            pLevel.setBlock(pPos, (BlockState)pState.cycle((Property)CT), 3);
            BlockEntity be = pLevel.getBlockEntity(pPos);
            if (!(be instanceof ICopycatBlockEntity)) {
                return InteractionResult.PASS;
            }
            ICopycatBlockEntity fbe = (ICopycatBlockEntity)be;
            BlockEntityUtils.redraw((BlockEntity)fbe);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    @Nullable
    public BlockState getAcceptedBlockState(Level pLevel, BlockPos pPos, ItemStack item, Direction face) {
        return ICopycatBlock.super.getAcceptedBlockState(pLevel, pPos, item, face);
    }

    @Override
    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(worldIn, pos, state, placer, stack);
        ICopycatBlock.super.setPlacedBy(worldIn, pos, state, placer, stack);
    }

    @Override
    public BlockState playerWillDestroy(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull Player player) {
        ICopycatBlock.super.playerWillDestroy(level, pos, state, player);
        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public boolean isIgnoredConnectivitySide(BlockAndTintGetter reader, BlockState state, Direction face, BlockPos fromPos, @Nullable BlockPos toPos, @Nullable BlockState toState) {
        return true;
    }

    @Override
    public boolean canConnectTexturesToward(BlockAndTintGetter reader, BlockPos fromPos, BlockPos toPos, BlockState state) {
        return false;
    }

    public boolean supportsExternalFaceHiding(BlockState state) {
        return true;
    }

    @Override
    public boolean hidesNeighborFace(BlockGetter level, BlockPos pos, BlockState state, BlockState neighborState, Direction dir) {
        BlockState neighborMaterial;
        BlockPos toPos = pos.relative(dir);
        BlockState toState = level.getBlockState(toPos);
        BlockState material = state.getBlock() instanceof ICopycatBlock ? ICopycatBlock.getMaterial(level, pos) : state;
        BlockState blockState = neighborMaterial = neighborState.getBlock() instanceof ICopycatBlock ? ICopycatBlock.getMaterial(level, toPos) : neighborState;
        if (neighborMaterial.is((Object)AllBlocks.COPYCAT_BASE) && material.is((Object)AllBlocks.COPYCAT_BASE)) {
            if (dir == Direction.UP && toState.is((Object)this) && toState.getValue((Property)HALF) == DoubleBlockHalf.UPPER) {
                return true;
            }
            if (dir == Direction.DOWN && toState.is((Object)this) && toState.getValue((Property)HALF) == DoubleBlockHalf.LOWER) {
                return true;
            }
        }
        return ICopycatBlock.hidesNeighborFace(level, pos, state, neighborState, dir);
    }

    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        if (state.getValue((Property)HALF) == DoubleBlockHalf.UPPER) {
            return null;
        }
        return this.getBlockEntityType().create(pos, state);
    }

    public BlockEntityType<? extends CopycatSlidingDoorBlockEntity> getBlockEntityType() {
        return (BlockEntityType)CCBlockEntityTypes.COPYCAT_SLIDING_DOOR.get();
    }
}

