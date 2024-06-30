package com.copycatsplus.copycats.content.copycat.test_block;

import com.copycatsplus.copycats.content.copycat.base.model.CopycatModelCore;
import com.copycatsplus.copycats.content.copycat.base.model.assembly.CopycatRenderContext;
import com.copycatsplus.copycats.content.copycat.slab.CopycatMultiSlabModelPart;
import net.minecraft.world.level.block.state.BlockState;

public class CopycatMultiTestBlockModelPart extends CopycatModelCore {

    private final CopycatMultiSlabModelPart model = new CopycatMultiSlabModelPart();

    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
        model.emitCopycatQuads(key, state, context, material);
    }
}
