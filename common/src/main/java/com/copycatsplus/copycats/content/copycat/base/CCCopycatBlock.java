package com.copycatsplus.copycats.content.copycat.base;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.copycatsplus.copycats.CCBlockEntityTypes;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

@SuppressWarnings("deprecation")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public abstract class CCCopycatBlock extends Block implements IBE<CCCopycatBlockEntity>, IWrenchable, ICopycatBlock {

    public CCCopycatBlock(Properties pProperties) {
        super(pProperties);
    }

    @Nullable
    @Override
    public <S extends BlockEntity> BlockEntityTicker<S> getTicker(Level p_153212_, BlockState p_153213_,
                                                                  BlockEntityType<S> p_153214_) {
        return null;
    }

    @Override
    public InteractionResult use(BlockState state,
                                 Level level,
                                 BlockPos pos,
                                 Player player,
                                 InteractionHand hand,
                                 BlockHitResult hit) {
        return ICopycatBlock.super.use(state, level, pos, player, hand, hit);
    }

    @Override
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, LivingEntity pPlacer, ItemStack pStack) {
        ICopycatBlock.super.setPlacedBy(pLevel, pPos, pState, pPlacer, pStack);
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        ICopycatBlock.super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }

    @Override
    public void playerWillDestroy(Level pLevel, BlockPos pPos, BlockState pState, Player pPlayer) {
        ICopycatBlock.super.playerWillDestroy(pLevel, pPos, pState, pPlayer);
    }

    @Override
    public Class<CCCopycatBlockEntity> getBlockEntityClass() {
        return CCCopycatBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends CCCopycatBlockEntity> getBlockEntityType() {
        return CCBlockEntityTypes.COPYCAT.get();
    }

    public static BlockState getMaterial(BlockGetter level, BlockPos pos) {
        return ICopycatBlock.getMaterial(level, pos);
    }

    // Connected Textures

    public BlockState getAppearance(BlockState state, BlockAndTintGetter level, BlockPos pos, Direction side,
                                    BlockState queryState, BlockPos queryPos) {
        if (isIgnoredConnectivitySide(level, state, side, pos, queryPos))
            return state;

        return multiPlatformGetAppearance(this, state, level, pos, side, queryState, queryPos);
    }

    @ExpectPlatform
    public static BlockState multiPlatformGetAppearance(ICopycatBlock block, BlockState state, BlockAndTintGetter level, BlockPos pos, Direction side,
                                                        BlockState queryState, BlockPos queryPos) {
        //noinspection DataFlowIssue
        return null;
    }

    @Environment(EnvType.CLIENT)
    public static BlockColor wrappedColor() {
        return ICopycatBlock.wrappedColor();
    }
}
