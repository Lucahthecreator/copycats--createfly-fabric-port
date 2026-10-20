package com.copycatsplus.copycats.content.copycat.trapdoor;

import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.AssemblyTransform;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableCullFace;
import java.util.List;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;

public class CopycatTrapdoorModelCore extends CopycatModelCore {
    @Override
    public void registerModels(List<CopycatModelCore.ModelEntry> entries) {
        entries.add(
            new CopycatModelCore.ModelEntry(
                "material", CopycatModelCore.ModelGetter.MATERIAL, this, updatePropertiesIfMatch(TrapDoorBlock.class), CopycatModelCore.EntryType.COPYCAT
            )
        );
    }

    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
        if (material.getBlock() instanceof TrapDoorBlock) {
            context.assembleAll();
        } else {
            int rot = (int)state.getValue(TrapDoorBlock.FACING).toYRot();
            boolean flipY = state.getValue(TrapDoorBlock.HALF) == Half.TOP;
            boolean open = state.getValue(TrapDoorBlock.OPEN);
            AssemblyTransform transform = t -> t.rotateY(rot).flipY(flipY);
            if (!open) {
                context.assemblePiece(
                    transform,
                    CopycatRenderContext.vec3(0.0, 0.0, 0.0),
                    CopycatRenderContext.aabb(16.0, 1.0, 16.0),
                    CopycatRenderContext.cull(MutableCullFace.UP)
                );
                context.assemblePiece(
                    transform,
                    CopycatRenderContext.vec3(0.0, 1.0, 0.0),
                    CopycatRenderContext.aabb(16.0, 2.0, 16.0).move(0.0, 14.0, 0.0),
                    CopycatRenderContext.cull(MutableCullFace.DOWN)
                );
            } else {
                context.assemblePiece(
                    transform,
                    CopycatRenderContext.vec3(0.0, 0.0, 0.0),
                    CopycatRenderContext.aabb(16.0, 16.0, 1.0),
                    CopycatRenderContext.cull(MutableCullFace.SOUTH)
                );
                context.assemblePiece(
                    transform,
                    CopycatRenderContext.vec3(0.0, 0.0, 1.0),
                    CopycatRenderContext.aabb(16.0, 16.0, 2.0).move(0.0, 0.0, 14.0),
                    CopycatRenderContext.cull(MutableCullFace.NORTH)
                );
            }
        }
    }
}
