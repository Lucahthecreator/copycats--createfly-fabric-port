package com.copycatsplus.copycats.content.copycat.base.model.kinetic;

import com.copycatsplus.copycats.content.copycat.base.ICopycatBlockEntity;
import com.copycatsplus.copycats.content.copycat.base.multistate.IMultiStateCopycatBlock;
import com.copycatsplus.copycats.content.copycat.base.multistate.IMultiStateCopycatBlockEntity;
import com.copycatsplus.copycats.content.copycat.partial.CopycatPartialModel;
import com.simibubi.create.foundation.render.SuperByteBuffer;

/**
 * An interface with implementation for kinetic copycats renderers.
 * <p>
 * Implementors should redirect calls of {@link IKineticCopycatBlockRenderer#getRotatedModel} to this interface.
 */
public interface IKineticCopycatBlockRenderer {

    default SuperByteBuffer getRotatedModel(CopycatPartialModel partialModel, ICopycatBlockEntity be) {
        return KineticCopycatRenderer.getBuffer(partialModel, be);
    }

    default SuperByteBuffer getRotatedModel(CopycatPartialModel partialModel, IMultiStateCopycatBlockEntity be, String property) {
        return KineticCopycatRenderer.getBuffer(partialModel, be, property);
    }
}
