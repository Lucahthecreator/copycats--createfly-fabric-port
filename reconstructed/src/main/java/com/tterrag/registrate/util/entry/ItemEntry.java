/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.resources.Identifier
 *  net.minecraft.world.item.Item
 */
package com.tterrag.registrate.util.entry;

import com.tterrag.registrate.util.entry.ItemProviderEntry;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;

public class ItemEntry<T extends Item>
extends ItemProviderEntry<T> {
    public ItemEntry(Identifier id, T value) {
        super(id, value);
    }
}

