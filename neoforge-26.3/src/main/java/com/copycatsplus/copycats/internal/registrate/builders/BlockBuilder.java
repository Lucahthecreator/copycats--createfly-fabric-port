package com.copycatsplus.copycats.internal.registrate.builders;

import com.copycatsplus.copycats.internal.registrate.AbstractRegistrate;
import com.copycatsplus.copycats.internal.registrate.providers.DataGenContext;
import com.copycatsplus.copycats.internal.registrate.providers.RegistrateBlockstateProvider;
import com.copycatsplus.copycats.internal.registrate.providers.loot.RegistrateBlockLootTables;
import com.copycatsplus.copycats.internal.registrate.util.entry.BlockEntry;
import com.copycatsplus.copycats.internal.registrate.util.entry.ItemEntry;
import com.copycatsplus.copycats.internal.registrate.util.nullness.NonNullBiConsumer;
import com.copycatsplus.copycats.internal.registrate.util.nullness.NonNullConsumer;
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
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class BlockBuilder<T extends Block, P> implements Builder<Block, T, P, BlockBuilder<T, P>> {
    protected final AbstractRegistrate<?> owner;
    protected final P parent;
    protected final String name;
    protected final Function<Properties, T> factory;
    protected Supplier<Properties> initialProperties = Properties::of;
    protected UnaryOperator<Properties> properties = UnaryOperator.identity();
    protected final List<NonNullConsumer<? super T>> callbacks = new ArrayList<>();
    protected BlockEntry<T> entry;

    public BlockBuilder(AbstractRegistrate<?> owner, P parent, String name, Function<Properties, T> factory) {
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

    public BlockBuilder<T, P> initialProperties(Supplier<Properties> properties) {
        this.initialProperties = properties;
        return this;
    }

    public BlockBuilder<T, P> properties(UnaryOperator<Properties> transform) {
        UnaryOperator<Properties> previous = this.properties;
        this.properties = value -> transform.apply(previous.apply(value));
        return this;
    }

    public BlockBuilder<T, P> onRegister(NonNullConsumer<? super T> callback) {
        this.callbacks.add(callback);
        return this;
    }

    public BlockBuilder<T, P> tag(TagKey<Block>... ignored) {
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

    public BlockBuilder.BlockItemBuilder<T, P> item() {
        return new BlockBuilder.BlockItemBuilder<>(this, BlockItem::new);
    }

    public BlockBuilder.BlockItemBuilder<T, P> item(BiFunction<T, net.minecraft.world.item.Item.Properties, ? extends BlockItem> factory) {
        return new BlockBuilder.BlockItemBuilder<>(this, factory);
    }

    public BlockEntry<T> register() {
        if (this.entry != null) {
            return this.entry;
        } else {
            Identifier id = Identifier.fromNamespaceAndPath(this.owner.getModid(), this.name);
            ResourceKey<Block> key = ResourceKey.create(Registries.BLOCK, id);
            T block = this.factory.apply(this.properties.apply(this.initialProperties.get()).setId(key));
            Registry.register(BuiltInRegistries.BLOCK, key, block);
            this.entry = new BlockEntry<>(id, block);
            this.owner.track(BuiltInRegistries.BLOCK.key(), this.entry);
            this.callbacks.forEach(callback -> callback.accept(block));
            return this.entry;
        }
    }

    public static class BlockItemBuilder<T extends Block, P> {
        private final BlockBuilder<T, P> block;
        private final BiFunction<T, net.minecraft.world.item.Item.Properties, ? extends BlockItem> factory;
        private final List<NonNullConsumer<? super BlockItem>> callbacks = new ArrayList<>();

        private BlockItemBuilder(BlockBuilder<T, P> block, BiFunction<T, net.minecraft.world.item.Item.Properties, ? extends BlockItem> factory) {
            this.block = block;
            this.factory = factory;
        }

        public BlockBuilder.BlockItemBuilder<T, P> onRegister(NonNullConsumer<? super BlockItem> callback) {
            this.callbacks.add(callback);
            return this;
        }

        public BlockBuilder.BlockItemBuilder<T, P> tag(Object... ignored) {
            return this;
        }

        public BlockBuilder.BlockItemBuilder<T, P> model(Object ignored) {
            return this;
        }

        public BlockBuilder.BlockItemBuilder<T, P> transform(UnaryOperator<BlockBuilder.BlockItemBuilder<T, P>> transform) {
            return transform.apply(this);
        }

        public BlockEntry<T> register() {
            BlockEntry<T> entry = this.block.register();
            Identifier id = entry.getId();
            ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, id);
            BlockItem item = this.factory.apply(entry.get(), new net.minecraft.world.item.Item.Properties().setId(key));
            Registry.register(BuiltInRegistries.ITEM, key, item);
            this.block.owner.track(BuiltInRegistries.ITEM.key(), new ItemEntry<>(id, item));
            this.callbacks.forEach(callback -> callback.accept(item));
            return entry;
        }
    }
}
