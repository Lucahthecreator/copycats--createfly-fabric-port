package com.copycatsplus.copycats.utility.neoforge;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class NBTUtilsImpl {

    public static CompoundTag serializeStack(ItemStack stack, HolderLookup.Provider registries) {
        return (CompoundTag) stack.saveOptional(registries);
    }
}
