package com.copycatsplus.copycats.foundation.copycat.model.kinetic;

import com.zurrtum.create.client.flywheel.lib.instance.AbstractInstance;

public record CopycatInstance<T extends AbstractInstance>(KineticCopycatRenderData renderData,
                                                          T instance) {
    public static <T extends AbstractInstance> CopycatInstance<T> of(KineticCopycatRenderData renderData, T instance) {
        return new CopycatInstance<T>(renderData, instance);
    }
}
