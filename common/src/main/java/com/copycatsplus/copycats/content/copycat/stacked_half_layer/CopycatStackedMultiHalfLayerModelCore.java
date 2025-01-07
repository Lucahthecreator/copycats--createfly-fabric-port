package com.copycatsplus.copycats.content.copycat.stacked_half_layer;

import com.copycatsplus.copycats.CCBlocks;
import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.AssemblyTransform;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.quad.QuadAutoCull;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.Objects;

import static com.copycatsplus.copycats.content.copycat.stacked_half_layer.CopycatStackedHalfLayerBlock.*;
import static com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext.*;
import static com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableCullFace.*;

public class CopycatStackedMultiHalfLayerModelCore extends CopycatModelCore {

    @Override
    public void registerModels(List<ModelEntry> entries) {
        registerForMultiState(entries, CCBlocks.COPYCAT_STACKED_HALF_LAYER.get(), false);
    }

    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
        if (Objects.equals(key, TOP_LAYERS.getName()) && state.getValue(TOP_LAYERS) == 0)
            return;
        if (Objects.equals(key, BOTTOM_LAYERS.getName()) && state.getValue(BOTTOM_LAYERS) == 0)
            return;

        int rot = (int) state.getValue(FACING).toYRot();
        boolean positive = key.equals(TOP_LAYERS.getName());
        int layer = state.getValue(positive ? TOP_LAYERS : BOTTOM_LAYERS);
        if (layer == 0) return;
        AssemblyTransform transform = t -> t.flipY(positive).rotateY(rot + 180);
        QuadAutoCull autoCull = autoCull(aabb(16, 8, 16));
        context.assemblePiece(
                transform,
                vec3(0, 0, 0),
                aabb(16, 4, layer),
                cull(UP | SOUTH),
                autoCull
        );
        context.assemblePiece(
                transform,
                vec3(0, 0, layer),
                aabb(16, 4, layer).move(0, 0, 16 - layer),
                cull(UP | NORTH),
                autoCull
        );
        context.assemblePiece(
                transform,
                vec3(0, 4, 0),
                aabb(16, 4, layer).move(0, 12, 0),
                cull(DOWN | SOUTH),
                autoCull
        );
        context.assemblePiece(
                transform,
                vec3(0, 4, layer),
                aabb(16, 4, layer).move(0, 12, 16 - layer),
                cull(DOWN | NORTH),
                autoCull
        );
    }
}
