package com.copycatsplus.copycats.internal.create.foundation.data;

import com.copycatsplus.copycats.internal.registrate.AbstractRegistrate;
import com.copycatsplus.copycats.internal.registrate.util.nullness.NonNullConsumer;
import java.util.function.Supplier;

public class CreateRegistrate extends AbstractRegistrate<CreateRegistrate> {
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
