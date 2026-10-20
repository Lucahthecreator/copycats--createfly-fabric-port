/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.level.block.entity.BlockEntity
 */
package com.zurrtum.create.foundation.data;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.builders.BlockEntityBuilder;
import net.minecraft.world.level.block.entity.BlockEntity;

public class CreateBlockEntityBuilder<T extends BlockEntity, P>
extends BlockEntityBuilder<T, P> {
    public CreateBlockEntityBuilder(AbstractRegistrate<?> owner, P parent, String name, BlockEntityBuilder.BlockEntityFactory<T> factory) {
        super(owner, parent, name, factory);
    }
}

