package com.copycatsplus.copycats.foundation.copycat.model.assembly.quad;

import com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public interface QuadTransform {
    boolean transformQuad(MutableQuad var1, TextureAtlasSprite var2);
}
