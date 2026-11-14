package com.copycatsplus.copycats.mixin.foundation.copycat;

import com.copycatsplus.copycats.content.copycat.board.CopycatBoardBlock;
import com.copycatsplus.copycats.content.copycat.board.CopycatBoxItem;
import com.copycatsplus.copycats.content.copycat.board.CopycatCatwalkItem;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Sets all board faces before placing the preassembled items. */
@Mixin(BlockItem.class)
public abstract class CopycatPreassembledPlacementMixin {
    @Inject(method = "getPlacementState", at = @At("RETURN"), cancellable = true)
    private void copycats$preassembleBoards(BlockPlaceContext context, CallbackInfoReturnable<BlockState> cir) {
        Object item = this;
        BlockState state = cir.getReturnValue();
        if (state == null) return;

        if (item instanceof CopycatBoxItem) {
            for (Direction direction : Direction.values()) {
                state = state.setValue(CopycatBoardBlock.byDirection(direction), true);
            }
        } else if (item instanceof CopycatCatwalkItem) {
            Player player = context.getPlayer();
            Direction facing = player == null ? Direction.SOUTH : player.getDirection();
            for (Direction direction : Direction.Plane.HORIZONTAL) {
                state = state.setValue(CopycatBoardBlock.byDirection(direction), direction.getAxis() != facing.getAxis());
            }
            state = state.setValue(CopycatBoardBlock.DOWN, true).setValue(CopycatBoardBlock.UP, false);
        } else {
            return;
        }
        cir.setReturnValue(state);
    }
}
