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

public record QuadSlope(Direction face, QuadSlopeFunction func) implements QuadTransform
{
    private static final double EPSILON = 0.00125;

    @Override
    public boolean transformQuad(MutableQuad quad, TextureAtlasSprite sprite) {
        block10: for (int i = 0; i < 4; ++i) {
            double a;
            MutableVec3 vertex = quad.vertices.get((int)i).xyz;
            double output = this.func.apply(a, switch (this.face.getAxis()) {
                case Direction.Axis.X -> {
                    a = vertex.y;
                    yield vertex.z;
                }
                case Direction.Axis.Y -> {
                    a = vertex.x;
                    yield vertex.z;
                }
                case Direction.Axis.Z -> {
                    a = vertex.x;
                    yield vertex.y;
                }
                default -> throw new RuntimeException("Unexpected value: " + String.valueOf(this.face.getAxis()));
            });
            if (Math.abs(output) < 0.00125) {
                output = 0.00125;
            }
            switch (this.face.getAxis()) {
                case X: {
                    if (this.face.getAxisDirection() == Direction.AxisDirection.POSITIVE) {
                        vertex.x *= output;
                        continue block10;
                    }
                    vertex.x = 1.0 - output * (1.0 - vertex.x);
                    continue block10;
                }
                case Y: {
                    if (this.face.getAxisDirection() == Direction.AxisDirection.POSITIVE) {
                        vertex.y *= output;
                        continue block10;
                    }
                    vertex.y = 1.0 - output * (1.0 - vertex.y);
                    continue block10;
                }
                case Z: {
                    if (this.face.getAxisDirection() == Direction.AxisDirection.POSITIVE) {
                        vertex.z *= output;
                        continue block10;
                    }
                    vertex.z = 1.0 - output * (1.0 - vertex.z);
                    continue block10;
                }
                default: {
                    throw new RuntimeException("Unexpected value: " + String.valueOf(this.face.getAxis()));
                }
            }
        }
        return true;
    }

    public static double map(double fromStart, double fromEnd, double toStart, double toEnd, double value) {
        return toStart + (value - fromStart) / (fromEnd - fromStart) * (toEnd - toStart);
    }

    @FunctionalInterface
    public static interface QuadSlopeFunction {
        public double apply(double var1, double var3);
    }
}

