package com.copycatsplus.copycats.foundation.copycat.model.assembly.quad;

import com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableQuad;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableVec3;
import com.zurrtum.create.client.catnip.render.SpriteShiftEntry;
import java.util.List;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public record QuadUVUpdate(QuadTransform... transforms) implements QuadTransform {
    @Override
    public boolean transformQuad(MutableQuad quad, TextureAtlasSprite sprite) {
        List<MutableVec3> prevXYZ = List.of(
            quad.vertices.get(0).xyz.copy(), quad.vertices.get(1).xyz.copy(), quad.vertices.get(2).xyz.copy(), quad.vertices.get(3).xyz.copy()
        );

        for (QuadTransform transform : this.transforms) {
            transform.transformQuad(quad, sprite);
        }

        updateUV(quad, sprite, prevXYZ);
        return true;
    }

    public static void updateUV(MutableQuad quad, TextureAtlasSprite sprite, List<MutableVec3> prevXYZ) {
        MutableVec3 xyz0 = prevXYZ.get(0);
        MutableVec3 xyz1 = prevXYZ.get(1);
        MutableVec3 xyz2 = prevXYZ.get(2);
        MutableVec3 xyz3 = prevXYZ.get(3);
        MutableVec3 uAxis = xyz3.copy().add(xyz2).scale(0.5);
        MutableVec3 vAxis = xyz1.copy().add(xyz2).scale(0.5);
        MutableVec3 center = xyz3.copy().add(xyz2).add(xyz0).add(xyz1).scale(0.25);
        float u0 = quad.vertices.get(0).uv.u;
        float u3 = quad.vertices.get(3).uv.u;
        float v0 = quad.vertices.get(0).uv.v;
        float v1 = quad.vertices.get(1).uv.v;
        float uScale = (float)Math.round(
            (SpriteShiftEntry.getUnInterpolatedU(sprite, u3) - SpriteShiftEntry.getUnInterpolatedU(sprite, u0)) / xyz3.distanceTo(xyz0)
        );
        float vScale = (float)Math.round(
            (SpriteShiftEntry.getUnInterpolatedV(sprite, v1) - SpriteShiftEntry.getUnInterpolatedV(sprite, v0)) / xyz1.distanceTo(xyz0)
        );
        if (uScale == 0.0F) {
            float v3 = quad.vertices.get(3).uv.v;
            float u1 = quad.vertices.get(1).uv.u;
            uAxis = xyz1.copy().add(xyz2).scale(0.5);
            vAxis = xyz3.copy().add(xyz2).scale(0.5);
            uScale = (float)Math.round(
                (SpriteShiftEntry.getUnInterpolatedU(sprite, u1) - SpriteShiftEntry.getUnInterpolatedU(sprite, u0)) / xyz1.distanceTo(xyz0)
            );
            vScale = (float)Math.round(
                (SpriteShiftEntry.getUnInterpolatedV(sprite, v3) - SpriteShiftEntry.getUnInterpolatedV(sprite, v0)) / xyz3.distanceTo(xyz0)
            );
        }

        uAxis = uAxis.subtract(center).normalize();
        vAxis = vAxis.subtract(center).normalize();

        for (int vertex = 0; vertex < 4; vertex++) {
            MutableVec3 xyz = prevXYZ.get(vertex);
            MutableVec3 newXyz = quad.vertices.get(vertex).xyz;
            MutableVec3 diff = newXyz.copy().subtract(xyz);
            if (diff.lengthSqr() > 0.0) {
                float u = quad.vertices.get(vertex).uv.u;
                float v = quad.vertices.get(vertex).uv.v;
                float uDiff = (float)uAxis.dot(diff) * uScale;
                float vDiff = (float)vAxis.dot(diff) * vScale;
                quad.vertices.get(vertex).uv.u = sprite.getU(SpriteShiftEntry.getUnInterpolatedU(sprite, u) + uDiff);
                quad.vertices.get(vertex).uv.v = sprite.getV(SpriteShiftEntry.getUnInterpolatedV(sprite, v) + vDiff);
            }
        }
    }
}
