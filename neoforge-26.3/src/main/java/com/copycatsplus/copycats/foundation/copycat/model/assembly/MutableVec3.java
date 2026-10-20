package com.copycatsplus.copycats.foundation.copycat.model.assembly;

import net.minecraft.core.Position;
import net.minecraft.core.Direction.Axis;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class MutableVec3 implements AssemblyTransform.Transformable<MutableVec3>, Position {
    public double x;
    public double y;
    public double z;

    public MutableVec3(Position position) {
        this(position.x(), position.y(), position.z());
    }

    public MutableVec3(double x, double y, double z) {
        this.set(x, y, z);
    }

    public MutableVec3 rotateY(int angle) {
        angle %= 360;
        if (angle < 0) {
            angle += 360;
        }
        return switch (angle) {
            case 90 -> this.set(1.0 - this.z, this.y, this.x);
            case 180 -> this.set(1.0 - this.x, this.y, 1.0 - this.z);
            case 270 -> this.set(this.z, this.y, 1.0 - this.x);
            default -> this;
        };
    }

    public MutableVec3 rotateX(int angle) {
        angle %= 360;
        if (angle < 0) {
            angle += 360;
        }
        return switch (angle) {
            case 90 -> this.set(this.x, this.z, 1.0 - this.y);
            case 180 -> this.set(this.x, 1.0 - this.y, 1.0 - this.z);
            case 270 -> this.set(this.x, 1.0 - this.z, this.y);
            default -> this;
        };
    }

    public MutableVec3 rotateZ(int angle) {
        angle %= 360;
        if (angle < 0) {
            angle += 360;
        }
        return switch (angle) {
            case 90 -> this.set(this.y, 1.0 - this.x, this.z);
            case 180 -> this.set(1.0 - this.x, 1.0 - this.y, this.z);
            case 270 -> this.set(1.0 - this.y, this.x, this.z);
            default -> this;
        };
    }

    public MutableVec3 flipX(boolean flip) {
        return !flip ? this : this.set(1.0 - this.x, this.y, this.z);
    }

    public MutableVec3 flipY(boolean flip) {
        return !flip ? this : this.set(this.x, 1.0 - this.y, this.z);
    }

    public MutableVec3 flipZ(boolean flip) {
        return !flip ? this : this.set(this.x, this.y, 1.0 - this.z);
    }

    public Vec3 toVec3() {
        return new Vec3(this.x, this.y, this.z);
    }

    public MutableVec3 set(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public double get(Axis axis) {
        return switch (axis) {
            case X -> this.x;
            case Y -> this.y;
            case Z -> this.z;
        };
    }

    public MutableVec3 set(Axis axis, double value) {
        switch (axis) {
            case X:
                this.x = value;
                break;
            case Y:
                this.y = value;
                break;
            case Z:
                this.z = value;
        }

        return this;
    }

    public boolean isZero() {
        return this.x == 0.0 && this.y == 0.0 && this.z == 0.0;
    }

    public MutableVec3 add(Position vec) {
        return this.add(vec.x(), vec.y(), vec.z());
    }

    public MutableVec3 add(double x, double y, double z) {
        return this.set(this.x + x, this.y + y, this.z + z);
    }

    public MutableVec3 subtract(Position vec) {
        return this.set(this.x - vec.x(), this.y - vec.y(), this.z - vec.z());
    }

    public MutableVec3 scale(double scale) {
        return this.set(this.x * scale, this.y * scale, this.z * scale);
    }

    public MutableVec3 multiply(Position vec) {
        return this.set(this.x * vec.x(), this.y * vec.y(), this.z * vec.z());
    }

    public MutableVec3 rotate(Position rotationVec) {
        return this.rotate(rotationVec.x(), rotationVec.y(), rotationVec.z());
    }

    public MutableVec3 rotate(double xRot, double yRot, double zRot) {
        this.rotate(xRot, Axis.X);
        this.rotate(yRot, Axis.Y);
        this.rotate(zRot, Axis.Z);
        return this;
    }

    public MutableVec3 rotate(double deg, Axis axis) {
        if (deg == 0.0) {
            return this;
        } else if (this.isZero()) {
            return this;
        } else {
            float angle = (float)(deg / 180.0 * Math.PI);
            double sin = Mth.sin(angle);
            double cos = Mth.cos(angle);
            double x = this.x;
            double y = this.y;
            double z = this.z;
            if (axis == Axis.X) {
                this.y = y * cos - z * sin;
                this.z = z * cos + y * sin;
            } else if (axis == Axis.Y) {
                this.x = x * cos + z * sin;
                this.z = z * cos - x * sin;
            } else if (axis == Axis.Z) {
                this.x = x * cos - y * sin;
                this.y = y * cos + x * sin;
            }

            return this;
        }
    }

    public double dot(Position vec) {
        return this.x * vec.x() + this.y * vec.y() + this.z * vec.z();
    }

    public MutableVec3 cross(Position vec) {
        return this.set(this.y * vec.z() - this.z * vec.y(), this.z * vec.x() - this.x * vec.z(), this.x * vec.y() - this.y * vec.x());
    }

    public double distanceTo(Position vec) {
        double d = vec.x() - this.x;
        double e = vec.y() - this.y;
        double f = vec.z() - this.z;
        return Math.sqrt(d * d + e * e + f * f);
    }

    public double distanceToSqr(Position vec) {
        double d = vec.x() - this.x;
        double e = vec.y() - this.y;
        double f = vec.z() - this.z;
        return d * d + e * e + f * f;
    }

    public double distanceToSqr(double x, double y, double z) {
        double d = x - this.x;
        double e = y - this.y;
        double f = z - this.z;
        return d * d + e * e + f * f;
    }

    public double length() {
        return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
    }

    public double lengthSqr() {
        return this.x * this.x + this.y * this.y + this.z * this.z;
    }

    public double horizontalDistance() {
        return Math.sqrt(this.x * this.x + this.z * this.z);
    }

    public double horizontalDistanceSqr() {
        return this.x * this.x + this.z * this.z;
    }

    public MutableVec3 normalize() {
        double d = Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
        return d < 1.0E-4 ? this.set(0.0, 0.0, 0.0) : this.set(this.x / d, this.y / d, this.z / d);
    }

    public MutableVec3 copy() {
        return new MutableVec3(this.x, this.y, this.z);
    }

    @Override
    public String toString() {
        return "(" + this.x + ", " + this.y + ", " + this.z + ")";
    }

    @Override
    public double x() {
        return this.x;
    }

    @Override
    public double y() {
        return this.y;
    }

    @Override
    public double z() {
        return this.z;
    }

    public static class AsAngle extends MutableVec3 {
        public AsAngle(double x, double y, double z) {
            super(x, y, z);
        }
    }

    public static class AsPivot extends MutableVec3 {
        public AsPivot(double x, double y, double z) {
            super(x, y, z);
        }
    }

    public static class AsScale extends MutableVec3 {
        public AsScale(double x, double y, double z) {
            super(x, y, z);
        }
    }
}
