package com.copycatsplus.copycats.internal.registrate.util.entry;

import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

public class ItemProviderEntry<T extends ItemLike> extends RegistryEntry<T> implements ItemLike {
    public ItemProviderEntry(Identifier id, T value) {
        super(id, value);
    }

    @Override
    public Item asItem() {
        return this.value.asItem();
    }

    public ItemStack asStack() {
        return new ItemStack(this.asItem());
    }
}
