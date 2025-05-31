package com.copycatsplus.copycats.foundation.copycat.model.neoforge;

import com.copycatsplus.copycats.foundation.copycat.model.ScaledBlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.BlockAndTintGetter;
import net.neoforged.neoforge.client.model.data.ModelDataManager;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class ScaledBlockAndTintGetterForge extends ScaledBlockAndTintGetter {

    public ScaledBlockAndTintGetterForge(String renderingProperty, BlockAndTintGetter wrapped, BlockPos origin, Vec3i originInner, Vec3i scale, Predicate<BlockPos> filter) {
        super(renderingProperty, wrapped, origin, originInner, scale, filter);
    }
}
