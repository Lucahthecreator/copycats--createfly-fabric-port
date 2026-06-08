/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Registry
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.state.BlockBehaviour$Properties
 */
package com.tterrag.registrate;

import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.builders.BlockEntityBuilder;
import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.util.entry.RegistryEntry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class AbstractRegistrate<S extends AbstractRegistrate<S>> {
    private final String modid;
    private final Map<ResourceKey<?>, Collection<RegistryEntry<?>>> entries = new HashMap();

    protected AbstractRegistrate(String modid) {
        this.modid = modid;
    }

    public String getModid() {
        return this.modid;
    }

    public <T extends Block> BlockBuilder<T, S> block(String name, Function<BlockBehaviour.Properties, T> factory) {
        return new BlockBuilder<T, S>(this, this.self(), name, factory);
    }

    public <T extends Item> ItemBuilder<T, S> item(String name, Function<Item.Properties, T> factory) {
        return new ItemBuilder<T, S>(this, this.self(), name, factory);
    }

    public <T extends BlockEntity> BlockEntityBuilder<T, S> blockEntity(String name, BlockEntityBuilder.BlockEntityFactory<T> factory) {
        return new BlockEntityBuilder<T, S>(this, this.self(), name, factory);
    }

    protected S self() {
        return (S)this;
    }

    public void track(ResourceKey<?> registry, RegistryEntry<?> entry) {
        this.entries.computeIfAbsent(registry, $ -> new ArrayList()).add(entry);
    }

    public <R> Collection<RegistryEntry<R>> getAll(ResourceKey<? extends Registry<R>> registry) {
        return this.entries.getOrDefault(registry, List.of());
    }

    public void register() {
    }

    public void setTooltipModifierFactory(Function<?, ?> ignored) {
    }
}

