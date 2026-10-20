package com.copycatsplus.copycats.internal.create.foundation.data;

import com.copycatsplus.copycats.internal.registrate.builders.BlockBuilder;
import com.copycatsplus.copycats.internal.registrate.util.nullness.NonNullUnaryOperator;
import net.minecraft.world.level.block.Block;

public class BuilderTransformers {
    public static <B extends Block, P> NonNullUnaryOperator<BlockBuilder<B, P>> copycat() {
        return builder -> builder;
    }
}
