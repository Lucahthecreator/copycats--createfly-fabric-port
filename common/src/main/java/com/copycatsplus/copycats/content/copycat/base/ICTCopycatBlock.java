package com.copycatsplus.copycats.content.copycat.base;

import com.copycatsplus.copycats.content.copycat.base.functional.IFunctionalCopycatBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;

/**
 * Copycat blocks that support toggling connected textures should implement this interface.
 */
public interface ICTCopycatBlock {

    @Nullable
    Object getBlockEntity(BlockGetter worldIn, BlockPos pos);

    default boolean allowCTAppearance(BlockState state, BlockAndTintGetter level, BlockPos pos, Direction side, BlockState queryState, BlockPos queryPos) {
        Object be = getBlockEntity(level, queryPos);
        if (!(be instanceof IFunctionalCopycatBlockEntity fbe))
            return true;
        return fbe.isCTEnabled();
    }

    default InteractionResult toggleCT(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pPlayer.isShiftKeyDown() && pPlayer.getItemInHand(pHand).equals(ItemStack.EMPTY)) {
            Object be = getBlockEntity(pLevel, pPos);
            if (!(be instanceof IFunctionalCopycatBlockEntity fbe))
                return InteractionResult.PASS;
            fbe.setCTEnabled(!fbe.isCTEnabled());
            fbe.callRedraw();
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }
}
