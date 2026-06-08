/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.texture.TextureAtlasSprite
 *  net.minecraft.core.Direction
 *  net.minecraft.core.Direction$Axis
 *  net.minecraft.core.Direction$AxisDirection
 */
package com.copycatsplus.copycats.foundation.copycat.model.assembly.quad;

import com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableAABB;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableQuad;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableVertex;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.quad.QuadTransform;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;

public record QuadAutoCull(MutableAABB cullingBox) implements QuadTransform
{
    public static QuadAutoCull BLOCK = new QuadAutoCull(new MutableAABB(1.0, 1.0, 1.0));
    private static final double EPSILON = 0.00125;

    @Override
    public boolean transformQuad(MutableQuad quad, TextureAtlasSprite sprite) {
        Direction lightFace = quad.computeLightFace();
        Direction.Axis axis = lightFace.getAxis();
        double target = lightFace.getAxisDirection() == Direction.AxisDirection.POSITIVE ? this.cullingBox.getMax(axis) : this.cullingBox.getMin(axis);
        for (MutableVertex vertex : quad.vertices) {
            if (!(Math.abs(vertex.xyz.get(axis) - target) > 0.00125)) continue;
            quad.cullFace = null;
            quad.disableFinalAutoCull = true;
            return true;
        }
        quad.cullFace = lightFace;
        quad.disableFinalAutoCull = true;
        return true;
    }
}

