package com.copycatsplus.copycats.content.copycat.base.model.assembly.quad;

import com.copycatsplus.copycats.content.copycat.base.model.assembly.MutableQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

/**
 * Transforms a quad by mutating its vertices or other properties.
 */
public interface QuadTransform {
    void transformVertices(MutableQuad quad, TextureAtlasSprite sprite);
}
