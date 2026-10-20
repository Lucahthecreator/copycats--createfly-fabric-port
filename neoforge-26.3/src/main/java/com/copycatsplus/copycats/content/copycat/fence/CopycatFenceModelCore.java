package com.copycatsplus.copycats.content.copycat.fence;

import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.AssemblyTransform;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableCullFace;
import com.zurrtum.create.catnip.data.Iterate;
import java.util.List;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.state.BlockState;

public class CopycatFenceModelCore extends CopycatModelCore {
    @Override
    public void registerModels(List<CopycatModelCore.ModelEntry> entries) {
        entries.add(
            new CopycatModelCore.ModelEntry(
                "material", CopycatModelCore.ModelGetter.MATERIAL, this, updatePropertiesIfMatch(FenceBlock.class), CopycatModelCore.EntryType.COPYCAT
            )
        );
    }

    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
        if (material.getBlock() instanceof FenceBlock) {
            context.assembleAll();
        } else {
            for (Direction direction : Iterate.horizontalDirections) {
                context.assemblePiece(
                    t -> t.rotateY((int)direction.toYRot()),
                    CopycatRenderContext.vec3(6.0, 0.0, 6.0),
                    CopycatRenderContext.aabb(2.0, 16.0, 2.0),
                    CopycatRenderContext.cull(MutableCullFace.SOUTH | MutableCullFace.EAST)
                );
            }

            for (Direction direction : Iterate.horizontalDirections) {
                if (state.getValue(CopycatFenceBlock.byDirection(direction))) {
                    int rot = (int)direction.toYRot();
                    AssemblyTransform transform = t -> t.rotateY(rot);
                    context.assemblePiece(
                        transform,
                        CopycatRenderContext.vec3(7.0, 6.0, 10.0),
                        CopycatRenderContext.aabb(1.0, 1.0, 6.0),
                        CopycatRenderContext.cull(MutableCullFace.UP | MutableCullFace.NORTH | MutableCullFace.EAST)
                    );
                    context.assemblePiece(
                        transform,
                        CopycatRenderContext.vec3(8.0, 6.0, 10.0),
                        CopycatRenderContext.aabb(1.0, 1.0, 6.0).move(15.0, 0.0, 0.0),
                        CopycatRenderContext.cull(MutableCullFace.UP | MutableCullFace.NORTH | MutableCullFace.WEST)
                    );
                    context.assemblePiece(
                        transform,
                        CopycatRenderContext.vec3(7.0, 7.0, 10.0),
                        CopycatRenderContext.aabb(1.0, 2.0, 6.0).move(0.0, 14.0, 0.0),
                        CopycatRenderContext.cull(MutableCullFace.DOWN | MutableCullFace.NORTH | MutableCullFace.EAST)
                    );
                    context.assemblePiece(
                        transform,
                        CopycatRenderContext.vec3(8.0, 7.0, 10.0),
                        CopycatRenderContext.aabb(1.0, 2.0, 6.0).move(15.0, 14.0, 0.0),
                        CopycatRenderContext.cull(MutableCullFace.DOWN | MutableCullFace.NORTH | MutableCullFace.WEST)
                    );
                    context.assemblePiece(
                        transform,
                        CopycatRenderContext.vec3(7.0, 12.0, 10.0),
                        CopycatRenderContext.aabb(1.0, 1.0, 6.0),
                        CopycatRenderContext.cull(MutableCullFace.UP | MutableCullFace.NORTH | MutableCullFace.EAST)
                    );
                    context.assemblePiece(
                        transform,
                        CopycatRenderContext.vec3(8.0, 12.0, 10.0),
                        CopycatRenderContext.aabb(1.0, 1.0, 6.0).move(15.0, 0.0, 0.0),
                        CopycatRenderContext.cull(MutableCullFace.UP | MutableCullFace.NORTH | MutableCullFace.WEST)
                    );
                    context.assemblePiece(
                        transform,
                        CopycatRenderContext.vec3(7.0, 13.0, 10.0),
                        CopycatRenderContext.aabb(1.0, 2.0, 6.0).move(0.0, 14.0, 0.0),
                        CopycatRenderContext.cull(MutableCullFace.DOWN | MutableCullFace.NORTH | MutableCullFace.EAST)
                    );
                    context.assemblePiece(
                        transform,
                        CopycatRenderContext.vec3(8.0, 13.0, 10.0),
                        CopycatRenderContext.aabb(1.0, 2.0, 6.0).move(15.0, 14.0, 0.0),
                        CopycatRenderContext.cull(MutableCullFace.DOWN | MutableCullFace.NORTH | MutableCullFace.WEST)
                    );
                }
            }
        }
    }
}
