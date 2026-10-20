package com.copycatsplus.copycats.internal.registrate.builders;

import com.copycatsplus.copycats.internal.registrate.AbstractRegistrate;
import com.copycatsplus.copycats.internal.registrate.util.nullness.NonNullUnaryOperator;

public interface Builder<R, T extends R, P, S extends Builder<R, T, P, S>> {
    AbstractRegistrate<?> getOwner();

    String getName();

    default S transform(NonNullUnaryOperator<S> transform) {
        return transform.apply((S)this);
    }
}
