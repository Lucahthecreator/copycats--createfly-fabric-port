/*
 * Decompiled with CFR 0.152.
 */
package com.tterrag.registrate.providers;

public interface DataGenContext<R, T extends R> {
    public T get();

    default public T getEntry() {
        return this.get();
    }

    public String getName();
}

