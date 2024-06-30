package com.copycatsplus.copycats.content.copycat.base.model.assembly;

import net.minecraft.world.level.block.state.BlockState;

@FunctionalInterface
public interface CopycatModelPart {
    void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material);
}
