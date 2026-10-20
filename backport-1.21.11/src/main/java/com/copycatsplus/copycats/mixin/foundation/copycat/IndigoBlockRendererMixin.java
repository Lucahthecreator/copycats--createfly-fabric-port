package com.copycatsplus.copycats.mixin.foundation.copycat;

import com.copycatsplus.copycats.compat.debug.CopycatsDebug;
import com.copycatsplus.copycats.compat.render.CopycatFaceHiding;
import net.minecraft.world.level.BlockAndTintGetter;
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
@Mixin(targets = "net.fabricmc.fabric.impl.client.indigo.renderer.render.BlockRenderInfo", remap = false)
public class IndigoBlockRendererMixin {
    @Shadow
    public BlockAndTintGetter blockView;

    @Shadow
    public BlockPos blockPos;

    @Shadow
    private BlockState blockState;

    @Inject(method = "shouldDrawSide", at = @At("HEAD"), cancellable = true)
    private void copycats$shouldDrawSide(Direction face, CallbackInfoReturnable<Boolean> cir) {
        if (face == null || blockView == null || blockPos == null || blockState == null) {
            return;
        }

        Optional<Boolean> override = CopycatFaceHiding.getRenderOverride(blockView, blockPos, blockState, face);
        CopycatsDebug.log("render", () -> "indigo shouldDrawSide pos=" + blockPos
                + " state=" + blockState + " face=" + face
                + " overrideShouldRender=" + override);
        override.ifPresent(cir::setReturnValue);
    }
}
