package com.copycatsplus.copycats.mixin.copycat.flat_pane;

import com.copycatsplus.copycats.content.copycat.flat_pane.CopycatFlatPaneBlock;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Allow normal panes to attach to copycat flat panes
 */
@Mixin(IronBarsBlock.class)
public class IronBarsBlockMixin {
    @Inject(
            method = "attachsTo",
            at = @At("HEAD"),
            cancellable = true
    )
    private void attachesToCopycat(BlockState state, boolean solidSide, CallbackInfoReturnable<Boolean> cir) {
        if (state.getBlock() instanceof CopycatFlatPaneBlock && state.getValue(BlockStateProperties.AXIS) != Direction.Axis.Y)
            cir.setReturnValue(true);
    }
}
