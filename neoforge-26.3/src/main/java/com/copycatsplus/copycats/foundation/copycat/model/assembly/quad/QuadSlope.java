package com.copycatsplus.copycats.foundation.copycat.model.assembly.quad;

import com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableQuad;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableVec3;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.AxisDirection;

public record QuadSlope(Direction face, QuadSlope.QuadSlopeFunction func) implements QuadTransform {
    private static final double EPSILON = 0.00125;

    @Override
    public boolean transformQuad(MutableQuad quad, TextureAtlasSprite sprite) {
        for (int i = 0; i < 4; i++) {
            MutableVec3 vertex = quad.vertices.get(i).xyz;
            double a;
            double b;
            switch (this.face.getAxis()) {
                case X:
                    a = vertex.y;
                    b = vertex.z;
                    break;
                case Y:
                    a = vertex.x;
                    b = vertex.z;
                    break;
                case Z:
                    a = vertex.x;
                    b = vertex.y;
                    break;
                default:
                    throw new RuntimeException("Unexpected value: " + this.face.getAxis());
            }

            double output = this.func.apply(a, b);
            if (Math.abs(output) < 0.00125) {
                output = 0.00125;
            }

            switch (this.face.getAxis()) {
                case X:
                    if (this.face.getAxisDirection() == AxisDirection.POSITIVE) {
                        vertex.x *= output;
                    } else {
                        vertex.x = 1.0 - output * (1.0 - vertex.x);
                    }
                    break;
                case Y:
                    if (this.face.getAxisDirection() == AxisDirection.POSITIVE) {
                        vertex.y *= output;
                    } else {
                        vertex.y = 1.0 - output * (1.0 - vertex.y);
                    }
                    break;
                case Z:
                    if (this.face.getAxisDirection() == AxisDirection.POSITIVE) {
                        vertex.z *= output;
                    } else {
                        vertex.z = 1.0 - output * (1.0 - vertex.z);
                    }
                    break;
                default:
                    throw new RuntimeException("Unexpected value: " + this.face.getAxis());
            }
        }

        return true;
    }

    public static double map(double fromStart, double fromEnd, double toStart, double toEnd, double value) {
        return toStart + (value - fromStart) / (fromEnd - fromStart) * (toEnd - toStart);
    }

    @FunctionalInterface
    public interface QuadSlopeFunction {
        double apply(double var1, double var3);
    }
}
