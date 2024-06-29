package com.copycatsplus.copycats.content.copycat.base.model.forge;

import com.copycatsplus.copycats.content.copycat.base.model.CopycatModelPart;
import net.minecraft.client.resources.model.BakedModel;

public class CopycatModelPartImpl {

    public static BakedModel create(BakedModel original, CopycatModelPart part) {
        return new SimpleCopycatModel(original, part);
    }
}
