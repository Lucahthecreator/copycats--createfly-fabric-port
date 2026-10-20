package com.copycatsplus.copycats.utility.shape;

import com.zurrtum.create.catnip.data.Pair;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import java.util.List;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes.DoubleLineConsumer;

public class OutlinedVoxelShape extends ExtensibleVoxelShape {
    private final VoxelShape collisionShape;
    private final List<Pair<Vec3, Vec3>> outlineShapeEdges;

    public OutlinedVoxelShape(VoxelShape collisionShape, List<Pair<Vec3, Vec3>> outlineShapeEdges) {
        super(collisionShape);
        this.collisionShape = collisionShape;
        this.outlineShapeEdges = outlineShapeEdges;
    }

    @Override
    public DoubleList getCoords(Axis axis) {
        return this.collisionShape.getCoords(axis);
    }

    @Override
    public void forAllEdges(DoubleLineConsumer boxConsumer) {
        for (Pair<Vec3, Vec3> edge : this.outlineShapeEdges) {
            boxConsumer.consume(edge.getFirst().x, edge.getFirst().y, edge.getFirst().z, edge.getSecond().x, edge.getSecond().y, edge.getSecond().z);
        }
    }
}
