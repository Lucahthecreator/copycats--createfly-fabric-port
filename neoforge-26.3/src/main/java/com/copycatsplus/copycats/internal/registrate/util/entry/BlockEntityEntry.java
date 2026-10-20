package com.copycatsplus.copycats.internal.registrate.util.entry;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityEntry<T extends BlockEntity> extends RegistryEntry<BlockEntityType<T>> {
    public BlockEntityEntry(Identifier id, BlockEntityType<T> value) {
        super(id, value);
    }

    public T create(BlockPos pos, BlockState state) {
        return this.value.create(pos, state);
    }
}
