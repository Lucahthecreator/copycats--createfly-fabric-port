package com.copycatsplus.copycats.content.copycat.base.model.fabric;

import com.copycatsplus.copycats.content.copycat.base.model.CopycatModelPart;
import net.minecraft.client.resources.model.BakedModel;

public class CopycatModelPartImpl {

    public static BakedModel create(BakedModel original, CopycatModelPart part) {
        return new SimpleCopycatModel(original, part);
    }
}
