/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.core.Direction
 *  net.minecraft.core.Direction$Axis
 *  org.jetbrains.annotations.Contract
 */
package com.copycatsplus.copycats.foundation.copycat.model.assembly;

import com.copycatsplus.copycats.foundation.copycat.model.assembly.AssemblyTransform;
import javax.annotation.Nullable;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.Contract;

public record Mutation(MutationType type, int value) {
    public <T extends AssemblyTransform.Transformable<T>> T mutate(T vec3) {
        return switch (this.type.ordinal()) {
            default -> throw new MatchException(null, null);
            case 0 -> vec3.rotateX(this.value);
            case 1 -> vec3.rotateY(this.value);
            case 2 -> vec3.rotateZ(this.value);
            case 3 -> this.value == 0 ? vec3.flipX(true) : (this.value == 1 ? vec3.flipY(true) : (this.value == 2 ? vec3.flipZ(true) : vec3));
        };
    }

    @Nullable
    @Contract(value="null -> null")
    public Direction mutate(@Nullable Direction dir) {
        if (dir == null) {
            return null;
        }
        return switch (this.type.ordinal()) {
            default -> throw new MatchException(null, null);
            case 0 -> Mutation.rotateX(dir, this.value);
            case 1 -> Mutation.rotateY(dir, this.value);
            case 2 -> Mutation.rotateZ(dir, this.value);
            case 3 -> this.value == 0 && dir.getAxis() == Direction.Axis.X ? dir.getOpposite() : (this.value == 1 && dir.getAxis() == Direction.Axis.Y ? dir.getOpposite() : (this.value == 2 && dir.getAxis() == Direction.Axis.Z ? dir.getOpposite() : dir));
        };
    }

    public <T extends AssemblyTransform.Transformable<T>> T undoMutate(T vec3) {
        return switch (this.type.ordinal()) {
            default -> throw new MatchException(null, null);
            case 0 -> vec3.rotateX(-this.value);
            case 1 -> vec3.rotateY(-this.value);
            case 2 -> vec3.rotateZ(-this.value);
            case 3 -> this.value == 0 ? vec3.flipX(true) : (this.value == 1 ? vec3.flipY(true) : (this.value == 2 ? vec3.flipZ(true) : vec3));
        };
    }

    @Nullable
    @Contract(value="null -> null")
    public Direction undoMutate(@Nullable Direction dir) {
        if (dir == null) {
            return null;
        }
        return switch (this.type.ordinal()) {
            default -> throw new MatchException(null, null);
            case 0 -> Mutation.rotateX(dir, -this.value);
            case 1 -> Mutation.rotateY(dir, -this.value);
            case 2 -> Mutation.rotateZ(dir, -this.value);
            case 3 -> this.value == 0 && dir.getAxis() == Direction.Axis.X ? dir.getOpposite() : (this.value == 1 && dir.getAxis() == Direction.Axis.Y ? dir.getOpposite() : (this.value == 2 && dir.getAxis() == Direction.Axis.Z ? dir.getOpposite() : dir));
        };
    }

    @Nullable
    @Contract(value="null, _ -> null")
    private static Direction rotateX(@Nullable Direction dir, int angle) {
        if (dir == null) {
            return null;
        }
        if ((angle %= 360) < 0) {
            angle += 360;
        }
        return switch (angle) {
            case 90 -> {
                switch (dir) {
                    case NORTH: {
                        yield Direction.DOWN;
                    }
                    case SOUTH: {
                        yield Direction.UP;
                    }
                    case UP: {
                        yield Direction.NORTH;
                    }
                    case DOWN: {
                        yield Direction.SOUTH;
                    }
                }
                yield dir;
            }
            case 180 -> {
                switch (dir) {
                    case NORTH: {
                        yield Direction.SOUTH;
                    }
                    case SOUTH: {
                        yield Direction.NORTH;
                    }
                    case UP: {
                        yield Direction.DOWN;
                    }
                    case DOWN: {
                        yield Direction.UP;
                    }
                }
                yield dir;
            }
            case 270 -> {
                switch (dir) {
                    case NORTH: {
                        yield Direction.UP;
                    }
                    case SOUTH: {
                        yield Direction.DOWN;
                    }
                    case UP: {
                        yield Direction.SOUTH;
                    }
                    case DOWN: {
                        yield Direction.NORTH;
                    }
                }
                yield dir;
            }
            default -> dir;
        };
    }

    @Nullable
    @Contract(value="null, _ -> null")
    private static Direction rotateY(@Nullable Direction dir, int angle) {
        if (dir == null) {
            return null;
        }
        if ((angle %= 360) < 0) {
            angle += 360;
        }
        return switch (angle) {
            case 90 -> {
                switch (dir) {
                    case NORTH: {
                        yield Direction.EAST;
                    }
                    case SOUTH: {
                        yield Direction.WEST;
                    }
                    case EAST: {
                        yield Direction.SOUTH;
                    }
                    case WEST: {
                        yield Direction.NORTH;
                    }
                }
                yield dir;
            }
            case 180 -> {
                switch (dir) {
                    case NORTH: {
                        yield Direction.SOUTH;
                    }
                    case SOUTH: {
                        yield Direction.NORTH;
                    }
                    case EAST: {
                        yield Direction.WEST;
                    }
                    case WEST: {
                        yield Direction.EAST;
                    }
                }
                yield dir;
            }
            case 270 -> {
                switch (dir) {
                    case NORTH: {
                        yield Direction.WEST;
                    }
                    case SOUTH: {
                        yield Direction.EAST;
                    }
                    case EAST: {
                        yield Direction.NORTH;
                    }
                    case WEST: {
                        yield Direction.SOUTH;
                    }
                }
                yield dir;
            }
            default -> dir;
        };
    }

    @Nullable
    @Contract(value="null, _ -> null")
    private static Direction rotateZ(@Nullable Direction dir, int angle) {
        if (dir == null) {
            return null;
        }
        if ((angle %= 360) < 0) {
            angle += 360;
        }
        return switch (angle) {
            case 90 -> {
                switch (dir) {
                    case UP: {
                        yield Direction.EAST;
                    }
                    case DOWN: {
                        yield Direction.WEST;
                    }
                    case EAST: {
                        yield Direction.DOWN;
                    }
                    case WEST: {
                        yield Direction.UP;
                    }
                }
                yield dir;
            }
            case 180 -> {
                switch (dir) {
                    case UP: {
                        yield Direction.DOWN;
                    }
                    case DOWN: {
                        yield Direction.UP;
                    }
                    case EAST: {
                        yield Direction.WEST;
                    }
                    case WEST: {
                        yield Direction.EAST;
                    }
                }
                yield dir;
            }
            case 270 -> {
                switch (dir) {
                    case UP: {
                        yield Direction.WEST;
                    }
                    case DOWN: {
                        yield Direction.EAST;
                    }
                    case EAST: {
                        yield Direction.UP;
                    }
                    case WEST: {
                        yield Direction.DOWN;
                    }
                }
                yield dir;
            }
            default -> dir;
        };
    }

    public static enum MutationType {
        ROTATE_X,
        ROTATE_Y,
        ROTATE_Z,
        MIRROR;

    }
}

