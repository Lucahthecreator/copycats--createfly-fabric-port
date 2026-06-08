/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.nbt.Tag
 *  net.minecraft.world.item.ItemStack
 *  org.jetbrains.annotations.NotNull
 */
package com.copycatsplus.copycats.utility;

import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ItemUtils {
    @NotNull
    public static ItemStack copyStackWithSize(ItemStack itemStack, int size) {
        return itemStack.copyWithCount(size);
    }

    @NotNull
    public static Tag serializeNBT(ItemStack stack) {
        throw new UnsupportedOperationException("Legacy ItemStack NBT serialization is unavailable on Minecraft 26.1");
    }
}

