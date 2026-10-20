package com.copycatsplus.copycats.utility;

import java.util.Objects;
import java.util.function.Function;

@FunctionalInterface
public interface TriFunction<T, U, P, R> {
    R apply(T var1, U var2, P var3);

    default <V> TriFunction<T, U, P, V> andThen(Function<? super R, ? extends V> after) {
        Objects.requireNonNull(after);
        return (t, u, p) -> (V)after.apply(this.apply(t, u, p));
    }
}
