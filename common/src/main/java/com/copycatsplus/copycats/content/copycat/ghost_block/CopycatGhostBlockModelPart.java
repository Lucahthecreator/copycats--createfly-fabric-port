package com.copycatsplus.copycats.content.copycat.ghost_block;

import com.copycatsplus.copycats.content.copycat.base.model.CopycatModelCore;
import com.copycatsplus.copycats.content.copycat.base.model.assembly.CopycatRenderContext;
import net.minecraft.world.level.block.state.BlockState;

public class CopycatGhostBlockModelPart extends CopycatModelCore {

    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
        context.assembleAll(); // assemble without any modifications
    }
}
