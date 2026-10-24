/*
 * Decompiled with CFR 0.152.
 */
package com.copycatsplus.copycats.utility;

import java.util.Objects;
import java.util.function.Function;

@FunctionalInterface
public interface TriFunction<T, U, P, R> {
    public R apply(T var1, U var2, P var3);

    default public <V> TriFunction<T, U, P, V> andThen(Function<? super R, ? extends V> after) {
        Objects.requireNonNull(after);
        return (t, u, p) -> after.apply((R)this.apply(t, u, p));
    }
}

