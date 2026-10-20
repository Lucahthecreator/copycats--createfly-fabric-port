package com.copycatsplus.copycats.internal.create.foundation.data;

import com.copycatsplus.copycats.internal.registrate.AbstractRegistrate;
import com.copycatsplus.copycats.internal.registrate.builders.BlockEntityBuilder;
import net.minecraft.world.level.block.entity.BlockEntity;

public class CreateBlockEntityBuilder<T extends BlockEntity, P> extends BlockEntityBuilder<T, P> {
    public CreateBlockEntityBuilder(AbstractRegistrate<?> owner, P parent, String name, BlockEntityBuilder.BlockEntityFactory<T> factory) {
        super(owner, parent, name, factory);
    }
}
