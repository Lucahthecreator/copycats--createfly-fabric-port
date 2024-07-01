package com.copycatsplus.copycats.content.copycat.base.model.kinetic;

import com.copycatsplus.copycats.content.copycat.base.ICopycatBlockEntity;
import com.copycatsplus.copycats.content.copycat.partial.CopycatPartialModel;
import com.simibubi.create.foundation.render.SuperByteBuffer;

public interface IKineticCopycatBlockRenderer {

    default SuperByteBuffer getRotatedModel(CopycatPartialModel partialModel, ICopycatBlockEntity be) {
        return KineticCopycatRenderer.getBuffer(partialModel, be);
    }
}
