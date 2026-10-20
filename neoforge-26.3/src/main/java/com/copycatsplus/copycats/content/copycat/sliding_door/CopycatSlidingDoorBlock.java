package com.copycatsplus.copycats.content.copycat.sliding_door;

import com.copycatsplus.copycats.CCBlockEntityTypes;
import com.copycatsplus.copycats.content.copycat.door.CopycatDoorBlock;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlockEntity;
import com.copycatsplus.copycats.utility.BlockEntityUtils;
import com.copycatsplus.copycats.utility.InteractionUtils;
import com.zurrtum.create.AllBlocks;
import com.zurrtum.create.content.decoration.slidingDoor.SlidingDoorBlock;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ParametersAreNonnullByDefault
public class CopycatSlidingDoorBlock extends SlidingDoorBlock implements ICopycatBlock {
    public static BooleanProperty CT = CopycatDoorBlock.CT;

    public CopycatSlidingDoorBlock(Properties properties, BlockSetType type, boolean folds) {
        super(properties, type, folds);
        this.registerDefaultState(this.defaultBlockState().setValue(CT, true));
    }

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder.add(CT));
    }

    public static CopycatSlidingDoorBlock metal(Properties properties, boolean folds) {
        return new CopycatSlidingDoorBlock(properties, TRAIN_SET_TYPE.get(), folds);
    }

    @Override
    public InteractionResult onSneakWrenched(BlockState state, UseOnContext context) {
        return InteractionUtils.sequential(() -> ICopycatBlock.super.onSneakWrenched(state, context), () -> super.onSneakWrenched(state, context));
    }

    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        return InteractionUtils.sequential(() -> ICopycatBlock.super.onWrenched(state, context), () -> super.onWrenched(state, context));
    }

    @Override
    protected InteractionResult useItemOn(
        ItemStack heldStack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult ray
    ) {
        return InteractionUtils.sequential(
            () -> ICopycatBlock.super.use(state, world, pos, player, hand, ray), () -> super.useItemOn(heldStack, state, world, pos, player, hand, ray)
        );
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        InteractionResult result = super.useWithoutItem(state, level, pos, player, hit);
        if (result.consumesAction()) {
            BlockPos lowerPos = state.getValue(HALF) == DoubleBlockHalf.LOWER ? pos : pos.below();
            BlockEntityUtils.redrawAt(level, lowerPos);
        }

        return result;
    }

    @Override
    public InteractionResult toggleCT(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (!pPlayer.isShiftKeyDown() || !pPlayer.getItemInHand(pHand).equals(ItemStack.EMPTY)) {
            return InteractionResult.PASS;
        } else if (!this.canToggleCT(pState, pLevel, pPos)) {
            return InteractionResult.PASS;
        } else {
            pLevel.setBlock(pPos, pState.cycle(CT), 3);
            if (pLevel.getBlockEntity(pPos) instanceof ICopycatBlockEntity fbe) {
                BlockEntityUtils.redraw((BlockEntity)fbe);
                return InteractionResult.SUCCESS;
            } else {
                return InteractionResult.PASS;
            }
        }
    }

    @Nullable
    @Override
    public BlockState getAcceptedBlockState(Level pLevel, BlockPos pPos, ItemStack item, Direction face) {
        return ICopycatBlock.super.getAcceptedBlockState(pLevel, pPos, item, face);
    }

    @Override
    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(worldIn, pos, state, placer, stack);
        ICopycatBlock.super.setPlacedBy(worldIn, pos, state, placer, stack);
    }

    @Override
    public BlockState playerWillDestroy(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull Player player) {
        ICopycatBlock.super.playerWillDestroy(level, pos, state, player);
        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public boolean isIgnoredConnectivitySide(
        BlockAndTintGetter reader, BlockState state, Direction face, BlockPos fromPos, @Nullable BlockPos toPos, @Nullable BlockState toState
    ) {
        return true;
    }

    @Override
    public boolean canConnectTexturesToward(BlockAndTintGetter reader, BlockPos fromPos, BlockPos toPos, BlockState state) {
        return false;
    }

    public boolean supportsExternalFaceHiding(BlockState state) {
        return true;
    }

    public boolean hidesNeighborFace(BlockGetter level, BlockPos pos, BlockState state, BlockState neighborState, Direction dir) {
        BlockPos toPos = pos.relative(dir);
        BlockState toState = level.getBlockState(toPos);
        BlockState material = state.getBlock() instanceof ICopycatBlock ? ICopycatBlock.getMaterial(level, pos) : state;
        BlockState neighborMaterial = neighborState.getBlock() instanceof ICopycatBlock ? ICopycatBlock.getMaterial(level, toPos) : neighborState;
        if (neighborMaterial.is(AllBlocks.COPYCAT_BASE) && material.is(AllBlocks.COPYCAT_BASE)) {
            if (dir == Direction.UP && toState.is(this) && toState.getValue(HALF) == DoubleBlockHalf.UPPER) {
                return true;
            }

            if (dir == Direction.DOWN && toState.is(this) && toState.getValue(HALF) == DoubleBlockHalf.LOWER) {
                return true;
            }
        }

        return ICopycatBlock.hidesNeighborFace(level, pos, state, neighborState, dir);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return state.getValue(HALF) == DoubleBlockHalf.UPPER ? null : this.getBlockEntityType().create(pos, state);
    }

    @Override
    public BlockEntityType<? extends CopycatSlidingDoorBlockEntity> getBlockEntityType() {
        return CCBlockEntityTypes.COPYCAT_SLIDING_DOOR.get();
    }
}
