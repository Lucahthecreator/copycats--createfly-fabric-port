package com.copycatsplus.copycats.utility.shape;

import it.unimi.dsi.fastutil.doubles.DoubleList;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.BitSetDiscreteVoxelShape;
import net.minecraft.world.phys.shapes.DiscreteVoxelShape;
import net.minecraft.world.phys.shapes.VoxelShape;

public abstract class ExtensibleVoxelShape extends VoxelShape {
    public ExtensibleVoxelShape(VoxelShape voxelProvider) {
        super(copyDiscreteShape(voxelProvider));
    }

    private static DiscreteVoxelShape copyDiscreteShape(VoxelShape source) {
        DoubleList xs = source.getCoords(Axis.X);
        DoubleList ys = source.getCoords(Axis.Y);
        DoubleList zs = source.getCoords(Axis.Z);
        BitSetDiscreteVoxelShape copy = new BitSetDiscreteVoxelShape(xs.size() - 1, ys.size() - 1, zs.size() - 1);

        for (AABB box : source.toAabbs()) {
            int minX = findCoordinate(xs, box.minX);
            int minY = findCoordinate(ys, box.minY);
            int minZ = findCoordinate(zs, box.minZ);
            int maxX = findCoordinate(xs, box.maxX);
            int maxY = findCoordinate(ys, box.maxY);
            int maxZ = findCoordinate(zs, box.maxZ);

            for (int x = minX; x < maxX; x++) {
                for (int y = minY; y < maxY; y++) {
                    for (int z = minZ; z < maxZ; z++) {
                        copy.fill(x, y, z);
                    }
                }
            }
        }

        return copy;
    }

    private static int findCoordinate(DoubleList coordinates, double value) {
        for (int i = 0; i < coordinates.size(); i++) {
            if (Math.abs(coordinates.getDouble(i) - value) < 1.0E-7) {
                return i;
            }
        }

        throw new IllegalArgumentException("Voxel shape box does not align with its coordinate grid");
    }

    @Override
    public abstract DoubleList getCoords(Axis var1);
}
