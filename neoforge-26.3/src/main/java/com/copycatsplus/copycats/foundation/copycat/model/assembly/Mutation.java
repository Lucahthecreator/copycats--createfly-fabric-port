package com.copycatsplus.copycats.foundation.copycat.model.assembly;

import javax.annotation.Nullable;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import org.jetbrains.annotations.Contract;

public record Mutation(Mutation.MutationType type, int value) {
    public <T extends AssemblyTransform.Transformable<T>> T mutate(T vec3) {
        return (T)(switch (this.type) {
            case ROTATE_X -> vec3.rotateX(this.value);
            case ROTATE_Y -> vec3.rotateY(this.value);
            case ROTATE_Z -> vec3.rotateZ(this.value);
            case MIRROR -> this.value == 0 ? vec3.flipX(true) : (this.value == 1 ? vec3.flipY(true) : (this.value == 2 ? vec3.flipZ(true) : vec3));
        });
    }

    @Nullable
    @Contract("null -> null")
    public Direction mutate(@Nullable Direction dir) {
        if (dir == null) {
            return null;
        } else {
            return switch (this.type) {
                case ROTATE_X -> rotateX(dir, this.value);
                case ROTATE_Y -> rotateY(dir, this.value);
                case ROTATE_Z -> rotateZ(dir, this.value);
                case MIRROR -> this.value == 0 && dir.getAxis() == Axis.X
                    ? dir.getOpposite()
                    : (this.value == 1 && dir.getAxis() == Axis.Y ? dir.getOpposite() : (this.value == 2 && dir.getAxis() == Axis.Z ? dir.getOpposite() : dir));
            };
        }
    }

    public <T extends AssemblyTransform.Transformable<T>> T undoMutate(T vec3) {
        return (T)(switch (this.type) {
            case ROTATE_X -> vec3.rotateX(-this.value);
            case ROTATE_Y -> vec3.rotateY(-this.value);
            case ROTATE_Z -> vec3.rotateZ(-this.value);
            case MIRROR -> this.value == 0 ? vec3.flipX(true) : (this.value == 1 ? vec3.flipY(true) : (this.value == 2 ? vec3.flipZ(true) : vec3));
        });
    }

    @Nullable
    @Contract("null -> null")
    public Direction undoMutate(@Nullable Direction dir) {
        if (dir == null) {
            return null;
        } else {
            return switch (this.type) {
                case ROTATE_X -> rotateX(dir, -this.value);
                case ROTATE_Y -> rotateY(dir, -this.value);
                case ROTATE_Z -> rotateZ(dir, -this.value);
                case MIRROR -> this.value == 0 && dir.getAxis() == Axis.X
                    ? dir.getOpposite()
                    : (this.value == 1 && dir.getAxis() == Axis.Y ? dir.getOpposite() : (this.value == 2 && dir.getAxis() == Axis.Z ? dir.getOpposite() : dir));
            };
        }
    }

    @Nullable
    @Contract("null, _ -> null")
    private static Direction rotateX(@Nullable Direction dir, int angle) {
        if (dir == null) {
            return null;
        } else {
            angle %= 360;
            if (angle < 0) {
                angle += 360;
            }
            return switch (angle) {
                case 90 -> {
                    switch (dir) {
                        case NORTH:
                            yield Direction.DOWN;
                        case SOUTH:
                            yield Direction.UP;
                        case UP:
                            yield Direction.NORTH;
                        case DOWN:
                            yield Direction.SOUTH;
                        default:
                            yield dir;
                    }
                }
                case 180 -> {
                    switch (dir) {
                        case NORTH:
                            yield Direction.SOUTH;
                        case SOUTH:
                            yield Direction.NORTH;
                        case UP:
                            yield Direction.DOWN;
                        case DOWN:
                            yield Direction.UP;
                        default:
                            yield dir;
                    }
                }
                case 270 -> {
                    switch (dir) {
                        case NORTH:
                            yield Direction.UP;
                        case SOUTH:
                            yield Direction.DOWN;
                        case UP:
                            yield Direction.SOUTH;
                        case DOWN:
                            yield Direction.NORTH;
                        default:
                            yield dir;
                    }
                }
                default -> dir;
            };
        }
    }

    @Nullable
    @Contract("null, _ -> null")
    private static Direction rotateY(@Nullable Direction dir, int angle) {
        if (dir == null) {
            return null;
        } else {
            angle %= 360;
            if (angle < 0) {
                angle += 360;
            }
            return switch (angle) {
                case 90 -> {
                    switch (dir) {
                        case NORTH:
                            yield Direction.EAST;
                        case SOUTH:
                            yield Direction.WEST;
                        case UP:
                        case DOWN:
                        default:
                            yield dir;
                        case EAST:
                            yield Direction.SOUTH;
                        case WEST:
                            yield Direction.NORTH;
                    }
                }
                case 180 -> {
                    switch (dir) {
                        case NORTH:
                            yield Direction.SOUTH;
                        case SOUTH:
                            yield Direction.NORTH;
                        case UP:
                        case DOWN:
                        default:
                            yield dir;
                        case EAST:
                            yield Direction.WEST;
                        case WEST:
                            yield Direction.EAST;
                    }
                }
                case 270 -> {
                    switch (dir) {
                        case NORTH:
                            yield Direction.WEST;
                        case SOUTH:
                            yield Direction.EAST;
                        case UP:
                        case DOWN:
                        default:
                            yield dir;
                        case EAST:
                            yield Direction.NORTH;
                        case WEST:
                            yield Direction.SOUTH;
                    }
                }
                default -> dir;
            };
        }
    }

    @Nullable
    @Contract("null, _ -> null")
    private static Direction rotateZ(@Nullable Direction dir, int angle) {
        if (dir == null) {
            return null;
        } else {
            angle %= 360;
            if (angle < 0) {
                angle += 360;
            }
            return switch (angle) {
                case 90 -> {
                    switch (dir) {
                        case UP:
                            yield Direction.EAST;
                        case DOWN:
                            yield Direction.WEST;
                        case EAST:
                            yield Direction.DOWN;
                        case WEST:
                            yield Direction.UP;
                        default:
                            yield dir;
                    }
                }
                case 180 -> {
                    switch (dir) {
                        case UP:
                            yield Direction.DOWN;
                        case DOWN:
                            yield Direction.UP;
                        case EAST:
                            yield Direction.WEST;
                        case WEST:
                            yield Direction.EAST;
                        default:
                            yield dir;
                    }
                }
                case 270 -> {
                    switch (dir) {
                        case UP:
                            yield Direction.WEST;
                        case DOWN:
                            yield Direction.EAST;
                        case EAST:
                            yield Direction.UP;
                        case WEST:
                            yield Direction.DOWN;
                        default:
                            yield dir;
                    }
                }
                default -> dir;
            };
        }
    }

    public static enum MutationType {
        ROTATE_X,
        ROTATE_Y,
        ROTATE_Z,
        MIRROR;
    }
}
