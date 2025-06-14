package com.copycatsplus.copycats.mixin.copycat.panel;

import com.copycatsplus.copycats.foundation.copycat.CopycatExternalContext;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlock;
import com.simibubi.create.content.decoration.copycat.CopycatBlockEntity;
import com.simibubi.create.content.decoration.copycat.CopycatPanelBlock;
import com.simibubi.create.content.decoration.copycat.WaterloggedCopycatBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Add CT toggle to Create's {@link CopycatPanelBlock}.
 */
@Mixin(value = CopycatPanelBlock.class, remap = false)
public abstract class CopycatPanelBlockMixin extends WaterloggedCopycatBlock implements ICopycatBlock {
    public CopycatPanelBlockMixin(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public BlockState getAppearance(BlockState state, BlockAndTintGetter level, BlockPos pos, Direction side, @Nullable BlockState queryState, @Nullable BlockPos queryPos) {
        if (!this.isCTEnabled(state, level, queryPos))
            return state;
        return super.getAppearance(state, level, pos, side, queryState, queryPos);
    }

    @Override
    public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        return toggleCT(state, level, pos, player, hitResult);
    }

    @Override
    @Unique
    public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Nullable
    @Override
    @Unique
    public CopycatBlockEntity getBlockEntity(BlockGetter worldIn, BlockPos pos) {
        return super.getBlockEntity(worldIn, pos);
    }

    @Inject(
            method = "isIgnoredConnectivitySide",
            at = @At("RETURN"),
            cancellable = true
    )
    private void isIgnoredConnectivitySide(BlockAndTintGetter reader, BlockState state, Direction face, BlockPos fromPos, BlockPos toPos, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue()) {
            return;
        }
        if (CopycatExternalContext.isForBlockingLogic()) {
            cir.setReturnValue(false);
            return;
        }
        if (toPos == null) {
            cir.setReturnValue(true);
            return;
        }
        cir.setReturnValue(!checkConnection(reader, toPos, fromPos, reader.getBlockState(toPos)));
    }

    @Inject(
            method = "canConnectTexturesToward",
            at = @At("RETURN"),
            cancellable = true
    )
    private void canConnectTexturesToward(BlockAndTintGetter reader, BlockPos fromPos, BlockPos toPos, BlockState fromState, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue()) {
            return;
        }
        BlockState toState = reader.getBlockState(toPos);

        if (toState.getBlock() instanceof ICopycatBlock) {
            cir.setReturnValue(true);
            return;
        }

        cir.setReturnValue(checkConnection(reader, fromPos, toPos, fromState));
    }

    @Override
    @Unique
    public @NotNull BlockState mirror(@NotNull BlockState pState, @NotNull Mirror pMirror) {
        return super.mirror(pState, pMirror);
    }

    @Override
    @Unique
    public @NotNull BlockState rotate(@NotNull BlockState pState, Rotation pRot) {
        return super.rotate(pState, pRot);
    }

    public boolean supportsExternalFaceHiding(BlockState state) {
        return true;
    }

    public boolean hidesNeighborFace(BlockGetter level,
                                     BlockPos pos,
                                     BlockState state,
                                     BlockState neighborState,
                                     Direction dir) {
        return ICopycatBlock.hidesNeighborFace(level, pos, state, neighborState, dir);
    }
}
