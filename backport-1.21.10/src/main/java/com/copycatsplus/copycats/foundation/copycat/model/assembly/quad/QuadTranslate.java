/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.texture.TextureAtlasSprite
 */
package com.copycatsplus.copycats.foundation.copycat.model.assembly.quad;

import com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableQuad;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.quad.QuadTransform;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public record QuadTranslate(double x, double y, double z) implements QuadTransform
{
    @Override
    public boolean transformQuad(MutableQuad quad, TextureAtlasSprite sprite) {
        for (int i = 0; i < 4; ++i) {
            quad.vertices.get((int)i).xyz.add(this.x, this.y, this.z);
        }
        return true;
    }
}

