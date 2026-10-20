package com.copycatsplus.copycats.mixin.foundation.copycat;

import com.copycatsplus.copycats.compat.debug.CopycatsDebug;
import com.copycatsplus.copycats.compat.render.CopycatFaceHiding;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Pseudo
@Mixin(targets = "net.caffeinemc.mods.sodium.client.render.model.AbstractBlockRenderContext", remap = false)
public class SodiumBlockRenderContextMixin {
    @Shadow
    protected BlockAndTintGetter level;

    @Shadow
    protected BlockPos pos;

    @Shadow
    protected BlockState state;

    @Inject(method = "shouldDrawSide", at = @At("HEAD"), cancellable = true, require = 0)
    private void copycats$shouldDrawSide(Direction face, CallbackInfoReturnable<Boolean> cir) {
        if (face == null || level == null || pos == null || state == null) {
            return;
        }

        Optional<Boolean> override = CopycatFaceHiding.getRenderOverride(level, pos, state, face);
        CopycatsDebug.log("render", () -> "sodium shouldDrawSide pos=" + pos
                + " state=" + state + " face=" + face
                + " override=" + override);
        override.ifPresent(cir::setReturnValue);
    }
}
