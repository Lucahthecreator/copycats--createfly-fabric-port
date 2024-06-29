package com.copycatsplus.copycats.content.copycat.base;

import com.copycatsplus.copycats.utility.InteractionUtils;
import com.simibubi.create.AllTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public abstract class WaterloggedCopycatWrappedBlock<W extends Block> extends CCWaterloggedCopycatBlock implements ICopycatWithWrappedBlock<W> {

    public WaterloggedCopycatWrappedBlock(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull Player pPlayer, @NotNull InteractionHand pHand, @NotNull BlockHitResult pHit) {
        return InteractionUtils.sequential(
                () -> super.use(pState, pLevel, pPos, pPlayer, pHand, pHit),
                () -> getWrappedBlock().use(pState, pLevel, pPos, pPlayer, pHand, pHit)
        );
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext pContext) {
        BlockState state = getWrappedBlock().getStateForPlacement(pContext);
        if (state == null) return super.getStateForPlacement(pContext);
        return copyState(state, super.getStateForPlacement(pContext), false);
    }

    public abstract BlockState copyState(BlockState from, BlockState to, boolean includeWaterlogged);
}
