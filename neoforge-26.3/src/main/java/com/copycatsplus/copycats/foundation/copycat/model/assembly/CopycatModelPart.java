package com.copycatsplus.copycats.foundation.copycat.model.assembly;

import net.minecraft.world.level.block.state.BlockState;

@FunctionalInterface
public interface CopycatModelPart {
    void emitCopycatQuads(String var1, BlockState var2, CopycatRenderContext var3, BlockState var4);
}
