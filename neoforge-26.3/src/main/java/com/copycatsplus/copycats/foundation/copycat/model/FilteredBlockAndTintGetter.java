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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public class FilteredBlockAndTintGetter implements BlockAndTintGetter {
    public final BlockAndTintGetter wrapped;
    public final Predicate<BlockPos> filter;

    public FilteredBlockAndTintGetter(BlockAndTintGetter wrapped, Predicate<BlockPos> filter) {
        this.wrapped = wrapped;
        this.filter = filter;
    }

    @Override
    public BlockEntity getBlockEntity(@NotNull BlockPos pPos) {
        return this.filter.test(pPos) ? this.wrapped.getBlockEntity(pPos) : null;
    }

    @NotNull
    @Override
    public BlockState getBlockState(@NotNull BlockPos pPos) {
        return this.filter.test(pPos) ? this.wrapped.getBlockState(pPos) : Blocks.AIR.defaultBlockState();
    }

    @NotNull
    @Override
    public FluidState getFluidState(@NotNull BlockPos pPos) {
        return this.filter.test(pPos) ? this.wrapped.getFluidState(pPos) : Fluids.EMPTY.defaultFluidState();
    }

    @Override
    public int getHeight() {
        return this.wrapped.getHeight();
    }

    @Override
    public int getMinY() {
        return this.wrapped.getMinY();
    }

    @Override
    public CardinalLighting cardinalLighting() {
        return this.wrapped.cardinalLighting();
    }

    @NotNull
    @Override
    public LevelLightEngine getLightEngine() {
        return this.wrapped.getLightEngine();
    }

    @Override
    public int getBlockTint(@NotNull BlockPos pBlockPos, @NotNull ColorResolver pColorResolver) {
        return this.wrapped.getBlockTint(pBlockPos, pColorResolver);
    }
}
