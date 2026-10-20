package com.copycatsplus.copycats.internal.registrate.util.nullness;

import java.util.function.Supplier;

@FunctionalInterface
public interface NonNullSupplier<T> extends Supplier<T> {
}
