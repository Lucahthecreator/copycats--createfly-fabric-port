/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Direction$Axis
 *  net.minecraft.world.phys.AABB
 */
package com.copycatsplus.copycats.foundation.copycat.model.assembly;

import com.copycatsplus.copycats.foundation.copycat.model.assembly.AssemblyTransform;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;

public class MutableAABB
implements AssemblyTransform.Transformable<MutableAABB> {
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
        this.minX += (dX /= 16.0);
        this.maxX += dX;
        this.minY += (dY /= 16.0);
        this.maxY += dY;
        this.minZ += (dZ /= 16.0);
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

    @Override
    public MutableAABB rotateY(int angle) {
        if ((angle %= 360) < 0) {
            angle += 360;
        }
        return switch (angle) {
            case 90 -> this.set(1.0 - this.minZ, this.minY, this.minX, 1.0 - this.maxZ, this.maxY, this.maxX);
            case 180 -> this.set(1.0 - this.minX, this.minY, 1.0 - this.minZ, 1.0 - this.maxX, this.maxY, 1.0 - this.maxZ);
            case 270 -> this.set(this.minZ, this.minY, 1.0 - this.minX, this.maxZ, this.maxY, 1.0 - this.maxX);
            default -> this;
        };
    }

    @Override
    public MutableAABB rotateX(int angle) {
        if ((angle %= 360) < 0) {
            angle += 360;
        }
        return switch (angle) {
            case 90 -> this.set(this.minX, this.minZ, 1.0 - this.minY, this.maxX, this.maxZ, 1.0 - this.maxY);
            case 180 -> this.set(this.minX, 1.0 - this.minY, 1.0 - this.minZ, this.maxX, 1.0 - this.maxY, 1.0 - this.maxZ);
            case 270 -> this.set(this.minX, 1.0 - this.minZ, this.minY, this.maxX, 1.0 - this.maxZ, this.maxY);
            default -> this;
        };
    }

    @Override
    public MutableAABB rotateZ(int angle) {
        if ((angle %= 360) < 0) {
            angle += 360;
        }
        return switch (angle) {
            case 90 -> this.set(this.minY, 1.0 - this.minX, this.minZ, this.maxY, 1.0 - this.maxX, this.maxZ);
            case 180 -> this.set(1.0 - this.minX, 1.0 - this.minY, this.minZ, 1.0 - this.maxX, 1.0 - this.maxY, this.maxZ);
            case 270 -> this.set(1.0 - this.minY, this.minX, this.minZ, 1.0 - this.maxY, this.maxX, this.maxZ);
            default -> this;
        };
    }

    @Override
    public MutableAABB flipX(boolean flip) {
        if (!flip) {
            return this;
        }
        return this.set(1.0 - this.minX, this.minY, this.minZ, 1.0 - this.maxX, this.maxY, this.maxZ);
    }

    @Override
    public MutableAABB flipY(boolean flip) {
        if (!flip) {
            return this;
        }
        return this.set(this.minX, 1.0 - this.minY, this.minZ, this.maxX, 1.0 - this.maxY, this.maxZ);
    }

    @Override
    public MutableAABB flipZ(boolean flip) {
        if (!flip) {
            return this;
        }
        return this.set(this.minX, this.minY, 1.0 - this.minZ, this.maxX, this.maxY, 1.0 - this.maxZ);
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

    public double getMin(Direction.Axis axis) {
        return switch (axis) {
            default -> throw new MatchException(null, null);
            case Direction.Axis.X -> this.minX;
            case Direction.Axis.Y -> this.minY;
            case Direction.Axis.Z -> this.minZ;
        };
    }

    public double getMax(Direction.Axis axis) {
        return switch (axis) {
            default -> throw new MatchException(null, null);
            case Direction.Axis.X -> this.maxX;
            case Direction.Axis.Y -> this.maxY;
            case Direction.Axis.Z -> this.maxZ;
        };
    }
}

