package com.copycatsplus.copycats.mixin.foundation.copycat;

import com.copycatsplus.copycats.foundation.copycat.ICopycatBlock;
import com.zurrtum.create.content.decoration.copycat.CopycatBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Implements Copycats+ material hooks for CreateFly's own copycat block.
 */
@Mixin(value = CopycatBlock.class, remap = false)
public class CopycatBlockMixin implements ICopycatBlock {
    @Inject(
            method = "getAcceptedBlockState",
            at = @At("HEAD"),
            cancellable = true
    )
    private void copycats$getAcceptedBlockState(Level level, BlockPos pos, ItemStack item, Direction face, CallbackInfoReturnable<BlockState> cir) {
        if (!(item.getItem() instanceof BlockItem blockItem)) {
            return;
        }

        Block block = blockItem.getBlock();
        if (block instanceof ICopycatBlock) {
            cir.setReturnValue(null);
        }
    }

    @Inject(
            method = "getMaterial",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void copycats$getMaterial(BlockGetter reader, BlockPos targetPos, CallbackInfoReturnable<BlockState> cir) {
        cir.setReturnValue(ICopycatBlock.getMaterial(reader, targetPos));
    }
}
