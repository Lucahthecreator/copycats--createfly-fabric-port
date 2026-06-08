/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Direction$Axis
 *  net.minecraft.core.Vec3i
 *  net.minecraft.util.Mth
 *  net.minecraft.world.phys.Vec3
 */
package com.copycatsplus.copycats.utility;

import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class MathUtils {
    public static Vec3i replaceAxis(Vec3i vec, Direction.Axis axis, int value) {
        return switch (axis) {
            default -> throw new MatchException(null, null);
            case Direction.Axis.X -> new Vec3i(value, vec.getY(), vec.getZ());
            case Direction.Axis.Y -> new Vec3i(vec.getX(), value, vec.getZ());
            case Direction.Axis.Z -> new Vec3i(vec.getX(), vec.getY(), value);
        };
    }

    public static Vec3 replaceAxis(Vec3 vec, Direction.Axis axis, double value) {
        return switch (axis) {
            default -> throw new MatchException(null, null);
            case Direction.Axis.X -> new Vec3(value, vec.y(), vec.z());
            case Direction.Axis.Y -> new Vec3(vec.x(), value, vec.z());
            case Direction.Axis.Z -> new Vec3(vec.x(), vec.y(), value);
        };
    }

    public static Vec3 clampToGrid(Vec3 vec, Vec3i pos) {
        return new Vec3(Mth.clamp((double)vec.x, (double)pos.getX(), (double)(pos.getX() + 1)), Mth.clamp((double)vec.y, (double)pos.getY(), (double)(pos.getY() + 1)), Mth.clamp((double)vec.z, (double)pos.getZ(), (double)(pos.getZ() + 1)));
    }
}

