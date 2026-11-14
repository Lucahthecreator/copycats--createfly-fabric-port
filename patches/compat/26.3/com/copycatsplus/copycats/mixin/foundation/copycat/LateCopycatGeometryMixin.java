package com.copycatsplus.copycats.mixin.foundation.copycat;

import com.copycatsplus.copycats.utility.CopycatTranslucentRendering;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollection;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.feature.CustomFeatureRenderer;
import net.minecraft.client.renderer.feature.phase.SimpleFeatureRenderPhase;
import net.minecraft.client.renderer.rendertype.RenderType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SubmitNodeCollection.class)
public abstract class LateCopycatGeometryMixin {
    @Shadow @Final public SimpleFeatureRenderPhase afterTerrain;

    @Inject(method = "submitCustomGeometry", at = @At("HEAD"), cancellable = true)
    private void copycats$submitTranslucentCopycatAfterTerrain(PoseStack pose, RenderType type,
            SubmitNodeCollector.CustomGeometryRenderer renderer, CallbackInfo ci) {
        if (CopycatTranslucentRendering.isLateCopycat()) {
            afterTerrain.submit(new CustomFeatureRenderer.Submit(pose.last().copy(), type, renderer));
            ci.cancel();
        }
    }
}
