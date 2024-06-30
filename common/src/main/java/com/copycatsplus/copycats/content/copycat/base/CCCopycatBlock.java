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
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("deprecation")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public abstract class CCCopycatBlock extends Block implements IBE<CCCopycatBlockEntity>, ICopycatBlock {

    public CCCopycatBlock(Properties pProperties) {
        super(pProperties);
    }

    @Nullable
    @Override
    public <S extends BlockEntity> BlockEntityTicker<S> getTicker(Level level, BlockState state, BlockEntityType<S> type) {
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
    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(worldIn, pos, state, placer, stack);
        ICopycatBlock.super.setPlacedBy(worldIn, pos, state, placer, stack);
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        super.onRemove(state, world, pos, newState, isMoving);
        ICopycatBlock.super.onRemove(state, world, pos, newState, isMoving);
    }

    @Override
    public void playerWillDestroy(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull Player player) {
        super.playerWillDestroy(level, pos, state, player);
        ICopycatBlock.super.playerWillDestroy(level, pos, state, player);
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

    @Environment(EnvType.CLIENT)
    public static BlockColor wrappedColor() {
        return ICopycatBlock.wrappedColor();
    }
}
