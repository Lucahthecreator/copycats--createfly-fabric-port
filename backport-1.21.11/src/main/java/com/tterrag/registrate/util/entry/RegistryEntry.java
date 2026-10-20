/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.resources.Identifier
 */
package com.tterrag.registrate.util.entry;

import java.util.function.Supplier;
import net.minecraft.resources.Identifier;

public class RegistryEntry<T>
implements Supplier<T> {
    protected final Identifier id;
    protected final T value;

    public RegistryEntry(Identifier id, T value) {
        this.id = id;
        this.value = value;
    }

    @Override
    public T get() {
        return this.value;
    }

    public Identifier getId() {
        return this.id;
    }

    public boolean is(T other) {
        return this.value == other;
    }
}

