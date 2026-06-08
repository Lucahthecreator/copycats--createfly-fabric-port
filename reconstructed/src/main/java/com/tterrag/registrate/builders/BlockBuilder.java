/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Registry
 *  net.minecraft.core.registries.BuiltInRegistries
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.resources.Identifier
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.tags.TagKey
 *  net.minecraft.world.item.BlockItem
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.state.BlockBehaviour$Properties
 */
package com.tterrag.registrate.builders;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.builders.Builder;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class BlockBuilder<T extends Block, P>
implements Builder<Block, T, P, BlockBuilder<T, P>> {
    protected final AbstractRegistrate<?> owner;
    protected final P parent;
    protected final String name;
    protected final Function<BlockBehaviour.Properties, T> factory;
    protected Supplier<BlockBehaviour.Properties> initialProperties = BlockBehaviour.Properties::of;
    protected UnaryOperator<BlockBehaviour.Properties> properties = UnaryOperator.identity();
    protected final List<NonNullConsumer<? super T>> callbacks = new ArrayList<NonNullConsumer<? super T>>();
    protected BlockEntry<T> entry;

    public BlockBuilder(AbstractRegistrate<?> owner, P parent, String name, Function<BlockBehaviour.Properties, T> factory) {
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

    public BlockBuilder<T, P> initialProperties(Supplier<BlockBehaviour.Properties> properties) {
        this.initialProperties = properties;
        return this;
    }

    public BlockBuilder<T, P> properties(UnaryOperator<BlockBehaviour.Properties> transform) {
        UnaryOperator<BlockBehaviour.Properties> previous = this.properties;
        this.properties = value -> (BlockBehaviour.Properties)transform.apply((BlockBehaviour.Properties)previous.apply((BlockBehaviour.Properties)value));
        return this;
    }

    public BlockBuilder<T, P> onRegister(NonNullConsumer<? super T> callback) {
        this.callbacks.add(callback);
        return this;
    }

    public BlockBuilder<T, P> tag(TagKey<Block> ... ignored) {
        return this;
    }

    public BlockBuilder<T, P> addLayer(Supplier<?> ignored) {
        return this;
    }

    public BlockBuilder<T, P> blockstate(NonNullBiConsumer<DataGenContext<Block, ? extends T>, RegistrateBlockstateProvider> ignored) {
        return this;
    }

    public BlockBuilder<T, P> loot(NonNullBiConsumer<RegistrateBlockLootTables, T> ignored) {
        return this;
    }

    public BlockItemBuilder<T, P> item() {
        return new BlockItemBuilder(this, BlockItem::new);
    }

    public BlockItemBuilder<T, P> item(BiFunction<T, Item.Properties, ? extends BlockItem> factory) {
        return new BlockItemBuilder(this, factory);
    }

    public BlockEntry<T> register() {
        if (this.entry != null) {
            return this.entry;
        }
        Identifier id = Identifier.fromNamespaceAndPath((String)this.owner.getModid(), (String)this.name);
        ResourceKey key = ResourceKey.create((ResourceKey)Registries.BLOCK, (Identifier)id);
        Block block = (Block)this.factory.apply(((BlockBehaviour.Properties)this.properties.apply(this.initialProperties.get())).setId(key));
        Registry.register((Registry)BuiltInRegistries.BLOCK, (ResourceKey)key, (Object)block);
        this.entry = new BlockEntry<Block>(id, block);
        this.owner.track(BuiltInRegistries.BLOCK.key(), this.entry);
        this.callbacks.forEach(callback -> callback.accept(block));
        return this.entry;
    }

    public static class BlockItemBuilder<T extends Block, P> {
        private final BlockBuilder<T, P> block;
        private final BiFunction<T, Item.Properties, ? extends BlockItem> factory;
        private final List<NonNullConsumer<? super BlockItem>> callbacks = new ArrayList<NonNullConsumer<? super BlockItem>>();

        private BlockItemBuilder(BlockBuilder<T, P> block, BiFunction<T, Item.Properties, ? extends BlockItem> factory) {
            this.block = block;
            this.factory = factory;
        }

        public BlockItemBuilder<T, P> onRegister(NonNullConsumer<? super BlockItem> callback) {
            this.callbacks.add(callback);
            return this;
        }

        public BlockItemBuilder<T, P> tag(Object ... ignored) {
            return this;
        }

        public BlockItemBuilder<T, P> model(Object ignored) {
            return this;
        }

        public BlockItemBuilder<T, P> transform(UnaryOperator<BlockItemBuilder<T, P>> transform) {
            return (BlockItemBuilder)transform.apply(this);
        }

        public BlockEntry<T> register() {
            BlockEntry<T> entry = this.block.register();
            Identifier id = entry.getId();
            ResourceKey key = ResourceKey.create((ResourceKey)Registries.ITEM, (Identifier)id);
            BlockItem item = this.factory.apply((Block)entry.get(), new Item.Properties().setId(key));
            Registry.register((Registry)BuiltInRegistries.ITEM, (ResourceKey)key, (Object)item);
            this.block.owner.track(BuiltInRegistries.ITEM.key(), new ItemEntry<BlockItem>(id, item));
            this.callbacks.forEach(callback -> callback.accept(item));
            return entry;
        }
    }
}

