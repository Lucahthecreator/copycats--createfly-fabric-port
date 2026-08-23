package com.copycatsplus.copycats.mixin.foundation.copycat;

import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.gen.Accessor;

/** Exposes ColorLight's model delegate without linking against ColorLight at compile time. */
@Pseudo
@Mixin(targets = "me.mrhikmen.colorlight.core.render.TintedBakedModel", remap = false)
public interface ColorLightTintedBakedModelAccessor {
    @Accessor("wrapped")
    BlockStateModel copycats$getWrapped();
}
