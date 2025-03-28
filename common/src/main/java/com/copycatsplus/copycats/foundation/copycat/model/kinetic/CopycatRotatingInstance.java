package com.copycatsplus.copycats.foundation.copycat.model.kinetic;

import com.simibubi.create.content.kinetics.base.RotatingInstance;

public record CopycatRotatingInstance(KineticCopycatRenderData renderData,
                                      RotatingInstance rotatingInstance) {
    public static CopycatRotatingInstance of(KineticCopycatRenderData renderData, RotatingInstance rotatingInstance) {
        return new CopycatRotatingInstance(renderData, rotatingInstance);
    }
}
