package com.copycatsplus.copycats.content.copycat.base.model.fabric;

import com.copycatsplus.copycats.content.copycat.base.model.CopycatModelCore;
import net.minecraft.client.resources.model.BakedModel;
import org.jetbrains.annotations.NotNull;

public class CopycatModelCoreImpl {

    @NotNull
    public static BakedModel createModel(BakedModel original, CopycatModelCore core) {
        return new CopycatModelFabric(original, core, false);
    }

    @NotNull
    public static BakedModel createModelWithoutAO(BakedModel original, CopycatModelCore core) {
        return new CopycatModelFabric(original, core, true);
    }
}
