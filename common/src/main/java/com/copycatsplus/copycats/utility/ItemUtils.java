package com.copycatsplus.copycats.utility;

import dev.architectury.injectables.annotations.ExpectPlatform;
import io.github.fabricators_of_create.porting_lib.util.NBTSerializer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ItemUtils {

    @ExpectPlatform
    @NotNull
    public static ItemStack copyStackWithSize(ItemStack itemStack, int size) {
        //noinspection DataFlowIssue
        return null;
    }

    @ExpectPlatform
    @NotNull
    public static Tag serializeNBT(ItemStack stack) {
        //noinspection DataFlowIssue
        return null;
    }
}
