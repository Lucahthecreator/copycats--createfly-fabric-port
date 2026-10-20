package com.copycatsplus.copycats.mixin.foundation.copycat;

import com.copycatsplus.copycats.compat.debug.CopycatsDebug;
import com.copycatsplus.copycats.compat.render.CopycatFaceHiding;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

/**
 * Ports Copycats+' face-hiding hook to MC 26's renderer-side culling path.
 * <p>
 * NeoForge: ModelBlockRenderer only calls NeoForge's level-aware overload
 * {@code shouldRenderFace(level, pos, state, face, neighborPos)}; vanilla's four-argument one is left unused.
 */
@Mixin(ModelBlockRenderer.class)
public class ModelBlockRendererMixin {
    @Shadow
    @Final
    private boolean cull;

    @Inject(
            method = "shouldRenderFace(Lnet/minecraft/client/renderer/block/BlockAndTintGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;Lnet/minecraft/core/BlockPos;)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    private void copycats$shouldRenderFace(BlockAndTintGetter reader, BlockPos pos, BlockState state, Direction face, BlockPos neighborPos, CallbackInfoReturnable<Boolean> cir) {
        if (!cull) {
            return;
        }

        Optional<Boolean> override = CopycatFaceHiding.getRenderOverride(reader, pos, state, face);
        CopycatsDebug.log("render", () -> "vanilla shouldRenderFace pos=" + pos
                + " neighborPos=" + neighborPos + " state=" + state
                + " face=" + face + " override=" + override);
        override.ifPresent(cir::setReturnValue);
    }
}
