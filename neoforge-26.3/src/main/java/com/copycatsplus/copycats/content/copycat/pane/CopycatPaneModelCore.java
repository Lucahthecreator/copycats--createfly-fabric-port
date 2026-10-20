package com.copycatsplus.copycats.content.copycat.pane;

import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.AssemblyTransform;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableCullFace;
import com.zurrtum.create.catnip.data.Iterate;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.state.BlockState;

public class CopycatPaneModelCore extends CopycatModelCore {
    @Override
    public void registerModels(List<CopycatModelCore.ModelEntry> entries) {
        entries.add(
            new CopycatModelCore.ModelEntry(
                "material", CopycatModelCore.ModelGetter.MATERIAL, this, updatePropertiesIfMatch(IronBarsBlock.class), CopycatModelCore.EntryType.COPYCAT
            )
        );
    }

    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
        if (material.getBlock() instanceof IronBarsBlock) {
            context.assembleAll();
        } else {
            Set<Direction> present = Arrays.stream(Iterate.horizontalDirections)
                .filter(dir -> state.getValue(CopycatPaneBlock.propertyForDirection(dir)))
                .collect(Collectors.toSet());
            if (present.size() == 2 && present.contains(Direction.NORTH) && present.contains(Direction.SOUTH)) {
                context.assemblePiece(
                    AssemblyTransform.IDENTITY,
                    CopycatRenderContext.vec3(7.0, 0.0, 0.0),
                    CopycatRenderContext.aabb(1.0, 16.0, 16.0).move(0.0, 0.0, 0.0),
                    CopycatRenderContext.cull(MutableCullFace.EAST)
                );
                context.assemblePiece(
                    AssemblyTransform.IDENTITY,
                    CopycatRenderContext.vec3(8.0, 0.0, 0.0),
                    CopycatRenderContext.aabb(1.0, 16.0, 16.0).move(15.0, 0.0, 0.0),
                    CopycatRenderContext.cull(MutableCullFace.WEST)
                );
            } else if (present.size() == 2 && present.contains(Direction.EAST) && present.contains(Direction.WEST)) {
                context.assemblePiece(
                    AssemblyTransform.IDENTITY,
                    CopycatRenderContext.vec3(0.0, 0.0, 7.0),
                    CopycatRenderContext.aabb(16.0, 16.0, 1.0).move(0.0, 0.0, 0.0),
                    CopycatRenderContext.cull(MutableCullFace.SOUTH)
                );
                context.assemblePiece(
                    AssemblyTransform.IDENTITY,
                    CopycatRenderContext.vec3(0.0, 0.0, 8.0),
                    CopycatRenderContext.aabb(16.0, 16.0, 1.0).move(0.0, 0.0, 15.0),
                    CopycatRenderContext.cull(MutableCullFace.NORTH)
                );
            } else if (present.size() == 1) {
                Direction dir = present.iterator().next();
                AssemblyTransform directionTransform = t -> t.rotateY((int)dir.toYRot());
                context.assemblePiece(
                    directionTransform,
                    CopycatRenderContext.vec3(7.0, 0.0, 7.0),
                    CopycatRenderContext.aabb(1.0, 16.0, 9.0).move(0.0, 0.0, 7.0),
                    CopycatRenderContext.cull(MutableCullFace.EAST | MutableCullFace.NORTH)
                );
                context.assemblePiece(
                    directionTransform,
                    CopycatRenderContext.vec3(8.0, 0.0, 7.0),
                    CopycatRenderContext.aabb(1.0, 16.0, 9.0).move(15.0, 0.0, 7.0),
                    CopycatRenderContext.cull(MutableCullFace.WEST | MutableCullFace.NORTH)
                );
                context.assemblePiece(
                    directionTransform,
                    CopycatRenderContext.vec3(7.0, 0.0, 7.0),
                    CopycatRenderContext.aabb(1.0, 16.0, 9.0).move(7.0, 0.0, 7.0),
                    CopycatRenderContext.cull(MutableCullFace.UP | MutableCullFace.DOWN | MutableCullFace.EAST | MutableCullFace.WEST | MutableCullFace.SOUTH)
                );
                context.assemblePiece(
                    directionTransform,
                    CopycatRenderContext.vec3(8.0, 0.0, 7.0),
                    CopycatRenderContext.aabb(1.0, 16.0, 9.0).move(8.0, 0.0, 7.0),
                    CopycatRenderContext.cull(MutableCullFace.UP | MutableCullFace.DOWN | MutableCullFace.EAST | MutableCullFace.WEST | MutableCullFace.SOUTH)
                );
            } else {
                for (Direction direction : Iterate.horizontalDirections) {
                    AssemblyTransform directionTransform = t -> t.rotateY((int)direction.toYRot());
                    context.assemblePiece(
                        directionTransform,
                        CopycatRenderContext.vec3(7.0, 0.0, 8.0),
                        CopycatRenderContext.aabb(1.0, 16.0, 1.0).move(0.0, 0.0, 8.0),
                        CopycatRenderContext.cull(
                            (present.contains(direction.getClockWise()) ? MutableCullFace.WEST : 0)
                                | MutableCullFace.SOUTH
                                | MutableCullFace.NORTH
                                | MutableCullFace.EAST
                        )
                    );
                    if (!present.contains(direction)) {
                        context.assemblePiece(
                            directionTransform,
                            CopycatRenderContext.vec3(7.0, 0.0, 8.0),
                            CopycatRenderContext.aabb(1.0, 16.0, 1.0).move(7.0, 0.0, 15.0),
                            CopycatRenderContext.cull(
                                MutableCullFace.NORTH | MutableCullFace.EAST | MutableCullFace.WEST | MutableCullFace.UP | MutableCullFace.DOWN
                            )
                        );
                    }
                }

                for (Direction directionx : present) {
                    AssemblyTransform directionTransform = t -> t.rotateY((int)directionx.toYRot());
                    context.assemblePiece(
                        directionTransform,
                        CopycatRenderContext.vec3(7.0, 0.0, 9.0),
                        CopycatRenderContext.aabb(1.0, 16.0, 7.0).move(0.0, 0.0, 9.0),
                        CopycatRenderContext.cull(MutableCullFace.EAST | MutableCullFace.NORTH)
                    );
                    context.assemblePiece(
                        directionTransform,
                        CopycatRenderContext.vec3(8.0, 0.0, 9.0),
                        CopycatRenderContext.aabb(1.0, 16.0, 7.0).move(15.0, 0.0, 9.0),
                        CopycatRenderContext.cull(MutableCullFace.WEST | MutableCullFace.NORTH)
                    );
                }
            }
        }
    }
}
