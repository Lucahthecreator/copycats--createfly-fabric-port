package com.copycatsplus.copycats.mixin.foundation.copycat;

import com.copycatsplus.copycats.CCBlocks;
import com.copycatsplus.copycats.compat.debug.CopycatsDebug;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.model.ScaledBlockAndTintGetter;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.zurrtum.create.client.infrastructure.model.CTModel;
import com.zurrtum.create.content.decoration.copycat.CopycatBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Lets Create's connected-texture model treat Copycats+ blocks like Create copycats.
 */
@Mixin(CTModel.class)
public class CTModelMixin {
    @WrapOperation(
        method = "createCTData",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/Block;shouldRenderFace(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;)Z"
        )
    )
    private boolean copycats$doNotSkipScaledCT(
            BlockState state,
            BlockState neighbor,
            Direction face,
            Operation<Boolean> original,
            BlockAndTintGetter world,
            BlockPos pos,
            BlockState modelState
    ) {
        if (world instanceof ScaledBlockAndTintGetter) {
            CopycatsDebug.log("ct", () -> "scaled CT occlusion pass pos=" + pos
                    + " face=" + face + " modelState=" + modelState
                    + " worldState=" + world.getBlockState(pos) + " neighbor=" + neighbor);
            return true;
        }
        return original.call(state, neighbor, face);
    }

    @WrapOperation(
        method = "createCTData",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;getBlock()Lnet/minecraft/world/level/block/Block;")
    )
    private Block copycats$useWrappedCopycatForCT(BlockState instance, Operation<Block> original) {
        if (instance.getBlock() instanceof ICopycatBlock copycat && !(instance.getBlock() instanceof CopycatBlock)) {
            CCBlocks.WRAPPED_COPYCAT.get().setWrapped(copycat);
            CopycatsDebug.log("ct", () -> "using wrapped copycat for CT state=" + instance
                    + " wrappedBlock=" + instance.getBlock());
            return CCBlocks.WRAPPED_COPYCAT.get();
        }
        Block block = original.call(instance);
        CopycatsDebug.log("ct", () -> "using original CT block state=" + instance
                + " block=" + block);
        return block;
    }
}
