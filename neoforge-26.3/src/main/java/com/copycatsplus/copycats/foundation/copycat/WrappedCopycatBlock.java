package com.copycatsplus.copycats.foundation.copycat;

import com.zurrtum.create.content.decoration.copycat.CopycatBlock;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndLightGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import org.jetbrains.annotations.ApiStatus.Internal;

@ParametersAreNonnullByDefault
@Internal
public final class WrappedCopycatBlock extends CopycatBlock {
    private final ThreadLocal<ICopycatBlock> wrapped = new ThreadLocal<>();

    public WrappedCopycatBlock(Properties pProperties) {
        super(pProperties);
    }

    public ICopycatBlock getWrapped() {
        return this.wrapped.get();
    }

    public void setWrapped(ICopycatBlock wrapped) {
        this.wrapped.set(wrapped);
    }

    @Override
    public boolean isIgnoredConnectivitySide(BlockAndLightGetter reader, BlockState state, Direction face, BlockPos fromPos, @Nullable BlockPos toPos) {
        return false;
    }

    @Override
    public boolean canConnectTexturesToward(BlockAndLightGetter reader, BlockPos fromPos, BlockPos toPos, BlockState state) {
        return true;
    }
}
