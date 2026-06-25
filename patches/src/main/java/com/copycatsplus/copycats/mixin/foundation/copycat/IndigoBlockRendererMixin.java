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
@Mixin(targets = "net.fabricmc.fabric.impl.client.indigo.renderer.render.AltModelBlockRendererImpl", remap = false)
public class IndigoBlockRendererMixin {
    @Shadow
    private BlockAndTintGetter level;

    @Shadow
    private BlockPos pos;

    @Shadow
    private BlockState blockState;

    @Inject(method = "shouldCullFace", at = @At("HEAD"), cancellable = true, require = 0)
    private void copycats$shouldCullFace(Direction face, CallbackInfoReturnable<Boolean> cir) {
        if (face == null || level == null || pos == null || blockState == null) {
            return;
        }

        Optional<Boolean> override = CopycatFaceHiding.getRenderOverride(level, pos, blockState, face);
        CopycatsDebug.log("render", () -> "indigo shouldCullFace pos=" + pos
                + " state=" + blockState + " face=" + face
                + " overrideShouldRender=" + override);
        override.ifPresent(shouldRender -> cir.setReturnValue(!shouldRender));
    }
}
