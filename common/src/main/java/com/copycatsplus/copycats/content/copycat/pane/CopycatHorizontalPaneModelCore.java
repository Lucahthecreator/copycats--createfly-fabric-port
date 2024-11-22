package com.copycatsplus.copycats.content.copycat.pane;

import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.AssemblyTransform;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext;
import net.minecraft.world.level.block.state.BlockState;

import static com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext.*;
import static com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableCullFace.DOWN;
import static com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableCullFace.UP;

public class CopycatHorizontalPaneModelCore extends CopycatModelCore {


    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
        AssemblyTransform transform = t -> t.rotateY(0);
        context.assemblePiece(transform,
                vec3(0, 7, 0),
                aabb(16, 1, 16),
                cull(UP));
        context.assemblePiece(transform,
                vec3(0, 8, 0),
                aabb(16, 1, 16),
                cull(DOWN));
    }
}
