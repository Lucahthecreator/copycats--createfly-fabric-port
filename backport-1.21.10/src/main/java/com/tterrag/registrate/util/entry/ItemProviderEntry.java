/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.ItemLike
 */
package com.tterrag.registrate.util.entry;

import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

public class ItemProviderEntry<T extends ItemLike>
extends RegistryEntry<T>
implements ItemLike {
    public ItemProviderEntry(ResourceLocation id, T value) {
        super(id, value);
    }

    public Item asItem() {
        return ((ItemLike)this.value).asItem();
    }

    public ItemStack asStack() {
        return new ItemStack((ItemLike)this.asItem());
    }
}

