package com.copycatsplus.copycats.mixin.foundation.copycat;

import com.copycatsplus.copycats.foundation.copycat.ICopycatBlock;
import net.fabricmc.fabric.api.client.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.client.renderer.v1.model.FabricBlockStateModel;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Predicate;

/**
 * Continuity's CT and emissive wrappers do not know which block-entity material a Copycat section
 * represents. Bypass only those optional wrappers for Copycat states and retain the wrapped model.
 */
@Pseudo
@Mixin(targets = {
        "me.pepperbell.continuity.client.model.CtmBlockStateModel",
        "me.pepperbell.continuity.client.model.EmissiveBlockStateModel"
}, remap = false)
public abstract class ContinuityCopycatModelMixin {
    @Inject(method = "emitQuads", at = @At("HEAD"), cancellable = true, require = 0)
    private void copycats$emitUnwrapped(QuadEmitter emitter, BlockAndTintGetter level, BlockPos pos,
                                       BlockState state, RandomSource random, Predicate<Direction> cullTest,
                                       CallbackInfo ci) {
        if (!(state.getBlock() instanceof ICopycatBlock)) {
            return;
        }
        BlockStateModel wrapped = ((FabricBlockStateModelWrapperAccessor) this).copycats$getWrapped();
        if (wrapped != null && wrapped != this) {
            ((FabricBlockStateModel) (Object) wrapped).emitQuads(emitter, level, pos, state, random, cullTest);
        }
        ci.cancel();
    }
}
