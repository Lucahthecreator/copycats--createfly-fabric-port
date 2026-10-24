/*
 * Decompiled with CFR 0.152.
 */
package com.zurrtum.create.foundation.data;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import java.util.function.Supplier;

public class CreateRegistrate
extends AbstractRegistrate<CreateRegistrate> {
    protected CreateRegistrate(String modid) {
        super(modid);
    }

    public static <T> NonNullConsumer<T> blockModel(Supplier<?> ignored) {
        return value -> {};
    }

    public static <T> NonNullConsumer<T> itemModel(Supplier<?> ignored) {
        return value -> {};
    }
}

