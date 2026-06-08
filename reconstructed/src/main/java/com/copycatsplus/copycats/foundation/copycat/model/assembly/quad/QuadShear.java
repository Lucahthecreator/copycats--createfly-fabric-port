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

import com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableQuad;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableVec3;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.quad.QuadTransform;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;

public record QuadShear(Direction.Axis axis, Direction direction, double amount) implements QuadTransform
{
    @Override
    public boolean transformQuad(MutableQuad quad, TextureAtlasSprite sprite) {
        for (int i = 0; i < 4; ++i) {
            MutableVec3 vertex = quad.vertices.get((int)i).xyz;
            double shearAxis = vertex.get(this.axis);
            double amount = this.amount * (double)(this.direction.getAxisDirection() == Direction.AxisDirection.POSITIVE ? 1 : -1);
            vertex.set(this.direction.getAxis(), vertex.get(this.direction.getAxis()) + shearAxis * amount);
        }
        return true;
    }
}

