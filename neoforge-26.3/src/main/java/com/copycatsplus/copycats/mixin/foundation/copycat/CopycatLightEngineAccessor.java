package com.copycatsplus.copycats.mixin.foundation.copycat;

import net.minecraft.world.level.chunk.LightChunkGetter;
import net.minecraft.world.level.lighting.LightEngine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LightEngine.class)
public interface CopycatLightEngineAccessor {
    @Accessor("chunkSource")
    LightChunkGetter copycats$getChunkSource();
}
