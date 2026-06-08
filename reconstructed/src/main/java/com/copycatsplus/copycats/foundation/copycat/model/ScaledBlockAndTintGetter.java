/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.block.BlockAndTintGetter
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Vec3i
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.CardinalLighting
 *  net.minecraft.world.level.ColorResolver
 *  net.minecraft.world.level.block.Blocks
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.lighting.LevelLightEngine
 *  net.minecraft.world.level.material.FluidState
 *  net.minecraft.world.level.material.Fluids
 *  org.jetbrains.annotations.ApiStatus$Internal
 *  org.jetbrains.annotations.NotNull
 */
package com.copycatsplus.copycats.foundation.copycat.model;

import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlock;
import java.util.function.Predicate;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.CardinalLighting;
import net.minecraft.world.level.ColorResolver;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.Internal
public class ScaledBlockAndTintGetter
implements BlockAndTintGetter {
    protected final String renderingProperty;
    protected final BlockAndTintGetter wrapped;
    protected final BlockPos origin;
    protected final Vec3i originInner;
    protected final Vec3i scale;
    protected final Predicate<BlockPos> filter;

    public ScaledBlockAndTintGetter(String renderingProperty, BlockAndTintGetter wrapped, BlockPos origin, Vec3i originInner, Vec3i scale, Predicate<BlockPos> filter) {
        this.renderingProperty = renderingProperty;
        this.wrapped = wrapped;
        this.origin = origin;
        this.originInner = originInner;
        this.scale = scale;
        this.filter = filter;
    }

    public String getRenderingProperty() {
        return this.renderingProperty;
    }

    public String getPropertyForRender(BlockState state, BlockPos pos) {
        BlockPos truePos = this.getTruePos(pos);
        Vec3i inner = this.getInner(pos);
        return ((IMultiStateCopycatBlock)state.getBlock()).getPropertyFromRender(this.getRenderingProperty(), state, (BlockGetter)this, inner, truePos);
    }

    public BlockAndTintGetter getWrapped() {
        return this.wrapped;
    }

    public Vec3i getScale() {
        return this.scale;
    }

    public BlockPos getTruePos(BlockPos pos) {
        return new BlockPos(this.origin.getX() + (int)Math.floor((double)(pos.getX() + this.originInner.getX() - this.origin.getX()) / (double)this.scale.getX()), this.origin.getY() + (int)Math.floor((double)(pos.getY() + this.originInner.getY() - this.origin.getY()) / (double)this.scale.getY()), this.origin.getZ() + (int)Math.floor((double)(pos.getZ() + this.originInner.getZ() - this.origin.getZ()) / (double)this.scale.getZ()));
    }

    public BlockPos getScaledPos(BlockPos pos) {
        return new BlockPos((pos.getX() - this.origin.getX()) * this.scale.getX() + this.origin.getX(), (pos.getY() - this.origin.getY()) * this.scale.getY() + this.origin.getY(), (pos.getZ() - this.origin.getZ()) * this.scale.getZ() + this.origin.getZ());
    }

    public Vec3i getInner(BlockPos pos) {
        int x = (pos.getX() - this.origin.getX() + this.originInner.getX()) % this.scale.getX();
        int y = (pos.getY() - this.origin.getY() + this.originInner.getY()) % this.scale.getY();
        int z = (pos.getZ() - this.origin.getZ() + this.originInner.getZ()) % this.scale.getZ();
        if (x < 0) {
            x += this.scale.getX();
        }
        if (y < 0) {
            y += this.scale.getY();
        }
        if (z < 0) {
            z += this.scale.getZ();
        }
        return new Vec3i(x, y, z);
    }

    public BlockEntity getBlockEntity(@NotNull BlockPos pPos) {
        if (!this.filter.test(pPos)) {
            return null;
        }
        BlockPos truePos = this.getTruePos(pPos);
        return this.wrapped.getBlockEntity(truePos);
    }

    @NotNull
    public BlockState getBlockState(@NotNull BlockPos pPos) {
        if (!this.filter.test(pPos)) {
            return Blocks.AIR.defaultBlockState();
        }
        BlockPos truePos = this.getTruePos(pPos);
        return this.wrapped.getBlockState(truePos);
    }

    @NotNull
    public FluidState getFluidState(@NotNull BlockPos pPos) {
        if (!this.filter.test(pPos)) {
            return Fluids.EMPTY.defaultFluidState();
        }
        BlockPos truePos = this.getTruePos(pPos);
        return this.wrapped.getFluidState(truePos);
    }

    public int getHeight() {
        return this.wrapped.getHeight();
    }

    public int getMinY() {
        return this.wrapped.getMinY();
    }

    public CardinalLighting cardinalLighting() {
        return this.wrapped.cardinalLighting();
    }

    @NotNull
    public LevelLightEngine getLightEngine() {
        return this.wrapped.getLightEngine();
    }

    public int getBlockTint(@NotNull BlockPos pBlockPos, @NotNull ColorResolver pColorResolver) {
        return this.wrapped.getBlockTint(pBlockPos, pColorResolver);
    }
}

