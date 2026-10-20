package com.copycatsplus.copycats.internal.registrate.providers;

public interface DataGenContext<R, T extends R> {
    T get();

    default T getEntry() {
        return this.get();
    }

    String getName();
}
