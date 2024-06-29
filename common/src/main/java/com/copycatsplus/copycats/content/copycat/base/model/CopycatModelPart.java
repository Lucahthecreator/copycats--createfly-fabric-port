package com.copycatsplus.copycats.content.copycat.base.model;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.level.block.state.BlockState;

import static com.copycatsplus.copycats.content.copycat.base.model.assembly.Assembler.*;

public interface CopycatModelPart {

    @ExpectPlatform
    static BakedModel create(BakedModel original, CopycatModelPart part) {
        throw new AssertionError();
    }


    void emitCopycatQuads(BlockState state, CopycatRenderContext context, BlockState material);

    interface WithData<T> extends CopycatModelPart {
        void acceptData(T data);
    }
}
