/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.material.MapColor
 */
package com.copycatsplus.copycats;

import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import com.zurrtum.create.foundation.data.SharedProperties;
import com.zurrtum.create.foundation.data.TagGen;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.MapColor;

public class CCBuilderTransformers {
    public static <B extends Block, P> NonNullUnaryOperator<BlockBuilder<B, P>> copycat() {
        return b -> b.initialProperties(SharedProperties::softMetal).properties(p -> p.noOcclusion().mapColor(MapColor.NONE).forceSolidOn()).transform(TagGen.axeOrPickaxe());
    }

    public static <B extends Block, P> NonNullUnaryOperator<BlockBuilder<B, P>> multiCopycat() {
        return CCBuilderTransformers.copycat();
    }
}

