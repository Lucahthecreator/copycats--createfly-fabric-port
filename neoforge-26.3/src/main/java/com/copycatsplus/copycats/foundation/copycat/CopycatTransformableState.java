package com.copycatsplus.copycats.foundation.copycat;

import com.zurrtum.create.catnip.math.VecHelper;
import com.zurrtum.create.content.contraptions.StructureTransform;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.phys.Vec3;

public class CopycatTransformableState<T> {
    public List<CopycatTransformableState.Part<T>> parts = new LinkedList<>();

    public static <T> CopycatTransformableState<T> create(Consumer<CopycatTransformableState<T>> consumer) {
        CopycatTransformableState<T> state = new CopycatTransformableState<>();
        consumer.accept(state);
        return state;
    }

    public CopycatTransformableState.Part<T> addPart(int x, int y, int z) {
        CopycatTransformableState.Part<T> part = new CopycatTransformableState.Part<>(x, y, z);
        this.parts.add(part);
        return part;
    }

    public CopycatTransformableState<T> transform(StructureTransform transform) {
        this.parts.forEach(part -> part.transform(transform));
        return this;
    }

    public CopycatTransformableState<T> untransform(StructureTransform transform) {
        this.parts.forEach(part -> part.untransform(transform));
        return this;
    }

    public static class Part<T> {
        public Vec3i vector;
        public T data = (T)null;

        public Part(int x, int y, int z) {
            this.vector = new Vec3i(x, y, z);
        }

        public CopycatTransformableState.Part<T> setData(T data) {
            this.data = data;
            return this;
        }

        public T getData() {
            return this.data;
        }

        public CopycatTransformableState.Part<T> rotateY(Direction facing, Direction origin) {
            StructureTransform transform = new StructureTransform(BlockPos.ZERO, 0.0F, origin.toYRot() - facing.toYRot(), 0.0F);
            return this.transform(transform);
        }

        public CopycatTransformableState.Part<T> rotateY(Direction facing) {
            return this.rotateY(facing, Direction.SOUTH);
        }

        public CopycatTransformableState.Part<T> transform(StructureTransform transform) {
            Vec3 transformed = transform.applyWithoutOffset(new Vec3(this.vector.getX() / 16.0, this.vector.getY() / 16.0, this.vector.getZ() / 16.0));
            this.vector = new Vec3i((int)Math.round(transformed.x * 16.0), (int)Math.round(transformed.y * 16.0), (int)Math.round(transformed.z * 16.0));
            return this;
        }

        private static Vec3 unapplyWithoutOffset(StructureTransform transform, Vec3 globalVec) {
            Vec3 vec = globalVec;
            if (transform.rotationAxis != null) {
                vec = VecHelper.rotateCentered(globalVec, -transform.angle, transform.rotationAxis);
            }

            if (transform.mirror != null) {
                vec = VecHelper.mirrorCentered(vec, transform.mirror);
            }

            return vec;
        }

        public CopycatTransformableState.Part<T> untransform(StructureTransform transform) {
            Vec3 transformed = unapplyWithoutOffset(transform, new Vec3(this.vector.getX() / 16.0, this.vector.getY() / 16.0, this.vector.getZ() / 16.0));
            this.vector = new Vec3i((int)Math.round(transformed.x * 16.0), (int)Math.round(transformed.y * 16.0), (int)Math.round(transformed.z * 16.0));
            return this;
        }

        public Direction getFacing() {
            int dx = Math.abs(this.vector.getX() - 8);
            int dy = Math.abs(this.vector.getY() - 8);
            int dz = Math.abs(this.vector.getZ() - 8);
            if (dz > dx && dz > dy) {
                return this.vector.getZ() > 8 ? Direction.SOUTH : Direction.NORTH;
            } else if (dx > dy && dx > dz) {
                return this.vector.getX() > 8 ? Direction.EAST : Direction.WEST;
            } else {
                return this.vector.getY() > 8 ? Direction.UP : Direction.DOWN;
            }
        }

        public Direction getHorizontalFacing() {
            int dx = Math.abs(this.vector.getX() - 8);
            int dz = Math.abs(this.vector.getZ() - 8);
            if (dz > dx) {
                return this.vector.getZ() > 8 ? Direction.SOUTH : Direction.NORTH;
            } else {
                return this.vector.getX() > 8 ? Direction.EAST : Direction.WEST;
            }
        }

        public boolean isTop() {
            return this.vector.getY() > 8;
        }

        public boolean isRight(Direction facing) {
            return switch (facing) {
                case SOUTH -> this.vector.getX() < 8;
                case NORTH -> this.vector.getX() > 8;
                case EAST -> this.vector.getZ() > 8;
                case WEST -> this.vector.getZ() < 8;
                default -> false;
            };
        }
    }
}
