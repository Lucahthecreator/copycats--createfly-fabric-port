package com.copycatsplus.copycats.config;

import com.copycatsplus.copycats.internal.registrate.builders.BlockBuilder;
import com.copycatsplus.copycats.internal.registrate.util.nullness.NonNullUnaryOperator;
import net.minecraft.world.level.block.Block;

public class CStress {
    public static <B extends Block, P> NonNullUnaryOperator<BlockBuilder<B, P>> setNoImpact() {
        return setImpact(0.0);
    }

    public static <B extends Block, P> NonNullUnaryOperator<BlockBuilder<B, P>> setImpact(double value) {
        return builder -> builder;
    }

    public static <B extends Block, P> NonNullUnaryOperator<BlockBuilder<B, P>> setCapacity(double value) {
        return builder -> builder;
    }
}
