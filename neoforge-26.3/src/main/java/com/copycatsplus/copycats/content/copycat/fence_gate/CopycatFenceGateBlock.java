package com.copycatsplus.copycats.content.copycat.fence_gate;

import com.copycatsplus.copycats.CCBlockEntityTypes;
import com.copycatsplus.copycats.foundation.copycat.CCCopycatBlockEntity;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.IStateType;
import com.copycatsplus.copycats.utility.BlockEntityUtils;
import com.copycatsplus.copycats.utility.InteractionUtils;
import com.zurrtum.create.foundation.block.IBE;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.phys.BlockHitResult;

@ParametersAreNonnullByDefault
public class CopycatFenceGateBlock extends FenceGateBlock implements ICopycatBlock, IBE<CCCopycatBlockEntity>, IStateType {
    public CopycatFenceGateBlock(Properties properties) {
        super(WoodType.OAK, properties);
    }

    public boolean collisionExtendsVertically(BlockState state, BlockGetter level, BlockPos pos, Entity collidingEntity) {
        return true;
    }

    @Nullable
    @Override
    public <S extends BlockEntity> BlockEntityTicker<S> getTicker(Level level, BlockState state, BlockEntityType<S> type) {
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
        }

        return result;
    }

    @Override
    public boolean isAcceptedRegardless(BlockState material) {
        return material.getBlock() instanceof FenceGateBlock;
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
        return ICopycatBlock.super.isIgnoredConnectivitySide(reader, state, face, fromPos, toPos, toState);
    }

    @Override
    public boolean canConnectTexturesToward(BlockAndTintGetter reader, BlockPos fromPos, BlockPos toPos, BlockState state) {
        return ICopycatBlock.super.canConnectTexturesToward(reader, fromPos, toPos, state);
    }

    public boolean supportsExternalFaceHiding(BlockState state) {
        return false;
    }
}
