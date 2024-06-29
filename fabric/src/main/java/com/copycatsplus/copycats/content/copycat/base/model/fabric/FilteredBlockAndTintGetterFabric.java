package com.copycatsplus.copycats.content.copycat.base.model.fabric;

import com.simibubi.create.content.decoration.copycat.FilteredBlockAndTintGetter;
import net.fabricmc.fabric.api.rendering.data.v1.RenderAttachedBlockView;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

@SuppressWarnings("deprecation")
public class FilteredBlockAndTintGetterFabric extends FilteredBlockAndTintGetter implements RenderAttachedBlockView {
    private final BlockAndTintGetter wrapped;

    public FilteredBlockAndTintGetterFabric(BlockAndTintGetter wrapped, Predicate<BlockPos> filter) {
        super(wrapped, filter);
        this.wrapped = wrapped;
    }

    @Deprecated
    @Nullable
    public Object getBlockEntityRenderAttachment(BlockPos pos) {
        return wrapped.getBlockEntityRenderData(pos);
    }
}
