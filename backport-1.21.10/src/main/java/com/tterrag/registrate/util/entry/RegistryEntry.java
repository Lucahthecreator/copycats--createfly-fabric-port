/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.resources.ResourceLocation
 */
package com.tterrag.registrate.util.entry;

import java.util.function.Supplier;
import net.minecraft.resources.ResourceLocation;

public class RegistryEntry<T>
implements Supplier<T> {
    protected final ResourceLocation id;
    protected final T value;

    public RegistryEntry(ResourceLocation id, T value) {
        this.id = id;
        this.value = value;
    }

    @Override
    public T get() {
        return this.value;
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public boolean is(T other) {
        return this.value == other;
    }
}

