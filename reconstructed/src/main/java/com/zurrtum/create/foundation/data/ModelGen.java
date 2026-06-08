/*
 * Decompiled with CFR 0.152.
 */
package com.zurrtum.create.foundation.data;

import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;

public class ModelGen {
    public static <T> NonNullUnaryOperator<T> customItemModel(String parent, String model) {
        return value -> value;
    }
}

