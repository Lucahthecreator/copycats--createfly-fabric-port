package com.copycatsplus.copycats;

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
import java.util.Map.Entry;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CCShapes {
    public static final Map<Direction, CCShapes.MutableShape> BOARD = forDirections(shape(aabb(16.0, 16.0, 1.0).move(0.0, 0.0, 15.0)));
    public static final Map<Axis, CCShapes.MutableShape> SLAB_BOTTOM = forAxes(shape(aabb(16.0, 16.0, 8.0)));
    public static final Map<Axis, CCShapes.MutableShape> SLAB_TOP = forAxes(shape(aabb(16.0, 16.0, 8.0).move(0.0, 0.0, 8.0)));
    public static final Map<Axis, CCShapes.MutableShape> BEAM = forAxes(shape(aabb(8.0, 8.0, 16.0).move(4.0, 4.0, 0.0)));
    public static final Map<Direction, CCShapes.MutableShape> VERTICAL_STEP = forHorizontalDirections(shape(aabb(8.0, 16.0, 8.0).move(8.0, 0.0, 8.0)));
    public static final Map<Direction, Map<Integer, CCShapes.MutableShape>> LAYER = forDirections(
        forAll(BlockStateProperties.LAYERS, layer -> shape(aabb(16.0, 16.0, layer * 2)))
    );
    public static final Map<Direction, Map<Direction, CCShapes.MutableShape>> HALF_PANEL = forAll(
        CopycatHalfPanelBlock.FACING, CopycatHalfPanelBlock.OFFSET, (direction, offset) -> {
            CCShapes.MutableShape shape = shape(aabb(16.0, 8.0, 3.0).move(0.0, 8.0, 13.0)).rotateZ((int)(-offset.toYRot()));
            directions(direction).apply(shape);
            if (direction.getAxis() == Axis.X) {
                shape.rotateX(-90).flipY(direction == Direction.WEST);
            }

            if (direction == Direction.NORTH) {
                shape.flipX(true);
            }

            if (direction == Direction.UP) {
                shape.flipZ(true);
            }

            return shape;
        }
    );
    public static final Map<Direction, Map<Half, Map<Integer, CCShapes.MutableShape>>> SLICE = forHorizontalDirections(
        forHalves(forAll(BlockStateProperties.LAYERS, layer -> shape(aabb(16.0, layer * 2, layer * 2).move(0.0, 0.0, 16 - layer * 2))))
    );
    public static final Map<Direction, Map<Integer, CCShapes.MutableShape>> VERTICAL_SLICE = forHorizontalDirections(
        forAll(BlockStateProperties.LAYERS, layer -> shape(aabb(layer * 2, 16.0, layer * 2).move(16 - layer * 2, 0.0, 16 - layer * 2)))
    );
    public static final Map<Direction, Map<Half, Map<Integer, CCShapes.MutableShape>>> CORNER_SLICE = forHorizontalDirections(
        forHalves(forAll(BlockStateProperties.LAYERS, layer -> shape(aabb(layer * 2, layer * 2, layer * 2).move(16 - layer * 2, 0.0, 16 - layer * 2))))
    );
    public static final Map<Axis, Map<Half, Map<Integer, CCShapes.MutableShape>>> HALF_LAYER_BOTTOM = forHorizontalAxes(
        forHalves(forAll(CopycatHalfLayerBlock.NEGATIVE_LAYERS, layer -> shape(aabb(16.0, layer * 2, 8.0))))
    );
    public static final Map<Axis, Map<Half, Map<Integer, CCShapes.MutableShape>>> HALF_LAYER_TOP = forHorizontalAxes(
        forHalves(forAll(CopycatHalfLayerBlock.POSITIVE_LAYERS, layer -> shape(aabb(16.0, layer * 2, 8.0).move(0.0, 0.0, 8.0))))
    );
    public static final Map<Axis, CCShapes.MutableShape> HORIZONTAL_PANE = forAxes(shape(aabb(16.0, 16.0, 2.0).move(0.0, 0.0, 7.0)));
    public static final Map<Direction, Map<Integer, CCShapes.MutableShape>> VERTICAL_HALF_LAYER_LEFT = forHorizontalDirections(
        forAll(CopycatHalfLayerBlock.POSITIVE_LAYERS, layer -> shape(aabb(8.0, 16.0, layer * 2).move(8.0, 0.0, 16 - layer * 2)))
    );
    public static final Map<Direction, Map<Integer, CCShapes.MutableShape>> VERTICAL_HALF_LAYER_RIGHT = forHorizontalDirections(
        forAll(CopycatHalfLayerBlock.NEGATIVE_LAYERS, layer -> shape(aabb(8.0, 16.0, layer * 2).move(0.0, 0.0, 16 - layer * 2)))
    );
    public static final Map<Direction, Map<Integer, CCShapes.MutableShape>> STACKED_HALF_LAYER_TOP = forHorizontalDirections(
        forAll(CopycatHalfLayerBlock.POSITIVE_LAYERS, layer -> shape(aabb(16.0, 8.0, layer * 2).move(0.0, 8.0, 16 - layer * 2)))
    );
    public static final Map<Direction, Map<Integer, CCShapes.MutableShape>> STACKED_HALF_LAYER_BOTTOM = forHorizontalDirections(
        forAll(CopycatHalfLayerBlock.NEGATIVE_LAYERS, layer -> shape(aabb(16.0, 8.0, layer * 2).move(0.0, 0.0, 16 - layer * 2)))
    );
    public static final Map<Direction, Map<CCBlockStateProperties.Side, Map<CCBlockStateProperties.VerticalStairShape, CCShapes.MutableShape>>> VERTICAL_STAIR = forHorizontalDirections(
        forAll(
            CopycatVerticalStairBlock.SIDE,
            CopycatVerticalStairBlock.SHAPE,
            (side, shape) -> {
                return (switch (shape) {
                        case STRAIGHT -> shape(aabb(16.0, 16.0, 8.0).move(0.0, 0.0, 8.0), aabb(8.0, 16.0, 8.0).move(8.0, 0.0, 0.0));
                        case OUTER_TOP, OUTER_BOTTOM -> shape(aabb(16.0, 16.0, 8.0).move(0.0, 0.0, 8.0), aabb(8.0, 8.0, 8.0).move(8.0, 0.0, 0.0))
                            .flipY(shape == CCBlockStateProperties.VerticalStairShape.OUTER_TOP);
                        case INNER_TOP, INNER_BOTTOM -> shape(
                                aabb(16.0, 16.0, 8.0).move(0.0, 0.0, 8.0), aabb(8.0, 16.0, 8.0).move(8.0, 0.0, 0.0), aabb(8.0, 8.0, 8.0)
                            )
                            .flipY(shape == CCBlockStateProperties.VerticalStairShape.INNER_TOP);
                    })
                    .flipX(side == CCBlockStateProperties.Side.RIGHT);
            }
        )
    );
    private static final int SLOPE_SUBDIVISIONS = 16;
    private static final int MAX_SLOPE_COLLISION_SUBDIVISIONS = 8;
    public static final Map<Direction, Map<Half, CCShapes.MutableShape>> SLOPE = forHorizontalDirections(
        forHalves(
            shape(IntStream.range(0, 16).mapToObj(i -> aabb(16.0, (i + 1.0) / 16.0 * 16.0, 1.0).move(0.0, 0.0, i * 16.0 / 16.0)).toArray(MutableAABB[]::new))
                .outline(
                    line(vec3(0.0, 0.0, 0.0), vec3(16.0, 0.0, 0.0)),
                    line(vec3(0.0, 0.0, 16.0), vec3(16.0, 0.0, 16.0)),
                    line(vec3(0.0, 0.0, 0.0), vec3(0.0, 0.0, 16.0)),
                    line(vec3(16.0, 0.0, 0.0), vec3(16.0, 0.0, 16.0)),
                    line(vec3(0.0, 0.0, 16.0), vec3(0.0, 16.0, 16.0)),
                    line(vec3(16.0, 0.0, 16.0), vec3(16.0, 16.0, 16.0)),
                    line(vec3(0.0, 16.0, 16.0), vec3(16.0, 16.0, 16.0)),
                    line(vec3(0.0, 0.0, 0.0), vec3(0.0, 16.0, 16.0)),
                    line(vec3(16.0, 0.0, 0.0), vec3(16.0, 16.0, 16.0))
                )
        )
    );
    public static final Map<Direction, Map<Half, CCShapes.MutableShape>> SLOPE_COLLISION = forHorizontalDirections(forHalves(slopeCollision(0.0, 16.0)));
    public static final Map<Direction, CCShapes.MutableShape> VERTICAL_SLOPE = forHorizontalDirections(
        shape(
                IntStream.range(0, 16)
                    .mapToObj(i -> aabb((i + 1.0) / 16.0 * 16.0, 16.0, 1.0).move(15.0 - i * 16.0 / 16.0, 0.0, i * 16.0 / 16.0))
                    .toArray(MutableAABB[]::new)
            )
            .outline(
                line(vec3(0.0, 0.0, 16.0), vec3(16.0, 0.0, 16.0)),
                line(vec3(0.0, 0.0, 16.0), vec3(0.0, 16.0, 16.0)),
                line(vec3(16.0, 0.0, 16.0), vec3(16.0, 16.0, 16.0)),
                line(vec3(0.0, 16.0, 16.0), vec3(16.0, 16.0, 16.0)),
                line(vec3(16.0, 0.0, 0.0), vec3(16.0, 0.0, 16.0)),
                line(vec3(16.0, 16.0, 0.0), vec3(16.0, 16.0, 16.0)),
                line(vec3(16.0, 0.0, 0.0), vec3(16.0, 16.0, 0.0)),
                line(vec3(16.0, 0.0, 0.0), vec3(0.0, 0.0, 16.0)),
                line(vec3(16.0, 16.0, 0.0), vec3(0.0, 16.0, 16.0))
            )
    );
    public static final Map<Direction, Map<Half, Map<Integer, CCShapes.MutableShape>>> SLOPE_LAYER = forHorizontalDirections(
        forHalves(
            forAll(
                BlockStateProperties.LAYERS,
                layer -> layer <= 4
                    ? shape(
                            IntStream.range(0, 16)
                                .mapToObj(i -> aabb(16.0, (i + 1.0) / 16.0 * 16.0 * layer.intValue() / 4.0, 1.0).move(0.0, 0.0, i * 16.0 / 16.0))
                                .toArray(MutableAABB[]::new)
                        )
                        .outline(
                            line(vec3(0.0, 0.0, 0.0), vec3(16.0, 0.0, 0.0)),
                            line(vec3(0.0, 0.0, 16.0), vec3(16.0, 0.0, 16.0)),
                            line(vec3(0.0, 0.0, 0.0), vec3(0.0, 0.0, 16.0)),
                            line(vec3(16.0, 0.0, 0.0), vec3(16.0, 0.0, 16.0)),
                            line(vec3(0.0, 0.0, 16.0), vec3(0.0, 4 * layer, 16.0)),
                            line(vec3(16.0, 0.0, 16.0), vec3(16.0, 4 * layer, 16.0)),
                            line(vec3(0.0, 4 * layer, 16.0), vec3(16.0, 4 * layer, 16.0)),
                            line(vec3(0.0, 0.0, 0.0), vec3(0.0, 4 * layer, 16.0)),
                            line(vec3(16.0, 0.0, 0.0), vec3(16.0, 4 * layer, 16.0))
                        )
                    : shape(
                            IntStream.range(0, 16)
                                .mapToObj(
                                    i -> aabb(16.0, 16 * (layer - 4) / 4.0 + (i + 1.0) / 16.0 * 16.0 * (1.0 - (layer - 4) / 4.0), 1.0)
                                        .move(0.0, 0.0, i * 16.0 / 16.0)
                                )
                                .toArray(MutableAABB[]::new)
                        )
                        .outline(
                            line(vec3(0.0, 0.0, 0.0), vec3(16.0, 0.0, 0.0)),
                            line(vec3(0.0, 0.0, 16.0), vec3(16.0, 0.0, 16.0)),
                            line(vec3(0.0, 0.0, 0.0), vec3(0.0, 0.0, 16.0)),
                            line(vec3(16.0, 0.0, 0.0), vec3(16.0, 0.0, 16.0)),
                            line(vec3(0.0, 0.0, 0.0), vec3(0.0, (layer - 4) * 4, 0.0)),
                            line(vec3(16.0, 0.0, 0.0), vec3(16.0, (layer - 4) * 4, 0.0)),
                            line(vec3(0.0, 0.0, 16.0), vec3(0.0, 16.0, 16.0)),
                            line(vec3(16.0, 0.0, 16.0), vec3(16.0, 16.0, 16.0)),
                            line(vec3(0.0, 16.0, 16.0), vec3(16.0, 16.0, 16.0)),
                            line(vec3(0.0, (layer - 4) * 4, 0.0), vec3(16.0, (layer - 4) * 4, 0.0)),
                            line(vec3(0.0, (layer - 4) * 4, 0.0), vec3(0.0, 16.0, 16.0)),
                            line(vec3(16.0, (layer - 4) * 4, 0.0), vec3(16.0, 16.0, 16.0))
                        )
            )
        )
    );
    public static final Map<Direction, Map<Half, Map<Integer, CCShapes.MutableShape>>> SLOPE_LAYER_COLLISION = forHorizontalDirections(
        forHalves(forAll(BlockStateProperties.LAYERS, layer -> slopeCollision(layer <= 4 ? 0.0 : (layer - 4) * 4, layer <= 4 ? layer * 4 : 16.0)))
    );

    private static MutableAABB aabb(double sizeX, double sizeY, double sizeZ) {
        return new MutableAABB(sizeX / 16.0, sizeY / 16.0, sizeZ / 16.0);
    }

    private static MutableVec3 vec3(double x, double y, double z) {
        return new MutableVec3(x / 16.0, y / 16.0, z / 16.0);
    }

    private static CCShapes.MutableShape slopeCollision(double minHeight, double maxHeight) {
        int subdivisions = Math.min(8, Math.max(1, (int)Math.round(Math.abs(maxHeight - minHeight) / 2.0)));
        return shape(
            IntStream.range(0, subdivisions)
                .mapToObj(
                    i -> aabb(16.0, minHeight + (i + 1.0) / subdivisions * (maxHeight - minHeight), 16.0 / subdivisions)
                        .move(0.0, 0.0, i * 16.0 / subdivisions)
                )
                .toArray(MutableAABB[]::new)
        );
    }

    public static AssemblyTransform halves(Half half) {
        return t -> t.flipY(half == Half.TOP);
    }

    public static AssemblyTransform axes(Axis axis) {
        if (axis == Axis.Z) {
            return AssemblyTransform.IDENTITY;
        } else {
            return axis == Axis.Y ? t -> t.rotateX(90) : t -> t.rotateY(-90);
        }
    }

    public static AssemblyTransform directions(Direction direction) {
        if (direction.getAxis().isHorizontal()) {
            return t -> t.rotateY((int)direction.toYRot());
        } else if (direction == Direction.UP) {
            return t -> t.rotateX(90);
        } else {
            return direction == Direction.DOWN ? t -> t.rotateX(-90) : AssemblyTransform.IDENTITY;
        }
    }

    private static <T, U> Map<T, U> applyTransform(Map<T, U> source, AssemblyTransform transform) {
        Map<T, U> copy = new HashMap<>();

        for (Entry<T, U> entry : source.entrySet()) {
            copy.put(entry.getKey(), applyTransform(entry.getValue(), transform));
        }

        return copy;
    }

    private static <U> U applyTransform(U source, AssemblyTransform transform) {
        if (source instanceof CCShapes.MutableShape shape) {
            CCShapes.MutableShape copy = shape.copy();
            transform.apply(copy);
            return (U)copy;
        } else if (source instanceof Map<?, ?> map) {
            return (U)applyTransform(map, transform);
        } else {
            throw new RuntimeException("Unsupported type: " + source.getClass().getName());
        }
    }

    public static <U> Map<Axis, U> forAxes(U factory) {
        return forAll(BlockStateProperties.AXIS, axis -> applyTransform(factory, axes(axis)));
    }

    public static <U> Map<Axis, U> forHorizontalAxes(U factory) {
        return forAll(BlockStateProperties.HORIZONTAL_AXIS, axis -> applyTransform(factory, axes(axis)));
    }

    public static <U> Map<Direction, U> forDirections(U factory) {
        return forAll(BlockStateProperties.FACING, direction -> applyTransform(factory, directions(direction)));
    }

    public static <U> Map<Direction, U> forHorizontalDirections(U factory) {
        return forAll(BlockStateProperties.HORIZONTAL_FACING, direction -> applyTransform(factory, directions(direction)));
    }

    public static <U> Map<Half, U> forHalves(U factory) {
        return forAll(BlockStateProperties.HALF, halves -> applyTransform(factory, halves(halves)));
    }

    public static <T extends Comparable<T>, U> Map<T, U> forAll(Property<T> property, Function<T, U> factory) {
        return property.getPossibleValues().stream().collect(Collectors.toMap(Function.identity(), factory));
    }

    public static <T1 extends Comparable<T1>, T2 extends Comparable<T2>, U> Map<T1, Map<T2, U>> forAll(
        Property<T1> property1, Property<T2> property2, BiFunction<T1, T2, U> factory
    ) {
        return property1.getPossibleValues().stream().collect(Collectors.toMap(Function.identity(), t1 -> forAll(property2, t2 -> factory.apply((T1)t1, t2))));
    }

    public static <T1 extends Comparable<T1>, T2 extends Comparable<T2>, T3 extends Comparable<T3>, U> Map<T1, Map<T2, Map<T3, U>>> forAll(
        Property<T1> property1, Property<T2> property2, Property<T3> property3, TriFunction<T1, T2, T3, U> factory
    ) {
        return property1.getPossibleValues()
            .stream()
            .collect(Collectors.toMap(Function.identity(), t1 -> forAll(property2, property3, (t2, t3) -> factory.apply((T1)t1, t2, t3))));
    }

    public static CCShapes.MutableShape shape(MutableAABB... boxes) {
        List<MutableAABB> newBoxes = new LinkedList<>();
        Collections.addAll(newBoxes, boxes);
        return new CCShapes.MutableShape(newBoxes, new ArrayList<>());
    }

    public static Pair<MutableVec3, MutableVec3> line(MutableVec3 start, MutableVec3 end) {
        return Pair.of(start, end);
    }

    public static class MutableShape implements AssemblyTransform.Transformable<CCShapes.MutableShape> {
        public List<MutableAABB> boxes;
        public List<Pair<MutableVec3, MutableVec3>> outlines;
        private VoxelShape output = null;

        public MutableShape(List<MutableAABB> boxes, List<Pair<MutableVec3, MutableVec3>> outlines) {
            this.boxes = boxes;
            this.outlines = outlines;
        }

        public MutableShape() {
            this.boxes = new LinkedList<>();
            this.outlines = new LinkedList<>();
        }

        @SafeVarargs
        public final CCShapes.MutableShape outline(Pair<MutableVec3, MutableVec3>... lines) {
            Collections.addAll(this.outlines, lines);
            return this;
        }

        public CCShapes.MutableShape copy() {
            return new CCShapes.MutableShape(
                this.boxes.stream().map(MutableAABB::copy).collect(Collectors.toList()),
                this.outlines.stream().map(p -> Pair.of(p.getFirst().copy(), p.getSecond().copy())).collect(Collectors.toList())
            );
        }

        public VoxelShape toShape() {
            if (this.output == null) {
                VoxelShape shape = Shapes.empty();

                for (MutableAABB box : this.boxes) {
                    shape = Shapes.joinUnoptimized(
                        shape,
                        Shapes.box(
                            Math.min(box.minX, box.maxX),
                            Math.min(box.minY, box.maxY),
                            Math.min(box.minZ, box.maxZ),
                            Math.max(box.maxX, box.minX),
                            Math.max(box.maxY, box.minY),
                            Math.max(box.maxZ, box.minZ)
                        ),
                        BooleanOp.OR
                    );
                }

                VoxelShape var4 = shape.optimize();
                if (!this.outlines.isEmpty()) {
                    var4 = new OutlinedVoxelShape(
                        (VoxelShape)var4, this.outlines.stream().map(p -> Pair.of(p.getFirst().toVec3(), p.getSecond().toVec3())).collect(Collectors.toList())
                    );
                }

                this.output = (VoxelShape)var4;
            }

            return this.output;
        }

        public CCShapes.MutableShape rotateX(int angle) {
            if (this.output != null) {
                throw new IllegalStateException("Cannot modify a shape after it has been built");
            } else {
                for (MutableAABB box : this.boxes) {
                    box.rotateX(angle);
                }

                for (Pair<MutableVec3, MutableVec3> outline : this.outlines) {
                    outline.getFirst().rotateX(angle);
                    outline.getSecond().rotateX(angle);
                }

                return this;
            }
        }

        public CCShapes.MutableShape rotateY(int angle) {
            if (this.output != null) {
                throw new IllegalStateException("Cannot modify a shape after it has been built");
            } else {
                for (MutableAABB box : this.boxes) {
                    box.rotateY(angle);
                }

                for (Pair<MutableVec3, MutableVec3> outline : this.outlines) {
                    outline.getFirst().rotateY(angle);
                    outline.getSecond().rotateY(angle);
                }

                return this;
            }
        }

        public CCShapes.MutableShape rotateZ(int angle) {
            if (this.output != null) {
                throw new IllegalStateException("Cannot modify a shape after it has been built");
            } else {
                for (MutableAABB box : this.boxes) {
                    box.rotateZ(angle);
                }

                for (Pair<MutableVec3, MutableVec3> outline : this.outlines) {
                    outline.getFirst().rotateZ(angle);
                    outline.getSecond().rotateZ(angle);
                }

                return this;
            }
        }

        public CCShapes.MutableShape flipX(boolean flip) {
            if (this.output != null) {
                throw new IllegalStateException("Cannot modify a shape after it has been built");
            } else {
                for (MutableAABB box : this.boxes) {
                    box.flipX(flip);
                }

                for (Pair<MutableVec3, MutableVec3> outline : this.outlines) {
                    outline.getFirst().flipX(flip);
                    outline.getSecond().flipX(flip);
                }

                return this;
            }
        }

        public CCShapes.MutableShape flipY(boolean flip) {
            if (this.output != null) {
                throw new IllegalStateException("Cannot modify a shape after it has been built");
            } else {
                for (MutableAABB box : this.boxes) {
                    box.flipY(flip);
                }

                for (Pair<MutableVec3, MutableVec3> outline : this.outlines) {
                    outline.getFirst().flipY(flip);
                    outline.getSecond().flipY(flip);
                }

                return this;
            }
        }

        public CCShapes.MutableShape flipZ(boolean flip) {
            if (this.output != null) {
                throw new IllegalStateException("Cannot modify a shape after it has been built");
            } else {
                for (MutableAABB box : this.boxes) {
                    box.flipZ(flip);
                }

                for (Pair<MutableVec3, MutableVec3> outline : this.outlines) {
                    outline.getFirst().flipZ(flip);
                    outline.getSecond().flipZ(flip);
                }

                return this;
            }
        }
    }
}
