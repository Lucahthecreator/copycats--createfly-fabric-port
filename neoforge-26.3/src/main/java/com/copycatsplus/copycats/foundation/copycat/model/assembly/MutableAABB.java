package com.copycatsplus.copycats.foundation.copycat.model.assembly;

import net.minecraft.core.Direction.Axis;
import net.minecraft.world.phys.AABB;

public class MutableAABB implements AssemblyTransform.Transformable<MutableAABB> {
    public double minX;
    public double minY;
    public double minZ;
    public double maxX;
    public double maxY;
    public double maxZ;

    public MutableAABB(double sizeX, double sizeY, double sizeZ) {
        this.set(0.0, 0.0, 0.0, sizeX, sizeY, sizeZ);
    }

    public MutableAABB move(double dX, double dY, double dZ) {
        dX /= 16.0;
        dY /= 16.0;
        dZ /= 16.0;
        this.minX += dX;
        this.maxX += dX;
        this.minY += dY;
        this.maxY += dY;
        this.minZ += dZ;
        this.maxZ += dZ;
        return this;
    }

    public MutableAABB shift(double dX, double dY, double dZ) {
        this.minX += dX;
        this.maxX += dX;
        this.minY += dY;
        this.maxY += dY;
        this.minZ += dZ;
        this.maxZ += dZ;
        return this;
    }

    public MutableAABB rotateY(int angle) {
        angle %= 360;
        if (angle < 0) {
            angle += 360;
        }
        return switch (angle) {
            case 90 -> this.set(1.0 - this.minZ, this.minY, this.minX, 1.0 - this.maxZ, this.maxY, this.maxX);
            case 180 -> this.set(1.0 - this.minX, this.minY, 1.0 - this.minZ, 1.0 - this.maxX, this.maxY, 1.0 - this.maxZ);
            case 270 -> this.set(this.minZ, this.minY, 1.0 - this.minX, this.maxZ, this.maxY, 1.0 - this.maxX);
            default -> this;
        };
    }

    public MutableAABB rotateX(int angle) {
        angle %= 360;
        if (angle < 0) {
            angle += 360;
        }
        return switch (angle) {
            case 90 -> this.set(this.minX, this.minZ, 1.0 - this.minY, this.maxX, this.maxZ, 1.0 - this.maxY);
            case 180 -> this.set(this.minX, 1.0 - this.minY, 1.0 - this.minZ, this.maxX, 1.0 - this.maxY, 1.0 - this.maxZ);
            case 270 -> this.set(this.minX, 1.0 - this.minZ, this.minY, this.maxX, 1.0 - this.maxZ, this.maxY);
            default -> this;
        };
    }

    public MutableAABB rotateZ(int angle) {
        angle %= 360;
        if (angle < 0) {
            angle += 360;
        }
        return switch (angle) {
            case 90 -> this.set(this.minY, 1.0 - this.minX, this.minZ, this.maxY, 1.0 - this.maxX, this.maxZ);
            case 180 -> this.set(1.0 - this.minX, 1.0 - this.minY, this.minZ, 1.0 - this.maxX, 1.0 - this.maxY, this.maxZ);
            case 270 -> this.set(1.0 - this.minY, this.minX, this.minZ, 1.0 - this.maxY, this.maxX, this.maxZ);
            default -> this;
        };
    }

    public MutableAABB flipX(boolean flip) {
        return !flip ? this : this.set(1.0 - this.minX, this.minY, this.minZ, 1.0 - this.maxX, this.maxY, this.maxZ);
    }

    public MutableAABB flipY(boolean flip) {
        return !flip ? this : this.set(this.minX, 1.0 - this.minY, this.minZ, this.maxX, 1.0 - this.maxY, this.maxZ);
    }

    public MutableAABB flipZ(boolean flip) {
        return !flip ? this : this.set(this.minX, this.minY, 1.0 - this.minZ, this.maxX, this.maxY, 1.0 - this.maxZ);
    }

    public AABB toAABB() {
        return new AABB(this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ);
    }

    public MutableAABB copy() {
        return new MutableAABB(0.0, 0.0, 0.0).set(this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ);
    }

    public MutableAABB set(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
        return this;
    }

    public double getMin(Axis axis) {
        return switch (axis) {
            case X -> this.minX;
            case Y -> this.minY;
            case Z -> this.minZ;
        };
    }

    public double getMax(Axis axis) {
        return switch (axis) {
            case X -> this.maxX;
            case Y -> this.maxY;
            case Z -> this.maxZ;
        };
    }
}
