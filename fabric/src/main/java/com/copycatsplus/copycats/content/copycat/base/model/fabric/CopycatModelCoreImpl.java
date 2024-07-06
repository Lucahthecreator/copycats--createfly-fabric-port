package com.copycatsplus.copycats.content.copycat.base.model.fabric;

import com.copycatsplus.copycats.content.copycat.base.model.CopycatModelCore;
import net.minecraft.client.resources.model.BakedModel;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class CopycatModelCoreImpl {

    @NotNull
    public static BakedModel createModel(BakedModel original, CopycatModelCore core) {
        return new CopycatModelFabric(original, core, false, s -> s);
    }

    @NotNull
    public static BakedModel createKineticModel(BakedModel original, CopycatModelCore core, Function<String, String> keyMapper) {
        return new CopycatModelFabric(original, core, true, keyMapper);
    }
}
