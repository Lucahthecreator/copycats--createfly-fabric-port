package com.copycatsplus.copycats.content.copycat.base;

import com.copycatsplus.copycats.content.copycat.base.functional.ICopycatBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

/**
 * Copycat blocks that support toggling connected textures should implement this interface.
 */
public interface ICTCopycatBlock {

    default boolean allowCTAppearance(BlockState state, BlockAndTintGetter level, BlockPos pos, Direction side, BlockState queryState, BlockPos queryPos) {
        BlockEntity be = level.getBlockEntity(queryPos);
        if (!(be instanceof ICopycatBlockEntity fbe))
            return true;
        return fbe.isCTEnabled();
    }

    default InteractionResult toggleCT(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pPlayer.isShiftKeyDown() && pPlayer.getItemInHand(pHand).equals(ItemStack.EMPTY)) {
            BlockEntity be = pLevel.getBlockEntity(pPos);
            if (!(be instanceof ICopycatBlockEntity fbe))
                return InteractionResult.PASS;
            fbe.setCTEnabled(!fbe.isCTEnabled());
            fbe.callRedraw();
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }
}
