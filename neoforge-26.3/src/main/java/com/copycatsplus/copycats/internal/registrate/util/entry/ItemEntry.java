package com.copycatsplus.copycats.internal.registrate.util.entry;

import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;

public class ItemEntry<T extends Item> extends ItemProviderEntry<T> {
    public ItemEntry(Identifier id, T value) {
        super(id, value);
    }
}
