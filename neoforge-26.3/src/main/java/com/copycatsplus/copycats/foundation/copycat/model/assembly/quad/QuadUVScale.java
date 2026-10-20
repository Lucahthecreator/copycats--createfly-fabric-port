package com.copycatsplus.copycats.foundation.copycat.model.assembly.quad;

import com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableQuad;
import com.zurrtum.create.client.catnip.render.SpriteShiftEntry;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;

public record QuadUVScale(Direction face, float pivotU, float pivotV, float scaleU, float scaleV) implements QuadTransform {
    @Override
    public boolean transformQuad(MutableQuad quad, TextureAtlasSprite sprite) {
        if (quad.computeLightFace() == this.face) {
            for (int vertex = 0; vertex < 4; vertex++) {
                float u = SpriteShiftEntry.getUnInterpolatedU(sprite, quad.vertices.get(vertex).uv.u) - this.pivotU;
                float v = SpriteShiftEntry.getUnInterpolatedV(sprite, quad.vertices.get(vertex).uv.v) - this.pivotV;
                quad.vertices.get(vertex).uv.u = sprite.getU(u * this.scaleU + this.pivotU);
                quad.vertices.get(vertex).uv.v = sprite.getV(v * this.scaleV + this.pivotV);
            }
        }

        return true;
    }
}
