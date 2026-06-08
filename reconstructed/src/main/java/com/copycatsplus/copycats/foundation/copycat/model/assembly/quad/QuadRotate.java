/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.texture.TextureAtlasSprite
 */
package com.copycatsplus.copycats.foundation.copycat.model.assembly.quad;

import com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableQuad;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableVec3;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.quad.QuadTransform;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public record QuadRotate(MutableVec3.AsPivot pivot, MutableVec3.AsAngle rotation) implements QuadTransform
{
    @Override
    public boolean transformQuad(MutableQuad quad, TextureAtlasSprite sprite) {
        for (int i = 0; i < 4; ++i) {
            quad.vertices.get((int)i).xyz.subtract(this.pivot).rotate(this.rotation).add(this.pivot);
        }
        return true;
    }
}

