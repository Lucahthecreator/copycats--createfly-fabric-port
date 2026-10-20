package com.copycatsplus.copycats.internal.registrate.util.nullness;

import java.util.function.BiConsumer;

@FunctionalInterface
public interface NonNullBiConsumer<T, U> extends BiConsumer<T, U> {
}
