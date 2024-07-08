package com.copycatsplus.copycats.mixin.copycat.base;

import com.copycatsplus.copycats.content.copycat.base.ICopycatBlock;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.simibubi.create.AllBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Compute occlusion shape cache for copycat blocks.
 */
@Mixin(targets = "net.minecraft.world.level.block.state.BlockBehaviour$BlockStateBase$Cache")
public class BlockStateBaseCacheMixin {
    @WrapOperation(
            method = "<init>",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;canOcclude()Z")
    )
    private boolean canCopycatOcclude(BlockState instance,
                                      Operation<Boolean> original) {
        if (instance.getBlock() instanceof ICopycatBlock) {
            return true;
        }
        return original.call(instance);
    }
}
