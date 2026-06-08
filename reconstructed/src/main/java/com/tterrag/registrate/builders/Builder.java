/*
 * Decompiled with CFR 0.152.
 */
package com.tterrag.registrate.builders;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;

public interface Builder<R, T extends R, P, S extends Builder<R, T, P, S>> {
    public AbstractRegistrate<?> getOwner();

    public String getName();

    default public S transform(NonNullUnaryOperator<S> transform) {
        return (S)((Builder)transform.apply(this));
    }
}

