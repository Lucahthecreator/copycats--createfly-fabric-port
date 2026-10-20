package com.copycatsplus.copycats.content.copycat.ladder;

import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.AssemblyTransform;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext;
import java.util.List;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.state.BlockState;

public class CopycatLadderModelCore extends CopycatModelCore {
    @Override
    public void registerModels(List<CopycatModelCore.ModelEntry> entries) {
        entries.add(
            new CopycatModelCore.ModelEntry(
                "material", CopycatModelCore.ModelGetter.MATERIAL, this, updatePropertiesIfMatch(LadderBlock.class), CopycatModelCore.EntryType.COPYCAT
            )
        );
    }

    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
        if (material.getBlock() instanceof LadderBlock) {
            context.assembleAll();
        } else {
            int rot = (int)state.getValue(LadderBlock.FACING).toYRot();
            AssemblyTransform transform = t -> t.rotateY(rot);
            assemblePoles(context, transform);
            assembleSteps(context, transform);
        }
    }

    public static void assemblePoles(CopycatRenderContext context, AssemblyTransform transform) {
        context.assemblePiece(transform, CopycatRenderContext.vec3(2.0, 0.0, 0.0), CopycatRenderContext.aabb(2.0, 16.0, 1.0), CopycatRenderContext.cull(0));
        context.assemblePiece(
            transform, CopycatRenderContext.vec3(12.0, 0.0, 0.0), CopycatRenderContext.aabb(2.0, 16.0, 1.0).move(14.0, 0.0, 0.0), CopycatRenderContext.cull(0)
        );
    }

    public static void assembleSteps(CopycatRenderContext context, AssemblyTransform transform) {
        context.assemblePiece(transform, CopycatRenderContext.vec3(1.0, 1.0, 0.1), CopycatRenderContext.aabb(14.0, 2.0, 0.8), CopycatRenderContext.cull(0));
        context.assemblePiece(transform, CopycatRenderContext.vec3(1.0, 5.0, 0.1), CopycatRenderContext.aabb(14.0, 2.0, 0.8), CopycatRenderContext.cull(0));
        context.assemblePiece(transform, CopycatRenderContext.vec3(1.0, 9.0, 0.1), CopycatRenderContext.aabb(14.0, 2.0, 0.8), CopycatRenderContext.cull(0));
        context.assemblePiece(transform, CopycatRenderContext.vec3(1.0, 13.0, 0.1), CopycatRenderContext.aabb(14.0, 2.0, 0.8), CopycatRenderContext.cull(0));
    }
}
