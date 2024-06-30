package com.copycatsplus.copycats.utility;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class NBTUtils {

    @ExpectPlatform
    @NotNull
    public static CompoundTag serializeStack(ItemStack stack) {
        //noinspection DataFlowIssue
        return null;
    }
}
