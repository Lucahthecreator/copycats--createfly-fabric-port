package com.copycatsplus.copycats.mixin.foundation.copycat;

import com.zurrtum.create.AllBlocks;
import com.zurrtum.create.content.decoration.bracket.BracketBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.world.level.block.state.BlockBehaviour$BlockStateBase")
public class BlockStateBaseMixin {
    @Inject(
            method = "canOcclude",
            at = @At("HEAD"),
            cancellable = true
    )
    private void customOcclusion(CallbackInfoReturnable<Boolean> cir) {
        BlockBehaviour.BlockStateBase instance = (BlockBehaviour.BlockStateBase) (Object) this;
        if ((Object) this instanceof BlockState state && state.is(AllBlocks.COPYCAT_BASE)) {
            cir.setReturnValue(false);
        }
        if (instance.getBlock() instanceof BracketBlock) {
            cir.setReturnValue(false);
        }
    }
}
