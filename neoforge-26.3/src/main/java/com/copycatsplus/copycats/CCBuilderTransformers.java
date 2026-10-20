package com.copycatsplus.copycats;

import com.copycatsplus.copycats.internal.create.foundation.data.SharedProperties;
import com.copycatsplus.copycats.internal.create.foundation.data.TagGen;
import com.copycatsplus.copycats.internal.registrate.builders.BlockBuilder;
import com.copycatsplus.copycats.internal.registrate.util.nullness.NonNullUnaryOperator;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.MapColor;

public class CCBuilderTransformers {
    public static <B extends Block, P> NonNullUnaryOperator<BlockBuilder<B, P>> copycat() {
        return b -> (BlockBuilder)b.initialProperties(SharedProperties::softMetal)
            .properties(p -> p.noOcclusion().mapColor(MapColor.NONE).forceSolidOn())
            .transform(TagGen.axeOrPickaxe());
    }

    public static <B extends Block, P> NonNullUnaryOperator<BlockBuilder<B, P>> multiCopycat() {
        return copycat();
    }
}
