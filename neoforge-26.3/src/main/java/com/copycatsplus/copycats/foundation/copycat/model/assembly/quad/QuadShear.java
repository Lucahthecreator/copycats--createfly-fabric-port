package com.copycatsplus.copycats.foundation.copycat.model.assembly.quad;

import com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableQuad;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableVec3;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;

public record QuadShear(Axis axis, Direction direction, double amount) implements QuadTransform {
    @Override
    public boolean transformQuad(MutableQuad quad, TextureAtlasSprite sprite) {
        for (int i = 0; i < 4; i++) {
            MutableVec3 vertex = quad.vertices.get(i).xyz;
            double shearAxis = vertex.get(this.axis);
            double amount = this.amount * (this.direction.getAxisDirection() == AxisDirection.POSITIVE ? 1 : -1);
            vertex.set(this.direction.getAxis(), vertex.get(this.direction.getAxis()) + shearAxis * amount);
        }

        return true;
    }
}
