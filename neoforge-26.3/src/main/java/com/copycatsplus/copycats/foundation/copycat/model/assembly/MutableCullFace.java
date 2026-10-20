package com.copycatsplus.copycats.foundation.copycat.model.assembly;

import net.minecraft.core.Direction;

public class MutableCullFace implements AssemblyTransform.Transformable<MutableCullFace> {
    public static final int UP = 2 << Direction.UP.get3DDataValue();
    public static final int DOWN = 2 << Direction.DOWN.get3DDataValue();
    public static final int NORTH = 2 << Direction.NORTH.get3DDataValue();
    public static final int EAST = 2 << Direction.EAST.get3DDataValue();
    public static final int SOUTH = 2 << Direction.SOUTH.get3DDataValue();
    public static final int WEST = 2 << Direction.WEST.get3DDataValue();
    public boolean up;
    public boolean down;
    public boolean north;
    public boolean south;
    public boolean east;
    public boolean west;

    public MutableCullFace(int mask) {
        this.set((mask & UP) > 0, (mask & DOWN) > 0, (mask & NORTH) > 0, (mask & SOUTH) > 0, (mask & EAST) > 0, (mask & WEST) > 0);
    }

    public MutableCullFace rotateY(int angle) {
        angle %= 360;
        if (angle < 0) {
            angle += 360;
        }
        return switch (angle) {
            case 90 -> this.set(this.up, this.down, this.west, this.east, this.north, this.south);
            case 180 -> this.set(this.up, this.down, this.south, this.north, this.west, this.east);
            case 270 -> this.set(this.up, this.down, this.east, this.west, this.south, this.north);
            default -> this;
        };
    }

    public MutableCullFace rotateX(int angle) {
        angle %= 360;
        if (angle < 0) {
            angle += 360;
        }
        return switch (angle) {
            case 90 -> this.set(this.south, this.north, this.up, this.down, this.east, this.west);
            case 180 -> this.set(this.down, this.up, this.south, this.north, this.east, this.west);
            case 270 -> this.set(this.north, this.south, this.down, this.up, this.east, this.west);
            default -> this;
        };
    }

    public MutableCullFace rotateZ(int angle) {
        angle %= 360;
        if (angle < 0) {
            angle += 360;
        }
        return switch (angle) {
            case 90 -> this.set(this.west, this.east, this.north, this.south, this.up, this.down);
            case 180 -> this.set(this.down, this.up, this.north, this.south, this.west, this.east);
            case 270 -> this.set(this.east, this.west, this.north, this.south, this.down, this.up);
            default -> this;
        };
    }

    public MutableCullFace flipX(boolean flip) {
        return !flip ? this : this.set(this.up, this.down, this.north, this.south, this.west, this.east);
    }

    public MutableCullFace flipY(boolean flip) {
        return !flip ? this : this.set(this.down, this.up, this.north, this.south, this.east, this.west);
    }

    public MutableCullFace flipZ(boolean flip) {
        return !flip ? this : this.set(this.up, this.down, this.south, this.north, this.east, this.west);
    }

    public boolean isCulled(Direction direction) {
        return switch (direction) {
            case DOWN -> this.down;
            case UP -> this.up;
            case NORTH -> this.north;
            case SOUTH -> this.south;
            case WEST -> this.west;
            case EAST -> this.east;
        };
    }

    public MutableCullFace set(boolean up, boolean down, boolean north, boolean south, boolean east, boolean west) {
        this.up = up;
        this.down = down;
        this.north = north;
        this.south = south;
        this.east = east;
        this.west = west;
        return this;
    }
}
