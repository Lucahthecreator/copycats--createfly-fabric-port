/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.zurrtum.create.content.contraptions.StructureTransform
 *  com.zurrtum.create.foundation.block.IBE
 *  javax.annotation.Nullable
 *  javax.annotation.ParametersAreNonnullByDefault
 *  net.minecraft.core.BlockPos
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.InteractionResult
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.Mirror
 *  net.minecraft.world.level.block.Rotation
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.entity.BlockEntityTicker
 *  net.minecraft.world.level.block.entity.BlockEntityType
 *  net.minecraft.world.level.block.state.BlockBehaviour$Properties
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.phys.BlockHitResult
 *  org.jetbrains.annotations.NotNull
 */
package com.copycatsplus.copycats.foundation.copycat.multistate;

import com.copycatsplus.copycats.CCBlockEntityTypes;
import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.multistate.MultiStateCopycatBlockEntity;
import com.zurrtum.create.content.contraptions.StructureTransform;
import com.zurrtum.create.foundation.block.IBE;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

@ParametersAreNonnullByDefault
public abstract class MultiStateCopycatBlock
extends Block
implements IMultiStateCopycatBlock,
IBE<MultiStateCopycatBlockEntity> {
    public MultiStateCopycatBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Nullable
    public <S extends BlockEntity> BlockEntityTicker<S> getTicker(Level p_153212_, BlockState p_153213_, BlockEntityType<S> p_153214_) {
        return null;
    }

    protected InteractionResult useItemOn(ItemStack heldStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        return IMultiStateCopycatBlock.super.use(state, level, pos, player, hand, hit);
    }

    @Override
    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(worldIn, pos, state, placer, stack);
        IMultiStateCopycatBlock.super.setPlacedBy(worldIn, pos, state, placer, stack);
    }

    @Override
    public BlockState playerWillDestroy(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull Player player) {
        IMultiStateCopycatBlock.super.playerWillDestroy(level, pos, state, player);
        return super.playerWillDestroy(level, pos, state, player);
    }

    @NotNull
    public BlockState rotate(@NotNull BlockState state, Rotation rot) {
        return IMultiStateCopycatBlock.super.rotateCopycat(state, rot);
    }

    @NotNull
    public BlockState mirror(@NotNull BlockState state, Mirror mirror) {
        return IMultiStateCopycatBlock.super.mirrorCopycat(state, mirror);
    }

    @Override
    public abstract BlockState transform(BlockState var1, StructureTransform var2);

    public Class<MultiStateCopycatBlockEntity> getBlockEntityClass() {
        return MultiStateCopycatBlockEntity.class;
    }

    public BlockEntityType<? extends MultiStateCopycatBlockEntity> getBlockEntityType() {
        return (BlockEntityType)CCBlockEntityTypes.MULTI_STATE_COPYCAT.get();
    }
}

