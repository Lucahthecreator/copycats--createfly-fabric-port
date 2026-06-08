/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.zurrtum.create.catnip.data.Pair
 *  net.minecraft.core.Direction
 *  net.minecraft.core.Direction$Axis
 *  net.minecraft.world.level.block.state.properties.BlockStateProperties
 *  net.minecraft.world.level.block.state.properties.Half
 *  net.minecraft.world.level.block.state.properties.Property
 *  net.minecraft.world.phys.shapes.BooleanOp
 *  net.minecraft.world.phys.shapes.Shapes
 *  net.minecraft.world.phys.shapes.VoxelShape
 */
package com.copycatsplus.copycats;

import com.copycatsplus.copycats.CCBlockStateProperties;
import com.copycatsplus.copycats.content.copycat.half_layer.CopycatHalfLayerBlock;
import com.copycatsplus.copycats.content.copycat.half_panel.CopycatHalfPanelBlock;
import com.copycatsplus.copycats.content.copycat.vertical_stairs.CopycatVerticalStairBlock;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.AssemblyTransform;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableAABB;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableVec3;
import com.copycatsplus.copycats.utility.TriFunction;
import com.copycatsplus.copycats.utility.shape.OutlinedVoxelShape;
import com.zurrtum.create.catnip.data.Pair;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CCShapes {
    public static final Map<Direction, MutableShape> BOARD = CCShapes.forDirections(CCShapes.shape(CCShapes.aabb(16.0, 16.0, 1.0).move(0.0, 0.0, 15.0)));
    public static final Map<Direction.Axis, MutableShape> SLAB_BOTTOM = CCShapes.forAxes(CCShapes.shape(CCShapes.aabb(16.0, 16.0, 8.0)));
    public static final Map<Direction.Axis, MutableShape> SLAB_TOP = CCShapes.forAxes(CCShapes.shape(CCShapes.aabb(16.0, 16.0, 8.0).move(0.0, 0.0, 8.0)));
    public static final Map<Direction.Axis, MutableShape> BEAM = CCShapes.forAxes(CCShapes.shape(CCShapes.aabb(8.0, 8.0, 16.0).move(4.0, 4.0, 0.0)));
    public static final Map<Direction, MutableShape> VERTICAL_STEP = CCShapes.forHorizontalDirections(CCShapes.shape(CCShapes.aabb(8.0, 16.0, 8.0).move(8.0, 0.0, 8.0)));
    public static final Map<Direction, Map<Integer, MutableShape>> LAYER = CCShapes.forDirections(CCShapes.forAll(BlockStateProperties.LAYERS, layer -> CCShapes.shape(CCShapes.aabb(16.0, 16.0, layer * 2))));
    public static final Map<Direction, Map<Direction, MutableShape>> HALF_PANEL = CCShapes.forAll(CopycatHalfPanelBlock.FACING, CopycatHalfPanelBlock.OFFSET, (direction, offset) -> {
        MutableShape shape = CCShapes.shape(CCShapes.aabb(16.0, 8.0, 3.0).move(0.0, 8.0, 13.0)).rotateZ((int)(-offset.toYRot()));
        CCShapes.directions(direction).apply(shape);
        if (direction.getAxis() == Direction.Axis.X) {
            shape.rotateX(-90).flipY(direction == Direction.WEST);
        }
        if (direction == Direction.NORTH) {
            shape.flipX(true);
        }
        if (direction == Direction.UP) {
            shape.flipZ(true);
        }
        return shape;
    });
    public static final Map<Direction, Map<Half, Map<Integer, MutableShape>>> SLICE = CCShapes.forHorizontalDirections(CCShapes.forHalves(CCShapes.forAll(BlockStateProperties.LAYERS, layer -> CCShapes.shape(CCShapes.aabb(16.0, layer * 2, layer * 2).move(0.0, 0.0, 16 - layer * 2)))));
    public static final Map<Direction, Map<Integer, MutableShape>> VERTICAL_SLICE = CCShapes.forHorizontalDirections(CCShapes.forAll(BlockStateProperties.LAYERS, layer -> CCShapes.shape(CCShapes.aabb(layer * 2, 16.0, layer * 2).move(16 - layer * 2, 0.0, 16 - layer * 2))));
    public static final Map<Direction, Map<Half, Map<Integer, MutableShape>>> CORNER_SLICE = CCShapes.forHorizontalDirections(CCShapes.forHalves(CCShapes.forAll(BlockStateProperties.LAYERS, layer -> CCShapes.shape(CCShapes.aabb(layer * 2, layer * 2, layer * 2).move(16 - layer * 2, 0.0, 16 - layer * 2)))));
    public static final Map<Direction.Axis, Map<Half, Map<Integer, MutableShape>>> HALF_LAYER_BOTTOM = CCShapes.forHorizontalAxes(CCShapes.forHalves(CCShapes.forAll(CopycatHalfLayerBlock.NEGATIVE_LAYERS, layer -> CCShapes.shape(CCShapes.aabb(16.0, layer * 2, 8.0)))));
    public static final Map<Direction.Axis, Map<Half, Map<Integer, MutableShape>>> HALF_LAYER_TOP = CCShapes.forHorizontalAxes(CCShapes.forHalves(CCShapes.forAll(CopycatHalfLayerBlock.POSITIVE_LAYERS, layer -> CCShapes.shape(CCShapes.aabb(16.0, layer * 2, 8.0).move(0.0, 0.0, 8.0)))));
    public static final Map<Direction.Axis, MutableShape> HORIZONTAL_PANE = CCShapes.forAxes(CCShapes.shape(CCShapes.aabb(16.0, 16.0, 2.0).move(0.0, 0.0, 7.0)));
    public static final Map<Direction, Map<Integer, MutableShape>> VERTICAL_HALF_LAYER_LEFT = CCShapes.forHorizontalDirections(CCShapes.forAll(CopycatHalfLayerBlock.POSITIVE_LAYERS, layer -> CCShapes.shape(CCShapes.aabb(8.0, 16.0, layer * 2).move(8.0, 0.0, 16 - layer * 2))));
    public static final Map<Direction, Map<Integer, MutableShape>> VERTICAL_HALF_LAYER_RIGHT = CCShapes.forHorizontalDirections(CCShapes.forAll(CopycatHalfLayerBlock.NEGATIVE_LAYERS, layer -> CCShapes.shape(CCShapes.aabb(8.0, 16.0, layer * 2).move(0.0, 0.0, 16 - layer * 2))));
    public static final Map<Direction, Map<Integer, MutableShape>> STACKED_HALF_LAYER_TOP = CCShapes.forHorizontalDirections(CCShapes.forAll(CopycatHalfLayerBlock.POSITIVE_LAYERS, layer -> CCShapes.shape(CCShapes.aabb(16.0, 8.0, layer * 2).move(0.0, 8.0, 16 - layer * 2))));
    public static final Map<Direction, Map<Integer, MutableShape>> STACKED_HALF_LAYER_BOTTOM = CCShapes.forHorizontalDirections(CCShapes.forAll(CopycatHalfLayerBlock.NEGATIVE_LAYERS, layer -> CCShapes.shape(CCShapes.aabb(16.0, 8.0, layer * 2).move(0.0, 0.0, 16 - layer * 2))));
    public static final Map<Direction, Map<CCBlockStateProperties.Side, Map<CCBlockStateProperties.VerticalStairShape, MutableShape>>> VERTICAL_STAIR = CCShapes.forHorizontalDirections(CCShapes.forAll(CopycatVerticalStairBlock.SIDE, CopycatVerticalStairBlock.SHAPE, (side, shape) -> (switch (shape) {
        default -> throw new MatchException(null, null);
        case CCBlockStateProperties.VerticalStairShape.STRAIGHT -> CCShapes.shape(CCShapes.aabb(16.0, 16.0, 8.0).move(0.0, 0.0, 8.0), CCShapes.aabb(8.0, 16.0, 8.0).move(8.0, 0.0, 0.0));
        case CCBlockStateProperties.VerticalStairShape.OUTER_TOP, CCBlockStateProperties.VerticalStairShape.OUTER_BOTTOM -> CCShapes.shape(CCShapes.aabb(16.0, 16.0, 8.0).move(0.0, 0.0, 8.0), CCShapes.aabb(8.0, 8.0, 8.0).move(8.0, 0.0, 0.0)).flipY(shape == CCBlockStateProperties.VerticalStairShape.OUTER_TOP);
        case CCBlockStateProperties.VerticalStairShape.INNER_TOP, CCBlockStateProperties.VerticalStairShape.INNER_BOTTOM -> CCShapes.shape(CCShapes.aabb(16.0, 16.0, 8.0).move(0.0, 0.0, 8.0), CCShapes.aabb(8.0, 16.0, 8.0).move(8.0, 0.0, 0.0), CCShapes.aabb(8.0, 8.0, 8.0)).flipY(shape == CCBlockStateProperties.VerticalStairShape.INNER_TOP);
    }).flipX(side == CCBlockStateProperties.Side.RIGHT)));
    private static final int SLOPE_SUBDIVISIONS = 16;
    private static final int MAX_SLOPE_COLLISION_SUBDIVISIONS = 8;
    public static final Map<Direction, Map<Half, MutableShape>> SLOPE = CCShapes.forHorizontalDirections(CCShapes.forHalves(CCShapes.shape((MutableAABB[])IntStream.range(0, 16).mapToObj(i -> CCShapes.aabb(16.0, ((double)i + 1.0) / 16.0 * 16.0, 1.0).move(0.0, 0.0, (double)i * 16.0 / 16.0)).toArray(MutableAABB[]::new)).outline(CCShapes.line(CCShapes.vec3(0.0, 0.0, 0.0), CCShapes.vec3(16.0, 0.0, 0.0)), CCShapes.line(CCShapes.vec3(0.0, 0.0, 16.0), CCShapes.vec3(16.0, 0.0, 16.0)), CCShapes.line(CCShapes.vec3(0.0, 0.0, 0.0), CCShapes.vec3(0.0, 0.0, 16.0)), CCShapes.line(CCShapes.vec3(16.0, 0.0, 0.0), CCShapes.vec3(16.0, 0.0, 16.0)), CCShapes.line(CCShapes.vec3(0.0, 0.0, 16.0), CCShapes.vec3(0.0, 16.0, 16.0)), CCShapes.line(CCShapes.vec3(16.0, 0.0, 16.0), CCShapes.vec3(16.0, 16.0, 16.0)), CCShapes.line(CCShapes.vec3(0.0, 16.0, 16.0), CCShapes.vec3(16.0, 16.0, 16.0)), CCShapes.line(CCShapes.vec3(0.0, 0.0, 0.0), CCShapes.vec3(0.0, 16.0, 16.0)), CCShapes.line(CCShapes.vec3(16.0, 0.0, 0.0), CCShapes.vec3(16.0, 16.0, 16.0)))));
    public static final Map<Direction, Map<Half, MutableShape>> SLOPE_COLLISION = CCShapes.forHorizontalDirections(CCShapes.forHalves(CCShapes.slopeCollision(0.0, 16.0)));
    public static final Map<Direction, MutableShape> VERTICAL_SLOPE = CCShapes.forHorizontalDirections(CCShapes.shape((MutableAABB[])IntStream.range(0, 16).mapToObj(i -> CCShapes.aabb(((double)i + 1.0) / 16.0 * 16.0, 16.0, 1.0).move(15.0 - (double)i * 16.0 / 16.0, 0.0, (double)i * 16.0 / 16.0)).toArray(MutableAABB[]::new)).outline(CCShapes.line(CCShapes.vec3(0.0, 0.0, 16.0), CCShapes.vec3(16.0, 0.0, 16.0)), CCShapes.line(CCShapes.vec3(0.0, 0.0, 16.0), CCShapes.vec3(0.0, 16.0, 16.0)), CCShapes.line(CCShapes.vec3(16.0, 0.0, 16.0), CCShapes.vec3(16.0, 16.0, 16.0)), CCShapes.line(CCShapes.vec3(0.0, 16.0, 16.0), CCShapes.vec3(16.0, 16.0, 16.0)), CCShapes.line(CCShapes.vec3(16.0, 0.0, 0.0), CCShapes.vec3(16.0, 0.0, 16.0)), CCShapes.line(CCShapes.vec3(16.0, 16.0, 0.0), CCShapes.vec3(16.0, 16.0, 16.0)), CCShapes.line(CCShapes.vec3(16.0, 0.0, 0.0), CCShapes.vec3(16.0, 16.0, 0.0)), CCShapes.line(CCShapes.vec3(16.0, 0.0, 0.0), CCShapes.vec3(0.0, 0.0, 16.0)), CCShapes.line(CCShapes.vec3(16.0, 16.0, 0.0), CCShapes.vec3(0.0, 16.0, 16.0))));
    public static final Map<Direction, Map<Half, Map<Integer, MutableShape>>> SLOPE_LAYER = CCShapes.forHorizontalDirections(CCShapes.forHalves(CCShapes.forAll(BlockStateProperties.LAYERS, layer -> layer <= 4 ? CCShapes.shape((MutableAABB[])IntStream.range(0, 16).mapToObj(i -> CCShapes.aabb(16.0, ((double)i + 1.0) / 16.0 * 16.0 * (double)layer.intValue() / 4.0, 1.0).move(0.0, 0.0, (double)i * 16.0 / 16.0)).toArray(MutableAABB[]::new)).outline(CCShapes.line(CCShapes.vec3(0.0, 0.0, 0.0), CCShapes.vec3(16.0, 0.0, 0.0)), CCShapes.line(CCShapes.vec3(0.0, 0.0, 16.0), CCShapes.vec3(16.0, 0.0, 16.0)), CCShapes.line(CCShapes.vec3(0.0, 0.0, 0.0), CCShapes.vec3(0.0, 0.0, 16.0)), CCShapes.line(CCShapes.vec3(16.0, 0.0, 0.0), CCShapes.vec3(16.0, 0.0, 16.0)), CCShapes.line(CCShapes.vec3(0.0, 0.0, 16.0), CCShapes.vec3(0.0, 4 * layer, 16.0)), CCShapes.line(CCShapes.vec3(16.0, 0.0, 16.0), CCShapes.vec3(16.0, 4 * layer, 16.0)), CCShapes.line(CCShapes.vec3(0.0, 4 * layer, 16.0), CCShapes.vec3(16.0, 4 * layer, 16.0)), CCShapes.line(CCShapes.vec3(0.0, 0.0, 0.0), CCShapes.vec3(0.0, 4 * layer, 16.0)), CCShapes.line(CCShapes.vec3(16.0, 0.0, 0.0), CCShapes.vec3(16.0, 4 * layer, 16.0))) : CCShapes.shape((MutableAABB[])IntStream.range(0, 16).mapToObj(i -> CCShapes.aabb(16.0, (double)(16 * (layer - 4)) / 4.0 + ((double)i + 1.0) / 16.0 * 16.0 * (1.0 - (double)(layer - 4) / 4.0), 1.0).move(0.0, 0.0, (double)i * 16.0 / 16.0)).toArray(MutableAABB[]::new)).outline(CCShapes.line(CCShapes.vec3(0.0, 0.0, 0.0), CCShapes.vec3(16.0, 0.0, 0.0)), CCShapes.line(CCShapes.vec3(0.0, 0.0, 16.0), CCShapes.vec3(16.0, 0.0, 16.0)), CCShapes.line(CCShapes.vec3(0.0, 0.0, 0.0), CCShapes.vec3(0.0, 0.0, 16.0)), CCShapes.line(CCShapes.vec3(16.0, 0.0, 0.0), CCShapes.vec3(16.0, 0.0, 16.0)), CCShapes.line(CCShapes.vec3(0.0, 0.0, 0.0), CCShapes.vec3(0.0, (layer - 4) * 4, 0.0)), CCShapes.line(CCShapes.vec3(16.0, 0.0, 0.0), CCShapes.vec3(16.0, (layer - 4) * 4, 0.0)), CCShapes.line(CCShapes.vec3(0.0, 0.0, 16.0), CCShapes.vec3(0.0, 16.0, 16.0)), CCShapes.line(CCShapes.vec3(16.0, 0.0, 16.0), CCShapes.vec3(16.0, 16.0, 16.0)), CCShapes.line(CCShapes.vec3(0.0, 16.0, 16.0), CCShapes.vec3(16.0, 16.0, 16.0)), CCShapes.line(CCShapes.vec3(0.0, (layer - 4) * 4, 0.0), CCShapes.vec3(16.0, (layer - 4) * 4, 0.0)), CCShapes.line(CCShapes.vec3(0.0, (layer - 4) * 4, 0.0), CCShapes.vec3(0.0, 16.0, 16.0)), CCShapes.line(CCShapes.vec3(16.0, (layer - 4) * 4, 0.0), CCShapes.vec3(16.0, 16.0, 16.0))))));
    public static final Map<Direction, Map<Half, Map<Integer, MutableShape>>> SLOPE_LAYER_COLLISION = CCShapes.forHorizontalDirections(CCShapes.forHalves(CCShapes.forAll(BlockStateProperties.LAYERS, layer -> CCShapes.slopeCollision(layer <= 4 ? 0.0 : (double)((layer - 4) * 4), layer <= 4 ? (double)(layer * 4) : 16.0))));

    private static MutableAABB aabb(double sizeX, double sizeY, double sizeZ) {
        return new MutableAABB(sizeX / 16.0, sizeY / 16.0, sizeZ / 16.0);
    }

    private static MutableVec3 vec3(double x, double y, double z) {
        return new MutableVec3(x / 16.0, y / 16.0, z / 16.0);
    }

    private static MutableShape slopeCollision(double minHeight, double maxHeight) {
        int subdivisions = Math.min(8, Math.max(1, (int)Math.round(Math.abs(maxHeight - minHeight) / 2.0)));
        return CCShapes.shape((MutableAABB[])IntStream.range(0, subdivisions).mapToObj(i -> CCShapes.aabb(16.0, minHeight + ((double)i + 1.0) / (double)subdivisions * (maxHeight - minHeight), 16.0 / (double)subdivisions).move(0.0, 0.0, (double)i * 16.0 / (double)subdivisions)).toArray(MutableAABB[]::new));
    }

    public static AssemblyTransform halves(Half half) {
        return t -> t.flipY(half == Half.TOP);
    }

    public static AssemblyTransform axes(Direction.Axis axis) {
        if (axis == Direction.Axis.Z) {
            return AssemblyTransform.IDENTITY;
        }
        if (axis == Direction.Axis.Y) {
            return t -> t.rotateX(90);
        }
        return t -> t.rotateY(-90);
    }

    public static AssemblyTransform directions(Direction direction) {
        if (direction.getAxis().isHorizontal()) {
            return t -> t.rotateY((int)direction.toYRot());
        }
        if (direction == Direction.UP) {
            return t -> t.rotateX(90);
        }
        if (direction == Direction.DOWN) {
            return t -> t.rotateX(-90);
        }
        return AssemblyTransform.IDENTITY;
    }

    private static <T, U> Map<T, U> applyTransform(Map<T, U> source, AssemblyTransform transform) {
        HashMap<T, U> copy = new HashMap<T, U>();
        for (Map.Entry<T, U> entry : source.entrySet()) {
            copy.put(entry.getKey(), CCShapes.applyTransform(entry.getValue(), transform));
        }
        return copy;
    }

    private static <U> U applyTransform(U source, AssemblyTransform transform) {
        if (source instanceof MutableShape) {
            MutableShape shape = (MutableShape)source;
            MutableShape copy = shape.copy();
            transform.apply(copy);
            return (U)copy;
        }
        if (source instanceof Map) {
            Map map = (Map)source;
            return (U)CCShapes.applyTransform(map, transform);
        }
        throw new RuntimeException("Unsupported type: " + source.getClass().getName());
    }

    public static <U> Map<Direction.Axis, U> forAxes(U factory) {
        return CCShapes.forAll(BlockStateProperties.AXIS, axis -> CCShapes.applyTransform(factory, CCShapes.axes(axis)));
    }

    public static <U> Map<Direction.Axis, U> forHorizontalAxes(U factory) {
        return CCShapes.forAll(BlockStateProperties.HORIZONTAL_AXIS, axis -> CCShapes.applyTransform(factory, CCShapes.axes(axis)));
    }

    public static <U> Map<Direction, U> forDirections(U factory) {
        return CCShapes.forAll(BlockStateProperties.FACING, direction -> CCShapes.applyTransform(factory, CCShapes.directions(direction)));
    }

    public static <U> Map<Direction, U> forHorizontalDirections(U factory) {
        return CCShapes.forAll(BlockStateProperties.HORIZONTAL_FACING, direction -> CCShapes.applyTransform(factory, CCShapes.directions(direction)));
    }

    public static <U> Map<Half, U> forHalves(U factory) {
        return CCShapes.forAll(BlockStateProperties.HALF, halves -> CCShapes.applyTransform(factory, CCShapes.halves(halves)));
    }

    public static <T extends Comparable<T>, U> Map<T, U> forAll(Property<T> property, Function<T, U> factory) {
        return property.getPossibleValues().stream().collect(Collectors.toMap(Function.identity(), factory));
    }

    public static <T1 extends Comparable<T1>, T2 extends Comparable<T2>, U> Map<T1, Map<T2, U>> forAll(Property<T1> property1, Property<T2> property2, BiFunction<T1, T2, U> factory) {
        return property1.getPossibleValues().stream().collect(Collectors.toMap(Function.identity(), t1 -> CCShapes.forAll(property2, t2 -> factory.apply(t1, t2))));
    }

    public static <T1 extends Comparable<T1>, T2 extends Comparable<T2>, T3 extends Comparable<T3>, U> Map<T1, Map<T2, Map<T3, U>>> forAll(Property<T1> property1, Property<T2> property2, Property<T3> property3, TriFunction<T1, T2, T3, U> factory) {
        return property1.getPossibleValues().stream().collect(Collectors.toMap(Function.identity(), t1 -> CCShapes.forAll(property2, property3, (t2, t3) -> factory.apply(t1, t2, t3))));
    }

    public static MutableShape shape(MutableAABB ... boxes) {
        LinkedList<MutableAABB> newBoxes = new LinkedList<MutableAABB>();
        Collections.addAll(newBoxes, boxes);
        return new MutableShape(newBoxes, new ArrayList<Pair<MutableVec3, MutableVec3>>());
    }

    public static Pair<MutableVec3, MutableVec3> line(MutableVec3 start, MutableVec3 end) {
        return Pair.of((Object)start, (Object)end);
    }

    public static class MutableShape
    implements AssemblyTransform.Transformable<MutableShape> {
        public List<MutableAABB> boxes;
        public List<Pair<MutableVec3, MutableVec3>> outlines;
        private VoxelShape output = null;

        public MutableShape(List<MutableAABB> boxes, List<Pair<MutableVec3, MutableVec3>> outlines) {
            this.boxes = boxes;
            this.outlines = outlines;
        }

        public MutableShape() {
            this.boxes = new LinkedList<MutableAABB>();
            this.outlines = new LinkedList<Pair<MutableVec3, MutableVec3>>();
        }

        @SafeVarargs
        public final MutableShape outline(Pair<MutableVec3, MutableVec3> ... lines) {
            Collections.addAll(this.outlines, lines);
            return this;
        }

        public MutableShape copy() {
            return new MutableShape(this.boxes.stream().map(MutableAABB::copy).collect(Collectors.toList()), this.outlines.stream().map(p -> Pair.of((Object)((MutableVec3)p.getFirst()).copy(), (Object)((MutableVec3)p.getSecond()).copy())).collect(Collectors.toList()));
        }

        public VoxelShape toShape() {
            if (this.output == null) {
                VoxelShape shape = Shapes.empty();
                for (MutableAABB box : this.boxes) {
                    shape = Shapes.joinUnoptimized((VoxelShape)shape, (VoxelShape)Shapes.box((double)Math.min(box.minX, box.maxX), (double)Math.min(box.minY, box.maxY), (double)Math.min(box.minZ, box.maxZ), (double)Math.max(box.maxX, box.minX), (double)Math.max(box.maxY, box.minY), (double)Math.max(box.maxZ, box.minZ)), (BooleanOp)BooleanOp.OR);
                }
                shape = shape.optimize();
                if (!this.outlines.isEmpty()) {
                    shape = new OutlinedVoxelShape(shape, this.outlines.stream().map(p -> Pair.of((Object)((MutableVec3)p.getFirst()).toVec3(), (Object)((MutableVec3)p.getSecond()).toVec3())).collect(Collectors.toList()));
                }
                this.output = shape;
            }
            return this.output;
        }

        @Override
        public MutableShape rotateX(int angle) {
            if (this.output != null) {
                throw new IllegalStateException("Cannot modify a shape after it has been built");
            }
            for (MutableAABB mutableAABB : this.boxes) {
                mutableAABB.rotateX(angle);
            }
            for (Pair pair : this.outlines) {
                ((MutableVec3)pair.getFirst()).rotateX(angle);
                ((MutableVec3)pair.getSecond()).rotateX(angle);
            }
            return this;
        }

        @Override
        public MutableShape rotateY(int angle) {
            if (this.output != null) {
                throw new IllegalStateException("Cannot modify a shape after it has been built");
            }
            for (MutableAABB mutableAABB : this.boxes) {
                mutableAABB.rotateY(angle);
            }
            for (Pair pair : this.outlines) {
                ((MutableVec3)pair.getFirst()).rotateY(angle);
                ((MutableVec3)pair.getSecond()).rotateY(angle);
            }
            return this;
        }

        @Override
        public MutableShape rotateZ(int angle) {
            if (this.output != null) {
                throw new IllegalStateException("Cannot modify a shape after it has been built");
            }
            for (MutableAABB mutableAABB : this.boxes) {
                mutableAABB.rotateZ(angle);
            }
            for (Pair pair : this.outlines) {
                ((MutableVec3)pair.getFirst()).rotateZ(angle);
                ((MutableVec3)pair.getSecond()).rotateZ(angle);
            }
            return this;
        }

        @Override
        public MutableShape flipX(boolean flip) {
            if (this.output != null) {
                throw new IllegalStateException("Cannot modify a shape after it has been built");
            }
            for (MutableAABB mutableAABB : this.boxes) {
                mutableAABB.flipX(flip);
            }
            for (Pair pair : this.outlines) {
                ((MutableVec3)pair.getFirst()).flipX(flip);
                ((MutableVec3)pair.getSecond()).flipX(flip);
            }
            return this;
        }

        @Override
        public MutableShape flipY(boolean flip) {
            if (this.output != null) {
                throw new IllegalStateException("Cannot modify a shape after it has been built");
            }
            for (MutableAABB mutableAABB : this.boxes) {
                mutableAABB.flipY(flip);
            }
            for (Pair pair : this.outlines) {
                ((MutableVec3)pair.getFirst()).flipY(flip);
                ((MutableVec3)pair.getSecond()).flipY(flip);
            }
            return this;
        }

        @Override
        public MutableShape flipZ(boolean flip) {
            if (this.output != null) {
                throw new IllegalStateException("Cannot modify a shape after it has been built");
            }
            for (MutableAABB mutableAABB : this.boxes) {
                mutableAABB.flipZ(flip);
            }
            for (Pair pair : this.outlines) {
                ((MutableVec3)pair.getFirst()).flipZ(flip);
                ((MutableVec3)pair.getSecond()).flipZ(flip);
            }
            return this;
        }
    }
}

