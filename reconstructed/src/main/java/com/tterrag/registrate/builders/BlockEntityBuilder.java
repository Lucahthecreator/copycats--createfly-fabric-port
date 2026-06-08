/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Registry
 *  net.minecraft.core.registries.BuiltInRegistries
 *  net.minecraft.resources.Identifier
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.entity.BlockEntityType
 *  net.minecraft.world.level.block.state.BlockState
 */
package com.tterrag.registrate.builders;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.builders.Builder;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityBuilder<T extends BlockEntity, P>
implements Builder<BlockEntityType<?>, BlockEntityType<T>, P, BlockEntityBuilder<T, P>> {
    protected final AbstractRegistrate<?> owner;
    protected final P parent;
    protected final String name;
    protected final BlockEntityFactory<T> factory;
    protected final List<Block> validBlocks = new ArrayList<Block>();

    public BlockEntityBuilder(AbstractRegistrate<?> owner, P parent, String name, BlockEntityFactory<T> factory) {
        this.owner = owner;
        this.parent = parent;
        this.name = name;
        this.factory = factory;
    }

    @Override
    public AbstractRegistrate<?> getOwner() {
        return this.owner;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @SafeVarargs
    public final BlockEntityBuilder<T, P> validBlocks(BlockEntry<? extends Block> ... blocks) {
        for (BlockEntry<? extends Block> block : blocks) {
            this.validBlocks.add((Block)block.get());
        }
        return this;
    }

    public BlockEntityBuilder<T, P> renderer(Object ignored) {
        return this;
    }

    public BlockEntityEntry<T> register() {
        BlockEntityType[] reference;
        Identifier id = Identifier.fromNamespaceAndPath((String)this.owner.getModid(), (String)this.name);
        BlockEntityType type = new BlockEntityType((pos, state) -> this.factory.create(reference[0], pos, state), Set.copyOf(this.validBlocks));
        reference = new BlockEntityType[]{type};
        Registry.register((Registry)BuiltInRegistries.BLOCK_ENTITY_TYPE, (Identifier)id, (Object)type);
        BlockEntityEntry entry = new BlockEntityEntry(id, type);
        this.owner.track(BuiltInRegistries.BLOCK_ENTITY_TYPE.key(), entry);
        return entry;
    }

    @FunctionalInterface
    public static interface BlockEntityFactory<T extends BlockEntity> {
        public T create(BlockEntityType<T> var1, BlockPos var2, BlockState var3);
    }
}

