package com.copycatsplus.copycats.content.copycat.shaft;

import com.copycatsplus.copycats.CCCopycatPartialModels;
import com.copycatsplus.copycats.foundation.copycat.model.kinetic.KineticCopycatRenderData;
import com.simibubi.create.content.kinetics.base.SingleAxisRotatingVisual;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;

public class CopycatShaftInstance extends SingleAxisRotatingVisual<CopycatShaftBlockEntity> {
    public CopycatShaftInstance(VisualizationContext context, CopycatShaftBlockEntity blockEntity, float partialTick) {
        super(context, blockEntity, partialTick, CCCopycatPartialModels.getSimpleModel(KineticCopycatRenderData.of(CCCopycatPartialModels.SHAFT, blockEntity)));
    }
}
