package com.copycatsplus.copycats.content.copycat.pane;

import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.AssemblyTransform;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.quad.QuadTransform;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;
import java.util.stream.Collectors;

import static com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext.*;
import static com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableCullFace.*;

public class CopycatPaneModelCore extends CopycatModelCore {

    @Override
    public void registerModels(List<ModelEntry> entries) {
        entries.add(new ModelEntry(MATERIAL_KEY, ModelGetter.MATERIAL, this, updatePropertiesIfMatch(IronBarsBlock.class), EntryType.COPYCAT));
    }

    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
        Set<Direction> present = Direction.stream().filter(dir -> dir.getAxis().isHorizontal()).filter(dir -> state.getValue(CopycatPaneBlock.propertyForDirection(dir))).collect(Collectors.toSet());
        AssemblyTransform transform = t -> t.rotateY((int) centerRotation(present));
        context.assemblePiece(transform,
                vec3(8, 0, 7),
                aabb(1, 16, 2).move(7, 0, 7),
                cull(EAST | (present.contains(Direction.SOUTH) ? SOUTH : 0) |
                        (present.contains(Direction.NORTH) ? NORTH : 0) |
                        (present.contains(Direction.EAST) ? EAST : 0) |
                        (present.contains(Direction.WEST) ? WEST : 0)),
                centerTransforms(present));
        context.assemblePiece(transform,
                vec3(7, 0, 7),
                aabb(1, 16, 2).move(7, 0, 7),
                cull(WEST | (present.contains(Direction.SOUTH) ? SOUTH : 0) |
                        (present.contains(Direction.NORTH) ? NORTH : 0) |
                        (present.contains(Direction.EAST) ? EAST : 0) |
                        (present.contains(Direction.WEST) ? WEST : 0)),
                centerTransforms(present));

        for (Direction direction : Iterate.horizontalDirections) {
            if (state.getValue(CopycatPaneBlock.propertyForDirection(direction))) {
                AssemblyTransform directionTransform = t -> t.rotateY((int) direction.toYRot());
                context.assemblePiece(directionTransform,
                        vec3(7, 0, 9),
                        aabb(1, 16, 7).move(0, 0, 9),
                        cull(0),
                        directionalTransforms(direction));
                context.assemblePiece(directionTransform,
                        vec3(8, 0, 9),
                        aabb(1, 16, 7).move(0, 0, 9),
                        cull(0),
                        directionalTransforms(direction));
            }
        }
    }

    private static float centerRotation(Set<Direction> present) {
        for (Direction direction : present) {
            return switch (direction) {
                case NORTH -> Direction.NORTH.toYRot();
                case SOUTH -> Direction.NORTH.toYRot();
                case WEST -> Direction.WEST.toYRot();
                case EAST -> Direction.EAST.toYRot();
                default -> throw new IllegalStateException("Unexpected value: " + direction);
            };
        }
        return 0f;
    }

    private static QuadTransform[] centerTransforms(Set<Direction> present) {
        Set<QuadTransform> transforms = new HashSet<>();
        for (Direction direction : present) {
            switch (direction) {
                case NORTH -> {
                    transforms.add(uvTranslate(Direction.UP, -8f, 0f));
                    transforms.add(uvTranslate(Direction.DOWN, -8f, 0f));
                }
                case WEST -> {
                    transforms.add(uvTranslate(Direction.UP, 0f, -7f));
                    transforms.add(uvTranslate(Direction.DOWN, 0f, -7f));
                }
                case SOUTH -> {
                    transforms.add(uvTranslate(Direction.UP, 0f, 0f));
                }
                case EAST -> {
                    transforms.add(uvTranslate(Direction.UP, 0f, (present.contains(Direction.WEST) ? -7f : -8f)));
                    transforms.add(uvTranslate(Direction.DOWN, 0f, (present.contains(Direction.WEST) ? -7f : -8f)));
                }
                default -> throw new IllegalStateException("Unexpected value: " + direction);
            }
        }
        return transforms.toArray(new QuadTransform[]{});
    }

    private static QuadTransform[] directionalTransforms(Direction direction) {
        Set<QuadTransform> transforms = new HashSet<>();
        switch (direction) {
            case NORTH -> {
                transforms.add(uvTranslate(Direction.UP, -15f, 0f));
                transforms.add(uvTranslate(Direction.DOWN, 15, 0f));
            }
            case WEST -> {
                transforms.add(uvTranslate(Direction.UP, 0f, 0f));
                transforms.add(uvTranslate(Direction.DOWN, 0f, 0f));
            }
            case SOUTH -> {
                transforms.add(uvTranslate(Direction.UP, 0f, 0f));
            }
            case EAST -> {
                transforms.add(uvTranslate(Direction.UP, 0f, -15f));
                transforms.add(uvTranslate(Direction.DOWN, 0f, 15f));
            }
            default -> throw new IllegalStateException("Unexpected value: " + direction);
        }
        return transforms.toArray(new QuadTransform[]{});
    }
}
