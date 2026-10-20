package com.copycatsplus.copycats.internal.registrate;

import com.copycatsplus.copycats.internal.registrate.builders.BlockBuilder;
import com.copycatsplus.copycats.internal.registrate.builders.BlockEntityBuilder;
import com.copycatsplus.copycats.internal.registrate.builders.ItemBuilder;
import com.copycatsplus.copycats.internal.registrate.util.entry.RegistryEntry;
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
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class AbstractRegistrate<S extends AbstractRegistrate<S>> {
    private final String modid;
    private final Map<ResourceKey<?>, Collection<RegistryEntry<?>>> entries = new HashMap<>();

    protected AbstractRegistrate(String modid) {
        this.modid = modid;
    }

    public String getModid() {
        return this.modid;
    }

    public <T extends Block> BlockBuilder<T, S> block(String name, Function<Properties, T> factory) {
        return new BlockBuilder<>(this, this.self(), name, factory);
    }

    public <T extends Item> ItemBuilder<T, S> item(String name, Function<net.minecraft.world.item.Item.Properties, T> factory) {
        return new ItemBuilder<>(this, this.self(), name, factory);
    }

    public <T extends BlockEntity> BlockEntityBuilder<T, S> blockEntity(String name, BlockEntityBuilder.BlockEntityFactory<T> factory) {
        return new BlockEntityBuilder<>(this, this.self(), name, factory);
    }

    protected S self() {
        return (S)this;
    }

    public void track(ResourceKey<?> registry, RegistryEntry<?> entry) {
        this.entries.computeIfAbsent(registry, $ -> new ArrayList<>()).add(entry);
    }

    public <R> Collection<RegistryEntry<R>> getAll(ResourceKey<? extends Registry<R>> registry) {
        return (Collection<RegistryEntry<R>>)(Collection<?>)this.entries.getOrDefault(registry, List.of());
    }

    public void register() {
    }

    public void setTooltipModifierFactory(Function<?, ?> ignored) {
    }
}
