package com.copycatsplus.copycats.content.copycat.base.model.functional;

import com.copycatsplus.copycats.content.copycat.base.ICopycatBlockEntity;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import net.minecraft.world.level.block.state.BlockState;

public interface IFunctionalCopycatBlockRenderer {

    default SuperByteBuffer getRotatedModel(ICopycatBlockEntity be, BlockState state) {
        return FunctionalCopycatRenderHelper.getBuffer(be);
    }
}
