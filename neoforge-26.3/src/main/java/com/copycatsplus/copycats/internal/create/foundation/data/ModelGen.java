package com.copycatsplus.copycats.internal.create.foundation.data;

import com.copycatsplus.copycats.internal.registrate.util.nullness.NonNullUnaryOperator;

public class ModelGen {
    public static <T> NonNullUnaryOperator<T> customItemModel(String parent, String model) {
        return value -> value;
    }
}
