/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.doubles.DoubleList
 *  net.minecraft.core.Direction$Axis
 *  net.minecraft.world.phys.AABB
 *  net.minecraft.world.phys.shapes.BitSetDiscreteVoxelShape
 *  net.minecraft.world.phys.shapes.DiscreteVoxelShape
 *  net.minecraft.world.phys.shapes.VoxelShape
 */
package com.copycatsplus.copycats.utility.shape;

import it.unimi.dsi.fastutil.doubles.DoubleList;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.BitSetDiscreteVoxelShape;
import net.minecraft.world.phys.shapes.DiscreteVoxelShape;
import net.minecraft.world.phys.shapes.VoxelShape;

public abstract class ExtensibleVoxelShape
extends VoxelShape {
    public ExtensibleVoxelShape(VoxelShape voxelProvider) {
        super(ExtensibleVoxelShape.copyDiscreteShape(voxelProvider));
    }

    private static DiscreteVoxelShape copyDiscreteShape(VoxelShape source) {
        DoubleList xs = source.getCoords(Direction.Axis.X);
        DoubleList ys = source.getCoords(Direction.Axis.Y);
        DoubleList zs = source.getCoords(Direction.Axis.Z);
        BitSetDiscreteVoxelShape copy = new BitSetDiscreteVoxelShape(xs.size() - 1, ys.size() - 1, zs.size() - 1);
        for (AABB box : source.toAabbs()) {
            int minX = ExtensibleVoxelShape.findCoordinate(xs, box.minX);
            int minY = ExtensibleVoxelShape.findCoordinate(ys, box.minY);
            int minZ = ExtensibleVoxelShape.findCoordinate(zs, box.minZ);
            int maxX = ExtensibleVoxelShape.findCoordinate(xs, box.maxX);
            int maxY = ExtensibleVoxelShape.findCoordinate(ys, box.maxY);
            int maxZ = ExtensibleVoxelShape.findCoordinate(zs, box.maxZ);
            for (int x = minX; x < maxX; ++x) {
                for (int y = minY; y < maxY; ++y) {
                    for (int z = minZ; z < maxZ; ++z) {
                        copy.fill(x, y, z);
                    }
                }
            }
        }
        return copy;
    }

    private static int findCoordinate(DoubleList coordinates, double value) {
        for (int i = 0; i < coordinates.size(); ++i) {
            if (!(Math.abs(coordinates.getDouble(i) - value) < 1.0E-7)) continue;
            return i;
        }
        throw new IllegalArgumentException("Voxel shape box does not align with its coordinate grid");
    }

    public abstract DoubleList getCoords(Direction.Axis var1);
}

