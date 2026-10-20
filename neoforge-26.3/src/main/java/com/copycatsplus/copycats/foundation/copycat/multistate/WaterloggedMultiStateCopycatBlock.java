package com.copycatsplus.copycats.foundation.copycat.multistate;

import com.zurrtum.create.foundation.block.ProperWaterloggedBlock;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.NotNull;

@ParametersAreNonnullByDefault
public abstract class WaterloggedMultiStateCopycatBlock extends MultiStateCopycatBlock implements ProperWaterloggedBlock {
    public WaterloggedMultiStateCopycatBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(WATERLOGGED, false));
    }

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder.add(WATERLOGGED));
    }

    @Override
    public FluidState getFluidState(BlockState pState) {
        return this.fluidState(pState);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.withWater(super.getStateForPlacement(pContext), pContext);
    }

    @NotNull
    @Override
    public BlockState updateShape(
        @NotNull BlockState pState,
        @NotNull LevelReader pLevel,
        @NotNull ScheduledTickAccess tickView,
        @NotNull BlockPos pCurrentPos,
        @NotNull Direction pDirection,
        @NotNull BlockPos pNeighborPos,
        @NotNull BlockState pNeighborState,
        @NotNull RandomSource random
    ) {
        super.updateShape(pState, pLevel, tickView, pCurrentPos, pDirection, pNeighborPos, pNeighborState, random);
        this.updateWater(pLevel, tickView, pState, pCurrentPos);
        return pState;
    }
}
