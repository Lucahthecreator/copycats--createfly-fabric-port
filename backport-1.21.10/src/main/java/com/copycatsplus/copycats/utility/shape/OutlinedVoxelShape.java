/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.zurrtum.create.catnip.data.Pair
 *  it.unimi.dsi.fastutil.doubles.DoubleList
 *  net.minecraft.core.Direction$Axis
 *  net.minecraft.world.phys.Vec3
 *  net.minecraft.world.phys.shapes.Shapes$DoubleLineConsumer
 *  net.minecraft.world.phys.shapes.VoxelShape
 */
package com.copycatsplus.copycats.utility.shape;

import com.copycatsplus.copycats.utility.shape.ExtensibleVoxelShape;
import com.zurrtum.create.catnip.data.Pair;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import java.util.List;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class OutlinedVoxelShape
extends ExtensibleVoxelShape {
    private final VoxelShape collisionShape;
    private final List<Pair<Vec3, Vec3>> outlineShapeEdges;

    public OutlinedVoxelShape(VoxelShape collisionShape, List<Pair<Vec3, Vec3>> outlineShapeEdges) {
        super(collisionShape);
        this.collisionShape = collisionShape;
        this.outlineShapeEdges = outlineShapeEdges;
    }

    @Override
    public DoubleList getCoords(Direction.Axis axis) {
        return this.collisionShape.getCoords(axis);
    }

    public void forAllEdges(Shapes.DoubleLineConsumer boxConsumer) {
        for (Pair<Vec3, Vec3> edge : this.outlineShapeEdges) {
            boxConsumer.consume(((Vec3)edge.getFirst()).x, ((Vec3)edge.getFirst()).y, ((Vec3)edge.getFirst()).z, ((Vec3)edge.getSecond()).x, ((Vec3)edge.getSecond()).y, ((Vec3)edge.getSecond()).z);
        }
    }
}

