/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.block.BlockAndTintGetter
 *  net.minecraft.core.BlockPos
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

import java.util.function.Predicate;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
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
public class FilteredBlockAndTintGetter
implements BlockAndTintGetter {
    public final BlockAndTintGetter wrapped;
    public final Predicate<BlockPos> filter;

    public FilteredBlockAndTintGetter(BlockAndTintGetter wrapped, Predicate<BlockPos> filter) {
        this.wrapped = wrapped;
        this.filter = filter;
    }

    public BlockEntity getBlockEntity(@NotNull BlockPos pPos) {
        return this.filter.test(pPos) ? this.wrapped.getBlockEntity(pPos) : null;
    }

    @NotNull
    public BlockState getBlockState(@NotNull BlockPos pPos) {
        return this.filter.test(pPos) ? this.wrapped.getBlockState(pPos) : Blocks.AIR.defaultBlockState();
    }

    @NotNull
    public FluidState getFluidState(@NotNull BlockPos pPos) {
        return this.filter.test(pPos) ? this.wrapped.getFluidState(pPos) : Fluids.EMPTY.defaultFluidState();
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

