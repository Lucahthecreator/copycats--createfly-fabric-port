package com.copycatsplus.copycats.content.copycat.base.model.multistate;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.level.block.state.BlockState;

import static com.copycatsplus.copycats.content.copycat.base.model.assembly.Assembler.*;

public interface MultiStateCopycatModelPart {

    @ExpectPlatform
    static BakedModel create(BakedModel original, MultiStateCopycatModelPart part) {
        throw new AssertionError();
    }


    default void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
        throw new AssertionError("If this is appearing then a model isn't implemented correctly");
    }
}
