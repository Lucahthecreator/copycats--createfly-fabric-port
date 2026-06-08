/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.core.Direction
 *  net.minecraft.core.Direction$Axis
 */
package com.copycatsplus.copycats.foundation.copycat.model.assembly;

import com.copycatsplus.copycats.foundation.copycat.model.assembly.AssemblyTransform;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableVec3;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableVertex;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.Mutation;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.Direction;

public class MutableQuad
implements AssemblyTransform.Transformable<MutableQuad> {
    public List<MutableVertex> vertices;
    @Nullable
    public Direction cullFace;
    public boolean disableFinalAutoCull = false;
    List<Mutation> mutations = new ArrayList<Mutation>();
    private static final double EPSILON = 0.05;

    public MutableQuad(List<MutableVertex> vertices, @Nullable Direction cullFace) {
        this.vertices = vertices;
        this.cullFace = cullFace;
    }

    public Direction computeLightFace() {
        MutableVec3 normal = this.computeFaceNormal();
        return switch (MutableQuad.longestAxis(normal.x, normal.y, normal.z)) {
            default -> throw new MatchException(null, null);
            case Direction.Axis.X -> {
                if (normal.x() > 0.0) {
                    yield Direction.EAST;
                }
                yield Direction.WEST;
            }
            case Direction.Axis.Y -> {
                if (normal.y() > 0.0) {
                    yield Direction.UP;
                }
                yield Direction.DOWN;
            }
            case Direction.Axis.Z -> normal.z() > 0.0 ? Direction.SOUTH : Direction.NORTH;
        };
    }

    public static Direction.Axis longestAxis(double normalX, double normalY, double normalZ) {
        Direction.Axis result = Direction.Axis.Y;
        double longest = Math.abs(normalY);
        double a = Math.abs(normalX);
        if (a > longest + 0.05) {
            result = Direction.Axis.X;
            longest = a;
        }
        return Math.abs(normalZ) > longest + 0.05 ? Direction.Axis.Z : result;
    }

    public MutableVec3 computeFaceNormal() {
        double y2 = this.vertices.get((int)2).xyz.y;
        double y0 = this.vertices.get((int)0).xyz.y;
        double dy0 = y2 - y0;
        double z3 = this.vertices.get((int)3).xyz.z;
        double z1 = this.vertices.get((int)1).xyz.z;
        double dz1 = z3 - z1;
        double z2 = this.vertices.get((int)2).xyz.z;
        double z0 = this.vertices.get((int)0).xyz.z;
        double dz0 = z2 - z0;
        double y3 = this.vertices.get((int)3).xyz.y;
        double y1 = this.vertices.get((int)1).xyz.y;
        double dy1 = y3 - y1;
        double normX = dy0 * dz1 - dz0 * dy1;
        double x3 = this.vertices.get((int)3).xyz.x;
        double x1 = this.vertices.get((int)1).xyz.x;
        double dx1 = x3 - x1;
        double x2 = this.vertices.get((int)2).xyz.x;
        double x0 = this.vertices.get((int)0).xyz.x;
        double dx0 = x2 - x0;
        double normY = dz0 * dx1 - dx0 * dz1;
        double normZ = dx0 * dy1 - dy0 * dx1;
        double l = (float)Math.sqrt(normX * normX + normY * normY + normZ * normZ);
        if (l != 0.0) {
            normX /= l;
            normY /= l;
            normZ /= l;
        }
        return new MutableVec3(normX, normY, normZ);
    }

    public MutableQuad mutate() {
        for (Mutation mutation : this.mutations) {
            for (MutableVertex vertex : this.vertices) {
                mutation.mutate(vertex.xyz);
            }
            if (mutation.type() == Mutation.MutationType.MIRROR) {
                this.reverseWinding();
            }
            this.cullFace = mutation.mutate(this.cullFace);
        }
        return this;
    }

    public MutableQuad undoMutate() {
        for (int i = this.mutations.size() - 1; i >= 0; --i) {
            Mutation mutation = this.mutations.get(i);
            for (MutableVertex vertex : this.vertices) {
                mutation.undoMutate(vertex.xyz);
            }
            if (mutation.type() == Mutation.MutationType.MIRROR) {
                this.reverseWinding();
            }
            this.cullFace = mutation.undoMutate(this.cullFace);
        }
        return this;
    }

    private void reverseWinding() {
        MutableVertex temp = this.vertices.get(0);
        this.vertices.set(0, this.vertices.get(1));
        this.vertices.set(1, temp);
        temp = this.vertices.get(2);
        this.vertices.set(2, this.vertices.get(3));
        this.vertices.set(3, temp);
    }

    @Override
    public MutableQuad rotateX(int angle) {
        this.mutations.add(new Mutation(Mutation.MutationType.ROTATE_X, angle));
        return this;
    }

    @Override
    public MutableQuad rotateY(int angle) {
        this.mutations.add(new Mutation(Mutation.MutationType.ROTATE_Y, angle));
        return this;
    }

    @Override
    public MutableQuad rotateZ(int angle) {
        this.mutations.add(new Mutation(Mutation.MutationType.ROTATE_Z, angle));
        return this;
    }

    @Override
    public MutableQuad flipX(boolean flip) {
        if (!flip) {
            return this;
        }
        this.mutations.add(new Mutation(Mutation.MutationType.MIRROR, 0));
        return this;
    }

    @Override
    public MutableQuad flipY(boolean flip) {
        if (!flip) {
            return this;
        }
        this.mutations.add(new Mutation(Mutation.MutationType.MIRROR, 1));
        return this;
    }

    @Override
    public MutableQuad flipZ(boolean flip) {
        if (!flip) {
            return this;
        }
        this.mutations.add(new Mutation(Mutation.MutationType.MIRROR, 2));
        return this;
    }
}

