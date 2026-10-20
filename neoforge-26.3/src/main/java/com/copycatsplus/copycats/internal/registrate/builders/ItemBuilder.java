package com.copycatsplus.copycats.internal.registrate.builders;

import com.copycatsplus.copycats.internal.registrate.AbstractRegistrate;
import com.copycatsplus.copycats.internal.registrate.util.entry.ItemEntry;
import com.copycatsplus.copycats.internal.registrate.util.nullness.NonNullConsumer;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;

public class ItemBuilder<T extends Item, P> implements Builder<Item, T, P, ItemBuilder<T, P>> {
    private final AbstractRegistrate<?> owner;
    private final String name;
    private final Function<Properties, T> factory;
    private final List<NonNullConsumer<? super T>> callbacks = new ArrayList<>();

    public ItemBuilder(AbstractRegistrate<?> owner, P parent, String name, Function<Properties, T> factory) {
        this.owner = owner;
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

    public ItemBuilder<T, P> onRegister(NonNullConsumer<? super T> callback) {
        this.callbacks.add(callback);
        return this;
    }

    public ItemBuilder<T, P> model(Object ignored) {
        return this;
    }

    public ItemBuilder<T, P> tag(TagKey<Item>... ignored) {
        return this;
    }

    public ItemEntry<T> register() {
        Identifier id = Identifier.fromNamespaceAndPath(this.owner.getModid(), this.name);
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, id);
        T item = this.factory.apply(new Properties().setId(key));
        Registry.register(BuiltInRegistries.ITEM, key, item);
        ItemEntry<T> entry = new ItemEntry<>(id, item);
        this.owner.track(BuiltInRegistries.ITEM.key(), entry);
        this.callbacks.forEach(callback -> callback.accept(item));
        return entry;
    }
}
