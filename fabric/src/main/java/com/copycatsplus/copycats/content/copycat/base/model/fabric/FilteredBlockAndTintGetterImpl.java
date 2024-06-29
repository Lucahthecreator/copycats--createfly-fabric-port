package com.copycatsplus.copycats.content.copycat.base.model.fabric;

import com.copycatsplus.copycats.content.copycat.base.model.FilteredBlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class FilteredBlockAndTintGetterImpl {

    @NotNull
    public static FilteredBlockAndTintGetter create(BlockAndTintGetter wrapped, Predicate<BlockPos> filter) {
        return new FilteredBlockAndTintGetterFabric(wrapped, filter);
    }
}
