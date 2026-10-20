package com.copycatsplus.copycats.internal.registrate.util.entry;

import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntry<T extends Block> extends ItemProviderEntry<T> {
    public BlockEntry(Identifier id, T value) {
        super(id, value);
    }

    public BlockState getDefaultState() {
        return this.value.defaultBlockState();
    }

    public boolean has(BlockState state) {
        return state.is(this.value);
    }
}
