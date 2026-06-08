/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.zurrtum.create.content.decoration.copycat.CopycatBlock
 *  javax.annotation.Nullable
 *  javax.annotation.ParametersAreNonnullByDefault
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.world.level.BlockAndLightGetter
 *  net.minecraft.world.level.block.state.BlockBehaviour$Properties
 *  net.minecraft.world.level.block.state.BlockState
 *  org.jetbrains.annotations.ApiStatus$Internal
 */
package com.copycatsplus.copycats.foundation.copycat;

import com.copycatsplus.copycats.foundation.copycat.ICopycatBlock;
import com.zurrtum.create.content.decoration.copycat.CopycatBlock;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndLightGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.ApiStatus;

@ParametersAreNonnullByDefault
@ApiStatus.Internal
public final class WrappedCopycatBlock
extends CopycatBlock {
    private final ThreadLocal<ICopycatBlock> wrapped = new ThreadLocal();

    public WrappedCopycatBlock(BlockBehaviour.Properties pProperties) {
        super(pProperties);
    }

    public ICopycatBlock getWrapped() {
        return this.wrapped.get();
    }

    public void setWrapped(ICopycatBlock wrapped) {
        this.wrapped.set(wrapped);
    }

    public boolean isIgnoredConnectivitySide(BlockAndLightGetter reader, BlockState state, Direction face, BlockPos fromPos, @Nullable BlockPos toPos) {
        return false;
    }

    public boolean canConnectTexturesToward(BlockAndLightGetter reader, BlockPos fromPos, BlockPos toPos, BlockState state) {
        return true;
    }
}

