package com.copycatsplus.copycats.content.copycat.vertical_half_layer;

import com.copycatsplus.copycats.CCBlocks;
import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.AssemblyTransform;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.quad.QuadAutoCull;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;

import java.util.List;
import java.util.Objects;

import static com.copycatsplus.copycats.content.copycat.vertical_half_layer.CopycatVerticalHalfLayerBlock.*;
import static com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext.*;
import static com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableCullFace.*;

public class CopycatVerticalMultiHalfLayerModelCore extends CopycatModelCore {

    @Override
    public void registerModels(List<ModelEntry> entries) {
        registerForMultiState(entries, CCBlocks.COPYCAT_VERTICAL_HALF_LAYER.get(), false);
    }

    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
        if (Objects.equals(key, LEFT_LAYERS.getName()) && state.getValue(LEFT_LAYERS) == 0)
            return;
        if (Objects.equals(key, RIGHT_LAYERS.getName()) && state.getValue(RIGHT_LAYERS) == 0)
            return;

        int rot = (int) state.getValue(FACING).toYRot();
        boolean positive = key.equals(LEFT_LAYERS.getName());
        int layer = state.getValue(positive ? LEFT_LAYERS : RIGHT_LAYERS);
        if (layer == 0) return;
        AssemblyTransform transform = t -> t.rotateY(rot + (positive ? 180 : 0));
        QuadAutoCull autoCull = autoCull(aabb(8, 16, 16));
        context.assemblePiece(
                transform,
                vec3(0, 0, 0),
                aabb(4, 16, layer),
                cull(EAST | SOUTH),
                autoCull
        );
       context.assemblePiece(
                transform,
                vec3(0, 0, layer),
                aabb(4, 16, layer).move(0, 0, 16 - layer),
                cull(EAST | NORTH),
                autoCull
        );
         context.assemblePiece(
                transform,
                vec3(4, 0, 0),
                aabb(4, 16, layer).move(12, 0, 0),
                cull(WEST | SOUTH),
                autoCull
        );
        context.assemblePiece(
                transform,
                vec3(4, 0, layer),
                aabb(4, 16, layer).move(12, 0, 16 - layer),
                cull(WEST | NORTH),
                autoCull
        );
    }
}
