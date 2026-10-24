/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.state.BlockState
 */
package com.tterrag.registrate.util.entry;

import com.tterrag.registrate.util.entry.ItemProviderEntry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntry<T extends Block>
extends ItemProviderEntry<T> {
    public BlockEntry(ResourceLocation id, T value) {
        super(id, value);
    }

    public BlockState getDefaultState() {
        return ((Block)this.value).defaultBlockState();
    }

    public boolean has(BlockState state) {
        return state.is(((Block)this.value));
    }
}

