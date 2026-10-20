/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.level.block.Blocks
 *  net.minecraft.world.level.block.state.BlockBehaviour
 *  net.minecraft.world.level.block.state.BlockBehaviour$Properties
 */
package com.zurrtum.create.foundation.data;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class SharedProperties {
    public static BlockBehaviour.Properties softMetal() {
        return BlockBehaviour.Properties.ofFullCopy((BlockBehaviour)Blocks.GOLD_BLOCK);
    }
}

