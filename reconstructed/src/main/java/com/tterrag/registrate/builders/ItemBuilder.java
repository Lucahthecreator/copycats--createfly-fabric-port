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
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$Properties
 */
package com.tterrag.registrate.builders;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.builders.Builder;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
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

public class ItemBuilder<T extends Item, P>
implements Builder<Item, T, P, ItemBuilder<T, P>> {
    private final AbstractRegistrate<?> owner;
    private final String name;
    private final Function<Item.Properties, T> factory;
    private final List<NonNullConsumer<? super T>> callbacks = new ArrayList<NonNullConsumer<? super T>>();

    public ItemBuilder(AbstractRegistrate<?> owner, P parent, String name, Function<Item.Properties, T> factory) {
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

    public ItemBuilder<T, P> tag(TagKey<Item> ... ignored) {
        return this;
    }

    public ItemEntry<T> register() {
        Identifier id = Identifier.fromNamespaceAndPath((String)this.owner.getModid(), (String)this.name);
        ResourceKey key = ResourceKey.create((ResourceKey)Registries.ITEM, (Identifier)id);
        Item item = (Item)this.factory.apply(new Item.Properties().setId(key));
        Registry.register((Registry)BuiltInRegistries.ITEM, (ResourceKey)key, (Object)item);
        ItemEntry<Item> entry = new ItemEntry<Item>(id, item);
        this.owner.track(BuiltInRegistries.ITEM.key(), entry);
        this.callbacks.forEach(callback -> callback.accept(item));
        return entry;
    }
}

