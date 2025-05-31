package com.copycatsplus.copycats.foundation.copycat.model.neoforge;

import com.copycatsplus.copycats.foundation.copycat.model.FilteredBlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.neoforged.neoforge.client.model.data.ModelDataManager;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public class FilteredBlockAndTintGetterForge extends FilteredBlockAndTintGetter {
    public FilteredBlockAndTintGetterForge(BlockAndTintGetter wrapped, Predicate<BlockPos> filter) {
        super(wrapped, filter);
    }

}
