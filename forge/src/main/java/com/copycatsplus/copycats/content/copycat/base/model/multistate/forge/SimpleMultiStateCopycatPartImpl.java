package com.copycatsplus.copycats.content.copycat.base.model.multistate.forge;

import com.copycatsplus.copycats.content.copycat.base.model.multistate.MultiStateCopycatModelPart;
import net.minecraft.client.resources.model.BakedModel;

public class SimpleMultiStateCopycatPartImpl {

    public static BakedModel create(BakedModel original, MultiStateCopycatModelPart part) {
        return new SimpleMultiStateCopycatModel(original, part);
    }
}
