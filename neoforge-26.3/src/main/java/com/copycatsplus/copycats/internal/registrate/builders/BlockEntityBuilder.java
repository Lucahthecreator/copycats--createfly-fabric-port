package com.copycatsplus.copycats.internal.registrate.builders;

import com.copycatsplus.copycats.internal.registrate.AbstractRegistrate;
import com.copycatsplus.copycats.internal.registrate.util.entry.BlockEntityEntry;
import com.copycatsplus.copycats.internal.registrate.util.entry.BlockEntry;
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

public class BlockEntityBuilder<T extends BlockEntity, P> implements Builder<BlockEntityType<?>, BlockEntityType<T>, P, BlockEntityBuilder<T, P>> {
    protected final AbstractRegistrate<?> owner;
    protected final P parent;
    protected final String name;
    protected final BlockEntityBuilder.BlockEntityFactory<T> factory;
    protected final List<Block> validBlocks = new ArrayList<>();

    public BlockEntityBuilder(AbstractRegistrate<?> owner, P parent, String name, BlockEntityBuilder.BlockEntityFactory<T> factory) {
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
    public final BlockEntityBuilder<T, P> validBlocks(BlockEntry<? extends Block>... blocks) {
        for (BlockEntry<? extends Block> block : blocks) {
            this.validBlocks.add(block.get());
        }

        return this;
    }

    public BlockEntityBuilder<T, P> renderer(Object ignored) {
        return this;
    }

    public BlockEntityEntry<T> register() {
        Identifier id = Identifier.fromNamespaceAndPath(this.owner.getModid(), this.name);
        BlockEntityType<T>[] reference = new BlockEntityType[1];
        BlockEntityType<T> type = new BlockEntityType<>((pos, state) -> this.factory.create(reference[0], pos, state), Set.copyOf(this.validBlocks));
        reference[0] = type;
        Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, id, type);
        BlockEntityEntry<T> entry = new BlockEntityEntry<>(id, type);
        this.owner.track(BuiltInRegistries.BLOCK_ENTITY_TYPE.key(), entry);
        return entry;
    }

    @FunctionalInterface
    public interface BlockEntityFactory<T extends BlockEntity> {
        T create(BlockEntityType<T> var1, BlockPos var2, BlockState var3);
    }
}
