/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.zurrtum.create.client.catnip.render.SpriteShiftEntry
 *  net.minecraft.client.renderer.texture.TextureAtlasSprite
 *  net.minecraft.core.Direction
 */
package com.copycatsplus.copycats.foundation.copycat.model.assembly.quad;

import com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableQuad;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.quad.QuadTransform;
import com.zurrtum.create.client.catnip.render.SpriteShiftEntry;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;

public record QuadUVScale(Direction face, float pivotU, float pivotV, float scaleU, float scaleV) implements QuadTransform
{
    @Override
    public boolean transformQuad(MutableQuad quad, TextureAtlasSprite sprite) {
        if (quad.computeLightFace() == this.face) {
            for (int vertex = 0; vertex < 4; ++vertex) {
                float u = SpriteShiftEntry.getUnInterpolatedU((TextureAtlasSprite)sprite, (float)quad.vertices.get((int)vertex).uv.u) - this.pivotU;
                float v = SpriteShiftEntry.getUnInterpolatedV((TextureAtlasSprite)sprite, (float)quad.vertices.get((int)vertex).uv.v) - this.pivotV;
                quad.vertices.get((int)vertex).uv.u = sprite.getU(u * this.scaleU + this.pivotU);
                quad.vertices.get((int)vertex).uv.v = sprite.getV(v * this.scaleV + this.pivotV);
            }
        }
        return true;
    }
}

