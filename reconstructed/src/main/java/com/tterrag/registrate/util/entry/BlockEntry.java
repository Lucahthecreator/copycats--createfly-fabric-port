/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.resources.Identifier
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.state.BlockState
 */
package com.tterrag.registrate.util.entry;

import com.tterrag.registrate.util.entry.ItemProviderEntry;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntry<T extends Block>
extends ItemProviderEntry<T> {
    public BlockEntry(Identifier id, T value) {
        super(id, value);
    }

    public BlockState getDefaultState() {
        return ((Block)this.value).defaultBlockState();
    }

    public boolean has(BlockState state) {
        return state.is((Object)((Block)this.value));
    }
}

