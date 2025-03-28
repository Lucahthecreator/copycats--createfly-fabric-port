package com.copycatsplus.copycats.foundation.copycat.model.kinetic;

import com.copycatsplus.copycats.foundation.copycat.ICopycatBlockEntity;
import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlockEntity;
import net.createmod.catnip.render.SuperByteBuffer;

/**
 * An interface with implementation for kinetic copycats renderers.
 * <p>
 * Implementors should redirect calls of {@link IKineticCopycatBlockRenderer#getRotatedModel} to this interface.
 */
public interface IKineticCopycatBlockRenderer {

    default SuperByteBuffer getRotatedModel(ICopycatPartialModel partialModel, ICopycatBlockEntity be) {
        return KineticCopycatRenderer.getRenderedBuffer(partialModel, be);
    }

    default SuperByteBuffer getRotatedModel(ICopycatPartialModel partialModel, IMultiStateCopycatBlockEntity be, String property) {
        return KineticCopycatRenderer.getRenderedBuffer(partialModel, be, property);
    }
}
