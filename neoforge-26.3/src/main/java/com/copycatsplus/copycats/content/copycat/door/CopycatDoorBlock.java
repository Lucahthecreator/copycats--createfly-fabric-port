package com.copycatsplus.copycats.content.copycat.door;

import com.copycatsplus.copycats.CCBlockEntityTypes;
import com.copycatsplus.copycats.foundation.copycat.CCCopycatBlockEntity;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlockEntity;
import com.copycatsplus.copycats.foundation.copycat.IStateType;
import com.copycatsplus.copycats.utility.BlockEntityUtils;
import com.copycatsplus.copycats.utility.InteractionUtils;
import com.zurrtum.create.AllBlocks;
import com.zurrtum.create.foundation.block.IBE;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class CopycatDoorBlock extends DoorBlock implements ICopycatBlock, IBE<CCCopycatBlockEntity>, IStateType {
    public static BooleanProperty CT = BooleanProperty.create("ct");

    public CopycatDoorBlock(Properties properties, BlockSetType type) {
        super(type, properties);
        this.registerDefaultState(this.defaultBlockState().setValue(CT, true));
    }

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder.add(CT));
    }

    @Nullable
    @Override
    public <S extends BlockEntity> BlockEntityTicker<S> getTicker(Level p_153212_, BlockState p_153213_, BlockEntityType<S> p_153214_) {
        return null;
    }

    @Override
    protected InteractionResult useItemOn(
        ItemStack heldStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit
    ) {
        return InteractionUtils.sequential(
            () -> ICopycatBlock.super.use(state, level, pos, player, hand, hit), () -> super.useItemOn(heldStack, state, level, pos, player, hand, hit)
        );
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        InteractionResult result = super.useWithoutItem(state, level, pos, player, hit);
        if (result.consumesAction()) {
            BlockEntityUtils.redrawAt(level, pos);
            BlockEntityUtils.redrawAt(level, state.getValue(HALF) == DoubleBlockHalf.LOWER ? pos.above() : pos.below());
        }

        return result;
    }

    @Override
    public InteractionResult toggleCT(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (!pPlayer.isShiftKeyDown() || !pPlayer.getItemInHand(pHand).equals(ItemStack.EMPTY)) {
            return InteractionResult.PASS;
        } else if (!this.canToggleCT(pState, pLevel, pPos)) {
            return InteractionResult.PASS;
        } else {
            pLevel.setBlock(pPos, pState.cycle(CT), 3);
            if (pLevel.getBlockEntity(pPos) instanceof ICopycatBlockEntity fbe) {
                BlockEntityUtils.redraw((BlockEntity)fbe);
                return InteractionResult.SUCCESS;
            } else {
                return InteractionResult.PASS;
            }
        }
    }

    @Override
    public boolean isAcceptedRegardless(BlockState material) {
        return material.getBlock() instanceof DoorBlock;
    }

    @Override
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, @Nullable LivingEntity pPlacer, ItemStack pStack) {
        ICopycatBlock.super.setPlacedBy(pLevel, pPos, pState, pPlacer, pStack);
        super.setPlacedBy(pLevel, pPos, pState, pPlacer, pStack);
    }

    @Override
    public BlockState playerWillDestroy(Level pLevel, BlockPos pPos, BlockState pState, Player pPlayer) {
        ICopycatBlock.super.playerWillDestroy(pLevel, pPos, pState, pPlayer);
        return super.playerWillDestroy(pLevel, pPos, pState, pPlayer);
    }

    @Override
    public Class<CCCopycatBlockEntity> getBlockEntityClass() {
        return CCCopycatBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends CCCopycatBlockEntity> getBlockEntityType() {
        return CCBlockEntityTypes.COPYCAT.get();
    }

    @Override
    public boolean isIgnoredConnectivitySide(
        BlockAndTintGetter reader, BlockState state, Direction face, BlockPos fromPos, @Nullable BlockPos toPos, @Nullable BlockState toState
    ) {
        return true;
    }

    @Override
    public boolean canConnectTexturesToward(BlockAndTintGetter reader, BlockPos fromPos, BlockPos toPos, BlockState state) {
        return false;
    }

    public boolean supportsExternalFaceHiding(BlockState state) {
        return true;
    }

    public boolean hidesNeighborFace(BlockGetter level, BlockPos pos, BlockState state, BlockState neighborState, Direction dir) {
        BlockPos toPos = pos.relative(dir);
        BlockState toState = level.getBlockState(toPos);
        BlockState material = state.getBlock() instanceof ICopycatBlock ? ICopycatBlock.getMaterial(level, pos) : state;
        BlockState neighborMaterial = neighborState.getBlock() instanceof ICopycatBlock ? ICopycatBlock.getMaterial(level, toPos) : neighborState;
        if (neighborMaterial.is(AllBlocks.COPYCAT_BASE) && material.is(AllBlocks.COPYCAT_BASE)) {
            if (dir == Direction.UP && toState.is(this) && toState.getValue(HALF) == DoubleBlockHalf.UPPER) {
                return true;
            }

            if (dir == Direction.DOWN && toState.is(this) && toState.getValue(HALF) == DoubleBlockHalf.LOWER) {
                return true;
            }
        }

        return ICopycatBlock.hidesNeighborFace(level, pos, state, neighborState, dir);
    }
}
